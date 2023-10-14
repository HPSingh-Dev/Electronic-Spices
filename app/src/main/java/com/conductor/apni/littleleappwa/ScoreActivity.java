package com.conductor.apni.littleleappwa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.conductor.apni.littleleappwa.adapter.NotificationAdapter;
import com.conductor.apni.littleleappwa.adapter.ScoreAdapter;
import com.conductor.apni.littleleappwa.data.ScorePojo;
import com.conductor.apni.littleleappwa.data.StoryItemPojo;
import com.conductor.apni.littleleappwa.data.StoryPojo;
import com.conductor.apni.littleleappwa.dbmanager.DataManager;
import com.conductor.apni.littleleappwa.dbmanager.StoryItemManager;
import com.conductor.apni.littleleappwa.dbmanager.StoryManager;
import com.conductor.apni.littleleappwa.services.Cons;
import com.conductor.apni.littleleappwa.services.VolleyManager;
import com.conductor.apni.littleleappwa.services.callback.CallBack;
import com.conductor.apni.littleleappwa.utils.DeviceInfoUtils;
import com.conductor.apni.littleleappwa.utils.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.snackbar.Snackbar;

import net.gotev.speech.Speech;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ScoreAdapter seriesAdapter;
    private RelativeLayout parent_layout_signup;
    private SessionManager sessionManager;
    private VolleyManager volleyManager;
    private StoryManager storyManager;
    private StoryItemManager storyItemManager;
    private LinearLayout errorLinear;
    private TextView bugText, retryText,scoreText;
    private ImageView menuView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        View rootView = findViewById(R.id.parent_layout);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);

        storyManager = DataManager.init(this).getStorymanager();
        storyItemManager = DataManager.init(this).getStoryItemmanager();

        sessionManager = new SessionManager(this);
        volleyManager = new VolleyManager();
        mRecyclerView = rootView.findViewById(R.id.mrecyclerview);
        parent_layout_signup = rootView.findViewById(R.id.parent_layout);
        errorLinear = rootView.findViewById(R.id.errorLinear);
        // mProgressBar=rootView.findViewById(R.id.mProgressBar);
        bugText = rootView.findViewById(R.id.bugText);
        retryText = rootView.findViewById(R.id.retryText);
        scoreText= rootView.findViewById(R.id.scoreText);
        menuView = rootView.findViewById(R.id.menuView);
        //menuView.setImageDrawable(Util.setVectorForPreLollipop(R.drawable.ic_arrow_back_white_24dp, this));
        menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScoreActivity.this.finish();
            }
        });


        if (isNetworkAvailable(this)) {
            getScore();
        } else {
//            mList=nManager.getAllNotifications();
//            if (mList.size() > 0) {
//                setAdapter(mList);
//            } else {
//                mRecyclerView.setVisibility(View.GONE);
//                mProgressBar.setVisibility(View.GONE);
//                errorLinear.setVisibility(View.VISIBLE);
//                bugText.setText("Notifications not found!!");
//                retryText.setText("Go Back");
//                // Toast.makeText(this, "Notification not found", Toast.LENGTH_SHORT).show();
//            }
            Toast.makeText(this, "Network not available!!", Toast.LENGTH_SHORT).show();
        }

    }


    public void showSnackBar(String message) {
        final Snackbar snackBar = Snackbar.make(parent_layout_signup, message, Snackbar.LENGTH_LONG);
        snackBar.setAction("CLOSE", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackBar.dismiss();
            }
        }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void setAdapter(List<ScorePojo> mList,String scorePercentage) {
        scoreText.setText(scorePercentage);
        if (mList != null && mList.size() > 0) {
            seriesAdapter = new ScoreAdapter(this, mList);
            mRecyclerView.setAdapter(seriesAdapter);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(llm);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
        }

    }

    ProgressDialog mProgressDialog;
    private List<ScorePojo> scorePojoList;
    private String scorePercentage;
    public void getScore() {
        mProgressDialog.show();
        Log.d("checkLog", "inside getStorydata token is " + sessionManager.getUserId());
        Map<String, String> header = new DeviceInfoUtils().getHeaderInfo(sessionManager.getUserId());
        Log.d("checkLog", "inside getStorydata header is url " + header.toString());
        String url = Cons.BASE_URL + "/api/score?student_id=" + sessionManager.getStudentId();
        Log.d("checkLog", "inside getStorydata is url " + url);
        volleyManager.makeGetCall(this, url, header, new CallBack() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                Log.d("checkLogin", "inside on onSuccessResponse is " + response.toString());
                mProgressDialog.dismiss();
                ObjectMapper mapper = new ObjectMapper();
                scorePojoList = new ArrayList<>();
                Log.d("checkLogin", "inside on onSuccessJsonArrayResponse is " + response.toString());
                try {
                    scorePercentage=response.getString("score_percentage");
                    JSONArray dataArray = response.getJSONArray("activity_data");
                    if (dataArray != null && dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            ScorePojo transactions = mapper.readValue(jsonObject.toString(),
                                    ScorePojo.class);
                            scorePojoList.add(transactions);
                        }
                    }
                } catch (JSONException e) {
                    // okDialogue(e.toString());
                    mProgressDialog.dismiss();
                    Toast.makeText(ScoreActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {

                }

                setAdapter(scorePojoList,scorePercentage);
            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray response) {
                Log.d("checkLogin", "inside on onSuccessResponse is " + response.toString());
                mProgressDialog.dismiss();
                ObjectMapper mapper = new ObjectMapper();
            }

            @Override
            public void onVolleyError(VolleyError error) {
                mProgressDialog.dismiss();
                Log.d("checkLog"," error is "+error.getMessage());
                Toast.makeText(ScoreActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTokenExpire() {

            }
        });

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

    void okDialogue(final String msg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        mRecyclerView.setVisibility(View.GONE);
                        //  mProgressBar.setVisibility(View.GONE);
                        errorLinear.setVisibility(View.VISIBLE);
                        bugText.setText(msg);
                        retryText.setText("Go Back");
                        dialog.dismiss();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

}
