package com.example.photos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class add_tag extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private EditText tagName;
    private ArrayList<Tag> a;
    private ArrayList<Album> entire;
    private String current;
    private int one;
    private int two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // activates the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tagName = findViewById(R.id.tag_name);
        Spinner spinner = (Spinner) findViewById(R.id.tag_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tag_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                current = (String) parent.getItemAtPosition(pos);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            a= (ArrayList<Tag>) bundle.getSerializable("currentPhoto");
            entire=(ArrayList<Album>) bundle.getSerializable("entire");
            one=bundle.getInt("one");
            two=bundle.getInt("two");
        }

    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void save(View view) {
        // gather all data from text fields
        String name = tagName.getText().toString().trim();


        // pop up dialog if errors in input, and return
        // name and year are mandatory
        if (name == null || name.length() == 0 ) {
            Bundle bundle = new Bundle();
            bundle.putString(PhotosDialogFragment.MESSAGE_KEY,
                    "Name of tag value required");
            DialogFragment newFragment = new PhotosDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return; // does not quit activity, just returns from method
        }
        for (int i=0;i<a.size();i++){
            if (a.get(i).getName().equalsIgnoreCase(current)){
                if (a.get(i).getValue().equalsIgnoreCase(name)){
                    Bundle bundle = new Bundle();
                    bundle.putString(PhotosDialogFragment.MESSAGE_KEY,
                            "Same tag already exists");
                    DialogFragment newFragment = new PhotosDialogFragment();
                    newFragment.setArguments(bundle);
                    newFragment.show(getSupportFragmentManager(), "badfields");
                    return; // does not quit activity, just returns from method
                }
            }
        }
        for (int i=0;i<a.size();i++){      //location only one allowed
            if (a.get(i).getName().equalsIgnoreCase("Location") && current.equalsIgnoreCase("Location")){
                Bundle bundle = new Bundle();
                bundle.putString(PhotosDialogFragment.MESSAGE_KEY,
                        "Only one location tag allowed");
                DialogFragment newFragment = new PhotosDialogFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "badfields");
                return; // does not quit activity, just returns from method

            }
        }
        a.add(new Tag(current,name));


        // make Bundle
        Bundle bundle = new Bundle();
        entire.get(one).lib.get(two).setLib(a);
        FileOutputStream fos = null;
        try {
            fos = getApplicationContext().openFileOutput("data.dat", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(entire);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bundle.putSerializable("album",a);
        bundle.putSerializable("entire",entire);
        // send back to caller
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish(); // pops activity from the call stack, returns to parent

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}