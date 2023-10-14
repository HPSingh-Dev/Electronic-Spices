package com.conductor.apni.littleleappwa;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.conductor.apni.littleleappwa.adapter.StoryAdapter;
import com.conductor.apni.littleleappwa.adapter.StoryItemAdapter;
import com.conductor.apni.littleleappwa.data.QItemPojo;
import com.conductor.apni.littleleappwa.data.StoryItemPojo;
import com.conductor.apni.littleleappwa.data.StoryPojo;
import com.conductor.apni.littleleappwa.databinding.ActivityHomeBinding;
import com.conductor.apni.littleleappwa.dbmanager.DataManager;
import com.conductor.apni.littleleappwa.dbmanager.StoryItemManager;
import com.conductor.apni.littleleappwa.services.Cons;
import com.conductor.apni.littleleappwa.services.VolleyManager;
import com.conductor.apni.littleleappwa.services.callback.CallBack;
import com.conductor.apni.littleleappwa.utils.AppUtil;
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
import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Logger;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;
import net.gotev.speech.TextToSpeechCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

// implements RecognitionListener
public class HomeActivity extends AppCompatActivity {

    public ActivityHomeBinding activityHomeBinding;
    private final int RecordAudioRequestCode = 101;
    private final int WRITERequestCode = 102;
    protected AudioManager mAudioManager;
    private List<StoryItemPojo> storyItemPojoList, pastItemPojoList;
    private StoryItemManager storyItemManager;
    private boolean ispermissionGranted = false;

    private String text = "", image = "";

    private SessionManager sessionManager;

    public TextView[] textArray;
    public LinearLayout[] textArrayOne;
    public boolean[] cArray;


    public void setLink(String name, int pos) {
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.team_link_item, null, false);
        view.setTag(pos);
        ((TextView) view).setText(name);
        //  ((TextView) view).setTextSize(18);
        //((TextView) view).setTypeface(((TextView) view).getTypeface(), Typeface.BOLD);
        //textArray[pos] = ((TextView) view);
        // ((TextView) view).setTextColor(getResources().getColor(R.color.storycolor));
        // ((TextView)view).setPaintFlags(((TextView)view).getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        //activityHomeBinding.myLinear.addView(view);

        View viewOne = mInflater.inflate(R.layout.team_link_item, null, false);
        viewOne.setTag(pos);
        ((TextView) viewOne).setText(name);
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                3,
                r.getDisplayMetrics());
        int px2 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                70,
                r.getDisplayMetrics());
        Log.d("checkPx", "px 3 value is " + px);
        ((TextView) viewOne).measure(0, 0);
        ((TextView) viewOne).setPadding(px, 1, px, 1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(px, px * 2, px, px * 2);
        //  ((TextView) view).setTextSize(18);
        //((TextView) view).setTypeface(((TextView) view).getTypeface(), Typeface.BOLD);
        textArray[pos] = ((TextView) viewOne);
        ((TextView) viewOne).setTextColor(getResources().getColor(R.color.black));
//        Log.d("checkLogPos", "pos is " + pos);
//        Log.d("checkLogPos", "width is " + width);
//        Log.d("checkLogPos", "lineWidth is " + getLineWidth(((LinearLayout) mView), ((TextView) viewOne)));
        int ttWidth = getLineWidth(((LinearLayout) mView), ((TextView) viewOne));
        // if (ttWidth+40>width ) {
        if (pos == 0) {
            mView = mInflater.inflate(R.layout.linear_linear, null, false);
//                RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            ((LinearLayout) rView).addView(mView);

            mView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    View parent = (View) v.getParent();
                    parent.performClick();
                }
            });
            //  preView=mView;
        } else if (ttWidth + px2 > width) {
            //int mPos = (pos / 5) - 1;
            // Log.d("checkLogPos", "pos is " + pos + " mpos " + mPos);
            mView = mInflater.inflate(R.layout.linear_linear, null, false);
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                LinearLayout v = (LinearLayout) ((RelativeLayout) rView).getChildAt(mPos);
//                params.addRule(RelativeLayout.BELOW, v.getId());
            ((LinearLayout) rView).addView(mView);
            mView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    View parent = (View) v.getParent();
                    parent.performClick();
                }
            });
        }
        //preView=mView;
        // }
        ((LinearLayout) mView).addView(viewOne, params);
        view.setVisibility(View.VISIBLE);
        viewOne.setVisibility(View.VISIBLE);

        viewOne.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View parent = (View) v.getParent();
                parent.performClick();
            }
        });

    }

    private int getChildCount() {
        int totalChild = 0;
        int cChild = ((LinearLayout) rView).getChildCount();
        for (int i = 0; i < cChild; i++) {
            LinearLayout mLinear = (LinearLayout) ((LinearLayout) rView).getChildAt(i);
            // Log.d("checkLog","inside getchildcount i "+mLinear.getChildCount());
            totalChild += mLinear.getChildCount();
            // Log.d("checkLog","inside getchildcount "+totalChild);
        }
        return totalChild;
    }

    public void addNextView(boolean isSpeak) {
        if (((LinearLayout) mView) != null) {
            int totalChild = getChildCount();
            if (totalChild > words.length + 1) {
                //  Log.d("checkLog","next line already added");
                viewNextButton();
            } else {
                if (isSpeak) {
                    //speakOne("It seems, You are Stuck");
                }
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewOne = mInflater.inflate(R.layout.team_link_item_two, null, false);
                viewOne.setTag(indexCount);
                Resources r = getResources();
                int px = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        3,
                        r.getDisplayMetrics());
                // Log.d("checkPx", "px 3 value is "+px);
                ((TextView) viewOne).measure(0, 0);
                ((TextView) viewOne).setPadding(px, px, px, px);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(px, 0, px, 0);
                ((LinearLayout) mView).addView(viewOne, params);
                viewOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        voidCounter = 0;
                        //getCorrectWord()>words.length/4 &&
                        if (getCorrectWord() > 0 && counter > 0) {
                            if (isGame) {
                                updateScoreGame(words.length, getCorrectWord());
                            } else {
                                updateScore(words.length, getCorrectWord());
                            }
                            if (getChildCount() > words.length) {
                                hideNextButton();
                            }
                        } else {
                            if (getChildCount() > words.length) {
                                hideNextButton();
                            }
                            setNextStory();
                        }
                    }
                });
            }
        }
    }


    public void addRetryView(boolean isSpeak) {
        if (((LinearLayout) mView) != null) {
            int totalChild = getChildCount();
            if (totalChild > words.length + 1) {
                Log.d("checkLog", "next line already added");
                viewNextButton();
            } else {
                if (isSpeak) {
                    //speakOne("It seems, You are Stuck");
                }
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewOne = mInflater.inflate(R.layout.team_link_item_three, null, false);
                viewOne.setTag(indexCount);
                Resources r = getResources();
                int px = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        3,
                        r.getDisplayMetrics());
                //  Log.d("checkPx", "px 3 value is "+px);
                ((TextView) viewOne).measure(0, 0);
                ((TextView) viewOne).setPadding(px, px, px, px);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(px, 0, px, 0);
                ((LinearLayout) mView).addView(viewOne, params);
                viewOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        voidCounter = 0;
                        //getCorrectWord()>words.length/4 &&
                        if (getChildCount() > words.length) {
                            hideNextButton();
                        }

                        int pos = (Integer) view.getTag();
                        if (pos == 0) {
                            indexCount = pos;
                            activityHomeBinding.myLinearOne.removeAllViews();
                            setLinkOne();
                        } else {
                            int childCount = activityHomeBinding.myLinearOne.getChildCount();
                            for (int i = pos; i < indexCount; i++) {
                                View childV = activityHomeBinding.myLinearOne.getChildAt(i);
                                childV.setVisibility(View.GONE);
                            }
                            indexCount = pos - 1;
                            //indexCount=indexCount-1;
                            setNextStory();
                        }

                    }
                });
            }
        }
    }

    private void hideNextButton() {
        int count = ((LinearLayout) mView).getChildCount();
        TextView nextText = (TextView) ((LinearLayout) mView).getChildAt(count - 1);
        nextText.setVisibility(View.GONE);

        TextView replayText = (TextView) ((LinearLayout) mView).getChildAt(count - 2);
        replayText.setVisibility(View.GONE);
    }

    private void viewNextButton() {
        int count = ((LinearLayout) mView).getChildCount();
        TextView nextText = (TextView) ((LinearLayout) mView).getChildAt(count - 1);
        nextText.setVisibility(View.VISIBLE);

        TextView replayText = (TextView) ((LinearLayout) mView).getChildAt(count - 2);
        replayText.setVisibility(View.VISIBLE);
    }

    private int getLineWidth(LinearLayout linearLayout, TextView textView) {
        int totalWidth = 0;
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                r.getDisplayMetrics());
        // Log.d("checkPx", "px value is "+px);
        if (linearLayout != null) {
            int childCount = linearLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                TextView txtView = (TextView) linearLayout.getChildAt(i);
                int width = txtView.getMeasuredWidth() + px;
                // Log.d("checkLog", " width is " + width);
                totalWidth += width;
                // Log.d("checkLog", " totalWidth is " + totalWidth);
            }
            int width = textView.getMeasuredWidth() + px;
            // Log.d("checkLog", " textView width is " + width);
            totalWidth += width;
        }
        return totalWidth;
    }

    public void setPastStory() {
        Log.d("checkLog", "setPastStory is ");
        if (pastItemPojoList != null && pastItemPojoList.size() > 0) {
            activityHomeBinding.myLinearTwo.removeAllViews();
            activityHomeBinding.myLinearTwo.setVisibility(View.VISIBLE);
            for (int i = 0; i < pastItemPojoList.size(); i++) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = mInflater.inflate(R.layout.team_link_item_one, null, false);
                View tView = ((LinearLayout) view).getChildAt(1);
                tView.setTag(i);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 20, 10, 20);
                view.setBackground(getResources().getDrawable(R.drawable.editstorydark));
                ((TextView) tView).setText(pastItemPojoList.get(i).getLine());
                ((TextView) tView).setTextColor(getResources().getColor(R.color.storycolorone));
                ((TextView) tView).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = (Integer) view.getTag();
                        //     Log.d("checkLog","position is "+pos);
                        // indexCount = pos;
                        StoryItemPojo storyItemPojo = pastItemPojoList.get(pos);
                        //    pastItemPojoList.remove(storyItemPojo);
                        View childV = activityHomeBinding.myLinearOne.getChildAt(pos);
                        childV.setVisibility(View.GONE);
                        //    Log.d("checkLog","indexCount is "+indexCount);
                        storyItemPojoList = getAllStoryItem(storyItemPojo);
                        activityHomeBinding.myLinearOne.removeAllViews();
                        setLinkOne();
                        setPastStory();
                        //   Log.d("checkLog","setpartStory is ");
                    }
                });
                activityHomeBinding.myLinearTwo.addView(view, params);
            }
        } else {
            activityHomeBinding.myLinearTwo.setVisibility(View.GONE);
        }
    }

    private ArrayList<StoryItemPojo> getAllStoryItem(StoryItemPojo storyItem) {
        ArrayList<StoryItemPojo> storyItemPojoArrayList = new ArrayList<>();
        for (int i = 0; i < storyItemPojoList.size(); i++) {
            if (i < indexCount) {
                pastItemPojoList.add(storyItemPojoList.get(i));
            } else if (i == indexCount) {
                storyItemPojoArrayList.add(storyItem);
            } else {
                storyItemPojoArrayList.add(storyItemPojoList.get(i));
            }
        }
        return storyItemPojoArrayList;
    }

    public void setLinkOne() {
        if (storyItemPojoList != null && storyItemPojoList.size() > 0) {
            activityHomeBinding.startBtn.setVisibility(View.GONE);
            textArrayOne = new LinearLayout[storyItemPojoList.size()];
            indexCount = 0;//Integer.parseInt(startIndex);
            activityHomeBinding.progressBar.setMax(pastItemPojoList.size() + storyItemPojoList.size());
            setContent();
            String image = storyPojo.getImage();
            Glide.with(this).
                    load(image)
                    .apply(new RequestOptions().error(R.mipmap.dummy_image_story))
                    .into(activityHomeBinding.image);
            activityHomeBinding.headerText.setText(storyPojo.getName());
            for (int i = 0; i < storyItemPojoList.size(); i++) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = mInflater.inflate(R.layout.team_link_item_one, null, false);
                View tView = ((LinearLayout) view).getChildAt(1);
                view.setTag(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = (Integer) view.getTag();
                        indexCount = pos;
                        activityHomeBinding.myLinearOne.removeAllViews();
                        setLinkOne();
//                        setContent();
//                        setTextCSS(pos);
//                        StoryItemPojo storyItemPojo=storyItemPojoList.get(pos);
//                        storyItemPojoList.remove(storyItemPojo);
//                        storyItemPojoList.add(indexCount,storyItemPojo);
//                        --indexCount;
//                        setNextStory();
                    }
                });
                //((TextView) tView).setTextSize(18);
                // ((TextView) tView).setTypeface(((TextView) tView).getTypeface(), Typeface.BOLD);
                textArrayOne[i] = ((LinearLayout) view);
                ((TextView) tView).setText(storyItemPojoList.get(i).getLine());
                ((TextView) tView).setTextColor(getResources().getColor(R.color.storycolorone));
                // ((TextView)view).setPaintFlags(((TextView)view).getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //((HorizontalScrollView)pView).add
                mParams.setMargins(0, 5, 0, 20);
                if (i == 0) {
                    // ((TextView) tView).setTextColor(getResources().getColor(R.color.storycolorone));
                    // ((TextView) tView).setCompoundDrawablesWithIntrinsicBounds(R.drawable.checked_one,0,0,0);
                    activityHomeBinding.myLinearOne.addView(pView, mParams);
                    //  view.setVisibility(View.VISIBLE);
                    view.setVisibility(View.GONE);
                } else {
                    //((TextView) tView).setTextColor(getResources().getColor(R.color.storycolorone));
                    //((TextView) tView).setCompoundDrawablesWithIntrinsicBounds(R.drawable.checked_two,0,0,0);
                    activityHomeBinding.myLinearOne.addView(view, mParams);
                    view.setVisibility(View.GONE);
                }
            }
            setListening();
            activityHomeBinding.playPauseButton.setVisibility(View.GONE);
//            String msg="Tap on the button and start reading story!";
//            speakStart(msg);
        } else {
            //okDialogueDone("Sorry!, Data not available.");
            activityHomeBinding.startBtn.setVisibility(View.GONE);
            stopListening();
            //Toast.makeText(this, "You have already completed this activity, please choose another one.!!", Toast.LENGTH_SHORT).show();
            String msg = "You have already completed this activity, please choose another one";
            speakEnd(msg);
        }
    }

    private void addOneView(int index) {
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.team_link_item_one, null, false);
        View tView = ((LinearLayout) view).getChildAt(1);
        view.setTag(index);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) view.getTag();
                indexCount = pos;
//                        setContent();
//                        setTextCSS(pos);
                StoryItemPojo storyItemPojo = storyItemPojoList.get(pos);
                storyItemPojoList.remove(storyItemPojo);
                storyItemPojoList.add(indexCount, storyItemPojo);
                --indexCount;
                setNextStory();
            }
        });
        //((TextView) tView).setTextSize(18);
        // ((TextView) tView).setTypeface(((TextView) tView).getTypeface(), Typeface.BOLD);
        textArrayOne[index] = ((LinearLayout) view);
        ((TextView) tView).setText(storyItemPojoList.get(index).getLine());
        ((TextView) tView).setTextColor(getResources().getColor(R.color.storycolorone));
        // ((TextView)view).setPaintFlags(((TextView)view).getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //((HorizontalScrollView)pView).add
        mParams.setMargins(0, 5, 0, 20);
        activityHomeBinding.myLinearOne.addView(view, mParams);
        view.setVisibility(View.GONE);
    }

    private void setProgressOne(int currentIndex, int max) {
        activityHomeBinding.progressBar.setProgress(currentIndex);
        activityHomeBinding.progressText.setText("" + currentIndex + "/" + max);
    }

    void okDialogueDone(final String msg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        HomeActivity.this.finish();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
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
                Toast.makeText(HomeActivity.this, "Error..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void speakEnd(String msg) {
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
                setResult(RESULT_OK);
                HomeActivity.this.finish();
            }

            @Override
            public void onError() {
                Log.i("speech", "speech error");
                setResult(RESULT_OK);
                HomeActivity.this.finish();
                Toast.makeText(HomeActivity.this, "Error..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private View mView = null, preView = null, rView, pView;

    private void setContent() {
        text = storyItemPojoList.get(indexCount).getLine();//getIntent().getStringExtra("story");
        image = storyItemPojoList.get(indexCount).getImage();
        mView = null;
        preView = null;
        voidCounter = 0;
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pView = mInflater.inflate(R.layout.team_link_linear, null, false);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //((HorizontalScrollView)pView).add
        mParams.setMargins(10, 20, 10, 20);
        View ppView = ((HorizontalScrollView) pView).getChildAt(0);
        // View ppLinearView = ((LinearLayout)ppView).getChildAt(0);
        rView = ((LinearLayout) ppView).getChildAt(1);

//        if(preView==null) {
//            mView = mInflater.inflate(R.layout.linear_linear, null, false);
//            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//            ((RelativeLayout)rView).addView(mView,params);
//            preView=mView;
//        }else{
//            mView = mInflater.inflate(R.layout.linear_linear, null, false);
//            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//            params.addRule(RelativeLayout.BELOW,preView.getId());
//            ((RelativeLayout)rView).addView(mView,params);
//            preView=mView;
//        }
        activityHomeBinding.content.setText("");
        setProgressOne(pastItemPojoList.size() + indexCount, pastItemPojoList.size() + storyItemPojoList.size());
        if (indexCount > 0) {
            textArrayOne[indexCount].setVisibility(View.GONE);
            activityHomeBinding.myLinearOne.removeViewAt(indexCount);
            activityHomeBinding.myLinearOne.addView(pView, indexCount, mParams);
            // activityHomeBinding.myLinearOne.addView(pView,mParams);
        } else {
            // textArrayOne[indexCount].setVisibility(View.GONE);
            // activityHomeBinding.myLinearOne.addView(pView);
        }

        Log.d("checkLog", "text is " + text);
        // activityHomeBinding.myLinear.removeAllViews();

        words = text.split("\\s+");
        textArray = new TextView[words.length];
        cArray = new boolean[words.length];
        for (int i = 0; i < words.length; i++) {
            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            words[i] = words[i]; //.replaceAll("[^\\w]", "");
            cArray[i] = false;

            setLink(words[i], i);

        }

        activityHomeBinding.scrollView.post(new Runnable() {
            @Override
            public void run() {
                activityHomeBinding.scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        ((LinearLayout) rView).setTag(indexCount);
        ((LinearLayout) rView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voidCounter = 0;
                int pos = (Integer) view.getTag();
                if (pos == 0) {
                    indexCount = pos;
                    activityHomeBinding.myLinearOne.removeAllViews();
                    setLinkOne();
                } else {
                    int childCount = activityHomeBinding.myLinearOne.getChildCount();
                    for (int i = pos; i < indexCount; i++) {
                        View childV = activityHomeBinding.myLinearOne.getChildAt(i);
                        childV.setVisibility(View.GONE);
                    }
                    indexCount = pos - 1;
                    //indexCount=indexCount-1;
                    setNextStory();
                }
            }
        });
        // Glide.with(this).load("http://stageadmin.littleleap.co.in/storage/images/" + image).into(activityHomeBinding.image);
    }


    private void setTextCSS(int pos) {
        for (int i = 0; i < storyItemPojoList.size(); i++) {
            LinearLayout linearLayout = textArrayOne[i];
            TextView textView = (TextView) linearLayout.getChildAt(1);
            if (pos == i) {
                textView.setTextColor(getResources().getColor(R.color.storycolorone));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checked_one, 0, 0, 0);
            } else if (pos <= indexCount) {
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checked_one, 0, 0, 0);
            } else {
                textView.setTextColor(getResources().getColor(R.color.storycolorone));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checked_two, 0, 0, 0);
            }
        }
    }

    private VolleyManager volleyManager;
    ProgressDialog mProgressDialog;
    public String[] words;
    private int indexCount = 0;
    private int startIndex = 0;
    private boolean isGame = false;
    private StoryAdapter storyAdapter;
    private StoryItemAdapter storyItemAdapter;
    private int storyId, gameScore = 0;
    private String gameId = "";
    private int unit_id=-1,chapter_id=-1;
    private Bundle params;
    DroidSpeech droidSpeech;
    SpeechRecognition speechRecognition;
    int height, width;

    private MediaPlayer mPlayer, mPlayerOne;
    public int voidCounter;
    private AppUtil appUtil;


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
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(HomeActivity.this)) {
//                    askPermission();
//                }else{
//                    startService(new Intent(HomeActivity.this, FloatingViewService.class));
//                }
//            }
//        },2000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // stopService(new Intent(HomeActivity.this, FloatingViewService.class));
    }

    ActivityResultLauncher<Intent> overlayResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(HomeActivity.this)) {
                            //askPermission();
                        } else {
                            startService(new Intent(HomeActivity.this, FloatingViewService.class));
                        }
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = SessionManager.getSessionmanager(this);
        activityHomeBinding = activityHomeBinding.inflate(getLayoutInflater());
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setContentView(activityHomeBinding.getRoot());
        volleyManager = new VolleyManager();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);

        appUtil = new AppUtil(this);

        voidCounter = 0;

        startIndex = Integer.parseInt(getIntent().getStringExtra("index"));
        gameId = getIntent().getStringExtra("gameId");
        Log.d("checkLog", "gameId is " + gameId);
        isGame = getIntent().getBooleanExtra("Game", false);
        gameScore = getIntent().getIntExtra("score", 0);
        unit_id = getIntent().getIntExtra("unit_id", -1);
        chapter_id = getIntent().getIntExtra("chapter_id", -1);
        Log.d("checkLog", "unit_id is " + unit_id + " chapter_id is " + chapter_id);
        Log.d("checkLog", "isGame is " + isGame + " gameScore is " + gameScore + " timeinmillis is " + sessionManager.getTimerGameTime());
        if (isGame) {
            initialPoints = gameScore;
            activityHomeBinding.timerText.setVisibility(View.VISIBLE);
            activityHomeBinding.scoreText.setText("" + gameScore);
            long endTimeInMillis = MainActivity.endTimeMillis;//getIntent().getLongExtra("timemillis",0);
            long currentTimeInMillis = java.lang.System.currentTimeMillis();
            // long endTimeInMillis=currentTimeInMillis+(10*60*1000);
            long savedTimeInMillis = endTimeInMillis - currentTimeInMillis;
            Log.d("checkLog", " savedTimeInMillis " + savedTimeInMillis);
            Log.d("checkLog", " currentTimeInMillis " + currentTimeInMillis);
            Log.d("checkLog", " endTimeInMillis " + endTimeInMillis);
            setTimer(savedTimeInMillis);

            activityHomeBinding.scoreText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("isScore", true);
                    intent.putExtra("type", 1);
                    int index = pastItemPojoList.size() + indexCount - 1;
                    intent.putExtra("index", index);
                    intent.putExtra("activityId", sessionManager.getActivityId());
                    setResult(RESULT_OK, intent);
                    HomeActivity.this.finish();
                }
            });

        } else {
            activityHomeBinding.timerText.setVisibility(View.GONE);
//            if (isNetworkAvailable(this)) {
//                getScoredata(true);
//            } else {
//                Toast.makeText(this, "Network not available!!", Toast.LENGTH_SHORT).show();
//            }
        }

        mPlayer = MediaPlayer.create(HomeActivity.this, R.raw.tin_surprise);
        mPlayerOne = MediaPlayer.create(HomeActivity.this, R.raw.small_applause);

//        storyItemManager = DataManager.init(this).getStoryItemmanager();
//        storyId = getIntent().getIntExtra("storyId", -1);
//        Log.d("checkLog", "storyid is " + storyId);
//        storyItemPojoList = storyItemManager.getStoryItemByStoryID(storyId);

        Speech.init(HomeActivity.this, getPackageName());
        Speech.getInstance().setTextToSpeechRate(Cons.speed);
//        Locale mLocale = new Locale("en_GB");
//        Speech.getInstance().setLocale(mLocale);

        //  activityHomeBinding.myLinear.removeAllViews();

        activityHomeBinding.myLinearOne.removeAllViews();

        Logger.setLogLevel(Logger.LogLevel.DEBUG);

        setLogger();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
//        } else {
//            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
//        }
        // activityHomeBinding.progress.setColors(colors);


        activityHomeBinding.scoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ScoreActivity.class);
                //  intent.putExtra("storyId",storyPojo.getId());
                startActivity(intent);
            }
        });

        activityHomeBinding.nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // stopListening();
                Log.d("checkLog", "correct words are " + getCorrectWord() + " total length are " + words.length);
//                if (getCorrectWord() > 0 && counter > 0) {
//                    updateScore(words.length, getCorrectWord());
//                    speakOne("Well done," + sessionManager.getGender() + " Lets try next line!!");
//                } else {
//                    Toast.makeText(HomeActivity.this, "No word matched!!", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        activityHomeBinding.menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HomeActivity.this.finish();
                showExitDialogue();
            }
        });

        activityHomeBinding.playPauseButton.setVisibility(View.GONE);
        activityHomeBinding.playPauseButton.setTag(0);
        activityHomeBinding.playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (Integer) view.getTag();
                if (tag == 0) {
                    activityHomeBinding.playPauseButton.setTag(1);
                    speakStop("Stopped Listening");
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.play, 0, 0, 0);
                } else {
                    activityHomeBinding.playPauseButton.setTag(0);
                    speakStartOne("Start Listening");
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause, 0, 0, 0);
                }
            }
        });

//        activityHomeBinding.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //speechRecognizer.startListening(speechRecognizerIntent);
//                try {
//                    Speech.getInstance().startListening(activityHomeBinding.progress, speechDelegate);
//                } catch (SpeechRecognitionNotAvailable exc) {
//                    Log.e("speech", "Speech recognition is not available on this device!");
//                    // You can prompt the user if he wants to install Google App to have
//                    // speech recognition, and then you can simply call:
//                    //
//                    // SpeechUtil.redirectUserToGoogleAppOnPlayStore(this);
//                    //
//                    // to redirect the user to the Google App page on Play Store
//                } catch (GoogleVoiceTypingDisabledException exc) {
//                    Log.e("speech", "Google voice typing must be enabled!");
//                }
//            }
//        });
        activityHomeBinding.startBtn.setVisibility(View.GONE);
//        activityHomeBinding.startBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String text = ((Button) view).getText().toString();
//                if (text.equalsIgnoreCase("Tap Here to Start Reading")) {
//                    if (isAudioPermission()) {
//                        ((Button) view).setText("Stop");
//                        offBeatSound();
//                        // Toast.makeText(HomeActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                String welcomeMsg="Good Luck! 100 points, Let's go!";
//                                speak(welcomeMsg);
//                            }
//                        }, 500);
//                        //SpeechRecognition speechRecognition = new SpeechRecognition(this);
//                        //initdroid();
//                        initSpeechLib();
//                        ispermissionGranted = true;
//                    } else {
//                        checkPermission();
//                    }
//                } else {
//                    ((Button) view).setText("Tap Here to Start Reading");
//                    onBeatSound();
//                    stopListening();
//                    //Toast.makeText(HomeActivity.this, "Stopped successfully!!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        Glide.with(this).
                load(sessionManager.getBlobId())
                .apply(new RequestOptions().placeholder(R.mipmap.girl_image).error(R.mipmap.girl_image))
                .into(activityHomeBinding.girlImage);
        activityHomeBinding.girlImage.setVisibility(View.VISIBLE);

        setTouchListener();

        // Speech.getInstance().setStopListeningAfterInactivity(1000000);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(HomeActivity.this)) {
//            Glide.with(this).
//                load(sessionManager.getBlobId())
//                .apply(new RequestOptions().placeholder(R.mipmap.girl_image).error(R.mipmap.girl_image))
//                .into(activityHomeBinding.girlImage);
//             activityHomeBinding.girlImage.setVisibility(View.VISIBLE);
//        }else{
//            startService(new Intent(HomeActivity.this, FloatingViewService.class));
//        }


        initConfietto();

        if (isNetworkAvailable(this)) {
            getStorydata();
        } else {
            Toast.makeText(this, "Network not available!!", Toast.LENGTH_SHORT).show();
        }

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
                HomeActivity.this.finish();
            }
        });
        TextView hintText = dialogView.findViewById(R.id.hintText);
        hintText.setText("Your score is " + score);
        AppCompatButton continueButton = dialogView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                HomeActivity.this.finish();
            }
        });
        dialog.show();
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
                    activityHomeBinding.timerText.setText(text);
                }
            }

            public void onFinish() {
                try {
                    activityHomeBinding.timerText.setText("00:00");
                    Log.d("checkLogTimer", "inside onfinish of cTimer");
                    String score = activityHomeBinding.scoreText.getText().toString();
                    showGameOverDialogue(score);
                } catch (Exception e) {
                    setResult(RESULT_OK);
                    HomeActivity.this.finish();
                }
                // activityRecordingBinding.playPauseButton.setText(" ");
                //txtTimer.setText(minutes + ":" + seconds);
            }

        };
        cTimer.start();
    }


    private void setListening() {
        if (isAudioPermission()) {
            //((Button) view).setText("Stop");
            offBeatSound();
            // Toast.makeText(HomeActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String welcomeMsg;
                    if (isRetry) {
                        welcomeMsg = "Good Luck! Let's go!";
                    } else {
                        welcomeMsg = "Good Luck! 100 points, Let's go!";
                    }
                    speak(welcomeMsg);
                }
            }, 100);
            //SpeechRecognition speechRecognition = new SpeechRecognition(this);
            //initdroid();
            initSpeechLib();
            ispermissionGranted = true;
        } else {
            checkPermission();
        }

    }

    private void setTouchListener() {
        activityHomeBinding.girlImage.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) activityHomeBinding.girlImage.getLayoutParams();
                //  Log.d("checkLog","params are leftMargin "+ params.leftMargin+" rightMargin "+ params.rightMargin+" topMargin "+ v.getTop()+" bottomMargin "+params.bottomMargin);
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
                        params.bottomMargin = height - (params.topMargin + activityHomeBinding.girlImage.getHeight());//initialY - (int) (event.getRawY() + initialTouchY);
                        Log.d("checkLog", "params after are leftMargin " + params.leftMargin + " rightMargin " + params.rightMargin + " topMargin " + params.topMargin + " bottomMargin " + params.bottomMargin);
                        activityHomeBinding.girlImage.setLayoutParams(params);
                        //  mWindowManager.updateViewLayout(activityHomeBinding.girlImage, params);
                        return true;
                }
                //activityHomeBinding.parent.invalidate();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        showExitDialogue();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().isConnectedOrConnecting();
    }


    private StoryPojo storyPojo;
    private boolean isRetry = false;

    public void getStorydata() {
        mProgressDialog.show();
        String url = Cons.BASE_URL + "stories/" + sessionManager.getActivityId() + "?student_id=" + sessionManager.getStudentId();
        Log.d("checkLog", "inside getStorydata is url " + url);
        volleyManager.makeGetCall(this, url, new CallBack() {

            @Override
            public void onSuccessResponse(JSONObject response) {
                Log.d("checkLogin", "inside on onSuccessResponse is " + response.toString());
                mProgressDialog.dismiss();
                storyItemPojoList = new ArrayList<>();
                pastItemPojoList = new ArrayList<>();
                int retryNum = 0;
                ObjectMapper mapper = new ObjectMapper();
                Log.d("checkLogin", "inside on onSuccessJsonArrayResponse is " + response.toString());
                try {
                    JSONObject resultObject = response.getJSONObject("result");
                    storyPojo = mapper.readValue(resultObject.toString(),
                            StoryPojo.class);
                    retryNum = resultObject.getInt("attempts");
//                            storyManager.createOrUpdate(transactions);
                    if (!resultObject.isNull("story_lines")) {
                        JSONArray storyItemArray = resultObject.getJSONArray("story_lines");
                        for (int j = 0; j < storyItemArray.length(); j++) {
                            JSONObject jsonObjectOne = storyItemArray.getJSONObject(j);
                            StoryItemPojo storyItemPojo = mapper.readValue(jsonObjectOne.toString(),
                                    StoryItemPojo.class);
                            //  storyItemManager.createOrUpdate(storyItemPojo);
                            if (storyItemPojo.getIs_completed() == 1) {
                                pastItemPojoList.add(storyItemPojo);
                            } else {
                                //  storyItemManager.createOrUpdate(storyItemPojo);
                                storyItemPojoList.add(storyItemPojo);
                            }
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    // okDialogue(e.toString());
                    mProgressDialog.dismiss();
                    Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {

                }

                if (storyItemPojoList.size() == 0 && pastItemPojoList.size() > 0) {
                    storyItemPojoList.addAll(pastItemPojoList);
                    pastItemPojoList.removeAll(pastItemPojoList);
                    isRetry = true;
                } else {
                    if (retryNum >= 1) {
                        isRetry = true;
                    } else {
                        isRetry = false;
                    }

                }

                Log.d("CheckLogSize", "storyItemPojoList size is " + storyItemPojoList.size());

                Log.d("CheckLogSize", "pastItemPojoList size is " + pastItemPojoList.size());

                setLinkOne();

                setPastStory();

                if (!isGame) {
                    getScoredata(true);
                }

                //setContent();

                // setAdapter(storyPojoList);
            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray response) {

            }

            @Override
            public void onVolleyError(VolleyError error) {
                mProgressDialog.dismiss();
                if (!isGame) {
                    getScoredata(true);
                }
                Toast.makeText(HomeActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTokenExpire() {

            }
        });

    }


    int points;
    int initialPoints = 0;

    public void getScoredata(boolean firstTime) {
        // mProgressDialog.show();
        String url = Cons.BASE_URL + "students/" + sessionManager.getStudentId() + "/points";
        Log.d("checkLog", "inside getStorydata is url " + url);
        volleyManager.makeGetCall(this, url, new CallBack() {

            @Override
            public void onSuccessResponse(JSONObject response) {
                Log.d("checkLogin", "inside on onSuccessResponse is " + response.toString());
                // mProgressDialog.dismiss();
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
                            activityHomeBinding.scoreText.setText("" + points);
                        }
                    }
                } catch (JSONException e) {
                    // okDialogue(e.toString());
                    Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onStart() {
        super.onStart();

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
                currentResult = new StringBuilder();
            }

            @Override
            public void OnSpeechRecognitionStopped() {
                Log.d("speechLog", " OnSpeechRecognitionStopped ");
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void OnSpeechRecognitionFinalResult(String result) {
                Log.d("speechLog", " OnSpeechRecognitionFinalResult " + result);
                Log.d("checkLog", "inside onSpeechRecognition counter " + counter);
                if (counter >= words.length) {
                    stopListening();
                    // speakOne("Wao," + " Time for reward");
                    Log.d("checkLog", "correct words are " + getCorrectWord() + " total length are " + words.length);
                    if (getCorrectWord() > 0 && counter > 0) {
                        if (getChildCount() > words.length) {
                            hideNextButton();
                        }
                        // speakOnefinal("Wao," + " You have got many stars.");
                        if (getChildCount() > words.length) {
                            hideNextButton();
                        }
                        if (isGame) {
                            updateScoreGame(words.length, getCorrectWord());
                        } else {
                            updateScore(words.length, getCorrectWord());
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "No word matched!!", Toast.LENGTH_SHORT).show();
                    }
                } else if (result != null && result.length() > 0) {
                    Log.i("speech", "result: " + result);
                    //activityHomeBinding.content.setText(result);
                    Log.d("checkLogK", "inside onSpeechRecognitionfinal Result " + result);
                    String mresult = removeDublicate(result);
                    Log.d("checkLogK", "inside onSpeechRecognitionfinal mResult " + mresult);
                    validateSpeech(mresult, false);
                    startListening();
                } else {
                    Log.d("checkLog", "inside onSpeechRecognition counter " + counter);
                    voidCounter++;
                    if (voidCounter == Cons.TIMES) {
                        addRetryView(false);
                        addNextView(true);
                    }
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void OnSpeechRecognitionCurrentResult(String result) {
                Log.d("checkLogK", " OnSpeechRecognitionCurrentResult " + result);
                if (result != null && result.length() > 0) {
                    String[] splitArray = result.split(" ");
                    Log.d("checkLogK", "length of splitArray is " + splitArray.length);
                    if (splitArray.length >= 1) {
                        String mresult = removeDublicate(result);
                        Log.d("checkLogK", "inside OnSpeechRecognitionCurrentResult mResult " + mresult);
                        validateSpeech(mresult, true);
                        currentResult.append(mresult);
                    }
                }
//                activityHomeBinding.content.setText(currentResult);
//                activityHomeBinding.content.setVisibility(View.GONE);
                //  validateSpeech(result, true);
            }

            @Override
            public void OnSpeechRecognitionError(int i, String s) {
                Log.d("speechLog", " OnSpeechRecognitionError " + s + " i" + i);
                if (counter >= words.length) {
                    stopListening();
                    speakOne("Well done," + "I think you might get a prize");
                    Log.d("checkLog", "correct words are " + getCorrectWord() + " total length are " + words.length);
                    if (getCorrectWord() > 0 && counter > 0) {
                        if (getChildCount() > words.length) {
                            hideNextButton();
                        }
                        // speakOnefinal("Wao ,"+ "you have got many stars.");
                        if (getChildCount() > words.length) {
                            hideNextButton();
                        }
                        if (isGame) {
                            updateScoreGame(words.length, getCorrectWord());
                        } else {
                            updateScore(words.length, getCorrectWord());
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "No word matched!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    voidCounter++;
                    if (voidCounter == Cons.TIMES) {
                        addRetryView(false);
                        addNextView(false);
                    }
                    if (!HomeActivity.this.isFinishing()) {
                        stopListening();
                        startListening();
                    }
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


    private boolean getAllDone() {
        boolean flag = true;
        for (int j = 0; j < cArray.length; j++) {
            if (!cArray[j]) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private int getCorrectWord() {
        int count = 0;
        for (int i = 0; i < cArray.length; i++) {
            if (cArray[i]) {
                ++count;
            }
        }
        return count;
    }


    private void speak(String msg) {
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
                // Toast.makeText(HomeActivity.this, "Completed..", Toast.LENGTH_SHORT).show();
                //speechRecognizer.startListening(speechRecognizerIntent);
                //startListening();
//                if(isWritePermission()) {
//                    changeSetting();
//                }else{
//                    checkWritePermission();
//                }
                //   checkWritePermission();
                startListening();

            }

            @Override
            public void onError() {
                Log.i("speech", "speech error");
                Toast.makeText(HomeActivity.this, "Error..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void speakOne(String msg) {
        //Speech.getInstance().setAudioStream(0);
//        Speech.getInstance().setTextToSpeechRate(Cons.speed);
//        Locale locale = new Locale("hi_IN");
//        Speech.getInstance().setLocale(locale);
        Speech.getInstance().say(msg, new TextToSpeechCallback() {
            @Override
            public void onStart() {
                stopListening();
                setAvatarAnimation(4000, 1000);
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


    private void speakOnefinal(String msg) {
        //Speech.getInstance().setAudioStream(0);
        Speech.getInstance().setTextToSpeechRate(Cons.speed);
        Locale locale = new Locale("hi_IN");
        Speech.getInstance().setLocale(locale);
        Speech.getInstance().say(msg, new TextToSpeechCallback() {
            @Override
            public void onStart() {
                // onBeatSound();
                // Log.i("speech", "speech started");
                //Toast.makeText(HomeActivity.this, "Start..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted() {
                if (getChildCount() > words.length) {
                    hideNextButton();
                }
                if (isGame) {
                    updateScoreGame(words.length, getCorrectWord());
                } else {
                    updateScore(words.length, getCorrectWord());
                }
            }

            @Override
            public void onError() {
                Log.i("speech", "speech error");
                // Toast.makeText(HomeActivity.this, "Error..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String convertingToCurrency(Double value) {
        DecimalFormat formatter = new DecimalFormat("#,##,##,##0.00");
        String result = formatter.format(value);
        return result;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopListening();
    }

    private void stopListening() {
        //Speech.getInstance().stopListening();
        try {
            if (speechRecognition != null && !this.isFinishing())
                speechRecognition.stopSpeechRecognition();
        } catch (IllegalStateException e) {

        }
    }

    private void offBeatSound() {
        Log.d("checkLogSound", "inside offbeatSound..");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, AudioManager.FLAG_VIBRATE);
        } else {
            mAudioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        }

    }

    private void onBeatSound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_VIBRATE);
        } else {
            mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
        }
    }


    private void startListening() {
        Log.d("checkLog", " inside startListening ");
        try {
            if (speechRecognition != null)
                speechRecognition.startSpeechRecognition();
        } catch (IllegalStateException e) {

        }
    }


    private boolean isAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private boolean isWritePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SECURE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void setLogger() {
        Logger.setLoggerDelegate(new Logger.LoggerDelegate() {
            @Override
            public void error(String tag, String message) {
                //your own implementation here
            }

            @Override
            public void error(String tag, String message, Throwable exception) {
                //your own implementation here
            }

            @Override
            public void debug(String tag, String message) {
                //your own implementation here
            }

            @Override
            public void info(String tag, String message) {
                //your own implementation here
            }
        });

        //getStorydata();
    }

//    SpeechRecognizer speechRecognizer;
//    Intent speechRecognizerIntent;
//    private void initSpeech(){
//        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
//        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        speechRecognizer.setRecognitionListener(this);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        // stopService(new Intent(HomeActivity.this, FloatingViewService.class));
        //Speech.getInstance().shutdown();
        // droidSpeech.closeDroidSpeechOperations();
        //speechRecognition.stopSpeechRecognition();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }

    private void checkWritePermission() {
        Log.d("checkLogReq", " inside checkWritePermission");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SECURE_SETTINGS}, WRITERequestCode);
//        }
//        final Intent intent = new Intent(Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
//        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RecordAudioRequestCode:
                if (isAudioPermission()) {
                    setListening();
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    checkPermission();
                }
                break;
            case WRITERequestCode:
                if (isWritePermission()) {
                    offBeatSound();
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    checkWritePermission();
                }
                break;
        }
    }

//    SpeechDelegate speechDelegate = new SpeechDelegate() {
//        @Override
//        public void onStartOfSpeech() {
//            // Log.i("speech", "speech recognition is now active");
//        }
//
//        @Override
//        public void onSpeechRmsChanged(float value) {
//            // Log.d("speech", "rms is now: " + value);
//        }
//
//        @Override
//        public void onSpeechPartialResults(List<String> results) {
//            StringBuilder str = new StringBuilder();
//            for (String res : results) {
//                str.append(res).append(" ");
//            }
//            // Log.i("speech", "partial result: " + str.toString().trim());
//
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        @Override
//        public void onSpeechResult(String result) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    //  speak("");
//                    startListening();
//                }
//            }, 500);
////            Speech.getInstance().shutdown();
////            Speech.init(HomeActivity.this, getPackageName());
//            Log.i("speech", "result: " + result);
//            activityHomeBinding.content.setText(result);
//            validateSpeech(result, true);
//            //String welcomeMsg="Hello ,"+ sessionManager.getGender();
//            // onBeatSound();
//            //startListening();
//        }
//    };


    public int counter = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void validateSpeech(String speech, boolean antim) {
        Log.d("checkLog", " speech is " + speech);
//        if(counter+1>words.length){
//            addNextView(false);
//        }
        if (speech != null && speech.length() > 0) {
            String[] speechArray = speech.split("\\s+");
            //   textArray= new TextView[words.length];
            for (int i = 0; i < speechArray.length; i++) {
                // You may want to check for a non-word character before blindly
                // performing a replacement
                // It may also be necessary to adjust the character class
                // synchronized (Integer.valueOf(counter)) {
                if (counter < words.length) {
                    String cWord = speechArray[i].replaceAll("[^\\w]", "");
                    cWord = cWord.replaceAll("[^a-zA-Z0-9]", "");
                    String indexWord = words[counter].replaceAll("[^\\w]", "");
                    Log.d("checkLog", " inside validate indexword  " + indexWord + " cword " + cWord);
                    indexWord = indexWord.replaceAll("[^a-zA-Z0-9]", "");
                    Log.d("checkLog", " inside validate indexword  " + indexWord + " cword " + cWord);
                    if (indexWord != null && indexWord.isEmpty()) {
                        Log.d("checkLog", "inside empty string");
                        ++counter;
                    } else if (cWord.equalsIgnoreCase(indexWord)) {
                        ifCall(indexWord, antim);
                    } else {
                        if (cWord.length() == indexWord.length()) {
                            int perc = appUtil.checkSameLength(indexWord, cWord);
                            if (cWord.length() <= 3) {
                                if (perc >= Cons.percT) {
                                    ifCall(indexWord, antim);
                                }
                            } else if (perc >= Cons.percF) {
                                ifCall(indexWord, antim);
                            }
                        }
//                        else if(cWord.length()>indexWord.length()){
//                            int perc=appUtil.checkMatchSubString(cWord,indexWord);
//                            if(cWord.length()<=3){
//                                if(perc>=Cons.percT){
//                                    ifCall(indexWord,antim);
//                                }
//                            }else if(perc>=Cons.percF){
//                                ifCall(indexWord,antim);
//                            }
//                        }else if(cWord.length()<indexWord.length()){
//                            int perc=appUtil.checkMatchSubString(indexWord,cWord);
//                            if(cWord.length()<=3){
//                                if(perc>=Cons.percT){
//                                    ifCall(indexWord,antim);
//                                }
//                            }else if(perc>=Cons.percF){
//                                ifCall(indexWord,antim);
//                            }
//                        }
                        else {
                            elseCall(cWord, antim);
                        }

                        if (counter > words.length) {
                            celebrationCall(indexWord, antim, cWord);
                            if (getChildCount() > words.length) {
                                hideNextButton();
                            }
                        } else {
                            Log.d("checkLog", " inside else of else  loop counter " + counter);
//                    String msg="Say "+ words[counter];
//                    speakOne(msg);
                        }
                    }
                } else {
                    String cWord = speechArray[i].replaceAll("[^\\w]", "");
                    cWord = cWord.replaceAll("[^a-zA-Z0-9]", "");
                    int index = -1;
                    Log.d("checkLog", " inside else match " + counter);
                    int counterOne = counter;
                    int counterTwo = counter - 1;
                    int length = counterOne + 5 < words.length ? counterOne + 5 : counterOne + 4 < words.length ? counterOne + 4 : counterOne + 3 < words.length ? counterOne + 3 : counterOne + 2 < words.length ? counterOne + 2 : counterOne + 1 < words.length ? counterOne + 1 : counterTwo;
                    //int length = counterOne + 2 < words.length ? counterOne + 2 : counterOne + 1 < words.length ? counterOne + 1 : counterTwo;
                    Log.d("checkLog", " inside validate indexword  " + cWord);
                    Log.d("checkLog", " inside else validate length  " + length + "counter is " + counter);
                    if (length < words.length) {
                        String indexWord = words[length].replaceAll("[^\\w]", "");
                        Log.d("checkLog", " inside validate indexword  " + indexWord + " cword " + cWord);
                        indexWord = indexWord.replaceAll("[^a-zA-Z0-9]", "");
                        Log.d("checkLog", " inside validate indexword  " + indexWord + " cword " + cWord);
                        for (int j = 0; j < length; j++) {
                            if (indexWord.toLowerCase().equalsIgnoreCase(cWord.toLowerCase()) && !cArray[j]) {
                                index = j;
                                break;
                            }
                        }
                    }
                    if (index >= 0) {
                        voidCounter = 0;
                        cArray[index] = true;
                        textArray[index].setVisibility(View.VISIBLE);
                        textArray[index].setTextColor(getResources().getColor(R.color.storycolor));
                        textArray[index].setFocusable(true);
                        textArray[index].invalidate();
                        if (index > counter) {
                            for (int k = counter; k < index; k++) {
                                textArray[k].setVisibility(View.VISIBLE);
                                textArray[k].setTextColor(Color.RED);
                                textArray[k].setFocusable(true);
                                textArray[k].invalidate();
                            }
                            counter = index;
                        }
//                        String msg = "Weldone " + sessionManager.getGender();
//                        speakOne(msg);
                        ++counter;

                        if (counter > words.length) {
                            int indexOne = -1;
                            //  Log.d("checkLog", " inside validate indexword  " + indexWord + " counter word " + words[counter]);
                            for (int j = 0; j < cArray.length; j++) {
                                if (!cArray[j]) {
                                    indexOne = j;
                                    break;
                                }
                            }
                            if (antim) {
                                if (indexOne >= 0) {
                                    String msg = "Say " + words[indexOne];
                                    // speakOne(msg);
                                } else {
                                    // stopListening();
                                    if (counter >= words.length) {
                                        Random r = new Random();
                                        String msg = Cons.applaud_lines[r.nextInt(Cons.applaud_lines.length)];
                                        speakOne(msg);
                                    }
                                }
                            }
                        }
                    } else if (counter < words.length) {
//                        textArray[counter].setVisibility(View.VISIBLE);
//                        textArray[counter].setTextColor(Color.RED);
//                        textArray[counter].setFocusable(true);
                        if (antim) {
                            String msg = "Say " + words[counter];
                            //  speakOne(msg);
                        }
                        // ++counter;
                        if (getChildCount() > words.length) {
                            hideNextButton();
                        } else {
                            voidCounter++;
                            if (voidCounter == Cons.TIMES) {
                                addRetryView(false);
                                addNextView(true);
                            }
                        }
                        Log.d("checkLog", " inside else if match outer " + counter);
                    } else {
                        // match completed
                        Log.d("checkLog", " inside else else outer match ");
                        // counter=0;
                        //Speech.getInstance().stopListening();
                        if (getChildCount() > words.length) {
                            hideNextButton();
                        } else {
                            voidCounter++;
                            if (voidCounter == Cons.TIMES) {
                                addRetryView(false);
                                addNextView(true);
                            }
                        }
                    }
                    Log.d("checkLog", " inside if match " + counter);
                }
            }
        }
        // }

    }

    private void celebrationCall(String indexWord, boolean antim, String cWord) {
        int index = -1;
        Log.d("checkLog", " inside validate indexword  " + indexWord + " counter word " + cWord);
        for (int j = 0; j < cArray.length; j++) {
            if (!cArray[j]) {
                index = j;
                break;
            }
        }
        if (index >= 0) {
            if (antim) {
                String msg = "Say " + words[index];
                //speakOne(msg);
            }
        } else {
            // stopListening();
            if (counter >= words.length) {
                if (antim) {
                    Random r = new Random();
                    String msg = Cons.applaud_lines[r.nextInt(Cons.applaud_lines.length)];
                    speakOne(msg);
                }
            }
        }
    }

    private void elseCall(String cWord, boolean antim) {
        Log.d("checkLog", " inside else match counter " + counter);
        int index = -1;
        int counterOne = counter;
        int counterTwo = counter - 1;
        int length = counterOne + 5 < words.length ? counterOne + 5 : counterOne + 4 < words.length ? counterOne + 4 : counterOne + 3 < words.length ? counterOne + 3 : counterOne + 2 < words.length ? counterOne + 2 : counterOne + 1 < words.length ? counterOne + 1 : counterTwo;
        //int length = counterOne + 2 < words.length ? counterOne + 2 : counterOne + 1 < words.length ? counterOne + 1 : counterTwo;
        Log.d("checkLog", " inside validate indexword  " + cWord);
        Log.d("checkLog", " inside else validate length  " + length + "counter is " + counter);
        if (length < words.length) {
            for (int j = 0; j < length; j++) {
                if (words[j].equalsIgnoreCase(cWord) && !cArray[j]) {
                    index = j;
                    break;
                }
            }
        }
        if (index >= 0) {
            voidCounter = 0;
            cArray[index] = true;
//                            ((TextView) activityHomeBinding.myLinear.getChildAt(index)).setVisibility(View.VISIBLE);
//                            ((TextView) activityHomeBinding.myLinear.getChildAt(index)).setTextColor(Color.BLUE);
//                            ((TextView) activityHomeBinding.myLinear.getChildAt(index)).setFocusable(true);
            textArray[index].setVisibility(View.VISIBLE);
            textArray[index].setTextColor(getResources().getColor(R.color.storycolor));
            textArray[index].setFocusable(true);
            textArray[index].invalidate();
            if (index > counter) {
                for (int k = counter; k < index; k++) {
                    textArray[k].setVisibility(View.VISIBLE);
                    textArray[k].setTextColor(Color.RED);
                    textArray[k].setFocusable(true);
                    textArray[k].invalidate();
                }
                counter = index;
            }
//                        String msg = "Weldone " + sessionManager.getGender();
//                        speakOne(msg);
            ++counter;
        } else if (counter < words.length) {
            textArray[counter].setVisibility(View.VISIBLE);
            textArray[counter].setTextColor(Color.RED);
            textArray[counter].setFocusable(true);
            textArray[counter].invalidate();
            if (antim) {
                String msg = "Say " + words[counter];
                // speakOne(msg);
            }
            // ++counter;
            Log.d("checkLog", " inside if match " + counter);
        } else {
            // match completed
            Log.d("checkLog", " inside else match ");
            // counter=0;
            //Speech.getInstance().stopListening();
            voidCounter++;
            if (voidCounter == Cons.TIMES) {
                addRetryView(false);
                addNextView(true);
            }
        }
    }

    private void ifCall(String indexWord, boolean antim) {
        Log.d("checkLog", " inside match ");
        voidCounter = 0;
        if (!isRetry) {
            initAnimation(activityHomeBinding.startImage.getX() / 6, 400, activityHomeBinding.startImage.getY() / 4, -800);
            // initAnimation(textArray[counter].getX(),activityHomeBinding.scoreText.getX(),textArray[counter].getY(),activityHomeBinding.scoreText.getY());
            runAnimation();
        }
        if (counter < words.length) {
            cArray[counter] = true;
            Log.d("checkLog", " inside validate indexword  " + indexWord + " counter word " + words[counter]);
            textArray[counter].setVisibility(View.VISIBLE);
            textArray[counter].setTextColor(getResources().getColor(R.color.storycolor));
            textArray[counter].setFocusable(true);
            textArray[counter].invalidate();
            ++counter;
            if (counter >= words.length) {
                int index = -1;
                for (int j = 0; j < cArray.length; j++) {
                    if (!cArray[j]) {
                        index = j;
                        break;
                    }
                }
                if (index >= 0) {
                    String msg = "Say " + words[index];
                    if (antim) {
                        //  speakOne(msg);
                    }
                } else {
                    // stopListening();
                    if (counter > words.length) {
                        if (antim) {
                            Random r = new Random();
                            String msg = Cons.applaud_lines[r.nextInt(Cons.applaud_lines.length)];
                            speakOne(msg);
                        }
                    }
                }
            }
            Log.d("checkLog", " inside if match " + counter);
        } else {
            // match completed
            Log.d("checkLog", " inside else match ");
            // counter=0;
            //Speech.getInstance().stopListening();
            int index = -1;
            Log.d("checkLog", " inside validate indexword  " + indexWord + " counter word " + words[counter]);
            for (int j = 0; j < cArray.length; j++) {
                if (!cArray[j]) {
                    index = j;
                    break;
                }
            }
            if (index >= 0) {
                if (antim) {
                    String msg = "Say " + words[index];
                    // speakOne(msg);
                }
            } else {
                // stopListening();
                if (counter >= words.length) {
                    if (antim) {
                        Random r = new Random();
                        String msg = Cons.applaud_lines[r.nextInt(Cons.applaud_lines.length)];
                        speakOne(msg);
                    }
                }
            }
        }
    }

    private void playFile() {
        try {
            mPlayer.start();
        } catch (IllegalStateException ex) {

        }
    }

    private void playFileOne() {
        try {
            mPlayerOne.start();
        } catch (IllegalStateException ex) {

        }
    }

    private int matchWord(String s1, String s2) {
        s1 = s1.replace(" ", "");
        s1 = s1.replace(",", "");

        s2 = s2.replace(" ", "");

        char[] a = s1.toCharArray();

        char[] b = s2.toCharArray();
        int counter = 0;
        int j = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == b[j]) {
                ++counter;
            }
            j++;
        }

        int percent = (int) counter * 100 / a.length;
        Log.d("checkLog", " percent is " + percent);

        return percent;
    }


//    private void startSpeech(String intruct) {
//        Speech.getInstance().say(intruct, new TextToSpeechCallback() {
//            @Override
//            public void onStart() {
//                Log.i("speech", "speech started");
//            }
//
//            @Override
//            public void onCompleted() {
//                Log.i("speech", "speech completed");
//                Toast.makeText(HomeActivity.this, "initialized..", Toast.LENGTH_SHORT).show();
//                //speechRecognizer.startListening(speechRecognizerIntent);
//                try {
//                    Speech.getInstance().startListening(activityHomeBinding.progress, speechDelegate);
//                } catch (SpeechRecognitionNotAvailable exc) {
//                    Log.e("speech", "Speech recognition is not available on this device!");
//                    // You can prompt the user if he wants to install Google App to have
//                    // speech recognition, and then you can simply call:
//                    //
//                    // SpeechUtil.redirectUserToGoogleAppOnPlayStore(this);
//                    //
//                    // to redirect the user to the Google App page on Play Store
//                } catch (GoogleVoiceTypingDisabledException exc) {
//                    Log.e("speech", "Google voice typing must be enabled!");
//                }
//            }
//
//            @Override
//            public void onError() {
//                Log.i("speech", "speech error");
//            }
//        });
//    }

//    @Override
//    public void onReadyForSpeech(Bundle bundle) {
//        Log.d("checkLog","onReadyForSpeech"+bundle.toString());
//        //Toast.makeText(this, "onReadyForSpeech is "+bundle.toString(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onBeginningOfSpeech() {
//        Log.d("checkLog","onBeginningOfSpeech");
//    }
//
//    @Override
//    public void onRmsChanged(float v) {
//
//    }
//
//    @Override
//    public void onBufferReceived(byte[] bytes) {
//
//    }
//
//    @Override
//    public void onEndOfSpeech() {
//        Toast.makeText(this, "onEndOfSpeech is ", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onError(int i) {
//        Toast.makeText(this, "Error is "+i, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onResults(Bundle bundle) {
//        Log.d("checkLog","onResults "+bundle.toString());
//        //Toast.makeText(this, "onResults is "+bundle.toString(), Toast.LENGTH_SHORT).show();
//        ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//        activityHomeBinding.text.setText(data.get(0));
//        if(data.get(0).contains(text)){
//            Speech.getInstance().say("Superb");
//            activityHomeBinding.content.setTextColor(Color.BLUE);
//            activityHomeBinding.content.setCompoundDrawables(null,null,getResources().getDrawable(R.drawable.ic_baseline_done_all_24),null);
//        }else{
//            activityHomeBinding.content.setCompoundDrawables(null,null,getResources().getDrawable(R.drawable.ic_baseline_close_24),null);
//            Speech.getInstance().say("Try Again");
//            activityHomeBinding.content.setTextColor(Color.RED);
//        }
//    }
//
//    @Override
//    public void onPartialResults(Bundle bundle) {
//        Log.d("checkLog","onPartialResults "+bundle.toString());
//        //Toast.makeText(this, "onPartialResults is "+bundle.toString(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onEvent(int i, Bundle bundle) {
//        Log.d("checkLog","onEvent "+bundle.toString());
//       // Toast.makeText(this, "onEvent is "+bundle.toString(), Toast.LENGTH_SHORT).show();
//    }

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
                            initAnimation(activityHomeBinding.startImage.getX() / 6, 400, activityHomeBinding.startImage.getY() / 4, -800);
                            // initAnimation(textArray[counter].getX(),activityHomeBinding.scoreText.getX(),textArray[counter].getY(),activityHomeBinding.scoreText.getY());
                            runAnimation();
                        }
                        setNextStory();
                    }
                }, 1500);
            } else {
                showConfettiTop();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isRetry) {
                            initAnimation(activityHomeBinding.startImage.getX() / 6, 400, activityHomeBinding.startImage.getY() / 4, -800);
                            // initAnimation(textArray[counter].getX(),activityHomeBinding.scoreText.getX(),textArray[counter].getY(),activityHomeBinding.scoreText.getY());
                            runAnimation();
                        }
                        setNextStory();
//                                float score = (float) getCorrectWord() * 5 / (float) words.length;
//                                Log.d("checkLog", "score is " + score);
//                                showStarDialogue(convertingToCurrency(Double.parseDouble(String.valueOf(score))));
                    }
                }, 1500);
            }
        } else {
            activityHomeBinding.animationView.setVisibility(View.VISIBLE);
            //activityHomeBinding.animationView.loop(true);
            int n = r.nextInt(resLottie.length);
            Log.d("checkLogLottie", "inside else of lottie " + n);
            // activityHomeBinding.animationView.setAnimationFromUrl(resLottieOne[0]);
            activityHomeBinding.animationView.setAnimation(resLottie[n]);
            activityHomeBinding.animationView.playAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activityHomeBinding.animationView.pauseAnimation();
                    activityHomeBinding.animationView.setVisibility(View.GONE);
                    if (!isRetry) {
                        initAnimation(activityHomeBinding.startImage.getX() / 6, 400, activityHomeBinding.startImage.getY() / 4, -800);
                        // initAnimation(textArray[counter].getX(),activityHomeBinding.scoreText.getX(),textArray[counter].getY(),activityHomeBinding.scoreText.getY());
                        runAnimation();
                    }
                    setNextStory();
                }
            }, 2000);
        }
        playFileOne();
    }

    public void updateActivityStatus() {
        mProgressDialog.show();
       // StoryItemPojo storyItemPojo = storyItemPojoList.get(counter-1);
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

                String msg = "You have completed this activity, please choose another one";
                speakFial(msg);

            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray response) {

            }

            @Override
            public void onVolleyError(VolleyError error) {
                mProgressDialog.dismiss();
                String msg = "You have completed this activity, please choose another one";
                speakFial(msg);
            }

            @Override
            public void onTokenExpire() {

            }
        });

    }

    public void updateScore(int totalWord, int correctWord) {
//        mProgressDialog.setMessage(getResources().getString(R.string.please_wait_score));
        mProgressDialog.show();
        // showConfetto();
        StoryItemPojo storyItemPojo = storyItemPojoList.get(indexCount);
        JSONObject scoreObject = new JSONObject();
        //String token= FirebaseInstanceId.getInstance().getToken();
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("total_words", totalWord);
            paramObject.put("correct_words", correctWord);
            paramObject.put("student_id", sessionManager.getStudentId());
            paramObject.put("unit_id",unit_id);
            paramObject.put("chapter_id", chapter_id);
            paramObject.put("sub_activity_id", storyItemPojo.getId());
            // paramObject.put("lineId", sessionManager.getStudentId());
            paramObject.put("notes", scoreObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String accessToken=getPreferences(MODE_PRIVATE).getString("accesstoken", null);//sessionManager.getToken();
        Log.d("checkLog", "inside updateStory data is " + paramObject.toString());
        Map<String, String> header = new DeviceInfoUtils().getHeaderInfo(sessionManager.getUserId());
        Log.d("checkLog", "inside updateStory header is url " + header.toString());
        String url = Cons.BASE_URL + "stories/" + sessionManager.getActivityId() + "/record";
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
//                        activityHomeBinding.scoreText.setText("" + score);
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

                //  chooseAnim();
            }

            @Override
            public void onTokenExpire() {

            }
        });

    }


    public void updateScoreGame(int totalWord, int correctWord) {
//        mProgressDialog.setMessage(getResources().getString(R.string.please_wait_score));
        mProgressDialog.show();
        // showConfetto();
        StoryItemPojo storyItemPojo = storyItemPojoList.get(indexCount);
        JSONObject scoreObject = new JSONObject();
        //String token= FirebaseInstanceId.getInstance().getToken();
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("total_words", totalWord);
            paramObject.put("correct_words", correctWord);
            paramObject.put("unit_id",unit_id);
            paramObject.put("chapter_id", chapter_id);
            paramObject.put("GameCode", sessionManager.getGameIdOne());
            paramObject.put("student_id", sessionManager.getStudentId());
            paramObject.put("sub_activity_id", storyItemPojo.getId());
            // paramObject.put("lineId", sessionManager.getStudentId());
            paramObject.put("notes", scoreObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String accessToken=getPreferences(MODE_PRIVATE).getString("accesstoken", null);//sessionManager.getToken();
        Log.d("checkLog", "inside updateStory data is " + paramObject.toString());
        Map<String, String> header = new DeviceInfoUtils().getHeaderInfo(sessionManager.getUserId());
        Log.d("checkLog", "inside updateStory header is url " + header.toString());
        String url = Cons.BASE_URL + "timergame/stories/" + sessionManager.getActivityId() + "/record";
        Log.d("checkLog", "inside getStorydata is url " + url);
        Log.d("checkLog", "inside getStorydata is paramobject  " + paramObject.toString());
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
                        activityHomeBinding.scoreText.setText("" + score);
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
                Log.d("checkLogScore", "error is " + error.getMessage());
                mProgressDialog.dismiss();
            }

            @Override
            public void onTokenExpire() {

            }
        });

    }

    private void setNextStory() {
        ++indexCount;
        Log.d("checkLogCount", "count is " + indexCount);
        voidCounter = 0;
        //speak("");
        startListening();
        counter = 0;
        View ppView = ((HorizontalScrollView) pView).getChildAt(0);
        TextView ppTextView = (TextView) ((LinearLayout) ppView).getChildAt(0);
        ppTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checked_two, 0, 0, 0);
        if (indexCount < storyItemPojoList.size()) {
            activityHomeBinding.startBtn.setVisibility(View.GONE);
            setContent();
//            String msg = "Let's try next line.";
//            speakOne(msg);
        } else {
            //send scores to server
            activityHomeBinding.startBtn.setVisibility(View.GONE);
            setProgressOne(pastItemPojoList.size() + indexCount, pastItemPojoList.size() + storyItemPojoList.size());
            stopListening();
            updateActivityStatus();
            // Toast.makeText(this, "You have completed this activity, please choose another one.!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void speakFial(String msg) {
        Speech.getInstance().setTextToSpeechRate(Cons.speed);
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
                    Intent intent = new Intent();
                    intent.putExtra("isCompleted", true);
                    intent.putExtra("type", 1);
                    setResult(RESULT_CANCELED, intent);
                    HomeActivity.this.finish();
                } else {
                    if (!isRetry) {
                        int gotScore = points - initialPoints;
                        if (gotScore > 0) {
                            showStarDialogue(String.valueOf(gotScore));
                        } else {
                            setResult(RESULT_OK);
                            HomeActivity.this.finish();
                        }
                    } else {
                        setResult(RESULT_OK);
                        HomeActivity.this.finish();
                    }
                }
            }

            @Override
            public void onError() {
                Log.i("speech", "speech error");
                // Toast.makeText(HomeActivity.this, "Error..", Toast.LENGTH_SHORT).show();
                if (!isRetry) {
                    int gotScore = points - initialPoints;
                    showStarDialogue(String.valueOf(gotScore));
                } else {
                    setResult(RESULT_OK);
                    HomeActivity.this.finish();
                }
            }
        });
    }

    ConfettoGenerator confettoGenerator;

    private void initConfietto() {
        final List<Bitmap> allPossibleConfetti = Utils.generateConfettiBitmaps(new int[]{Color.BLUE}, 20 /* size */);
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
        final int containerMiddleX = activityHomeBinding.parent.getWidth() / 2;
        final int containerMiddleY = activityHomeBinding.parent.getHeight() / 2;
        final ConfettiSource confettiSource = new ConfettiSource(containerMiddleX, containerMiddleY);

        new ConfettiManager(HomeActivity.this, confettoGenerator, confettiSource, activityHomeBinding.parent)
                .setEmissionDuration(1000)
                .setEmissionRate(100)
                .setVelocityX(20, 10)
                .setVelocityY(100)
                .setRotationalVelocity(180, 180)
                .animate();
    }


    private void showConfettiTop() {
        activityHomeBinding.viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                .setPosition(-20f, activityHomeBinding.viewKonfetti.getWidth() + 50f, -20f, -50f)
                .streamFor(100, 2000L);
    }

    private void showConfettiCenter() {
        activityHomeBinding.viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                .setPosition(activityHomeBinding.viewKonfetti.getX() + activityHomeBinding.viewKonfetti.getWidth() / 2, activityHomeBinding.viewKonfetti.getY() + activityHomeBinding.viewKonfetti.getHeight() / 3)
                .burst(200);
    }


    public void showStarDialogue(String score) {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_star, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);
        ImageView imageView = dialogView.findViewById(R.id.backImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setResult(RESULT_OK);
                HomeActivity.this.finish();
            }
        });
        TextView hintText = dialogView.findViewById(R.id.hintText);
        hintText.setText("Congratulations , You have won " + score + " stars");
        RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.simpleRatingBar);
        ratingBar.setRating(5);
        // hintText.setText(hint);
        AppCompatButton continueButton = dialogView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setNextStory();
                dialog.dismiss();
                setResult(RESULT_OK);
                HomeActivity.this.finish();
            }
        });
        dialog.show();
    }

    public void showExitDialogue() {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_exit, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);
        ImageView imageView = dialogView.findViewById(R.id.backImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startListening();
            }
        });
        stopListening();
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
                onBeatSound();
                stopListening();
                setResult(RESULT_CANCELED);
                HomeActivity.this.finish();

            }
        });
        dialog.show();
    }

    public void runAnimation() {
        Log.d("checkLog", "inside runAnimation ");
//        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //   View view = mInflater.inflate(R.layout.star_item, null, false);
        // activityHomeBinding.startImage.setAnimation(animation);
        activityHomeBinding.startImage.setVisibility(View.VISIBLE);
        activityHomeBinding.startImage.startAnimation(animation);
        activityHomeBinding.startImage.invalidate();
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
            activityHomeBinding.startImage.setVisibility(View.GONE);
            activityHomeBinding.startImage.clearAnimation();
            String text = activityHomeBinding.scoreText.getText().toString();
            Log.d("checkAnimLog", "inside onAnimationEnd previous score " + text + " current score " + points);
            if (points > 0 && points >= Integer.parseInt(text)) {
                animateTextViewOne(Integer.parseInt(text), points, activityHomeBinding.scoreText);
            }
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
                    activityHomeBinding.girlImage.setImageResource(resAvatar[imageCounter]);
                } else {
                    imageCounter = 0;
                    activityHomeBinding.girlImage.setImageResource(resAvatar[3]);
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