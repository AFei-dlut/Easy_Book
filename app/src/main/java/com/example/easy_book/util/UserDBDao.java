package com.example.easy_book.util;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.easy_book.bean.User;
import java.util.LinkedList;


/**
 * 数据库操作类
 * @author  dlong
 * created at 2019/3/13 10:33 AM
 */
public class UserDBDao {

    /** 数据库名称 */
    private static final String DB_NAME = "easy_book.db";
    /** 数据表名称 */
    private static final String TABLE_NAME = "user_info";
    /** 数据库版本 */
    private static final int DB_VERSION = 1;


    private SQLiteDatabase mDatabase;
    private Context mContext;
    /** 数据库打开帮助类 */
    private uDBOpenHelper mDbOpenHelper;


    public UserDBDao(Context context) {
        mContext = context;
    }

    /**
     * 数据表打开帮助类
     */
    private static class uDBOpenHelper extends SQLiteOpenHelper {

        public uDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //自动检测是否存在数据库文件，若不存在则调用onCreate函数
        @Override
        public void onCreate(SQLiteDatabase db) {
            final String sqlStr = "create table if not exists " + TABLE_NAME + " (" +
                    "username" + " text primary key  unique, " +
                    "password" + " text not null, " +
                    "sex" + " text not null check(sex = '男' or sex = '女'),"+
                    "phone"+ " text not null unique,"+
                    "nickname" + "text,"+
                    "grade" + "text,"+
                    "college" + "text,"+
                    "major" + "text,"+
                    "QQ" + "text unique," +
                    "location" + "text);";
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
     * 打开用户数据库
     */
    public void openDataBase() {
        mDbOpenHelper = new uDBOpenHelper(mContext, DB_NAME, null, DB_VERSION);
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

    /**
     * @param username
     * @return User
     * 通过学号查询单个学生信息
     */
    public User queryUsername(String username){
        User user = new User();
        Cursor cursor = mDatabase.rawQuery("select * from user_info where username=?", new String[]{username});
        if (cursor.moveToFirst()) {
            do {
                String sex = cursor.getString(cursor.getColumnIndex("sex"));
                String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                String grade = cursor.getString(cursor.getColumnIndex("grade"));
                String college = cursor.getString(cursor.getColumnIndex("college"));
                String major = cursor.getString(cursor.getColumnIndex("major"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String QQ = cursor.getString(cursor.getColumnIndex("QQ"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                byte[] card = cursor.getBlob(cursor.getColumnIndex("card"));
                Integer card_identify = cursor.getInt(cursor.getColumnIndex("card_identify"));

                user.setSex(sex);
                user.setNickname(nickname);
                user.setGrade(grade);
                user.setCollege(college);
                user.setMajor(major);
                user.setPhone(phone);
                user.setQQ(QQ);
                user.setLocation(location);
                user.setCard(card);
                user.setCard_identify(card_identify);


            } while (cursor.moveToNext());
        }
            cursor.close();
            return user;
    }


    //注册一条数据
    public long AddData(User user) {
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("sex", user.getSex());
        values.put("phone", user.getPhone());

        return mDatabase.insert(TABLE_NAME, null, values);
    }

//    //删除一条数据
//    public long deleteData(long id) {
//        return mDatabase.delete(TABLE_NAME, KEY_ID + "=" + id, null);
//    }

//    删除所有数据
//    public long deleteAllData() {
//        return mDatabase.delete(TABLE_NAME, null, null);
//    }


    /**
     * @param phone
     * @param password
     * @return
     * 根据手机号修改密码
     */
    public long update_pwd_by_phone(Integer phone, String password ) {

        ContentValues values = new ContentValues();
        values.put("password", password);
        return mDatabase.update(TABLE_NAME, values, "phone" + "=" + phone, null);
    }

    /**
    *根据学号插入校园卡识别信息
     * @param username
     * @param nickname
     * @param grade
     * @param college
     * @param major
     * @return
     */

    public long update_cardinfo_by_username(String username, String nickname, String grade, String college, String major, byte[] card, int card_identify){
        ContentValues values = new ContentValues();
        values.put("nickname", nickname);
        values.put("grade", grade);
        values.put("college", college);
        values.put("major", major);
        values.put("card", card);
        values.put("card_identify", card_identify);
        return mDatabase.update(TABLE_NAME, values, "username" + "=" + username, null);
    }


    public long update_userinfo(String username, String phone,  String nickname, String grade, String college, String major, String QQ, String location){
        ContentValues values = new ContentValues();

        values.put("phone", phone);
        values.put("nickname", nickname);
        values.put("grade", grade);
        values.put("college", college);
        values.put("major", major);
        values.put("QQ", QQ);
        values.put("location", location);
        return mDatabase.update(TABLE_NAME, values, "username" + "=" + username, null);

    }



    //查询所有数据
    public LinkedList<User> queryDataList() {
        LinkedList<User> users = new LinkedList<>();
        Cursor cursor = mDatabase.rawQuery("select * from user_info", null);
        if (cursor.moveToFirst()){
            do {
                User user = new User();
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));

                user.setUsername(username);
                user.setPassword(password);
                user.setPhone(phone);
                users.add(user);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

}
