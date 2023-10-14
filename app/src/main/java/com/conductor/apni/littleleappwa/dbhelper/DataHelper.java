package com.conductor.apni.littleleappwa.dbhelper;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.conductor.apni.littleleappwa.R;
import com.conductor.apni.littleleappwa.data.AnswerPojo;
import com.conductor.apni.littleleappwa.data.StoryItemPojo;
import com.conductor.apni.littleleappwa.data.StoryPojo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by Saipro on 28-04-2017.
 */

public class DataHelper extends OrmLiteSqliteOpenHelper {

    private ConcurrentHashMap<Class<?>, Dao> concurrentDaoHashMap = null;
    private Context mContext;
    public static final Class<?>[] TABLES = {
            StoryPojo.class,
            StoryItemPojo.class,
            AnswerPojo.class,
    };

    public static final Class<?>[] TABLESONE = {
            StoryPojo.class,
            StoryItemPojo.class,
            AnswerPojo.class,
    };


    public DataHelper(Context context) {
        super(context, context.getString(R.string.db_name), null, context.getResources().getInteger(
                R.integer.db_version));
        mContext = context;
        concurrentDaoHashMap = new ConcurrentHashMap<>();
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        DbHelperUtils.onCreate(connectionSource, TABLES);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        DbHelperUtils.onOpen(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        DbHelperUtils.onUpgrade(database, connectionSource, TABLES);

        if (oldVersion < 31) {
           // database.execSQL(DbHelperUtils.DROP_QUERY + "Story");
            database.execSQL(DbHelperUtils.DROP_QUERY + "StoryItem");
            //database.execSQL(DbHelperUtils.DROP_QUERY + "city");
            try {
                //TableUtils.createTable(connectionSource, SellerDashboardPojo.class);
          //      TableUtils.createTable(connectionSource, StoryPojo.class);
                TableUtils.createTable(connectionSource, StoryItemPojo.class);
           //     TableUtils.createTable(connectionSource, AnswerPojo.class);
//                TableUtils.createTable(connectionSource, ProductOne.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

        }

    }

    public void clearTable(Class clazz) {
        DbHelperUtils.clearTable(connectionSource, clazz);
    }

    public void clearTables() {
        DbHelperUtils.clearTables(connectionSource, TABLES);
    }

    public void clearTablesOne() {
        DbHelperUtils.clearTables(connectionSource, TABLESONE);
    }

    public <T> Dao getDaoByClass(Class<T> clazz) {
        try {
            if (isInMap(clazz)) {
                return getFromMap(clazz);
            } else {
                return addToMap(clazz, getDao(clazz));
            }
        } catch (java.sql.SQLException e) {
            ErrorUtils.logError(e);
        }
        return null;
    }

    private <T> Dao addToMap(Class<T> clazz, Dao dao) {
        concurrentDaoHashMap.put(clazz, dao);
       // Log.w("Leena","addToMap====="+concurrentDaoHashMap.get(clazz));
        return concurrentDaoHashMap.put(clazz, dao);
    }

    private <T> boolean isInMap(Class<T> clazz) {
        return concurrentDaoHashMap.contains(clazz);
    }

    public <T> Dao getFromMap(Class<T> clazz) {
       // Log.w("Leena","getFromMap====="+concurrentDaoHashMap.get(clazz));
        return concurrentDaoHashMap.get(clazz);
    }



    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }


}
