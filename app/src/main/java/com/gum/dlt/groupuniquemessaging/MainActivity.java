package com.gum.dlt.groupuniquemessaging;

import android.Manifest;
import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * The central hub of the Group Unique Messaging Application. This is where the user
 * inputs the template message, selects contacts, sets variables, and sends the messages.
 * @author Tayler Baker
 * @author Daniel Hornberger
 * @author Logan Summers
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    final String CONTACT_FILE = "savedContacts";
    final String CONTACT_KEY = "contactKey";
    private Uri uriContact;

    // File to save templates
    final String TEMPLATE_FILE = "savedTemplates";

    // All contacts are stored in this list
    List<Contact> _contactList;

    // The generated variable block names stored here
    List<String> _generatedTemplateVarNames;

    // Contains the variables from the selected contact to be displayed in the var ListView
    List<String> _variablesList;

    // An adapter to interact between the _contactList and the contactListVeiw.
    ContactsAdapter _contactsAdapter;

    ArrayAdapter<String> _variablesAdapter;
    // contacts unique ID
    private String _contactID;

    // variable for saving templates with a title
    private String _templateTitle = "";

    // name for entered variable
    private String _varName = "";

    // our selected contact
    private int _selectedContactPosition;

    // our selected variable
    private int _selectedVarPosition;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This is when you load a saved template message.
        final EditText editText = (EditText) findViewById(R.id.editMessage);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Get the location of the EditText field and populate it with the message.
            String message = bundle.getString(String.valueOf(R.string.loadTemplate));
            editText.setText(message);
        }

        // This makes it so the keyboard doesn't push up the buttons
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);

        // Allocate a list for the template message's variable block names
        _generatedTemplateVarNames = new ArrayList<>();

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

        // Create the contacts adapter so we can populate the contactListView
        _contactsAdapter = new ContactsAdapter(this, android.R.layout.simple_selectable_list_item, _contactList);
        final ListView contactListView = (ListView) findViewById(R.id.contactListView);
        contactListView.setAdapter(_contactsAdapter);

        // Allocate list for the variablesList items
        _variablesList =  new ArrayList<>();

        // Create the contacts adapter so we can populate the variablesListView
        _variablesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, _variablesList);
        final ListView variablesListView = (ListView) findViewById(R.id.variablesListView);
        variablesListView.setAdapter(_variablesAdapter);

        // If there are contacts to load, populate the contact ListView with them (I don't think it ever goes here because of when we get the json)
        if (_contactList != null && _contactList.isEmpty()) {
            // Add the list of contacts to the contactListView in an AsyncTask for threading
            ContactListViewTask addContact = new ContactListViewTask(_contactsAdapter, _contactList,
                    MainActivity.this);
            addContact.execute();
            Log.i(TAG, "Contacts added.");
        }

        /*
         * Listener for when a contact is selected to show the contact's variables. It sets
         * the _selectedContactPosition and displays the selected contact's variables in the
         * variables listView.
         * */
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                Contact selectedFromList = (Contact) (contactListView.getItemAtPosition(myItemInt));
                _selectedContactPosition = myItemInt;
                Log.d("MainActivity", selectedFromList.get_contactName());

                // A hack for changing the contact listView highlighting from transparent to grey
                // after deleting the last item in the list
                contactListView.setSelector(android.R.color.darker_gray);

                Log.d(TAG, "ABOUT TO REASSIGN THE VARIABLES!!!");
                _variablesAdapter.clear();
                _variablesList.addAll(_contactList.get(myItemInt).get_variables());
                _variablesAdapter.notifyDataSetChanged();

                for(String var: _variablesList) {
                    Log.d(TAG, "THE VAR IS!!! : " + var);
                }
            }
        });

        // Create the array adapter so we can populate the variableList
        final ListView variableListView = (ListView) findViewById(R.id.variablesListView);
        variableListView.setAdapter(_variablesAdapter);

        /**
         * Listener for when a variable is selected to give the popup window for input.
         */
        variableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                _selectedVarPosition = position;
                String varName = _variablesList.get(_selectedVarPosition);
                if (varName.equals("time") || varName.equals("Time")) {
                    generateTimePopUp();
                }
                else if(varName.equals("day") || varName.equals("Day")){
                    generateDayPopUp();
                }
                else {
                    generateVarPopUp();
                }
            }
        });

        // Listener for when focus changes from the EditText view in order to hide the keyboard
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    /**
     * Generates a time selector popup with regular text input.
     */
    public void generateTimePopUp() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Enter a Time");

        // Set up the input
        final EditText input = new EditText(MainActivity.this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        dialog.setView(input);

        // Set up the Ok button.
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                _varName = input.getText().toString();

                onClickOkVariables();
            }
        })

                // Set up the Cancel button.
                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    /**
     * Generates a day selector popup with a day drop-down menu.
     */
    public void generateDayPopUp() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Select a Day");

        // Set up the input
        final Spinner input = new Spinner(MainActivity.this);
        ArrayAdapter<String> adapter;
        List<String> list;

        list = new ArrayList<String>();
        list.add("Sunday");
        list.add("Monday");
        list.add("Tuesday");
        list.add("Wednesday");
        list.add("Thursday");
        list.add("Friday");
        list.add("Saturday");
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input.setAdapter(adapter);
        dialog.setView(input);

        // Set up the Ok button.
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                _varName = input.getSelectedItem().toString();

                onClickOkVariables();
            }
        })

                // Set up the Cancel button.
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();

    }

    /**
     * Generates a generic variable input popup with regular text input.
     */
    public void generateVarPopUp() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        String var = _variablesList.get(_selectedVarPosition);
        dialog.setTitle("Enter a Value for" + " " + var);

        // Set up the input
        final EditText input = new EditText(MainActivity.this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        dialog.setView(input);

        // Set up the Ok button.
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                _varName = input.getText().toString();

                // If the user enters an empty string for a var, replace it with the original var
                if (_varName.isEmpty()){
                    if(!(_generatedTemplateVarNames.get(_selectedVarPosition).equals("contact")
                        || _generatedTemplateVarNames.get(_selectedVarPosition).equals("Contact"))) {
                        _varName = _generatedTemplateVarNames.get(_selectedVarPosition);
                    }

                    else{
                        _varName = _variablesList.get(_selectedVarPosition);
                    }
                }
                onClickOkVariables();
            }
        })

                // Set up the Cancel button.
                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    /**
     * Sets the selected contact's variable to the user's input.
     */
    public void onClickOkVariables() {
        _contactList.get(_selectedContactPosition).set_variableAtIndex(_varName, _selectedVarPosition);
        _variablesList.clear();
        _variablesList.addAll(_contactList.get(_selectedContactPosition).get_variables());
        _variablesAdapter.notifyDataSetChanged();
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

            // Set the new contact's name and numbers
            contact = new Contact();
            contact.set_contactName(name);
            contact.setPhoneNumber(number);

            // Set the contact's message to the current message in the EditMessage box
            EditText editText = (EditText) findViewById(R.id.editMessage);
            Editable editable = editText.getText();
            String templateString = editable.toString();

            // Set the contact's variables to the previously generated variables, if any
            Message message = new Message();
            message.set_msg_template(templateString);
            contact.set_message(message);
            contact.set_variable_block_names(_generatedTemplateVarNames);
        }

        // Add the contact to the ListView
        _contactsAdapter.add(contact);
    }

    /**
     * This method obtains the phone number of the selected contact.
     * The code that was used to retrieve contacts is modified code
     * from: https://gist.github.com/evandrix/7058235
     * @return the contact's phone number
     */
    private String retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            _contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + _contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{_contactID},
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

        if (cursor != null && cursor.moveToFirst()) {
            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            cursor.close();
        }

        Log.d(TAG, "Contact Name: " + contactName);

        return contactName;
    }

    /**
     * This method makes it so the user can select a contact in the contactListView and remove it.
     */
    public void onRemoveContact(View btnSelectContact) {
        if (!_contactList.isEmpty()) {
            if (_selectedContactPosition <= _contactList.size()) {
                _contactList.remove(_contactList.remove(_selectedContactPosition));
                _contactsAdapter.notifyDataSetChanged();
                if (_contactList.isEmpty()) {
                    _variablesList.clear();
                    _variablesAdapter.notifyDataSetChanged();
                }
            }

            // Check if the selected contact position is past the size of the list
            if (_selectedContactPosition >= _contactList.size()) {
                // Make sure we don't set the position negative
                _selectedContactPosition--;
                _variablesList.clear();
                _variablesAdapter.notifyDataSetChanged();

                // A hack for unselecting the empty space where the deleted contact was
                ListView contactListView = (ListView) findViewById(R.id.contactListView);
                contactListView.setSelector(android.R.color.transparent);
            }

        }
    }

    /**
     * This method will get the names of the variable blocks and save them in a global list so
     * they can be added to all the current and future contacts in the contact list.
     */
    public void onGenerateVariables(View view) {

        // Hide the keyboard when this button is clicked
        EditText editText = (EditText) findViewById(R.id.editMessage);
        if (editText.hasFocus()) {
            editText.clearFocus();
        }

        // Get the template from the textBox
        EditText textBox = (EditText) findViewById(R.id.editMessage);
        Editable template = textBox.getText();
        String templateString = template.toString();

        // Create a message object to insert into the contact
        Log.d(TAG, "About to create the message for the contact...");
        Message message = new Message();
        Log.d(TAG, "About to set the message template in the message object...");
        message.set_msg_template(templateString);
        _generatedTemplateVarNames = message.get_variable_names_from_template();

        for (String variable: _generatedTemplateVarNames) {
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
                contact.set_variable_block_names(_generatedTemplateVarNames);
            }
        }
    }

    /**
     * This method will save the templates with a custom title
     *      which will cause a pop up box to appear
     *      Using modified code from https://stackoverflow.com/questions/10903754/input-text-dialog-android
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

                // passes template title to onSaveListen
                onSaveListen();
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

    /**
     *  This method takes the Template title and Template message and saves
     *      them using Shared Preferences
     */
    public void onSaveListen(){
        EditText template = (EditText) findViewById(R.id.editMessage);

        String templateMessage;

        // get the template message
        templateMessage = template.getText().toString();

        SharedPreferences mPrefs = getSharedPreferences(TEMPLATE_FILE, MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        prefsEditor.putString(_templateTitle, templateMessage);

        Log.i(TAG, "The template title is: " + _templateTitle);

        // gets all titles and messages
        mPrefs.getAll();

        Log.i(TAG, "The template message is: " + templateMessage);

        prefsEditor.apply();

        Toast.makeText(this, "Template Saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Open the MessageTemplateActivity
     */
    public void btnLoadTemplate (View view) {
        Intent intent = new Intent(this, MessageTemplateActivity.class);
        startActivity(intent);
    }

    /**
     * Serially loops through all of the contacts in the list and sends their message and
     * phone number to the user-selected messaging app.
     */
    public void onSendMessage(View view) {

        // Loop through all of the contacts in the list and send each respective message to the messaging app
        for (Contact contact: _contactList) {
            String number = contact.getPhoneNumber();
            String msgString = contact.get_constructedMessage();

            Uri uri = Uri.parse("smsto:" + number);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
            sendIntent.putExtra("sms_body", msgString);
            startActivity(sendIntent);
        }
    }

    /**
     * Hides the keyboard when the focus changes from the edit text view.
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
