package com.gum.dlt.groupuniquemessaging;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * This class provides a threaded process to add a list of contacts to the contact ListView
 */
public class ContactListViewTask extends AsyncTask<Void, String, Void> {

    ArrayAdapter<String> _arrayAdapter;
    List<Contact> _contacts;
    Context _currentContext;

    ContactListViewTask(ArrayAdapter<String> arrayAdapter, List<Contact> contacts, Context currentContext) {
        _arrayAdapter = arrayAdapter;
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

        // Traverse throught the list of contacts and publish them
        for (Contact contact: _contacts) {
            publishProgress(contact.get_contact());
        }

        return null;
    }

    // This runs on the main thread so it can modify the GUI
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        // Add each contact to the ListView
        _arrayAdapter.add(values[0]);
    }

    // This class runs on the main thread
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
