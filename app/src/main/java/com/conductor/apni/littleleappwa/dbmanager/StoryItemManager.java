package com.conductor.apni.littleleappwa.dbmanager;

import android.util.Log;

import com.conductor.apni.littleleappwa.data.StoryItemPojo;
import com.conductor.apni.littleleappwa.data.StoryPojo;
import com.conductor.apni.littleleappwa.dbhelper.ErrorUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saipro on 28-04-2017.
 */

public class StoryItemManager extends BaseManager<StoryItemPojo> {

    public StoryItemManager(Dao<StoryItemPojo,Long> dao) {
        super(dao, StoryItemManager.class.getSimpleName());
        Log.w("Leena","Dao userDetailManager===="+dao);
    }

    public List<StoryItemPojo> getStoryItemByStoryID(int story_id) {
        List<StoryItemPojo> myTransactions = new ArrayList<>();
        QueryBuilder<StoryItemPojo, Long> queryBuilder = dao.queryBuilder().orderBy(StoryItemPojo.Column.STORYID, false);
        try {
            myTransactions = dao.query(queryBuilder.where().eq(StoryItemPojo.Column.STORYID,story_id).prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myTransactions;
    }


    public List<StoryItemPojo> getAllStoryItem() {
        List<StoryItemPojo> myTransactions = new ArrayList<>();
        QueryBuilder<StoryItemPojo, Long> queryBuilder = dao.queryBuilder().orderBy(StoryItemPojo.Column._ID, false);
        try {
            myTransactions = dao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myTransactions;
    }

    public void deleteCategoryByProduct(StoryItemPojo data){
        try {
            dao.delete(data);
        } catch (SQLException e) {
            ErrorUtils.logError(e);
        }
    }

    public void addCategory(StoryItemPojo message) {
        try {
            dao.create(message);
            Log.w("checkHPS", "inside add message transactional message====");
        } catch (SQLException e) {
            ErrorUtils.logError(e);
        }
    }

    public void deleteStoryItem(StoryItemPojo adCategory){
        try {
            dao.delete(adCategory);
        } catch (SQLException e) {
            ErrorUtils.logError(e);
        }
    }

}
