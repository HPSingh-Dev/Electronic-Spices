package com.conductor.apni.littleleappwa;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Amitabh on 10/20/2016.
 */

/*This Class is used to initialize the Views, set OnClicks for all the activities*/

 public  abstract class BaseActivity extends AppCompatActivity {

//        public abstract void initViews();
//        public abstract void initData();
//        public abstract void setListeners();

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    }

