package com.gum.dlt.groupuniquemessaging;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by tayle on 11/1/2017.
 */

public class MsgParserUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_getVar_names() throws Exception{
        String msg1 = "Hello <name> with you come to <place>?";
        MsgParser parser = new MsgParser(msg1);
        List<String> variables = parser.getVariableNames();
        assertEquals(variables.get(0), "name");
        assertEquals(variables.get(1), "place");

        String msg2 = "Hello <name with you come to <place>?";
        MsgParser parser1 = new MsgParser(msg2);
        variables = parser1.getVariableNames();
        assertEquals(variables.get(0), "name with you come to <place");



    }
}

