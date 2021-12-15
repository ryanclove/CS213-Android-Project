package com.example.photos;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SeeAlbum extends AppCompatActivity implements CustomList.ItemClickListener{


    private ArrayList<Photo> currentphoto;
    private ArrayList<Album> entire;
    int currentint=-1;
    Photo current;
    private RecyclerView recyclerview;
    private CustomList adapter;
    int currentalbum;
    View view2;

    ActivityResultLauncher<Intent> someActivityResultLauncherPhoto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Log.d("dsad","SAda");
                        Intent data = result.getData();
                        if (data != null) {
                            Uri chosenImageUri = data.getData();
                            Photo temporary=new Photo(createCopyAndReturnRealPath(getApplicationContext(),chosenImageUri));
                            temporary.setUri(chosenImageUri.getPath());

                            for (int i=0;i<entire.size();i++){
                                for (int j=0;j<entire.get(i).lib.size();j++){
                                    if (entire.get(i).getLib().get(j).getUri().equalsIgnoreCase(temporary.getUri())){
                                        Bundle bundle2 = new Bundle();
                                        bundle2.putString(PhotosDialogFragment.MESSAGE_KEY,
                                                "Photo exists in an album already.");
                                        DialogFragment newFragment = new PhotosDialogFragment();
                                        newFragment.setArguments(bundle2);
                                        newFragment.show(getSupportFragmentManager(), "badfields");
                                        return;
                                    }
                                }
                            }


                            currentphoto.add(temporary);
                            entire.get(currentalbum).lib=currentphoto;
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
                            adapter.notifyDataSetChanged();
                            recyclerview.setAdapter(adapter);
                            current=null;
                            currentint=-1;
                        }
                    }
                }
            });
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
                            entire = (ArrayList<Album>) bundle.getSerializable("entire");
                            currentphoto=entire.get(currentalbum).getLib();
                            recyclerview=(RecyclerView)findViewById(R.id.rv);
                            recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            adapter = new CustomList(getApplicationContext(), currentphoto);
                            adapter.setClickListener(SeeAlbum.this::onItemClick);
                            recyclerview.setAdapter(adapter);
                            current=null;
                            currentint=-1;

                        }
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // activates the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the fields
        //listView2 = findViewById(R.id.photo_list);


        // see if info was passed in to populate fields
        Bundle bundle = getIntent().getExtras();
        Album current=(Album)bundle.getSerializable("Album");
        entire=(ArrayList<Album>)bundle.getSerializable("entire");
        currentalbum=bundle.getInt("one");

        currentphoto=current.getLib();
        if (bundle != null) {
            recyclerview=(RecyclerView)findViewById(R.id.rv);
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CustomList(this, currentphoto);
            adapter.setClickListener(this);
            recyclerview.setAdapter(adapter);

            /*
            listView2.setOnItemClickListener((p, V, pos, id) ->
                    selectPhotos(p,pos,V));

             */
        }
    }
    @Override
    public void onItemClick(View view, int position) {
        /*
        if (currentint!=-1) {
            for (int i = 0; i <listView2.getChildCount(); i++) {
                listView2.getChildAt(i).setBackgroundColor(Color.WHITE);
            }
        }
        /*
         */
        //view.getChildAt(pos-parent.getFirstVisiblePosition()).setBackgroundColor(Color.YELLOW);
        //adapter.notifyDataSetChanged();
        if (view2!=null) {
            view2.setBackgroundColor(Color.WHITE);
        }
        view2=view;
        view2.setBackgroundColor(Color.YELLOW);
/*
        if (currentint!=-1) {
            for (int i = 0; i <adapter.getItemCount(); i++) {
                adapter.getChildAt(i).setBackgroundColor(Color.WHITE);
            }
        }
        parent.getChildAt(pos-parent.getFirstVisiblePosition()).setBackgroundColor(Color.YELLOW);


 */

        current = (Photo)adapter.getItem(position);
        for (int i=0;i<currentphoto.size();i++) {
            if (currentphoto.get(i).equals(current)){
                currentint=i;
                Log.d("sada","asd");
            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        // do your stuff
        Bundle bundle = new Bundle();
        bundle.putSerializable("entire",entire);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
        return true;
    }

    /*
    private void selectPhotos(AdapterView<?> parent, int pos, View view) {

        if (currentint!=-1) {
            for (int i = 0; i <listView2.getChildCount(); i++) {
                listView2.getChildAt(i).setBackgroundColor(Color.WHITE);
            }
        }
        parent.getChildAt(pos-parent.getFirstVisiblePosition()).setBackgroundColor(Color.YELLOW);
        current = (Photo)listView2.getItemAtPosition(pos);
        for (int i=0;i<currentphoto.size();i++) {
            if (currentphoto.get(i).equals(current)){
                currentint=i;
            }
        }
    }
    */

    public void add(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
        someActivityResultLauncherPhoto.launch(intent);
        adapter.notifyDataSetChanged();
        recyclerview.setAdapter(adapter);
    }
    public void delete(View view) {
        if (current==null){
            Bundle bundle2 = new Bundle();
            bundle2.putString(PhotosDialogFragment.MESSAGE_KEY,
                    "No photo selected");
            DialogFragment newFragment = new PhotosDialogFragment();
            newFragment.setArguments(bundle2);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }
        currentphoto.remove(current);
        entire.get(currentalbum).lib=currentphoto;
        current=null;
        adapter.notifyDataSetChanged();
        recyclerview.setAdapter(adapter);
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
    }
    public void display(View view) {
        if (current==null){
            Bundle bundle2 = new Bundle();
            bundle2.putString(PhotosDialogFragment.MESSAGE_KEY,
                    "No photo selected");
            DialogFragment newFragment = new PhotosDialogFragment();
            newFragment.setArguments(bundle2);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("two",currentint);
        bundle.putInt("one",currentalbum);
        bundle.putSerializable("entire",entire);
        bundle.putSerializable("currentphoto",current);
        Intent intent = new Intent(this, display_photo.class);
        intent.putExtras(bundle);
        someActivityResultLauncher.launch(intent);

    }
    public void move(View view) {
        if (current==null){
            Bundle bundle2 = new Bundle();
            bundle2.putString(PhotosDialogFragment.MESSAGE_KEY,
                    "No photo selected");
            DialogFragment newFragment = new PhotosDialogFragment();
            newFragment.setArguments(bundle2);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("two",currentint);
        bundle.putInt("one",currentalbum);
        bundle.putSerializable("entire",entire);
        bundle.putSerializable("currentphoto",current);
        Intent intent = new Intent(this, move.class);
        intent.putExtras(bundle);
        someActivityResultLauncher.launch(intent);

    }
    @Nullable
    public static String createCopyAndReturnRealPath(
            @NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return null;

        // Create file path inside app's data dir
        String filePath = context.getApplicationInfo().dataDir + File.separator
                + System.currentTimeMillis();

        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null)
                return null;

            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);

            outputStream.close();
            inputStream.close();
        } catch (IOException ignore) {
            return null;
        }

        return file.getAbsolutePath();
    }






}

