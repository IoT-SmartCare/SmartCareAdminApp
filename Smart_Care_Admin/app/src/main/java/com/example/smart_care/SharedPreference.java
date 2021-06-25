package com.example.smart_care;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor ;
    private static SharedPreference mysharedPreferance=null;

    private SharedPreference(Context context)
    {
        sharedPreferences = context.getSharedPreferences("shared",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }


    public static synchronized SharedPreference getPreferences(Context context)
    {

        if(mysharedPreferance==null) {
            mysharedPreferance = new SharedPreference(context);
        }

        return mysharedPreferance;
    }


    public void setData(String user)
    {
        editor.putString("login",user);
        editor.apply();
    }

    public String getData()
    {
        return sharedPreferences.getString("login","none");
    }



    public void setEmployeeKey(String device_id)
    {
        editor.putString("device_id",device_id);
        editor.apply();
    }

    public String getEmployeeKey()
    {
        return sharedPreferences.getString("device_id","none");
    }


    public void setloginType(String type)
    {
        editor.putString("loginType",type);
        editor.apply();
    }

    public String getloginType()
    {
        return sharedPreferences.getString("loginType","none");
    }


    public void setSuggestionTime(String suggestion_time)
    {
        editor.putString("suggestion_time",suggestion_time);
        editor.apply();
    }

    public String getSuggestionTime()
    {
        return sharedPreferences.getString("suggestion_time","none");
    }


}
