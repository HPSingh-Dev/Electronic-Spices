package com.conductor.apni.littleleappwa.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MySMSBroadcastReceiver extends BroadcastReceiver {

    private OTPReceiveListener otpReceiveListener;
    private final Pattern p = Pattern.compile("(|^)\\d{4}");

    public MySMSBroadcastReceiver() {
    }

    public void init(OTPReceiveListener otpReceiveListener) {
        this.otpReceiveListener = otpReceiveListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("checkLogReciever", "inside onRecieve ");
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
                if (status != null)
                    switch (status.getStatusCode()) {
                        case CommonStatusCodes.SUCCESS:
                            Intent messageIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                            if (this.otpReceiveListener != null) {
                                Log.d("checkLogReciever", "inside otpReceiveListener  ");
                                this.otpReceiveListener.onOTPReceived(messageIntent);
                            }
                            // Get SMS message contents
//                            String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
//                            Log.d("checkLogReciever","inside onRecieve message "+message);
//                            if (message != null) {
//                                Pattern pattern = Pattern.compile("(\\d{4})");
//                                //   \d is for a digit
//                                //   {} is the number of digits here 4.
//                                Matcher matcher = pattern.matcher(message);
//                                String val = "";
//                                if (matcher.find()) {
//                                    val = matcher.group(0);  // 4 digit number
//                                    Log.d("checkLogReciever","if match inside otpReceiveListener  "+val);
//                                    if (this.otpReceiveListener != null) {
//                                        Log.d("checkLogReciever","inside otpReceiveListener  ");
//                                        this.otpReceiveListener.onOTPReceived(val);
//                                    }
//                                } else {
//                                    Log.d("checkLogReciever","else match inside otpReceiveListener  "+val);
//                                    if (this.otpReceiveListener != null)
//                                        this.otpReceiveListener.onOTPReceived(null);
//                                }
//                            }else{
//                               // if (this.otpReceiveListener != null)
//                                    //this.otpReceiveListener.onOTPReceived("1111");
//                            }
                            break;
                        case CommonStatusCodes.TIMEOUT:
                            if (this.otpReceiveListener != null)
                                this.otpReceiveListener.onOTPTimeOut();
                            break;
                    }
            }
        }
    }

    public interface OTPReceiveListener {
        void onOTPReceived(Intent intent);

        void onOTPTimeOut();
    }
}
