package com.gum.dlt.groupuniquemessaging;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Tayler on 11/1/2017.
 * These unit tests are for the MsgParser class.
 */

public class MsgParserUnitTest {

    /**
     * This test verifies that the getVariableNames method can successfully pull the names of each
     * variable block from the template message and return them in a List.
     * @throws Exception
     */
    @Test
    public void test_getVariableNames() throws Exception{

        // Normal case
        String msg1 = "Hello <name> will you come to <place>?";
        MsgParser parser1 = new MsgParser(msg1);
        List<String> variables = parser1.getVariableNames();
        assertEquals(variables.get(0), "name");
        assertEquals(variables.get(1), "place");

        // Missing end bracket in middle of message
        String msg2 = "Hello <name will you come to <place>?";
        MsgParser parser2 = new MsgParser(msg2);
        variables = parser2.getVariableNames();
        assertEquals(variables.get(0), "name will you come to <place");

        // Missing beginning bracket in middle of message
        String msg3 = "Hello name> will you come to <place>?";
        MsgParser parser3 = new MsgParser(msg3);
        variables = parser3.getVariableNames();
        assertEquals(variables.get(0), "place");

        // Missing missing closing bracket at end of message
        String msg4 = "Hello <name> will you come to <place";
        MsgParser parser4 = new MsgParser(msg4);
        variables = parser4.getVariableNames();
        assertEquals(variables.get(0), "name");
        assertEquals(variables.get(1), "place");

        // Missing missing closing bracket at end of message
        String msg5 = "";
        MsgParser parser5 = new MsgParser(msg5);
        variables = parser5.getVariableNames();
        assert(variables.isEmpty());
    }

    /**
     * This test verifies that when insertUserSetVariables receives a list of variables, it
     * can successfully insert them into the established variable blocks found in the
     * template message.
     * @throws Exception
     */
    @Test
    public void test_insertUserSetVariables() throws Exception {
        String name = "Bill";
        String place = "church";
        List<String> userVars = new ArrayList<>();
        userVars.add(name);
        userVars.add(place);

        // Normal case
        String msg1 = "Hello <name> will you come to <place>?";
        MsgParser parser = new MsgParser(msg1);
        String result = parser.insertUserSetVariables(userVars);
        String exp1 = "Hello Bill will you come to church?";
        assertEquals(exp1, result);

        // First variable missing closing bracket
        String msg2 = "Hello <name will you come to <place>?";
        MsgParser parser2 = new MsgParser(msg2);
        String result2 = parser2.insertUserSetVariables(userVars);
        String exp2 = "Hello Bill?";
        assertEquals(exp2, result2);

        // First variable missing closing bracket at end of string
        String msg3 = "Hello <name> will you come to <place";
        MsgParser parser3 = new MsgParser(msg3);
        String result3 = parser3.insertUserSetVariables(userVars);
        String exp3 = "Hello Bill will you come to church";
        assertEquals(exp3, result3);

        // First variable missing opening bracket
        String msg4 = "Hello name> will you come to <place>?";
        MsgParser parser4 = new MsgParser(msg4);
        String result4 = parser4.insertUserSetVariables(userVars);
        String exp4 = "Hello name> will you come to Bill?"; // The user should notice his error
        assertEquals(exp4, result4);
    }
}

