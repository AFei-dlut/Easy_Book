package com.example.easy_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_book.adapter.AllProductsAdapter;
import com.example.easy_book.bean.Product;
import com.example.easy_book.util.ProductDBDao;
import com.wega.library.loadingDialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context mContext = this;
    private TextView tv_welcome;
    private ImageButton ib_addproduct_home, ib_myinfo_home, ib_refresh, ib_select;
    private EditText et_select;
    ListView lvAllProducts;
    List<Product> allProducts = new ArrayList<>();


    private ProductDBDao productDBDao;
    private AllProductsAdapter adapter, adapter_label;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvAllProducts = findViewById(R.id.lv_products);

        //打开商品数据库
        productDBDao = new ProductDBDao(mContext);
        productDBDao.openDataBase();
        adapter = new AllProductsAdapter(mContext);
        allProducts = productDBDao.readAllProducts();

        //设置数据适配器
        adapter.setData(allProducts);
        lvAllProducts.setAdapter(adapter);

        final String username = this.getIntent().getStringExtra("username_to_home");
        tv_welcome = findViewById(R.id.tv_homepage_username);
        tv_welcome.setText("欢迎！" + username);

        ib_addproduct_home = findViewById(R.id.ib_addproduct_home);
        ib_myinfo_home = findViewById(R.id.ib_myinfo_home);
        ib_refresh = findViewById(R.id.ib_refresh);
        ib_select = findViewById(R.id.ib_select);
        et_select = findViewById(R.id.et_select);



        //跳转发布页面
        ib_addproduct_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("username_to_addproduct", username);
                Intent intent = new Intent(MainActivity.this, AddproductActivity.class);
                intent.putExtra("bundle_to_addproduct", bundle);
                startActivity(intent);
            }
        });

        //跳转我的页面
        ib_myinfo_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("username_to_myinfo", username);
                Intent intent = new Intent(MainActivity.this, MyInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        //根据筛选条件进行搜索
        ib_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String label = et_select.getText().toString();
                List<Product> labelProducts = productDBDao.FindProductByLabel(label);

                //筛选不到目标
                if (labelProducts.size() == 0 || labelProducts == null){


                    Toast.makeText(MainActivity.this, "对不起，暂未搜索到此类商品", Toast.LENGTH_SHORT).show();
//                    System.out.println(1);
                }else {
//                    System.out.println(2);




                    adapter_label = new AllProductsAdapter(mContext);
                    adapter_label.setData(labelProducts);
                    lvAllProducts.setAdapter(adapter_label);

                }



            }
        });


        //刷新界面
        ib_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LoadingDialog loadingDialog = new LoadingDialog(mContext);
                loadingDialog.loading("刷新中...");
                loadingDialog.cancelDelay(3000);

                adapter = new AllProductsAdapter(mContext);
                allProducts = productDBDao.readAllProducts();

                adapter.setData(allProducts);
                lvAllProducts.setAdapter(adapter);


            }
        });




        //为每一个item设置点击查看事件
        lvAllProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //view是一个句柄，可以用view.findViewById()方法来获取所点击item中的控件。
                //position是所点击item在适配器中的位置
                //id是当前点击的item在listview 里的第几行的位置，通常id与position的值相同。

                Product product = (Product) lvAllProducts.getAdapter().getItem(position);

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


                Intent intent = new Intent(MainActivity.this, ProductInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }
}
