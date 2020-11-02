/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.dto.Order;
import com.mthree.flooringmastery.service.NoOrderFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author beshoy
 */
@DisplayName("Orders Dao Implementation Test")
public class OrdersDaoFileImplTest {

    private OrdersDao ordersDao;

    public OrdersDaoFileImplTest() {
        ApplicationContext appContext
                = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        ordersDao = appContext.getBean("ordersDao", OrdersDaoFileImpl.class);
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    @DisplayName("Test orders dao to load all order from files to memory")
    public void testOrdersDao_toLoadAllOrdersIntoMemory() throws DataPersistenceException {
        // load test data from files 
        ordersDao.loadOrders();
        assertFalse(ordersDao.getAllOrders().isEmpty(), "should not be empty, files has three products");
        assertEquals(3, ordersDao.getAllOrders().size(), "should be 3, because all files has 3 orders");
    }

    @Test
    @DisplayName("Test orders dao to unmarshall data correctly into order object")
    public void testOrdersDao_toUnmarshallDataIntoOrderObject() throws DataPersistenceException {
        // set test data
        Order testOrder = new Order(LocalDate.parse("06-01-2013", DateTimeFormatter.ofPattern("MM-dd-yyyy")), 1, "Ada Lovelace",
                "CA", new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"),
                new BigDecimal("3.50"), new BigDecimal("4.15"), new BigDecimal("871.50"),
                new BigDecimal("1033.35"), new BigDecimal("476.21"), new BigDecimal("2381.06"));
        // load data 
        ordersDao.loadOrders();
        assertTrue(ordersDao.getAllOrders().contains(testOrder), "should contain test object as it is in the file");
    }

    @Test
    @DisplayName("test orders dao to return all orders for chosen date")
    public void testOrdersDao_toReturnAllOrdersForAgivenDate() throws DataPersistenceException {
        Order testOrder = new Order(LocalDate.parse("06-01-2013", DateTimeFormatter.ofPattern("MM-dd-yyyy")), 1, "Ada Lovelace",
                "CA", new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"),
                new BigDecimal("3.50"), new BigDecimal("4.15"), new BigDecimal("871.50"),
                new BigDecimal("1033.35"), new BigDecimal("476.21"), new BigDecimal("2381.06"));
        // load test data
        ordersDao.loadOrders();
        // local date
        LocalDate dateChoice = LocalDate.parse("06-01-2013", DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        LocalDate anotherDateChoice = LocalDate.parse("06-02-2013", DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        assertEquals(1, ordersDao.getAllOrders(dateChoice).size(), "should be 1 because there is one order for that date");
        assertEquals(testOrder, ordersDao.getAllOrders(dateChoice).get(0));
        assertEquals(2, ordersDao.getAllOrders(anotherDateChoice).size(), "should be 2 because there is two order for that date");
    }

    @Test
    @DisplayName("test orders dao to return order using order number and date")
    public void testOrdersDao_toReturnOrder() throws DataPersistenceException, NoOrderFoundException {
        Order testOrder = new Order(LocalDate.parse("06-01-2013", DateTimeFormatter.ofPattern("MM-dd-yyyy")), 1, "Ada Lovelace",
                "CA", new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"),
                new BigDecimal("3.50"), new BigDecimal("4.15"), new BigDecimal("871.50"),
                new BigDecimal("1033.35"), new BigDecimal("476.21"), new BigDecimal("2381.06"));
        // load test data
        ordersDao.loadOrders();
        assertEquals(testOrder, ordersDao.getOrder(testOrder.getOrderDate(), testOrder.getOrderNumber()), "should be equal becasue test order is in the list");
    }

    @Test
    @DisplayName("test orders dao to throw exception when order not found")
    public void testOrdersDao_toThrowException_whenOrderNotFound() throws DataPersistenceException, NoOrderFoundException {
        Order testOrder = new Order(LocalDate.parse("06-01-2013", DateTimeFormatter.ofPattern("MM-dd-yyyy")), 1, "Ada Lovelace",
                "CA", new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"),
                new BigDecimal("3.50"), new BigDecimal("4.15"), new BigDecimal("871.50"),
                new BigDecimal("1033.35"), new BigDecimal("476.21"), new BigDecimal("2381.06"));
        // load test data
        ordersDao.loadOrders();
        assertDoesNotThrow(() -> ordersDao.getOrder(testOrder.getOrderDate(), testOrder.getOrderNumber()), "should not throw because order in the list");
        // test throws with wrong order number
        try {
            ordersDao.getOrder(testOrder.getOrderDate(), 24);
            fail("shoud have thrown NoOrderFoundException because 24 is wrong order number");
        } catch (NoOrderFoundException ex) {
            return;
        }
        // test throws with wrong order date
        try {
            // adds one to order data to change it
            ordersDao.getOrder(testOrder.getOrderDate().plusDays(1), testOrder.getOrderNumber());
            fail("shoud have thrown NoOrderFoundException because date order is wrong");
        } catch (NoOrderFoundException ex) {
            return;
        }

    }

    @Test
    @DisplayName(" test orders dao to add new order to list")
    public void testOrdersDao_toAddOrderToList() throws DataPersistenceException, NoOrderFoundException {
        Order testOrder = new Order(LocalDate.parse("08-06-2020", DateTimeFormatter.ofPattern("MM-dd-yyyy")), 4, "test customer",
                "CA", new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"),
                new BigDecimal("3.50"), new BigDecimal("4.15"), new BigDecimal("871.50"),
                new BigDecimal("1033.35"), new BigDecimal("476.21"), new BigDecimal("2381.06"));
        // load test data 
        ordersDao.loadOrders();
        // add order to data 
        ordersDao.saveOrder(testOrder);
        // assert new data
        assertEquals(testOrder, ordersDao.getOrder(testOrder.getOrderDate(), 4), " should be equal because order added to list");
    }

    @Test
    @DisplayName(" test orders dao to remove order from list")
    public void testOrdersDao_toRemoveOrderFromList() throws DataPersistenceException, NoOrderFoundException {
        Order testOrder = new Order(LocalDate.parse("08-06-2020", DateTimeFormatter.ofPattern("MM-dd-yyyy")), 4, "test customer",
                "CA", new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"),
                new BigDecimal("3.50"), new BigDecimal("4.15"), new BigDecimal("871.50"),
                new BigDecimal("1033.35"), new BigDecimal("476.21"), new BigDecimal("2381.06"));
        // load test data 
        ordersDao.loadOrders();
        // add order to data 
        ordersDao.saveOrder(testOrder);
        // assert order add
        assertEquals(testOrder, ordersDao.getOrder(testOrder.getOrderDate(), testOrder.getOrderNumber()), " should be equal because order added to list");
        // remove order from list
        ordersDao.removeOrder(testOrder);
        // assert order removed
        try {
            ordersDao.getOrder(testOrder.getOrderDate(), testOrder.getOrderNumber());
            fail("should have thrown NoOrderFoundException because order was removed from list");
        } catch (NoOrderFoundException ex) {
            return;
        }
    }
}
