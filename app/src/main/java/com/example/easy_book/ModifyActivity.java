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

public class ModifyActivity extends AppCompatActivity {

    private String username, nickname, grade, college, major, phone, QQ, location;
    private String modify_nickname, modify_grade, modify_college, modify_major, modify_phone, modify_QQ, modify_location;
    EditText et_nickname, et_college, et_major, et_grade, et_phone, et_QQ, et_location;
    Button b_save;
    private UserDBDao userDBDao;
    private Context mContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        //取出bundle中的username
        //从bundle中读取存入的username
        username = this.getIntent().getStringExtra("username_to_modify");


        System.out.println(username);


        et_nickname = findViewById(R.id.et_nickname_modify);
        et_college = findViewById(R.id.et_college_modify);
        et_major = findViewById(R.id.et_major_modify);
        et_grade = findViewById(R.id.et_grade_modify);
        et_phone = findViewById(R.id.et_phone_modify);
        et_QQ = findViewById(R.id.et_QQ_modify);
        et_location = findViewById(R.id.et_location_modify);
        b_save = findViewById(R.id.b_save);


        userDBDao = new UserDBDao(mContext);
        userDBDao.openDataBase();

        User user = userDBDao.queryUsername(username);

        if (user != null) {

            nickname = user.getNickname();
            grade = user.getGrade();
            college = user.getCollege();
            major = user.getMajor();
            phone = user.getPhone();
            QQ = user.getQQ();
            location = user.getLocation();

            System.out.println(nickname + grade + college + major + phone + QQ + location);


            if(nickname != null){
                et_nickname.setText(nickname);
            }else{
                et_nickname.setText("暂未填写");
            }

            if(college != null){
                et_college.setText(college);
            }else{
                et_college.setText("暂未填写");
            }

            if(major != null){
                et_major.setText(major);
            }else{
                et_major.setText("暂未填写");
            }

            if(grade != null){
                et_grade.setText(grade);
            }else{
                et_grade.setText("暂未填写");
            }

            if(phone != null){
                et_phone.setText(phone);
            }else{
                et_phone.setText("暂未填写");
            }

            if(QQ != null){
                et_QQ.setText(QQ);
            }else{
                et_QQ.setText("暂未填写");
            }

            if(location != null){
                et_location.setText(location);
            }else{
                et_location.setText("暂未填写");
            }

        }


        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                modify_nickname = et_nickname.getText().toString();
                modify_college = et_college.getText().toString();
                modify_major = et_major.getText().toString();
                modify_grade = et_grade.getText().toString();
                modify_phone = et_phone.getText().toString();
                modify_QQ = et_QQ.getText().toString();
                modify_location = et_location.getText().toString();

                userDBDao.update_userinfo(username, modify_phone, modify_nickname, modify_grade, modify_college, modify_major, modify_QQ, modify_location);
                Toast.makeText(ModifyActivity.this, "修改信息成功！", Toast.LENGTH_SHORT).show();
                finish();


            }
        });



    }
}
