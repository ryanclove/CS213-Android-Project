package com.example.photos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class add_edit_album extends AppCompatActivity {


    private int photoIndex;
    private ArrayList<Album> a;
    private EditText albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // activates the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the fields
        albumName = findViewById(R.id.album_name);

        // see if info was passed in to populate fields
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            photoIndex = bundle.getInt("current");
            a= (ArrayList<Album>) bundle.getSerializable("album");
            if (photoIndex!=-1){
                albumName.setText(a.get(photoIndex).name);
            }
        }
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void save(View view) {
        // gather all data from text fields
        String name = albumName.getText().toString().trim();


        // pop up dialog if errors in input, and return
        // name and year are mandatory
        if (name == null || name.length() == 0 ) {
            Bundle bundle = new Bundle();
            bundle.putString(PhotosDialogFragment.MESSAGE_KEY,
                    "Name of album required");
            DialogFragment newFragment = new PhotosDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return; // does not quit activity, just returns from method
        }
        for (int i=0;i<a.size();i++){
            if (a.get(i).name.equalsIgnoreCase(name)){
                Bundle bundle = new Bundle();
                bundle.putString(PhotosDialogFragment.MESSAGE_KEY,
                        "Name already taken, case insensitive");
                DialogFragment newFragment = new PhotosDialogFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "badfields");
                return; // does not quit activity, just returns from method
            }
        }
        if (photoIndex==-1){
            a.add(new Album(name));
        }
        else{
            a.get(photoIndex).name=name;
        }
        FileOutputStream fos = null;
        try {
            fos = getApplicationContext().openFileOutput("data.dat", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(a);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // make Bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable("album",a);

        // send back to caller
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish(); // pops activity from the call stack, returns to parent

    }
}