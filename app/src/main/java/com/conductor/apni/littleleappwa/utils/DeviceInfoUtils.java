package com.conductor.apni.littleleappwa.utils;

import android.os.Build;


import com.conductor.apni.littleleappwa.services.Cons;

import java.util.HashMap;
import java.util.Map;

public class DeviceInfoUtils {

    private static final String DEVICE = "Device: ";
    private static final String SDK_VERSION = "SDK version: ";
    private static final String MODEL = "Model: ";
    private static final String APP_VERSION = "SkyApp build version: ";
    private static final String NEW_LINE = "\n";
    private static final String DIVIDER_STRING = "----------";

    public static StringBuilder getDeviseInfoForFeedback() {
        StringBuilder infoStringBuilder = new StringBuilder();
        infoStringBuilder.append(DEVICE).append(Build.DEVICE);
        infoStringBuilder.append(NEW_LINE);
        infoStringBuilder.append(SDK_VERSION).append(Build.VERSION.SDK_INT);
        infoStringBuilder.append(NEW_LINE);
        infoStringBuilder.append(MODEL).append(Build.MODEL);
        infoStringBuilder.append(NEW_LINE);
        infoStringBuilder.append(APP_VERSION).append(StringObfuscator.getAppVersionName());
        infoStringBuilder.append(NEW_LINE);
        infoStringBuilder.append(DIVIDER_STRING);
        infoStringBuilder.append(NEW_LINE);
        return infoStringBuilder;
    }

    public Map<String, String> getHeaderInfo(String token){
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+token);
//        headers.put("source", Cons.SOURCE);
//        headers.put("token",token);
//        headers.put("Content-Type",getContentType());
//        headers.put("Accept",getContentType());
        return headers;
    }

    public String getContentType(){
        String contentType="application/json";
        return contentType;
    }
}