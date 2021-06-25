package com.example.smart_care;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.smart_care.Adapter.Adapter_patientList;

import com.example.smart_care.databinding.ActivityAdminBinding;
import com.example.smart_care.model.Model_data;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private DatabaseReference mDatabase;
    DatabaseReference database_patient;
    Adapter_patientList adapter;
    SharedPreference sharedPreference;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPreference = SharedPreference.getPreferences(Admin.this);


        if (sharedPreference.getEmployeeKey().equals("admin123")) {
            binding.fab.setVisibility(View.VISIBLE);
        }

        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreference.setData("none");
                sharedPreference.setEmployeeKey("none");
                sharedPreference.setloginType("none");
                startActivity(new Intent(Admin.this, SplashActivity.class));

            }
        });

        database_patient = FirebaseDatabase.getInstance().getReference("patients");

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.searchButton.setVisibility(View.GONE);
                binding.searchCross.setVisibility(View.VISIBLE);
                binding.etSearch.setVisibility(View.VISIBLE);
                binding.tvAdmin2.setVisibility(View.GONE);


            }
        });

        binding.searchCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.searchCross.setVisibility(View.GONE);
                binding.searchButton.setVisibility(View.VISIBLE);
                binding.tvAdmin2.setVisibility(View.VISIBLE);
                binding.etSearch.setVisibility(View.GONE);
                binding.etSearch.getText().clear();
                hideKeyboard(Admin.this);


            }
        });

        get_data();


        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                processsearch(binding.etSearch.getText().toString());

            }
        });


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //goto new activity
                Intent intent = new Intent(Admin.this, AddDevice.class);
                startActivity(intent);

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        //stopPlayer();
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void processsearch(String s) {
        /*if (sharedPreference.getDevice_id().equals("admin123"))
        {*/


        FirebaseRecyclerOptions<Model_data> options =
                new FirebaseRecyclerOptions.Builder<Model_data>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("patients").orderByChild("patient_name").startAt(s).endAt(s + "\uf8ff"), Model_data.class)
                        .build();

        adapter = new Adapter_patientList(options, Admin.this);
        adapter.startListening();
        binding.patientRecyler.setAdapter(adapter);

     /*   }else {

            FirebaseRecyclerOptions<Model_data> options =
                    new FirebaseRecyclerOptions.Builder<Model_data>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("patients/"+sharedPreference.getDevice_id()).orderByChild("patient_name").startAt(s).endAt(s+"\uf8ff"), Model_data.class)
                            .build();

            adapter=new Adapter_patientList(options,Admin.this);
            adapter.startListening();
            binding.patientRecyler.setAdapter(adapter);
        }*/


    }


    public void get_data() {

        if (sharedPreference.getEmployeeKey().equals("admin123")) {

            binding.patientRecyler.setLayoutManager(new GridLayoutManager(Admin.this, 2, GridLayoutManager.VERTICAL, false));

            FirebaseRecyclerOptions<Model_data> options =
                    new FirebaseRecyclerOptions.Builder<Model_data>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("patients"), Model_data.class)
                            .build();

            adapter = new Adapter_patientList(options, Admin.this);
            binding.patientRecyler.setAdapter(adapter);
        } else {

            binding.patientRecyler.setLayoutManager(new GridLayoutManager(Admin.this, 2, GridLayoutManager.VERTICAL, false));

            FirebaseRecyclerOptions<Model_data> options1 =
                    new FirebaseRecyclerOptions.Builder<Model_data>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("patients").
                                    startAt(sharedPreference.getEmployeeKey()).orderByChild(sharedPreference.getloginType()).
                                    endAt(sharedPreference.getEmployeeKey() + "\uf8ff"), Model_data.class)
                            .build();


            adapter = new Adapter_patientList(options1, Admin.this);
            binding.patientRecyler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }


}