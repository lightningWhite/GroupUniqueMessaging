package com.gum.dlt.groupuniquemessaging;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Daniel on 11/9/2017.
 * This class provides a custom ArrayAdapter for a List of Contacts. This enables us to
 * bind the list in the MainActivity to this adapter so the GUI list will always reflect
 * what is actually in the list.
 *
 * This code was patterned after the example found at this URL:
 * https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */

public class ContactsAdapter extends ArrayAdapter<Contact> {

    private static class ViewHolder {
        private TextView itemView;
    }

    public ContactsAdapter(Context context, int textViewResourceId, ArrayList<Contact> items) {
        super(context, textViewResourceId, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contact, parent, false);
        }
        // Lookup view for data population
        TextView contactName = (TextView) convertView.findViewById(R.id.contactName);
        // Populate the data into the template view using the data object
        contactName.setText(contact.get_contact());
        // Return the completed view to render on screen
        return convertView;
    }
}
