package com.example.easy_book.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.easy_book.bean.Collection;


import java.util.ArrayList;
import java.util.List;

public class CollectionDBDao {

    /** 数据库名称 */
    private static final String DB_NAME = "easy_book.db";
    /** 数据表名称 */
    private static final String TABLE_NAME = "collection_info";
    /** 数据库版本 */
    private static final int DB_VERSION = 1;

    private SQLiteDatabase mDatabase;
    private Context mContext;
    private cDBOpenHelper mDbOpenHelper;

    public CollectionDBDao(Context context) {
        mContext = context;
    }


    private static class cDBOpenHelper extends SQLiteOpenHelper{

        public cDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            final String sqlStr = "create table if not exists " + TABLE_NAME + " (" +
                    "uid" + " text not null," +
                    "pid" + " Integer not null unique," +
                    "title" + "text," +
                    "label" + "text," +
                    "price" + "float," +
                    "description" + "text," +
                    "picture" + "blob," +
                    "primary key(uid, pid));";
            db.execSQL(sqlStr);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            final String sqlStr = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sqlStr);
            onCreate(db);
        }
    }

    /**
     * 打开商品数据库
     */
    public void openDataBase(){
        mDbOpenHelper = new cDBOpenHelper(mContext, DB_NAME, null, DB_VERSION);
        try {
            mDatabase = mDbOpenHelper.getWritableDatabase();//获取可写数据库
        } catch (SQLException e) {
            mDatabase = mDbOpenHelper.getReadableDatabase();//获取只读数据库
        }

    }

    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }


    public boolean AddCollection(Collection collection){

        ContentValues values = new ContentValues();

        values.put("uid", collection.getUid());
        values.put("pid", collection.getPid());
        values.put("title", collection.getTitle());
        values.put("label", collection.getLabel());
        values.put("price", collection.getPrice());
        values.put("description", collection.getDescription());
        values.put("picture", collection.getPicture());

        mDatabase.insert(TABLE_NAME, null, values);

        values.clear();
        return true;
    }

    public List<Collection> readMyCollections(String username) {
        List<Collection> myCollections = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("select * from collection_info where uid=?", new String[]{username});
        if (cursor.moveToFirst()) {
            do {

                String uid = cursor.getString(cursor.getColumnIndex("uid"));
                Integer pid = cursor.getInt(cursor.getColumnIndex("pid"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String label = cursor.getString(cursor.getColumnIndex("label"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));


//                System.out.println(pid + title + label);

                Collection collection = new Collection();

                collection.setUid(uid);
                collection.setPid(pid);
                collection.setTitle(title);
                collection.setLabel(label);
                collection.setPrice(price);
                collection.setDescription(description);
                collection.setPicture(picture);


                myCollections.add(collection);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return myCollections;
    }


    public void deleteCollectionby_uid_pid(String uid, String  pid) {

        mDatabase.delete(TABLE_NAME, "uid=? and pid=?", new String[]{uid, pid});

    }


    //该商品是否被该用户已经收藏
    public boolean isinCollection(String uid, String pid){

        boolean flag;

        Cursor cursor = mDatabase.rawQuery("select * from collection_info where uid=? and pid=?", new String[]{uid, pid});

        flag = cursor.moveToFirst();

        cursor.close();

        return flag;
    }

}
