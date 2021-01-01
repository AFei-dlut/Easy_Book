package com.example.easy_book;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.easy_book.bean.User;


import com.example.easy_book.util.UserDBDao;


public class MyInfoActivity extends AppCompatActivity {


    private Button orc_test_btn, b_quitsys, b_modify, b_my_products, b_my_collection, b_chat_board;
    TextView tv_username, tv_nickname, tv_college, tv_grade, tv_major, tv_phone, tv_QQ, tv_location;
    ImageView iv_headphoto;
    private UserDBDao userDBDao;
    private Context mContext = this;
    private static String  sex, nickname, grade, college, major, phone , QQ, location;
    private String username;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        //从bundle中读取存入的username
        username = this.getIntent().getStringExtra("username_to_myinfo");



        tv_username = findViewById(R.id.tv_username);
        tv_nickname = findViewById(R.id.tv_nickname_value);
        tv_college = findViewById(R.id.tv_college_value);
        tv_grade = findViewById(R.id.tv_grade_value);
        tv_major = findViewById(R.id.tv_major_value);
        iv_headphoto = findViewById(R.id.iv_headphoto);
        tv_phone = findViewById(R.id.tv_phone_value);
        tv_QQ = findViewById(R.id.tv_QQ_value);
        tv_location = findViewById(R.id.tv_location_value);

//        String imageUri = "drawable://" + R.drawable.icon_boy;



        b_quitsys = findViewById(R.id.b_quitsys);
        b_modify = findViewById(R.id.b_modify);
        b_my_products = findViewById(R.id.b_my_products);
        b_my_collection = findViewById(R.id.b_my_collection);
        b_chat_board = findViewById(R.id.b_chat_board);

        //监听我的发布按钮
        b_my_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("username_to_my_products", username);
                Intent intent = new Intent(MyInfoActivity.this, MyproductsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //监听我的收藏按钮
        b_my_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("username_to_my_collections", username);
                Intent intent = new Intent(MyInfoActivity.this, MycollectionsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        //聊天按钮
        b_chat_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("username_to_chat_menu", username);
                Intent intent = new Intent(MyInfoActivity.this, ChatmenuActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



        //退出登录,返回登录界面
        b_quitsys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyInfoActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确认退出系统吗？");
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(MyInfoActivity.this, LoginActivity.class);
                                startActivity(intent);

                            }
                        });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

        //修改个人信息
        b_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("username_to_modify", username);
                Intent intent = new Intent(MyInfoActivity.this, ModifyActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });




        tv_username.setText(username);

        //根据username查询学生其他信息
        userDBDao = new UserDBDao(mContext);
        userDBDao.openDataBase();

        User user = userDBDao.queryUsername(username);

        //如果查到该学号对应学生
        if(user != null){


            sex = user.getSex();
            nickname = user.getNickname();
            grade = user.getGrade();
            college = user.getCollege();
            major = user.getMajor();
            phone = user.getPhone();
            QQ = user.getQQ();
            location = user.getLocation();


//            System.out.println(sex + nickname + grade + college + major + contact + location);

            //根据查询结果填入信息
            //根据性别加载不同头像icon
            if ("男".equals(sex)){
                iv_headphoto.setImageDrawable(getResources().getDrawable(R.drawable.icon_boy));
            }else{
                iv_headphoto.setImageDrawable(getResources().getDrawable(R.drawable.icon_girl));
            }


           if (nickname != null && grade != null && college != null && major != null){

               tv_nickname.setText("昵称：" + nickname);
               tv_grade.setText("年级："+ grade);
               tv_college.setText("学院：" + college);
               tv_major.setText("专业：" + major);

           }
           else {
               //首次进入app，提示进行校园卡识别
               firstDialog();

               tv_nickname.setText("昵称：暂未填写");
               tv_grade.setText("年级：暂未填写");
               tv_college.setText("学院：暂未填写");
               tv_major.setText("专业：暂未填写");

           }

           //填入联系方式住宿地址等信息
            if (phone != null){
                tv_phone.setText("联系方式：" + phone);
            }else {
                tv_phone.setText("联系方式：暂未填写");
            }
            if (QQ != null){
                tv_QQ.setText("QQ：" + QQ);
            }else {
                tv_QQ.setText("QQ：暂未填写");
            }
            if (location != null){
                tv_location.setText("宿舍地址：" + location);
            }else {
                tv_location.setText("宿舍地址：暂未填写");
            }


        }



        orc_test_btn = findViewById(R.id.b_ocr);
//        imageView = findViewById(R.id.image_test);
        orc_test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Bundle bundle = new Bundle();
               bundle.putString("nickname_to_card", nickname);
               bundle.putString("grade_to_card", grade);
               bundle.putString("college_to_card", college);
               bundle.putString("major_to_card", major);
               bundle.putString("username_to_card", username);
               Intent intent = new Intent(MyInfoActivity.this, CardIdentifyActivity.class);
               intent.putExtra("bundle_to_card",bundle);
               startActivity(intent);
               finish();

            }
        });
    }


    private void firstDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("您未进行校园卡认证，请先进行校园卡认证！");
        builder.setPositiveButton("我知道了",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }



}

