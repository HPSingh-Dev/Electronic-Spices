package com.conductor.apni.littleleappwa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.conductor.apni.littleleappwa.broadcast.MySMSBroadcastReceiver;
import com.conductor.apni.littleleappwa.roundedimageview.RoundedImageView;
import com.conductor.apni.littleleappwa.services.Cons;
import com.conductor.apni.littleleappwa.services.VolleyManager;
import com.conductor.apni.littleleappwa.utils.AppRater;
import com.conductor.apni.littleleappwa.utils.ConnectivityUtils;
import com.conductor.apni.littleleappwa.utils.Constants;
import com.conductor.apni.littleleappwa.utils.SessionManager;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
//import com.suddenh4x.ratingdialog.AppRating;

import net.gotev.speech.Speech;
import net.gotev.speech.TextToSpeechCallback;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
//import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;
//import in.myinnos.inappupdate.InAppUpdate;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class MainActivity extends AppCompatActivity implements DroidListener,InAppUpdateManager.InAppUpdateHandler{
   // InAppUpdateManager.InAppUpdateHandler

    //    private static final String TAG = "MainActivity";
    private WebView webView;
    private static final String TAG = "MyActivity";
    private static final int REQ_USER_CONSENT = 200;
    boolean doubleBackToExitPressedOnce = false;
    public static long endTimeMillis=0;
    //    private Bundle savedInstanceState;
    private String _FcmToken = "";
    private SessionManager sessionManager;
    private RoundedImageView girlImage;
    private WebInterface webInterface;
    private ConstraintLayout container;
    public RelativeLayout connectivityRelative;
    private VolleyManager volleyManager;
    ProgressDialog mProgressDialog;
    private DroidNet mDroidNet;
    private KonfettiView viewKonfetti;
    private MediaPlayer mPlayer, mPlayerOne;
    protected AudioManager mAudioManager;
    private LottieAnimationView animationView;
    private ImageView startImage;
    private boolean isLoaded;
    private String gameRedirectUrl;
    public static String scoreUrl;
    private String url;
    private String daily, inviteid;
    private TextToSpeech t1;
    public static String gameId="";
    private AppUpdateManager appUpdateManager;
    public static int height, width;
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private static final String TAGONE = "Sample";
    private InAppUpdateManager inAppUpdateManager;
    private AppRater appRater;

    @Override
    protected void onStart() {
        super.onStart();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)) {
//            askPermission();
//        }else{
//            startService(new Intent(MainActivity.this, FloatingViewService.class));
//        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.setCancelable(true);
        volleyManager = new VolleyManager();
        sessionManager = SessionManager.getSessionmanager(this);
        webInterface = new WebInterface(MainActivity.this);
        Speech.init(MainActivity.this, getPackageName());

        mySMSBroadcastReceiver = new MySMSBroadcastReceiver();

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        appRater=new AppRater(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        //String url = "https://calendly.com/komal-18/expert-consultation";
        // String url = "http://192.168.29.156/newlittle/app/parent/home";

        //   String url = "http://3.110.92.151/applatest/parent/home#";

        //String url = "http://stageapp.littleleap.co.in/";

        daily = getIntent().getStringExtra("daily");
        Log.d("checkExtra", "extras are " + daily);

        if (daily != null && daily.equalsIgnoreCase("true")) {
            url = "https://digihelpapp.com/app/";
        } else {
            //url = "https://app.littleleap.co.in/";
            url = "https://digihelpapp.com/app/";
        }

        inviteid = getIntent().getStringExtra("invitedby");
        Log.d("checkExtra", "extras are " + inviteid);

     //   Toast.makeText(this, " iviteId id "+inviteid, Toast.LENGTH_SHORT).show();

        isLoaded = true;

        mPlayer = MediaPlayer.create(MainActivity.this, R.raw.tin_surprise);
        mPlayerOne = MediaPlayer.create(MainActivity.this, R.raw.small_applause);

        FirebaseApp.initializeApp(this);

        mDroidNet = DroidNet.getInstance();


//        appUpdateManager = AppUpdateManagerFactory.create(this);
//        InAppUpdate.setImmediateUpdate(appUpdateManager, this);

        startSMSRetrieverClient();

//        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status != TextToSpeech.ERROR) {
//                    //t1.setLanguage(Locale.UK);
//                    Locale locale = new Locale("hi_IN");
//                    //t1.setLanguage(new Locale("hin", "IND"));
//                    t1.setLanguage(locale);
//                }
//            }
//        }, "com.google.android.tts");

//        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            //code for TTS
//            t1.setLanguage(new Locale("hin"));
//        }, "com.google.android.tts");//specifying which engine to use; if this is not available, it uses default
        firebaseNotification_OnCreate();

        webView_OnCreate();


//        girlImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // throw new RuntimeException("Test Crash");
//            }
//        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    String token = task.getResult();
                    Log.d(TAG, token);
                    _FcmToken = token;
                    sessionManager.setFcmId(_FcmToken);
//                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                    ClipData clip = ClipData.newPlainText("fcmid", _FcmToken);
//                    clipboard.setPrimaryClip(clip);
                    // Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                });

        if (sessionManager.getBlobId() != null && sessionManager.getBlobId().length() > 0) {
            setAvatarImageOne();
        } else {
//            girlImage.setVisibility(View.VISIBLE);
//            setTouchListener();
            //girlImage.setOnTouchListener(touchListener);
        }

//        Animation animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
//        girlImage.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                girlImage.startAnimation(animationFadeOut);
//            }
//        });

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

       // createManualDynamicLinkGame("ankur","10");

       // createShortDynamicLink("jhsdjhdjhjhgdf");

        //createlink();

       // ttsSetting();

        inAppUpdateManager = InAppUpdateManager.Builder(this, REQ_CODE_VERSION_UPDATE)
                .resumeUpdates(true) // Resume the update, if the update was stalled. Default is true
                .mode(eu.dkaratzas.android.inapp.update.Constants.UpdateMode.FLEXIBLE)
                // default is false. If is set to true you,
                // have to manage the user confirmation when
                // you detect the InstallStatus.DOWNLOADED status,
                .snackBarMessage("An update has just been downloaded.")
                .snackBarAction("RESTART")
                .useCustomNotification(true)
                .handler(this);

        inAppUpdateManager.checkForAppUpdate();

        appRater.showGoogleInAppAppRating();

    }

    // InAppUpdateHandler implementation

    @Override
    public void onInAppUpdateError(int code, Throwable error) {

    }

    @Override
    public void onInAppUpdateStatus(InAppUpdateStatus status) {
        /*
         * If the update downloaded, ask user confirmation and complete the update
         */

        if (status.isDownloaded()) {

            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);

            Snackbar snackbar = Snackbar.make(rootView,
                    "An update has just been downloaded.",
                    Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("RESTART", view -> {

                // Triggers the completion of the update of the app for the flexible flow.
                inAppUpdateManager.completeUpdate();

            });

            snackbar.show();

        }
    }

    private void ttsSetting(){
//        ComponentName componentToLaunch = new ComponentName(
//                "com.android.settings",
//                "com.android.settings.TextToSpeechSettings");
//        Intent intent = new Intent();
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setComponent(componentToLaunch);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);

        Intent intent = new Intent();
        intent.setAction("com.android.settings.TTS_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void speak(String toSpeak) {
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        mDroidNet.removeInternetConnectivityChangeListener(this);
    }

    private boolean isAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private final int RecordAudioRequestCode = 101;

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }


    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackbarForCompleteUpdate();
                        Log.d("checkLog", "inside installStateUpdatedListener downloaded");
                    } else if (state.installStatus() == InstallStatus.INSTALLED) {
                        if (mAppUpdateManager != null) {
                            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                        }
                        Log.d("checkLog", "inside installStateUpdatedListener installed");
                    } else {
                        Log.i(TAG, "InstallStateUpdatedListener: state: " + state.installStatus());
                    }
                }
            };

    @Override
    protected void onResume() {
        super.onResume();
      //  checkUpdate();
        //InAppUpdate.setImmediateUpdateOnResume(appUpdateManager, this);
        offBeatSound();
        mDroidNet.addInternetConnectivityListener(this);
        if (ConnectivityUtils.isNetworkAvailable(this)) {
            //netIsOn();
        }else{
            netIsOff();
        }
        if (!isAudioPermission()) {
            checkPermission();
        }
        // speak("Hello Welcome..");
    }


    private void offBeatSound() {
        Log.d("checkLogSound", "inside offbeatSound..");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, AudioManager.FLAG_VIBRATE);
            } else {
                mAudioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            }
        } catch (SecurityException e) {

        }

    }


    public ValueCallback<Uri[]> mFilePathCallback;
    public String mCameraPhotoPath;
    public static final Integer ActivityRequestCode = 2;
    public static final int INPUT_FILE_REQUEST_CODE = 1;
    public static final int FILECHOOSER_RESULTCODE = 1;
    public ValueCallback<Uri> mUploadMessage;
    public Uri mCapturedImageURI = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
//            case InAppUpdate.REQUEST_APP_UPDATE:
//                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
//                break;
            case RecordAudioRequestCode:
                if (isAudioPermission()) {
                    // Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    checkPermission();
                }
                break;
            case REQ_USER_CONSENT:
                if (resultCode == RESULT_OK && (data != null)) {
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Log.d("checkLogActivity", "inside onRecieve message " + message);
                    if (message != null) {
                        Pattern pattern = Pattern.compile("(|^)\\d{4}");
                        //Pattern pattern = Pattern.compile("(\\d{4})");
                        //   \d is for a digit
                        //   {} is the number of digits here 4.
                        Matcher matcher = pattern.matcher(message);
                        String val = "";
                        if (matcher.find()) {
                            val = matcher.group(0);  // 4 digit number
                            Log.d("checkLogActivity", "if match inside MainActivity  " + val);
                            callOTP(val);
                        } else {

                        }
                    } else {
                        // if (this.otpReceiveListener != null)
                        //this.otpReceiveListener.onOTPReceived("1111");
                    }

                }
                break;
            case 111:
                if (resultCode == RESULT_OK) {
                    webView.reload();
                }
                break;
            case 199:
                Log.d("checkLog", "inside onactivityresult 199 ");
                if (resultCode == RESULT_OK) {
                    callControl(sessionManager.getStudentId(), sessionManager.getActivityId());
                }
                break;
            case RC_APP_UPDATE:
                Toast.makeText(this, "App updated.", Toast.LENGTH_SHORT).show();
                break;
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                        super.onActivityResult(requestCode, resultCode, data);
                        return;
                    }
                    Uri[] results = null;
                    // Check that the response is a good one
                    if (resultCode == Activity.RESULT_OK) {
                        if (data == null) {
                            // If there is not data, then we may have taken a photo
                            if (mCameraPhotoPath != null) {
                                results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                            }
                        } else {
                            String dataString = data.getDataString();
                            if (dataString != null) {
                                results = new Uri[]{Uri.parse(dataString)};
                            }
                        }
                    }
                    mFilePathCallback.onReceiveValue(results);
                    mFilePathCallback = null;
                } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                        super.onActivityResult(requestCode, resultCode, data);
                        return;
                    }
                    if (requestCode == FILECHOOSER_RESULTCODE) {
                        if (null == this.mUploadMessage) {
                            return;
                        }
                        Uri result = null;
                        try {
                            if (resultCode != RESULT_OK) {
                                result = null;
                            } else {
                                // retrieve from the private variable if the intent is null
                                result = data == null ? mCapturedImageURI : data.getData();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "activity :" + e,
                                    Toast.LENGTH_LONG).show();
                        }
                        mUploadMessage.onReceiveValue(result);
                        mUploadMessage = null;
                    }
                }
        }
    }

    private void callControl(String student_id, String activity_id) {
        Log.d("checkLogin", "url student_id  " + student_id + " activity_id " + activity_id);
        String call = "javascript:activitiesNativeCallHandler(" + "'" + student_id + "','" + activity_id + "'" + ")";
        Log.d("checkLogin", "url string is " + call);
        webView.loadUrl(call);
    }

    public static final int RC_APP_UPDATE = 333;
    private AppUpdateManager mAppUpdateManager;

    private void checkUpdate() {
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        Log.d("checkLog", "inside checkUpdate");
        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            Log.d("checkLog", "inside update successlistener");

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                Log.d("checkLog", "inside checkUpdate available");
                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE, MainActivity.this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    Log.d("checkLog", "inside checkUpdate error");
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
                Log.d("checkLog", "inside checkUpdate complete");
            } else {
                Log.e(TAG, "checkForAppUpdateAvailability: something else");
            }
        });
    }


    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.parent),
                        "New app is ready!",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null) {
                mAppUpdateManager.completeUpdate();
            }
        });

        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(MainActivity.this, FloatingViewService.class));
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sessionManager.setGameId("");
        //sessionManager.setGameIdOne("");
        sessionManager.setReferId("");
        stopService(new Intent(MainActivity.this, FloatingViewService.class));
        if (mySMSBroadcastReceiver != null) {
            unregisterReceiver(mySMSBroadcastReceiver);
        }
        System.gc();
    }

    private boolean isOtpDone;
    private void registerListener() {
        isOtpDone=false;
        registerReceiver(mySMSBroadcastReceiver, new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION));
        mySMSBroadcastReceiver.init(new MySMSBroadcastReceiver.OTPReceiveListener() {
            @Override
            public void onOTPReceived(Intent intent) {
                // OTP Received
                Log.d("checkLogSms", " onOTPReceived otp ");
               // Toast.makeText(MainActivity.this, "onOTPReceived otp", Toast.LENGTH_SHORT).show();
                //  callOTP(otp);
                someActivityResultLauncher.launch(intent);
            }

            @Override
            public void onOTPTimeOut() {
                Log.d("checkLogSms", " onOTPTimeOut  is ");
            }
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Here, no request code
                    Intent data = result.getData();
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Log.d("checkLogActivity", "inside onRecieve message " + message);
                    if (message != null) {
                        Pattern pattern = Pattern.compile("(|^)\\d{4}");
                        //Pattern pattern = Pattern.compile("(\\d{4})");
                        //   \d is for a digit
                        //   {} is the number of digits here 4.
                        Matcher matcher = pattern.matcher(message);
                        String val = "";
                        if (matcher.find()) {
                            val = matcher.group(0);  // 4 digit number
                            Log.d("checkLogActivity", "if match inside MainActivity  " + val);
                            callOTP(val);
                        } else {

                        }
                    } else {
                        // if (this.otpReceiveListener != null)
                        //this.otpReceiveListener.onOTPReceived("1111");
                    }


                }
            });


    MySMSBroadcastReceiver mySMSBroadcastReceiver;

    private void startSMSRetrieverClient() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null herec
        Log.d("checkLogin", "inside startSmsUserConsnet");
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //   Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setWebChromeClient(WebView mWebView) {
        // new WebChromeClient(this).setWebChromeClient(mWebView);
        MyWebChromeClient client = new MyWebChromeClient(MainActivity.this);
        mWebView.setWebChromeClient(client);
    }


    private void webView_OnCreate() {
        connectivityRelative = (RelativeLayout) findViewById(R.id.connectivityRelative);
        container = findViewById(R.id.parent);
        webView = findViewById(R.id.webView);
        girlImage = findViewById(R.id.girlImage);
        startImage = findViewById(R.id.startImage);
        animationView = findViewById(R.id.animationView);
        viewKonfetti = findViewById(R.id.viewKonfetti);
        webView.setWebViewClient(new MyWebClient());
        setWebChromeClient(webView);
        webView.addJavascriptInterface(webInterface, "Android");
        setWebviewSetting();

        //when clicked on notification
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("URL")) {
                url = getIntent().getExtras().getString("URL");
//                Toast.makeText(MainActivity.this, "URL: " + url , Toast.LENGTH_LONG).show();
            }
            if (getIntent().getExtras().containsKey("link")) {
                url = getIntent().getExtras().getString("link");
//                Toast.makeText(MainActivity.this, "LINK: " + url , Toast.LENGTH_LONG).show();
            }
        }
        Map<String, String> headerMap = getHeader();
        webView.loadUrl(url,headerMap);

        CookieManager.getInstance().setAcceptCookie(true);
    }

//   // private String MyUA = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36";
//    public void setWebviewSetting() {
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//      //  webSettings.setUserAgentString(MyUA);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setDatabaseEnabled(true);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            webSettings.setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");
//        }
//        Log.d("checkLogAgent","user agent is "+webSettings.getUserAgentString());
//        webSettings.setDefaultFontSize(20);
//        webSettings.setSupportMultipleWindows(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.supportZoom();
//        webSettings.setAppCacheEnabled(true);
//        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        webSettings.setLoadsImagesAutomatically(true);
//        webSettings.setGeolocationEnabled(true);
//        webSettings.setNeedInitialFocus(false);
//        webSettings.setAllowFileAccess(true);
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        cookieManager.acceptCookie();
//    }


    private String MyUA = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36";
    public void setWebviewSetting() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString(MyUA);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webSettings.setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");
        }
        webSettings.setJavaScriptEnabled(true);
        Log.d("checkLogAgent","user agent is "+webSettings.getUserAgentString());
        webSettings.setDefaultFontSize(20);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.supportZoom();
//        webSettings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setNeedInitialFocus(false);
        webSettings.setAllowFileAccess(true);
    }

    private void callOnBack() {
        String call = "javascript:quitGame("  + ")";
        Log.d("checkLogin", "callOTP url string is " + call);
        webView.loadUrl(call);
    }

    private void callOnBackTwo(String hostCode,int type,String activityId,int index) {
        String call = "javascript:backFromScoreboard(" + "'" + hostCode + "'," + "'" + type + "',"+ "'" + activityId + "',"+ "'" + index + "'" + ")";
        Log.d("checkLogin", "callOTP url string is " + call);
        webView.loadUrl(call);
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            String currentUrl = webView.getUrl();
            Log.d("checkLogin", "onBackPressed if url is  "+currentUrl);
            if(currentUrl.contains("timer-game-room") || currentUrl.contains("timer-game-leaderboard") || currentUrl.contains("timergame-daily-challenges")||currentUrl.contains("timer-game-participants")||currentUrl.contains("timer-game-questions")){
                Log.d("checkLogin", "onBackPressed if url is  "+currentUrl);
                callOnBack();
            }else if(currentUrl.contains("timer-game-scoreboard")){
                Log.d("checkLogin", "onBackPressed timer-game-scoreboard is  "+currentUrl);
                currentUrl=currentUrl.replace("?","/");
                Log.d("checkLogin", "onBackPressed timer-game-scoreboard is  "+currentUrl);
                String [] splitArray=currentUrl.split("/");
                String hostCode=splitArray[splitArray.length-2];
                Log.d("checkLogin", "onBackPressed hostCode is  "+hostCode);
                callOnBackTwo(hostCode,type,activityId,index);
            }else if (!currentUrl.equalsIgnoreCase(url)) {
                webView.goBack();
            }else if(currentUrl.equalsIgnoreCase("https://digihelpapp.com/app/home/login")){
                MainActivity.this.finish();
            }else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 3000);
            }
        } else {
            String currentUrl = webView.getUrl();
            if (!currentUrl.equalsIgnoreCase(url)) {
                webView.loadUrl(url);
            }else if(currentUrl.equalsIgnoreCase("https://digihelpapp.com/app/home/login")){
                MainActivity.this.finish();
            }else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 3000);
            }
//            super.onBackPressed();
        }
    }


    private void firebaseNotification_OnCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID), getString(R.string.CHANNEL_NAME), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(getString(R.string.CHANNEL_DESC));
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        //subscribe to topics
//        FirebaseMessaging.getInstance().subscribeToTopic("notification");
        FirebaseMessaging.getInstance().subscribeToTopic("notification")
                .addOnCompleteListener(task -> {
                    String msg = "done subscribeToTopic notification";
                    if (!task.isSuccessful()) {
                        msg = "Failed subscribeToTopic notification";
                    }
                    Log.d(TAG, msg);
                });

    }


    private class MyWebClient extends WebViewClient {
        //        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//            //return super.shouldOverrideUrlLoading(view, url);
//            Uri uri;
//
//            try {
//                uri = Uri.parse(url);
//            } catch (NullPointerException e) {
//                // let Android deal with this
//                return true;
//            }
//
//            String host = uri.getHost(); //Host is null when user clicked on email, phone number, ...
//
//            if (host != null && host.equals("stackoverflow.com")) {
//                // This is my web site, so do not override; let my WebView load the page
//                return false;
//            }
//            else {
//                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs or anything else (email, phone number, ...)
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
//                return true;
//            }
//        }
        @Override
        public void onPageFinished(WebView view, String murl) {
            Log.d(TAG, "Page finished loading:" + murl);
           // SharedPreferences prefs = getPreferences(MODE_PRIVATE);

            //Log.d("checkLog", "inside on pagefinished " + murl);

//            //Is the url the login-page?
//            if (url.equals("https://littleleap.co.in/app/")) {
//                //load javascript to set the values to input fields
//                String mobile= prefs.getString("mobile", null);
//                Log.d(TAG, "app");
//                if (mobile!=null) {
//                    view.loadUrl("javascript:fillMobile(" + mobile + ");");
//                    Log.d(TAG, "app|mobile");
//                }
//            }
//            if (url.equals("https://littleleap.co.in/app/teacherlogin")) {
            if (murl.equals("http://192.168.29.156/newlittle/app/teacherlogin")) {
                Log.d(TAG, "teacherLogin");
                Toast.makeText(MainActivity.this, "teacherLogin", Toast.LENGTH_SHORT).show();
                //load javascript to set the values to input fields
//                String otp= prefs.getString("otp", null);
//                if (otp!=null) {
//                    Log.d(TAG, "teacherLogin|otp");
//                    view.loadUrl("javascript:fillOtp(" + otp + ");");
//                }
                if (_FcmToken != "") {
                    Log.d(TAG, "teacherLogin|_FcmToken");
//                    Toast.makeText(MainActivity.this, "teacherLogin|_FcmToken", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        webView.evaluateJavascript("fillFcmToken('" + _FcmToken + "');", null);
                    } else {
                        webView.loadUrl("javascript:fillFcmToken('" + _FcmToken + "');");
                    }
//                    webView.loadUrl("javascript:fillFcmToken('" + _FcmToken + "');");
                    Toast.makeText(MainActivity.this, "teacherLogin|_FcmToken|done", Toast.LENGTH_SHORT).show();
                }
            }

            super.onPageFinished(view, murl);
        }
    }

    private void callOnReady(String murl){
        Log.d("checkLog","inside callOnReady "+murl);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (murl.equalsIgnoreCase(url) && isLoaded) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callAppLaunchedFinished();
                            isLoaded = false;
                        }
                    },100);

                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callReferralMethod(sessionManager.getReferId(),sessionManager.getGameId());
                    }
                },1000);
            }
        });

    }

    private void callReferralMethod(String referralId,String gId) {
        if(gId!=null && gId.length()>2) {
            Log.d("checkLogD","inside if of callReferralMethod "+gId);
            gameId = gId;
            sessionManager.setGameIdOne(gId);
        }else{
            Log.d("checkLogD","inside else of callReferralMethod ");
        }
        if((referralId!=null && referralId.length()>0) && (gId!=null && gId.length()>2)) {
            Log.d("checkLogin", "inside if of ");
            String call = "javascript:callReferralSignupPlay(" + "'" + referralId + "'," + "'" + gId + "'" + ")";
            Log.d("checkLogin", "callReferralSignupPlay  url string is " + call);
            webView.loadUrl(call);
        } else if(referralId!=null && referralId.length()>0){
            Log.d("checkLogin", "inside else if of ");
//            String name="ABDC";
//            String call = "javascript:callReferralSignup(" + "'" + name + "'" + ")";
//            Log.d("checkLogin", "callReferralSignupPlay  url string is " + call);
//            webView.loadUrl(call);
            String call = "javascript:callReferralSignupPlay(" + "'" + referralId + "'," + "'" + gId + "'" + ")";
            Log.d("checkLogin", "callReferralSignupPlay  url string is " + call);
            webView.loadUrl(call);
        }else{
            Log.d("checkLogin", "inside else of ");
//            String call = "javascript:callReferralSignupPlay(" + "'" + referralId + "'," + "'" + gId + "'" + ")";
//            Log.d("checkLogin", "callReferralSignupPlay  url string is " + call);
//            webView.loadUrl(call);
        }
        sessionManager.setReferId("");
        Log.d("checkLogin", "callReferralMethod   " + gameId);
        sessionManager.setGameId("");
    }

    private void callAppLaunchedFinished() {
        String call = "javascript:callAppLaunchedFinished(" + ")";
       // String call = "callAppLaunchedFinished(" + ")";
        Log.d("checkLogin", "callAppLaunchedFinished method is " + call);
        webView.loadUrl(call);
    }


    private final class WebInterface {
        /**
         * Caution: If you've set your targetSdkVersion to 17 or higher, you must
         * add the @JavascriptInterface annotation to any method that you want
         * available to your JavaScript (the method must also be public). If you do
         * not provide the annotation, the method is not accessible by your web page
         * when running on Android 4.2 or higher.
         */

        private final WeakReference<Activity> mContextRef;
        private Toast toast;

        public WebInterface(Activity context) {
            this.mContextRef = new WeakReference<>(context);
        }

        @JavascriptInterface
        public void showToast(final String toastMsg) {
            if (TextUtils.isEmpty(toastMsg) || mContextRef.get() == null)
                return;
            // JavaScript doesn't run on the UI thread, make sure you do anything UI related like this
            // You don't need this for the Toast, but otherwise it's a good idea
            mContextRef.get().runOnUiThread(() -> {
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(mContextRef.get(), toastMsg, Toast.LENGTH_SHORT);
                toast.show();
            });
        }

        @JavascriptInterface
        public void setToken(String token) {
            // Toast.makeText(MainActivity.this, " setToken", Toast.LENGTH_SHORT).show();
            Log.d("checkLog", "inside setToken token is " + token);
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putString("accesstoken", token);
            editor.apply();
        }


        @JavascriptInterface
        public void isReady(String url) {
            Log.d("checkLog", "inside isReady "+url);
            callOnReady(url);
        }

        @JavascriptInterface
        public void setToken(String token, String studentId) {
            Log.d("checkLog", "token is " + token + " studentId is " + studentId);
            sessionManager.setUserId(token);
            sessionManager.setStudentId(studentId);
            Intent intent = new Intent(MainActivity.this, StoryActivity.class);
            MainActivity.this.startActivity(intent);
        }

        @JavascriptInterface
        public void setToken(String token, String studentId, String userName) {
            Log.d("checkLog", "token is " + token + " studentId is " + studentId + " username is " + userName);
            sessionManager.setGender(userName);
            sessionManager.setUserId(token);
            sessionManager.setStudentId(studentId);
            Intent intent = new Intent(MainActivity.this, StoryActivity.class);
            MainActivity.this.startActivity(intent);
        }

        @JavascriptInterface
        public void deleteToken() {
            Log.d("checkLog", "inside deleteToken token is ");
            // sessionManager.setToken(null);
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putString("accesstoken", null);
            editor.apply();
        }

        @JavascriptInterface
        public String getToken() {
            String accessToken = sessionManager.getUserId();//getPreferences(MODE_PRIVATE).getString("accesstoken", null);//sessionManager.getToken();
            Log.d("checkLog", "inside getToken token is " + accessToken);
            return accessToken;
        }

        @JavascriptInterface
        public void startReading(String token) {
            Log.d("checkLog", "token is " + token);
            Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, StoryActivity.class);
            MainActivity.this.startActivity(intent);
        }

        @JavascriptInterface
        public void startReading(String token, String studentId) {
            Log.d("checkLog", "token is " + token + " studentId is " + studentId);
            Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, StoryActivity.class);
            MainActivity.this.startActivity(intent);
        }

        @JavascriptInterface
        public void startReading(String device_id, String activity_id, String student_id) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id);
            //  Log.d("checkLog","token is "+token+" studentId is "+studentId+" username is "+userName);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            //    Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            // MainActivity.this.startActivityForResult(intent,199);
            myActivityResultLauncher.launch(intent);
        }

        @JavascriptInterface
        public void startReading(String device_id, String activity_id, String student_id, String index) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " length " + index);
            //  Log.d("checkLog","token is "+token+" studentId is "+studentId+" username is "+userName);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            //    Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            // MainActivity.this.startActivityForResult(intent,199);
            intent.putExtra("index", index);
            myActivityResultLauncher.launch(intent);
        }

        @JavascriptInterface
        public void startReading(String device_id, String activity_id, String student_id, String index,int unit_id,int chapter_id ) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " length " + index);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            //    Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            // MainActivity.this.startActivityForResult(intent,199);
            intent.putExtra("index", index);
            Log.d("checkLog","unitId is "+unit_id+" chapterId is "+chapter_id);
            intent.putExtra("unit_id", unit_id);
            intent.putExtra("chapter_id", chapter_id);
            myActivityResultLauncher.launch(intent);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @JavascriptInterface
        public void startReadingGame(String device_id, String activity_id, String student_id, String index, String gameTimeInMillis, int gameScore, String callbackUrl) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " length " + index);
            Log.d("checkLog","gameTimeInMillis is "+gameTimeInMillis+" gameScore is "+gameScore+" callbackUrl is "+callbackUrl);
            sessionManager.setActivityId(activity_id);
            //sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
           // sessionManager.setTimerGameTime(gameTimeInMillis*1000);
            gameRedirectUrl=callbackUrl;
            Log.d("checkLogT","timeinmillis are "+getMilliFromDate(gameTimeInMillis));
            endTimeMillis=getMilliFromDate(gameTimeInMillis);
            //    Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            // MainActivity.this.startActivityForResult(intent,199);
            intent.putExtra("index", index);
            intent.putExtra("Game",true);
            intent.putExtra("timemillis",gameTimeInMillis);
            intent.putExtra("score",gameScore);
            myActivityResultLauncherGame.launch(intent);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @JavascriptInterface
        public void startReadingGame(String device_id, String activity_id, String student_id, String index, String gameTimeInMillis, int gameScore, String callbackUrl,String scoreRedirectUrl) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " length " + index);
            Log.d("checkLog","gameTimeInMillis is "+gameTimeInMillis+" gameScore is "+gameScore+" callbackUrl is "+callbackUrl+" scoreRedirectUrl is "+scoreRedirectUrl);
            sessionManager.setActivityId(activity_id);
            //sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            // sessionManager.setTimerGameTime(gameTimeInMillis*1000);
            gameRedirectUrl=callbackUrl;
            scoreUrl=scoreRedirectUrl;
            Log.d("checkLogT","timeinmillis are "+getMilliFromDate(gameTimeInMillis));
            endTimeMillis=getMilliFromDate(gameTimeInMillis);
            //    Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            // MainActivity.this.startActivityForResult(intent,199);
            intent.putExtra("index", index);
            intent.putExtra("Game",true);
            intent.putExtra("timemillis",gameTimeInMillis);
            intent.putExtra("score",gameScore);
            intent.putExtra("gameId",gameId);
            myActivityResultLauncherGame.launch(intent);
        }



        @RequiresApi(api = Build.VERSION_CODES.N)
        @JavascriptInterface
        public void startReadingGame(String device_id, String activity_id, String student_id, String index, String gameTimeInMillis, int gameScore, String callbackUrl,String scoreRedirectUrl,int unit_id,int chapter_id ) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " length " + index);
            Log.d("checkLog","gameTimeInMillis is "+gameTimeInMillis+" gameScore is "+gameScore+" callbackUrl is "+callbackUrl+" scoreRedirectUrl is "+scoreRedirectUrl);
            sessionManager.setActivityId(activity_id);
            //sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            // sessionManager.setTimerGameTime(gameTimeInMillis*1000);
            gameRedirectUrl=callbackUrl;
            scoreUrl=scoreRedirectUrl;
            Log.d("checkLogT","timeinmillis are "+getMilliFromDate(gameTimeInMillis));
            endTimeMillis=getMilliFromDate(gameTimeInMillis);
            //    Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            // MainActivity.this.startActivityForResult(intent,199);
            intent.putExtra("index", index);
            intent.putExtra("Game",true);
            intent.putExtra("timemillis",gameTimeInMillis);
            intent.putExtra("score",gameScore);
            intent.putExtra("gameId",gameId);
            Log.d("checkLog","unitId is "+unit_id+" chapterId is "+chapter_id);
            intent.putExtra("unit_id", unit_id);
            intent.putExtra("chapter_id", chapter_id);
            myActivityResultLauncherGame.launch(intent);
        }

        @JavascriptInterface
        public void setAvatar(String avatar, String name, String desc, String likes) {
            Log.d("checkLog", "avatar is " + avatar + " name is " + name);
            Log.d("checkLog", "desc is " + desc + " likes is " + likes);
            sessionManager.setBlobId(avatar);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setFirstName(name);
            setAvatarImage();
            callFcmId(sessionManager.getStudentId());
        }

        @JavascriptInterface
        public void setAvatar(String avatar, String name, String desc, String likes,String studentName) {
            Log.d("checkLog", "avatar is " + avatar + " name is " + name);
            Log.d("checkLog", "desc is " + desc + " likes is " + likes +" studentName "+studentName);
            sessionManager.setBlobId(avatar);
            if(studentName!=null && studentName.length()>0) {
                sessionManager.setGender(studentName);
            }
            sessionManager.setFirstName(name);
            setAvatarImage();
            callFcmId(sessionManager.getStudentId());
        }

        @JavascriptInterface
        public void startSpeaking(String device_id, String activity_id, String student_id) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id);
            //  Log.d("checkLog","token is "+token+" studentId is "+studentId+" username is "+userName);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            //  Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, RecordingActivity.class);
//            MainActivity.this.startActivity(intent);
            myActivityResultLauncher.launch(intent);
            if (sessionManager.getUncaughtError() != null && sessionManager.getUncaughtError().length() > 0) {
                sentEvent(student_id);
                sessionManager.setUncaughtError(null);
            }
        }

        @JavascriptInterface
        public void startSpeaking(String device_id, String activity_id, String student_id, String index,int unit_id,int chapter_id) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " index " + index);
            //  Log.d("checkLog","token is "+token+" studentId is "+studentId+" username is "+userName);
            Log.d("checkLog","unitId is "+unit_id+" chapterId is "+chapter_id);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            //  Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, RecordingActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("unit_id", unit_id);
            intent.putExtra("chapter_id", chapter_id);
            myActivityResultLauncher.launch(intent);
            if (sessionManager.getUncaughtError() != null && sessionManager.getUncaughtError().length() > 0) {
                sentEvent(student_id);
                sessionManager.setUncaughtError(null);
            }
            // MainActivity.this.startActivity(intent);
        }


        @JavascriptInterface
        public void startSpeaking(String device_id, String activity_id, String student_id, String index) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " index " + index);
            //  Log.d("checkLog","token is "+token+" studentId is "+studentId+" username is "+userName);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            //  Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, RecordingActivity.class);
            intent.putExtra("index", index);
            myActivityResultLauncher.launch(intent);
            if (sessionManager.getUncaughtError() != null && sessionManager.getUncaughtError().length() > 0) {
                sentEvent(student_id);
                sessionManager.setUncaughtError(null);
            }
            // MainActivity.this.startActivity(intent);
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        @JavascriptInterface
        public void startSpeakingGame(String device_id, String activity_id, String student_id, String index, String gameTimeInMillis, int gameScore, String callbackUrl) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " index " + index);
            //Log.d("checkLog","token is "+token+" studentId is "+studentId+" username is "+userName);
            Log.d("checkLog","gameTimeInMillis is "+gameTimeInMillis+" gameScore is "+gameScore+" callbackUrl is "+callbackUrl);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            //   sessionManager.setTimerGameTime(gameTimeInMillis);
            gameRedirectUrl=callbackUrl;
            endTimeMillis=getMilliFromDate(gameTimeInMillis);
            //  Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, RecordingActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("Game",true);
            intent.putExtra("score",gameScore);
            myActivityResultLauncherGame.launch(intent);
            if (sessionManager.getUncaughtError() != null && sessionManager.getUncaughtError().length() > 0) {
                sentEvent(student_id);
                sessionManager.setUncaughtError(null);
            }
            // MainActivity.this.startActivity(intent);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @JavascriptInterface
        public void startSpeakingGame(String device_id, String activity_id, String student_id, String index, String gameTimeInMillis, int gameScore, String callbackUrl,String scoreRedirectUrl) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " index " + index);
            //Log.d("checkLog","token is "+token+" studentId is "+studentId+" username is "+userName);
            Log.d("checkLog","gameTimeInMillis is "+gameTimeInMillis+" gameScore is "+gameScore+" callbackUrl is "+callbackUrl);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            //   sessionManager.setTimerGameTime(gameTimeInMillis);
            gameRedirectUrl=callbackUrl;
            scoreUrl=scoreRedirectUrl;
            endTimeMillis=getMilliFromDate(gameTimeInMillis);
            //  Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, RecordingActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("Game",true);
            intent.putExtra("score",gameScore);
            intent.putExtra("gameId",gameId);
            myActivityResultLauncherGame.launch(intent);
            if (sessionManager.getUncaughtError() != null && sessionManager.getUncaughtError().length() > 0) {
                sentEvent(student_id);
                sessionManager.setUncaughtError(null);
            }
            // MainActivity.this.startActivity(intent);
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        @JavascriptInterface
        public void startSpeakingGame(String device_id, String activity_id, String student_id, String index, String gameTimeInMillis, int gameScore, String callbackUrl,String scoreRedirectUrl,int unit_id,int chapter_id) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " index " + index);
            //Log.d("checkLog","token is "+token+" studentId is "+studentId+" username is "+userName);
            Log.d("checkLog","gameTimeInMillis is "+gameTimeInMillis+" gameScore is "+gameScore+" callbackUrl is "+callbackUrl);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            //   sessionManager.setTimerGameTime(gameTimeInMillis);
            gameRedirectUrl=callbackUrl;
            scoreUrl=scoreRedirectUrl;
            endTimeMillis=getMilliFromDate(gameTimeInMillis);
            //  Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, RecordingActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("Game",true);
            intent.putExtra("score",gameScore);
            intent.putExtra("gameId",gameId);
            Log.d("checkLog","unitId is "+unit_id+" chapterId is "+chapter_id);
            intent.putExtra("unit_id", unit_id);
            intent.putExtra("chapter_id", chapter_id);
            myActivityResultLauncherGame.launch(intent);
            if (sessionManager.getUncaughtError() != null && sessionManager.getUncaughtError().length() > 0) {
                sentEvent(student_id);
                sessionManager.setUncaughtError(null);
            }
            // MainActivity.this.startActivity(intent);
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        public long getMilliFromDate(String dateFormat) {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = formatter.parse(dateFormat);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d("checkLogT","exception is "+e.toString());
            }
            System.out.println("Today is " + date);
            System.out.println("Today is " + date.getTime());
            return date.getTime();
        }

        @JavascriptInterface
        public void startDuet(String device_id, String activity_id, String student_id) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id);
            //  Log.d("checkLog","token is "+token+" studentId is "+studentId+" username is "+userName);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            //  Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, DuetActivity.class);
            //MainActivity.this.startActivity(intent);
            myActivityResultLauncher.launch(intent);
        }

        @JavascriptInterface
        public void startDuet(String device_id, String activity_id, String student_id, String index) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " index " + index);
            //  Log.d("checkLog","token is "+token+" studentId is "+studentId+" username is "+userName);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            //  Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, DuetActivity.class);
            intent.putExtra("index", index);
            // MainActivity.this.startActivity(intent);
            myActivityResultLauncher.launch(intent);

            if (sessionManager.getUncaughtError() != null && sessionManager.getUncaughtError().length() > 0) {
                sentEvent(student_id);
                sessionManager.setUncaughtError(null);
            }
        }

        @JavascriptInterface
        public void startDuet(String device_id, String activity_id, String student_id, String index,int unit_id,int chapter_id) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " index " + index);
            //  Log.d("checkLog","token is "+token+" studentId is "+studentId+" username is "+userName);
            Log.d("checkLog","unitId is "+unit_id+" chapterId is "+chapter_id);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            //  Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, DuetActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("unit_id", unit_id);
            intent.putExtra("chapter_id", chapter_id);
            // MainActivity.this.startActivity(intent);
            myActivityResultLauncher.launch(intent);

            if (sessionManager.getUncaughtError() != null && sessionManager.getUncaughtError().length() > 0) {
                sentEvent(student_id);
                sessionManager.setUncaughtError(null);
            }
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        @JavascriptInterface
        public void startDuetGame(String device_id, String activity_id, String student_id, String index,String gameTimeInMillis, int gameScore,String callbackUrl) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " index " + index);
            //  Log.d("checkLog","token is "+token+" studentId is "+studentId+" username is "+userName);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
           // sessionManager.setTimerGameTime(gameTimeInMillis*1000);
            gameRedirectUrl=callbackUrl;
            endTimeMillis=getMilliFromDate(gameTimeInMillis);
            //  Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, DuetActivity.class);

            intent.putExtra("index", index);
            intent.putExtra("Game",true);
            intent.putExtra("score",gameScore);
            // MainActivity.this.startActivity(intent);
            myActivityResultLauncherGame.launch(intent);

            if (sessionManager.getUncaughtError() != null && sessionManager.getUncaughtError().length() > 0) {
                sentEvent(student_id);
                sessionManager.setUncaughtError(null);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @JavascriptInterface
        public void startDuetGame(String device_id, String activity_id, String student_id, String index,String gameTimeInMillis, int gameScore,String callbackUrl,String scoreRedirectUrl) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " index " + index);
            //  Log.d("checkLog","token is "+token+" studentId is "+studentId+" username is "+userName);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            // sessionManager.setTimerGameTime(gameTimeInMillis*1000);
            gameRedirectUrl=callbackUrl;
            scoreUrl=scoreRedirectUrl;
            endTimeMillis=getMilliFromDate(gameTimeInMillis);
            //  Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, DuetActivity.class);

            intent.putExtra("index", index);
            intent.putExtra("Game",true);
            intent.putExtra("score",gameScore);
            intent.putExtra("gameId",gameId);
            // MainActivity.this.startActivity(intent);
            myActivityResultLauncherGame.launch(intent);

            if (sessionManager.getUncaughtError() != null && sessionManager.getUncaughtError().length() > 0) {
                sentEvent(student_id);
                sessionManager.setUncaughtError(null);
            }
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        @JavascriptInterface
        public void startDuetGame(String device_id, String activity_id, String student_id, String index,String gameTimeInMillis, int gameScore,String callbackUrl,String scoreRedirectUrl,int unit_id,int chapter_id) {
            Log.d("checkLog", "token is " + device_id + " studentId is " + student_id + " username is " + activity_id + " index " + index);
            //  Log.d("checkLog","token is "+token+" studentId is "+studentId+" username is "+userName);
            sessionManager.setActivityId(activity_id);
            //  sessionManager.setSubActivityId(sub_activity_id);
            sessionManager.setUserId(device_id);
            sessionManager.setStudentId(student_id);
            // sessionManager.setTimerGameTime(gameTimeInMillis*1000);
            gameRedirectUrl=callbackUrl;
            scoreUrl=scoreRedirectUrl;
            endTimeMillis=getMilliFromDate(gameTimeInMillis);
            //  Toast.makeText(MainActivity.this, "startReading started", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, DuetActivity.class);

            intent.putExtra("index", index);
            intent.putExtra("Game",true);
            intent.putExtra("score",gameScore);
            intent.putExtra("gameId",gameId);
            Log.d("checkLog","unitId is "+unit_id+" chapterId is "+chapter_id);
            intent.putExtra("unit_id", unit_id);
            intent.putExtra("chapter_id", chapter_id);
            // MainActivity.this.startActivity(intent);
            myActivityResultLauncherGame.launch(intent);
            if (sessionManager.getUncaughtError() != null && sessionManager.getUncaughtError().length() > 0) {
                sentEvent(student_id);
                sessionManager.setUncaughtError(null);
            }
        }

        @JavascriptInterface
        public void saveMobile(String mobile) {
            if (mobile == null)
                return;
            //save the values in SharedPrefs
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putString("mobile", mobile);
            editor.apply();
        }

        @JavascriptInterface
        public void saveOtp(String otp) {
            if (otp == null)
                return;
            //save the values in SharedPrefs
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putString("otp", otp);
            editor.apply();
        }

        @JavascriptInterface
        public void setGameId(String gameId) {
            Log.d("checkLogD", " setGameId gameId " + gameId);
            sessionManager.setGameIdOne(gameId);
        }

        @JavascriptInterface
        public void callMessage(String msg) {
            Log.d("checkLog", " callMessage msg " + msg);
            speakOne(msg);
        }

        @JavascriptInterface
        public void createInviteGame(String referral,String gameId) {
            Log.d("checkLog", " createInviteGame referral " + referral+ "gameid is "+gameId);
            createManualDynamicLinkGame(referral,gameId);
        }

        @JavascriptInterface
        public void createInviteRegistration(String referral) {
            Log.d("checkLog", " createInviteRegistration referral " + referral);
            createManualDynamicLink(referral);
        }

        @JavascriptInterface
        public void callInvite(String referral) {
            Log.d("checkLog", " callInvite referral " + referral);
            inviteGame(referral);
        }

        @JavascriptInterface
        public void showCelebration() {
            Log.d("checkLog", " callMessage showCelebration ");
            showConfetti();
        }

        @JavascriptInterface
        public void getAppVersion() {
            Log.d("checkLog", " getAppVersion getAppVersion ");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callAppVersion();
                }
            });
        }

        @JavascriptInterface
        public void getMediaPermission() {
            Log.d("checkLog", " callMessage showCelebration ");
            allowPermission();
        }

        @JavascriptInterface
        public void setOtpListener() {
            Log.d("checkLog", " setOtpListener called ");
            registerListener();
        }

        @JavascriptInterface
        public void callInvitation(String url) {
            Log.d("checkLog", " callMessage showCelebration ");
            callInvite(url);
        }

    }

    private FirebaseAnalytics mFirebaseAnalytics;

    private void sentEvent(String studentId) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, studentId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, sessionManager.getUncaughtError());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void allowPermission() {
        // , Manifest.permission.CAMERA
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.isAnyPermissionPermanentlyDenied()) {
                    //permissionSettingForQRCode(getString(R.string.need_permission), getString(R.string.need_permission_message), QRCodeActivity.this);
                } else {
                    if (report.areAllPermissionsGranted()) {
                        // selectImage();
                        callPermission(true);
                    } else {
                        callPermission(false);
                        // allowPermission();
                        //showPermissionDenyDialog(true,true);
                    }
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

    }

    private void callAppVersion() {
        String version = BuildConfig.VERSION_NAME;
        String call = "javascript:showVersion(" + "'" + version + "'" + ")";
        Log.d("checkLogin", "callOTP url string is " + call);
        webView.loadUrl(call);
    }

    private void callRef(String link) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String call = "javascript:sendReferralLink(" + "'" + link + "'" + ")";
                Log.d("checkLogin", "callOTP url string is " + call);
                webView.loadUrl(call);
            }
        });

    }

    private void callRefOne(String link) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String call = "javascript:sendReferralLinkRegistration(" + "'" + link + "'" + ")";
                Log.d("checkLogin", "callOTP url string is " + call);
                webView.loadUrl(call);
            }
        });
    }


    private void callFcmId(String studentId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (studentId != null && studentId.length() > 0) {
                    String fcmId = sessionManager.getFcmId();
                    if (fcmId != null && fcmId.length() > 0) {
                        String call = "javascript:setFcmId(" + "'" + fcmId + "'," + "'" + studentId + "'" + ")";
                        Log.d("checkLogin", "callFcmId url string is " + call);
                        webView.loadUrl(call);
                    }
                }
            }
        });

    }

    private void callPermission(boolean allDone) {
        String call = "javascript:permissionResult(" + "'" + allDone + "'" + ")";
        Log.d("checkLogin", "callOTP url string is " + call);
        webView.loadUrl(call);
    }


    ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("checkLog", "inside myActivityResultLauncher ");
                    webView.reload();
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        // Here, no request code
//                        Intent data = result.getData();
//                        Log.d("checkLog","inside onactivityresult 199 ");
//                        callControl(sessionManager.getStudentId(),sessionManager.getActivityId());
//                    }else{
//                        webView.reload();
//                    }
                }
            });
    private int type=0,index=0;
    private String activityId="";
    ActivityResultLauncher<Intent> myActivityResultLauncherGame = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("checkLogResult", "inside myActivityResultLauncherGame ");
                    Intent intent=result.getData();
                    if(intent!=null){
                        Log.d("checkLog", " inside intent not null myActivityResultLauncherGame ");
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            activityId = intent.getStringExtra("activityId");
                            boolean isScore = intent.getBooleanExtra("isScore", false);
                            type = intent.getIntExtra("type", 0);
                            index = intent.getIntExtra("index", 0);
                            Log.d("checkLog", "inside myActivityResultLauncherGame " + activityId + " isScore " + isScore + " type " + type +" index is "+index);
                            String url = MainActivity.scoreUrl + "?activityId=" + activityId + "&type=" + type+ "&index=" + index;
                            Log.d("checkLog", "inside myActivityResultLauncherGame " +url);
                            webView.loadUrl(url);
                        }else{
                            boolean isCompleted = intent.getBooleanExtra("isCompleted", false);
                            int type = intent.getIntExtra("type", 0);
                            if(isCompleted){
                                UpdatePagination(type);
                            }
                            webView.reload();
                        }
                    }else {
                        Log.d("checkLog", " inside intent null myActivityResultLauncherGame ");
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Here, no request code
                            webView.loadUrl(gameRedirectUrl);
                            sessionManager.setGameId("");
                        } else {
                            webView.reload();
                        }
                    }
                }
            });


    private void UpdatePagination(int type) {
        String call = "javascript:UpdatePagination(" + "'" + type + "'" + ")";
        Log.d("checkLogin", "UpdatePagination url string is " + call);
        webView.loadUrl(call);
    }

    private void callOTP(String otp) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },2000);

        if(!isOtpDone) {
            isOtpDone=true;
            String call = "javascript:getOtpFromNative(" + "'" + otp + "'" + ")";
            Log.d("checkLogin", "callOTP url string is " + call);
            webView.loadUrl(call);
            if (mySMSBroadcastReceiver != null) {
                unregisterReceiver(mySMSBroadcastReceiver);
                //mySMSBroadcastReceiver = null;
            }
        }

    }

    private void callSuccess(JSONObject jsonObject, String pnrNumber) {
        String call = "javascript:paymentVerifyExternal(" + "'" + pnrNumber + "','" + jsonObject.toString() + "'" + ")";
        Log.d("checkLogin", "url string is " + call);
        webView.loadUrl(call);
    }

    private void speakOne(String msg) {
        String msgOne="Let's Harendra Doesn't have job";
        Speech.getInstance().setTextToSpeechRate(Cons.speed);
        // Locale mLocale=new Locale("hi_IN");
        //Locale mLocale=new Locale("en_IN");
        // Locale mLocale=new Locale("hi", "IN");
        //Speech.getInstance().setLocale(mLocale);
        // Speech.init()
//        Set<String> a=new HashSet<>();
//        a.add("male");//here you can give male if you want to select male voice.
        //Voice v=new Voice("en-us-x-sfg#female_2-local",new Locale("en","US"),400,200,true,a);
//        Voice v=new Voice("en-us-x-sfg#male_2-local",new Locale("en","hi_IN"),400,200,true,a);
        Voice voice = Speech.getInstance().getTextToSpeechVoice();
        String name = voice.getName();
        Log.d("checkLogName", "name is " + name);
        //Speech.getInstance().setVoice(v);
        Locale locale = new Locale("hi_IN");
        //Speech.getInstance().setTextToSpeechPitch(1.25f);
        Speech.getInstance().setLocale(locale);
        List<Voice> voiceList = Speech.getInstance().getSupportedTextToSpeechVoices();
        for (int i = 0; i < voiceList.size(); i++) {
            Voice mVoice = voiceList.get(i);
            Log.d("checkLogName","name is "+mVoice.getName());
            if(mVoice.getName().equalsIgnoreCase("hi-in-x-hia-local")){
                Speech.getInstance().setVoice(mVoice);
                break;
            }else if (mVoice.getName().equalsIgnoreCase("hi-IN-language")) {
                Speech.getInstance().setVoice(mVoice);
                break;
            } else if (mVoice.getName().equalsIgnoreCase("hi-in-x-hie-network")) {
                Speech.getInstance().setVoice(mVoice);
                break;
            }
        }

        Speech.getInstance().say(msg, new TextToSpeechCallback() {
            @Override
            public void onStart() {
                if (msg.length() < 40) {
                    setAvatarAnimation(4000, 1000);
                } else if (msg.length() < 80) {
                    setAvatarAnimation(8000, 1000);
                } else {
                    setAvatarAnimation(10000, 1000);
                }
                // onBeatSound();
                // Log.i("speech", "speech started");
                //Toast.makeText(HomeActivity.this, "Start..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted() {
                // offBeatSound();
                // Log.i("speech", "speech completed");
                // Toast.makeText(HomeActivity.this, "Completed..", Toast.LENGTH_SHORT).show();
                //speechRecognizer.startListening(speechRecognizerIntent);
            }

            @Override
            public void onError() {
                Log.i("speech", "speech error");
                // Toast.makeText(HomeActivity.this, "Error..", Toast.LENGTH_SHORT).show();
            }
        });
       // speakTest(msgOne);
    }


    private void speakTest(String msg) {
        Locale locale = new Locale("hi_IN");
        //Speech.getInstance().setTextToSpeechPitch(1.25f);
        Speech.getInstance().setLocale(locale);
        List<Voice> voiceList = Speech.getInstance().getSupportedTextToSpeechVoices();
        for (int i = 0; i < voiceList.size(); i++) {
            Voice mVoice = voiceList.get(i);
            Log.d("checkLogSp","voice name is "+mVoice.getName()+" "+mVoice.getLocale());
            Speech.getInstance().setVoice(mVoice);
            Speech.getInstance().say(msg, new TextToSpeechCallback() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError() {

                }
            });
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setAvatarImage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                girlImage.setVisibility(View.VISIBLE);
//                if(resAvatar!=null && resAvatar.length>1){
//                    girlImage.setImageResource(resAvatar[resAvatar.length-1]);
//                }else {
//                    Glide.with(MainActivity.this).
//                            load(sessionManager.getBlobId())
//                            .apply(new RequestOptions().placeholder(R.mipmap.girl_image).error(R.mipmap.girl_image))
//                            .into(girlImage);
//                }
                Glide.with(MainActivity.this).
                        load(sessionManager.getBlobId())
                        .apply(new RequestOptions().placeholder(R.mipmap.girl_image).error(R.mipmap.girl_image))
                        .into(girlImage);
                setTouchListener();
            }
        });

    }


    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        overlayResultLauncher.launch(intent);
    }

    private void setAvatarImageOne() {
        girlImage.setVisibility(View.VISIBLE);
//        if(resAvatar!=null && resAvatar.length>1){
//            girlImage.setImageResource(resAvatar[resAvatar.length-1]);
//        }else {
//            Glide.with(MainActivity.this).
//                    load(sessionManager.getBlobId())
//                    .apply(new RequestOptions().placeholder(R.mipmap.girl_image).error(R.mipmap.girl_image))
//                    .into(girlImage);
//        }
        Glide.with(MainActivity.this).
                load(sessionManager.getBlobId())
                .apply(new RequestOptions().placeholder(R.mipmap.girl_image).error(R.mipmap.girl_image))
                .into(girlImage);
        setTouchListener();
    }


    private int xDelta, yDelta;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            final int x = (int) event.getRawX();
            final int y = (int) event.getRawY();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: {
                    ConstraintLayout.LayoutParams lParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();

                    xDelta = x - lParams.leftMargin;
                    yDelta = y - lParams.topMargin;
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    Toast.makeText(getApplicationContext(), "Объект перемещён", Toast.LENGTH_SHORT).show();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (x - xDelta + view.getWidth() <= container.getWidth()
                            && y - yDelta + view.getHeight() <= container.getHeight()
                            && x - xDelta >= 0
                            && y - yDelta >= 0) {
                        ConstraintLayout.LayoutParams layoutParams =
                                (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                    }
                    break;
                }
            }
            container.invalidate();
            return true;
        }
    };


    private void setTouchListener() {
        girlImage.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) girlImage.getLayoutParams();
              //  Log.d("checkLog", "params are leftMargin " + params.leftMargin + " rightMargin " + params.rightMargin + " topMargin " + v.getTop() + " bottomMargin " + params.bottomMargin);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.leftMargin;
                        initialY = v.getTop() > 0 ? v.getTop() : 600;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        // Toast.makeText(MainActivity.this, "Action UP", Toast.LENGTH_SHORT).show();
                        //when the drag is ended switching the state of the widget
//                        collapsedView.setVisibility(View.GONE);
//                        expandedView.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers
                        //  params.leftMargin = initialX + (int) (event.getRawX() - initialTouchX);
                        params.topMargin = initialY + (int) (event.getRawY() - initialTouchY);
                        // params.rightMargin = width-(params.leftMargin+girlImage.getWidth());//initialX - (int) (event.getRawX() + initialTouchX);
                        params.bottomMargin = height - (params.topMargin + girlImage.getHeight());//initialY - (int) (event.getRawY() + initialTouchY);
                       // Log.d("checkLog", "params after are leftMargin " + params.leftMargin + " rightMargin " + params.rightMargin + " topMargin " + params.topMargin + " bottomMargin " + params.bottomMargin);
                        girlImage.setLayoutParams(params);
                        //  mWindowManager.updateViewLayout(activityHomeBinding.girlImage, params);
                        return true;
                }
                container.invalidate();
                return false;
            }
        });
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        Log.d("checkLogN","onInternetConnectivityChanged");
        if (isConnected) {
            //do Stuff with internet
            netIsOn();
        } else {
            //no internet
            netIsOff();
        }
    }


    //  Fragment currentFragment = null;

    //  List<BookTicketPojo> bookTicketModelList;
    // private int counter = 0;

    private void netIsOn() {
        Log.d("checkLogN", "inside net netIsOn");
        webView.setVisibility(View.VISIBLE);
        connectivityRelative.setVisibility(View.GONE);
        if(isNetOff) {
            webView.reload();
            isNetOff = false;
        }
//        Intent intentOne = new Intent(Constants.PUSH_NOTIFICATION);
//        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intentOne);
    }

    private boolean isNetOff=false;
    private void netIsOff() {
        isNetOff=true;
        Log.d("checkLogN", "inside net netIsOff");
        webView.setVisibility(View.GONE);
        connectivityRelative.setVisibility(View.VISIBLE);
//        Intent internetIntent = new Intent(MainActivity.this, InternetActivity.class);
//        internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        netResultLauncher.launch(internetIntent);
    }

    ActivityResultLauncher<Intent> netResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();
                        webView.reload();
                    }
                }
            });

    ActivityResultLauncher<Intent> overlayResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)) {
                            //   askPermission();
                        } else {
                            startService(new Intent(MainActivity.this, FloatingViewService.class));
                        }
                    }
                }
            });

    private void showConfetti() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
//                Random r=new Random();
//                int c=r.nextInt(100);
//                if(c%2==0) {
//                    showConfettiCenter();
//                }else{
//                    showConfettiTop();
//                }
//                playFileOne();
                chooseAnim();
            }
        });


    }


    private int[] resLottie = {R.raw.celebration_giraffe,
            R.raw.star_gold_icon_loop, R.raw.sticker_flying, R.raw.party_hat, R.raw.dancing_burger, R.raw.dance_party, R.raw.monkey_celeb};

    // private String [] resLottieOne={"https://assets1.lottiefiles.com/private_files/lf30_kzsvoqie.json"};
    private void chooseAnim() {
        Random r = new Random();
        int d = r.nextInt(100);
        if (d % 2 == 0) {
            int c = r.nextInt(100);
            if (c % 2 == 0) {
                showConfettiCenter();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        initAnimation(startImage.getX() / 6, 400, startImage.getY() / 4, -800);
//                        // initAnimation(textArray[counter].getX(),activityHomeBinding.scoreText.getX(),textArray[counter].getY(),activityHomeBinding.scoreText.getY());
//                        runAnimation();
//                    }
//                }, 2000);
            } else {
                showConfettiTop();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                        initAnimation(startImage.getX() / 6, 400, startImage.getY() / 4, -800);
////                        // initAnimation(textArray[counter].getX(),activityHomeBinding.scoreText.getX(),textArray[counter].getY(),activityHomeBinding.scoreText.getY());
////                        runAnimation();
//                    }
//                }, 2000);
            }
        } else {
            animationView.setVisibility(View.VISIBLE);
            //activityHomeBinding.animationView.loop(true);
            int n = r.nextInt(resLottie.length);
            Log.d("checkLogLottie", "inside else of lottie " + n);
            // activityHomeBinding.animationView.setAnimationFromUrl(resLottieOne[0]);
            animationView.setAnimation(resLottie[n]);
            animationView.playAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    animationView.pauseAnimation();
                    animationView.setVisibility(View.GONE);
//                    initAnimation(startImage.getX() / 6, 400, startImage.getY() / 4, -800);
//                    // initAnimation(textArray[counter].getX(),activityHomeBinding.scoreText.getX(),textArray[counter].getY(),activityHomeBinding.scoreText.getY());
//                    runAnimation();
                }
            }, 2000);
        }
        playFileOne();
    }

    public void runAnimation() {
        Log.d("checkLog", "inside runAnimation ");
//        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //   View view = mInflater.inflate(R.layout.star_item, null, false);
        // activityHomeBinding.startImage.setAnimation(animation);
        startImage.setVisibility(View.VISIBLE);
        startImage.startAnimation(animation);
        startImage.invalidate();
    }

    TranslateAnimation animation;

    public void initAnimation(float fromXDelta, float toXDelta, float fromYdelta,
                              float toYDelta) {
        Log.d("checkLog", "inside initAnimation  fromXDelta " + fromXDelta + " toXDelta " + toXDelta + " fromYdelta " + fromYdelta + " toYDelta " + toYDelta);
        animation = new TranslateAnimation(fromXDelta, toXDelta, fromYdelta, toYDelta); //(float From X,To X, From Y, To Y)
        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());
        Log.d("checkLog", "inside initAnimation ");
    }

    private class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.d("checkLog", " onAnimationEnd ");
            startImage.setVisibility(View.GONE);
            startImage.clearAnimation();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            Log.d("checkLog", " onAnimationRepeat ");
        }

        @Override
        public void onAnimationStart(Animation animation) {
            Log.d("checkLog", " onAnimationStart ");
            playFile();
        }

    }

    private void playFile() {
        try {
            mPlayer.start();
        } catch (IllegalStateException ex) {

        }
    }


    private void showConfettiTop() {
        viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                .setPosition(-20f, viewKonfetti.getWidth() + 50f, -20f, -50f)
                .streamFor(300, 1500L);
    }

    private void showConfettiCenter() {
        viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                .setPosition(viewKonfetti.getX() + viewKonfetti.getWidth() / 2, viewKonfetti.getY() + viewKonfetti.getHeight() / 3)
                .burst(200);
    }


    private void playFileOne() {
        try {
            mPlayerOne.start();
        } catch (IllegalStateException ex) {

        }
    }


    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;

    class MyWebChromeClient extends android.webkit.WebChromeClient {
        /**
         * Instantiate the interface and set the context
         *
         * @param c
         */
        public MyWebChromeClient(MainActivity c) {
            super();
        }

        // For 3.0+ Devices (Start)
        // onActivityResult attached before constructor
        protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            imageLauncherFileChooser.launch(Intent.createChooser(i, "File Chooser"));
            // startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }


        // For Lollipop 5.0+ Devices
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, android.webkit.WebChromeClient.FileChooserParams fileChooserParams) {
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }

            uploadMessage = filePathCallback;

            Intent intent = fileChooserParams.createIntent();
            try {
                // startActivityForResult(intent, REQUEST_SELECT_FILE);
                imageLauncherOne.launch(intent);
            } catch (ActivityNotFoundException e) {
                uploadMessage = null;
                Toast.makeText(MainActivity.this, "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

        //For Android 4.1 only
        protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            imageLauncherFileChooser.launch(Intent.createChooser(intent, "File Chooser"));
            //startActivityForResult(Intent.createChooser(intent, "File Chooser"), FILECHOOSER_RESULTCODE);
        }

        protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            imageLauncherFileChooser.launch(Intent.createChooser(i, "File Chooser"));
            //  startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }
    }

    ActivityResultLauncher<Intent> imageLauncherOne = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (uploadMessage == null)
                                return;
                            uploadMessage.onReceiveValue(android.webkit.WebChromeClient.FileChooserParams.parseResult(result.getResultCode(), result.getData()));
                            uploadMessage = null;

                        } else
                            Toast.makeText(MainActivity.this, "Failed to Upload Image", Toast.LENGTH_LONG).show();
                    }
                }
            });

    ActivityResultLauncher<Intent> imageLauncherFileChooser = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        if (null == mUploadMessage) {
                            Toast.makeText(MainActivity.this, "Failed to Upload Image", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
                        // Use RESULT_OK only if you're implementing WebView inside an Activity
                        Uri resultOne = result.getData() == null || result.getResultCode() != MainActivity.RESULT_OK ? null : result.getData().getData();
                        mUploadMessage.onReceiveValue(resultOne);
                        mUploadMessage = null;
                    }
                }
            });


    private AnimationDrawable ad;
    public static int[] resAvatar = new int[4];

    private void setAvatarAnimation(long millis, long interval) {
        Log.d("checkLog", "inside setAvatarAnimation ");
        String name = sessionManager.getBlobId();
        String[] nameArray = name.split("/");
        if (nameArray.length > 1) {
            String imageName = nameArray[nameArray.length - 1];
            if (imageName.contains("1")) {
                ad = (AnimationDrawable) getResources().getDrawable(
                        R.drawable.one);
                Log.d("checkLog", "inside setAvatarAnimation one");
                resAvatar[3] = R.drawable.one_one;
                resAvatar[0] = R.drawable.one_two;
                resAvatar[1] = R.drawable.one_three;
                resAvatar[2] = R.drawable.one_four;
            } else if (imageName.contains("2")) {
                ad = (AnimationDrawable) getResources().getDrawable(
                        R.drawable.two);
                Log.d("checkLog", "inside setAvatarAnimation Two");
                resAvatar[3] = R.drawable.two_one;
                resAvatar[0] = R.drawable.two_two;
                resAvatar[1] = R.drawable.two_three;
                resAvatar[2] = R.drawable.two_four;
            } else if (imageName.contains("3")) {
                ad = (AnimationDrawable) getResources().getDrawable(
                        R.drawable.three);
                Log.d("checkLog", "inside setAvatarAnimation Three");
                resAvatar[3] = R.drawable.three_one;
                resAvatar[0] = R.drawable.three_two;
                resAvatar[1] = R.drawable.three_three;
                resAvatar[2] = R.drawable.three_four;
            } else if (imageName.contains("4")) {
//                ad = (AnimationDrawable) getResources().getDrawable(
//                        R.drawable.four);
                //iv.setBackgroundResource(R.drawable.four);
                // resAvatar[0]=R.drawable.four_one;
                resAvatar[3] = R.drawable.four_one;
                resAvatar[0] = R.drawable.four_two;
                resAvatar[1] = R.drawable.four_three;
                resAvatar[2] = R.drawable.four_four;
                Log.d("checkLog", "inside setAvatarAnimation four");
            } else if (imageName.contains("5")) {
                ad = (AnimationDrawable) getResources().getDrawable(
                        R.drawable.five);
                resAvatar[3] = R.drawable.four_one;
                resAvatar[0] = R.drawable.five_two;
                resAvatar[1] = R.drawable.five_three;
                resAvatar[2] = R.drawable.five_four;
                Log.d("checkLog", "inside setAvatarAnimation five");
            } else if (imageName.contains("6")) {
                ad = (AnimationDrawable) getResources().getDrawable(
                        R.drawable.six);
                resAvatar[3] = R.drawable.six_one;
                resAvatar[0] = R.drawable.six_two;
                resAvatar[1] = R.drawable.six_three;
                resAvatar[2] = R.drawable.six_four;
                Log.d("checkLog", "inside setAvatarAnimation six");
            } else {
                ad = (AnimationDrawable) getResources().getDrawable(
                        R.drawable.one);
                Log.d("checkLog", "inside setAvatarAnimation else");
                resAvatar[3] = R.drawable.six_one;
                resAvatar[0] = R.drawable.one_two;
                resAvatar[1] = R.drawable.one_three;
                resAvatar[2] = R.drawable.one_four;
            }
            imageCounter = 0;
            setTimer(millis, interval);
            Log.d("checkLog", "inside setAvatarAnimation start ");
        }
        //For stop animation you can use ad.stop();
    }

    CountDownTimer countDownTimer;
    private int imageCounter = 0;

    public void setTimer(long millis, long interval) {
        countDownTimer = new CountDownTimer(millis, interval) {

            public void onTick(long millisUntilFinished) {
                // activityRecordingBinding.playPauseButton.setText(" " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                Log.d("checkLogAv", "inside onTick " + imageCounter);
                if (imageCounter < resAvatar.length) {
                    girlImage.setImageResource(resAvatar[imageCounter]);
                } else {
                    imageCounter = 0;
                    girlImage.setImageResource(resAvatar[3]);
                }
                ++imageCounter;
            }

            public void onFinish() {
                // activityRecordingBinding.playPauseButton.setText(" ");
                //txtTimer.setText(minutes + ":" + seconds);
                imageCounter = 0;
                //  girlImage.setImageResource(resAvatar[resAvatar.length-1]);
//                Glide.with(MainActivity.this).
//                        load(sessionManager.getBlobId())
//                        .apply(new RequestOptions().placeholder(resAvatar[resAvatar.length-1]).error(resAvatar[resAvatar.length-1]))
//                        .into(girlImage);
            }

        }.start();
    }

    public String getDeviceName() {
        String deviceName = Build.MODEL;
        return deviceName;
    }

    //
//    public String getPackageName(){
//        String packageName=getPackageName();
//        return packageName;
//    }
    public String getApkVersion() {
        int versionName = BuildConfig.VERSION_CODE;
        return String.valueOf(versionName);
    }

    public String getDeviceId() {
        String android_id = "";
        try {
            android_id = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Log.d("checkReg", "inside oncreate android id is  " + android_id);
        } catch (SecurityException e) {

        }
        return android_id;
    }

    String fieldName = "na";

    public String getOSName() {
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
        }

        return fieldName;

    }

    public String getPlatformInfo() {
        String platform = "android";
        return platform;
    }

    private Map<String, String> getHeader() {
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("action", "setWrapper");
            paramObject.put("st", "true");
            paramObject.put("version", Build.VERSION.RELEASE);
            paramObject.put("appVersion", BuildConfig.VERSION_NAME);
            // paramObject.put("deviceID", deviceFingerPrint);
            paramObject.put("appID", getPackageName());

            paramObject.put("device_name", getDeviceName());
            paramObject.put("device_id", getDeviceId());
            // paramObject.put("imei", sessionManager.getLanguage());
            paramObject.put("os", getPlatformInfo());
            paramObject.put("os_name", getOSName());

            paramObject.put("os_type", "Android");
            paramObject.put("appType", "Native");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("wrapper", paramObject.toString());
        return extraHeaders;
    }


    private void createManualDynamicLinkGame(String referralCode,String gameId) {
        String link = "https://app.littleleap.co.in/?invitedby=" + referralCode;
        String sharelinkText="https://littleleap.page.link/?"+"link="+link+"&gameId="+gameId;
        Uri buildUri=FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(sharelinkText))
                .setDomainUriPrefix("https://littleleap.page.link/")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.littleleap.android")
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle("Timer Game")
                                .setDescription("Timer game invitation link.")
                                .setImageUrl(Uri.parse("https://littleleap-prod.s3.ap-south-1.amazonaws.com/app/icon.jpeg"))
                                .build())
                .buildDynamicLink().getUri();
             //  inviteGame(buildUri.toString());
               callRef(buildUri.toString());
    }

    private void createManualDynamicLink(String referralCode) {
        String link = "https://app.littleleap.co.in/?invitedby=" + referralCode;
        String sharelinkText="https://littleleap.page.link/?"+"link="+link;
        Uri buildUri=FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(sharelinkText))
                .setDomainUriPrefix("https://littleleap.page.link/")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.littleleap.android")
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle("App Registration")
                                .setDescription("Referral and joining link.")
                                .setImageUrl(Uri.parse("https://littleleap-prod.s3.ap-south-1.amazonaws.com/app/icon.jpeg"))
                                .build())
                .buildDynamicLink().getUri();
        //  inviteGame(buildUri.toString());
         callRefOne(buildUri.toString());
        //inviteGame(buildUri.toString());
    }


    private void createDynamicLink(String referralCode) {
        String link = "https://app.littleleap.co.in/?invitedby=" + referralCode;
        Uri buildUri=FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://littleleap.page.link/V9Hh")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("")
                                .build())
                .buildDynamicLink().getUri();
              //  inviteGame(buildUri.toString());
                callRef(buildUri.toString());
             //   inviteGame(buildUri.toString());
    }

    private void createShortDynamicLink(String referralCode) {
        Log.d("checkLog","inside createShortDynamicLink Listener "+referralCode);
        String link = "https://app.littleleap.co.in/?invitedby=" + referralCode;
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://littleleap.page.link/V9Hh")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("")
                                .build())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        Log.d("checkLog","inside onComplete Listener "+task.toString());
                        if (task.isSuccessful()) {
                            Log.d("checkLog","inside onComplete Listener "+task.isSuccessful());
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            callRef(shortLink.toString());
                            inviteGame(shortLink.toString());
                            Uri flowchartLink = task.getResult().getPreviewLink();
                        } else {
                            Log.d("checkLog","inside onComplete Listener "+task.isSuccessful());
                            // Error
                            // ...
                        }
                    }
                });
               // .buildDynamicLink().getUri();
        //  inviteGame(buildUri.toString());
    }

    private void inviteGame(String mInvitationUrl) {
        String referrerName = sessionManager.getGender();
        String subject = "Little Leap Timer Game "+ referrerName;
        String invitationLink = mInvitationUrl;
        String msg = "Get excited! Your friend "+referrerName+" is inviting you to join the Little Leap Timer Game.\nWin amazing exciting prizes while having fun with your friends!\n\nClick on the button below or join here:"
                + invitationLink;
        String msgHtml = String.format("<p>Let's play MyExampleGame together! Use my "
                + "<a href=\"%s\">referrer link</a>!</p>", invitationLink);

        Intent intent = new Intent(Intent.ACTION_SEND);
      //  intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, msg);

       // intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        //intent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
       // startActivity(intent);
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_app)));
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
    }

    public void createlink() {
        Log.e("main", "create link ");
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://app.littleleap.co.in"))
                .setDynamicLinkDomain("littleleap.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                //.setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();
//click -- link -- google play store -- inistalled/ or not  ----
        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.e("main", "  Long refer " + dynamicLink.getUri());
        inviteGame(dynamicLinkUri.toString());
        //   https://referearnpro.page.link?apn=blueappsoftware.referearnpro&link=https%3A%2F%2Fwww.blueappsoftware.com%2F
        // apn  ibi link
        // manuall link
//        String sharelinktext  = "https://referearnpro.page.link/?"+
//                "link=http://www.blueappsoftware.com/"+
//                "&apn="+ getPackageName()+
//                "&st="+"My Refer Link"+
//                "&sd="+"Reward Coins 20"+
//                "&si="+"https://www.blueappsoftware.com/logo-1.png";
//        // shorten the link
//        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                //.setLongLink(dynamicLink.getUri())
//                .setLongLink(Uri.parse(sharelinktext))  // manually
//                .buildShortDynamicLink()
//                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
//                    @Override
//                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
//                        if (task.isSuccessful()) {
//                            // Short link created
//                            Uri shortLink = task.getResult().getShortLink();
//                            Uri flowchartLink = task.getResult().getPreviewLink();
//                            Log.e("main ", "short link "+ shortLink.toString());
//                            // share app dialog
//                            Intent intent = new Intent();
//                            intent.setAction(Intent.ACTION_SEND);
//                            intent.putExtra(Intent.EXTRA_TEXT,  shortLink.toString());
//                            intent.setType("text/plain");
//                            startActivity(intent);
//                        } else {
//                            // Error
//                            // ...
//                            Log.e("main", " error "+task.getException() );
//                        }
//                    }
//                });

    }


}