package com.example.smart_care;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smart_care.databinding.ActivityAddDeviceBinding;
import com.example.smart_care.databinding.ActivityAdminBinding;
import com.example.smart_care.databinding.ActivityPatientDetailsBinding;
import com.example.smart_care.model.Model_data;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddDevice extends AppCompatActivity {

    private ActivityAddDeviceBinding binding;
    private DatabaseReference mDatabase;
    DatabaseReference database_patient;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDeviceBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseDatabase=FirebaseDatabase.getInstance();
        database_patient = FirebaseDatabase.getInstance().getReference("patients");

        binding.backAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String get_name = binding.patientName.getText().toString().trim();
                String get_age = binding.patientAge.getText().toString().trim();
                String get_weight = binding.patientWeight.getText().toString().trim();
                String get_monitor_id = binding.monitorId.getText().toString().trim();
                String get_dev_id = binding.devId.getText().toString().trim();
                String get_patient_phone = binding.patientPhone.getText().toString().trim();
                String get_nurse_id = binding.nurseId.getText().toString().trim();

                String get_gender;
                if (binding.male.isChecked())
                {
                    get_gender="Male";
                }else {

                    get_gender="Female";
                }


                if (binding.patientName.getText().toString().trim().isEmpty()
                        ||binding.patientAge.getText().toString().trim().isEmpty()||
                      binding.patientWeight.getText().toString().trim().isEmpty()
                        ||binding.monitorId.getText().toString().trim().isEmpty()||
                        binding.devId.getText().toString().trim().isEmpty()||
                        binding.nurseId.getText().toString().trim().isEmpty()||
                        binding.patientPhone.getText().toString().trim().isEmpty()
                        ||(!binding.male.isChecked()
                        && !binding.female.isChecked())
                ) {

                    error();


                } else {


                    String database_path = get_dev_id;
                    Model_data model_data = new Model_data("0",get_monitor_id,get_name,get_age,get_weight,get_gender,get_dev_id,get_patient_phone,get_nurse_id);
                    database_patient.child(database_path).setValue(model_data);

                    share_data();


            /*        binding.patientName.getText().clear();
                    binding.patientAge.getText().clear();
                    binding.nurseId.getText().clear();
                    binding.patientPhone.getText().clear();
                    binding.patientWeight.getText().clear();
                    binding.monitorId.getText().clear();
                    binding.devId.getText().clear();
                    binding.male.setChecked(false);
                    binding.female.setChecked(false);*/






                }


            }
        });
    }

    public void error()
    {

        View parentLayout = findViewById(android.R.id.content);
        Snackbar mSnackBar = Snackbar.make(parentLayout, "Any field can't be empty", Snackbar.LENGTH_LONG);
        View view = mSnackBar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.RED);
        TextView mainTextView = (TextView) (view).findViewById(R.id.snackbar_text);
        mainTextView.setTextColor(Color.WHITE);
        mSnackBar.show();
    }

    public void share_data()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddDevice.this);
        final View mview = LayoutInflater.from(AddDevice.this).inflate(R.layout.share_data, null);
        builder.setView(mview);
        final AlertDialog dialog_condition = builder.create();
        dialog_condition.show();

        TextView assign_name,assign_phone,attach_name,attach_phone;
        Button share_patient,imformed_nurse;

        assign_name=mview.findViewById(R.id.assign_name);
        assign_phone=mview.findViewById(R.id.assign_phone);
        attach_name=mview.findViewById(R.id.attached_name);
        attach_phone=mview.findViewById(R.id.attached_phone);
        share_patient=mview.findViewById(R.id.share_to_patient);
        imformed_nurse=mview.findViewById(R.id.inform_nurse);


        attach_name.setText(binding.patientName.getText().toString());
        attach_phone.setText(binding.patientPhone.getText().toString());





        databaseReference=firebaseDatabase.getReference("employee/"+binding.nurseId.getText().toString()+"/name");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                assign_name.setText("  "+dataSnapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference=firebaseDatabase.getReference("employee/"+binding.nurseId.getText().toString()+"/cell");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                assign_phone.setText("  "+dataSnapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        share_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){



                        try {

                            String sms="Dear Patient your device key is "+binding.devId.getText().toString()+"\nDon't share this key with other.\n\nGet Well Soon\nSmart Care";

                            SmsManager smsManager=SmsManager.getDefault();
                            smsManager.sendTextMessage(attach_phone.getText().toString(),null,sms,null,null);

                            Toast.makeText(AddDevice.this, "Sms Successfully send", Toast.LENGTH_LONG).show();
                        }catch (Exception e)
                        {

                            Toast.makeText(AddDevice.this, "Failed to send sms !", Toast.LENGTH_LONG).show();
                        }


                    }
                    else {

                        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                    }
                }



            }
        });

        imformed_nurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sms="A new Patient is assign to you. Please take care of him/her and keep monitoring via app.\nThanks you";

                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){



                        try {


                            SmsManager smsManager=SmsManager.getDefault();
                            smsManager.sendTextMessage(assign_phone.getText().toString(),null,sms,null,null);

                            Toast.makeText(AddDevice.this, "Sms Successfully send", Toast.LENGTH_LONG).show();
                        }catch (Exception e)
                        {

                            Toast.makeText(AddDevice.this, "Failed to send sms !", Toast.LENGTH_LONG).show();
                        }


                    }
                    else {

                        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                    }
                }



            }
        });


    }




}