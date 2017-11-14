package com.gum.dlt.groupuniquemessaging;

/**
 * Created by Daniel on 11/13/2017.
 */

public class ContactParser {

    Contact _contact;
    String _firstName;

    ContactParser(Contact contact) {
        _contact = contact;
    }

    public String getContactFirstName() {
        _firstName = parse(_contact.get_contact());
        return _firstName;
    }

    private String parse(String contactLabel) {
        String firstName = "";

        // Place each character of the variable into a string until end of block or end of
        // string
        int i = 0;
        while (i < contactLabel.length() && contactLabel.charAt(i) != ' ') {
            firstName += contactLabel.charAt(i);
            // Go to the next letter in the variable
            i++;
        }
        return firstName;
    }
}
