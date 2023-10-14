package com.conductor.apni.littleleappwa;

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
import androidx.recyclerview.widget.RecyclerView;

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
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieDrawable;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.conductor.apni.littleleappwa.adapter.AnswerAdapter;
import com.conductor.apni.littleleappwa.adapter.AnswerAdapterTwo;
import com.conductor.apni.littleleappwa.adapter.DuetAdapter;
import com.conductor.apni.littleleappwa.data.AnswerPojo;
import com.conductor.apni.littleleappwa.data.DuetItemPojo;
import com.conductor.apni.littleleappwa.data.QItemPojo;
import com.conductor.apni.littleleappwa.data.StoryItemPojo;
import com.conductor.apni.littleleappwa.data.StoryPojo;
import com.conductor.apni.littleleappwa.databinding.ActivityRecordingBinding;
import com.conductor.apni.littleleappwa.dbmanager.AnswerManager;
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
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class RecordingActivity extends AppCompatActivity {

    ActivityRecordingBinding activityRecordingBinding;
    private SessionManager sessionManager;
    private final int RecordAudioRequestCode = 101;
    private final int WRITERequestCode = 102;
    protected AudioManager mAudioManager;

    private VolleyManager volleyManager;
    ProgressDialog mProgressDialog;
    SpeechRecognition speechRecognition;
    private boolean ispermissionGranted = false;
    private StoryManager storyManager;
    private StoryItemManager storyItemManager;
    private AnswerManager answerManager;
    private List<QItemPojo> storyItemPojoList, pastItemList;
    private List<String> answerList;
    private int startIndex = 0, gameScore = 0;
    private int unit_id=-1,chapter_id=-1;
    private boolean isGame = false;
    private MediaPlayer mPlayer;
    int height, width;
    private String gameId="";

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
                //   countDownTimer.cancel();
                setResult(RESULT_CANCELED);
                RecordingActivity.this.finish();

            }
        });
        dialog.show();
    }

    private void stopListening() {
        //Speech.getInstance().stopListening();
        try {
            if (speechRecognition != null) {
//                activityRecordingBinding.playPauseButton.setVisibility(View.INVISIBLE);
//                countDownTimer.cancel();
                speechRecognition.stopSpeechRecognition();
//                activityRecordingBinding.playPauseButton.setTag(1);
//                activityRecordingBinding.playPauseButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.play,0,0,0);
            }
        } catch (IllegalStateException e) {

        }
    }

    private void startListening() {
        // Log.d("checkLog", " inside startListening ");
        try {
            if (speechRecognition != null) {
                speechRecognition.startSpeechRecognition();
                //  activityRecordingBinding.playPauseButton.setTag(0);
                //activityRecordingBinding.playPauseButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause,0,0,0);
            }
        } catch (IllegalStateException e) {

        }
    }

    @Override
    public void onBackPressed() {
        stopListening();
        showExitDialogue();
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
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(RecordingActivity.this)) {
////            Glide.with(this).
////                    load(sessionManager.getBlobId())
////                    .apply(new RequestOptions().placeholder(R.mipmap.girl_image).error(R.mipmap.girl_image))
////                    .into(activityRecordingBinding.girlImage);
////            activityRecordingBinding.girlImage.setVisibility(View.VISIBLE);
//                    askPermission();
//                }else{
//                    startService(new Intent(RecordingActivity.this, FloatingViewService.class));
//                }
//            }
//        },2000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //stopService(new Intent(RecordingActivity.this, FloatingViewService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    ActivityResultLauncher<Intent> overlayResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(RecordingActivity.this)) {
                            //askPermission();
                        } else {
                            startService(new Intent(RecordingActivity.this, FloatingViewService.class));
                        }
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = SessionManager.getSessionmanager(this);
        activityRecordingBinding = activityRecordingBinding.inflate(getLayoutInflater());
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setContentView(activityRecordingBinding.getRoot());
        volleyManager = new VolleyManager();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);

        activityRecordingBinding.resultEdit.setMovementMethod(ScrollingMovementMethod.getInstance());

        answerList = new ArrayList<>();

        startIndex = Integer.parseInt(getIntent().getStringExtra("index"));
        gameId = getIntent().getStringExtra("gameId");
        Log.d("checkLog","gameId is "+gameId);
        isGame = getIntent().getBooleanExtra("Game", false);
        gameScore = getIntent().getIntExtra("score", 0);
        unit_id = getIntent().getIntExtra("unit_id", -1);
        chapter_id = getIntent().getIntExtra("chapter_id", -1);
        Log.d("checkLog", "unit_id is " + unit_id + " chapter_id is " + chapter_id);
        Log.d("checkLog", " gameId " + MainActivity.gameId);
        if (isGame) {
            initialPoints = gameScore;
            activityRecordingBinding.timerText.setVisibility(View.VISIBLE);
            activityRecordingBinding.scoreText.setText("" + gameScore);
            long endTimeInMillis = MainActivity.endTimeMillis;//getIntent().getLongExtra("timemillis",0);
            long currentTimeInMillis = java.lang.System.currentTimeMillis();
            // long endTimeInMillis=currentTimeInMillis+(10*60*1000);
            long savedTimeInMillis = endTimeInMillis - currentTimeInMillis;
//            Log.d("checkLog"," savedTimeInMillis "+savedTimeInMillis);
//            Log.d("checkLog"," currentTimeInMillis "+currentTimeInMillis);
//            Log.d("checkLog"," endTimeInMillis "+endTimeInMillis);
            setTimer(savedTimeInMillis);
            activityRecordingBinding.scoreText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.putExtra("isScore",true);
                    intent.putExtra("type",2);
                    int index = startIndex + counter;
                    intent.putExtra("index",index);
                    intent.putExtra("activityId",sessionManager.getActivityId());
                    setResult(RESULT_OK,intent);
                    RecordingActivity.this.finish();
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

        mPlayer = MediaPlayer.create(RecordingActivity.this, R.raw.tin_surprise);
        mPlayerOne = MediaPlayer.create(RecordingActivity.this, R.raw.small_applause);

        Speech.init(RecordingActivity.this, getPackageName());

        storyManager = DataManager.init(this).getStorymanager();
        answerManager = DataManager.init(this).getAnswermanager();
        storyItemManager = DataManager.init(this).getStoryItemmanager();

        activityRecordingBinding.menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopListening();
                showExitDialogue();
            }
        });

        activityRecordingBinding.hintText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QItemPojo storyItemPojo = storyItemPojoList.get(counter);
                if (storyItemPojo.getHint() != null && !storyItemPojo.getHint().isEmpty()) {
                    showHintDialogue(storyItemPojo.getHint());
                } else {
                    showHintDialogue("Hint not available!!");
                    //Toast.makeText(RecordingActivity.this, "Hint not available!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        activityRecordingBinding.startBtn.setVisibility(View.GONE);
        activityRecordingBinding.listenRelative.setVisibility(View.GONE);

        activityRecordingBinding.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerList();
            }
        });


        if (isNetworkAvailable(this)) {
            getStorydata();
        } else {
            Toast.makeText(this, "Network not available!!", Toast.LENGTH_SHORT).show();
        }

//        activityRecordingBinding.startBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isAudioPermission()) {
//                   // Toast.makeText(RecordingActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(counter<storyItemPojoList.size()) {
//                                QItemPojo storyItemPojo = storyItemPojoList.get(counter);
//                                String welcomeMsg = storyItemPojo.getQuestion();
//                                String cWord = welcomeMsg.replaceAll("[^\\w]", " ");
//                                cWord = cWord.replaceAll("[^a-zA-Z0-9]", " ");
//                                Log.d("checkLogRec", "cWord is " + cWord);
//                                speak(cWord);
//                                latestResult = new StringBuilder();
//                                activityRecordingBinding.listenRelative.setVisibility(View.VISIBLE);
//                                activityRecordingBinding.micSendImage.setEnabled(true);
//                                activityRecordingBinding.startBtn.setVisibility(View.GONE);
//                            }
//                           // activityRecordingBinding.startBtn.setVisibility(View.INVISIBLE);
//                        }
//                    }, 10);
//                    initSpeechLib();
//                    ispermissionGranted = true;
//                } else {
//                    checkPermission();
//                }
//            }
//        });
        activityRecordingBinding.micSendImage.setEnabled(false);
        activityRecordingBinding.micSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mProgressDialog.show();
                String answer = activityRecordingBinding.resultEdit.getText().toString();
                if (!answer.isEmpty() && answer.length() > 0) {
                    String[] breakWord = answer.split(" ");
                    if (breakWord.length > 4) {
                        QItemPojo storyItemPojoPojo = storyItemPojoList.get(counter);
                        Log.d("checkLogQ", "Question is " + storyItemPojoPojo.getQuestion());
                        if (checkResult(answer, storyItemPojoPojo.getQuestion())) {
                            int index = startIndex + counter + 1;
                            String answerOne = "A" + index + " : " + activityRecordingBinding.resultEdit.getText().toString();
                            activityRecordingBinding.micSendImage.setEnabled(false);
                            activityRecordingBinding.playPauseButton.setVisibility(View.GONE);
                            // countDownTimer.cancel();
                            AnswerPojo quetionPojo = new AnswerPojo();
                            quetionPojo.setActivity_id(Integer.parseInt(sessionManager.getActivityId()));
                            QItemPojo storyItemPojo = storyItemPojoList.get(counter);
                            int indexQ = startIndex + counter + 1;
                            String question = "Q" + indexQ + ". " + storyItemPojo.getQuestion();
                            quetionPojo.setLine(question);
                            answerManager.createOrUpdate(quetionPojo);
                            answerList.add(question);
                            initAnimation(activityRecordingBinding.startImage.getX() / 6, 400, activityRecordingBinding.startImage.getY() / 4, -800);
                            // initAnimation(textArray[counter].getX(),activityHomeBinding.scoreText.getX(),textArray[counter].getY(),activityHomeBinding.scoreText.getY());
                            AnswerPojo answerPojo = new AnswerPojo();
                            answerPojo.setActivity_id(Integer.parseInt(sessionManager.getActivityId()));
                            answerPojo.setLine(answerOne);
                            answerManager.createOrUpdate(answerPojo);
                            answerList.add(answerOne);
                            if (isGame) {
                                updateScoreGame(answer);
                            } else {
                                updateScore(answer);
                            }
                            stopListening();
                            //   activityRecordingBinding.playPauseButton.setVisibility(View.INVISIBLE);
                            //   countDownTimer.cancel();
                        } else {
                            String message = "At least one word should matched from question!!";
                            speakOne(message);
                        }
                    } else {
                        String message = "Answer should be greater than 4 words!!";
                        speakOne(message);
                    }
                } else {
                    String message = "Answer should be greater than 4 words!!";
                    speakOne(message);
                }
            }
        });

//        activityRecordingBinding.refreshImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activityRecordingBinding.resultEdit.setText("");
//                String msg="Tap on the button and start answering!";
//                speakStart(msg);
//            }
//        });

        activityRecordingBinding.refreshImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopListening();
                startListening();
                String content = activityRecordingBinding.resultEdit.getText().toString();
                activityRecordingBinding.resultEdit.setText("");
                latestResult = new StringBuilder();
                subLatestResults = new StringBuilder();
                System.gc();
//                if(content!=null && content.length()>0) {
//                    activityRecordingBinding.resultEdit.setEnabled(true);
//                    Toast.makeText(RecordingActivity.this, "Edit your answer!!", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(RecordingActivity.this, "Nothing to edit!!", Toast.LENGTH_SHORT).show();
//                }

            }
        });

//        if(sessionManager.getBlobId()!=null && sessionManager.getBlobId().length()>0){
//            Glide.with(RecordingActivity.this).
//                    load(sessionManager.getBlobId())
//                    .apply(new RequestOptions().placeholder(R.mipmap.girl_image).error(R.mipmap.girl_image))
//                    .into(activityRecordingBinding.girlImage);
//
//        }else{
//            activityRecordingBinding.girlImage.setVisibility(View.GONE);
//        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(RecordingActivity.this)) {
//            Glide.with(this).
//                    load(sessionManager.getBlobId())
//                    .apply(new RequestOptions().placeholder(R.mipmap.girl_image).error(R.mipmap.girl_image))
//                    .into(activityRecordingBinding.girlImage);
//            activityRecordingBinding.girlImage.setVisibility(View.VISIBLE);
//        }else{
//            startService(new Intent(RecordingActivity.this, FloatingViewService.class));
//        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        Glide.with(this).
                load(sessionManager.getBlobId())
                .apply(new RequestOptions().placeholder(R.mipmap.girl_image).error(R.mipmap.girl_image))
                .into(activityRecordingBinding.girlImage);
        activityRecordingBinding.girlImage.setVisibility(View.VISIBLE);

        setTouchListener();

        activityRecordingBinding.playPauseButton.setVisibility(View.GONE);
        activityRecordingBinding.noteText.setVisibility(View.GONE);
        activityRecordingBinding.playPauseButton.setTag(0);
        activityRecordingBinding.playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityRecordingBinding.playPauseButton.setVisibility(View.GONE);
                activityRecordingBinding.noteText.setVisibility(View.GONE);
                speakStartOne("");
//                int tag=(Integer) view.getTag();
//                if(tag==0){
//                    activityRecordingBinding.playPauseButton.setTag(1);
//                    speakStop("Stopped");
//                    ((TextView)view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.play,0,0,0);
//                }else{
//                    activityRecordingBinding.playPauseButton.setTag(0);
//                    speakStartOne("Start");
//                    ((TextView)view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause,0,0,0);
//                }
            }
        });

        // setTimer(10000);

//        int indexOne=startIndex+counter;
//        setProgressOne(indexOne,startIndex+storyItemPojoList.size());

//        if (isNetworkAvailable(this)) {
//            getStorydata();
//        } else {
//            Toast.makeText(this, "Network not available!!", Toast.LENGTH_SHORT).show();
//        }

        // showAnswerList();

    }


    private boolean checkResult(String result, String answer) {
        Log.d("checkLog", "answer is " + answer);
        Log.d("checkLog", "result is " + result);
        boolean flag = false;
        String matchResult = answer;
        String cWord = matchResult.replaceAll("[^\\w]", " ");
        cWord = cWord.replaceAll("[^a-zA-Z0-9]", " ");
        Log.d("checkLog", "matchresult is " + cWord);
        String ccWord = result.replaceAll("[^\\w]", " ");
        ccWord = ccWord.replaceAll("[^a-zA-Z0-9]", " ");
        Log.d("checkLog", "cSenetence is " + ccWord);
        if (result != null && !result.isEmpty()) {
            String[] resultArray = ccWord.split("\\s+");
            String[] questionArray = cWord.split("\\s+");
            for (int i = 0; i < resultArray.length; i++) {
                for (int j = 0; j < questionArray.length; j++) {
                    if (questionArray[j].toLowerCase().equalsIgnoreCase(resultArray[i].toLowerCase())) {
                        flag = true;
                        break;
                    }
                }
            }
        } else {
            flag = false;
        }

        return flag;

    }

    private void setListening() {
        if (isAudioPermission()) {
            // Toast.makeText(RecordingActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (counter < storyItemPojoList.size()) {
                        QItemPojo storyItemPojo = storyItemPojoList.get(counter);
                        String welcomeMsg = storyItemPojo.getQuestion();
                        String cWord = welcomeMsg.replaceAll("[^\\w]", " ");
                        cWord = cWord.replaceAll("[^a-zA-Z0-9]", " ");
                        Log.d("checkLogRec", "cWord is " + cWord);
                        speak(cWord);
                        latestResult = new StringBuilder();
                        subLatestResults = new StringBuilder();
                        activityRecordingBinding.listenRelative.setVisibility(View.GONE);
                        activityRecordingBinding.startBtn.setVisibility(View.GONE);
                    }
                    // activityRecordingBinding.startBtn.setVisibility(View.INVISIBLE);
                }
            }, 10);
            initSpeechLib();
            ispermissionGranted = true;
        } else {
            checkPermission();
        }
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

    private void gotoNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
                activityRecordingBinding.listenRelative.setVisibility(View.GONE);
                activityRecordingBinding.startBtn.setVisibility(View.GONE);
                ++counter;
                if (counter < storyItemPojoList.size()) {
                    QItemPojo storyItemPojo = storyItemPojoList.get(counter);
//                    if(storyItemPojo.getHint()!=null && storyItemPojo.getHint().length()>0) {
//                        activityRecordingBinding.instructionText.setText(storyItemPojo.getHint());
//                    }else{
//                        activityRecordingBinding.instructionText.setText("Instruction ot available!!");
//                    }
                    int index = startIndex + counter + 1;
                    String question = "Q" + index + ". " + storyItemPojo.getQuestion();
                    activityRecordingBinding.qText.setText(question);

                    int indexOne = startIndex + counter;
                    setProgressOne(indexOne, startIndex + storyItemPojoList.size());

                    AnswerPojo answerPojo = new AnswerPojo();
                    answerPojo.setActivity_id(Integer.parseInt(sessionManager.getActivityId()));
                    answerPojo.setLine(question);
//                    answerManager.createOrUpdate(answerPojo);
//                    answerList.add(question);
                    activityRecordingBinding.resultEdit.setText("");
                    setListening();
                } else {
                    //okDialogue("It's Done, Thank you "+sessionManager.getGender());

                    setProgressOne(pastItemList.size() + storyItemPojoList.size(), pastItemList.size() + storyItemPojoList.size());
                    activityRecordingBinding.optionsLinear.setVisibility(View.GONE);
                    activityRecordingBinding.qText.setVisibility(View.INVISIBLE);
                    activityRecordingBinding.resultEdit.setVisibility(View.INVISIBLE);
                    activityRecordingBinding.startBtn.setVisibility(View.GONE);
                    updateActivityStatus();
                }
            }
        }, 100);
    }


    private void speakFial(String msg) {
        //Speech.getInstance().setTextToSpeechRate(Cons.speed);
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
                // Toast.makeText(HomeActivity.this, "Completed..", Toast.LENGTH_SHORT).show();
                if (isGame) {
                    Intent intent=new Intent();
                    intent.putExtra("isCompleted",true);
                    intent.putExtra("type",2);
                    setResult(RESULT_CANCELED,intent);
                    RecordingActivity.this.finish();
                } else {
                    if (!isRetry) {
                        int gotScore = points - initialPoints;
                        if (gotScore > 0) {
                            showStarDialogueFinal(gotScore);
                        } else {
                            setResult(RESULT_OK);
                            RecordingActivity.this.finish();
                        }
                    } else {
                        setResult(RESULT_OK);
                        RecordingActivity.this.finish();
                    }
                }
            }

            @Override
            public void onError() {
                Log.i("speech", "speech error");
                if (!isRetry) {
                    int gotScore = points - initialPoints;
                    showStarDialogueFinal(gotScore);
                } else {
                    setResult(RESULT_OK);
                    RecordingActivity.this.finish();
                }
                // Toast.makeText(RecordingActivity.this, "Error..", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setProgressOne(int currentIndex, int max) {
        activityRecordingBinding.progressBar.setProgress(currentIndex);
        activityRecordingBinding.progressText.setText("" + currentIndex + "/" + max);
    }

    void okDialogue(final String msg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        RecordingActivity.this.finish();
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

    private void speak(String msg) {
//        Speech.getInstance().setTextToSpeechRate(Cons.speed);
//        Locale locale = new Locale("hi_IN");
//        Speech.getInstance().setLocale(locale);
        Speech.getInstance().say(msg, new TextToSpeechCallback() {
            @Override
            public void onStart() {
                setAvatarAnimation(4000, 1000);
                Log.i("speech", "speech started");
                // onBeatSound();
                //Toast.makeText(HomeActivity.this, "Start..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted() {
                Log.i("speech", "speech completed");
                startListening();
                activityRecordingBinding.playPauseButton.setVisibility(View.GONE);
                activityRecordingBinding.noteText.setVisibility(View.GONE);
                //  countDownTimer.start();
            }

            @Override
            public void onError() {
                Log.i("speech", "speech error");
                Toast.makeText(RecordingActivity.this, "Error..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void speakOne(String msg) {
        //  Speech.getInstance().setTextToSpeechRate(1.5F);
//        Speech.getInstance().setTextToSpeechRate(Cons.speed);
//        Locale locale = new Locale("hi_IN");
//        Speech.getInstance().setLocale(locale);
        Speech.getInstance().say(msg, new TextToSpeechCallback() {
            @Override
            public void onStart() {
                Log.i("speech", "speech started");
                stopListening();
                setAvatarAnimation(4000, 1000);
                // onBeatSound();
                //Toast.makeText(HomeActivity.this, "Start..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted() {
                Log.i("speech", "speech completed");
                startListening();
                // gotoNext();

            }

            @Override
            public void onError() {
                Log.i("speech", "speech error");
                Toast.makeText(RecordingActivity.this, "Error..", Toast.LENGTH_SHORT).show();
            }
        });
    }


    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RecordAudioRequestCode:
                if (isAudioPermission()) {
                    // initSpeechLib();
                    setListening();
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    checkPermission();
                }
                break;
        }
    }

    private StringBuilder latestResult, subLatestResults;

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
                Log.d("checkLog", " OnSpeechRecognitionFinalResult " + result);
                String prevString = activityRecordingBinding.resultEdit.getText().toString();
                if (result != null && result.length() > 0) {
                    // latestResult.append(result);
                    Log.i("checkLog", "result: " + result);
                    String[] breakWord = latestResult.toString().split(" ");
                    if (breakWord.length > 0) {
                        activityRecordingBinding.micSendImage.setEnabled(true);
                    } else {
                        // Toast.makeText(RecordingActivity.this, "Answer Length should be greater than 4 words.", Toast.LENGTH_SHORT).show();
                    }
                    //String mresult= removeDublicate(result);
                    latestResult.append(" " + result);
                    activityRecordingBinding.resultEdit.setText(latestResult);
                    activityRecordingBinding.resultEdit.setEnabled(false);
                    //  validateSpeech(result, false);
                    subLatestResults = new StringBuilder();
                    startListening();
                }
            }

            @Override
            public void OnSpeechRecognitionCurrentResult(String result) {
                Log.d("speechLog", " OnSpeechRecognitionCurrentResult " + result);
//                String prevString=activityRecordingBinding.resultEdit.getText().toString();
                subLatestResults.append(result);
                if (result != null && result.length() > 0) {
                    String [] splitArray=result.split(" ");
                    Log.d("checkLogK","length of splitArray is "+splitArray.length);
                    if(splitArray.length>1) {
                        String mresult = removeDublicate(result);
                        Log.d("checkLogK", "inside OnSpeechRecognitionCurrentResult mResult " + mresult);
                        activityRecordingBinding.resultEdit.setText(latestResult + " " + mresult);
                    }
                    //  activityRecordingBinding.resultEdit.setEnabled(false);
                    //  validateSpeech(result, false);
                    //   startListening();
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
        String prevString = subLatestResults.toString();
        String[] cWords = prevString.split(" ");
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

    public void getStorydata() {
        mProgressDialog.show();
        String url = Cons.BASE_URL + "speaking/" + sessionManager.getActivityId() + "/questions?student_id=" + sessionManager.getStudentId();
        Log.d("checkLog", "inside getStorydata is url " + url);
        volleyManager.makeGetCall(this, url, new CallBack() {

            @Override
            public void onSuccessResponse(JSONObject response) {
                Log.d("checkLogin", "inside on onSuccessResponse is " + response.toString());
                storyItemPojoList = new ArrayList<>();
                pastItemList = new ArrayList<>();
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
                    if (!resultObject.isNull("speaking_questions")) {
                        JSONArray storyItemArray = resultObject.getJSONArray("speaking_questions");
                        for (int j = 0; j < storyItemArray.length(); j++) {
                            JSONObject jsonObjectOne = storyItemArray.getJSONObject(j);
                            QItemPojo storyItemPojo = mapper.readValue(jsonObjectOne.toString(),
                                    QItemPojo.class);
                            if (storyItemPojo.getIs_completed() == 1) {
                                pastItemList.add(storyItemPojo);
                            } else {
                                storyItemPojoList.add(storyItemPojo);
                            }
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    // okDialogue(e.toString());
                    mProgressDialog.dismiss();
                    Toast.makeText(RecordingActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {

                }

                if (storyItemPojoList.size() == 0 && pastItemList.size() > 0) {
                    isRetry = true;
                    storyItemPojoList.addAll(pastItemList);
                    pastItemList.removeAll(pastItemList);
                    deleteAnswers(Integer.parseInt(sessionManager.getActivityId()));
                } else {
                    if (retryNum >= 1) {
                        isRetry = true;
                    } else {
                        isRetry = false;
                    }
                }

                Log.d("CheckLogSize", "storyItemPojoList size is " + storyItemPojoList.size());

                Log.d("CheckLogSize", "pastItemPojoList size is " + pastItemList.size());

                startIndex = pastItemList.size();

                setLinkOne();

                if (!isGame)
                    getScoredata(true);

                // setAdapter(storyPojoList);
            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray response) {

            }

            @Override
            public void onVolleyError(VolleyError error) {
                mProgressDialog.dismiss();
                if (!isGame)
                    getScoredata(true);
                // Toast.makeText(RecordingActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTokenExpire() {

            }
        });

    }

    private void deleteAnswers(int activityId) {
        answerManager.deleteByActivityId(activityId);
        //activityRecordingBinding.startBtn.setVisibility(View.VISIBLE);
    }

    int points;
    int initialPoints = 0;

    public void getScoredata(boolean isFirstTime) {
        // mProgressDialog.show();
        String url = Cons.BASE_URL + "students/" + sessionManager.getStudentId() + "/points";
        Log.d("checkLog", "inside getStorydata is url " + url);
        volleyManager.makeGetCall(this, url, new CallBack() {

            @Override
            public void onSuccessResponse(JSONObject response) {
                Log.d("checkLogin", "inside on onSuccessResponse is " + response.toString());
                //  mProgressDialog.dismiss();
                ObjectMapper mapper = new ObjectMapper();
                Log.d("checkLogin", "inside on onSuccessJsonArrayResponse is " + response.toString());
                try {
                    int status = response.getInt("status");
                    int statusCode = response.getInt("statusCode");
                    if (statusCode == 200) {
                        JSONObject resultObject = response.getJSONObject("result");
                        points = resultObject.getInt("points");
                        if (isFirstTime) {
                            initialPoints = points;
                            activityRecordingBinding.scoreText.setText("" + points);
                        }
                    }
                } catch (JSONException e) {
                    // okDialogue(e.toString());
                    Toast.makeText(RecordingActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

                if (!isFirstTime) {
                    chooseAnim();
                }


            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray response) {

            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (!isFirstTime) {
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

    public void showGameOverDialogue(String score) {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_game_over, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);
        ImageView imageView = dialogView.findViewById(R.id.backImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                RecordingActivity.this.finish();
            }
        });
        TextView hintText = dialogView.findViewById(R.id.hintText);
        hintText.setText("Your score is " + score);
        AppCompatButton continueButton = dialogView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                RecordingActivity.this.finish();
            }
        });
        dialog.show();
    }

    CountDownTimer cTimer;
    public void setTimer(long millis) {
        cTimer=new CountDownTimer(millis, 1000) {

            public void onTick(long millisUntilFinished) {
                // activityRecordingBinding.playPauseButton.setText(" " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                //  Log.d("checkLogTimer","inside  onTick "+millisUntilFinished);
                if(millisUntilFinished>1000) {
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
                }catch (Exception e){
                    setResult(RESULT_OK);
                    RecordingActivity.this.finish();
                }
                // activityRecordingBinding.playPauseButton.setText(" ");
                //txtTimer.setText(minutes + ":" + seconds);
            }

        };
        cTimer.start();
    }


    private int counter;

    private void setLinkOne() {
        if (storyPojo != null) {
            String image = storyPojo.getImage();
            Glide.with(this).load(image).apply(new RequestOptions().placeholder(R.mipmap.dummy_image_story).error(R.mipmap.dummy_image_story)).into(activityRecordingBinding.image);
            activityRecordingBinding.headerText.setText(storyPojo.getName());
            activityRecordingBinding.instructionText.setText(storyPojo.getInstruction());
        }
        if (storyItemPojoList != null && storyItemPojoList.size() > 0) {
            activityRecordingBinding.startBtn.setVisibility(View.GONE);
            activityRecordingBinding.listenRelative.setVisibility(View.GONE);
            counter = 0;
            QItemPojo storyItemPojo = storyItemPojoList.get(counter);
//            if(storyItemPojo.getHint()!=null && storyItemPojo.getHint().length()>0) {
//                activityRecordingBinding.instructionText.setText(storyItemPojo.getHint());
//            }else{
//                activityRecordingBinding.instructionText.setText("Instruction not available!!");
//            }
            int index = startIndex + counter + 1;
            String question = "Q" + index + ". " + storyItemPojo.getQuestion();
            activityRecordingBinding.qText.setText(question);
            int indexOne = startIndex + counter;
            activityRecordingBinding.progressBar.setMax(startIndex + storyItemPojoList.size());
            setProgressOne(indexOne, startIndex + storyItemPojoList.size());
            //answerList.add(question);
            AnswerPojo answerPojo = new AnswerPojo();
            answerPojo.setActivity_id(Integer.parseInt(sessionManager.getActivityId()));
            answerPojo.setLine(question);
            setListening();
            //  countDownTimer.start();
            // answerManager.createOrUpdate(answerPojo);
//            String msg="Tap on the button and start answering!";
//            speakStart(msg);
        } else {
            // okDialogueDone("Sorry!, Data not available.");
            activityRecordingBinding.optionsLinear.setVisibility(View.GONE);
            activityRecordingBinding.resultEdit.setVisibility(View.INVISIBLE);
            activityRecordingBinding.progressBar.setMax(startIndex + storyItemPojoList.size());
            setProgressOne(pastItemList.size() + storyItemPojoList.size(), pastItemList.size() + storyItemPojoList.size());
            String msg = "You have already completed this activity, please choose another one";
            speakStart(msg);
            activityRecordingBinding.startBtn.setVisibility(View.VISIBLE);
            // showAnswerList();
            // Toast.makeText(this, "You have already completed this activity, please choose another one.!!", Toast.LENGTH_SHORT).show();
            // setResult(RESULT_OK);
            //  RecordingActivity.this.finish();

        }
    }

    void okDialogueDone(final String msg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        RecordingActivity.this.finish();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
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

    public void showHintDialogue(String hint) {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_hint, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);
        ImageView imageView = dialogView.findViewById(R.id.backImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView hintText = dialogView.findViewById(R.id.hintText);
        hintText.setText(hint);
        AppCompatButton continueButton = dialogView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
                            initAnimation(activityRecordingBinding.startImage.getX() / 6, 400, activityRecordingBinding.startImage.getY() / 4, -800);
                            runAnimation();
                        } else {
                            gotoNext();
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
                            initAnimation(activityRecordingBinding.startImage.getX() / 6, 400, activityRecordingBinding.startImage.getY() / 4, -800);
                            runAnimation();
                        } else {
                            gotoNext();
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
                        gotoNext();
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
       // QItemPojo storyItemPojo = storyItemPojoList.get(counter-1);
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
                String msg = "Congratulations , You have completed this activity.";
                speakFial(msg);
            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray response) {

            }

            @Override
            public void onVolleyError(VolleyError error) {
                mProgressDialog.dismiss();
                String msg = "Congratulations , You have completed this activity.";
                speakFial(msg);
            }

            @Override
            public void onTokenExpire() {

            }
        });

    }


    public void updateScore(String answer) {
        mProgressDialog.show();
        QItemPojo storyItemPojo = storyItemPojoList.get(counter);
        JSONObject scoreObject = new JSONObject();
        //String token= FirebaseInstanceId.getInstance().getToken();
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("answer", answer);
            paramObject.put("student_id", sessionManager.getStudentId());
            paramObject.put("sub_activity_id", storyItemPojo.getId());
            paramObject.put("chapter_id", chapter_id);
            paramObject.put("unit_id",unit_id);
            // paramObject.put("lineId", sessionManager.getStudentId());
            paramObject.put("notes", scoreObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String accessToken=getPreferences(MODE_PRIVATE).getString("accesstoken", null);//sessionManager.getToken();
        Log.d("checkLog", "inside updateStory data is " + paramObject.toString());
        Map<String, String> header = new DeviceInfoUtils().getHeaderInfo(sessionManager.getUserId());
        Log.d("checkLog", "inside updateStory header is url " + header.toString());
        String url = Cons.BASE_URL + "speaking/" + sessionManager.getActivityId() + "/record";
        Log.d("checkLog", "inside getStorydata is url " + url);
        volleyManager.makePostCall(this, paramObject, url, header, new CallBack() {

            @Override
            public void onSuccessResponse(JSONObject response) {
                Log.d("checkLogin", "inside on onSuccessResponse is " + response.toString());
                mProgressDialog.dismiss();
                try {
                    int statusCode = response.getInt("statusCode");
                    if (statusCode == 200) {
                        JSONObject mObject = response.getJSONObject("result");
                        int score = mObject.getInt("points");
//                        activityRecordingBinding.scoreText.setText("" + score);
//                        chooseAnim();
                    } else {
                        //   getScoredata(false);
                    }
                    // Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    getScoredata(false);
                }

                getScoredata(false);
                //   chooseAnim();
            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray response) {

            }

            @Override
            public void onVolleyError(VolleyError error) {
                getScoredata(false);
                activityRecordingBinding.micSendImage.setEnabled(true);
                mProgressDialog.dismiss();
            }

            @Override
            public void onTokenExpire() {

            }
        });

    }


    public void updateScoreGame(String answer) {
        mProgressDialog.show();
        QItemPojo storyItemPojo = storyItemPojoList.get(counter);
        JSONObject scoreObject = new JSONObject();
        //String token= FirebaseInstanceId.getInstance().getToken();
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("answer", answer);
            paramObject.put("GameCode", sessionManager.getGameIdOne());
            paramObject.put("student_id", sessionManager.getStudentId());
            paramObject.put("sub_activity_id", storyItemPojo.getId());
            paramObject.put("chapter_id", chapter_id);
            paramObject.put("unit_id",unit_id);
            // paramObject.put("lineId", sessionManager.getStudentId());
            paramObject.put("notes", scoreObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String accessToken=getPreferences(MODE_PRIVATE).getString("accesstoken", null);//sessionManager.getToken();
        Log.d("checkLog", "inside updateStory data is " + paramObject.toString());
        Map<String, String> header = new DeviceInfoUtils().getHeaderInfo(sessionManager.getUserId());
        Log.d("checkLog", "inside updateStory header is url " + header.toString());
        String url = Cons.BASE_URL + "timergame/speaking/" + sessionManager.getActivityId() + "/record";
        Log.d("checkLog", "inside getStorydata is url " + url);
        volleyManager.makePostCall(this, paramObject, url, header, new CallBack() {

            @Override
            public void onSuccessResponse(JSONObject response) {
                Log.d("checkLogin", "inside on onSuccessResponse is " + response.toString());
                mProgressDialog.dismiss();
                try {
                    int statusCode = response.getInt("statusCode");
                    if (statusCode == 200) {
                        JSONObject mObject = response.getJSONObject("result");
                        points = mObject.getInt("points");
                        activityRecordingBinding.scoreText.setText("" + points);
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
                activityRecordingBinding.micSendImage.setEnabled(true);
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

        new ConfettiManager(RecordingActivity.this, confettoGenerator, confettiSource, activityRecordingBinding.parent)
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
                .setTimeToLive(1500L)
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
                Random r = new Random();
                String msg = Cons.applaud_lines[r.nextInt(Cons.applaud_lines.length)];
                speakOne(msg);
                gotoNext();
            }
        });
        TextView hintText = dialogView.findViewById(R.id.hintText);
        // hintText.setText(hint);
        hintText.setText("You have won " + score + " stars");
        RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.simpleRatingBar);
        ratingBar.setRating(score);
        AppCompatButton continueButton = dialogView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Random r = new Random();
                String msg = Cons.applaud_lines[r.nextInt(Cons.applaud_lines.length)];
                speakOne(msg);
                gotoNext();
            }
        });
        dialog.show();
    }


    public void showStarDialogueFinal(int score) {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_star_final, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);
        ImageView imageView = dialogView.findViewById(R.id.backImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // showAnswerList();
                setResult(RESULT_OK);
                RecordingActivity.this.finish();
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
                showAnswerList();
                // RecordingActivity.this.finish();
            }
        });
        dialog.show();
    }

    private AnswerAdapter duetAdapter;
    private AnswerAdapterTwo duetAdapterOne;

    public void setAdapter(List<String> mList, RecyclerView mrecyclerview, BottomSheetDialog dialog) {
        List<AnswerPojo> answerPojoList = answerManager.getStoryItemByActivityID(Integer.parseInt(sessionManager.getActivityId()));
        Log.d("checkLog", "inside setAdapter database list size is " + answerPojoList.size());
        Collections.reverse(answerPojoList);
        if (answerPojoList != null && answerPojoList.size() > 0) {
            duetAdapterOne = new AnswerAdapterTwo(this);
            duetAdapterOne.setSeriesArrayList(answerPojoList);
            mrecyclerview.setAdapter(duetAdapterOne);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            mrecyclerview.setLayoutManager(llm);
            mrecyclerview.setVisibility(View.VISIBLE);
            duetAdapterOne.notifyDataSetChanged();
            dialog.show();
        } else if (mList != null && mList.size() > 0) {
            duetAdapter = new AnswerAdapter(this);
            duetAdapter.setSeriesArrayList(mList);
            mrecyclerview.setAdapter(duetAdapter);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            mrecyclerview.setLayoutManager(llm);
            mrecyclerview.setVisibility(View.VISIBLE);
            duetAdapter.notifyDataSetChanged();
            dialog.show();
        } else {
            dialog.dismiss();
            Toast.makeText(RecordingActivity.this, "Sorry, Answers not available.!!", Toast.LENGTH_SHORT).show();

        }

    }


    public void showAnswerList() {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_answer, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);
        ImageView imageView = dialogView.findViewById(R.id.backImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String msg = "You have completed this activity, please choose another one";
                speak(msg);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RecordingActivity.this.finish();
                    }
                }, 4000);

            }
        });
        RecyclerView mRecycler = dialogView.findViewById(R.id.mrecyclerview);
        setAdapter(answerList, mRecycler, dialog);
        // hintText.setText(hint);
    }

    private MediaPlayer mPlayerOne;

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
            activityRecordingBinding.startBtn.setVisibility(View.GONE);
            String text = activityRecordingBinding.scoreText.getText().toString();
            Log.d("checkAnimLog", "inside onAnimationEnd previous score " + text + " current score " + points);
            if(points>0 && points>= Integer.parseInt(text)) {
                animateTextViewOne(Integer.parseInt(text), points, activityRecordingBinding.scoreText);
            }
            gotoNext();
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

    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(0.8f);
        int start = Math.min(initialValue, finalValue);
        int end = Math.max(initialValue, finalValue);
        int difference = Math.abs(finalValue - initialValue);
        Handler handler = new Handler();
        for (int count = start; count <= end; count++) {
            int time = 1500; //Math.round(decelerateInterpolator.getInterpolation((((float) count) / difference)) * 100) * count;
            final int finalCount = ((initialValue > finalValue) ? initialValue - count : count);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textview.setText(String.valueOf(finalCount));
                }
            }, time);
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
                setAvatarAnimation(4000, 1000);
                Log.i("speech", "speech started");
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
                Toast.makeText(RecordingActivity.this, "Error..", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void speakStartOne(String msg) {
        //Speech.getInstance().setAudioStream(0);
//        Speech.getInstance().setTextToSpeechRate(Cons.speed);
//        Locale locale = new Locale("hi_IN");
//        Speech.getInstance().setLocale(locale);
        Speech.getInstance().say(msg, new TextToSpeechCallback() {
            @Override
            public void onStart() {
                //setAvatarAnimation(3000,1000);
                // stopListening();
                // onBeatSound();
                // Log.i("speech", "speech started");
                //Toast.makeText(HomeActivity.this, "Start..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted() {
                startListening();
                activityRecordingBinding.playPauseButton.setVisibility(View.GONE);
                activityRecordingBinding.noteText.setVisibility(View.GONE);
                //    countDownTimer.start();
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
                //  countDownTimer.cancel();
                stopListening();
                //  activityRecordingBinding.playPauseButton.setVisibility(View.INVISIBLE);
                //  countDownTimer.cancel();
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