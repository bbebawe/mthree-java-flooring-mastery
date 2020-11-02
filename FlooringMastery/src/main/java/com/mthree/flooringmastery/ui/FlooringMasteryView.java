/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.ui;

import com.mthree.flooringmastery.dto.Order;
import com.mthree.flooringmastery.dto.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author beshoy
 */
public class FlooringMasteryView {

    private UserIO io;

    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    /**
     * displays banner to user
     *
     * @param banner banner string
     */
    public void displyBanner(String banner) {
        io.print("***** " + banner + " *****");
    }

    /**
     * displays menu options
     */
    public void displayMenuOptions() {
        io.print("1. Display Orders\n"
                + "2. Add an Order\n"
                + "3. Edit an Order\n"
                + "4. Remove an Order\n"
                + "5. Export All Data\n"
                + "6. Quit");
    }

    /**
     * prompts user to choose from menu options
     *
     * @return Integer represents chosen menu option
     */
    public int getMenuSelection() {
        return io.readInt("Please choose from the above options", 1, 6);
    }

    /**
     * displays exit message
     */
    public void displayExitMessage() {
        io.print("----- Thank You for using Flooring Mastery-----");
        io.print("--------------------- Goodbye ---------------------");
    }

    /**
     * prompts user to enter local date
     *
     * @return LocalDate object
     */
    public LocalDate getOrdersDate() {
        return io.readLocalDate("Please enter Orders date in format (MM-DD-YYYY)");
    }

    /**
     * displays Order object
     *
     * @param order Order object to display
     */
    public void displayOrder(Order order) {
        io.print("---------------------------------------");
        io.print("Order Date : " + order.getOrderDate());
        io.print("Order Number : " + order.getOrderNumber());
        io.print("Customer Name : " + order.getCustomerName());
        io.print("State : " + order.getState());
        io.print("Tax Rate : " + order.getTaxRate());
        io.print("Product Type : " + order.getProductType());
        io.print("Area : " + order.getArea());
        io.print("Cost Per Square Foot : " + order.getCostPerSquareFoot());
        io.print("Labor Cost Per Square Foot : " + order.getLaborCostPerSquareFoot());
        io.print("Material Cost : " + order.getMaterialCost());
        io.print("Labor Cost : " + order.getLaborCost());
        io.print("Tax : " + order.getTax());
        io.print("Total : " + order.getTotal());
        io.print("---------------------------------------");
    }

    /**
     * displays list of orders to user
     *
     * @param ordersList orders list to display
     */
    public void displayAllDateOrders(List<Order> ordersList) {
        io.print("----- " + ordersList.size() + " Orders Found -----");
        ordersList.stream().forEach((order) -> {
            displayOrder(order);
            io.readEnter("hit enter to continue: ");
        });
    }

    /**
     * displays error message to user
     *
     * @param message error message
     */
    public void displayErrorMessage(String message) {
        io.print("----- " + message + " -----");
        io.readEnter("hit enter to continue: ");
    }

    /**
     * creates order object with order date, customer name, state, product type
     * and area
     *
     * @param availableProducts list of available products
     * @param availableStateCodes list of available states
     * @return Order object
     */
    public Order getOrderDetails(List<Product> availableProducts, List<String> availableStateCodes) {
        Order newOrder = new Order();
        newOrder.setOrderDate(getOrderDate());
        newOrder.setCustomerName(getCustomerName(false));
        // pass list of states to user, false is used to prevent blank input
        newOrder.setState(getOrderState(availableStateCodes, false));
        // pass list of availableProducts so user can choose from it, false is used to prevent blank input
        newOrder.setProductType(getProductType(availableProducts, false));
        // false is used to prevent blank input
        newOrder.setArea(getOrderArea(false));
        return newOrder;
    }

    /**
     * prompts user to enter order date
     *
     * @return
     */
    public LocalDate getOrderDate() {
        return io.readLocalDate("Enter order date (MM-DD-YYYY): ", LocalDate.now());
    }

    /**
     * prompts user to enter customer name
     *
     * @return
     */
    private String getCustomerName(boolean canBeBlank) {
        return io.readString("Enter customer name: ", canBeBlank);
    }

    /**
     * prompts user to choose order state
     *
     * @param availableStateCodes list of available state
     * @param canBeBlank boolean to accept blank input or no
     * @return order state as string
     */
    private String getOrderState(List<String> availableStateCodes, Boolean canBeBlank) {
        displayStates(availableStateCodes);
        return io.readString("Enter order state abbreviation: ", availableStateCodes, canBeBlank);
    }

    /**
     * displays list of available to user
     *
     * @param availableStateCodes
     */
    private void displayStates(List<String> availableStateCodes) {
        displyBanner("We Ship to the Following States");
        io.print(availableStateCodes.toString());

    }

    /**
     * prompts user to enter order area
     *
     * @param canBeBlank boolean to accept blank input or no
     * @return order area
     */
    private BigDecimal getOrderArea(boolean canBeBlank) {
        // min values allowed it 100 sq ft
        return io.readBigDecimal("Enter order Area is square feet (minimum 100): ", new BigDecimal("100"), canBeBlank);
    }

    /**
     * prompts user to enter product type
     *
     * @param availableProducts list of available products
     * @param canBeBlank boolean to accept blank input or no
     * @return
     */
    private String getProductType(List<Product> availableProducts, Boolean canBeBlank) {
        displyBanner("Available Products");
        displayavailableProducts(availableProducts);
        List<String> productsName = getavailableProductsNames(availableProducts);
        return io.readString("Enter product type: ", productsName, canBeBlank);
    }

    /**
     * displays list of available products to user
     *
     * @param availableProducts list of products
     */
    private void displayavailableProducts(List<Product> availableProducts) {
        availableProducts.forEach((product) -> displayProduct(product));
    }

    /**
     * displays product to user
     *
     * @param product product to display
     */
    private void displayProduct(Product product) {
        io.print("Product Type: " + product.getProductType()
                + ", Cost per Square Foot: " + product.getCostPerSquareFoot()
                + ", Labor Cost per Square Foot: " + product.getLaborCostPerSquareFoot());
        io.print("----------");
    }

    /**
     * displays list of available products names
     *
     * @param availableProducts list of products
     * @return list of products names
     */
    private List<String> getavailableProductsNames(List<Product> availableProducts) {
        return availableProducts.stream().map((product) -> product.getProductType()).collect(Collectors.toList());
    }

    /**
     * asks user to save order or no
     *
     * @return boolean to save or not
     */
    public boolean askToSave() {
        return io.readNextOperation("Do you want to save the order (y/n)");
    }

    /**
     * displays success message with order number when order is created
     *
     * @param orderNumber order number
     */
    public void displayAddOrderSucessBaner(int orderNumber) {
        io.print("Order Added sucessfuly, Order number is " + orderNumber);
        io.readEnter("hit enter to continue: ");
    }

    /**
     * displays message to user
     *
     * @param message message to display
     */
    public void displayMessage(String message) {
        io.print(message);
    }

    /**
     * prompts user to enter order number
     *
     * @return
     */
    public int getOrderNumber() {
        return io.readInt("Enter order number: (min 1)", 1);
    }

    /**
     * displays success message with order number when order is removed
     *
     * @param orderNumber order number to display
     */
    public void displayRemoveOrderSucessBanner(int orderNumber) {
        io.print("Order Number " + orderNumber + " Removed sucessfuly");
        io.readEnter("hit enter to continue: ");
    }

    /**
     * prompts user to confirm order removal with order number
     *
     * @param orderNmber order number to display
     * @return boolean value to remove or no
     */
    public boolean confirmOrderRemoval(int orderNmber) {
        return io.readNextOperation("Are you sure you want to remove order number "
                + orderNmber + " (y/n)");
    }

    /**
     * displays order summary (number, date, customer and total)
     *
     * @param order Order to display
     */
    public void displayOrderSummary(Order order) {
        io.print("----- Order Found -----");
        io.print("Order Date : " + order.getOrderDate() + ", Order Number : " + order.getOrderNumber()
                + ", Customer Name : " + order.getCustomerName() + ", Total : " + order.getTotal());
    }

    /**
     * handles display and getting new order details
     *
     * @param availableProducts list of products to display to user
     * @param availableStateCodes list of states to display to user
     * @param foundOrder original order
     * @return order with updated data
     */
    public Order getUpdatedOrderDetails(List<Product> availableProducts, List<String> availableStateCodes, Order foundOrder) {
        // update object
        Order updateOrder = new Order();
        String customerName = getUpdatedCustomerName(foundOrder.getCustomerName());
        String updatedState = getUpdatedSate(availableStateCodes, true, foundOrder.getState());
        String updatedProductType = getUpdatedProductType(availableProducts, true, foundOrder.getProductType());
        BigDecimal updatedArea = getUpdatedOrderArea(true, foundOrder.getArea());
        // set value to edit product
        updateOrder.setOrderDate(foundOrder.getOrderDate());
        updateOrder.setOrderNumber(foundOrder.getOrderNumber());
        updateOrder.setCustomerName(customerName.isBlank() ? foundOrder.getCustomerName() : customerName);
        updateOrder.setState(updatedState.isBlank() ? foundOrder.getState() : updatedState);
        updateOrder.setProductType(updatedProductType.isBlank() ? foundOrder.getProductType() : updatedProductType);
        updateOrder.setArea(updatedArea.intValue() == 0 ? foundOrder.getArea() : updatedArea);
        return updateOrder;
    }

    /**
     * prompts user to enter updated customer name
     * @param currentName
     * @return 
     */
    private String getUpdatedCustomerName(String currentName) {
        return io.readString("Enter customer name ( " + currentName + " ): ");
    }

    /**
     * prompts user to enter updated order state
     *
     * @param availableStateCodes list of states
     * @param canBeBlank boolean value to accept blank
     * @param currentState current state value
     * @return state value or blank
     */
    private String getUpdatedSate(List<String> availableStateCodes, boolean canBeBlank, String currentState) {
        displayStates(availableStateCodes);
        return io.readString("Enter order state abbreviation:( " + currentState + " )", availableStateCodes, canBeBlank);
    }

    /**
     *
     * @param availableProducts
     * @param canBeBlank
     * @param currentType
     * @return
     */
    private String getUpdatedProductType(List<Product> availableProducts, boolean canBeBlank, String currentType) {
        displyBanner("Available Products");
        displayavailableProducts(availableProducts);
        List<String> productsName = getavailableProductsNames(availableProducts);
        return io.readString("Enter product type ( " + currentType + " ): ", productsName, canBeBlank);
    }

    /**
     * prompts user to enter updated order area
     *
     * @param canBeBlank boolean value to allow blank inputs
     * @param currentProductArea current order area
     * @return new order area or 0 as blank
     */
    private BigDecimal getUpdatedOrderArea(boolean canBeBlank, BigDecimal currentProductArea) {
        // min values allowed it 100 sq ft
        return io.readBigDecimal("Enter order Area is square feet minimum 100 (" + currentProductArea + " ): ", new BigDecimal("100"), canBeBlank);
    }

    /**
     * prompts user to confirm update operation
     *
     * @return boolean value to update or no
     */
    public boolean confirmOrderUpdate() {
        return io.readNextOperation("Are you sure you want to update order (y/n)");
    }

    /**
     * displays success message with order number when order is updates
     *
     * @param updatedOrder
     */
    public void displayUpdateOrderSucessMessage(int orderNumber) {
        io.print("Order number " + orderNumber + " Updated sucessfuly");
        io.readEnter("hit enter to continue: ");
    }

    /**
     * displays message when data is exported to one file
     */
    public void displayExprtedSucessBanner() {
        displayMessage("----- Data Exported sucessfuly -----");
        io.readEnter("hit enter to continue: ");
    }

}
