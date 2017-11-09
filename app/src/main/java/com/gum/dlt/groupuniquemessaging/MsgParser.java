package com.gum.dlt.groupuniquemessaging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 10/30/2017.
 * This class provides functionality to pull variable blocks from a message as well as insert
 * variables in the place of variable blocks.
 */
public class MsgParser {

    String _message;
    List<String> _variables;
    String _msgWithSetVariables;

    public MsgParser(String message) {
        _message = message;
        _variables = new ArrayList<>();
    }

    /**
     * This method will return the names of all of the variables contained within the passed
     * message.
     * @return List<String>
     */
    public List<String> getVariableNames() {
        parse(_message);
        return _variables;
    }

    /**
     * This method takes a list of user-set variables and returns the message with these set
     * variables in place of the variable blocks.
     * @param vars
     * @return _msgWithSetVariables
     */
    public String getMessageWithSetVars(List<String> vars) {
        _msgWithSetVariables = insertUserSetVariables(vars);
        return _msgWithSetVariables;
    }

    /**
     * This method inserts the passed user-set variables in place of the variable blocks and
     * sets the _msgWithSetVariables to the new message.
     * @param vars
     */
    public String insertUserSetVariables(List<String> vars) {

        StringBuilder stringBuilder = new StringBuilder(_message);
        String variable = "";
        int varIndex = 0;

        // Loop through each character in the message looking for variable blocks
        for (int i = 0; i < stringBuilder.length(); i++) {
            char c = stringBuilder.charAt(i);

            // Search for the beginning of a variable block
            if (c == '<') {
                int startPos = i;
                // Delete the variable block
                while (i < stringBuilder.length() && stringBuilder.charAt(i) != '>') {
                    stringBuilder.deleteCharAt(i);
                }

                // Delete the closing angle bracket
                if (i < stringBuilder.length()) {
                    stringBuilder.deleteCharAt(i);
                }

                // Insert the user-set variable
                stringBuilder.insert(startPos, vars.get(varIndex));
                varIndex++;
            }
        }

        // Return the message with the user-set variables inserted
        return stringBuilder.toString();
    }

    /**
     * This function reads through the message, locates the variable blocks indicated
     * by <> brackets, and adds the name of the variable block to the _variables List.
     * @param msg
     */
    public List<String> parse(String msg) {
        String variable = "";

        // Loop through each character in the message looking for variable blocks
        for (int i = 0; i < msg.length(); i++) {
            char c = msg.charAt(i);

            // Search for the beginning of a variable block
            if (c == '<') {
                // Go to the first letter of the variable
                i++;

                // Place each character of the variable into a string until end of block or end of
                // string
                while (i < msg.length() && msg.charAt(i) != '>') {
                    variable += msg.charAt(i);
                    // Go to the next letter in the variable
                    i++;
                }
                // Add the variable to the list
                _variables.add(variable);
                variable = "";
            }
        }
        return _variables;
    }
}
