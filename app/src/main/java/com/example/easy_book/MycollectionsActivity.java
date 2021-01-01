package com.example.easy_book;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_book.adapter.MyCollectionsAdapter;
import com.example.easy_book.adapter.MyProductsAdapter;
import com.example.easy_book.bean.Collection;
import com.example.easy_book.bean.Product;
import com.example.easy_book.util.CollectionDBDao;
import com.example.easy_book.util.ProductDBDao;

import java.util.ArrayList;
import java.util.List;

public class MycollectionsActivity extends AppCompatActivity {

    TextView tv_mycollections_uid;
    ListView lv_my_collections;
    private String username;
    List<Collection> myCollections = new ArrayList<>();
    MyCollectionsAdapter adapter;
    CollectionDBDao collectionDBDao;
    ProductDBDao productDBDao;
    private Context mContext = this;
    ImageView iv_refresh_mycollections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollection);

        username = getIntent().getStringExtra("username_to_my_collections");

        tv_mycollections_uid = findViewById(R.id.tv_mycollections_uid);
        tv_mycollections_uid.setText(username);

        iv_refresh_mycollections = findViewById(R.id.iv_refresh_mycollections);
        lv_my_collections = findViewById(R.id.lv_my_collections);


        collectionDBDao = new CollectionDBDao(mContext);
        collectionDBDao.openDataBase();

        productDBDao = new ProductDBDao(mContext);
        productDBDao.openDataBase();


        adapter = new MyCollectionsAdapter(mContext);

        myCollections = collectionDBDao.readMyCollections(username);

        adapter.setData(myCollections);
        lv_my_collections.setAdapter(adapter);


        //为每一个item设置点击查看详情事件
        lv_my_collections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Collection collection = (Collection) lv_my_collections.getAdapter().getItem(position);

                //bundle存储
                Bundle bundle = new Bundle();

                bundle.putString("username_to_ProductInfo", username);
                bundle.putInt("pid", collection.getPid());
                bundle.putString("title", collection.getTitle());
                bundle.putString("label", collection.getLabel());
                bundle.putFloat("price", collection.getPrice());
                bundle.putString("description", collection.getDescription());
                bundle.putByteArray("picture", collection.getPicture());
                bundle.putString("uid", productDBDao.selectProductbypid(String.valueOf(collection.getPid())).getUid());


                Intent intent = new Intent(MycollectionsActivity.this, ProductInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


        //长按删除收藏
        lv_my_collections.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MycollectionsActivity.this);
                builder.setTitle("提示:").setMessage("确定删除此收藏商品吗?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Collection collection = (Collection) adapter.getItem(position);
                        //删除收藏商品项
                        collectionDBDao.deleteCollectionby_uid_pid(username, String.valueOf(collection.getPid()));
                        //商品收藏数-1
                        productDBDao.sub_favorites_num(String.valueOf(collection.getPid()), productDBDao.selectProductbypid(String.valueOf(collection.getPid())).getFavorites());



                        Toast.makeText(MycollectionsActivity.this,"已删除此收藏 ", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).show();

                return true;
            }
        });


        //刷新我的收藏
        iv_refresh_mycollections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter = new MyCollectionsAdapter(mContext);
                myCollections = collectionDBDao.readMyCollections(username);
                adapter.setData(myCollections);
                lv_my_collections.setAdapter(adapter);
                Toast.makeText(MycollectionsActivity.this, "已刷新", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
