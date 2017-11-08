package com.gum.dlt.groupuniquemessaging;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by tayle on 11/6/2017.
 */

public class ContactListViewTask extends AsyncTask<Void, String, Void> {

    ArrayAdapter<String> _arrayAdapter;
    Contact _contact;
    Context _currentContext;

    ContactListViewTask(ArrayAdapter<String> arrayAdapter, Contact contact, Context currentContext) {
        _arrayAdapter = arrayAdapter;
        _contact = contact;
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

        Log.d("ContactListViewTask", _contact.get_contact());
        publishProgress(_contact.get_contact());

        return null;
    }

    // This runs on the main thread so it can modify the GUI
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d("dslkjsdf;lkj", Integer.toString(values.length));
        _arrayAdapter.addAll(values);
    }

    // This class runs on the main thread
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
