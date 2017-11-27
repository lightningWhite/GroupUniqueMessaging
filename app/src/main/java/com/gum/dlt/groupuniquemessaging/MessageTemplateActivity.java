package com.gum.dlt.groupuniquemessaging;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.*;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MessageTemplateActivity extends AppCompatActivity {

    final String TAG = "MessageTemplateActivity";

    // File to save templates
    final String TEMPLATE_FILE = "savedTemplates";

    // holds the saved title and template
    Map<String, String> _savedTemplates;

    Set<String> _titleSet;

    List<String> _titleString;

    ArrayAdapter<String> _titlesAdapter;

    int _selectedTemplatePosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_template);

        SharedPreferences mPrefs = getSharedPreferences(TEMPLATE_FILE, MODE_PRIVATE);

        //TODO: Check to see if shared pref is empty

        _savedTemplates = (Map<String, String>) mPrefs.getAll();

        _titleString = new ArrayList<>();

        _titleSet = _savedTemplates.keySet();

        String array[] = _titleSet.toArray(new String[_titleSet.size()]);

//        if(_titleString != null && !_titleString.isEmpty()){

            for (int i = 0; i < array.length; i++) {
                _titleString.add(array[i]);
            }

            _titlesAdapter = new ArrayAdapter(this, android.R.layout.simple_selectable_list_item, _titleString);
            final ListView messageTemplates = (ListView) findViewById(R.id.messageTemplates);
            messageTemplates.setAdapter(_titlesAdapter);

            // Listener for when a template is selected get the index of which template is selected.
            messageTemplates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                    String selectedFromList = (String) (messageTemplates.getItemAtPosition(myItemInt));
                    _selectedTemplatePosition = myItemInt;
                    Log.d(TAG, selectedFromList);
                }
            });
      //  }
//        else{
//            Toast toast = Toast.makeText(this, "EEROR: no templates saved", Toast.LENGTH_SHORT);
//            toast.show();
      //  }
    }

    /**
     * Removes a selected template from the shared preferences.
     */
    public void onDeleteTemplate(View view) {

        if (_titleString != null && !_titleString.isEmpty()) {
            // Get the title for the key
            final String templateTitle = _titleString.get(_selectedTemplatePosition);

            // Create a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(MessageTemplateActivity.this);
            builder.setTitle("Are you sure you want to delete the " + templateTitle + " template?");

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Get a reference to the shared preferences
                    SharedPreferences mPrefs = getSharedPreferences(TEMPLATE_FILE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = mPrefs.edit();

                    // Remove the template associated with the title key
                    editor.remove(templateTitle);
                    editor.commit();

                    // Update the ListView
                    _titleString.remove(_selectedTemplatePosition);
                    _titlesAdapter.notifyDataSetChanged();

                    Context context = getApplicationContext();
                    CharSequence text = "Template Deleted";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
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

    public void onViewSelectedTemplate(View view){

        // Allow access to the Shared Preferences to load the saved templates
        SharedPreferences mPrefs = getSharedPreferences(TEMPLATE_FILE, MODE_PRIVATE);

        if (_titleString != null && !_titleString.isEmpty()) {

            // Get the title for the key
            final String templateTitle = _titleString.get(_selectedTemplatePosition);

            // Gets the titles message
            final String template = _savedTemplates.get(templateTitle);

            // Create a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(MessageTemplateActivity.this);
            builder.setTitle(templateTitle);

            builder.setMessage(template);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();
        }
    }
}