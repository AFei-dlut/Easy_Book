package com.example.easy_book;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_book.adapter.AllProductsAdapter;
import com.example.easy_book.adapter.MyProductsAdapter;
import com.example.easy_book.bean.Product;
import com.example.easy_book.util.ProductDBDao;

import java.util.ArrayList;
import java.util.List;

public class MyproductsActivity extends AppCompatActivity {

    TextView tv_myproducts_uid;
    ListView lv_my_products;
    private String username;
    List<Product> myProducts = new ArrayList<>();
    MyProductsAdapter adapter;
    ProductDBDao productDBDao;
    private Context mContext = this;
    Button pop_modify, pop_delete;
    ImageView iv_refresh_myproducts;
    PopupWindow mPopWindow = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myproducts);

        username = getIntent().getStringExtra("username_to_my_products");

        tv_myproducts_uid = findViewById(R.id.tv_myproducts_uid);
        tv_myproducts_uid.setText(username);

        iv_refresh_myproducts = findViewById(R.id.iv_refresh_myproducts);

        lv_my_products = findViewById(R.id.lv_my_products);
        productDBDao = new ProductDBDao(mContext);
        productDBDao.openDataBase();
        adapter = new MyProductsAdapter(mContext);

        myProducts = productDBDao.readMyProducts(username);

        adapter.setData(myProducts);
        lv_my_products.setAdapter(adapter);


        //单击对商品进行查看
        lv_my_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) lv_my_products.getAdapter().getItem(position);

                //bundle存储
                Bundle bundle = new Bundle();

                bundle.putString("username_to_ProductInfo", username);
                bundle.putInt("pid", product.getPid());
                bundle.putString("title", product.getTitle());
                bundle.putString("label", product.getLabel());
                bundle.putFloat("price", product.getPrice());
                bundle.putString("description", product.getDescription());
                bundle.putByteArray("picture", product.getPicture());
                bundle.putString("uid", product.getUid());


                Intent intent = new Intent(MyproductsActivity.this, ProductInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        //长按对商品进行修改或者删除
        lv_my_products.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                System.out.println(position);

                showPopupWindow(position);

                return true;//返回值为true代表当前有能力消费掉该长按事件，阻止事件向下传递，短按事件就不会被触发了
            }
        });

        //刷新我的发布
        iv_refresh_myproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter = new MyProductsAdapter(mContext);
                myProducts = productDBDao.readMyProducts(username);
                adapter.setData(myProducts);
                lv_my_products.setAdapter(adapter);
                Toast.makeText(MyproductsActivity.this, "已刷新", Toast.LENGTH_SHORT).show();
            }
        });



    }


    //单击提示框
    private void showPopupWindow(final int flag) {
        //获取popup_window的view
        View contentView = LayoutInflater.from(MyproductsActivity.this).inflate(R.layout.popup_window, null);

        //构造函数
        mPopWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

        addBackground();

        mPopWindow.setAnimationStyle(R.style.PopupWindowAnimation);

        mPopWindow.setContentView(contentView);
        //设置各个控件的点击响应
        pop_modify = contentView.findViewById(R.id.pop_modify);
        pop_delete = contentView.findViewById(R.id.pop_delete);

        //编辑商品信息
        pop_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //根据position获取product
                Product product = (Product) adapter.getItem(flag);

                Bundle bundle = new Bundle();
                bundle.putString("username_to_addproduct", product.getUid());
                bundle.putString("pid_to_addproduct", product.getPid().toString());
                Intent intent = new Intent(MyproductsActivity.this, AddproductActivity.class);
                intent.putExtra("bundle_to_addproduct",bundle);
                startActivity(intent);

                Toast.makeText(MyproductsActivity.this, "重新编辑商品信息", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
            }
        });



        //删除此商品
        pop_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyproductsActivity.this);
                builder.setTitle("提示:").setMessage("确认删除此商品吗?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        //根据position获取product
                        Product product = (Product) adapter.getItem(flag);

                        //从product表中根据pid删除此商品
                        productDBDao.deleteProductbypid(product.getPid().toString());

                        Toast.makeText(MyproductsActivity.this, "已删除此商品", Toast.LENGTH_SHORT).show();

                        mPopWindow.dismiss();
                    }
                }).show();


            }
        });


        //显示PopupWindow
        View rootview = LayoutInflater.from(MyproductsActivity.this).inflate(R.layout.activity_myproducts, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);




    }


    private void addBackground() {
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;//调节透明度
        getWindow().setAttributes(lp);
        //dismiss时恢复原样
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }


}
