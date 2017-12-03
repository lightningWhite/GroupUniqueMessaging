package com.gum.dlt.groupuniquemessaging;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Daniel on 11/13/2017.
 * Unit Tests for the ContactParser class.
 */
public class ContactParserUnitTest {

    /**
     * Verify that getContactFirstName returns the first name of the contact.
     * @throws Exception
     */
    @Test
    public void test_getContactFirstName() throws Exception {
        ContactParser contactParser = new ContactParser();

        String testContact = "Billy Bob Jones";
        String expectedFirstName = "Billy";

        String actualFirstName = contactParser.getContactFirstName(testContact);

        assertEquals(expectedFirstName, actualFirstName);
    }

    /**
     * Tests the parse function to verify that it functions properly with multiple words.
     * @throws Exception
     */
    @Test
    public void test_parse_multiple_words() throws Exception {
        ContactParser contactParser = new ContactParser();

        String testString = "Billy Bob Jones";
        String expectedString = "Billy";

        String actualString = contactParser.getContactFirstName(testString);

        assertEquals(expectedString, actualString);
    }

    /**
     * Tests the parse function to verify that it functions properly with one word.
     * @throws Exception
     */
    @Test
    public void test_parse_single_word() throws Exception {
        ContactParser contactParser = new ContactParser();

        String testString = "Billy";
        String expectedString = "Billy";

        String actualString = contactParser.getContactFirstName(testString);

        assertEquals(expectedString, actualString);
    }

    /**
     * Tests the parse function to verify that it functions properly with and empty string.
     * @throws Exception
     */
    @Test
    public void test_parse_empty_string() throws Exception {
        ContactParser contactParser = new ContactParser();

        String testString = "";
        String expectedString = "";

        String actualString = contactParser.getContactFirstName(testString);

        assertEquals(expectedString, actualString);
    }

    /**
     * Tests the parse function to verify that it functions properly with a null string.
     * @throws Exception
     */
    @Test
    public void test_parse_null() throws Exception {
        ContactParser contactParser = new ContactParser();

        String testString = null;
        String expectedString = null;

        String actualString = contactParser.getContactFirstName(testString);

        assertEquals(expectedString, actualString);
    }

    /**
     * Tests the parse function to verify that it functions properly with a null string.
     * @throws Exception
     */
    @Test
    public void test_parse_space_before() throws Exception {
        ContactParser contactParser = new ContactParser();

        String testString = " Billy";
        String expectedString = " Billy";

        String actualString = contactParser.getContactFirstName(testString);

        assertEquals(expectedString, actualString);
    }
}
