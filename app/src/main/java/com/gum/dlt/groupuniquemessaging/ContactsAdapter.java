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
