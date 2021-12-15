package com.example.photos;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Photos extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Album> currentalbum;
    private int addCode=1;
    private int editCode=0;
    private Album current;
    private int currentint=-1;
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
                            currentalbum = (ArrayList<Album>) bundle.getSerializable("album");
                            listView.setAdapter(
                                    new ArrayAdapter<Album>(getApplicationContext(), R.layout.albumname, currentalbum));
                            current=null;
                            currentint=-1;
                        }
                    }
                }
            });
    ActivityResultLauncher<Intent> someActivityResultLauncher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {


                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Bundle bundle = data.getExtras();
                            currentalbum = (ArrayList<Album>) bundle.getSerializable("entire");
                            listView.setAdapter(
                                    new ArrayAdapter<Album>(getApplicationContext(), R.layout.albumname, currentalbum));
                            current=null;
                            currentint=-1;
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  //load up page
        setContentView(R.layout.album);    //set xml
        currentint=-1;
        //set listview
        File cFile = new File(getApplicationContext().getFilesDir(),"data.dat");
        if (cFile.exists()) {
            FileInputStream fis = null;
            try {
                fis = getApplicationContext().openFileInput("data.dat");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                ObjectInputStream ois = new ObjectInputStream(fis);
                currentalbum = (ArrayList<Album>) ois.readObject();
                fis.close();
                ois.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            currentalbum = new ArrayList<Album>();
            FileOutputStream fos = null;
            try {
                fos = getApplicationContext().openFileOutput("data.dat", Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(currentalbum);
                oos.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        listView = findViewById(R.id.album_list);
        listView.setAdapter(
                new ArrayAdapter<Album>(this, R.layout.albumname, currentalbum));
        listView.setOnItemClickListener((p, V, pos, id) ->
                selectAlbums(p,pos, V));


    }

    private void selectAlbums(AdapterView<?> parent, int pos, View view) {

        if (currentint!=-1) {
            for (int i = 0; i <listView.getChildCount(); i++) {
                listView.getChildAt(i).setBackgroundColor(Color.WHITE);
            }
        }
        parent.getChildAt(pos-parent.getFirstVisiblePosition()).setBackgroundColor(Color.YELLOW);
        current = (Album)listView.getItemAtPosition(pos);
        for (int i=0;i<currentalbum.size();i++) {
            if (currentalbum.get(i).equals(current)){
                currentint=i;
            }
        }



    }
    public void showPhotos(View view) {
        Bundle bundle = new Bundle();
        if (current==null){
            Bundle bundle2 = new Bundle();
            bundle2.putString(PhotosDialogFragment.MESSAGE_KEY,
                    "No album selected");
            DialogFragment newFragment = new PhotosDialogFragment();
            newFragment.setArguments(bundle2);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }
        bundle.putSerializable("one",currentint);
        bundle.putSerializable("Album",current);
        bundle.putSerializable("entire",currentalbum);
        Intent intent = new Intent(this, SeeAlbum.class);
        intent.putExtras(bundle);
        someActivityResultLauncher2.launch(intent);
    }
    public void delete(View view) {
        if (current==null){
            Bundle bundle2 = new Bundle();
            bundle2.putString(PhotosDialogFragment.MESSAGE_KEY,
                    "No album selected");
            DialogFragment newFragment = new PhotosDialogFragment();
            newFragment.setArguments(bundle2);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }
        currentalbum.remove(current);
        current=null;
        listView.setAdapter(
                new ArrayAdapter<Album>(this, R.layout.albumname, currentalbum));
        FileOutputStream fos = null;
        try {
            fos = getApplicationContext().openFileOutput("data.dat", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(currentalbum);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void editAlbum(View view) {
        if (current==null){
            Bundle bundle2 = new Bundle();
            bundle2.putString(PhotosDialogFragment.MESSAGE_KEY,
                    "No album selected");
            DialogFragment newFragment = new PhotosDialogFragment();
            newFragment.setArguments(bundle2);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("current",currentint);
        bundle.putSerializable("album",currentalbum);
        Intent intent = new Intent(this, add_edit_album.class);
        intent.putExtras(bundle);
        someActivityResultLauncher.launch(intent);
    }

    public void addAlbum() {

        //Launch activity to get result
        Bundle bundle = new Bundle();
        bundle.putInt("current", -1);
        bundle.putSerializable("album", currentalbum);
        Intent intent = new Intent(this, add_edit_album.class);
        intent.putExtras(bundle);
        someActivityResultLauncher.launch(intent);
    }
    public void search(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("album", currentalbum);
        Intent intent = new Intent(this, search.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add:
                addAlbum();
                return true;
            case R.id.action_search:
                search();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}