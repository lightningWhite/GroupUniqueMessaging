package com.gum.dlt.groupuniquemessaging;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * This class provides a threaded process to add a list of contacts to the contact ListView
 */
public class ContactListViewTask extends AsyncTask<Void, Contact, Void> {

    ContactsAdapter _contactsAdapter;
    List<Contact> _contacts;
    Context _currentContext;

    ContactListViewTask(ContactsAdapter contactsAdapter, List<Contact> contacts, Context currentContext) {
        _contactsAdapter = contactsAdapter;
        _contacts = contacts;
        _currentContext = currentContext;
    }

    // This acts in the main thread. We initialize the progress bar to zero here
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // This class acts on a separate thread. It cannot access the GUI
    @Override
    protected Void doInBackground(Void... params) {

        // Traverse through the list of contacts and publish them
        for (Contact contact: _contacts) {
            publishProgress(contact);
        }

        return null;
    }

    // This runs on the main thread so it can modify the GUI
    @Override
    protected void onProgressUpdate(Contact... values) {
        super.onProgressUpdate(values);
        // Add each contact to the ListView
        _contactsAdapter.add(values[0]);
    }

    // This class runs on the main thread
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
