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
        // First and Last Name
        String name = "John Doe";
        Contact contact = new Contact();
        contact.set_contactName(name);
        String expectedFirstName = "John";
        String actualFirstName = contact.get_firstName();
        assertEquals(expectedFirstName, actualFirstName);

        // One name
        String name2 = "John";
        Contact contact2 = new Contact();
        contact2.set_contactName(name);
        String expectedFirstName2 = "John";
        String actualFirstName2 = contact2.get_firstName();
        assertEquals(expectedFirstName2, actualFirstName2);
    }

    /**
     * Checks for a odd spaces in the contact
     * @throws Exception
     */
    @Test
    public void test_getFirstName_OddSpc() throws Exception{
        // Space before contact
        String name = " John Doe";
        Contact contact = new Contact();
        contact.set_contactName(name);
        String expectedFirstName = "John";
        String actualFirstName = contact.get_firstName();
        System.out.format("This is the the actual first name: %s", actualFirstName);
        assertEquals(expectedFirstName, actualFirstName);

        //Space after contact
        String name2 = "John Doe ";
        Contact contact2 = new Contact();
        contact2.set_contactName(name2);
        String expectedFirstName2 = "John";
        String actualFirstName2 = contact2.get_firstName();
        System.out.format("This is the the actual first name: %s", actualFirstName2);
        assertEquals(expectedFirstName2, actualFirstName2);

        //Space after contact
        String name3 = "John  Doe";
        Contact contact3 = new Contact();
        contact3.set_contactName(name3);
        String expectedFirstName3 = "John";
        String actualFirstName3 = contact.get_firstName();
        System.out.format("This is the the actual first name: %s", actualFirstName3);
        assertEquals(expectedFirstName3, actualFirstName3);
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
