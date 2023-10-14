package com.conductor.apni.littleleappwa.dbmanager;

import android.util.Log;

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

public class StoryManager extends BaseManager<StoryPojo> {

    public StoryManager(Dao<StoryPojo,Long> dao) {
        super(dao, StoryManager.class.getSimpleName());
        Log.w("Leena","Dao userDetailManager===="+dao);
    }

    public List<StoryPojo> getAllCategory() {
        List<StoryPojo> myTransactions = new ArrayList<>();
        QueryBuilder<StoryPojo, Long> queryBuilder = dao.queryBuilder().orderBy(StoryPojo.Column._ID, false);
        try {
            myTransactions = dao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myTransactions;
    }

    public void deleteCategoryByProduct(StoryPojo data){
        try {
            dao.delete(data);
        } catch (SQLException e) {
            ErrorUtils.logError(e);
        }
    }

    public void addCategory(StoryPojo message) {
        try {
            dao.create(message);
            Log.w("checkHPS", "inside add message transactional message====");
        } catch (SQLException e) {
            ErrorUtils.logError(e);
        }
    }

    public void deleteStory(StoryPojo adCategory){
        try {
            dao.delete(adCategory);
        } catch (SQLException e) {
            ErrorUtils.logError(e);
        }
    }

}
