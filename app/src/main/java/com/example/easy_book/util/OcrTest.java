package com.example.easy_book.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.net.URLEncoder;

/**
 * 通用文字识别
 */
public class OcrTest {

    Context mContext;

    AccessToken accessToken = new AccessToken();

    public OcrTest(Context context){
        mContext = context;
    }

    public  String ocrtest(Uri uri) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
        try {

            String accesstoken = accessToken.getAuth();

            Log.i("token", accesstoken);
            // 本地文件路径
            String realpath = FileUtil.getFileAbsolutePath(mContext, uri);
            Log.i("realpath",  realpath);
            byte[] imgData = FileUtil.readFileByBytes(realpath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String result = HttpUtil.post(url, accesstoken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}