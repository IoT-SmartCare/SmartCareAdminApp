package com.example.smart_care;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.smart_care.Adapter.Adapter_patientList;
import com.example.smart_care.model.Model_data;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class myBackgroundProcess extends Service {

    int bpm;
    SharedPreference sharedPreference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference,suggestion_ref;
    ArrayList<Model_data> data;

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sharedPreference = SharedPreference.getPreferences(myBackgroundProcess.this);

        if (sharedPreference.getEmployeeKey().equals("admin123"))
        {


            FirebaseRecyclerOptions<Model_data> options =
                    new FirebaseRecyclerOptions.Builder<Model_data>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("patients"), Model_data.class)
                            .build();



        }
        else {

            FirebaseRecyclerOptions<Model_data> options1 =
                    new FirebaseRecyclerOptions.Builder<Model_data>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("patients").
                                    startAt(sharedPreference.getEmployeeKey()).orderByChild(sharedPreference.getloginType()).
                                    endAt(sharedPreference.getEmployeeKey()+"\uf8ff"), Model_data.class)
                            .build();

        }






        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }



    public static void wait(int s)
    {
        try
        {
            Thread.sleep(s);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }


    private void scheduleNotification (Notification notification , int delay) {
        Intent notificationIntent = new Intent( this, MyService. class ) ;
        notificationIntent.putExtra(MyService. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyService. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
    }

    private Notification getNotification (String content) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Abnormal Health Condition ! " ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.mipmap.ic_final_launcher ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }

    private Notification getNotification_sms (String content) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "You have a new suggestion" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.mipmap.ic_final_launcher ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }
}
