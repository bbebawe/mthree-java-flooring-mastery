/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.dao.DataPersistenceException;
import com.mthree.flooringmastery.dto.Order;
import com.mthree.flooringmastery.dto.Product;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author beshoy
 */
public interface FlooringMasteryService {

    /**
     * loads all data (states, orders, products) into memory
     *
     * @throws DataPersistenceException
     */
    public void loadAllData() throws DataPersistenceException;

    /**
     * gets list of orders for chosen date or throw exception if no orders
     *
     * @param dateChoice chosen orders date
     * @return List of orders or throws exception
     * @throws DataPersistenceException
     * @throws NoOrderFoundException
     */
    public List<Order> getDateOrders(LocalDate dateChoice) throws DataPersistenceException, NoOrderFoundException;

    /**
     * saves all data from memory to external resources
     *
     * @throws DataPersistenceException
     */
    public void saveAllOrders() throws DataPersistenceException;

    /**
     * gets list of all available products
     *
     * @return list of products
     * @throws DataPersistenceException
     */
    public List<Product> getProductsList() throws DataPersistenceException;

    /**
     * gets list of all available states
     *
     * @return list of states
     * @throws DataPersistenceException
     */
    public List<String> getSatesCodes() throws DataPersistenceException;

    /**
     * preforms order calculations such as tax, total
     *
     * @param order Order to calculate cost for
     * @return calculated order object
     * @throws DataPersistenceException
     */
    public Order calcualteOrder(Order order) throws DataPersistenceException;

    /**
     * saves order to orders list
     *
     * @param order order to add to list
     * @throws DataPersistenceException
     */
    public void saveOrder(Order order) throws DataPersistenceException;

    /**
     * gets order from list using order date and number
     *
     * @param orderDate Order date
     * @param orderNumber Order number
     * @return Order object
     * @throws DataPersistenceException
     * @throws NoOrderFoundException
     */
    public Order getOrder(LocalDate orderDate, int orderNumber) throws DataPersistenceException, NoOrderFoundException;

    /**
     * removes order from list using order date and number
     *
     * @param orderDate Order date
     * @param orderNumber Order number
     * @return removed order object
     * @throws DataPersistenceException
     * @throws NoOrderFoundException
     */
    public Order removeOrder(LocalDate orderDate, int orderNumber) throws DataPersistenceException, NoOrderFoundException;

    /**
     * checks if order has new data otherwiserwis throws exception
     *
     * @param editedOrder edited order object
     * @param originalOrder original order object
     * @throws NoEditDataExceprtion
     */
    public void isOrderEdited(Order editedOrder, Order originalOrder) throws NoEditDataExceprtion;

    /**
     * updates order calculations
     *
     * @param updatedOrder
     * @throws DataPersistenceException
     */
    public void updateOrderCalculations(Order updatedOrder) throws DataPersistenceException;

    /**
     * exports all data into single file
     *
     * @throws DataPersistenceException
     */
    public void exportAllData() throws DataPersistenceException;

    /**
     * edits order and adds it to the list
     *
     * @param editedOrder order to edit
     * @throws DataPersistenceException
     */
    public void editOrder(Order editedOrder) throws DataPersistenceException;

}
