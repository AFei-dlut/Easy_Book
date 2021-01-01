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
import com.example.easy_book.bean.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyProductsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Product> products = new ArrayList<>();
    //对每一个item保存位置
    HashMap<Integer, View> location = new HashMap<>();

    public MyProductsAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Product> products){
        this.products = products;

        //数据更新后，通知Activity更新ListView
        notifyDataSetChanged();
    }

    @Override
    //适配器中数据集的数据个数
    public int getCount() {
        return products.size();
    }

    @Override
    //获取数据集中与索引对应的数据项
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    //获取指定行对应的ID
    public long getItemId(int position) {
        return position;
    }

    @Override
    //获取每一行Item的显示内容
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(location.get(position) == null){
            convertView = layoutInflater.inflate(R.layout.layout_allproducts,null);
            //此布局用来显示ListView中每一个数据项的布局
            Product product = (Product) getItem(position);

            //对holder进行赋值
            holder = new ViewHolder(convertView, product);

            //保存view的位置position
            location.put(position, convertView);
            convertView.setTag(holder);
        }else{
            convertView = location.get(position);
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }


    static class ViewHolder {
        ImageView ivProduct;
        TextView tvTitle,tvLabel,tvDescription,tvPrice,tv_favorites_num;

        public ViewHolder(View itemView, Product product) {
            //获取位置
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvLabel = itemView.findViewById(R.id.tv_label);
            tvPrice = itemView.findViewById(R.id.tv_price_info);
            tvDescription = itemView.findViewById(R.id.tv_description);
            ivProduct = itemView.findViewById(R.id.iv_product);
            tv_favorites_num = itemView.findViewById(R.id.tv_favorites_num);
            //传值
            tvTitle.setText(product.getTitle());
            tvLabel.setText(product.getLabel());
            tvPrice.setText("￥" + String.valueOf(product.getPrice()));
            tvDescription.setText(product.getDescription());
            tv_favorites_num.setText("❤收藏" + product.getFavorites());


            byte[] picture = product.getPicture();
            //从字节数组中解码生成不可变的位图
            //public static Bitmap decodeByteArray(byte[] data, int offset, int length)
            Bitmap img = BitmapFactory.decodeByteArray(picture,0,picture.length);
            ivProduct.setImageBitmap(img);

        }
    }
}
