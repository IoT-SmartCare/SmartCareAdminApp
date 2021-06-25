package com.example.smart_care.Adapter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smart_care.Admin;
import com.example.smart_care.MyService;
import com.example.smart_care.PatientDetails;
import com.example.smart_care.R;
import com.example.smart_care.model.Model_data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifAnimationMetaData;
import pl.droidsonroids.gif.GifImageView;

import static com.example.smart_care.R.*;
import static com.example.smart_care.myBackgroundProcess.NOTIFICATION_CHANNEL_ID;

public class Adapter_patientList extends FirebaseRecyclerAdapter<Model_data,Adapter_patientList.ViewHolder> {


    Context context;
    Vibrator v;
    int alert_flag=0;
    private NotificationManagerCompat notificationManager;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    public Adapter_patientList( FirebaseRecyclerOptions<Model_data> options, Context context) {
        super(options);
        /*this.options=options;*/
        this.context = context;
    }



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull Adapter_patientList.ViewHolder holder, int position, @NonNull final Model_data model) {

        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        holder.name.setText("  "+model.getPatient_name().toString());
        holder.bp.setText("  BPM: "+model.getBPM().toString());
        holder.temp.setText("Temp: 98.6 F");


        int bp= Integer.parseInt(model.getBPM());



        if(bp>100 ||bp<60)
        {
            alert_flag=1;
           // play();

            scheduleNotification(getNotification(model.getPatient_name()+"'s BPM is abnormal. BPM : "+bp),1000);
            holder.status.setBackgroundResource(drawable.ic_warning1);

            holder.bp.setTextColor(Color.parseColor("#FF2525"));
            Glide.with(context)
                    .load(drawable.wev)
                    .into(holder.gifImageView);
        }
       else {

           alert_flag=0;

            holder.status.setBackgroundResource(drawable.ic_ok);
           v.cancel();
         // stop();
            Glide.with(context)
                    .load(drawable.wev)
                    .into(holder.gifImageView);

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, PatientDetails.class);
                intent.putExtra("age", model.getPatient_age());
                intent.putExtra("name", model.getPatient_name());
                intent.putExtra("gender",model.getPatient_gender());
                intent.putExtra("weight", model.getPatient_weight());
                intent.putExtra("device_id", model.getDevice_id());
                intent.putExtra("nurse_id", model.getNurse_id());
                intent.putExtra("doc_id", model.getDoctor_id());
                context.startActivity(intent);


            }
        });

    }

    @NonNull
    @Override
    public Adapter_patientList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(layout.item_patient_data,parent,false);
        return new ViewHolder(view);
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,temp,bp;
        CardView constraintLayout;
        GifImageView gifImageView,gifImageView1;
        ImageView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(id.tt_item_name);
            temp=itemView.findViewById(id.tt_item_tem);
            bp=itemView.findViewById(id.tt_item_bp);
            constraintLayout=itemView.findViewById(id.item_background);
            gifImageView=itemView.findViewById(id.gifImageView);
            status=itemView.findViewById(id.status);

        }
    }



    public void vibration()
    {

// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           v.vibrate(VibrationEffect.createOneShot(50000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(50000);
        }
    }


    private void scheduleNotification (Notification notification , int delay) {
        Intent notificationIntent = new Intent( context, MyService. class ) ;
        notificationIntent.putExtra(MyService. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyService. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( context, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
    }

    private Notification getNotification (String content) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder( context, default_notification_channel_id ) ;
        builder.setContentTitle( "Abnormal Health Condition ! " ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.mipmap.ic_final_launcher ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }


}
