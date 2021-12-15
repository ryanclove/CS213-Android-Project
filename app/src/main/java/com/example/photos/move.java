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
import android.widget.ListView;

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

public class move extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ListView listView;
    private Photo currents;
    private ArrayList<Album>entire;
    private int one;
    private int two;
    private Album current;
    private int currentint=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // activates the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the fields
        listView = findViewById(R.id.album_list);

        // see if info was passed in to populate fields
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currents=(Photo)bundle.getSerializable("currentphoto");
            entire=(ArrayList<Album>) bundle.getSerializable("entire");
            one=bundle.getInt("one");
            two=bundle.getInt("two");
        }
        listView.setAdapter(
                new ArrayAdapter<Album>(this, R.layout.albumname, entire));
        listView.setOnItemClickListener((p, V, pos, id) ->
                selectAlbums(p,pos, V));

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    private void selectAlbums(AdapterView<?> parent, int pos, View view) {

        if (currentint!=-1) {
            for (int i = 0; i <listView.getChildCount(); i++) {
                listView.getChildAt(i).setBackgroundColor(Color.WHITE);
            }
        }
        parent.getChildAt(pos-parent.getFirstVisiblePosition()).setBackgroundColor(Color.YELLOW);
        current = (Album)listView.getItemAtPosition(pos);
        for (int i=0;i<entire.size();i++) {
            if (entire.get(i).equals(current)){
                currentint=i;
            }
        }
    }
    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void save(View view) {
        // gather all data from text fields



        // pop up dialog if errors in input, and return
        // name and year are mandatory
        if (current==null ) {
            Bundle bundle = new Bundle();
            bundle.putString(PhotosDialogFragment.MESSAGE_KEY,
                    "Name of album required");
            DialogFragment newFragment = new PhotosDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return; // does not quit activity, just returns from method
        }
        if (currentint==one){
            Bundle bundle = new Bundle();
            bundle.putString(PhotosDialogFragment.MESSAGE_KEY,
                    "Cannot move to current album");
            DialogFragment newFragment = new PhotosDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return; // does not quit activity, just returns from method
        }
        entire.get(currentint).addPhoto(currents);
        entire.get(one).lib.remove(two);

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

        // make Bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable("entire",entire);

        // send back to caller
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish(); // pops activity from the call stack, returns to parent

    }
}