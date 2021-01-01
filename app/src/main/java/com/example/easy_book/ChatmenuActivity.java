package com.example.easy_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.easy_book.adapter.ChatAdapter;
import com.example.easy_book.bean.Msg;
import com.example.easy_book.util.MsgDBDao;

import java.util.ArrayList;
import java.util.List;

public class ChatmenuActivity extends AppCompatActivity {

    private List<Msg> allmsg = new ArrayList<>();
    private Context mContext = this;
    private String username;
    ListView lv_chatlist;
    ImageView iv_back_board;

    private ChatAdapter chatAdapter;
    private MsgDBDao msgDBDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatmenu);

        username = this.getIntent().getStringExtra("username_to_chat_menu");

        System.out.println(username);

        lv_chatlist = findViewById(R.id.lv_chatlist);
        iv_back_board = findViewById(R.id.iv_back_board);

        msgDBDao = new MsgDBDao(mContext);
        msgDBDao.openDataBase();
        chatAdapter = new ChatAdapter(mContext);
        allmsg = msgDBDao.readDistinctMsg(username);

        for (int i = 0; i<allmsg.size(); i++){
            System.out.println(allmsg.get(i).getMsg_a() + allmsg.get(i).getMsg_b() + allmsg.get(i).getContent());
        }

        //设置数据适配器
        chatAdapter.setData(allmsg);
        lv_chatlist.setAdapter(chatAdapter);




        //返回按钮
        iv_back_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //为聊天板每一个item设置点击事件
        lv_chatlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Msg msg = (Msg) lv_chatlist.getAdapter().getItem(position);

                //bundle存储
                Bundle bundle = new Bundle();

                bundle.putString("username_to_chat_Activity", username);
                bundle.putString("uid_to_chat_Activity", msg.getMsg_b());


                Intent intent = new Intent(ChatmenuActivity.this, ChatActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });





    }
}
