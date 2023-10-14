package com.conductor.apni.littleleappwa.utils;

import com.conductor.apni.littleleappwa.BuildConfig;

public class StringObfuscator {


    public static String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public static boolean getDebugEnabled() {
        return BuildConfig.DEBUG;
    }
}