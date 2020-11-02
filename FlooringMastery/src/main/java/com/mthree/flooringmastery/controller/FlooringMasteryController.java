/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.controller;

import com.mthree.flooringmastery.dao.DataPersistenceException;
import com.mthree.flooringmastery.dto.Order;
import com.mthree.flooringmastery.dto.Product;
import com.mthree.flooringmastery.service.FlooringMasteryService;
import com.mthree.flooringmastery.service.NoEditDataExceprtion;
import com.mthree.flooringmastery.service.NoOrderFoundException;
import com.mthree.flooringmastery.ui.FlooringMasteryView;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author beshoy
 */
public class FlooringMasteryController {

    private FlooringMasteryService service;
    private FlooringMasteryView view;
    private boolean keepGoing;

    public FlooringMasteryController(FlooringMasteryService service, FlooringMasteryView view, boolean keepGoing) {
        this.service = service;
        this.view = view;
        this.keepGoing = keepGoing;
    }

    public void run() throws DataPersistenceException, NoOrderFoundException, NoEditDataExceprtion {
        int menuSelection = 0;
        // load all data into memory
        loadData();
        do {
            menuSelection = displayMenuAndGetSelection();
            switch (menuSelection) {
                case 1:
                    displayOrders();
                    break;
                case 2:
                    addOrder();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
                    break;
                case 5:
                    exportAllData();
                    break;
                case 6:
                    exit();
            }
        } while (keepGoing);
    }

    /**
     * handles menu display and user selection from menu
     *
     * @return menu option selection
     */
    private int displayMenuAndGetSelection() {
        displayOperationBanner("Flooring Mastery Program");
        view.displayMenuOptions();
        return view.getMenuSelection();
    }

    /**
     * handles display orders operation
     *
     * @throws DataPersistenceException
     * @throws NoOrderFoundException
     */
    private void displayOrders() throws DataPersistenceException, NoOrderFoundException {
        displayOperationBanner("Display Orders");
        // get orders date
        LocalDate dateChoice = view.getOrdersDate();
        try {
            // try to get all orders from servie and pass it to view
            view.displayAllDateOrders(service.getDateOrders(dateChoice));
        } catch (NoOrderFoundException ex) {
            view.displayErrorMessage(ex.getMessage());
        }
    }

    /**
     * handles the operation of adding new order into the system
     *
     * @throws DataPersistenceException
     */
    private void addOrder() throws DataPersistenceException {
        displayOperationBanner("Add Order");
        // get products list and states codes so user can choose from them
        List<Product> availableProducts = service.getProductsList();
        List<String> availableStateCodes = service.getSatesCodes();
        Order newOrder = view.getOrderDetails(availableProducts, availableStateCodes);
        // process order calcualtion and return object
        Order calculatedOrder = service.calcualteOrder(newOrder);
        // display full order back to user
        view.displyBanner("Order Details");
        view.displayOrder(calculatedOrder);
        // confrim is save or not
        boolean saveOrder = view.askToSave();
        if (saveOrder) {
            service.saveOrder(calculatedOrder);
            view.displayAddOrderSucessBaner(calculatedOrder.getOrderNumber());
        } else {
            view.displayMessage("Order data discarded, Order is not added");
        }
    }

    /**
     * handles the operation of editing an order
     *
     * @throws DataPersistenceException
     * @throws NoEditDataExceprtion
     */
    private void editOrder() throws DataPersistenceException, NoEditDataExceprtion {
        displayOperationBanner("Edit Order");
        // get date and order number to check if order exsits
        LocalDate orderDate = view.getOrderDate();
        int orderNumber = view.getOrderNumber();
        try {
            List<Product> availableProducts = service.getProductsList();
            List<String> availableStateCodes = service.getSatesCodes();
            // throws exception is order not found
            Order foundOrder = service.getOrder(orderDate, orderNumber);
            // promts user to update order details
            Order editedOrder = view.getUpdatedOrderDetails(availableProducts, availableStateCodes, foundOrder);
            // checks if user has changed order details, throws exception otherwise
            service.isOrderEdited(editedOrder, foundOrder);
            // update calculations only if data changed
            service.updateOrderCalculations(editedOrder);
            view.displayOrder(editedOrder);
            boolean update = view.confirmOrderUpdate();
            if (update) {
                service.editOrder(editedOrder);
                view.displayUpdateOrderSucessMessage(editedOrder.getOrderNumber());
            } else {
                view.displayMessage("Changes discared, order is not updated");
            }
        } catch (NoOrderFoundException ex) {
            view.displayErrorMessage(ex.getMessage());
        } catch (NoEditDataExceprtion ex) {
            view.displayErrorMessage(ex.getMessage());
        }
    }

    /**
     * handles remove order from orders list
     *
     * @throws DataPersistenceException
     * @throws NoOrderFoundException
     */
    private void removeOrder() throws DataPersistenceException, NoOrderFoundException {
        displayOperationBanner("Remove Order");
        // get date and number to find order before it is removed
        LocalDate orderDate = view.getOrderDate();
        int orderNumber = view.getOrderNumber();
        try {
            // if order is not found, throws exception
            Order orderToRemove = service.getOrder(orderDate, orderNumber);
            view.displayOrderSummary(orderToRemove);
            boolean remove = view.confirmOrderRemoval(orderToRemove.getOrderNumber());
            if (remove) {
                Order order = service.removeOrder(orderDate, orderNumber);
                view.displayRemoveOrderSucessBanner(order.getOrderNumber());
            } else {
                view.displayMessage("Order number " + orderToRemove.getOrderNumber() + " was not removed.");
            }
        } catch (NoOrderFoundException ex) {
            view.displayErrorMessage(ex.getMessage());
        }

    }

    /**
     * handles exporting all data into single file
     *
     * @throws DataPersistenceException
     */
    private void exportAllData() throws DataPersistenceException {
        displayOperationBanner("Export All Data");
        service.exportAllData();
        view.displayExprtedSucessBanner();
    }

    /**
     * handles program exit and saves data
     *
     * @throws DataPersistenceException
     */
    private void exit() throws DataPersistenceException {
        view.displayExitMessage();
        saveData();
        keepGoing = false;
    }

    /**
     * loads all data into memory
     *
     * @throws DataPersistenceException
     */
    private void loadData() throws DataPersistenceException {
        service.loadAllData();
    }

    /**
     * save all data from memory to external resources
     *
     * @throws DataPersistenceException
     */
    private void saveData() throws DataPersistenceException {
        service.saveAllOrders();
    }

    /**
     * displays operation banner
     *
     * @param operation operation name
     */
    private void displayOperationBanner(String operation) {
        view.displyBanner(operation);
    }
}
