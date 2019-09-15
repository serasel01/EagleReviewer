package com.example.serasel.eagle.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

//Shared Preferences allows saving of student info locally
public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;


    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_COURSE = "user_course";
    private static final String KEY_QUIZ_CORRECTS = "quiz_corrects";
    private static final String KEY_QUIZ_WRONGS = "quiz_wrongs";
    private static final String KEY_SETTINGS_DOWNLOAD = "settings_download";

    private SharedPrefManager(Context context){
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //function for saving user upon login
    public boolean userLogin(String id, String name, String course){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //(tag, value)
        editor.putString(KEY_USER_ID, id); //saves user id
        editor.putString(KEY_USER_NAME, name); // name
        editor.putString(KEY_USER_COURSE, course); //course
        editor.apply();

        return true;
    }

    public void updateCorrects(int corrects){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_QUIZ_CORRECTS, corrects);
        editor.apply();
    }

    public void updateWrongs(int wrongs){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_QUIZ_WRONGS, wrongs);
        editor.apply();
    }

    public void updateDownloads(boolean isDownload){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(KEY_SETTINGS_DOWNLOAD, isDownload);
        editor.apply();
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
        return true;
    }

    public String getKeyUserId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public String getKeyUserName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    public String getKeyUserCourse(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_COURSE, null);
    }

    public int getKeyQuizCorrects(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(String.valueOf(KEY_QUIZ_CORRECTS), -1);
    }

    public int getKeyQuizWrongs(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(String.valueOf(KEY_QUIZ_WRONGS), -1);
    }
}
