 package com.example.easy_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easy_book.bean.User;
import com.example.easy_book.util.UserDBDao;

import java.util.LinkedList;

 public class ForgetpwdActivity extends AppCompatActivity {

     EditText phone,pwd,repwd;
     private UserDBDao userDBDao;
     private Context mContext = this;
     LinkedList<User> users = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);
        userDBDao = new UserDBDao(mContext);
        userDBDao.openDataBase();

        phone = findViewById(R.id.forget_phone);
        pwd = findViewById(R.id.forget_pwd);
        repwd = findViewById(R.id.foeget_repwd);

        Button forget_btn = findViewById(R.id.forget_btn);

        forget_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //排除输入不正确情况
                if (CheckInput()){
                    //若存在手机号，则更新密码
                    users = userDBDao.queryDataList();

                    for (User user : users){
                        if (user.getPhone().equals(Integer.parseInt(phone.getText().toString()))){
                            userDBDao.update_pwd_by_phone(Integer.parseInt(phone.getText().toString()), pwd.getText().toString());
                            Toast.makeText(ForgetpwdActivity.this, "修改密码成功!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                }
            }
        });

    }




     public boolean CheckInput() {
         String forget_phone = phone.getText().toString();
         String forget_pwd = pwd.getText().toString();
         String forget_repwd = repwd.getText().toString();
         if(forget_phone.trim().equals("")) {
             Toast.makeText(ForgetpwdActivity.this,"手机号不能为空!",Toast.LENGTH_SHORT).show();
             return false;
         }
         if(forget_pwd.trim().equals("")) {
             Toast.makeText(ForgetpwdActivity.this,"新密码不能为空!",Toast.LENGTH_SHORT).show();
             return false;
         }
         if (forget_repwd.trim().equals("")){
             Toast.makeText(ForgetpwdActivity.this,"确认密码不能为空!",Toast.LENGTH_SHORT).show();
             return false;
         }
         if (!forget_pwd.trim().equals(forget_repwd.trim())){
             Toast.makeText(ForgetpwdActivity.this,"两次密码输入不一致!",Toast.LENGTH_SHORT).show();
             return false;
         }
         return true;
 }
}
