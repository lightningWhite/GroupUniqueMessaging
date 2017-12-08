package com.gum.dlt.groupuniquemessaging;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by logan on 12/8/2017.
 */

public class MessageUnitTest {

    @Test
    public void Test_get_variable_names_from_template() {
        String title = "The Title";
        String msgTemplate = "Hello <name> could you meet with bishop on <day> at <time>?";

        Message msg = new Message(title, msgTemplate);

        List<String> myList = msg.get_variable_names_from_template();

        assertEquals(myList.get(0), "name");
        assertEquals(myList.get(1), "day");
        assertEquals(myList.get(2), "time");
    }

    @Test
    public void Test_get_msg_with_set_variables() {
        String title = "The Title";
        String msgTemplate = "Hello <contact> could you meet with bishop on <day> at <time>?";

        Message msg = new Message(title, msgTemplate);
        List<String > myList = msg.get_variable_names_from_template();

        msg.set_msg_template(msg._msgTemplate);
        String msgActual = msg.get_msg_with_set_variables(myList);

        assertEquals(msgActual, "Hello contact could you meet with bishop on day at time?");
    }
}
