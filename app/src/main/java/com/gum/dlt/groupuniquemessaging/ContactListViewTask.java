package com.gum.dlt.groupuniquemessaging;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Tayler on 11/6/2017.
 */

public class ContactListViewTask extends AsyncTask<Void, String, Void> {

    ArrayAdapter<String> _arrayAdapter;
    List<Contact> _contactList;
    Context _currentContext;

    ContactListViewTask(ArrayAdapter<String> arrayAdapter, List<Contact> contactList, Context currentContext) {
        _arrayAdapter = arrayAdapter;
        _contactList = contactList;
        _currentContext = currentContext;
    }

    // This acts in the main thread. We initialize the progress bar to zero here
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // This class acts on a seperate thread. It cannot access the GUI
    @Override
    protected Void doInBackground(Void... params) {

        for (Contact contact: _contactList) {
            publishProgress(contact.get_contact());
        }

        return null;
    }

    // This runs on the main thread so it can modify the GUI
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        _arrayAdapter.add(values[0]);
    }

    // This class runs on the main thread
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}