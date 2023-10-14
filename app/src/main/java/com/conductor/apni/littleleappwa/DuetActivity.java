package com.conductor.apni.littleleappwa;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.conductor.apni.littleleappwa.adapter.DuetAdapter;
import com.conductor.apni.littleleappwa.adapter.NotificationAdapter;
import com.conductor.apni.littleleappwa.data.DuetItemPojo;
import com.conductor.apni.littleleappwa.data.QItemPojo;
import com.conductor.apni.littleleappwa.data.StoryItemPojo;
import com.conductor.apni.littleleappwa.data.StoryPojo;
import com.conductor.apni.littleleappwa.databinding.ActivityFaqBinding;
import com.conductor.apni.littleleappwa.dbmanager.DataManager;
import com.conductor.apni.littleleappwa.dbmanager.StoryItemManager;
import com.conductor.apni.littleleappwa.dbmanager.StoryManager;
import com.conductor.apni.littleleappwa.services.Cons;
import com.conductor.apni.littleleappwa.services.VolleyManager;
import com.conductor.apni.littleleappwa.services.callback.CallBack;
import com.conductor.apni.littleleappwa.utils.DeviceInfoUtils;
import com.conductor.apni.littleleappwa.utils.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.Utils;
import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.maxwell.speechrecognition.OnSpeechRecognitionListener;
import com.maxwell.speechrecognition.OnSpeechRecognitionPermissionListener;
import com.maxwell.speechrecognition.SpeechRecognition;

import net.gotev.speech.Speech;
import net.gotev.speech.TextToSpeechCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class DuetActivity extends AppCompatActivity {

    ActivityFaqBinding activityRecordingBinding;
    private SessionManager sessionManager;
    private final int RecordAudioRequestCode = 101;
    private final int WRITERequestCode = 102;
    protected AudioManager mAudioManager;
    private int startIndex = 0, gameScore = 0;
    private boolean isGame = false;
    private VolleyManager volleyManager;
    ProgressDialog mProgressDialog;
    SpeechRecognition speechRecognition;
    private boolean ispermissionGranted = false;
    private StoryManager storyManager;
    private StoryItemManager storyItemManager;
    private List<DuetItemPojo> storyItemPojoList;
    private String gameId="";
    private int unit_id=-1,chapter_id=-1;
    int height, width;
    private StringBuilder latestResult, lastResultOne;
    public static boolean isPlay;

    private MediaPlayer mPlayerOne;

    @Override
    public void onBackPressed() {
        stopListening();
        showExitDialogue();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = SessionManager.getSessionmanager(this);
        activityRecordingBinding = ActivityFaqBinding.inflate(getLayoutInflater());
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setContentView(activityRecordingBinding.getRoot());
        volleyManager = new VolleyManager();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);

        mPlayerOne = MediaPlayer.create(DuetActivity.this, R.raw.small_applause);
        mPlayer = MediaPlayer.create(DuetActivity.this, R.raw.tin_surprise);

        startIndex = Integer.parseInt(getIntent().getStringExtra("index"));

        isGame = getIntent().getBooleanExtra("Game", false);
        gameId = getIntent().getStringExtra("gameId");
        Log.d("checkLog","gameId is "+gameId);
        unit_id = getIntent().getIntExtra("unit_id", -1);
        chapter_id = getIntent().getIntExtra("chapter_id", -1);
        Log.d("checkLog", "unit_id is " + unit_id + " chapter_id is " + chapter_id);
        isGame = getIntent().getBooleanExtra("Game", false);
        gameScore = getIntent().getIntExtra("score", 0);
        if (isGame) {
            initialPoints = gameScore;
            activityRecordingBinding.timerText.setVisibility(View.VISIBLE);
            activityRecordingBinding.scoreText.setText("" + gameScore);
            long endTimeInMillis = MainActivity.endTimeMillis;//getIntent().getLongExtra("timemillis",0);
            long currentTimeInMillis = java.lang.System.currentTimeMillis();
            // long endTimeInMillis=currentTimeInMillis+(10*60*1000);
            long savedTimeInMillis = endTimeInMillis - currentTimeInMillis;
            Log.d("checkLog", " savedTimeInMillis " + savedTimeInMillis);
            Log.d("checkLog", " currentTimeInMillis " + currentTimeInMillis);
            Log.d("checkLog", " endTimeInMillis " + endTimeInMillis);
            setTimer(savedTimeInMillis);

            activityRecordingBinding.scoreText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("isScore", true);
                    intent.putExtra("type", 3);
                    int index = startIndex + counter;
                    intent.putExtra("index", index);
                    intent.putExtra("activityId", sessionManager.getActivityId());
                    setResult(RESULT_OK, intent);
                    DuetActivity.this.finish();
                }
            });


        } else {
            activityRecordingBinding.timerText.setVisibility(View.GONE);
//            if (isNetworkAvailable(this)) {
//                getScoredata(true);
//            } else {
//                Toast.makeText(this, "Network not available!!", Toast.LENGTH_SHORT).show();
//            }
        }

        if (isNetworkAvailable(this)) {
            getDuetdata();
        } else {
            Toast.makeText(this, "Network not available!!", Toast.LENGTH_SHORT).show();
        }

        Log.d("checkLog", " startIndex is " + startIndex);

        Speech.init(DuetActivity.this, getPackageName());

        storyManager = DataManager.init(this).getStorymanager();
        storyItemManager = DataManager.init(this).getStoryItemmanager();

        activityRecordingBinding.menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopListening();
                showExitDialogue();
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        activityRecordingBinding.playPauseButton.setVisibility(View.GONE);
        activityRecordingBinding.playPauseButton.setTag(0);
        activityRecordingBinding.playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (Integer) view.getTag();
                if (tag == 0) {
                    activityRecordingBinding.playPauseButton.setTag(1);
                    speakStop("Stopped Listening");
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.play, 0, 0, 0);
                } else {
                    activityRecordingBinding.playPauseButton.setTag(0);
                    speakStartOne("Start Listening");
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause, 0, 0, 0);
                }
            }
        });

        activityRecordingBinding.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = ((Button) view).getText().toString();
                if (text.equalsIgnoreCase("Tap Here to Start Speaking")) {
                    if (isAudioPermission()) {
                        activityRecordingBinding.startBtn.setVisibility(View.GONE);
                        activityRecordingBinding.playPauseButton.setVisibility(View.GONE);
                        ((Button) view).setText("Stop");
                        //offBeatSound();
                        // Toast.makeText(HomeActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (counter <= 1 && (tempArray != null && tempArray.size() > 0)) {
                                    DuetItemPojo storyItemPojo = tempArray.get(startIndex + counter);
                                    String storyText = storyItemPojo.getText();
                                    if (!storyText.isEmpty()) {
                                        String[] mArray = storyText.split(":");
                                        if (mArray.length > 1) {
                                            String cWord = mArray[1].replaceAll("[^\\w]", " ");
                                            cWord = cWord.replaceAll("[^a-zA-Z0-9]", " ");
                                            speakOne(cWord);
                                        } else {
                                            String cWord = storyText.replaceAll("[^\\w]", " ");
                                            cWord = cWord.replaceAll("[^a-zA-Z0-9]", " ");
                                            speakOne(cWord);
                                        }
                                    }
                                } else {
                                    String msg = "Let's start again";
                                    speakRe(msg);
                                }
                            }
                        }, 100);
                        //SpeechRecognition speechRecognition = new SpeechRecognition(this);
                        //initdroid();
                        initSpeechLib();
                        ispermissionGranted = true;
                    } else {
                        checkPermission();
                    }
                } else {
                    ((Button) view).setText("Tap Here to Start Speaking");
                    //onBeatSound();
                    stopListening();
                    speak("Reading stopped!!", null);
                    Toast.makeText(DuetActivity.this, "Stopped successfully!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Glide.with(this).
                load(sessionManager.getBlobId())
                .apply(new RequestOptions().placeholder(R.mipmap.girl_image).error(R.mipmap.girl_image))
                .into(activityRecordingBinding.girlImage);
        activityRecordingBinding.girlImage.setVisibility(View.VISIBLE);

        setTouchListener();


    }

    CountDownTimer cTimer;

    public void setTimer(long millis) {
        cTimer = new CountDownTimer(millis, 1000) {

            public void onTick(long millisUntilFinished) {
                // activityRecordingBinding.playPauseButton.setText(" " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                //  Log.d("checkLogTimer","inside  onTick "+millisUntilFinished);
                if (millisUntilFinished > 1000) {
                    String text = String.format(Locale.getDefault(), "%02d : %02d ", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                    activityRecordingBinding.timerText.setText(text);
                }
            }

            public void onFinish() {
                try {
                    activityRecordingBinding.timerText.setText("00:00");
                    Log.d("checkLogTimer", "inside onfinish of cTimer");
                    String score = activityRecordingBinding.scoreText.getText().toString();
                    showGameOverDialogue(score);
                } catch (Exception e) {
                    setResult(RESULT_OK);
                    DuetActivity.this.finish();
                }
                // activityRecordingBinding.playPauseButton.setText(" ");
                //txtTimer.setText(minutes + ":" + seconds);
            }

        };
        cTimer.start();
    }

    public void showGameOverDialogue(String score) {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_game_over, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);
        ImageView imageView = dialogView.findViewById(R.id.backImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                DuetActivity.this.finish();
            }
        });
        TextView hintText = dialogView.findViewById(R.id.hintText);
        hintText.setText("Your score is " + score);
        AppCompatButton continueButton = dialogView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                DuetActivity.this.finish();
            }
        });
        dialog.show();
    }


    private void setTouchListener() {
        activityRecordingBinding.girlImage.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) activityRecordingBinding.girlImage.getLayoutParams();
                Log.d("checkLog", "params are leftMargin " + params.leftMargin + " rightMargin " + params.rightMargin + " topMargin " + v.getTop() + " bottomMargin " + params.bottomMargin);
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
                        params.bottomMargin = height - (params.topMargin + activityRecordingBinding.girlImage.getHeight());//initialY - (int) (event.getRawY() + initialTouchY);
                        Log.d("checkLog", "params after are leftMargin " + params.leftMargin + " rightMargin " + params.rightMargin + " topMargin " + params.topMargin + " bottomMargin " + params.bottomMargin);
                        activityRecordingBinding.girlImage.setLayoutParams(params);
                        //  mWindowManager.updateViewLayout(activityHomeBinding.girlImage, params);
                        return true;
                }
                //activityHomeBinding.parent.invalidate();
                return false;
            }
        });
    }

//    private void askPermission() {
//        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                Uri.parse("package:" + getPackageName()));
//        overlayResultLauncher.launch(intent);
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(DuetActivity.this)) {
////            Glide.with(this).
////                    load(sessionManager.getBlobId())
////                    .apply(new RequestOptions().placeholder(R.mipmap.girl_image).error(R.mipmap.girl_image))
////                    .into(activityRecordingBinding.girlImage);
////            activityRecordingBinding.girlImage.setVisibility(View.VISIBLE);
//                    askPermission();
//                }else{
//                    startService(new Intent(DuetActivity.this, FloatingViewService.class));
//                }
//            }
//        },2000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //stopService(new Intent(DuetActivity.this, FloatingViewService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    ActivityResultLauncher<Intent> overlayResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(DuetActivity.this)) {
                            //askPermission();
                        } else {
                            startService(new Intent(DuetActivity.this, FloatingViewService.class));
                        }
                    }
                }
            });

    public void showExitDialogue() {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_exit, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);
        ImageView imageView = dialogView.findViewById(R.id.backImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //TextView hintText = dialogView.findViewById(R.id.hintText);
        // hintText.setText(hint);
        AppCompatButton continueButton = dialogView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startListening();
            }
        });

        AppCompatButton exitButton = dialogView.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                stopListening();
                setResult(RESULT_CANCELED);
                DuetActivity.this.finish();

            }
        });
        dialog.show();
    }


    public void stopRecording(View view) {
        stopListening();
        isPlay = false;
        if (view != null) {
            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.play, 0, 0, 0);
        }
        Log.d("checkLog", "story size is " + storyItemPojoList.size() + " counter is " + counter);
    }

    public void startRecording(View view) {
        if (isAudioPermission()) {
            //   Toast.makeText(DuetActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isPlay = true;
//                    String welcomeMsg = "Awesome let's do another.";
//                    speak(welcomeMsg, null);
                    if (view != null) {
                        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause, 0, 0, 0);
                    }
                }
            }, 200);
            //SpeechRecognition speechRecognition = new SpeechRecognition(this);
            //initdroid();
            initSpeechLib();
            ispermissionGranted = true;
        } else {
            checkPermission();
        }
    }

    private void nextElement() {
        ++counter;
        if (counter < storyItemPojoList.size() - 1) {
            isPlay = false;
            latestResult = new StringBuilder();
            lastResultOne = new StringBuilder();
            currentResult = new StringBuilder();
            // DuetItemPojo storyItemPojoOne=tempArray.get(j);
            //  storyItemPojoOne.setText(lastResultOne.toString());
            activityRecordingBinding.startBtn.setVisibility(View.INVISIBLE);
            DuetItemPojo storyItemPojo = storyItemPojoList.get(counter);
            tempArray.add(storyItemPojo);
            int indexOne = startIndex + counter;
            setProgressOne(indexOne, startIndex + storyItemPojoList.size());
            duetAdapter.setSeriesArrayList(tempArray);
            duetAdapter.notifyDataSetChanged();
            String storyText = storyItemPojo.getText();
            if (!storyText.isEmpty()) {
                String[] mArray = storyText.split(":");
                if (mArray.length > 1) {
                    String cWord = mArray[1].replaceAll("[^\\w]", " ");
                    cWord = cWord.replaceAll("[^a-zA-Z0-9]", " ");
                    speakOne(cWord);
                } else {
                    String cWord = storyText.replaceAll("[^\\w]", " ");
                    cWord = cWord.replaceAll("[^a-zA-Z0-9]", " ");
                    speakOne(cWord);
                }
            }
            activityRecordingBinding.mrecyclerview.smoothScrollToPosition(duetAdapter.getItemCount() - 1);
        } else {
            //okDialogueDone("All records submitted successfully,Thanks!!");
            updateActivityStatus();
            activityRecordingBinding.startBtn.setVisibility(View.INVISIBLE);
            setProgressOne(tempArray.size(), startIndex + storyItemPojoList.size());
            duetAdapter.notifyDataSetChanged();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isGame) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent=new Intent();
                                intent.putExtra("isCompleted",true);
                                intent.putExtra("type",3);
                                setResult(RESULT_CANCELED,intent);
                                DuetActivity.this.finish();
                            }
                        }, 3000);
                        String msg = "Well done, Try next activity quickly,time is running out.";
                        speak(msg, null);
                    } else {
                        if (!isRetry) {
                            int gotScore = points - initialPoints;
                            if (gotScore > 0) {
                                showStarDialogue(gotScore);
                            } else {
                                setResult(RESULT_OK);
                                DuetActivity.this.finish();
                            }
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setResult(RESULT_OK);
                                    DuetActivity.this.finish();
                                }
                            }, 3000);
                        }
                        Random r = new Random();
                        String msg = Cons.applaud_lines[r.nextInt(Cons.applaud_lines.length)];
                        speak(msg, null);
                    }
                }
            }, 100);
            //  Toast.makeText(DuetActivity.this, "All records submitted successfully,Thanks!!", Toast.LENGTH_SHORT).show();
        }
    }

    void okDialogueDone(final String msg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        DuetActivity.this.finish();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void speak(String msg, View view) {
//        Speech.getInstance().setTextToSpeechRate(Cons.speed);
//        Locale locale = new Locale("hi_IN");
//        Speech.getInstance().setLocale(locale);
        Speech.getInstance().say(msg, new TextToSpeechCallback() {
            @Override
            public void onStart() {
                Log.i("speech", "speech started");
                setAvatarAnimation(4000, 1000);
                // onBeatSound();
                //Toast.makeText(HomeActivity.this, "Start..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted() {
                Log.i("speech", "speech completed");
                if (view != null) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.play, 0, 0, 0);
                } else {
                }

            }

            @Override
            public void onError() {
                Log.i("speech", "speech error");
                //    Toast.makeText(DuetActivity.this, "Error..", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RecordAudioRequestCode:
                if (isAudioPermission()) {
                    // initSpeechLib();
                    //Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    checkPermission();
                }
                break;
        }
    }

    private StringBuilder currentResult;

    private void initSpeechLib() {
        speechRecognition = new SpeechRecognition(this);
        // speechRecognition.useGoogleImeRecognition(true,"Hey, Speak");
        //speechRecognition.setPreferredLanguage();
        speechRecognition.setSpeechRecognitionPermissionListener(new OnSpeechRecognitionPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Log.d("speechLog", " onPermissionGranted ");
                ispermissionGranted = true;

            }

            @Override
            public void onPermissionDenied() {
                Log.d("speechLog", " onPermissionCancelled ");
                ispermissionGranted = false;
                checkPermission();
            }
        });
        speechRecognition.setSpeechRecognitionListener(new OnSpeechRecognitionListener() {
            @Override
            public void OnSpeechRecognitionStarted() {
                Log.d("speechLog", " OnSpeechRecognitionStarted ");
            }

            @Override
            public void OnSpeechRecognitionStopped() {
                Log.d("speechLog", " OnSpeechRecognitionStopped ");
            }

            @Override
            public void OnSpeechRecognitionFinalResult(String result) {
                Log.d("speechLog", " OnSpeechRecognitionFinalResult " + result);
                if (result != null && result.length() > 0) {
                    lastResultOne.append(" " + result);
                    initAnimation(activityRecordingBinding.startImage.getX() / 6, 400, activityRecordingBinding.startImage.getY() / 4, -1000);
                    DuetItemPojo duetItemPojo = storyItemPojoList.get(counter);
                    String[] mArray = null;
                    String storyText = duetItemPojo.getText();
                    if (checkResult(lastResultOne.toString(), storyText) || checkResult(result, storyText)) {
                        // duetItemPojo.setText(result);
                        if (duetItemPojo.getUser_type() == 2) {
                            View itemView = llm.findViewByPosition(duetAdapter.getItemCount() - 1);
                            if (itemView != null) {
                                //lastResultOne.append(" "+result);
                                ConstraintLayout constraintLayout = (ConstraintLayout) ((LinearLayout) itemView).getChildAt(0);
                                TextView textView = (TextView) constraintLayout.getChildAt(0);
                                //textView.setText(lastResultOne);
                                int length = duetItemPojo.getText().length();
                                setColorWordInProgress(textView, 0, length);
                            }
                        }else{
                            View itemView = llm.findViewByPosition(duetAdapter.getItemCount() - 1);
                            if (itemView != null) {
                                //lastResultOne.append(" "+result);
                                ConstraintLayout constraintLayout = (ConstraintLayout) ((LinearLayout) itemView).getChildAt(1);
                                TextView textView = (TextView) constraintLayout.getChildAt(0);
                                //textView.setText(lastResultOne);
                                int length = duetItemPojo.getText().length();
                                setColorWordInProgress(textView, 0, length);
                            }
                        }
                        if (isGame) {
                            updateScoreGame(duetItemPojo.getText().length(), lastResultOne.length());
                        } else {
                            updateScore(duetItemPojo.getText().length(), lastResultOne.length());
                        }
                    } else {
                        Log.i("speech", "result: " + lastResultOne);
                        if (duetItemPojo.getUser_type() == 2) {
                            View itemView = llm.findViewByPosition(duetAdapter.getItemCount() - 1);
                            if (itemView != null) {
                                //lastResultOne.append(" "+result);
                                ConstraintLayout constraintLayout = (ConstraintLayout) ((LinearLayout) itemView).getChildAt(0);
                                TextView textView = (TextView) constraintLayout.getChildAt(0);
                                // textView.setText(lastResultOne);
                                // setColorWord(textView, 2, 15);
                              //  String mresult = removeDublicate(result);
                                Log.d("checkLogK", "inside OnSpeechRecognitionCurrentResult lastResultOne " + lastResultOne);
                                // validateSpeech(mresult, true);
                               // currentResult.append(mresult);
                                storyText= textView.getText().toString();
                                if (!storyText.isEmpty()) {
                                    mArray = storyText.split(":");
                                    if (mArray.length > 1) {
                                        storyText = mArray[1];
                                    }
                                    int length = lastResultOne.length();
                                    int flength = length > storyText.length() ? storyText.length() : length;
                                    setColorWordInProgress(textView, mArray[0].length(), mArray[0].length() + flength);
                                }
                            }
                        }else{
                            View itemView = llm.findViewByPosition(duetAdapter.getItemCount() - 1);
                            if (itemView != null) {
                                //lastResultOne.append(" "+result);
                                ConstraintLayout constraintLayout = (ConstraintLayout) ((LinearLayout) itemView).getChildAt(1);
                                TextView textView = (TextView) constraintLayout.getChildAt(0);
                                // textView.setText(lastResultOne);
                                // setColorWord(textView, 2, 15);
                                //String mresult = removeDublicate(result);
                                Log.d("checkLogK", "inside OnSpeechRecognitionCurrentResult lastResultOne " + lastResultOne);
                                // validateSpeech(mresult, true);
                               // currentResult.append(mresult);
                                storyText= textView.getText().toString();
                                if (!storyText.isEmpty()) {
                                    mArray = storyText.split(":");
                                    if (mArray.length > 1) {
                                        storyText = mArray[1];
                                    }
                                    int length = lastResultOne.length();
                                    int flength = length > storyText.length() ? storyText.length() : length;
                                    setColorWordInProgress(textView, mArray[0].length(), mArray[0].length() + flength);
                                }
                            }
                        }
                        startListening();
                    }
                }
            }

            @Override
            public void OnSpeechRecognitionCurrentResult(String result) {
                Log.d("speechLog", " OnSpeechRecognitionCurrentResult " + result);

                // Log.d("checkLogK", " OnSpeechRecognitionCurrentResult " + result);
                String mresult = removeDublicate(result);
                Log.d("checkLogK", "inside OnSpeechRecognitionCurrentResult mResult " + mresult);
                // validateSpeech(mresult, true);
                currentResult.append(" "+mresult);
                Log.d("checkLogK", "inside OnSpeechRecognitionCurrentResult currentResult " + currentResult);
                DuetItemPojo duetItemPojo = storyItemPojoList.get(counter);
                if (duetItemPojo.getUser_type() == 2) {
                    Log.d("checkLogK", "inside if of OnSpeechRecognitionCurrentResult currentResult count is "+currentResult.length());
                    View itemView = llm.findViewByPosition(duetAdapter.getItemCount()-1);
                    if (itemView != null) {
                        //lastResultOne.append(" "+result);
                        ConstraintLayout constraintLayout = (ConstraintLayout) ((LinearLayout) itemView).getChildAt(0);
                        TextView textView = (TextView) constraintLayout.getChildAt(0);
                        //textView.setText(lastResultOne);
                        String[] mArray = null;
                        String storyText = textView.getText().toString();//duetItemPojo.getText();
                        if (!storyText.isEmpty()) {
                            mArray = storyText.split(":");
                            if (mArray.length > 1) {
                                storyText = mArray[1];
                            }
                            int length =currentResult.length();
                            int flength = length > storyText.length() ? storyText.length() : length;
                            Log.d("checkLog","flength is "+flength);
                            setColorWordInProgress(textView, mArray[0].length(), mArray[0].length() + flength);
                        }
                    }else {
                        Log.d("checkLog", "inside else of OnSpeechRecognitionCurrentResult");
                    }

                }else{
                    Log.d("checkLogK", "inside else of OnSpeechRecognitionCurrentResult currentResult count is "+currentResult.length());
                    View itemView = llm.findViewByPosition(duetAdapter.getItemCount()-1);
                    if (itemView != null) {
                        //lastResultOne.append(" "+result);
                        ConstraintLayout constraintLayout = (ConstraintLayout) ((LinearLayout) itemView).getChildAt(1);
                        TextView textView = (TextView) constraintLayout.getChildAt(0);
                        //textView.setText(lastResultOne);
                        String[] mArray = null;
                        String storyText = textView.getText().toString();//duetItemPojo.getText();
                        if (!storyText.isEmpty()) {
                            mArray = storyText.split(":");
                            if (mArray.length > 1) {
                                storyText = mArray[1];
                            }
                            int length =currentResult.length();
                            int flength = length > storyText.length() ? storyText.length() : length;
                            Log.d("checkLog","flength is "+flength);
                            setColorWordInProgress(textView, mArray[0].length(), mArray[0].length() + flength);
                        }
                    }else {
                        Log.d("checkLog", "inside else of OnSpeechRecognitionCurrentResult");
                    }
                }
            }

            @Override
            public void OnSpeechRecognitionError(int i, String s) {
                Log.d("speechLog", " OnSpeechRecognitionError " + s + " i" + i);
                if (!isFinishing()) {
                    stopListening();
                    startListening();
                }
            }
        });

    }

    private String removeDublicate(String result) {
        String[] cWords = currentResult.toString().split(" ");
        for (int i = 0; i < cWords.length; i++) {
            result = removeWord(result, cWords[i]);
        }
        return result;
    }

    public String removeWord(String string, String word) {
        // Check if the word is present in string
        // If found, remove it using removeAll()
        if (string.contains(word)) {

            // To cover the case
            // if the word is at the
            // beginning of the string
            // or anywhere in the middle
            String tempWord = word + " ";
            string = string.replaceAll(tempWord, " ");

            // To cover the edge case
            // if the word is at the
            // end of the string
            tempWord = " " + word;
            string = string.replaceAll(tempWord, " ");
        }

        // Return the resultant string
        return string;
    }

    public void setColorWord(TextView t, int start, int end) {
        Spannable s = (Spannable) t.getText();
//        int start = first.length();
//        int end = start + next.length();
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.storycolor)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        t.invalidate();
    }

    public void setColorWordInProgress(TextView t, int start, int end) {
        Log.d("checkLogK","inside setColorWordInProgress ");
        try {
            Spannable s = (Spannable) t.getText();
//        int start = first.length();
//        int end = start + next.length();
            if(!(end>t.getText().toString().length())) {
                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.storycolor)), start, end-t.getText().toString().length()*1/8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), end, t.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                t.invalidate();
            }else{
                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.storycolor)), 0, t.getText().toString().length()-t.getText().toString().length()*1/8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                t.invalidate();
            }
        }catch (IndexOutOfBoundsException ex){

        }
    }

    public void setColorWordOne(TextView t, int start, int end) {
        Log.d("checkLog", "inside setColorWordOne ");
        Spannable s = (Spannable) t.getText();
//        int start = first.length();
//        int end = start + next.length();
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.purple_200)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        t.invalidate();
    }

    private boolean checkResult(String result, String answer) {
        Log.d("checkLog", "answer is " + answer);
        Log.d("checkLog", "result is " + result);
        boolean flag = false;
        String[] mArray = answer.split(" ");
        if (answer != null && mArray.length <= 1 && result != null && result.length() > 0) {
            String cWord = answer.replaceAll("[^\\w]", " ");
            cWord = cWord.replaceAll("[^a-zA-Z0-9]", " ");
            Log.d("checkLog", "matchresult is " + cWord);
            Log.d("checkLog", "cSenetence is " + result);
            if (cWord.toLowerCase().contains(result.toLowerCase())) {
                flag = true;
            } else if (result.toLowerCase().contains(cWord.toLowerCase())) {
                flag = true;
            } else {
                flag = false;
            }
        } else {
            String matchResult = answer.substring(answer.length() / 2);
            String cWord = matchResult.replaceAll("[^\\w]", " ");
            cWord = cWord.replaceAll("[^a-zA-Z0-9]", " ");
            Log.d("checkLog", "matchresult is " + cWord);
            String ccWord = result.replaceAll("[^\\w]", " ");
            ccWord = ccWord.replaceAll("[^a-zA-Z0-9]", " ");
            Log.d("checkLog", "cSenetence is " + ccWord);
            if (result != null && !result.isEmpty() && result.length() >= answer.length() * 75 / 100) {
                String[] resultArray = ccWord.split("\\s+");
                for (int i = 0; i < resultArray.length; i++) {
                    if (cWord.toLowerCase().contains(resultArray[i].toLowerCase())) {
                        flag = true;
                        break;
                    }
                }
            } else {
                flag = false;
            }
        }

        return flag;

    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }


    private boolean isAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }


    private StoryPojo storyPojo;
    private boolean isRetry = false;

    public void getDuetdata() {
        mProgressDialog.show();
        tempArray = new ArrayList<>();
        storyItemPojoList = new ArrayList<>();
        String url = Cons.BASE_URL + "duets/" + sessionManager.getActivityId() + "?student_id=" + sessionManager.getStudentId();
        Log.d("checkLog", "inside getStorydata is url " + url);
        volleyManager.makeGetCall(this, url, new CallBack() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                Log.d("checkLogin", "inside on onSuccessResponse is " + response.toString());
                mProgressDialog.dismiss();
                ObjectMapper mapper = new ObjectMapper();
                int retryNum = 0;
                Log.d("checkLogin", "inside on onSuccessJsonArrayResponse is " + response.toString());
                try {
                    JSONObject resultObject = response.getJSONObject("result");
                    storyPojo = mapper.readValue(resultObject.toString(),
                            StoryPojo.class);
                    retryNum = resultObject.getInt("attempts");
//                            storyManager.createOrUpdate(transactions);
                    if (!resultObject.isNull("duets")) {
                        JSONArray storyItemArray = resultObject.getJSONArray("duets");
                        for (int j = 0; j < storyItemArray.length(); j++) {
                            JSONObject jsonObjectOne = storyItemArray.getJSONObject(j);
                            Log.d("checkLog", " jsonObjectOne is " + jsonObjectOne.toString());
                            DuetItemPojo storyItemPojo = mapper.readValue(jsonObjectOne.toString(),
                                    DuetItemPojo.class);

                            if (storyItemPojo.getIs_completed() == 1) {
                                storyItemPojo.setType(0);
                                //  storyItemManager.createOrUpdate(storyItemPojo);
                                tempArray.add(storyItemPojo);
                            } else {
                                storyItemPojo.setType(0);
//                            //  storyItemManager.createOrUpdate(storyItemPojo);
                                if ((j == storyItemArray.length() - 1) && storyItemPojoList.size() <= 0) {
                                    tempArray.add(storyItemPojo);
                                } else {
                                    storyItemPojoList.add(storyItemPojo);
                                }
                            }
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    Log.d("checkLog", "exceptions is " + e.toString());
                    // okDialogue(e.toString());
                    mProgressDialog.dismiss();
                    // Toast.makeText(DuetActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {
                    Log.d("checkLog", "exceptions is " + ex.toString());
                }

                if (storyItemPojoList.size() == 0 && tempArray.size() > 0) {
                    isRetry = true;
                    storyItemPojoList.addAll(tempArray);
                    tempArray.removeAll(tempArray);
                } else {
                    if (retryNum >= 1) {
                        isRetry = true;
                    } else {
                        isRetry = false;
                    }
                }

                Log.d("CheckLogSize", "storyItemPojoList size is " + storyItemPojoList.size());

                Log.d("CheckLogSize", "pastItemPojoList size is " + tempArray.size());

                startIndex = tempArray.size();

                setLinkOne();

                if (!isGame)
                    getScoredata(true);

            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray response) {

            }

            @Override
            public void onVolleyError(VolleyError error) {
                mProgressDialog.dismiss();
                if (!isGame)
                    getScoredata(true);
                // Toast.makeText(DuetActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTokenExpire() {

            }
        });

    }

    int points;
    int initialPoints = 0;

    public void getScoredata(boolean firstTime) {
        //  mProgressDialog.show();
        String url = Cons.BASE_URL + "students/" + sessionManager.getStudentId() + "/points";
        Log.d("checkLog", "inside getStorydata is url " + url);
        volleyManager.makeGetCall(this, url, new CallBack() {

            @Override
            public void onSuccessResponse(JSONObject response) {
                Log.d("checkLogin", "inside on onSuccessResponse is " + response.toString());
                //   mProgressDialog.dismiss();
                ObjectMapper mapper = new ObjectMapper();
                Log.d("checkLogin", "inside on onSuccessJsonArrayResponse is " + response.toString());
                try {
                    int status = response.getInt("status");
                    int statusCode = response.getInt("statusCode");
                    if (statusCode == 200) {
                        JSONObject resultObject = response.getJSONObject("result");
                        points = resultObject.getInt("points");
                        if (firstTime) {
                            initialPoints = points;
                            activityRecordingBinding.scoreText.setText("" + points);
                        }
                    }
                } catch (JSONException e) {
                    // okDialogue(e.toString());
                    Toast.makeText(DuetActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

                if (!firstTime) {
                    chooseAnim();
                }

            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray response) {

            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (!firstTime) {
                    chooseAnim();
                }
                // mProgressDialog.dismiss();
                //  Toast.makeText(HomeActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTokenExpire() {

            }
        });

    }

    private void setProgressOne(int currentIndex, int max) {
        activityRecordingBinding.progressBar.setProgress(currentIndex);
        activityRecordingBinding.progressText.setText("" + currentIndex + "/" + max);
    }

    public int counter;
    private List<DuetItemPojo> tempArray;

    private void setLinkOne() {
        if (storyPojo != null) {
            String image = storyPojo.getImage();
            Glide.with(this).load(image).error(R.mipmap.dummy_image_story).into(activityRecordingBinding.categoryImage);
            activityRecordingBinding.headerText.setText(storyPojo.getName());
            activityRecordingBinding.instructionText.setText(storyPojo.getInstruction());
        }
        if (storyItemPojoList != null && storyItemPojoList.size() > 0) {
            activityRecordingBinding.startBtn.setVisibility(View.VISIBLE);
            latestResult = new StringBuilder();
            lastResultOne = new StringBuilder();
            currentResult = new StringBuilder();
            counter = 0;
            DuetItemPojo storyItemPojo = storyItemPojoList.get(counter);
            //activityRecordingBinding.qText.setText(storyItemPojo.getQuestion());
            tempArray.add(storyItemPojo);
//            DuetItemPojo duetItemPojo=new DuetItemPojo();
//            duetItemPojo.setType(1);
//            duetItemPojo.setText("");
            int indexOne = startIndex + counter;
            activityRecordingBinding.progressBar.setMax(tempArray.size() + storyItemPojoList.size());
            setProgressOne(indexOne, startIndex + storyItemPojoList.size());
            setAdapter(tempArray);

            activityRecordingBinding.mrecyclerview.smoothScrollToPosition(duetAdapter.getItemCount() - 1);
            String msg = "Amazing prizes on offer! Let's win this!";
            speakStart(msg);
        } else {
            activityRecordingBinding.progressBar.setMax(tempArray.size());
            activityRecordingBinding.startBtn.setVisibility(View.INVISIBLE);
            setProgressOne(tempArray.size(), startIndex + storyItemPojoList.size());
            String msg = "You have already completed this activity, please choose another one";
            speak(msg, null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //  Toast.makeText(DuetActivity.this, "You have already completed this activity, please choose another one.!!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    DuetActivity.this.finish();
                }
            }, 4000);
        }
    }

    private void speakStart(String msg) {
        //  Speech.getInstance().setTextToSpeechRate(1.5F);
//        Speech.getInstance().setTextToSpeechRate(Cons.speed);
//        Locale locale = new Locale("hi_IN");
//        Speech.getInstance().setLocale(locale);
        Speech.getInstance().say(msg, new TextToSpeechCallback() {
            @Override
            public void onStart() {
                Log.i("speech", "speech started");
                setAvatarAnimation(4000, 1000);
                // speechRecognition.stopSpeechRecognition();
                // stopListening();
                //Toast.makeText(HomeActivity.this, "Start..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted() {
                Log.i("speech", "speech completed");
                // startListening();
                // gotoNext();

            }

            @Override
            public void onError() {
                Log.i("speech", "speech error");
                Toast.makeText(DuetActivity.this, "Error..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private DuetAdapter duetAdapter;
    private LinearLayoutManager llm;
    public void setAdapter(List<DuetItemPojo> mList) {
        if (mList != null && mList.size() > 0) {
            duetAdapter = new DuetAdapter(this);
            duetAdapter.setSeriesArrayList(mList);
            activityRecordingBinding.mrecyclerview.setAdapter(duetAdapter);
            llm = new LinearLayoutManager(this);
            activityRecordingBinding.mrecyclerview.setLayoutManager(llm);
            activityRecordingBinding.mrecyclerview.setVisibility(View.VISIBLE);
            duetAdapter.notifyDataSetChanged();
        } else {
            activityRecordingBinding.mrecyclerview.setVisibility(View.GONE);
        }

    }


    public void clearStory() {
        List<StoryPojo> supportTypePojoList = storyManager.getAll();
        for (int i = 0; i < supportTypePojoList.size(); i++) {
            storyManager.deleteStory(supportTypePojoList.get(i));
        }
    }

    public void clearStoryType() {
        List<StoryItemPojo> supportSubjectPojoList = storyItemManager.getAll();
        for (int i = 0; i < supportSubjectPojoList.size(); i++) {
            storyItemManager.deleteStoryItem(supportSubjectPojoList.get(i));
        }
    }


    private int[] resLottie = {R.raw.celebration_giraffe,
            R.raw.star_gold_icon_loop, R.raw.sticker_flying, R.raw.party_hat, R.raw.dancing_burger, R.raw.dance_party, R.raw.monkey_celeb};
    private String[] resLottieOne = {"https://assets9.lottiefiles.com/private_files/lf30_rjqwaenm.json"};

    private void chooseAnim() {
        Random r = new Random();
        int d = r.nextInt(100);
        if (d % 2 == 0) {
            int c = r.nextInt(100);
            if (c % 2 == 0) {
                showConfettiCenter();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isRetry) {
                            runAnimation();
                        } else {
                            nextElement();
                        }
                        //showStarDialogue(5);
                    }
                }, 1500);
            } else {
                showConfettiTop();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isRetry) {
                            runAnimation();
                        } else {
                            nextElement();
                        }
                        //showStarDialogue(5);
                    }
                }, 1500);
            }
        } else {
            activityRecordingBinding.animationView.setVisibility(View.VISIBLE);
            activityRecordingBinding.animationView.playAnimation();
            int n = r.nextInt(resLottie.length);
            Log.d("checkLogLottie", "inside else of lottie " + n);
            activityRecordingBinding.animationView.setAnimation(resLottie[n]);
            //activityRecordingBinding.animationView.setAnimationFromUrl(resLottieOne[0]);
            // activityRecordingBinding.animationView.setRepeatMode();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activityRecordingBinding.animationView.pauseAnimation();
                    activityRecordingBinding.animationView.setVisibility(View.GONE);
                    if (!isRetry) {
                        initAnimation(activityRecordingBinding.startImage.getX() / 6, 400, activityRecordingBinding.startImage.getY() / 4, -800);
                        // initAnimation(textArray[counter].getX(),activityHomeBinding.scoreText.getX(),textArray[counter].getY(),activityHomeBinding.scoreText.getY());
                        runAnimation();
                    } else {
                        nextElement();
                    }
                }
            }, 2000);
        }
        playFileOne();


//        activityRecordingBinding.animationView.setVisibility(View.VISIBLE);
//        activityRecordingBinding.animationView.playAnimation();
//        int n = r.nextInt(resLottie.length);
//        Log.d("checkLogLottie","inside else of lottie "+n);
//        //activityRecordingBinding.animationView.setAnimation(resLottie[n]);
//        activityRecordingBinding.animationView.setAnimationFromUrl(resLottieOne[0]);
//        // activityRecordingBinding.animationView.setRepeatMode();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                activityRecordingBinding.animationView.pauseAnimation();
//                activityRecordingBinding.animationView.setVisibility(View.GONE);
//                initAnimation(activityRecordingBinding.startImage.getX() / 6, 400, activityRecordingBinding.startImage.getY() / 4, -800);
//                // initAnimation(textArray[counter].getX(),activityHomeBinding.scoreText.getX(),textArray[counter].getY(),activityHomeBinding.scoreText.getY());
//                runAnimation();
//            }
//        }, 3000);
    }


    public void updateActivityStatus() {
        mProgressDialog.show();
       // DuetItemPojo storyItemPojo = storyItemPojoList.get(counter-2);
        JSONObject scoreObject = new JSONObject();
        //String token= FirebaseInstanceId.getInstance().getToken();
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("student_id", sessionManager.getStudentId());
            paramObject.put("activity_id", sessionManager.getActivityId());
            paramObject.put("chapter_id", chapter_id);
            paramObject.put("unit_id", unit_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String accessToken=getPreferences(MODE_PRIVATE).getString("accesstoken", null);//sessionManager.getToken();
        Log.d("checkLog", "inside updateStory data is " + paramObject.toString());
        Map<String, String> header = new DeviceInfoUtils().getHeaderInfo(sessionManager.getUserId());
        Log.d("checkLog", "inside updateStory header is url " + header.toString());
        String url = Cons.BASE_URL + "tree/update-user-activity";
        Log.d("checkLog", "inside getStorydata is url " + url);
        volleyManager.makePostCall(this, paramObject, url, header, new CallBack() {

            @Override
            public void onSuccessResponse(JSONObject response) {
                Log.d("checkLogin", "inside on onSuccessResponse is " + response.toString());
                mProgressDialog.dismiss();
                try {
                    int statusCode = response.getInt("statusCode");
                    if (statusCode == 200) {

                    } else {
                        //   getScoredata(false);
                    }
                    // Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {

                }

            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray response) {

            }

            @Override
            public void onVolleyError(VolleyError error) {
                mProgressDialog.dismiss();
            }

            @Override
            public void onTokenExpire() {

            }
        });

    }

    public void updateScore(int totalWord, int correctWord) {
        mProgressDialog.show();
        DuetItemPojo storyItemPojo = storyItemPojoList.get(counter);
        JSONObject scoreObject = new JSONObject();
        //String token= FirebaseInstanceId.getInstance().getToken();
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("total_words", totalWord);
            paramObject.put("correct_words", correctWord);
            paramObject.put("student_id", sessionManager.getStudentId());
            paramObject.put("sub_activity_id", storyItemPojo.getId());
            paramObject.put("unit_id",unit_id);
            paramObject.put("chapter_id", chapter_id);
            // paramObject.put("lineId", sessionManager.getStudentId());
            paramObject.put("notes", scoreObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String accessToken=getPreferences(MODE_PRIVATE).getString("accesstoken", null);//sessionManager.getToken();
        Log.d("checkLog", "inside updateStory data is " + paramObject.toString());
        Map<String, String> header = new DeviceInfoUtils().getHeaderInfo(sessionManager.getUserId());
        Log.d("checkLog", "inside updateStory header is url " + header.toString());
        String url = Cons.BASE_URL + "duets/" + sessionManager.getActivityId() + "/record";
        Log.d("checkLog", "inside getStorydata is url " + url);
        volleyManager.makePostCall(this, paramObject, url, header, new CallBack() {

            @Override
            public void onSuccessResponse(JSONObject response) {
                mProgressDialog.dismiss();
                Log.d("checkLogin", "inside on onSuccessResponse is " + response.toString());
                try {
                    int statusCode = response.getInt("statusCode");
                    if (statusCode == 200) {
//                        JSONObject mObject=response.getJSONObject("result");
//                        int score = mObject.getInt("points");
//                        activityRecordingBinding.scoreText.setText("" + score);
//                        chooseAnim();
                    } else {
                        // getScoredata(false);
                    }
                    // Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    getScoredata(false);
                }

                getScoredata(false);

            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray response) {

            }

            @Override
            public void onVolleyError(VolleyError error) {
                mProgressDialog.dismiss();
                getScoredata(false);
            }

            @Override
            public void onTokenExpire() {

            }
        });

    }


    public void updateScoreGame(int totalWord, int correctWord) {
        mProgressDialog.show();
        DuetItemPojo storyItemPojo = storyItemPojoList.get(counter);
        JSONObject scoreObject = new JSONObject();
        //String token= FirebaseInstanceId.getInstance().getToken();
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("total_words", totalWord);
            paramObject.put("correct_words", correctWord);
            paramObject.put("GameCode", sessionManager.getGameIdOne());
            paramObject.put("student_id", sessionManager.getStudentId());
            paramObject.put("sub_activity_id", storyItemPojo.getId());
            paramObject.put("unit_id",unit_id);
            paramObject.put("chapter_id", chapter_id);
            // paramObject.put("lineId", sessionManager.getStudentId());
            paramObject.put("notes", scoreObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String accessToken=getPreferences(MODE_PRIVATE).getString("accesstoken", null);//sessionManager.getToken();
        Log.d("checkLog", "inside updateStory data is " + paramObject.toString());
        Map<String, String> header = new DeviceInfoUtils().getHeaderInfo(sessionManager.getUserId());
        Log.d("checkLog", "inside updateStory header is url " + header.toString());
        String url = Cons.BASE_URL + "timergame/duets/" + sessionManager.getActivityId() + "/record";
        Log.d("checkLog", "inside getStorydata is url " + url);
        volleyManager.makePostCall(this, paramObject, url, header, new CallBack() {

            @Override
            public void onSuccessResponse(JSONObject response) {
                mProgressDialog.dismiss();
                Log.d("checkLogin", "inside on onSuccessResponse is " + response.toString());
                try {
                    int statusCode = response.getInt("statusCode");
                    if (statusCode == 200) {
                        JSONObject mObject = response.getJSONObject("result");
                        int score = mObject.getInt("points");
                        points = score;
                        activityRecordingBinding.scoreText.setText("" + score);
                        chooseAnim();
                    } else {

                    }
                    // Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {

                }

            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray response) {

            }

            @Override
            public void onVolleyError(VolleyError error) {
                mProgressDialog.dismiss();
            }

            @Override
            public void onTokenExpire() {

            }
        });

    }

    ConfettoGenerator confettoGenerator;

    private void initConfietto() {
        final List<Bitmap> allPossibleConfetti = Utils.generateConfettiBitmaps(new int[]{Color.BLUE}, 20 /* size */);
        ;//constructBitmapsForConfetti();
// Alternatively, we provide some helper methods inside `Utils` to generate square, circle,
// and triangle bitmaps.
        final int numConfetti = allPossibleConfetti.size();
        confettoGenerator = new ConfettoGenerator() {
            @Override
            public Confetto generateConfetto(Random random) {
                final Bitmap bitmap = allPossibleConfetti.get(random.nextInt(numConfetti));
                return new BitmapConfetto(bitmap);
            }
        };
    }

    private void showConfetto() {
        final int containerMiddleX = activityRecordingBinding.parent.getWidth() / 2;
        final int containerMiddleY = activityRecordingBinding.parent.getHeight() / 2;
        final ConfettiSource confettiSource = new ConfettiSource(containerMiddleX, containerMiddleY);

        new ConfettiManager(DuetActivity.this, confettoGenerator, confettiSource, activityRecordingBinding.parent)
                .setEmissionDuration(1000)
                .setEmissionRate(100)
                .setVelocityX(20, 10)
                .setVelocityY(100)
                .setRotationalVelocity(180, 180)
                .animate();
    }


    private void showConfettiTop() {
        activityRecordingBinding.viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                .setPosition(-20f, activityRecordingBinding.viewKonfetti.getWidth() + 50f, -20f, -50f)
                .streamFor(200, 2000L);
    }

    private void showConfettiCenter() {
        activityRecordingBinding.viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                .setPosition(activityRecordingBinding.viewKonfetti.getX() + activityRecordingBinding.viewKonfetti.getWidth() / 2, activityRecordingBinding.viewKonfetti.getY() + activityRecordingBinding.viewKonfetti.getHeight() / 3)
                .burst(100);

    }

    public void showStarDialogue(int score) {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_star, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);
        ImageView imageView = dialogView.findViewById(R.id.backImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setResult(RESULT_OK);
                DuetActivity.this.finish();
                //Random r = new Random();
//                String msg = Cons.applaud_lines[r.nextInt(Cons.applaud_lines.length)];
//                speakOne(msg);
                //nextElement();
            }
        });
        TextView hintText = dialogView.findViewById(R.id.hintText);
        // hintText.setText(hint);
        hintText.setText("Congratulations , You have won " + score + " stars");
        RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.simpleRatingBar);
        ratingBar.setRating(5);
        AppCompatButton continueButton = dialogView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                DuetActivity.this.finish();
//                Random r = new Random();
//                String msg = Cons.applaud_lines[r.nextInt(Cons.applaud_lines.length)];
//                speakOne(msg);
//                nextElement();
            }
        });
        dialog.show();
    }

    private void speakOne(String msg) {
//        Speech.getInstance().setTextToSpeechRate(Cons.speed);
//        Locale locale = new Locale("hi_IN");
//        Speech.getInstance().setLocale(locale);
        Speech.getInstance().say(msg, new TextToSpeechCallback() {
            @Override
            public void onStart() {
                stopListening();
                if (msg.length() < 40) {
                    setAvatarAnimation(4000, 1000);
                } else if (msg.length() < 80) {
                    setAvatarAnimation(8000, 1000);
                } else {
                    setAvatarAnimation(12000, 1000);
                }
                // onBeatSound();
                // Log.i("speech", "speech started");
                //Toast.makeText(HomeActivity.this, "Start..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted() {
                int indexTwo = startIndex + counter;
                DuetItemPojo duetItemPojo = tempArray.get(indexTwo);
                if (duetItemPojo.getUser_type() == 1) {
                    View itemView = activityRecordingBinding.mrecyclerview.getChildAt(duetAdapter.getItemCount() - 1);
                    if (itemView != null) {
                        //lastResultOne.append(" "+result);
                        ConstraintLayout constraintLayout = (ConstraintLayout) ((LinearLayout) itemView).getChildAt(1);
                        TextView textView = (TextView) constraintLayout.getChildAt(0);
                        //textView.setText(lastResultOne);
                        int length = duetItemPojo.getText().length();
                        setColorWordOne(textView, 0, length);
                    }
                }
                ++counter;
                if (counter < storyItemPojoList.size()) {
                    tempArray.add(storyItemPojoList.get(counter));
                    duetAdapter.setSeriesArrayList(tempArray);
                    duetAdapter.notifyDataSetChanged();
                    int indexOne = startIndex + counter;
                    setProgressOne(indexOne, startIndex + storyItemPojoList.size());
                    startListening();
                    activityRecordingBinding.mrecyclerview.smoothScrollToPosition(duetAdapter.getItemCount() - 1);
                } else {
                    setProgressOne(tempArray.size(), startIndex + storyItemPojoList.size());
                    // setProgressOne(startIndex + storyItemPojoList.size(), startIndex * 2 + storyItemPojoList.size());
                    if (!isRetry) {
                        int gotScore = points - initialPoints;
                        showStarDialogue(gotScore);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setResult(RESULT_OK);
                                DuetActivity.this.finish();
                            }
                        }, 3000);
                    }
                    Random r = new Random();
                    String msg = Cons.applaud_lines[r.nextInt(Cons.applaud_lines.length)];
                    speak(msg, null);
                }
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
    }

    private void speakRe(String msg) {
//        Speech.getInstance().setTextToSpeechRate(Cons.speed);
//        Locale locale = new Locale("hi_IN");
//        Speech.getInstance().setLocale(locale);
        Speech.getInstance().say(msg, new TextToSpeechCallback() {
            @Override
            public void onStart() {
                stopListening();
                // onBeatSound();
                // Log.i("speech", "speech started");
                //Toast.makeText(HomeActivity.this, "Start..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted() {
                startListening();
            }

            @Override
            public void onError() {
                Log.i("speech", "speech error");
                // Toast.makeText(HomeActivity.this, "Error..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void stopListening() {
        //Speech.getInstance().stopListening();
        try {
            if (speechRecognition != null)
                speechRecognition.stopSpeechRecognition();
        } catch (IllegalStateException ex) {

        }

    }

    private void startListening() {
        Log.d("checkLog", " inside startListening ");
        try {
            if (speechRecognition != null)
                speechRecognition.startSpeechRecognition();
        } catch (IllegalStateException ex) {

        }
    }

    private void playFileOne() {
        try {
            mPlayerOne.start();
        } catch (IllegalStateException ex) {

        }
    }


    public void runAnimation() {
        Log.d("checkLog", "inside runAnimation ");
//        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //   View view = mInflater.inflate(R.layout.star_item, null, false);
        // activityHomeBinding.startImage.setAnimation(animation);
        activityRecordingBinding.startImage.setVisibility(View.VISIBLE);
        activityRecordingBinding.startImage.startAnimation(animation);
        activityRecordingBinding.startImage.invalidate();
    }

    TranslateAnimation animation;

    public void initAnimation(float fromXDelta, float toXDelta, float fromYdelta, float toYDelta) {
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
            activityRecordingBinding.startImage.setVisibility(View.GONE);
            activityRecordingBinding.startImage.clearAnimation();
            activityRecordingBinding.startBtn.setVisibility(View.VISIBLE);
            String text = activityRecordingBinding.scoreText.getText().toString();
            Log.d("checkAnimLog", "inside onAnimationEnd previous score " + text + " current score " + points);
            if (points > 0 && points >= Integer.parseInt(text)) {
                animateTextViewOne(Integer.parseInt(text), points, activityRecordingBinding.scoreText);
            }
            nextElement();
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

    private MediaPlayer mPlayer;

    private void playFile() {
        try {
            mPlayer.start();
        } catch (IllegalStateException ex) {

        }
    }

    public void animateTextViewOne(int initialValue, int finalValue, final TextView textview) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                textview.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();

    }

    public void speakStartOne(String msg) {
        //Speech.getInstance().setAudioStream(0);
//        Speech.getInstance().setTextToSpeechRate(Cons.speed);
//        Locale locale = new Locale("hi_IN");
//        Speech.getInstance().setLocale(locale);
        Speech.getInstance().say(msg, new TextToSpeechCallback() {
            @Override
            public void onStart() {
                // stopListening();
                // onBeatSound();
                // Log.i("speech", "speech started");
                //Toast.makeText(HomeActivity.this, "Start..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted() {
                startListening();
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
    }


    public void speakStop(String msg) {
        //Speech.getInstance().setAudioStream(0);
//        Speech.getInstance().setTextToSpeechRate(Cons.speed);
//        Locale locale = new Locale("hi_IN");
//        Speech.getInstance().setLocale(locale);
        Speech.getInstance().say(msg, new TextToSpeechCallback() {
            @Override
            public void onStart() {
                stopListening();
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
    }


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
                    activityRecordingBinding.girlImage.setImageResource(resAvatar[imageCounter]);
                } else {
                    imageCounter = 0;
                    activityRecordingBinding.girlImage.setImageResource(resAvatar[3]);
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


}