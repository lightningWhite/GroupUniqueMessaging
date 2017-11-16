package com.gum.dlt.groupuniquemessaging;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *  The code that was used to retrieve contacts is modified code
 *      from: https://gist.github.com/evandrix/7058235
 *
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    final String CONTACT_FILE = "savedContacts";
    final String CONTACT_KEY = "contactKey";
    private Uri uriContact;

    // All contacts are stored in this list
    ArrayList<Contact> _contactList;
    List<String> _templateVariableNames;

    // An adapter to interact between the _contactList and the contactListVeiw.
    ContactsAdapter _contactsAdapter;

    VariablesAdapter _variableAdapter;

    // contacts unique ID
    private String contactID;

    // variable for saving templates with a title
    private String _templateTitle = "";

    /**
     * Respond to the Load Template Button.
     */
    public void btnLoadTemplate (View view) {
        Intent intent = new Intent(this, MessageTemplateActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        // This makes it so the keyboard doesn't push up the buttons
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Allocate a list for the template message's variable block names
        _templateVariableNames = new ArrayList<>();

        // Load the SharedPreferences file containing the contacts that were saved for activity switches
        SharedPreferences contactPref = this.getSharedPreferences(CONTACT_FILE, MODE_PRIVATE);
        Gson gson = new Gson();

        // Get the stored json list of contacts. Note: This line prevents it from populating the list in the ContactListViewTask
        _contactList = gson.fromJson(contactPref.getString(CONTACT_KEY, null),
                new TypeToken<ArrayList<Contact>>() {
                }.getType());

        // If there are no saved contacts in the shared preferences, allocate a new ArrayList
        if (_contactList == null) {
            _contactList = new ArrayList<>();
        }

        // Create the array adapter so we can populate the contactListView
        _contactsAdapter = new ContactsAdapter(this, android.R.layout.simple_selectable_list_item, _contactList);
        final ListView contactListView = (ListView) findViewById(R.id.contactListView);
        contactListView.setAdapter(_contactsAdapter);

        // If there are contacts to load, populate the contact ListView with them (I don't think it ever goes here because of when we get the json)
        if (_contactList != null && _contactList.isEmpty()) {
            // Add the list of contacts to the contactListView in an AsyncTask for threading
            ContactListViewTask addContact = new ContactListViewTask(_contactsAdapter, _contactList,
                    MainActivity.this);
            addContact.execute();
            Log.i(TAG, "Contacts added.");
        }

        /* Listener for when a contact is selected to show the contact's variables.
        * This is also used to add the message and generated variables to the contact if the
        * selected contact was added after the generate variables button was clicked. */
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                Contact selectedFromList = (Contact) (contactListView.getItemAtPosition(myItemInt));
                Log.d("MainActivity", selectedFromList.get_contact());

                // Set the added contact's variables and message if the message exists and vars are generated and the blocks haven't been set already
                if(_templateVariableNames != null && (!_templateVariableNames.isEmpty()) && (!_contactList.get(myItemInt).get_varBlocksAdded())) {
                    // Get the last contact in the list since it is the most recently added contact and set its variables
                    _contactList.get(myItemInt).set_variable_block_names(_templateVariableNames);

                    // Get the template from the textBox
                    EditText textBox = (EditText) findViewById(R.id.editMessage);
                    Editable template = textBox.getText();
                    String templateString = template.toString();

                    // Create a message object to insert into the contact
                    Message message = new Message();
                    message.set_msg_template(templateString);

                    // Log the contact's variable blocks that were added to the contact
                    List<String> tempContactVars;
                    tempContactVars = _contactList.get(myItemInt).get_variables();
                    if (tempContactVars != null) {
                        for (String var : tempContactVars) {
                            Log.d(TAG, "This is the variable block added to the contact: " + var);
                        }
                    }
                }
            }
        });
    }

    /**
     * This method saves all of the contacts so they can be preserved when changing
     * between activities.
     */
    @Override
    public void onPause() {

        super.onPause();  // Always call the superclass method first
        SharedPreferences mPrefs = getSharedPreferences(CONTACT_FILE, MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();

        // Convert the contacts list into a json string
        String json = gson.toJson(_contactList);
        Log.d("MainActivity", json);

        prefsEditor.putString(CONTACT_KEY, json);
        prefsEditor.commit();
    }

    /**
     * This method starts an activity that gives the user access to the phone's stored contacts
     * so the user can select a contact to add to the list.
     * @param btnSelectContact
     */
    public void onClickSelectContact(View btnSelectContact) {

        Log.d("onClickSelectContact()", "setup");
        // using native contacts selection
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);

        // DUE TO THREADING THIS GETS EXECUTED BEFORE THE CONTACT IS EVEN ADDED
//        // Set the added contact's variables and message if the message exists and vars are generated
//        if(_templateVariableNames != null && (!_templateVariableNames.isEmpty()) && (!_contactList.isEmpty())) {
//            // Get the last contact in the list since it is the most recently added contact and set its variables
//            _contactList.get(_contactList.size() - 1).set_variable_block_names(_templateVariableNames);
//
//            // Get the template from the textBox
//            EditText textBox = (EditText) findViewById(R.id.editMessage);
//            Editable template = textBox.getText();
//            String templateString = template.toString();
//
//            // Create a message object to insert into the contact
//            Message message = new Message();
//            message.set_msg_template(templateString);
//
//            List<String> tempContactVars;
//            tempContactVars = _contactList.get(_contactList.size() - 1).get_variables();
//            if (tempContactVars != null) {
//                for (String var : tempContactVars) {
//                    Log.d(TAG, "THIS IS THE VAR FOR CONTACT ADDED: " + var);
//                }
//            }
//        }

    }

    /**
     * This method obtains the user-selected contact's name and phone number and adds it to the
     * contactListView.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Contact contact = new Contact();

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            String name = retrieveContactName();
            String number = retrieveContactNumber();

            contact = new Contact();
            contact.set_contact(name);
            contact.setPhoneNumber(number);
        }

        // Add the contact to the ListView
        _contactsAdapter.add(contact);
    }

    /**
     * This method obtains the phone number of the selected contact.
     * @return the contact's phone number
     */
    private String retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);

        return contactNumber;
    }

    /**
     * This method obtains the contact name from the selected contact.
     * @return the contact name
     */
    private String retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {
            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);

        return contactName;
    }

    /**
     * This method makes it so the user can select a contact in the contactListView and remove it.
     * @param btnSelectContact
     */
    public void onRemoveContact(View btnSelectContact) {
        if (!_contactList.isEmpty()) {
            int pos = _contactsAdapter.getSelectedItem();
            _contactList.remove(_contactList.remove(pos));
            _contactsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This method will get the names of the variable blocks and...
     * @param view
     */
    public void onGenerateVariables(View view) {

        // Get the template from the textBox
        EditText textBox = (EditText) findViewById(R.id.editMessage);
        Editable template = textBox.getText();
        String templateString = template.toString();

        // Create a message object to insert into the contact
        Log.d(TAG, "About to create the message for the contact...");
        Message message = new Message();
        Log.d(TAG, "About to set the message template in the message object...");
        message.set_msg_template(templateString);
        _templateVariableNames = message.get_variable_names_from_template();

        for (String variable: _templateVariableNames) {
            Log.d(TAG, "Parsed Variable: " + variable);
        }

        Log.d(TAG, "About to check if the _contactList is empty...");
        // Make sure there are contacts in the list
        if (!_contactList.isEmpty()) {
            // Set the contacts's variable block names for all contacts already in the contacts list
            Log.d(TAG, "About to set the message in each of the existing contacts...");
            for (Contact contact : _contactList) {
                contact.set_message(message);

                // Make sure we don't overwrite any potentially set contact variable values
                Log.d(TAG, "About to set the variable block names of the contact...");
                contact.set_variable_block_names(_templateVariableNames);
            }
        }
    }

    /**
     * This method will save the templates with a custom title
     *      which will cause a pop up box to appear
     */
    public void onSaveTemplates(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a title for the template");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _templateTitle = input.getText().toString();

                Log.i(TAG, "The template title is: " + _templateTitle);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
