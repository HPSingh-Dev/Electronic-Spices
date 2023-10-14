package com.conductor.apni.littleleappwa.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.suddenh4x.ratingdialog.AppRating;
import com.suddenh4x.ratingdialog.preferences.RatingThreshold;

//import com.suddenh4x.ratingdialog.AppRating;
//import com.suddenh4x.ratingdialog.preferences.RatingThreshold;

public class AppRater {

    private  AppCompatActivity activity;

    private final static String APP_TITLE = "Rareview Fashion";// App Name
    private final static String APP_PNAME = "com.rareview.fashion.lifestyle";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public AppRater(AppCompatActivity activity) {
        this.activity = activity;
    }

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("RiderPWA", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
               // showRateDialog(mContext, editor);
            }
        }

      //  showRateDialog(mContext, editor);

        editor.commit();
    }

//    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
//        final Dialog dialog = new Dialog(mContext);
//        dialog.setTitle("Rate " + APP_TITLE);
//
//        LinearLayout ll = new LinearLayout(mContext);
//        ll.setOrientation(LinearLayout.VERTICAL);
//
//        TextView tv = new TextView(mContext);
//        tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
//        Resources r = mContext.getResources();
//        int px = (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                350,
//                r.getDisplayMetrics());
//
//        int pxmarging = (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                20,
//                r.getDisplayMetrics());
//        int pxmargingVer = (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                20,
//                r.getDisplayMetrics());
//        Log.d("checkLog","px value is "+px);
//        tv.setWidth(px);
//        tv.setTextSize(18);
//        tv.setTypeface(Typeface.DEFAULT_BOLD);
//       // tv.setTextColor(mContext.getColor(R.color.black));
//        tv.setPadding(pxmarging, pxmargingVer, pxmarging, pxmargingVer);
//        ll.addView(tv);
//
//        Button b1 = new Button(mContext);
//        b1.setText("Rate " + APP_TITLE);
//        b1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
//                dialog.dismiss();
//            }
//        });
//        ll.addView(b1);
//
//        Button b2 = new Button(mContext);
//        b2.setText("Remind me later");
//        b2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        ll.addView(b2);
//
//        Button b3 = new Button(mContext);
//        b3.setText("No, thanks");
//        b3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (editor != null) {
//                    editor.putBoolean("dontshowagain", true);
//                    editor.commit();
//                }
//                dialog.dismiss();
//            }
//        });
//        ll.addView(b3);
//
//        dialog.setContentView(ll);
//        dialog.show();
//    }

    public void showAppRating(){
       new AppRating.Builder(activity)
                .setMinimumLaunchTimes(2)
                .setMinimumDays(2)
                .setMinimumLaunchTimesToShowAgain(5)
                .setMinimumDaysToShowAgain(3)
                .setRatingThreshold(RatingThreshold.FOUR)
                .showIfMeetsConditions();
    }

    public void showGoogleInAppAppRating(){
        new AppRating.Builder(activity)
                .setMinimumLaunchTimes(5)
                .setMinimumDays(3)
                .setMinimumLaunchTimesToShowAgain(5)
                .setMinimumDaysToShowAgain(3)
                .setRatingThreshold(RatingThreshold.FOUR)
                .useGoogleInAppReview();
    }


    public void showImmediateReview(){
        new AppRating.Builder(activity)
                .setMinimumLaunchTimes(2)
                .setMinimumDays(2)
                .setMinimumLaunchTimesToShowAgain(5)
                .setMinimumDaysToShowAgain(3)
                .setRatingThreshold(RatingThreshold.FOUR)
                .showNow();
    }

}
