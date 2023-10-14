package com.conductor.apni.littleleappwa.dbmanager;

import android.util.Log;

import com.conductor.apni.littleleappwa.data.AnswerPojo;
import com.conductor.apni.littleleappwa.data.QItemPojo;
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

public class AnswerManager extends BaseManager<AnswerPojo> {

    public AnswerManager(Dao<AnswerPojo,Long> dao) {
        super(dao, AnswerManager.class.getSimpleName());
        Log.w("Leena","Dao userDetailManager===="+dao);
    }


    public List<AnswerPojo> getStoryItemByActivityID(int activityId) {
        List<AnswerPojo> myTransactions = new ArrayList<>();
        QueryBuilder<AnswerPojo, Long> queryBuilder = dao.queryBuilder().orderBy(AnswerPojo.Column._ID, false);
        try {
            myTransactions = dao.query(queryBuilder.where().eq(AnswerPojo.Column.STORYID,activityId).prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myTransactions;
    }

    public List<AnswerPojo> getAllCategory() {
        List<AnswerPojo> myTransactions = new ArrayList<>();
        QueryBuilder<AnswerPojo, Long> queryBuilder = dao.queryBuilder().orderBy(AnswerPojo.Column._ID, false);
        try {
            myTransactions = dao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myTransactions;
    }

    public void deleteCategoryByProduct(AnswerPojo data){
        try {
            dao.delete(data);
        } catch (SQLException e) {
            ErrorUtils.logError(e);
        }
    }

    public void addCategory(AnswerPojo message) {
        try {
            dao.create(message);
            Log.w("checkHPS", "inside add message transactional message====");
        } catch (SQLException e) {
            ErrorUtils.logError(e);
        }
    }

    public void deleteByActivityId(int activityId){
        List<AnswerPojo> answerPojoList=getStoryItemByActivityID(activityId);
        for(int i=0; i<answerPojoList.size();i++) {
            AnswerPojo answerPojo=answerPojoList.get(i);
            try {
                dao.delete(answerPojo);
            } catch (SQLException e) {
                ErrorUtils.logError(e);
            }
        }
    }

    public void deleteStory(AnswerPojo adCategory){
        try {
            dao.delete(adCategory);
        } catch (SQLException e) {
            ErrorUtils.logError(e);
        }
    }

}
