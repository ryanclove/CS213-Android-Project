package com.example.photos;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.photos.databinding.ActivitySearchBinding;

import java.util.ArrayList;

public class search extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySearchBinding binding;
    private String first;
    private String second;
    private String combo;
    EditText e;
    TextView t;
    private ArrayList<Album> entire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // activates the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            entire= (ArrayList<Album>) bundle.getSerializable("album");
        }
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
                first = (String) parent.getItemAtPosition(pos);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Spinner spinner2 = (Spinner) findViewById(R.id.tag_spinner2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.tag_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                second = (String) parent.getItemAtPosition(pos);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        e=(EditText) findViewById(R.id.tag_name2);
        t=(TextView) findViewById(R.id.name);
        Spinner spinner3 = (Spinner) findViewById(R.id.tag_spinnercombo);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.tag_array2, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                combo = (String) parent.getItemAtPosition(pos);
                if (combo.equals("1 Tag")){
                    spinner2.setVisibility(view.GONE);
                    e.setVisibility(view.GONE);
                    t.setVisibility(view.GONE);
                }
                else{
                    spinner2.setVisibility(view.VISIBLE);
                    e.setVisibility(view.VISIBLE);
                    t.setVisibility(view.VISIBLE);

                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }
    public void search(View view){
        ArrayList<Photo>results=new ArrayList<Photo>();
        if (combo.equals("1 Tag")){
            EditText tempo= findViewById(R.id.tag_name);
            String other=tempo.getText().toString().trim();
            if (other == null || other.length() == 0 ) {
                Bundle bundle = new Bundle();
                bundle.putString(PhotosDialogFragment.MESSAGE_KEY,
                        "Name of value required");
                DialogFragment newFragment = new PhotosDialogFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "badfields");
                return; // does not quit activity, just returns from method
            }
            Tag temp=new Tag(first,other);
            for (int i=0;i<entire.size();i++){
                for (int j=0;j<entire.get(i).lib.size();j++){
                    for (int k=0;k<entire.get(i).lib.get(j).getLib().size();k++){
                        Log.d("qqqq","qqqq");
                        if (temp.getName().equalsIgnoreCase(entire.get(i).lib.get(j).getLib().get(k).getName())){
                            if (substrings(entire.get(i).lib.get(j).getLib().get(k).getValue(),temp.getValue())==true){
                                results.add(entire.get(i).lib.get(j));
                                break;
                            }
                        }
                    }
                }
            }
        }
        else if (combo.equals("AND")){
            boolean flag=false;
            boolean flag2=false;
            EditText tempo= findViewById(R.id.tag_name);
            String other=tempo.getText().toString().trim();
            Tag temp=new Tag(first,other);
            String other2=e.getText().toString().trim();
            if (other == null || other.length() == 0 ) {
                Bundle bundle = new Bundle();
                bundle.putString(PhotosDialogFragment.MESSAGE_KEY,
                        "Name of value required");
                DialogFragment newFragment = new PhotosDialogFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "badfields");
                return; // does not quit activity, just returns from method
            }
            if (other2 == null || other2.length() == 0 ) {
                Bundle bundle = new Bundle();
                bundle.putString(PhotosDialogFragment.MESSAGE_KEY,
                        "Name of value required");
                DialogFragment newFragment = new PhotosDialogFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "badfields");
                return; // does not quit activity, just returns from method
            }
            Tag temp2=new Tag(second,other2);
            for (int i=0;i<entire.size();i++){
                for (int j=0;j<entire.get(i).lib.size();j++){
                    for (int k=0;k<entire.get(i).lib.get(j).getLib().size();k++){
                        Log.d("qqqq","qqqq");
                        if (temp.getName().equalsIgnoreCase(entire.get(i).lib.get(j).getLib().get(k).getName())){
                            if (substrings(entire.get(i).lib.get(j).getLib().get(k).getValue(),temp.getValue())==true){
                                flag=true;
                            }
                        }
                        if (temp2.getName().equalsIgnoreCase(entire.get(i).lib.get(j).getLib().get(k).getName())){
                            if (substrings(entire.get(i).lib.get(j).getLib().get(k).getValue(),temp2.getValue())==true){
                                flag2=true;

                            }
                        }
                    }
                    if (flag==true && flag2==true){
                        results.add(entire.get(i).lib.get(j));
                        flag=false;
                        flag2=false;
                        continue;
                    }
                    flag=false;
                    flag2=false;
                }
            }

        }
        else{
            int flag=0;
            EditText tempo= findViewById(R.id.tag_name);
            String other=tempo.getText().toString().trim();
            Tag temp=new Tag(first,other);
            String other2=e.getText().toString().trim();
            if (other == null || other.length() == 0 ) {
                Bundle bundle = new Bundle();
                bundle.putString(PhotosDialogFragment.MESSAGE_KEY,
                        "Name of value required");
                DialogFragment newFragment = new PhotosDialogFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "badfields");
                return; // does not quit activity, just returns from method
            }
            if (other2 == null || other2.length() == 0 ) {
                Bundle bundle = new Bundle();
                bundle.putString(PhotosDialogFragment.MESSAGE_KEY,
                        "Name of value required");
                DialogFragment newFragment = new PhotosDialogFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "badfields");
                return; // does not quit activity, just returns from method
            }
            Tag temp2=new Tag(second,other2);
            for (int i=0;i<entire.size();i++){
                for (int j=0;j<entire.get(i).lib.size();j++){
                    for (int k=0;k<entire.get(i).lib.get(j).getLib().size();k++){
                        Log.d("qqqq","qqqq");
                        if (temp.getName().equalsIgnoreCase(entire.get(i).lib.get(j).getLib().get(k).getName())){
                            if (substrings(entire.get(i).lib.get(j).getLib().get(k).getValue(),temp.getValue())==true){
                                results.add(entire.get(i).lib.get(j));
                                flag=1;
                                break;
                            }
                        }
                        if (temp2.getName().equalsIgnoreCase(entire.get(i).lib.get(j).getLib().get(k).getName())){
                            if (substrings(entire.get(i).lib.get(j).getLib().get(k).getValue(),temp2.getValue())==true){
                                results.add(entire.get(i).lib.get(j));
                                flag=1;
                                break;
                            }
                        }
                    }
                    if (flag==1){
                        flag=0;
                        continue;
                    }
                    flag=0;
                }
            }
        }
        if (results.size()==0){
            Bundle bundle2 = new Bundle();
            bundle2.putString(PhotosDialogFragment.MESSAGE_KEY,
                    "No items match your search");
            DialogFragment newFragment = new PhotosDialogFragment();
            newFragment.setArguments(bundle2);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("album", results);
        Intent intent = new Intent(this, searchresults.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public boolean substrings(String a, String b){
        if (b.length()>a.length()){
            return false;
        }
        for (int i=0;i<b.length();i++){
            if (!a.substring(i,i+1).equalsIgnoreCase(b.substring(i,i+1))){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}