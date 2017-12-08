package com.gum.dlt.groupuniquemessaging;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Tayler on 12/4/2017.
 * These are the unit test for the Contact class
 */

public class ContactUnitTest {

    /**
     * This test the get_firstName() method that parses the contact and
     * returns the first name.
     * @throws Exception
     */
    @Test
    public void test_getFirstName_normal() throws Exception{
        String name = "John Doe";
        Contact contact = new Contact();
        contact.set_contactName(name);
        String expectedFirstName = "John";
        String actualFirstName = contact.get_firstName();
        assertEquals(expectedFirstName, actualFirstName);
    }

    /**
     * Checks for a odd or wierd input from user
     * @throws Exception
     */
    @Test
    public void test_getFirstName_crazy() throws Exception{
        String name = " John Doe";
        Contact contact = new Contact();
        contact.set_contactName(name);
        String expectedFirstName = "John";
        String actualFirstName = contact.get_firstName();

        System.out.format("This is the the actual first name: %s", actualFirstName);

        assertEquals(expectedFirstName, actualFirstName);
    }

    /**
     * Checks for a null while getting the first name from the contact
     * @throws Exception
     */
    @Test
    public void test_getFirstName_null() throws Exception{
        String name = null;
        Contact contact = new Contact();
        contact.set_contactName(name);
        String expectedFirstName = null;
        String actualFirstName = contact.get_firstName();
        assertEquals(expectedFirstName, actualFirstName);
    }

    /**
     * Test if the contact parser gets the first name
     * @throws Exception
     */
    @Test
    public void test_set_variable_block_names() throws Exception{
        String case1 = "Contact";
        String case2 = "contact";
        String expected = "John";
        Contact contact = new Contact();
        contact.set_firstName("John");
        List<String> testVariables = new ArrayList<>();
        testVariables.add(case1);
        testVariables.add(case2);
        List<String> variables = new ArrayList<>();
        contact.set_variable_block_names(testVariables);
        for (int i = 0; i < variables.size(); i++){
            assertEquals(variables.get(i), expected);
        }

    }




}
