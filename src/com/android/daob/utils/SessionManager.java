package com.android.daob.utils;

import java.util.HashMap;

import com.android.daob.activity.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    SharedPreferences pref;
    Editor editor;
    Context mContext;
    
    //Shared pref mode
    int PRIVATE_MODE = 0;
    
    //Shared pref file name
    private static final String PREF_NAME = "DAOB";
    
    //All Shared prefs keys
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_ROLE = "role";
    public static final String KEY_USERID = "userId";
    public static final String KEY_ROLEID = "roleId";
    
    public SessionManager(Context mContext) {
        this.mContext = mContext;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.commit();
    }
    
    public void createLoginSession(String name, String role, String userId, String roleId) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_USERID, userId);
        editor.putString(KEY_ROLEID, roleId);
        editor.commit();
        Log.i("editor", "" + editor);
    }
    
    public void checkLogin() {
        if(!this.isLoggedIn()){
            Intent i = new Intent(mContext, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        }
    }
    
    //Get stored session data
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_ROLE, pref.getString(KEY_ROLE, null));
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
        user.put(KEY_ROLEID, pref.getString(KEY_ROLEID, null));
        Log.i("user", "" + user);
        return user;
        
    }
    
    
    public void logoutUser() {
        //clearing all data from Shared Pref
        editor.clear();
        editor.commit();
        
        Intent i = new Intent(mContext, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
        
    }
    
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
