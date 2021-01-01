package com.example.easy_book.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easy_book.R;
import com.example.easy_book.bean.Msg;
import com.example.easy_book.util.UserDBDao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatAdapter extends BaseAdapter {

    private Context context;
    private UserDBDao userDBDao;
    private LayoutInflater layoutInflater;
    private List<Msg> allmsg = new ArrayList<>();
    //对每一个item保存位置
    HashMap<Integer, View> location = new HashMap<>();


    public ChatAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }


    public void setData(List<Msg> allmsg){
        this.allmsg = allmsg;
        notifyDataSetChanged();
    }

    @Override
    //适配器中数据集的数据个数
    public int getCount() {
        return allmsg.size();
    }

    @Override
    //获取数据集中与索引对应的数据项
    public Object getItem(int position) {
        return allmsg.get(position);
    }

    @Override
    //获取指定行对应的ID
    public long getItemId(int position) {
        return position;
    }


    @Override
    //获取每一行Item的显示内容
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatAdapter.ViewHolder holder = null;
        if(location.get(position) == null){
            convertView = layoutInflater.inflate(R.layout.chat_menu_item,null);
            //此布局用来显示ListView中每一个数据项的布局
            Msg msg = (Msg) getItem(position);

            //对holder进行赋值
            holder = new ChatAdapter.ViewHolder(convertView, msg);

            //保存view的位置position
            location.put(position, convertView);
            convertView.setTag(holder);
        }else{
            convertView = location.get(position);
            holder = (ChatAdapter.ViewHolder) convertView.getTag();
        }
        return convertView;
    }


    static class ViewHolder {
        ImageView iv_chat_user;
        TextView tv_chat_uid;



        public ViewHolder(View itemView, Msg msg) {
            //获取位置
            iv_chat_user = itemView.findViewById(R.id.iv_chat_user);
            tv_chat_uid = itemView.findViewById(R.id.tv_chat_uid);

            //传值
            tv_chat_uid.setText(msg.getMsg_b() + "(" + msg.getMsg_b_nickname() + ")");

        }
    }

}
