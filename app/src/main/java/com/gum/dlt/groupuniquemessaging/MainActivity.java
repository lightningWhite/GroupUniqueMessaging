package com.gum.dlt.groupuniquemessaging;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;

    List<Contact> contactList;


    // items nessecary to fill the contactList
    //List<Integer> numberList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    // contacts unique ID
    private String contactID;

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

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);

        contactList = new ArrayList<>();
        // arrayAdapter to put contact name into contactList
        //arrayAdapter = new ArrayAdapter<Integer>(this, R.layout.contact_list_item, numberList);
        //ListView listView = (ListView) findViewById(R.id.contactList);
        //listView.setAdapter(arrayAdapter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item);
        final ListView contactListView = (ListView) findViewById(R.id.contactListView);
        contactListView.setAdapter(arrayAdapter);

        // Todo: We need to figure out how to select a contact and remove it from the list
//
//        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
//                String selectedFromList =(String) (contactListView.getItemAtPosition(myItemInt));
//                Log.d("MainActivity", selectedFromList);
//            }
//        });
    }

    public void onClickSelectContact(View btnSelectContact) {

        Log.d("onClickSelectContact()", "setup");
        // using native contacts selection
        // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);

    }


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

            contactList.add(contact);
        }

        Log.d("MainActivity", "About to allocate the ContactListViewTask");
        ContactListViewTask addContact = new ContactListViewTask(arrayAdapter, contact, MainActivity.this);
        Log.d("MainActivity", "About to execute the task");
        addContact.execute();
    }

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
            //TextView textView = (TextView) findViewById(R.id.textView4);
            //textView.setText(contactNumber);
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);

        return contactNumber;
    }

    private String retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //TextView textView = (TextView) findViewById(R.id.contactList);
            //textView.setText(contactName);


        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);

        return contactName;
    }

}
