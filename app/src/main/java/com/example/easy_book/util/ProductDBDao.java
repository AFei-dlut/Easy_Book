package com.example.easy_book.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.easy_book.bean.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDBDao {

    /** 数据库名称 */
    private static final String DB_NAME = "easy_book.db";
    /** 数据表名称 */
    private static final String TABLE_NAME = "product_info";
    /** 数据库版本 */
    private static final int DB_VERSION = 1;

    private SQLiteDatabase mDatabase;
    private Context mContext;
    /** 数据库打开帮助类 */
    private pDBOpenHelper mDbOpenHelper;

    public ProductDBDao(Context context) {
        mContext = context;
    }

    /**
     * 数据表打开帮助类
     */
    private static class pDBOpenHelper extends SQLiteOpenHelper {

        public pDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //自动检测是否存在数据库文件，若不存在则调用onCreate函数
        @Override
        public void onCreate(SQLiteDatabase db) {
            final String sqlStr = "create table if not exists " + TABLE_NAME + " (" +
                    "pid" + " integer primary key autoincrement unique, " +
                    "title" + " text not null, " +
                    "label" + " text not null,"+
                    "price"+ " float not null,"+
                    "description" + "text,"+
                    "picture" + "blob,"+
                    "uid" + "text,"+
                    "foreign key(uid) references user_info(username));";
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
     * 打开商品数据库
     */
    public void openDataBase(){
        mDbOpenHelper = new pDBOpenHelper(mContext, DB_NAME, null, DB_VERSION);
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
     * @param product
     * @return
     * 发布商品，添加一条商品信息
     */
    public boolean AddProduct(Product product){

        ContentValues values = new ContentValues();
        values.put("title", product.getTitle());
        values.put("label", product.getLabel());
        values.put("price", product.getPrice());
        values.put("description",  product.getDescription());
        values.put("picture", product.getPicture());
        values.put("uid", product.getUid());

        mDatabase.insert(TABLE_NAME, null, values);

        values.clear();
        return true;

    }


    public long update_productinfo(String pid, String title, String label, Float price, String description, byte[] picture){

        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("label", label);
        values.put("price", price);
        values.put("description", description);
        values.put("picture", picture);

        return mDatabase.update(TABLE_NAME, values, "pid" + "=" + pid, null);
    }


    //获取加入新商品记录后的自增列pid的值
//    public int pidAfter(){
//        String sql = "select last_insert_rowid() from" + TABLE_NAME;
//        Cursor cursor = mDatabase.rawQuery(sql, null);
//        int pid = cursor.getInt(cursor.getColumnIndex("pid"));
//        System.out.println(pid);
//        return pid;
//    }


    //通过标签搜索商品
    public List<Product> FindProductByLabel(String factor){

        List<Product> labelProducts = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("select * from product_info", null);
        if (cursor.moveToFirst()){
            do {
                String all_label = cursor.getString(cursor.getColumnIndex("label"));
                if (all_label.contains(factor)){

                    System.out.println(1);
                    Integer pid = cursor.getInt(cursor.getColumnIndex("pid"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String label = cursor.getString(cursor.getColumnIndex("label"));
                    float price = cursor.getFloat(cursor.getColumnIndex("price"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));
                    String uid = cursor.getString(cursor.getColumnIndex("uid"));
                    Integer favorites = cursor.getInt(cursor.getColumnIndex("favorites"));

                    Product product = new Product();
                    product.setPid(pid);
                    product.setTitle(title);
                    product.setLabel(label);
                    product.setPrice(price);
                    product.setDescription(description);
                    product.setPicture(picture);
                    product.setUid(uid);
                    product.setFavorites(favorites);

                    labelProducts.add(product);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return labelProducts;

    }


    public List<Product> readAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("select * from product_info order by pid DESC", null);
            if (cursor.moveToFirst()) {
            do {
                Integer pid = cursor.getInt(cursor.getColumnIndex("pid"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String label = cursor.getString(cursor.getColumnIndex("label"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));
                String uid = cursor.getString(cursor.getColumnIndex("uid"));
                Integer favorites = cursor.getInt(cursor.getColumnIndex("favorites"));


//                System.out.println(pid + title + label);

                Product product = new Product();
                product.setPid(pid);
                product.setTitle(title);
                product.setLabel(label);
                product.setPrice(price);
                product.setDescription(description);
                product.setPicture(picture);
                product.setUid(uid);
                product.setFavorites(favorites);

                allProducts.add(product);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return allProducts;
    }

    public List<Product> readMyProducts(String username){
        List<Product> myProducts = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("select * from product_info where uid=?", new String[]{username});
        if (cursor.moveToFirst()) {
            do {
                Integer pid = cursor.getInt(cursor.getColumnIndex("pid"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String label = cursor.getString(cursor.getColumnIndex("label"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));
                String uid = cursor.getString(cursor.getColumnIndex("uid"));
                Integer favorites = cursor.getInt(cursor.getColumnIndex("favorites"));

//                System.out.println(pid + title + label);

                Product product = new Product();
                product.setPid(pid);
                product.setTitle(title);
                product.setLabel(label);
                product.setPrice(price);
                product.setDescription(description);
                product.setPicture(picture);
                product.setUid(uid);
                product.setFavorites(favorites);

                myProducts.add(product);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return myProducts;
    }


    //根据pid删除商品
    public void deleteProductbypid(String pid) {

        mDatabase.delete(TABLE_NAME, "pid=?", new String[]{pid});

    }

    //根据pid查找商品
    public Product selectProductbypid(String pid) {

       Cursor cursor =  mDatabase.rawQuery("select * from product_info where pid=?", new String[]{pid});
        Product product = new Product();

       if (cursor.moveToFirst()){


           String title = cursor.getString(cursor.getColumnIndex("title"));
           String label = cursor.getString(cursor.getColumnIndex("label"));
           float price = cursor.getFloat(cursor.getColumnIndex("price"));
           String description = cursor.getString(cursor.getColumnIndex("description"));
           byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));
           String uid = cursor.getString(cursor.getColumnIndex("uid"));
           int favorites = cursor.getInt(cursor.getColumnIndex("favorites"));


//                System.out.println(pid + title + label);



           product.setTitle(title);
           product.setLabel(label);
           product.setPrice(price);
           product.setDescription(description);
           product.setPicture(picture);
           product.setUid(uid);
           product.setFavorites(favorites);


       }
        return product;
    }



    //点击收藏增加对应商品收藏数+1
    public long add_favorites_num(String pid, Integer favorites){

        ContentValues values = new ContentValues();

        values.put("favorites", favorites + 1);

        return mDatabase.update(TABLE_NAME, values, "pid" + "=" + pid, null);
    }


    //点击已收藏取消商品收藏对应商品收藏数-1
    public long sub_favorites_num(String pid, Integer favorites){

        ContentValues values = new ContentValues();

        values.put("favorites", favorites - 1);

        return mDatabase.update(TABLE_NAME, values, "pid" + "=" + pid, null);
    }



}
