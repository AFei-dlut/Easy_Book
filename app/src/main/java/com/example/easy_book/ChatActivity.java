package com.example.easy_book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_book.adapter.MsgAdapter;
import com.example.easy_book.bean.Msg;
import com.example.easy_book.util.MsgDBDao;
import com.example.easy_book.util.UserDBDao;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private EditText inputText;
    private TextView chat_to_who;
    private ImageView iv_back, iv_result;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private UserDBDao userDBDao;
    private MsgDBDao msgDBDao;
    private Context mContext = this;
    private String username, uid, a_nickname, b_nickname;
    private int identify_card;
    private List<Msg> allmsg = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        userDBDao = new UserDBDao(mContext);
        userDBDao.openDataBase();

        msgDBDao = new MsgDBDao(mContext);
        msgDBDao.openDataBase();

        username = this.getIntent().getStringExtra("username_to_chat_Activity");
        uid = this.getIntent().getStringExtra("uid_to_chat_Activity");
        a_nickname = userDBDao.queryUsername(username).getNickname();//查询当前用户昵称
        b_nickname = userDBDao.queryUsername(uid).getNickname();//查询点击商品卖家昵称


        identify_card = userDBDao.queryUsername(uid).getCard_identify();

        iv_result = findViewById(R.id.iv_result);


        if (identify_card == 1){
            iv_result.setImageDrawable(getResources().getDrawable(R.drawable.identify_1));
            Toast.makeText(ChatActivity.this, "此用户以进行校园卡认证，请放心交流", Toast.LENGTH_SHORT).show();
        }else {
            iv_result.setImageDrawable(getResources().getDrawable(R.drawable.identify_0));
            Toast.makeText(ChatActivity.this, "此用户暂未进行校园卡认证，请警惕交易", Toast.LENGTH_LONG).show();
        }

        System.out.println(username + " " + uid);
        //获取用户聊天记录
        allmsg = msgDBDao.readAllMsg(username, uid);

        inputText = findViewById(R.id.chat_editText);
        send = findViewById(R.id.chat_button);
        msgRecyclerView = findViewById(R.id.chat_recyclerview);
        chat_to_who = findViewById(R.id.chat_to_who);
        iv_back = findViewById(R.id.iv_back);

        chat_to_who.setText(uid + "(" + b_nickname + ")");

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter=new MsgAdapter(allmsg);
        msgRecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=inputText.getText().toString();
                if(!"".contentEquals(content)) //判断内容是否为空
                {

                    Msg msg = new Msg();
                    String type = "received";
                    msg.setMsg_a(uid);
                    msg.setMsg_a_nickname(b_nickname);
                    msg.setMsg_b(username);
                    msg.setMsg_b_nickname(a_nickname);
                    msg.setContent(content);
                    msg.setType(type);
                    msgDBDao.AddMsg(msg);//b用户对a用户的消息记录

                    type = "sent";
                    msg.setMsg_a(username);
                    msg.setMsg_a_nickname(a_nickname);
                    msg.setMsg_b(uid);
                    msg.setMsg_b_nickname(b_nickname);
                    msg.setContent(content);
                    msg.setType(type);
                    System.out.println(content + type);
                    msgDBDao.AddMsg(msg);//a用户对b用户的消息记录
                    allmsg.add(msg);

                    adapter.notifyItemInserted(allmsg.size()-1);//当有新消息时，刷新RecyclerView中的显示
                    msgRecyclerView.scrollToPosition(allmsg.size()-1); //将RecyclerView定位到最后一行

                    inputText.setText("");//清空输入框中的内容

                }else{
                    Toast.makeText(ChatActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });










    }



}
