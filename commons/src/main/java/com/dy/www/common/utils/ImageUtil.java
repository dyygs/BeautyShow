package com.dy.www.common.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dy on 2017/2/10.
 */

public class ImageUtil {


    public static void saveImageToGallery(Context context, Bitmap bmp, String filePath) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), filePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + appDir.getAbsolutePath())));
    }

    public static void saveToSD(Context context, InputStream inputStream, String filePath, String title, int contentLength) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), filePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = title + ".mp4";
        File file = new File(appDir, fileName);
        try {
            // 打开一个已存在文件的输出流
            FileOutputStream fos = new FileOutputStream(file);
            // 将输入流is写入文件输出流fos中
            int ch = 0;
            try {
                while((ch = inputStream.read()) != -1){
                    fos.write(ch);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally{
                //关闭输入流等
                fos.close();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
