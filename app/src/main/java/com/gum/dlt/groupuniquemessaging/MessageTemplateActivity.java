package com.gum.dlt.groupuniquemessaging;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

/**
 * Provides an interface for the user to see all saved templates, select and use one, and delele
 * a selected template.
 */
public class MessageTemplateActivity extends AppCompatActivity {

    final String TAG = "MessageTemplateActivity";

    // File to save templates
    final String TEMPLATE_FILE = "savedTemplates";

    // holds the saved title and template
    Map<String, String> _savedTemplates;

    // All of the titles contained in the SharedPreferences
    Set<String> _titleSet;

    // The list of all the titles of the saved templates
    List<String> _titleString;

    // The adapter for the Template ListView
    ArrayAdapter<String> _titlesAdapter;

    // The position of the selected template
    int _selectedTemplatePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_template);

        SharedPreferences mPrefs = getSharedPreferences(TEMPLATE_FILE, MODE_PRIVATE);

        if (mPrefs != null) {
            _savedTemplates = (Map<String, String>) mPrefs.getAll();

            _titleString = new ArrayList<>();

            _titleSet = _savedTemplates.keySet();

            String array[] = _titleSet.toArray(new String[_titleSet.size()]);

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
                    ListView templateListView = (ListView) findViewById(R.id.messageTemplates);
                    templateListView.setSelector(android.R.color.darker_gray);
                    Log.d(TAG, selectedFromList);
                }
            });
        }
        else{
            Toast toast = Toast.makeText(this, "No Saved Templates!", Toast.LENGTH_SHORT);
            toast.show();
        }
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

                    if (!_titleString.isEmpty()) {
                        if (_selectedTemplatePosition <= _titleString.size()) {
                            _titleString.remove(_titleString.remove(_selectedTemplatePosition));
                            _titlesAdapter.notifyDataSetChanged();
                        }

                        // Check if the selected contact position is past the size of the list
                        if (_selectedTemplatePosition >= _titleString.size()) {
                            // Make sure we don't set the position negative
                            _selectedTemplatePosition--;
                            _titlesAdapter.notifyDataSetChanged();

                            // A hack for unselecting the empty space where the deleted contact was
                            ListView templatesListView = (ListView) findViewById(R.id.messageTemplates);
                            templatesListView.setSelector(android.R.color.transparent);
                        }
                    }

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

    /**
     * Open a dialog that displays the content of the selected template.
     * @param view
     */
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

    /**
     * Uses the selected message from the message template list view, and puts it into the
     * main activity edit message box.
     */
    public void onUseSelectedTemplate(View view) {
        if (_titleString != null && !_titleString.isEmpty()) {
            Intent messageTemplate = new Intent(MessageTemplateActivity.this, MainActivity.class);
            // Get the title for the key
            final String templateTitle = _titleString.get(_selectedTemplatePosition);
            // Get the String message from the title.
            final String template = _savedTemplates.get(templateTitle);
            // Put the message into the intent then start the activity.
            messageTemplate.putExtra(String.valueOf(R.string.loadTemplate), template);
            startActivity(messageTemplate);

            Toast.makeText(this, "Template Loaded", Toast.LENGTH_SHORT).show();
        }
    }
}