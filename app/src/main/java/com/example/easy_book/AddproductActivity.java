package com.example.easy_book;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.easy_book.bean.Product;
import com.example.easy_book.bean.User;
import com.example.easy_book.util.ProductDBDao;
import com.example.easy_book.util.UserDBDao;
import com.wega.library.loadingDialog.LoadingDialog;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;

public class AddproductActivity extends AppCompatActivity {

    private String username, select_grade, select_major, select_category, label, pid;
    private ProductDBDao productDBDao;
    private Context mContext = this;
    private UserDBDao userDBDao;
    private TextView tv_addproduct_username;
    private EditText et_product_title, et_product_price, et_product_description;
    private Button b_add_product;
    ImageView iv_addphoto;


    //spinner控件数据
    private Spinner sp_grade, sp_major, sp_category;
    private ArrayAdapter<String> gradeAdapter;
    private ArrayAdapter<String> majorAdapter;
    private ArrayAdapter<String> categoryAdapter;
    private String[] grade = {"适用年级", "大一", "大二", "大三", "大四", "研究生"};
    private String[] major = {"适用专业", "计算机", "化工", "医学", "考研"};
    private String[][] categories = {{"书本类别"}, {"数据库", "计算机网络", "python", "android", "算法"}, {"工业化学", "工程热力学", "流体力学", "理论力学", "化工原理"}, {"内科学", "外科学", "病理学", "生理学", "生物化学"}, {"高等数学", "英语", "政治", "专业课"}};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        productDBDao = new ProductDBDao(mContext);
        productDBDao.openDataBase();

        userDBDao = new UserDBDao(mContext);
        userDBDao.openDataBase();

        //从bundle中读取存入的username,pid
        Bundle bundle = getIntent().getBundleExtra("bundle_to_addproduct");

        username = bundle.getString("username_to_addproduct");
        pid = bundle.getString("pid_to_addproduct");

        //获取对应布局按键
        tv_addproduct_username = findViewById(R.id.tv_addproduct_username);
        et_product_title = findViewById(R.id.et_product_title);

        et_product_price = findViewById(R.id.et_product_price);
        et_product_description = findViewById(R.id.et_product_description);
        b_add_product = findViewById(R.id.b_add_product);
        iv_addphoto = findViewById(R.id.iv_addphoto);
        sp_grade = findViewById(R.id.sp_grade);
        sp_major = findViewById(R.id.sp_major);
        sp_category = findViewById(R.id.sp_category);

        //显示用户名
        tv_addproduct_username.setText(username);


        //若是从我的发布页面选择修改商品信息跳转过来则pid不为空，自动填入商品信息
        if (pid != null) {

            Product product = productDBDao.selectProductbypid(pid);

            byte[] picture = product.getPicture();
            //从字节数组中解码生成不可变的位图
            //public static Bitmap decodeByteArray(byte[] data, int offset, int length)
            Bitmap img = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            iv_addphoto.setImageBitmap(img);
            Float price1 = product.getPrice();
            String price2 = price1.toString();
            et_product_title.setText(product.getTitle());
            et_product_price.setText(price2);
            et_product_description.setText(product.getDescription());

//            String label_total = product.getLabel();
//            int index1 = StringUtils.ordinalIndexOf(label_total, ";", 1);
//            String label1 = label_total.substring(0, index1);
//            System.out.println(label1);
//            int index2 = StringUtils.ordinalIndexOf(label_total,";", 2);
//            String label2 = label_total.substring(index1+1, index2);
//            System.out.println(label2);
//            int index3 = StringUtils.ordinalIndexOf(label_total, ";", 3);
//            String label3 = label_total.substring(index2+1, index3);
//            System.out.println(label3);
//            setSpinnerItemSelectedByValue(sp_grade, label1);
//            setSpinnerItemSelectedByValue(sp_major, label2);
//            setSpinnerItemSelectedByValue(sp_category, label3);

        }

        //spinner适配器绑定
        gradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, grade);
        sp_grade.setAdapter(gradeAdapter);

        majorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, major);
        sp_major.setAdapter(majorAdapter);

        categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        sp_category.setAdapter(categoryAdapter);

        //监听适用年级spinner
        sp_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_grade = (String) sp_grade.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //监听适用专业的spinner选项,与书本类别级联
        sp_major.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] category = categories[position];

                select_major = (String) sp_major.getSelectedItem();

                categoryAdapter.clear();

                categoryAdapter.addAll(category);

                sp_category.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //监听书本类别的spinner
        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_category = (String) sp_category.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //上传图片按钮事件
        iv_addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);

            }
        });

        //发布按钮事件
        b_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userDBDao.queryUsername(username).getCard_identify() == 1){//如果此用户已进行校园卡认证，则可以发布商品
                    if (CheckInput()) {
                        Product product = new Product();
                        //将图片转化为bitmap格式
                        BitmapDrawable drawable = (BitmapDrawable) iv_addphoto.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        //二进制数组输出流
                        ByteArrayOutputStream byStream = new ByteArrayOutputStream();
                        //将图片压缩成质量为30的JPEG格式图片
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byStream);
                        //把输出流转换为二进制数组
                        byte[] byteArray = byStream.toByteArray();

                        //product进行赋值

                        product.setTitle(et_product_title.getText().toString());
                        product.setLabel(label);
                        product.setPrice(Float.parseFloat(et_product_price.getText().toString()));
                        product.setDescription(et_product_description.getText().toString());
                        product.setPicture(byteArray);
                        product.setUid(username);

//                    for(int i=0;i<byteArray.length;i++)
//                    {
//                        System.out.println(byteArray[i]);
//                    }

                        if (pid == null){

                            if (productDBDao.AddProduct(product)) {

                                Toast.makeText(getApplicationContext(), "商品信息发布成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "商品信息发布失败", Toast.LENGTH_SHORT).show();
                            }

                        }else {

                            productDBDao.update_productinfo(pid, et_product_title.getText().toString(), label, Float.parseFloat(et_product_price.getText().toString()),
                                    et_product_description.getText().toString(), byteArray);
                            Toast.makeText(AddproductActivity.this, "商品信息修改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                }else{
                    Toast.makeText(AddproductActivity.this, "您未进行校园卡识别，暂时不能发布商品", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            //从相册返回的数据
            if (data != null) {
                //得到图片的全路径
                Uri uri = data.getData();
                iv_addphoto.setImageURI(uri);
            }
        }
    }

    public boolean CheckInput() {
        String title = et_product_title.getText().toString();
        String price = et_product_price.getText().toString();
        String description = et_product_description.getText().toString();
        label = select_grade + ";" + select_major + ";" + select_category + ";";
        System.out.println(label);
        if (title.trim().equals("")) {
            Toast.makeText(this, "商品标题不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (price.trim().equals("")) {
            Toast.makeText(this, "商品价格不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (label.equals("适用年级;适用专业;书本类别;")) {
            Toast.makeText(this, "商品分类未选择!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description.trim().equals("")) {
            Toast.makeText(this, "商品描述不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }




    //根据值，设置spinner默认选中项
    public  void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for(int i=0; i<k; i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
//                spinner.setSelection(i,true);// 默认选中项
                spinner.setSelection(i);// 默认选中项

                break;
            }
        }
    }
}
