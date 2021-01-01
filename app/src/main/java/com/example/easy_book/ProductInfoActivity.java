package com.example.easy_book;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_book.bean.Collection;
import com.example.easy_book.bean.Product;
import com.example.easy_book.bean.User;
import com.example.easy_book.util.CollectionDBDao;
import com.example.easy_book.util.ProductDBDao;
import com.example.easy_book.util.UserDBDao;

public class ProductInfoActivity extends AppCompatActivity {

    TextView tv_description, tv_price, tv_contact, tv_location, tv_title, tv_label;
    ImageView ivproduct;
    byte[] picture;
    private String username, title, label, description, price, uid;
    private Integer pid, favorites;
    private CollectionDBDao collectionDBDao;
    private ProductDBDao productDBDao;
    private Context mContext = this;
    Button b_collection, b_chat_to_seller;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        //打开数据库
        collectionDBDao = new CollectionDBDao(mContext);
        collectionDBDao.openDataBase();

        productDBDao = new ProductDBDao(mContext);
        productDBDao.openDataBase();

        ivproduct = findViewById(R.id.iv_product_info);
        tv_description = findViewById(R.id.tv_description_info);
        tv_price = findViewById(R.id.tv_price_info);
        tv_title = findViewById(R.id.tv_title_info);
        tv_label = findViewById(R.id.tv_label_info);
        b_collection = findViewById(R.id.b_collection);
        b_chat_to_seller = findViewById(R.id.b_chat_to_seller);

        //从bundle中取值
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            username = bundle.getString("username_to_ProductInfo");
            pid = bundle.getInt("pid");
            title = bundle.getString("title");
            label = bundle.getString("label");
            description = bundle.getString("description");
            price = String.valueOf(bundle.getFloat("price"));
            picture = bundle.getByteArray("picture");
            uid = bundle.getString("uid");


            //如果当前用户就是此商品卖家
            if (username.equals(uid)){
                b_chat_to_seller.setText("您就是该商品卖家");

                //设置按钮不可点击
                b_chat_to_seller.setEnabled(false);
            }



            //如果此商品已被用户收藏
            if (collectionDBDao.isinCollection(username, String.valueOf(pid))){

                b_collection.setText("已收藏");
            }else{

                b_collection.setText("收藏");
            }


            //转换bitmap
            Bitmap img = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            ivproduct.setImageBitmap(img);
            tv_description.setText(description);
            tv_price.setText("￥ " + price);
            tv_title.setText(title);
            tv_label.setText(label);


        }


        //收藏按钮事件
        b_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                favorites = productDBDao.selectProductbypid(String.valueOf(pid)).getFavorites();

                //未收藏
                if (b_collection.getText().toString().equals("收藏")){


                Collection collection = new Collection();

                collection.setUid(username);
                collection.setPid(pid);
                collection.setTitle(title);
                collection.setLabel(label);
                collection.setDescription(description);
                collection.setPrice(Float.valueOf(price));
                collection.setPicture(picture);

                collectionDBDao.AddCollection(collection);

                productDBDao.add_favorites_num(String.valueOf(pid), favorites);

                Toast.makeText(ProductInfoActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();

                b_collection.setText("已收藏");

                }else if (b_collection.getText().toString().equals("已收藏")){


                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductInfoActivity.this);
                    builder.setTitle("提示:").setMessage("确定取消此收藏吗?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //删除收藏商品项
                            collectionDBDao.deleteCollectionby_uid_pid(username, String.valueOf(pid));
                            productDBDao.sub_favorites_num(String.valueOf(pid), favorites);
                            Toast.makeText(ProductInfoActivity.this,"已取消收藏 ", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            b_collection.setText("收藏");
                        }
                    }).show();




                }




            }
        });



        //与卖家沟通按钮事件
        b_chat_to_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("uid_to_chat_Activity", uid);
                bundle.putString("username_to_chat_Activity", username);
                Intent intent = new Intent(ProductInfoActivity.this, ChatActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);



            }
        });



    }
}
