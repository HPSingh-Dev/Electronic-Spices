package com.conductor.apni.littleleappwa;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.droidnet.DroidNet;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class LittleLeapApplication extends Application {

    private static LittleLeapApplication mInstance;
    Context context;
    public SharedPreferences preferences;
    public String prefName;
    public static boolean isStage;
    public static int config_interval;

    public LittleLeapApplication() {
        mInstance = this;
    }

    public LittleLeapApplication(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        isStage = false;
        DroidNet.init(this);
        prefName = getResources().getString(R.string.app_name);
        preferences = getSharedPreferences(prefName, MODE_PRIVATE);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners();
    }

    /**
     * Enables https connections
     */
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       // MultiDex.install(this);
    }

    public static synchronized LittleLeapApplication getInstance() {
        return mInstance;
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean outcome = false;

        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo[] networkInfos = cm.getAllNetworkInfo();

            for (NetworkInfo tempNetworkInfo : networkInfos) {


                /**
                 * Can also check if the user is in roaming
                 */
                if (tempNetworkInfo.isConnected()) {
                    outcome = true;
                    break;
                }
            }
        }

        return outcome;
    }

//    public void getAppSettingsRequest() {
//        String xAccessToken = "mykey";
//        mainAPIInterface.getAppSettingsRequest(xAccessToken).enqueue(new Callback<GetAppSettings>() {
//            @Override
//            public void onResponse(Call<GetAppSettings> call, Response<GetAppSettings> response) {
//                if (response.isSuccessful()) {
//
//                    if (response.body().getSuccess().equalsIgnoreCase("1")) {
//
//
//                        TRACKING_MORE_API_KEY = response.body().getSettings().getTrackingMoreKey();
//                        GOOGLE_MAP_API_KEY = response.body().getSettings().getGoogleMapKey();
//                        APP_SUPPORT_EMAIL = response.body().getSettings().getSupportEmail();
//                        APP_SUPPORT_PHONE = response.body().getSettings().getSupportPhone();
//                        ABOUT_APP = response.body().getSettings().getAboutUs();
//
//                        CURRENCY_SYMBOL = response.body().getCurrency().getListSymbol();
//                        CURRENCY_CODE = response.body().getCurrency().getListCc();
//                        CURRENCY_NAME = response.body().getCurrency().getListName();
//                        CURRENCY_EXCHANGE_RATE = response.body().getCurrency().getExchangeRate();
//
//                        PAYPAL_CLIENT_ID = response.body().getSettings().getPaypalClientId();
//                        PAYPAL_SECRET = response.body().getSettings().getPaypalClientSecret();
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetAppSettings> call, Throwable t) {
//
//
//                Log.i("tag", t.getMessage().toString());
//            }
//        });
//
//    }
//
//    ProgressDialog dialog;
//    SessionManager sessionManager;
//
//    public void setUserLogout(Activity activity) {
//        dialog = new ProgressDialog(activity);
//        dialog.setMessage("Verifying your details.");
//        if(activity!=null && (!activity.isDestroyed() && !activity.isFinishing())) {
//            dialog.show();
//        }
//
//        sessionManager = new SessionManager(activity);
//
//        MultipartBody.Part userId_body = MultipartBody.Part.createFormData("user_id", sessionManager.getUserId());
//
//        MultipartBody.Part fcm_body = MultipartBody.Part.createFormData("fcm_id", sessionManager.getFcmId());
//
//
//        mainAPIInterface.logOut(userId_body, fcm_body).enqueue(new Callback<NormalResponseBody>() {
//            @Override
//            public void onResponse(Call<NormalResponseBody> call, Response<NormalResponseBody> response) {
//
//                Log.i("checkLog", "inside onResponse " + response.toString());
//
//                if (response.isSuccessful()) {
//                    if(activity!=null && (!activity.isDestroyed() && !activity.isFinishing())) {
//                        dialog.dismiss();
//                    }
//
//                    if (response.body().getStatus().equalsIgnoreCase("Success")) {
//
//                        Toast.makeText(getInstance(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                        sessionManager.clearSession();
//                        DataManager.init(getApplicationContext()).clearAllTables();
//                        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        ActivityCompat.finishAffinity(activity);
//                    } else {
//                        Toast.makeText(getInstance(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NormalResponseBody> call, Throwable t) {
//                if(activity!=null && (!activity.isDestroyed() && !activity.isFinishing())) {
//                    dialog.dismiss();
//                }
//                Log.i("checkLog", t.getMessage().toString());
//            }
//        });
//
//    }

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
