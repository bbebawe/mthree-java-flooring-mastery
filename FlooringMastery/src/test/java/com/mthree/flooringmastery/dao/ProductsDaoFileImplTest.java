/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.dto.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
@DisplayName("Products Dao Implementation Test")
public class ProductsDaoFileImplTest {

    private ProductsDao productsDao;

    public ProductsDaoFileImplTest() {
        ApplicationContext appContext
                = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        productsDao = appContext.getBean("productsDao", ProductsDaoFileImpl.class);
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
    @DisplayName("Test products dao to load all products from file")
    public void testProductsDao_toLoadAllProductsFromFile() throws DataPersistenceException {
        // load test data from test file
        productsDao.loadProducts();
        assertAll(
                () -> assertDoesNotThrow(() -> productsDao.loadProducts(), "should not throw exception"),
                () -> assertFalse(productsDao.getAllProducts().isEmpty(), "should not be empty because file has 4 products"),
                () -> assertEquals(4, productsDao.getAllProducts().size(), "should be 4, because test file has 4 products")
        );
    }

    @Test
    @DisplayName("test products dao to unmarshall data correctly into product object")
    public void testProductsDao_toUnmarshallDataIntoProductObject() throws DataPersistenceException {
        // load test data from test file
        productsDao.loadProducts();
        Product testProduct = new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10"));
        assertAll(
                () -> assertDoesNotThrow(() -> productsDao.getProduct(testProduct.getProductType()), "should not throw exception because Carpet product is in file"),
                () -> assertEquals(testProduct, productsDao.getProduct(testProduct.getProductType()), "should be equal to Carpet, 2.25, 2.10"),
                () -> assertEquals(testProduct.getProductType(), productsDao.getProduct(testProduct.getProductType()).getProductType(), "should be Carpet"),
                () -> assertEquals(testProduct.getCostPerSquareFoot(), productsDao.getProduct(testProduct.getProductType()).getCostPerSquareFoot(), "should be 2.25"),
                () -> assertEquals(testProduct.getLaborCostPerSquareFoot(), productsDao.getProduct(testProduct.getProductType()).getLaborCostPerSquareFoot(), "should be 2.10")
        );
    }

    @Test
    @DisplayName("test products dao to return list of all products")
    public void testProductsDao_toReturnListOfProducts() throws DataPersistenceException {
        productsDao.loadProducts();

        List<Product> productsList = new ArrayList<>();
        productsList.add(new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10")));
        productsList.add(new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10")));
        productsList.add(new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
        productsList.add(new Product("Wood", new BigDecimal("5.15"), new BigDecimal("4.75")));

        assertEquals(4, productsDao.getAllProducts().size(), "should be 4, because file has 4 products");
        assertTrue(productsDao.getAllProducts().contains(productsList.get(0)));
        assertTrue(productsDao.getAllProducts().contains(productsList.get(1)));
        assertTrue(productsDao.getAllProducts().contains(productsList.get(2)));
        assertTrue(productsDao.getAllProducts().contains(productsList.get(3)));
    }

    @Test
    @DisplayName(" test products dao to return null when product does not exist")
    public void testProductsDao_toReturnNull_whenNoProduct() throws DataPersistenceException {
        assertNull(productsDao.getProduct("Paint"), " paint is not in product list");
        assertNull(productsDao.getProduct("nails"), " nails is not in product list");
    }
}
