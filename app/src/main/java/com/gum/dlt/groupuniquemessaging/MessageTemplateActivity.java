package com.gum.dlt.groupuniquemessaging;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MessageTemplateActivity extends AppCompatActivity {

    final String TAG = "MessageTemplateActivity";

    // File to save templates
    final String TEMPLATE_FILE = "savedTemplates";

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

        for (int i = 0; i < array.length; i++){
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
    }

    /**
     * Removes a selected template from the shared preferences.
     * @param view
     */
    public void onDeleteTemplate(View view) {

        if (_titleString != null && !_titleString.isEmpty()) {
            // Get the title for the key
            String templateTitle = _titleString.get(_selectedTemplatePosition);

            SharedPreferences mPrefs = getSharedPreferences(TEMPLATE_FILE, MODE_PRIVATE);
            SharedPreferences.Editor editor = mPrefs.edit();

            // Remove the template associated with the title key
            editor.remove(templateTitle);
            editor.commit();

            _titleString.remove(_selectedTemplatePosition);
            _titlesAdapter.notifyDataSetChanged();
        }
    }
}
