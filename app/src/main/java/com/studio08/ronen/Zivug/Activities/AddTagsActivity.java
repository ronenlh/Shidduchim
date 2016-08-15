package com.studio08.ronen.Zivug.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.studio08.ronen.Zivug.R;

public class AddTagsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tags);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Tags);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_tags);
        textView.setAdapter(adapter);
    }


    // sample tags
    private static final String[] Tags = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };



}
