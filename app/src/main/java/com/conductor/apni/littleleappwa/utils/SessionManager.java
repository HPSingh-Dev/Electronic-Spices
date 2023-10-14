package com.conductor.apni.littleleappwa.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


/**
 * Created by Saipro on 07-02-2017.
 */

public class SessionManager {
    // Shared Preferences
    public SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "LittleLeap";


    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_UPDATE = "IsUpdate";
    private static final String IS_PROFILE_UPDATED = "is_profile_updated";
    private static final String IS_FIRST_LOGIN = "IsFirstLoggedIn";
    private static final String IS_UPDATE_CONTACT = "IsContactUpdate";

    private static final String IS_INSTALLED = "IsInstalled";
    public static final String KEY_FCMID = "fcmid";
    // User name (make variable public to access from outside)
    public static final String _ID = "idone";
    public static final String KEY_PHONE = "userLogin";
    public static final String KEY_BLOB_ID = "blobId";
    public static final String KEY_PROMO_CODE = "promoCode";
    public static final String KEY_REFER_ID = "referId";
    public static final String KEY_GAME_ID = "gameId";
    public static final String KEY_GAME_ID_ONE = "gameIdone";
    public static final String KEY_FIRST_NAME = "userName";
    public static final String KEY_EMAIL = "userEmail";
    public static final String KEY_SUBSCRIBED = "keySubscribed";

    // Email address (make variable public to access from outside)
    public static final String KEY_ID = "userId";
    public static final String KEY_STUDENT_ID = "studentId";
    public static final String KEY_TIMER_GAME_TIME = "timergametime";

    private static SessionManager sessionManager;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static SessionManager getSessionmanager(Context context) {

        if (sessionManager == null) {
            sessionManager = new SessionManager(context);
        }
        return sessionManager;
    }

    /**
     * Create login session
     */
    public void createLoginSession(boolean isFirstTime) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putBoolean(IS_FIRST_LOGIN, isFirstTime);
//        editor.putBoolean(IS_UPDATE_CONTACT, isUpdateContact);
//        editor.putString(KEY_ID, String.valueOf(user.getUserid()));
//        editor.putString(KEY_FIRST_NAME, String.valueOf(user.getName()));
//        editor.putString(KEY_PHONE, String.valueOf(Phonenumber));
        editor.commit();
    }

    public String getUncaughtError() {
        return pref.getString("error", "");
    }

    public void setUncaughtError(String error) {
        editor.putString("error", error);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(KEY_ID, "-1");
    }

    public void setUserId(String id) {
        editor.putString(KEY_ID, id);
        editor.commit();
    }

    public String getStudentId() {
        return pref.getString(KEY_STUDENT_ID, "-1");
    }

    public void setStudentId(String id) {
        editor.putString(KEY_STUDENT_ID, id);
        editor.commit();
    }

    public long getTimerGameTime() {
        return pref.getLong(KEY_TIMER_GAME_TIME, 0);
    }

    public void setTimerGameTime(long timeinmillis) {
        editor.putLong(KEY_TIMER_GAME_TIME, timeinmillis);
        editor.commit();
    }

    public void setLogin(boolean login) {
        editor.putBoolean(IS_LOGIN, login);
        editor.commit();
    }

    public void setIsOrderUpdate(boolean login) {
        editor.putBoolean(IS_UPDATE, login);
        editor.commit();
    }

    public void setIsProfileUpdated(boolean isProfileUpdated) {
        editor.putBoolean(IS_PROFILE_UPDATED, isProfileUpdated);
        editor.commit();
    }

    public void setFcmId(String FCMID) {
        editor.putString(KEY_FCMID, FCMID);
        editor.commit();
    }

    public String getFcmId() {
        return pref.getString(KEY_FCMID, "NA");
    }

    public void setReferId(String referId) {
        editor.putString(KEY_REFER_ID, referId);
        editor.commit();
    }

    public String getReferId() {
        return pref.getString(KEY_REFER_ID, "");
    }

    public void setGameId(String referId) {
        editor.putString(KEY_GAME_ID, referId);
        editor.commit();
    }

    public String getGameId() {
        return pref.getString(KEY_GAME_ID, "");
    }

    public void setGameIdOne(String referId) {
        editor.putString(KEY_GAME_ID_ONE, referId);
        editor.commit();
    }

    public String getGameIdOne() {
        return pref.getString(KEY_GAME_ID_ONE, "");
    }


    public boolean getIsOrderUpdated(){
        return pref.getBoolean(IS_UPDATE,false);
    }

    public boolean getIsProfileUpdated(){
        return pref.getBoolean(IS_PROFILE_UPDATED,false);
    }

    public void setIsUpdateContact(boolean isUpdateContact) {
        editor.putBoolean(IS_UPDATE_CONTACT, isUpdateContact);
        editor.commit();
    }


    public static final String KEY_GENDER = "userGender";
    public static final String KEY_ACTIVTIY_ID = "activity_id";
    public static final String KEY_SUB_ACTIVTIY_ID = "sub_activity_id";

    public void setGender(String email) {
        editor.putString(KEY_GENDER, email);
        editor.commit();
    }

    public String getGender() {
        return pref.getString(KEY_GENDER, "Kiki");
    }


    public void setActivityId(String activityId) {
        editor.putString(KEY_ACTIVTIY_ID, activityId);
        editor.commit();
    }

    public String getActivityId() {
        return pref.getString(KEY_ACTIVTIY_ID, null);
    }

    public void setSubActivityId(String activityId) {
        editor.putString(KEY_SUB_ACTIVTIY_ID, activityId);
        editor.commit();
    }

    public void setFirstName(String fName) {
        editor.putString(KEY_FIRST_NAME, fName);
        editor.commit();
    }

    public String getFirstName() {
        HashMap<String, String> hashMap = getUserDetails();
        for (int i = 0; i < hashMap.size(); i++) {
            return hashMap.get(KEY_FIRST_NAME);
        }
        return null;
    }

    public String getSubActivityId() {
        return pref.getString(KEY_SUB_ACTIVTIY_ID, null);
    }

    public void setBlobId(String blobId) {
        editor.putString(KEY_BLOB_ID, blobId);
        editor.commit();
    }

    public String getBlobId() {
        HashMap<String, String> hashMap = getUserDetails();
        for (int i = 0; i < hashMap.size(); i++) {
            return hashMap.get(KEY_BLOB_ID);
        }
        return null;
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));
        user.put(KEY_FIRST_NAME, pref.getString(KEY_FIRST_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_BLOB_ID, pref.getString(KEY_BLOB_ID, null));
        user.put(KEY_PROMO_CODE, pref.getString(KEY_PROMO_CODE, null));

        // user email idCha
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        // return user
        return user;
    }

}
