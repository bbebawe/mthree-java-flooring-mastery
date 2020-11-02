/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery;

import com.mthree.flooringmastery.controller.FlooringMasteryController;
import com.mthree.flooringmastery.dao.DataPersistenceException;
import com.mthree.flooringmastery.service.NoEditDataExceprtion;
import com.mthree.flooringmastery.service.NoOrderFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author beshoy
 */
public class App {
    public static void main(String[] args) throws DataPersistenceException, NoOrderFoundException, NoEditDataExceprtion {
           ApplicationContext appContext
                = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        FlooringMasteryController controller = appContext.getBean("controller", FlooringMasteryController.class);
        controller.run();
    }
}
