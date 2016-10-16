package com.unimelb.feelinglucky.snapsheet.Story;

import android.content.ClipData;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.unimelb.feelinglucky.snapsheet.Discover.DaoMaster;
import com.unimelb.feelinglucky.snapsheet.Discover.DaoSession;
import com.unimelb.feelinglucky.snapsheet.Discover.DiscoverItem;
import com.unimelb.feelinglucky.snapsheet.Discover.DiscoverItemDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by yuhaoliu on 28/08/16.
 */
public class DBManager {
    private final static String dbName = "david_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }


    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class)
        {
            if (mInstance == null)
                {
                    mInstance = new DBManager(context);
                }
        }
        }
            return mInstance;
    }

    public void insertStoryList(List<Story> events) {
        if (events == null || events.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        StoryDao storyDao = daoSession.getStoryDao();
        storyDao.insertInTx(events);
    }

    public List<Story> getAllStories() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        StoryDao storyDao = daoSession.getStoryDao();
        QueryBuilder<Story> qb = storyDao.queryBuilder();
        List<Story> list = qb.list();
        return list;
    }

    public void insertDiscoveryItemList(List<DiscoverItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DiscoverItemDao itemDao = daoSession.getDiscoverItemDao();
        itemDao.insertInTx(items);
    }

    public List<DiscoverItem> getAllItems() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DiscoverItemDao itemDao = daoSession.getDiscoverItemDao();
        QueryBuilder<DiscoverItem> qb = itemDao.queryBuilder();
        List<DiscoverItem> list = qb.list();
        return list;
    }

    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    public SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    public void clearItems(){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DiscoverItemDao itemDao = daoSession.getDiscoverItemDao();
        itemDao.deleteAll();
    }

    public void clearDB(){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        StoryDao storyDao = daoSession.getStoryDao();
        storyDao.deleteAll();
        DiscoverItemDao itemDao = daoSession.getDiscoverItemDao();
        itemDao.deleteAll();
    }
}
