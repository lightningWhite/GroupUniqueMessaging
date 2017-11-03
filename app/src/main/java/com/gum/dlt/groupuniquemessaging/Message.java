package com.gum.dlt.groupuniquemessaging;

import java.util.List;

/**
 * Created by Daniel on 10/30/2017.
 * This class contains all of the information associated with a Message.
 */

public class Message {
    public String _msgTitle;
    public List<String> _variables;
    public String _msgTemplate;
    public String _msgWithSetVariables;
    public MsgParser _parser; // TODO: This should be declared as Parser so we can program to an interface instead of a class


    /**
     * Constructor to receive a title of the message as well as the message template. It
     * also createa a Parser object to be used for inserting and obtaining the variables
     * into and from the message template.
     * @param msgTitle
     * @param msgTemplate
     */
    public void Message(String msgTitle, String msgTemplate) {
        _msgTitle = msgTitle;
        _msgTemplate = msgTemplate;
        _parser = new MsgParser(_msgTemplate);
    }

    public String get_msgTitle() {
        return _msgTitle;
    }

    public void set_msgTitle(String _msgTitle) {
        this._msgTitle = _msgTitle;
    }


    /**
     * This method will obtain the names of the variable blocks and return them in a List.
     * @return a list of variable names from the template message
     */
    public List<String> get_variable_names_from_template() {
        return _parser.getVariableNames();
    }

    /**
     * Getter for the message template contained in this object.
     * @return a string containing the message template.
     */
    public String get_msg_template() {
        return _msgTemplate;
    }

    /**
     * This method sets the member variable containing the message template.
     * @param _msgWithVariables
     */
    public void set_msg_template(String _msgWithVariables) {
        this._msgTemplate = _msgWithVariables;
    }

    /**
     * This method uses the parser object to insert a list of user-set variables into the message
     * template and returns the newly formed message without any variable blocks.
     * @return the message with all variable blocks set to the user defined values.
     */
    public String get_msg_with_set_variables(List<String> variables) {
        _msgWithSetVariables = _parser.getMessageWithSetVars(variables);
        return _msgWithSetVariables;
    }
}
