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
import com.example.easy_book.bean.Collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyCollectionsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<Collection> collections = new ArrayList<>();

    HashMap<Integer, View> location = new HashMap<>();


    public MyCollectionsAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Collection> collections) {
        this.collections = collections;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public Object getItem(int position) {
        return collections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(location.get(position) == null){
            convertView = layoutInflater.inflate(R.layout.layout_allproducts,null);
            Collection collection = (Collection) getItem(position);
            holder = new ViewHolder(convertView,collection);
            location.put(position,convertView);
            convertView.setTag(holder);
        }else{
            convertView = location.get(position);
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    //定义静态类,包含每一个item的所有元素
    static class ViewHolder {

        ImageView ivProduct;
        TextView tvTitle,tvLabel,tvDescription,tvPrice,tv_favorites_num;

        public ViewHolder(View itemView,Collection collection) {


            tvTitle = itemView.findViewById(R.id.tv_title);
            tvLabel = itemView.findViewById(R.id.tv_label);
            tvPrice = itemView.findViewById(R.id.tv_price_info);
            tvDescription = itemView.findViewById(R.id.tv_description);
            ivProduct = itemView.findViewById(R.id.iv_product);
            tv_favorites_num = itemView.findViewById(R.id.tv_favorites_num);
            //传值
            tvTitle.setText(collection.getTitle());
            tvLabel.setText(collection.getLabel());
            tvPrice.setText("￥" + String.valueOf(collection.getPrice()));
            tvDescription.setText(collection.getDescription());
            tv_favorites_num.setText("");



            byte[] picture = collection.getPicture();
            //从字节数组中解码生成不可变的位图
            //public static Bitmap decodeByteArray(byte[] data, int offset, int length)
            Bitmap img = BitmapFactory.decodeByteArray(picture,0,picture.length);
            ivProduct.setImageBitmap(img);

        }
    }


}
