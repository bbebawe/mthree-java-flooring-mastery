/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.dto.Product;
import java.util.List;

/**
 * collection of methods to handle and manipulate products data
 *
 * @author beshoy
 */
public interface ProductsDao {

    /**
     * loads products into memory
     *
     * @throws DataPersistenceException
     */
    public void loadProducts() throws DataPersistenceException;

    /**
     * saves all products in memory to external resource
     *
     * @throws DataPersistenceException
     */
    public void saveProducts() throws DataPersistenceException;

    /**
     * gets product by its product type
     *
     * @param productType product type to look for
     * @return Product object matches product type
     * @throws DataPersistenceException
     */
    public Product getProduct(String productType) throws DataPersistenceException;

    /**
     * gets list of all available products
     *
     * @return List of products objects
     * @throws DataPersistenceException
     */
    public List<Product> getAllProducts() throws DataPersistenceException;

}
