package com.gum.dlt.groupuniquemessaging;

/**
 * Created by Daniel on 11/13/2017.
 * A class that provides functions to get the first name of a given contact.
 */
public class ContactParser {

    private String _contactName;
    private String _firstName;

    public String get_contactName() {
        return _contactName;
    }

    public void set_contactName(String _contactName) {
        this._contactName = _contactName;
    }

    /**
     * This method returns the first name of a passed contact name.
     * @param fullContactName
     * @return The first name of the given contact.
     */
    public String getContactFirstName(String fullContactName) {
        if (fullContactName != null) {
            _firstName = parse(fullContactName);
        }

        return _firstName;
    }

    /**
     * This method returns the first word in a string.
     * @param contactLabel
     * @return A String containing the first word in the contact string.
     */
    private String parse(String contactLabel) {
        if(contactLabel == null){
            return contactLabel;
        }

        String firstName = "";

        // Place each character of the variable into a string until end of block or end of string
        int i = 0;

        // Check for a space before the contact
        while (i < contactLabel.length() && contactLabel.charAt(i) == ' ')
            i++;

        // Parse the contact
        while (i < contactLabel.length() && contactLabel.charAt(i) != ' ') {
            firstName += contactLabel.charAt(i);
            // Go to the next letter in the variable
            i++;
        }
        return firstName;
    }
}
