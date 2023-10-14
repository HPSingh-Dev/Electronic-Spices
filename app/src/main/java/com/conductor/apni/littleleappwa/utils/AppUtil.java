package com.conductor.apni.littleleappwa.utils;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.conductor.apni.littleleappwa.HomeActivity;
import com.conductor.apni.littleleappwa.R;
import com.conductor.apni.littleleappwa.services.Cons;

import java.util.Random;

public class AppUtil {

    HomeActivity homeActivity;
    public AppUtil(HomeActivity activity) {
        this.homeActivity=activity;
    }


    public void validateSpeech(String speech, boolean antim,HomeActivity homeActivity) {
        Log.d("checkLog", " speech is " + speech);
        if(homeActivity.counter+4>homeActivity.words.length){
            homeActivity.addNextView(false);
        }
        if (speech != null && speech.length() > 0) {
            String[] speechArray = speech.split("\\s+");
            //   textArray= new TextView[words.length];
            for (int i = 0; i < speechArray.length; i++) {
                // You may want to check for a non-word character before blindly
                // performing a replacement
                // It may also be necessary to adjust the character class
                if (homeActivity.counter < homeActivity.words.length) {
                    String cWord = speechArray[i].replaceAll("[^\\w]", "");
                    String indexWord = homeActivity.words[homeActivity.counter].replaceAll("[^\\w]", "");
                    Log.d("checkLog", " inside validate indexword  " + indexWord + " cword " + cWord);
                    if (cWord.equalsIgnoreCase(indexWord)) {
                        Log.d("checkLog", " inside match ");
                        homeActivity.voidCounter=0;
                        homeActivity.initAnimation(homeActivity.activityHomeBinding.startImage.getX() / 6, 400, homeActivity.activityHomeBinding.startImage.getY() / 4, -800);
                        // initAnimation(textArray[counter].getX(),activityHomeBinding.scoreText.getX(),textArray[counter].getY(),activityHomeBinding.scoreText.getY());
                        homeActivity.runAnimation();
                        if (homeActivity.counter < homeActivity.words.length) {
                            homeActivity.cArray[homeActivity.counter] = true;
                            Log.d("checkLog", " inside validate indexword  " + indexWord + " counter word " + homeActivity.words[homeActivity.counter]);
                            homeActivity.textArray[homeActivity.counter].setVisibility(View.VISIBLE);
                            homeActivity.textArray[homeActivity.counter].setTextColor(homeActivity.getResources().getColor(R.color.storycolor));
                            homeActivity.textArray[homeActivity.counter].setFocusable(true);
                            ++homeActivity.counter;
                            if (homeActivity.counter >= homeActivity.words.length) {
                                int index = -1;
                                for (int j = 0; j < homeActivity.cArray.length; j++) {
                                    if (!homeActivity.cArray[j]) {
                                        index = j;
                                        break;
                                    }
                                }
                                if (index >= 0) {
                                    String msg = "Say " + homeActivity.words[index];
                                    if (antim) {
                                        //  speakOne(msg);
                                    }
                                } else {
                                    // stopListening();
                                    if (homeActivity.counter > homeActivity.words.length) {
                                        if (antim) {
                                            Random r = new Random();
                                            String msg = Cons.applaud_lines[r.nextInt(Cons.applaud_lines.length)];
                                            homeActivity.speakOne(msg);
                                        }
                                    }
                                }
                            }
                            Log.d("checkLog", " inside if match " + homeActivity.counter);
                        } else {
                            // match completed
                            Log.d("checkLog", " inside else match ");
                            // counter=0;
                            //Speech.getInstance().stopListening();
                            int index = -1;
                            Log.d("checkLog", " inside validate indexword  " + indexWord + " counter word " + homeActivity.words[homeActivity.counter]);
                            for (int j = 0; j < homeActivity.cArray.length; j++) {
                                if (!homeActivity.cArray[j]) {
                                    index = j;
                                    break;
                                }
                            }
                            if (index >= 0) {
                                if (antim) {
                                    String msg = "Say " + homeActivity.words[index];
                                    // speakOne(msg);
                                }
                            } else {
                                // stopListening();
                                if (homeActivity.counter >= homeActivity.words.length) {
                                    if (antim) {
                                        Random r = new Random();
                                        String msg = Cons.applaud_lines[r.nextInt(Cons.applaud_lines.length)];
                                        homeActivity.speakOne(msg);
                                    }
                                }
                            }
                        }
                    } else {
                        Log.d("checkLog", " inside else match ");
                        int index = -1;
                        int length = homeActivity.counter + 5 < homeActivity.words.length ? homeActivity.counter + 5 : homeActivity.counter + 4 < homeActivity.words.length ? homeActivity.counter + 4 : homeActivity.counter + 3 < homeActivity.words.length ? homeActivity.counter + 3 : homeActivity.counter + 2 < homeActivity.words.length ? homeActivity.counter + 2 : homeActivity.counter + 1 < homeActivity.words.length ? homeActivity.counter + 1 : --homeActivity.counter;
                        Log.d("checkLog", " inside validate indexword  " + speechArray[i]);
                        for (int j = 0; j < length; j++) {
                            if (homeActivity.words[j].equalsIgnoreCase(speechArray[i]) && !homeActivity.cArray[j]) {
                                index = j;
                                break;
                            }
                        }
                        if (index >= 0) {
                            homeActivity.cArray[index] = true;
//                            ((TextView) activityHomeBinding.myLinear.getChildAt(index)).setVisibility(View.VISIBLE);
//                            ((TextView) activityHomeBinding.myLinear.getChildAt(index)).setTextColor(Color.BLUE);
//                            ((TextView) activityHomeBinding.myLinear.getChildAt(index)).setFocusable(true);
                            homeActivity.textArray[index].setVisibility(View.VISIBLE);
                            homeActivity.textArray[index].setTextColor(homeActivity.getResources().getColor(R.color.storycolor));
                            homeActivity.textArray[index].setFocusable(true);
                            if (index > homeActivity.counter) {
                                for (int k = homeActivity.counter; k < index; k++) {
                                    homeActivity.textArray[k].setVisibility(View.VISIBLE);
                                    homeActivity.textArray[k].setTextColor(Color.RED);
                                    homeActivity.textArray[k].setFocusable(true);
                                }
                                homeActivity.counter = index;
                            }
//                        String msg = "Weldone " + sessionManager.getGender();
//                        speakOne(msg);
                            ++homeActivity.counter;
                        } else if (homeActivity.counter < homeActivity.words.length) {
                            homeActivity.textArray[homeActivity.counter].setVisibility(View.VISIBLE);
                            homeActivity.textArray[homeActivity.counter].setTextColor(Color.RED);
                            homeActivity.textArray[homeActivity.counter].setFocusable(true);
                            if (antim) {
                                String msg = "Say " + homeActivity.words[homeActivity.counter];
                                // speakOne(msg);
                            }
                            // ++counter;
                            Log.d("checkLog", " inside if match " + homeActivity.counter);
                        } else {
                            // match completed
                            Log.d("checkLog", " inside else match ");
                            // counter=0;
                            //Speech.getInstance().stopListening();
                            homeActivity.voidCounter++;
                            if(homeActivity.voidCounter==10){
                                homeActivity.addNextView(true);
                            }
                        }
                    }

                    if (homeActivity.counter > homeActivity.words.length) {
                        int index = -1;
                        Log.d("checkLog", " inside validate indexword  " + indexWord + " counter word " + homeActivity.words[homeActivity.counter]);
                        for (int j = 0; j < homeActivity.cArray.length; j++) {
                            if (!homeActivity.cArray[j]) {
                                index = j;
                                break;
                            }
                        }
                        if (index >= 0) {
                            if (antim) {
                                String msg = "Say " + homeActivity.words[index];
                                //speakOne(msg);
                            }
                        } else {
                            // stopListening();
                            if (homeActivity.counter >= homeActivity.words.length) {
                                if (antim) {
                                    Random r = new Random();
                                    String msg = Cons.applaud_lines[r.nextInt(Cons.applaud_lines.length)];
                                    homeActivity.speakOne(msg);

                                }
                            }
                        }
                    } else {
                        Log.d("checkLog", " inside else of else  loop counter " + homeActivity.counter);
//                    String msg="Say "+ words[counter];
//                    speakOne(msg);
                    }

                    Log.d("checkLog", " inside if match " + homeActivity.counter);

                } else {
                    int index = -1;
                    int length = homeActivity.counter + 5 < homeActivity.words.length ? homeActivity.counter + 5 : homeActivity.counter + 4 < homeActivity.words.length ? homeActivity.counter + 4 : homeActivity.counter + 3 < homeActivity.words.length ? homeActivity.counter + 3 : homeActivity.counter + 2 < homeActivity.words.length ? homeActivity.counter + 2 : homeActivity.counter + 1 < homeActivity.words.length ? homeActivity.counter + 1 : --homeActivity.counter;
                    Log.d("checkLog", " inside outer else  actual word  " + speechArray[i]);
                    Log.d("checkLog", " inside validate indexword  " + speechArray[i] + " lenght is " + length);
                    for (int j = 0; j < length; j++) {
                        if (homeActivity.words[j].equalsIgnoreCase(speechArray[i]) && !homeActivity.cArray[j]) {
                            index = j;
                            break;
                        }
                    }
                    if (index >= 0) {
                        homeActivity.cArray[index] = true;
                        homeActivity.textArray[index].setVisibility(View.VISIBLE);
                        homeActivity.textArray[index].setTextColor(homeActivity.getResources().getColor(R.color.storycolor));
                        homeActivity.textArray[index].setFocusable(true);
                        if (index > homeActivity.counter) {
                            for (int k = homeActivity.counter; k < index; k++) {
                                homeActivity.textArray[k].setVisibility(View.VISIBLE);
                                homeActivity.textArray[k].setTextColor(Color.RED);
                                homeActivity.textArray[k].setFocusable(true);
                            }
                            homeActivity.counter = index;
                        }
//                        String msg = "Weldone " + sessionManager.getGender();
//                        speakOne(msg);
                        ++homeActivity.counter;

                        if (homeActivity.counter > homeActivity.words.length) {
                            int indexOne = -1;
                            //  Log.d("checkLog", " inside validate indexword  " + indexWord + " counter word " + words[counter]);
                            for (int j = 0; j < homeActivity.cArray.length; j++) {
                                if (!homeActivity.cArray[j]) {
                                    indexOne = j;
                                    break;
                                }
                            }
                            if (antim) {
                                if (indexOne >= 0) {
                                    String msg = "Say " + homeActivity.words[indexOne];
                                    // speakOne(msg);
                                } else {
                                    // stopListening();
                                    if (homeActivity.counter >= homeActivity.words.length) {
                                        Random r = new Random();
                                        String msg = Cons.applaud_lines[r.nextInt(Cons.applaud_lines.length)];
                                        homeActivity.speakOne(msg);
                                    }
                                }
                            }
                        }
                    } else if (homeActivity.counter < homeActivity.words.length) {
//                        textArray[counter].setVisibility(View.VISIBLE);
//                        textArray[counter].setTextColor(Color.RED);
//                        textArray[counter].setFocusable(true);
                        if (antim) {
                            String msg = "Say " + homeActivity.words[homeActivity.counter];
                            //  speakOne(msg);
                        }
                        // ++counter;
                        homeActivity.voidCounter++;
                        if(homeActivity.voidCounter==10){
                            homeActivity.addNextView(true);
                        }
                        Log.d("checkLog", " inside else if match outer " + homeActivity.counter);
                    } else {
                        // match completed
                        Log.d("checkLog", " inside else else outer match ");
                        // counter=0;
                        //Speech.getInstance().stopListening();
                        homeActivity.voidCounter++;
                        if(homeActivity.voidCounter==10){
                            homeActivity.addNextView(true);
                        }
                    }
                    Log.d("checkLog", " inside if match " + homeActivity.counter);
                }
            }
        }

    }

    public int checkSameLength(String oWord, String rWord){
        int counter=0;
        char[] first  = oWord.toLowerCase().toCharArray();
        char[] second = rWord.toLowerCase().toCharArray();
        int minLength = Math.min(first.length, second.length);
        for(int i=0; i<oWord.length();i++){
            if (first[i] != second[i]) {
                counter++;
            }
        }
        int perc=counter*100/oWord.length();
        Log.d("checkUtil","perc is "+perc);
        return perc;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int checkMatchSubString(String oWord, String rWord){
        int m = oWord.length();
        int n = rWord.length();

        int match=LCSubStr(oWord.toCharArray(),
                rWord.toCharArray(), m,
                n);
        int perc=match*100/oWord.length();
        Log.d("checkUtil","perc is "+perc);
        return perc;
    }


    /*
       Returns length of longest common substring
       of X[0..m-1] and Y[0..n-1]
    */
    @RequiresApi(api = Build.VERSION_CODES.N)
    static int LCSubStr(char X[], char Y[],
                        int m, int n) {
        // Create a table to store
        // lengths of longest common
        // suffixes of substrings.
        // Note that LCSuff[i][j]
        // contains length of longest
        // common suffix of
        // X[0..i-1] and Y[0..j-1].
        // The first row and first
        // column entries have no
        // logical meaning, they are
        // used only for simplicity of program
        int LCStuff[][] = new int[m + 1][n + 1];

        // To store length of the longest
        // common substring
        int result = 0;

        // Following steps build
        // LCSuff[m+1][n+1] in bottom up fashion
        for (int i = 0; i <= m; i++)
        {
            for (int j = 0; j <= n; j++)
            {
                if (i == 0 || j == 0)
                    LCStuff[i][j] = 0;
                else if (X[i - 1] == Y[j - 1])
                {
                    LCStuff[i][j]
                            = LCStuff[i - 1][j - 1] + 1;
                    result = Integer.max(result,
                            LCStuff[i][j]);
                }
                else
                    LCStuff[i][j] = 0;
            }
        }
        return result;
    }



}
