package com.example.photos;

import android.app.Activity;
import android.content.Context;
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


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class display_photo extends AppCompatActivity {
    private ImageView imageview;
    private ListView listView2;
    private int currentint=-1;
    private ArrayList<Tag> currentTag;
    private ArrayList<Album> entire;
    Photo currents;
    Tag current;
    int one;
    int two;
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Bundle bundle = data.getExtras();
                            ArrayList<Tag>currentagalbum = (ArrayList<Tag>) bundle.getSerializable("album");
                            entire = (ArrayList<Album>) bundle.getSerializable("entire");
                            currentTag=currentagalbum;
                            listView2.setAdapter(
                                    new ArrayAdapter<Tag>(getApplicationContext(), R.layout.tagname, currentTag));
                            current=null;
                            currentint=-1;
                        }
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photo);

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
            entire=(ArrayList<Album>) bundle.getSerializable("entire");
            one=bundle.getInt("one");
            two=bundle.getInt("two");
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
        Bundle bundle = new Bundle();
        bundle.putSerializable("entire",entire);
        Intent intent = new Intent(this, SeeAlbum.class);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
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
        if (two>=entire.get(one).lib.size()-1){
            return;
        }
        two++;
        currents=entire.get(one).lib.get(two);
        Bitmap bmImg = BitmapFactory.decodeFile(currents.getLocation());
        imageview.setImageBitmap(bmImg);
        currentTag=currents.getLib();
        listView2.setAdapter(
                new ArrayAdapter<Tag>(this, R.layout.tagname, currentTag));


    }
    public void backward(View view) {     //save data somehow
        if (two==0){
            return;
        }
        else{
            two--;
            currents=entire.get(one).lib.get(two);
            Bitmap bmImg = BitmapFactory.decodeFile(currents.getLocation());
            imageview.setImageBitmap(bmImg);
            currentTag=currents.getLib();
            listView2.setAdapter(
                    new ArrayAdapter<Tag>(this, R.layout.tagname, currentTag));

        }

    }
    public void add(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("one",one);
        bundle.putInt("two",two);
        bundle.putSerializable("currentPhoto", currentTag);
        bundle.putSerializable("entire", entire);
        Intent intent = new Intent(this, add_tag.class);
        intent.putExtras(bundle);
        someActivityResultLauncher.launch(intent);
    }
    public void delete(View view) {
        if (current==null){
            Bundle bundle2 = new Bundle();
            bundle2.putString(PhotosDialogFragment.MESSAGE_KEY,
                    "No tag selected");
            DialogFragment newFragment = new PhotosDialogFragment();
            newFragment.setArguments(bundle2);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }
        currentTag.remove(current);
        entire.get(one).lib.get(two).setLib(currentTag);
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
        current=null;
        listView2.setAdapter(
                new ArrayAdapter<Tag>(this, R.layout.tagname, currentTag));
    }


}