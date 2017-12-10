package com.gum.dlt.groupuniquemessaging;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tayler on 11/3/17
 *
 * Contact Class
 *      This class sets up all of the variables so that we can handle
 *      the contacts in the app.
 */
public class Contact {

    private String _contactName;
    private String _phoneNumber;
    private Message _message;
    private String _constructedMessage;
    private ContactParser _parser;
    private List<String> _variables;
    private boolean _varBlocksAdded;

    private String _firstName;

    public Contact() {
        super();
        _contactName = "";
        _phoneNumber = "";
        _message = new Message();
        _constructedMessage = "";
        _parser = new ContactParser();
        _parser.set_contactName(get_contactName());
        _variables = new ArrayList<>();
        _varBlocksAdded = false;
    }

    /**
     * Sets up the variables
     * @param contactName
     * @param phoneNumber
     */
    public Contact(String contactName, String phoneNumber){
        super();
        _contactName = contactName;
        _phoneNumber = phoneNumber;
        _parser = new ContactParser();
        _message = new Message();
       _firstName = _parser.getContactFirstName(contactName);
       _varBlocksAdded = false;
    }

    public String get_contactName() {
        return _contactName;
    }

    public void set_contactName(String _contactName) {
        this._contactName = _contactName;
    }

    public String getPhoneNumber() {
        return _phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this._phoneNumber = phoneNumber;
    }

    /**
     * Checks if there's a message and if there's not one create a new message
     * @return the message
     */
    public Message get_message() {
        if (_message == null) {
            _message = new Message();
        }
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
     * Parses the Contact and returns the first name
     * @return the first name
     */
    public String get_firstName() {
        //_parser.set_contactName(_contactName);
        Log.d("Contact", "THE CONTACT'S FULL NAME: " + get_contactName());
        _firstName = _parser.getContactFirstName(_contactName);
        Log.d("Contact", "THE CONTACT'S FIRST NAME: " + _firstName);

        return _firstName;
    }

    public void set_firstName(String _firstName) {
        this._firstName = _firstName;
    }

    /**
     * Insert the names of the variable blocks to the list of variables.
     * @param variables
     */
    public void set_variable_block_names(List<String> variables) {
        this._variables.clear();
        this._variables.addAll(variables);

        // Check if any of the variable blocks is a <contact> and set it to the contact's first name
        for (int i = 0; i < _variables.size(); i++) {
            if(_variables.get(i).equals("contact") || _variables.get(i).equals("Contact")) {
                // Get insert the first name at the index of the name block
                _variables.add(i, get_firstName());

                // Remove the variable block name because we set the variable now
                _variables.remove(i+1);
            }
        }

        // Indicate that the variable blocks names have been added
        if (!_variables.isEmpty()) {
            set_varBlocksAdded(true);
        }
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

    public boolean get_varBlocksAdded() {
        return _varBlocksAdded;
    }

    public void set_varBlocksAdded(boolean varBlocksAdded) {
        this._varBlocksAdded = varBlocksAdded;
    }

    public String get_constructedMessage() {
        _constructedMessage = _message.get_msg_with_set_variables(_variables);
        return _constructedMessage;
    }
}
