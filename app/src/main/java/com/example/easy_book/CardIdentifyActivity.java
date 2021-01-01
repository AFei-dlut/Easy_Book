package com.example.easy_book;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.easy_book.util.FileUtil;
import com.example.easy_book.util.OcrTest;
import com.example.easy_book.util.UserDBDao;
import com.wega.library.loadingDialog.LoadingDialog;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;

public class CardIdentifyActivity extends AppCompatActivity {

    private Button b_select_photo;
    private String nickname, college, grade, major;
    private ImageView iv_card_test;
    private UserDBDao userDBDao;
    private static String username;
    private Context mContext = this;
    OcrTest ocrtest = new OcrTest(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_identify);


        b_select_photo = findViewById(R.id.b_select_photo);
        iv_card_test = findViewById(R.id.iv_card_test);

        Intent intent = getIntent();
        //从intent取出bundle
        Bundle bundle = intent.getBundleExtra("bundle_to_card");

        nickname = bundle.getString("nickname_to_card");
        grade = bundle.getString("grade_to_card");
        college = bundle.getString("college_to_card");
        major = bundle.getString("major_to_card");
        username = bundle.getString("username_to_card");

        System.out.println(nickname + grade + college + major);


        /*强制主线程执行网络请求，适用于请求量小的任务*/
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        b_select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nickname != null && grade != null && college != null && major != null){
                    alreadyDialog();
                }
                else {
                    //经过提示框注意事项后放可以选择图片
                    cardDialog();
                }
            }
        });

    }


    private void identifyDialog(String nickname, String grade, String college, String major){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("校园卡识别为：" + "\n\r" +  "昵称：" + nickname + "\n\r" + "年级：" + grade
                + "\n\r" + "学院：" + college + "\n\r" + "专业：" + major + "\n\r" + "已同步到数据库，请返回查看");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                       finish();
                    }
                });

        AlertDialog dialog=builder.create();
        dialog.show();
    }




    private void cardDialog(){

        //校园卡认证

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("校园卡图片建议平放在桌面上横放拍摄！建议裁剪至只有校园卡部分哦！若无法识别，请裁剪后重试！");
        builder.setPositiveButton("我知道了",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //获取存取权限
                        FileUtil.verifyStoragePermissions(CardIdentifyActivity.this);

                        //选择图片
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, 2);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void alreadyDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("您已经进行过校园卡认证，确定要再次认证吗？");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cardDialog();
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){


        super.onActivityResult(requestCode, resultCode, data);


        System.out.println(username);

        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();

                System.out.println(uri);

                iv_card_test.setImageURI(uri);


                Toast.makeText(CardIdentifyActivity.this,"校园卡识别中，请稍后！", Toast.LENGTH_LONG).show();


                String result = ocrtest.ocrtest(uri);


                //获取姓名
                int index1 = StringUtils.ordinalIndexOf(result,":",5);
                int index2 = StringUtils.ordinalIndexOf(result,"\"", 10);
                String nickname = result.substring(index1+1,index2);

                //获取年级
                int index3 = StringUtils.ordinalIndexOf(result,":",11);
                String grade = result.substring(index3+1, index3+5);

                //获取学院
                int index5 = StringUtils.ordinalIndexOf(result,":",7);
                int index6 = StringUtils.ordinalIndexOf(result,"\"",14);
                String college = result.substring(index5+1, index6);

                //获取专业
                int index7 = StringUtils.ordinalIndexOf(result,":",9);
                int index8 = StringUtils.ordinalIndexOf(result,"\"",18);
                String major = result.substring(index7+1, index8);

                System.out.println(nickname + grade + college + major);


                //将校园卡图片压缩改二进制存储
                BitmapDrawable drawable = (BitmapDrawable) iv_card_test.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                //二进制数组输出流
                ByteArrayOutputStream byStream = new ByteArrayOutputStream();
                //将图片压缩成质量为30的JPEG格式图片
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byStream);
                //把输出流转换为二进制数组
                byte[] byteArray = byStream.toByteArray();


                userDBDao = new UserDBDao(mContext);
                userDBDao.openDataBase();

                userDBDao.update_cardinfo_by_username(username, nickname, grade, college, major, byteArray, 1);

                identifyDialog(nickname, grade, college, major);

            }
        }
    }


}
