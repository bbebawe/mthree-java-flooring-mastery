/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.dto.Order;
import com.mthree.flooringmastery.service.NoOrderFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author beshoy
 */
public class OrdersDaoFileImpl implements OrdersDao {

    private Map<Integer, Order> orders;
    private String ordersFile;
    private String delimiter;
    private String backupFile;
    private String ordersHeader;

    public OrdersDaoFileImpl() {
    }

    public OrdersDaoFileImpl(Map<Integer, Order> orders, String ordersFile, String delimiter, String backupFile, String ordersHeader) {
        this.orders = orders;
        this.ordersFile = ordersFile;
        this.delimiter = delimiter;
        this.backupFile = backupFile;
        this.ordersHeader = ordersHeader;
    }

    @Override
    public void loadOrders() throws DataPersistenceException {

        // uses Stream Path to real all files inside a folder or path
        try ( Stream<Path> paths = Files.walk(Paths.get(ordersFile))) {
            // for each path or file found read the data
            paths.forEach((filePath) -> {
                if (Files.isRegularFile(filePath)) {
                    Scanner scanner = null;
                    try {
                        // Create Scanner for reading the file
                        scanner = new Scanner(
                                new BufferedReader(
                                        new FileReader(filePath.toString())));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(OrdersDaoFileImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // currentLine holds the most recent line read from the file
                    String currentLine;
                    // skip data header in file 
                    scanner.nextLine();
                    // holds the most recent state unmarshalled
                    Order currentOrder;
                    // Process while we have more lines in the file
                    while (scanner.hasNextLine()) {
                        // get the next line in the file
                        currentLine = scanner.nextLine();
                        // unmarshall the line into a product
                        currentOrder = unmarshallOrder(currentLine);
                        // SET ORDER DATE TO BE SAME DATE AS THE FILE NAME
                        LocalDate orderDate = getOrderDateFromFileName(filePath.toString());
                        currentOrder.setOrderDate(orderDate);
                        // Put order into the map using order number as key
                        orders.put(currentOrder.getOrderNumber(), currentOrder);
                    }
                    // close scanner
                    scanner.close();
                }
            });
        } catch (IOException ex) {
            throw new DataPersistenceException("could not load orders files");
        }
    }

    @Override
    public void saveOrders() throws DataPersistenceException {
        // get list of dates on orders
        List<LocalDate> allOrdersDates = getAllOrders().stream().map((order) -> order.getOrderDate()).collect(Collectors.toList());
        // create new file for each date found and write date orders to it
        allOrdersDates.forEach((orderDate) -> {
            // create file name for order date
            String fileDate = orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy"));
            // path to store file
            String fileName = "Orders/Orders_" + fileDate + ".txt";
            PrintWriter out = null;
            File file = new File(fileName);
            try {
                out = new PrintWriter(new FileWriter(file));
                // print header on fiel - print only once
                out.println(ordersHeader);
                // write each found order to the file
                for (Order order : orders.values()) {
                    // write to the file only if order made in same day as file name
                    if (order.getOrderDate().compareTo(orderDate) == 0) {
                        String orderAsText = marshallOrder(order);
                        out.println(orderAsText);
                        out.flush();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(OrdersDaoFileImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.close();
        });
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws DataPersistenceException, NoOrderFoundException {
        Order order = orders.get(orderNumber);
        if (order == null) {
            throw new NoOrderFoundException("No order found for the given date or number");
        } else {
            if (order.getOrderDate().compareTo(date) == 0) {
                return order;
            } else {
                throw new NoOrderFoundException("No order found for the given date or number");
            }
        }
    }

    @Override
    public Order removeOrder(Order order) throws DataPersistenceException {
        return orders.remove(order.getOrderNumber());
    }

    @Override
    public List<Order> getAllOrders(LocalDate orderDate) throws DataPersistenceException {
        List<Order> allDateOrders = orders.values().stream()
                .filter((order) -> order.getOrderDate().compareTo(orderDate) == 0)
                .collect(Collectors.toList());
        return allDateOrders;
    }

    @Override
    public List<Order> getAllOrders() throws DataPersistenceException {
        return new ArrayList<Order>(orders.values());
    }

    @Override
    public void saveOrder(Order order) throws DataPersistenceException {
        orders.put(order.getOrderNumber(), order);
    }

    @Override
    public void exportAllOrders() throws DataPersistenceException {
        PrintWriter out;
        try {
            File file = new File(backupFile);
            out = new PrintWriter(file);
            // print file header 
            String exportDataHeader = ordersHeader + ",OrderDate";
            out.println(exportDataHeader);
        } catch (IOException e) {
            throw new DataPersistenceException(
                    "Could not save states data.", e);
        }
        // create list of products objects to be wrriten to the file
        List<Order> ordersList = new ArrayList(orders.values());
        ordersList.stream().forEach((order) -> {
            String orderAsText = marshallOrder(order);
            orderAsText += delimiter + order.getOrderDate().toString();
            out.println(orderAsText);
            out.flush();
        });
        // Clean up
        out.close();
    }

    /**
     * extracts order date from file name
     *
     * @param fileName file name with data on it
     * @return Local date object from file name
     */
    private LocalDate getOrderDateFromFileName(String fileName) {
        // use _ and . to get date from file name
        String dateString = fileName.substring(fileName.indexOf("_") + 1, fileName.indexOf("."));
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MMddyyyy"));
    }

    /**
     * creates order object from string
     *
     * @param orderAsText order object as string
     * @return Order object
     */
    private Order unmarshallOrder(String orderAsText) {
        String[] orderTokens = orderAsText.split(delimiter);
        Order orderFromFile = new Order();
        // index 0 - OrderNumber
        orderFromFile.setOrderNumber(Integer.parseInt(orderTokens[0]));
        // index 1 - CustomerName
        // checks if cutomerName has / and replace it with comma 
        // this was added to prevent program from crashing when name has a value similiar to delimiter
        if (orderTokens[1].contains("/")) {
            orderFromFile.setCustomerName(orderTokens[1].replace("/", ","));
        } else {
            orderFromFile.setCustomerName(orderTokens[1]);
        }
        // index 2 - State
        orderFromFile.setState(orderTokens[2]);
        // index 3 - TaxRate
        orderFromFile.setTaxRate(new BigDecimal(orderTokens[3]));
        // index 4 - ProductType
        orderFromFile.setProductType(orderTokens[4]);
        // index 5 - Area
        orderFromFile.setArea(new BigDecimal(orderTokens[5]));
        // index 6 - CostPerSquareFoot
        orderFromFile.setCostPerSquareFoot(new BigDecimal(orderTokens[6]));
        // index 7 - LaborCostPerSquareFoot
        orderFromFile.setLaborCostPerSquareFoot(new BigDecimal(orderTokens[7]));
        // index 8 - MaterialCost
        orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]));
        // index 9 - LaborCost
        orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]));
        // index 10 - Tax
        orderFromFile.setTax(new BigDecimal(orderTokens[10]));
        // index 11 - Total
        orderFromFile.setTotal(new BigDecimal(orderTokens[11]));
        return orderFromFile;
    }

    /**
     * creates string from order object
     *
     * @param order order object
     * @return string has order object data
     */
    private String marshallOrder(Order order) {
        // create String to represnt order object
        String orderAsText = order.getOrderNumber() + delimiter;
        // checks if name has a value similair to delimiter replace it with / 
        // this prevents program from crashing when name has comma
        if (order.getCustomerName().contains(",")) {
            orderAsText += order.getCustomerName().replace(",", "/") + delimiter;
        } else {
            orderAsText += order.getCustomerName() + delimiter;
        }
        orderAsText += order.getState() + delimiter;
        orderAsText += order.getTaxRate() + delimiter;
        orderAsText += order.getProductType() + delimiter;
        orderAsText += order.getArea() + delimiter;
        orderAsText += order.getCostPerSquareFoot() + delimiter;
        orderAsText += order.getLaborCostPerSquareFoot() + delimiter;
        orderAsText += order.getMaterialCost() + delimiter;
        orderAsText += order.getLaborCost() + delimiter;
        orderAsText += order.getTax() + delimiter;
        orderAsText += order.getTotal();
        // order object as string
        return orderAsText;
    }
}
