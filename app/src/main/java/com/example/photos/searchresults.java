package com.example.photos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.lang.reflect.Array;
import java.util.ArrayList;

public class searchresults extends AppCompatActivity {
    private ArrayList<Photo>currentphoto;
    int currentint=-1;
    Photo current;
    private RecyclerView recyclerview;
    private CustomList adapter;
    int currentalbum;
    View view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_searchresults);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // activates the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentphoto= (ArrayList<Photo>) bundle.getSerializable("album");
        }
        recyclerview=(RecyclerView)findViewById(R.id.rv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomList(this, currentphoto);
        adapter.setClickListener(this::onItemClick);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
       finish();
       return true;
    }

    public void onItemClick(View view, int position) {
        if (view2!=null) {
            view2.setBackgroundColor(Color.WHITE);
        }
        view2=view;
        view2.setBackgroundColor(Color.YELLOW);
        current = (Photo)adapter.getItem(position);
        for (int i=0;i<currentphoto.size();i++) {
            if (currentphoto.get(i).equals(current)){
                currentint=i;
            }
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
        bundle.putInt("int",currentint);
        bundle.putSerializable("currentphoto",current);
        bundle.putSerializable("album",currentphoto);
        Intent intent = new Intent(this, searchresultsdisplay.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}