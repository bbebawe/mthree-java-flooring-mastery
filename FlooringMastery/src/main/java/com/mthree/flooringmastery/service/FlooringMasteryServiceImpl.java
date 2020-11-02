/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.dao.AuditDao;
import com.mthree.flooringmastery.dao.DataPersistenceException;
import com.mthree.flooringmastery.dao.OrdersDao;
import com.mthree.flooringmastery.dao.ProductsDao;
import com.mthree.flooringmastery.dao.StatesDao;
import com.mthree.flooringmastery.dto.Order;
import com.mthree.flooringmastery.dto.Product;
import com.mthree.flooringmastery.dto.State;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author beshoy
 */
public class FlooringMasteryServiceImpl implements FlooringMasteryService {

    private ProductsDao productsDao;
    private StatesDao statesDao;
    private OrdersDao ordersDao;
    private AuditDao auditsDao;

    public FlooringMasteryServiceImpl(ProductsDao productDao, StatesDao stateDao, OrdersDao orderDao, AuditDao auditDao) {
        this.productsDao = productDao;
        this.statesDao = stateDao;
        this.ordersDao = orderDao;
        this.auditsDao = auditDao;
    }

    @Override
    public void loadAllData() throws DataPersistenceException {
        // load all data into memory
        productsDao.loadProducts();
        statesDao.loadStates();
        ordersDao.loadOrders();
    }

    @Override
    public List<Order> getDateOrders(LocalDate dateChoice) throws DataPersistenceException, NoOrderFoundException {
        List<Order> allDateOrders = ordersDao.getAllOrders(dateChoice);
        if (allDateOrders.isEmpty()) {
            throw new NoOrderFoundException("Sorry, no orders found for " + dateChoice);
        } else {
            return ordersDao.getAllOrders(dateChoice);
        }
    }

    @Override
    public void saveAllOrders() throws DataPersistenceException {
        ordersDao.saveOrders();
    }

    @Override
    public List<Product> getProductsList() throws DataPersistenceException {
        productsDao.loadProducts();
        return productsDao.getAllProducts();
    }

    @Override
    public List<String> getSatesCodes() throws DataPersistenceException {
        return statesDao.getAllStatesCodes();
    }

    @Override
    public Order calcualteOrder(Order order) throws DataPersistenceException {
        // create new object for calcualted order
        Order calculatedOrder = new Order();
        // get Porudct and State information so it can be used in calculation and to fill other fiels
        Product orderProduct = getOrderProduct(order);
        State orderSate = getOrderState(order);

        // add non calcuated fields to order
        calculatedOrder.setOrderDate(order.getOrderDate());
        calculatedOrder.setCustomerName(order.getCustomerName());
        calculatedOrder.setState(order.getState());
        calculatedOrder.setTaxRate(orderSate.getTaxRate());
        calculatedOrder.setProductType(order.getProductType());
        calculatedOrder.setArea(order.getArea());
        calculatedOrder.setCostPerSquareFoot(orderProduct.getCostPerSquareFoot());
        calculatedOrder.setLaborCostPerSquareFoot(orderProduct.getLaborCostPerSquareFoot());
        // set calcualted fields
        // generate order number and set it to order number
        calculatedOrder.setOrderNumber(generateOrderNumber());
        // calcualte material cost
        calculatedOrder.setMaterialCost(calculateMaterialCost(calculatedOrder));
        // calculate labor cost
        calculatedOrder.setLaborCost(calculateLaborCost(calculatedOrder));
        // calcualte tax
        calculatedOrder.setTax(calculateTax(calculatedOrder));
        // calcualte order total
        calculatedOrder.setTotal(calculateOrderTotal(calculatedOrder));
        return calculatedOrder;
    }

    @Override
    public void saveOrder(Order order) throws DataPersistenceException {
        ordersDao.saveOrder(order);
        //update audit
        auditsDao.writeAuditEntry("Order Number " + order.getOrderNumber() + ", CREATED");
    }

    @Override
    public Order removeOrder(LocalDate orderDate, int orderNumber) throws DataPersistenceException, NoOrderFoundException {
        Order orderToRemove = ordersDao.getOrder(orderDate, orderNumber);
        if (orderToRemove == null) {
            throw new NoOrderFoundException("Invalid order date or number. please try again");
        } else {
            auditsDao.writeAuditEntry("Order Number " + orderToRemove.getOrderNumber() + ", REMOVED");
            return ordersDao.removeOrder(orderToRemove);
        }
    }

    @Override
    public Order getOrder(LocalDate orderDate, int orderNumber) throws DataPersistenceException, NoOrderFoundException {
        Order order = ordersDao.getOrder(orderDate, orderNumber);
        if (order == null) {
            throw new NoOrderFoundException("No order found for the given date or number");
        } else {
            return order;
        }
    }

    @Override
    public void isOrderEdited(Order editedOrder, Order foundOrder) throws NoEditDataExceprtion {
        if (editedOrder.getCustomerName().equals(foundOrder.getCustomerName())
                && editedOrder.getState().equals(foundOrder.getState())
                && editedOrder.getProductType().equals(foundOrder.getProductType())
                && editedOrder.getArea().compareTo(foundOrder.getArea()) == 0) {
            throw new NoEditDataExceprtion("no changes detected, order is not edited");
        }
    }

    @Override
    public void updateOrderCalculations(Order updatedOrder) throws DataPersistenceException {
        // 
        updatedOrder.setTaxRate(getOrderState(updatedOrder).getTaxRate());
        updatedOrder.setCostPerSquareFoot(getOrderProduct(updatedOrder).getCostPerSquareFoot());
        updatedOrder.setLaborCostPerSquareFoot(getOrderProduct(updatedOrder).getLaborCostPerSquareFoot());
        updatedOrder.setMaterialCost(calculateMaterialCost(updatedOrder));
        updatedOrder.setLaborCost(calculateLaborCost(updatedOrder));
        updatedOrder.setTax(calculateTax(updatedOrder));
        updatedOrder.setTotal(calculateOrderTotal(updatedOrder));
    }

    @Override
    public void exportAllData() throws DataPersistenceException {
        ordersDao.exportAllOrders();
        auditsDao.writeAuditEntry("DATA EXPORTED");
    }

    @Override
    public void editOrder(Order editedOrder) throws DataPersistenceException {
        ordersDao.saveOrder(editedOrder);
        //update audit
        auditsDao.writeAuditEntry("Order Number " + editedOrder.getOrderNumber() + ", EDITED");
    }

    /**
     * generates order number using last order number
     *
     * @return order number
     * @throws DataPersistenceException
     */
    private int generateOrderNumber() throws DataPersistenceException {
        int lastOrderNumber = getLastOrderNumber();
        return ++lastOrderNumber;
    }

    /**
     * gets last order number found in orders list
     *
     * @return
     * @throws DataPersistenceException
     */
    private int getLastOrderNumber() throws DataPersistenceException {
        return ordersDao.getAllOrders().stream()
                .max(Comparator.comparing(Order::getOrderNumber)).get().getOrderNumber();
    }

    /**
     * calculates material cost for an order
     *
     * @param calculatedOrder order to calculate
     * @return order material cost
     */
    private BigDecimal calculateMaterialCost(Order calculatedOrder) {
        // MaterialCost = (Area * CostPerSquareFoot)
        return calculatedOrder.getArea().multiply(calculatedOrder.getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * calculates order labor cost
     *
     * @param calculatedOrder order to calculate
     * @return order labor cost
     */
    private BigDecimal calculateLaborCost(Order calculatedOrder) {
        // LaborCost = (Area * LaborCostPerSquareFoot)
        return calculatedOrder.getArea().multiply(calculatedOrder.getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * calculates order tax
     *
     * @param calculatedOrder order to calculate
     * @return order tax
     */
    private BigDecimal calculateTax(Order calculatedOrder) {
        // Tax = (MaterialCost + LaborCost) * (TaxRate/100) 
        BigDecimal materialAndLaborCost = calculatedOrder.getMaterialCost().add(calculatedOrder.getLaborCost());
        return materialAndLaborCost.multiply(calculatedOrder.getTaxRate().divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * calculates total order cost
     *
     * @param calculatedOrder order to calculate
     * @return total order cost
     */
    private BigDecimal calculateOrderTotal(Order calculatedOrder) {
        // Total = (MaterialCost + LaborCost + Tax)
        return calculatedOrder.getMaterialCost().add(calculatedOrder.getLaborCost()).add(calculatedOrder.getTax());
    }

    /**
     * gets order Product
     *
     * @param order order to find product in
     * @return Product found in order
     * @throws DataPersistenceException
     */
    private Product getOrderProduct(Order order) throws DataPersistenceException {
        return productsDao.getProduct(order.getProductType());
    }

    /**
     * gets order State
     *
     * @param order order to find State in
     * @return order State
     * @throws DataPersistenceException
     */
    private State getOrderState(Order order) throws DataPersistenceException {
        return statesDao.getState(order.getState());
    }

}
