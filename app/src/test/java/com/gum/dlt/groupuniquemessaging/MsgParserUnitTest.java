package com.gum.dlt.groupuniquemessaging;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Tayler on 11/1/2017.
 * These unit tests are for the MsgParser class.
 */

public class MsgParserUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_getVar_names() throws Exception{

        // Normal case
        String msg1 = "Hello <name> will you come to <place>?";
        MsgParser parser = new MsgParser(msg1);
        List<String> variables = parser.getVariableNames();
        assertEquals(variables.get(0), "name");
        assertEquals(variables.get(1), "place");

        // Missing end bracket in middle of message
        String msg2 = "Hello <name will you come to <place>?";
        MsgParser parser1 = new MsgParser(msg2);
        variables = parser1.getVariableNames();
        assertEquals(variables.get(0), "name will you come to <place");

        // Missing beginning bracket in middle of message
        String msg3 = "Hello name> will you come to <place>?";
        MsgParser parser2 = new MsgParser(msg3);
        variables = parser2.getVariableNames();
        assertEquals(variables.get(0), "place");

        // Missing missing closing bracket at end of message
        String msg4 = "Hello <name> will you come to <place";
        MsgParser parser3 = new MsgParser(msg4);
        variables = parser3.getVariableNames();
        assertEquals(variables.get(0), "name");
        assertEquals(variables.get(1), "place");
    }
}

