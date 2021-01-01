package com.example.easy_book.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;

import com.example.easy_book.bean.Msg;

import java.util.ArrayList;
import java.util.List;

public class MsgDBDao {

    /** 数据库名称 */
    private static final String DB_NAME = "easy_book.db";
    /** 数据表名称 */
    private static final String TABLE_NAME = "msg_info";
    /** 数据库版本 */
    private static final int DB_VERSION = 1;

    private SQLiteDatabase mDatabase;
    private Context mContext;
    /** 数据库打开帮助类 */
    private msgDBOpenHelper msgDbOpenHelper;

    public MsgDBDao(Context context) {
        mContext = context;
    }


    /**
     * 数据表打开帮助类
     */
    private static class msgDBOpenHelper extends SQLiteOpenHelper {

        public msgDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //自动检测是否存在数据库文件，若不存在则调用onCreate函数
        @Override
        public void onCreate(SQLiteDatabase db) {
            final String sqlStr = "create table if not exists " + TABLE_NAME + " (" +
                    "msg_a" + " text not null unique, " +
                    "msg_b" + " text not null unique, " +
                    "content" + " text not null,"+
                    "timestamp"+ " long default (datetime('now', 'localtime')),"+
                    "primary key(msg_from, msg_to));";
            db.execSQL(sqlStr);
        }

        //检测版本号，若有更高版本，则调用onUpgrade函数
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            final String sqlStr = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sqlStr);
            onCreate(db);
        }
    }


    /**
     * 打开msg数据库
     */
    public void openDataBase(){
        msgDbOpenHelper = new msgDBOpenHelper(mContext, DB_NAME, null, DB_VERSION);
        try {
            mDatabase = msgDbOpenHelper.getWritableDatabase();//获取可写数据库
        } catch (SQLException e) {
            mDatabase = msgDbOpenHelper.getReadableDatabase();//获取只读数据库
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



    public boolean AddMsg(Msg msg){

        ContentValues values = new ContentValues();

        values.put("msg_a", msg.getMsg_a());
        values.put("msg_a_nickname", msg.getMsg_a_nickname());
        values.put("msg_b", msg.getMsg_b());
        values.put("msg_b_nickname", msg.getMsg_b_nickname());
        values.put("content", msg.getContent());
        values.put("type", msg.getType());


        mDatabase.insert(TABLE_NAME, null, values);

        values.clear();
        return true;
    }


    /**
     * @param a 读取a用户与b用户之间的所有聊天信息
     * @param b
     * @return
     */
    public List<Msg> readAllMsg(String a, String b){

        List<Msg> allMsg = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("select * from msg_info where msg_a=? and msg_b=? order by timestamp", new String[]{a, b});

        if (cursor.moveToFirst()){
            do {

                String msg_a = cursor.getString(cursor.getColumnIndex("msg_a"));
                String msg_a_nickname = cursor.getString(cursor.getColumnIndex("msg_a_nickname"));
                String msg_b = cursor.getString(cursor.getColumnIndex("msg_b"));
                String msg_b_nickname = cursor.getString(cursor.getColumnIndex("msg_b_nickname"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String type = cursor.getString(cursor.getColumnIndex("type"));

                Msg msg = new Msg();
                msg.setMsg_a(msg_a);
                msg.setMsg_a_nickname(msg_a_nickname);
                msg.setMsg_b(msg_b);
                msg.setMsg_b_nickname(msg_b_nickname);
                msg.setContent(content);
                msg.setType(type);


                allMsg.add(msg);

            }while (cursor.moveToNext());
        }

        cursor.close();
        return allMsg;


    }


    /**
     * @param a 查询a用户的所有聊天对象
     * @return
     */
    public List<Msg>  readDistinctMsg(String a){


        List<Msg> allMsg = new ArrayList<>();
        Cursor cursor = mDatabase.query(true,"msg_info",new String[]{"msg_a","msg_a_nickname", "msg_b", "msg_b_nickname", "content"},
                "msg_a" + "=" + a, null, "msg_b", null, null, null);
        //按照msg_b来去重

        if (cursor.moveToFirst()){
            do {

                Msg msg = new Msg();
                msg.setMsg_a(cursor.getString(cursor.getColumnIndex("msg_a")));
                msg.setMsg_a_nickname(cursor.getString(cursor.getColumnIndex("msg_a_nickname")));
                msg.setMsg_b(cursor.getString(cursor.getColumnIndex("msg_b")));
                msg.setMsg_b_nickname(cursor.getString(cursor.getColumnIndex("msg_b_nickname")));
                msg.setContent(cursor.getString(cursor.getColumnIndex("content")));

                allMsg.add(msg);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return allMsg;

    }



}
