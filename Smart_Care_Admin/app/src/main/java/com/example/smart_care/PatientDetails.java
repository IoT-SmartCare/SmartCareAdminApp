package com.example.smart_care;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.smart_care.databinding.ActivityPatientDetailsBinding;
import com.example.smart_care.model.ModelSuggestion;
import com.example.smart_care.model.Model_data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PatientDetails extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    String get_real_time_bp;
    DatabaseReference database;

    SharedPreference sharedPreference;
    private ActivityPatientDetailsBinding binding;
    DatabaseReference bpm_reference,databaseReference;
    public  static final int request_call=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPreference = SharedPreference.getPreferences(PatientDetails.this);

        firebaseDatabase=FirebaseDatabase.getInstance();
        database= FirebaseDatabase.getInstance().getReference("suggestion");


        binding.backPatientDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        binding.btSendSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat dateFormat = new SimpleDateFormat("h:mm a");
                Date date = new Date();
                ModelSuggestion model_data = new ModelSuggestion(getIntent().getStringExtra("device_id"),binding.etSuggestion.getText().toString(),dateFormat.format(date));
                database.child(getIntent().getStringExtra("device_id")).setValue(model_data);


            }
        });


        binding.tvPatientName.setText(getIntent().getStringExtra("name"));
        binding.tvPatientAge.setText("Age: "+getIntent().getStringExtra("age"));
        binding.tvPatientWeight.setText("Weight: "+getIntent().getStringExtra("weight"));
        binding.tvPatientGender.setText("Gender : "+getIntent().getStringExtra("gender"));


        //** data get in realtime from sensor
        firebaseDatabase=FirebaseDatabase.getInstance();

        //device id get from the previous activity
        String get_device_id=getIntent().getStringExtra("device_id");


        bpm_reference=firebaseDatabase.getReference("patients/"+get_device_id+"/bpm");

        bpm_reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                get_real_time_bp =dataSnapshot.getValue(String.class);
                binding.tvCurrentHeartRate.setText(" BPM : "+get_real_time_bp);

                int bpm = Integer.parseInt(""+get_real_time_bp);

                if (bpm>100||bpm<60)
                {
                    binding.status.setBackgroundResource(R.drawable.ic_warning1);
                    binding.tvCurrentHeartRate.setTextColor(Color.parseColor("#FF2525"));
                    binding.tvHealthStatus.setText("Health Status: Dangerous");
                    binding.tvHealthStatus.setTextColor(Color.parseColor("#FF2525"));
                }
                else {
                    binding.tvCurrentHeartRate.setTextColor(Color.parseColor("#2e2e2e"));
                    binding.status.setBackgroundResource(R.drawable.ic_ok);
                    binding.tvHealthStatus.setText("Health Status: Normal");
                    binding.tvHealthStatus.setTextColor(Color.parseColor("#29B30D"));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference=firebaseDatabase.getReference("patients/"+get_device_id+"/patient_phone");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                binding.patinetPhone.setText("  "+dataSnapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        databaseReference=firebaseDatabase.getReference("suggestion/"+get_device_id+"/suggestion");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String getText=""+dataSnapshot.getValue(String.class);

                if(!getText.equals("null"))
                {
                    binding.etSuggestion.setText("  "+dataSnapshot.getValue(String.class));
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        databaseReference=firebaseDatabase.getReference("suggestion/"+get_device_id+"/last_send");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String getTime=""+dataSnapshot.getValue(String.class);
                if (!getTime.equals("null"))
                {
                    binding.lastSeen.setText("  "+dataSnapshot.getValue(String.class));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference=firebaseDatabase.getReference("employee/"+getIntent().getStringExtra("doc_id")+"/name");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                binding.tvDoctorName.setText("  "+dataSnapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {




            }
        });


        databaseReference=firebaseDatabase.getReference("employee/"+getIntent().getStringExtra("doc_id")+"/cell");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                binding.tvDoctorPhone.setText("  "+dataSnapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {




            }
        });



        databaseReference=firebaseDatabase.getReference("employee/"+getIntent().getStringExtra("doc_id")+"/email");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                binding.docMail.setText("  "+dataSnapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });




        databaseReference=firebaseDatabase.getReference("employee/"+getIntent().getStringExtra("nurse_id")+"/email");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                binding.nurseMail.setText("  "+dataSnapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

        databaseReference=firebaseDatabase.getReference("employee/"+getIntent().getStringExtra("nurse_id")+"/cell");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                binding.tvNursePhone.setText("  "+dataSnapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

        databaseReference=firebaseDatabase.getReference("employee/"+getIntent().getStringExtra("nurse_id")+"/name");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                binding.tvNurseName.setText("  "+dataSnapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


        binding.patinetPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                call(binding.patinetPhone.getText().toString());

            }
        });

        binding.tvDoctorPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                call(binding.tvDoctorPhone.getText().toString());


            }
        });

        binding.docMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mail(binding.docMail.getText().toString());

            }
        });

        binding.tvNursePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                call(binding.tvNursePhone.getText().toString());


            }
        });

        binding.nurseMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mail(binding.nurseMail.getText().toString());

            }
        });

    }


    void call(String number) {
        // number = binding.patinetPhone.getText().toString();

        if (ContextCompat.checkSelfPermission(PatientDetails.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) PatientDetails.this, new String[]{
                    Manifest.permission.CALL_PHONE}, request_call);

        } else {

            String dail = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dail)));
        }
    }



    void mail(String myemail)
    {

      //  String get_subject = sub;


        //String myemail =binding.docMail.getText().toString();
        String recipientlist = myemail.toString();
        String[] recipients = recipientlist.split(",");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
       // intent.putExtra(Intent.EXTRA_SUBJECT, get_subject);
       // intent.putExtra(Intent.EXTRA_TEXT, get_message);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "* Select Gmail *"));

    }


}