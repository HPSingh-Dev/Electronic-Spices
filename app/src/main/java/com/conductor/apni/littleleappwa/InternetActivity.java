package com.conductor.apni.littleleappwa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.conductor.apni.littleleappwa.utils.ConnectivityUtils;
import com.conductor.apni.littleleappwa.utils.Constants;
import com.droidnet.DroidListener;

public class InternetActivity extends AppCompatActivity implements DroidListener {


    private MyReceiver myReceiver;
    /**
     * Receiver for broadcasts sent by {@link}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("checkInternet","inside on recieve ");
            InternetActivity.this.finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("checkLogInternet","inside onresume ");
        //IronSource.onResume(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(Constants.PUSH_NOTIFICATION));
        if(ConnectivityUtils.isNetworkAvailable(this)){
            netIsOn();
            InternetActivity.this.finish();
        }
    }

//    private void netIsOn() {
//        Log.d("checkLog", "inside net netIsOn");
//        Intent intentOne = new Intent(Constants.PUSH_NOTIFICATION);
//        LocalBroadcastManager.getInstance(InternetActivity.this).sendBroadcast(intentOne);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        myReceiver = new MyReceiver();
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (isConnected) {
            //do Stuff with internet
            netIsOn();
        } else {
            //no internet
            netIsOff();
        }
    }

    @Override
    public void onBackPressed() {

    }

    //  Fragment currentFragment = null;

    //  List<BookTicketPojo> bookTicketModelList;
    // private int counter = 0;

    private void netIsOn() {
        Log.d("checkLog", "inside net netIsOn Internet Activity");
        setResult(RESULT_OK);
        InternetActivity.this.finish();
    }

    private void netIsOff() {
        Log.d("checkLog", "inside net netIsOff");
    }


}