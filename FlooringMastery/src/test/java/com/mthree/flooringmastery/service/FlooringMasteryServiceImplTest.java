/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.dao.DataPersistenceException;
import com.mthree.flooringmastery.dto.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author beshoy
 */
public class FlooringMasteryServiceImplTest {

    FlooringMasteryService service;

    public FlooringMasteryServiceImplTest() {
        ApplicationContext appContext
                = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        service = appContext.getBean("service", FlooringMasteryService.class);
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
    @DisplayName("test service to load all data from text files into objects")
    public void testService_toLoadAllData() throws DataPersistenceException, NoOrderFoundException {
        // set test date 
        LocalDate testDate = LocalDate.parse("06-01-2013", DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        // load test data
        service.loadAllData();
        assertFalse(service.getProductsList().isEmpty(), "should no be empty because there are products in file");
        assertFalse(service.getSatesCodes().isEmpty(), "should no be empty because there are states in file");
        assertFalse(service.getDateOrders(testDate).isEmpty(), "should not be empty because there are orders for that date in file");
    }

    @Test
    @DisplayName("test service to return product from orders list")
    public void testService_toReturnProduct() throws DataPersistenceException, NoOrderFoundException {
        // set test data
        Order testOrder = new Order(LocalDate.parse("06-01-2013", DateTimeFormatter.ofPattern("MM-dd-yyyy")), 1, "Ada Lovelace",
                "CA", new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"),
                new BigDecimal("3.50"), new BigDecimal("4.15"), new BigDecimal("871.50"),
                new BigDecimal("1033.35"), new BigDecimal("476.21"), new BigDecimal("2381.06"));
        // load data 
        service.loadAllData();
        assertDoesNotThrow(() -> service.getOrder(testOrder.getOrderDate(), testOrder.getOrderNumber()), "should not throw, order is in files");
        assertEquals(testOrder, service.getOrder(testOrder.getOrderDate(), testOrder.getOrderNumber()), "should be equal");
    }

    @Test
    @DisplayName("test service to return all orders chosen date")
    public void testService_toReurnAllOrdersForChosenDate() throws DataPersistenceException, NoOrderFoundException {
        // set test date 
        LocalDate testDate = LocalDate.parse("06-01-2013", DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        LocalDate secondTestDate = LocalDate.parse("06-02-2013", DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        service.loadAllData();
        // load data from files
        assertEquals(1, service.getDateOrders(testDate).size(), "should be 1 because there is only one order for that date");
        assertEquals(2, service.getDateOrders(secondTestDate).size(), "should be 1 because there is only one order for that date");
    }

    @Test
    @DisplayName("test service to save order to list")
    public void testService_toAddProductToList() throws DataPersistenceException, NoOrderFoundException {
        // set test data
        Order testOrder = new Order(LocalDate.parse("10-10-2020", DateTimeFormatter.ofPattern("MM-dd-yyyy")), 1, "test customer",
                "CA", new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"),
                new BigDecimal("3.50"), new BigDecimal("4.15"), new BigDecimal("871.50"),
                new BigDecimal("1033.35"), new BigDecimal("476.21"), new BigDecimal("2381.06"));
        // load data
        service.loadAllData();
        // add order to list
        service.saveOrder(testOrder);
        // assert new data
        assertDoesNotThrow(() -> service.getOrder(testOrder.getOrderDate(), testOrder.getOrderNumber()), "should not throw because order added to list");
        assertFalse(service.getDateOrders(testOrder.getOrderDate()).isEmpty(), "shoud not be empty");
    }

    @Test
    @DisplayName(" test service to remove order from list")
    public void testService_toRemoveOrderFromList() throws DataPersistenceException, NoOrderFoundException {
        // set test data
        Order testOrder = new Order(LocalDate.parse("10-10-2020", DateTimeFormatter.ofPattern("MM-dd-yyyy")), 1, "test customer",
                "CA", new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"),
                new BigDecimal("3.50"), new BigDecimal("4.15"), new BigDecimal("871.50"),
                new BigDecimal("1033.35"), new BigDecimal("476.21"), new BigDecimal("2381.06"));
        // load data
        service.loadAllData();
        // add order to list
        service.saveOrder(testOrder);
        service.removeOrder(testOrder.getOrderDate(), testOrder.getOrderNumber());
        // assert order is removed
        assertThrows(NoOrderFoundException.class, () -> service.getOrder(testOrder.getOrderDate(), testOrder.getOrderNumber()), "should throw exception because order is removed");
    }

    @Test
    @DisplayName("test service to edit order from the list")
    public void testService_toEditOrder() throws DataPersistenceException, NoOrderFoundException {
        // set test data
        Order testOrder = new Order(LocalDate.parse("10-10-2020", DateTimeFormatter.ofPattern("MM-dd-yyyy")), 1, "test customer",
                "CA", new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"),
                new BigDecimal("3.50"), new BigDecimal("4.15"), new BigDecimal("871.50"),
                new BigDecimal("1033.35"), new BigDecimal("476.21"), new BigDecimal("2381.06"));
        // load data
        service.loadAllData();
        // add order to list
        service.saveOrder(testOrder);
        // edit order 
        testOrder.setCustomerName("another test name");
        service.editOrder(testOrder);
        assertEquals(testOrder.getCustomerName(), service.getOrder(testOrder.getOrderDate(), testOrder.getOrderNumber()).getCustomerName(), "name changed so should be equal");
    }

    @Test
    @DisplayName("test service to generate new order number using last order number")
    public void testService_toGenerateNewOrderNumber() throws DataPersistenceException {
        // set test data
        Order testOrder = new Order(LocalDate.parse("06-01-2013", DateTimeFormatter.ofPattern("MM-dd-yyyy")), 0, "Ada Lovelace",
                "CA", new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"),
                new BigDecimal("3.50"), new BigDecimal("4.15"), new BigDecimal("871.50"),
                new BigDecimal("1033.35"), new BigDecimal("476.21"), new BigDecimal("2381.06"));
        // load test data
        service.loadAllData();
        assertEquals(4, service.calcualteOrder(testOrder).getOrderNumber(), "should be 4, because last order number in the list is 3");
    }

    @Test
    @DisplayName("test service to preform order calcualtions")
    public void testService_toPerformOrderCalculations() throws DataPersistenceException {
        // set test data
        Order testOrder = new Order(LocalDate.parse("06-01-2013", DateTimeFormatter.ofPattern("MM-dd-yyyy")), 0, "Ada Lovelace",
                "CA", new BigDecimal("0"), "Tile", new BigDecimal("249.00"),
                new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"),
                new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"));
        // load test data
        service.loadAllData();
        Order calculatedOrder = service.calcualteOrder(testOrder);
        assertAll(
                () -> assertEquals(new BigDecimal("25.00"), calculatedOrder.getTaxRate(), "should be 25 becasue CA tax rate is 25"),
                () -> assertEquals(new BigDecimal("3.50"), calculatedOrder.getCostPerSquareFoot(), "should be 3.50 becasue tile cost is 3.50"),
                () -> assertEquals(new BigDecimal("4.15"), calculatedOrder.getLaborCostPerSquareFoot(), "should be 4.15 becasue tile labor cost is 4.15"),
                () -> assertEquals(new BigDecimal("871.50"), calculatedOrder.getMaterialCost(), "should be 871.50 (area * costPerFoot ) -> (3.50 * 249) "),
                () -> assertEquals(new BigDecimal("1033.35"), calculatedOrder.getLaborCost(), "should be 1033.35 (Area * LaborCostPerSquareFoot) -> (249 * 4.15) "),
                () -> assertEquals(new BigDecimal("476.21"), calculatedOrder.getTax(), "should be 476.21(MaterialCost + LaborCost) * (TaxRate/100) "),
                () -> assertEquals(new BigDecimal("2381.06"), calculatedOrder.getTotal(), "should be 2381.06 (MaterialCost + LaborCost + Tax)")
        );
    }

}
