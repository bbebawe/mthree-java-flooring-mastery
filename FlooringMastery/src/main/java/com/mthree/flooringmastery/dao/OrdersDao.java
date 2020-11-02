/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.dto.Order;
import com.mthree.flooringmastery.service.NoOrderFoundException;
import java.time.LocalDate;
import java.util.List;

/**
 * collection of methods to handle and manipulate orders data
 *
 * @author beshoy
 */
public interface OrdersDao {

    /**
     * loads all orders into memory
     *
     * @throws DataPersistenceException
     */
    public void loadOrders() throws DataPersistenceException;

    /**
     * saves all orders data to external resource
     *
     * @throws DataPersistenceException
     */
    public void saveOrders() throws DataPersistenceException;

    /**
     * gets order from orders list using order number and date
     *
     * @param date Order date
     * @param orderNumber Order number
     * @return Order object or throws NoOrderFoundException
     * @throws DataPersistenceException
     * @throws NoOrderFoundException
     */
    public Order getOrder(LocalDate date, int orderNumber) throws DataPersistenceException, NoOrderFoundException;

    /**
     * removes order from orders list
     *
     * @param order Order to remove
     * @return removed order
     * @throws DataPersistenceException
     */
    public Order removeOrder(Order order) throws DataPersistenceException;

    /**
     * gets list of orders for chosen date
     *
     * @param orderDate orders date
     * @return list of orders for that date
     * @throws DataPersistenceException
     */
    public List<Order> getAllOrders(LocalDate orderDate) throws DataPersistenceException;

    /**
     * gets list of all found orders
     *
     * @return list of all orders
     * @throws DataPersistenceException
     */
    public List<Order> getAllOrders() throws DataPersistenceException;

    /**
     * adds order to orders list
     *
     * @param order order to add to list
     * @throws DataPersistenceException
     */
    public void saveOrder(Order order) throws DataPersistenceException;

    /**
     * exports all orders to one single data file
     *
     * @throws DataPersistenceException
     */
    public void exportAllOrders() throws DataPersistenceException;

}
