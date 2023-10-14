package com.conductor.apni.littleleappwa.dbmanager;

import android.content.Context;

import com.conductor.apni.littleleappwa.data.AnswerPojo;
import com.conductor.apni.littleleappwa.data.StoryItemPojo;
import com.conductor.apni.littleleappwa.data.StoryPojo;
import com.conductor.apni.littleleappwa.dbhelper.DataHelper;
/**
 * Created by Saipro on 28-04-2017.
 */

public class DataManager {
    private static DataManager instance;
    private DataHelper dataHelper;
    private StoryManager storyManager;
    private AnswerManager answerManager;
    private StoryItemManager storyItemManager;

    public DataManager(Context context) {
        dataHelper = new DataHelper(context);
    }

    public StoryManager getStorymanager() {
        if (storyManager== null) {
            storyManager = new StoryManager(getDataHelper().getDaoByClass(StoryPojo.class));
        }
        return storyManager;
    }

    public AnswerManager getAnswermanager() {
        if (answerManager== null) {
            answerManager = new AnswerManager(getDataHelper().getDaoByClass(AnswerPojo.class));
        }
        return answerManager;
    }

    public StoryItemManager getStoryItemmanager() {
        if (storyItemManager== null) {
            storyItemManager = new StoryItemManager(getDataHelper().getDaoByClass(StoryItemPojo.class));
        }
        return storyItemManager;
    }

    public static DataManager init(Context context) {
        if (instance== null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    public static DataManager getInstance() {
        return instance;
    }

    private DataHelper getDataHelper() {
        return dataHelper;
    }

    public void clearAllTables() {
        dataHelper.clearTables();
    }

    public void clearAllTablesOne() {
        dataHelper.clearTablesOne();
    }

}
