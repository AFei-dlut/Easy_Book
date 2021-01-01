package com.example.easy_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_book.bean.User;
import com.example.easy_book.util.UserDBDao;
import com.wega.library.loadingDialog.LoadingDialog;


import java.util.LinkedList;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class LoginActivity extends AppCompatActivity {

    EditText etusername, etpassword;
    private String username;
    private UserDBDao userDBDao;
    private Context mContext = this;
    LinkedList<User> users = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SQLiteStudioService.instance().start(this);
        userDBDao = new UserDBDao(mContext);
        userDBDao.openDataBase();
        Button login_btn = findViewById(R.id.login_btn);
        //监听登录按钮，若成功则跳转到主界面，否则给出错误信息
        etusername = findViewById(R.id.login_user);
        etpassword = findViewById(R.id.login_pwd);

        //测试数据
//        String u = "20160360210";//李娜
//        String u = "20162430630";
//        String p = "123456";
//        etusername.setText(u);
//        etpassword.setText(p);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean flag = false;//判断是否在数据库有匹配的用户名及密码
                if (CheckInput()) {
                    users = userDBDao.queryDataList();
                    for (User user : users){
                        if (user.getUsername().equals(etusername.getText().toString()) && user.getPassword().equals(etpassword.getText().toString())){
                            flag = true;



                            LoadingDialog loadingDialog = new LoadingDialog(mContext);
                            loadingDialog.loading("登录中...");
                            loadingDialog.cancelDelay(3000);





                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            username = etusername.getText().toString();
                            bundle.putString("username_to_home", username);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                        //登陆失败
                        if (!flag){
                            Toast.makeText(LoginActivity.this, "学号或密码输入错误！请重新输入", Toast.LENGTH_SHORT).show();
                        }


                }

            }
        });

        TextView login_newuser = findViewById(R.id.login_newuser);
        //跳转到注册界面
        login_newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        TextView login_forget = findViewById(R.id.login_forget);
        //跳转到重置密码界面
        login_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetpwdActivity.class);
                startActivity(intent);
            }
        });
    }



    public boolean CheckInput() {
        String login_user = etusername.getText().toString();
        String login_pwd = etpassword.getText().toString();
        if(login_user.trim().equals("")) {
            Toast.makeText(LoginActivity.this,"学号不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(login_pwd.trim().equals("")) {
            Toast.makeText(LoginActivity.this,"密码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
