package com.gum.dlt.groupuniquemessaging;

import java.util.List;

/**
 * Created by Tayler on 11/3/17
 *
 * Contact Class
 *      This class sets up all of the variables so that we can handle
 *      the contacts in the app.
 */

public class Contact {

    public String _contact;
    public String _phoneNumber;
    public Message _message;
    public ContactParser _parser;
    public List<String> _variables;
    public Boolean _hasVarBlocks; // Check to see if variable block names have been inserted to
                                  // prevent overwriting them

    private String _firstName;

    public void Contact(String contact, String phoneNumber){
        _contact = contact;
        _phoneNumber = phoneNumber;
        _parser = new ContactParser(this);
        _message = new Message();
       _hasVarBlocks = false;
       _firstName = _parser.getContactFirstName();
    }

    public String get_contact() {
        return _contact;
    }

    public void set_contact(String _contact) {
        this._contact = _contact;
    }

    public String getPhoneNumber() {
        return _phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this._phoneNumber = phoneNumber;
    }

    public Message get_message() {
        return _message;
    }

    public void set_message(Message _message) {
        this._message = _message;
    }

    public ContactParser get_parser() {
        return _parser;
    }

    public List<String> get_variables() {
        return _variables;
    }

    /**
     * Insert the names of the variable blocks to the list of variables.
     * @param _variables
     */
    public void set_variable_block_names(List<String> _variables) {
        this._variables = _variables;

        // Check if any of the variable blocks is a <contact> and set it to the contact's first name
        for (int i = 0; i < _variables.size(); i++) {
            if(_variables.get(i) == "contact" || _variables.get(i) == "Contact") {
                // Get insert the first name at the index of the name block
                _variables.add(i, _firstName);

                // Remove the variable block name because we set the variable now
                _variables.remove(i+1);
            }
        }

        // Indicate that the variable names have been set
        if (!_variables.isEmpty()) {
            _hasVarBlocks = true;
        }
    }

    public Boolean get_hasVarBlocks() {
        return _hasVarBlocks;
    }

    public void set_hasVarBlocks(Boolean setHasVarBlocks) {
        _hasVarBlocks = setHasVarBlocks;
    }

    /**
     * This method will take a variable and the index of the variable in the list of variable
     * It will insert the variable in front of the variable block and then delete the variable
     * block.
     * @param variable
     * @param index
     */
    public void set_variableAtIndex(String variable, int index) {
        _variables.add(index, variable);
        _variables.remove(index + 1);
    }
}
