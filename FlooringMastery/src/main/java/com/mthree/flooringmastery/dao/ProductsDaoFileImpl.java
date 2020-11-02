/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.dto.Product;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * implements ProductsDao interface using text files as data source
 *
 * @author beshoy
 */
public class ProductsDaoFileImpl implements ProductsDao {

    private Map<String, Product> products;
    private String productsFile;
    private String delimiter;

    public ProductsDaoFileImpl() {
    }

    public ProductsDaoFileImpl(Map<String, Product> products, String productsFile, String delimiter) {
        this.products = products;
        this.productsFile = productsFile;
        this.delimiter = delimiter;
    }

    @Override
    public void loadProducts() throws DataPersistenceException {
        Scanner scanner;
        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(productsFile)));
        } catch (FileNotFoundException e) {
            throw new DataPersistenceException(
                    "Could not load products data into memory.", e);
        }
        // currentLine holds the most recent line read from the file
        String currentLine;
        // skip data header in file 
        scanner.nextLine();
        // holds the most recent state unmarshalled
        Product currentProduct;
        // Process while we have more lines in the file
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a product
            currentProduct = unmarshallProduct(currentLine);
            // Put product into the map using product type
            products.put(currentProduct.getProductType(), currentProduct);
        }
        // close scanner
        scanner.close();
    }

    @Override
    public void saveProducts() throws DataPersistenceException {
        PrintWriter out;
        try {
            File file = new File(productsFile);
            out = new PrintWriter(file);
        } catch (IOException e) {
            throw new DataPersistenceException(
                    "Could not save products data.", e);
        }
        // create list of products objects to be wrriten to the file
        List<Product> productsList = new ArrayList(products.values());
        // write each product in the list
        productsList.stream().forEach((product) -> {
            String productAsText = marshallProduct(product);
            out.println(productAsText);
            out.flush();
        });
        // Clean up
        out.close();
    }

    @Override
    public Product getProduct(String productType) throws DataPersistenceException {
        // returns null if no mapping
        return products.get(productType);
    }

    @Override
    public List<Product> getAllProducts() throws DataPersistenceException {
        return new ArrayList<Product>(products.values());
    }

    /**
     * creates product object using string representation of the object
     *
     * @param productAsText Product object as string
     * @return Product object
     */
    private Product unmarshallProduct(String productAsText) {
        String[] productTokens = productAsText.split(delimiter);
        Product productFromFile = new Product();
        // index 0 - ProductType
        productFromFile.setProductType(productTokens[0]);
        // index 1 - CostPerSquareFoot
        productFromFile.setCostPerSquareFoot(new BigDecimal(productTokens[1]));
        // index 2 - LaborCostPerSquareFoot
        productFromFile.setLaborCostPerSquareFoot(new BigDecimal(productTokens[2]));
        // We have now created a student! Return it!
        return productFromFile;
    }

    /**
     * creates string representation of product object
     *
     * @param product Product object
     * @return object as String
     */
    private String marshallProduct(Product product) {
        // create String to represnt product object
        String productAsText = product.getProductType() + delimiter;
        productAsText += product.getCostPerSquareFoot() + delimiter;
        productAsText += product.getLaborCostPerSquareFoot();
        // state object as string
        return productAsText;
    }
}
