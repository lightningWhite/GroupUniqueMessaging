package com.gum.dlt.groupuniquemessaging;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by logan on 12/8/2017.
 * These tests are for the Message class.
 */

public class MessageUnitTest {

    /**
     * This test verifies that the getVariableNamesFromTemplate retrieves the
     * correct variables and in the right order for a list.
     */
    @Test
    public void Test_get_variable_names_from_template() {
        String title = "The Title";
        String msgTemplate = "Hello <name> could you meet with bishop on <day> at <time>?";

        // Set the message title and message template with the constructor.
        Message msg = new Message(title, msgTemplate);

        // Call the function to add the variables from the message into your list.
        List<String> myList = msg.get_variable_names_from_template();

        // Verify your list matches with the original message.
        assertEquals(myList.get(0), "name");
        assertEquals(myList.get(1), "day");
        assertEquals(myList.get(2), "time");
    }

    /**
     * This test verifies the getMsgWithSetVaribales returns the message with set
     * variables and without the angled brackets.
     */
    @Test
    public void Test_get_msg_with_set_variables() {
        String title = "The Title";
        String msgTemplate = "Hello <contact> could you meet with bishop on <day> at <time>?";

        // Fill the list with variables from the message.
        Message msg = new Message(title, msgTemplate);
        List<String > myList = msg.get_variable_names_from_template();

        // Set the message to hold the actual message intended to be sent.
        String msgActual = msg.get_msg_with_set_variables(myList);
        
        assertEquals(msgActual, "Hello contact could you meet with bishop on day at time?");
    }
}
