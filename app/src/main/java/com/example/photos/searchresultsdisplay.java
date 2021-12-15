package com.example.photos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;



import java.util.ArrayList;

public class searchresultsdisplay extends AppCompatActivity {
    private ImageView imageview;
    private ListView listView2;
    private int currentint=-1;
    private ArrayList<Tag> currentTag;
    private ArrayList<Photo> entire;
    Photo currents;
    Tag current;
    int one;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresultsdisplay);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // activates the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView2 = findViewById(R.id.listview);
        imageview = findViewById(R.id.imageView3);


        // see if info was passed in to populate fields
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            currents=(Photo)bundle.getSerializable("currentphoto");
            entire=(ArrayList<Photo>) bundle.getSerializable("album");
            one=bundle.getInt("int");
            Bitmap bmImg = BitmapFactory.decodeFile(currents.getLocation());
            imageview.setImageBitmap(bmImg);
            currentTag=currents.getLib();
            listView2.setAdapter(
                    new ArrayAdapter<Tag>(this, R.layout.tagname, currentTag));
            listView2.setOnItemClickListener((p, V, pos, id) ->
                    selectTags(p,pos,V));
        }


    }
    @Override
    public boolean onSupportNavigateUp() {
        // do your stuff
        finish();
        return true;
    }

    private void selectTags(AdapterView<?> parent, int pos, View view) {

        if (currentint!=-1) {
            for (int i = 0; i <listView2.getChildCount(); i++) {
                listView2.getChildAt(i).setBackgroundColor(Color.WHITE);
            }
        }
        parent.getChildAt(pos-parent.getFirstVisiblePosition()).setBackgroundColor(Color.YELLOW);
        current = (Tag)listView2.getItemAtPosition(pos);
        for (int i=0;i<currentTag.size();i++) {
            if (currentTag.get(i).equals(current)){
                currentint=i;
            }
        }
    }
    public void forward(View view) {
        if (one>=entire.size()-1){
            return;
        }
        one++;
        currents=entire.get(one);
        Bitmap bmImg = BitmapFactory.decodeFile(currents.getLocation());
        imageview.setImageBitmap(bmImg);
        currentTag=currents.getLib();
        listView2.setAdapter(
                new ArrayAdapter<Tag>(this, R.layout.tagname, currentTag));


    }
    public void backward(View view) {     //save data somehow
        if (one==0){
            return;
        }
        else{
            one--;
            currents=entire.get(one);
            Bitmap bmImg = BitmapFactory.decodeFile(currents.getLocation());
            imageview.setImageBitmap(bmImg);
            currentTag=currents.getLib();
            listView2.setAdapter(
                    new ArrayAdapter<Tag>(this, R.layout.tagname, currentTag));

        }

    }



}