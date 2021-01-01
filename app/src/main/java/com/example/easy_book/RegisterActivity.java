package com.example.easy_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.easy_book.bean.User;
import com.example.easy_book.util.UserDBDao;


public class RegisterActivity extends AppCompatActivity {

    EditText new_username, new_password, new_phone, new_sex;
    RadioGroup sex_group;
    RadioButton sex_btn;
    private UserDBDao userDBDao;
    private Context mContext = this;
    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userDBDao = new UserDBDao(mContext);
        userDBDao.openDataBase();
        new_username = findViewById(R.id.register_user);
        new_password = findViewById(R.id.register_pwd);
        new_phone = findViewById(R.id.register_phone);
        sex_group = findViewById(R.id.register_sex_group);

        sex_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                sex_btn = findViewById(id);
                sex = sex_btn.getText().toString();

            }
        });


        //注册点击事件
        Button register_btn = findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInput()) {
                    //输入不能为空
                    User user = new User();
                    user.setUsername(new_username.getText().toString());//EditText转Integer
                    user.setPassword(new_password.getText().toString());//EditText转String类型
                    user.setPhone(new_phone.getText().toString());//EditText转Integer
                    user.setSex(sex);
                    userDBDao.AddData(user);
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }


    public boolean CheckInput() {
        String username = new_username.getText().toString();
        String password = new_password.getText().toString();
        String confirm_password = new_phone.getText().toString();
        if(username.trim().equals("")) {
            Toast.makeText(RegisterActivity.this,"学号不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.trim().equals("")) {
            Toast.makeText(RegisterActivity.this,"密码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(confirm_password.trim().equals("")) {
            Toast.makeText(RegisterActivity.this,"手机号不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
