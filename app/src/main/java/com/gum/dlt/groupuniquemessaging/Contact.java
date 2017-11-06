package com.gum.dlt.groupuniquemessaging;

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
    public Parser _parser;

    public void Contact(String contact, String phoneNumber){
        _contact = contact;
        _phoneNumber = phoneNumber;
        //_parser = new ContactParser(contact); // TODO: uncomment after ContactParser class is created
        _message = new Message();
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

    public Parser get_parser() {
        return _parser;
    }

    public void set_parser(Parser _parser) {
        this._parser = _parser;
    }
}
