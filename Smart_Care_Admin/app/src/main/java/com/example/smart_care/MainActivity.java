package com.example.smart_care;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smart_care.databinding.ActivityMainBinding;
import com.example.smart_care.model.Model_data;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SharedPreference sharedPreference;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ArrayList arrayList;
    private ActivityMainBinding binding;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        sharedPreference = SharedPreference.getPreferences(MainActivity.this);



        binding.tvForgotPass.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            final View mview = LayoutInflater.from(MainActivity.this).inflate(R.layout.forgot_id_help, null);
            builder.setView(mview);
            final AlertDialog dialog_condition = builder.create();
            dialog_condition.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog_condition.show();


        });

        binding.btSignIn.setOnClickListener(view1 -> {


            if (binding.devId.getText().toString().isEmpty())
            {

                binding.devId.setError("Invalid Id");

            }
            else if (!binding.adminRadio.isChecked()&&!binding.docRadio.isChecked()&&!binding.nurseRadio.isChecked())
            {
                Toast.makeText(this, "Select login type !!", Toast.LENGTH_LONG).show();
            }

            else {
                binding.loginProgress.setVisibility(View.VISIBLE);
                binding.btSignIn.setVisibility(View.GONE);

                firebaseDatabase=FirebaseDatabase.getInstance();
                databaseReference=firebaseDatabase.getReference("employee/"+binding.devId.getText().toString()+"/id");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //value match for login

                        if(binding.devId.getText().toString().equals(dataSnapshot.getValue(String.class)))
                        {

                            sharedPreference.setData("login");
                            if (binding.adminRadio.isChecked())
                            {
                                sharedPreference.setloginType("admin");
                            }else if (binding.docRadio.isChecked()){
                                sharedPreference.setloginType("doctor_id");
                            }
                            else if (binding.nurseRadio.isChecked()) {
                                sharedPreference.setloginType("nurse_id");
                            }

                            sharedPreference.setEmployeeKey(binding.devId.getText().toString());
                            Intent intent=new Intent(MainActivity.this,Admin.class);
                            intent.putExtra("id",dataSnapshot.getValue(String.class));
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {

                            loginFailed();
                            binding.loginProgress.setVisibility(View.GONE);
                            binding.btSignIn.setVisibility(View.VISIBLE);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                        Toast.makeText(MainActivity.this, "Something Wrong !", Toast.LENGTH_SHORT).show();

                    }
                });


            }





        });
    }


    public void loginFailed()
    {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar mSnackBar = Snackbar.make(parentLayout, "Login Failed ! ID Not Match", Snackbar.LENGTH_LONG);
        View view = mSnackBar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.RED);
        TextView mainTextView = (TextView) (view).findViewById(R.id.snackbar_text);
        mainTextView.setTextColor(Color.WHITE);
        mSnackBar.show();
    }


}