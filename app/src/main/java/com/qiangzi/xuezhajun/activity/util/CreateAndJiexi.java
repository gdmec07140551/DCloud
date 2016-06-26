package com.qiangzi.xuezhajun.activity.util;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qiangzi.xuezhajun.activity.activity.MyApplication;
import com.qiangzi.xuezhajun.activity.activity.RGBLuminanceSource;


import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

public class CreateAndJiexi {
	private static final int QR_WIDTH=200;
	private static final int QR_HEIGHT=200;
	// ����QRͼ
    public Bitmap createImage(String text) {
    	 Bitmap bitmap = null;
        try {
            // ��Ҫ����core��
            QRCodeWriter writer = new QRCodeWriter();

            Log.i("aa", "���ɵ��ı���" + text);
            if (text == null || "".equals(text) || text.length() < 1) {
            	 Toast.makeText(MyApplication.mainactivity, "格式错误", Toast.LENGTH_SHORT).show();
                 
            }

            // ��������ı�תΪ��ά��
            BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT);

            System.out.println("w:" + martix.getWidth() + "h:"
                    + martix.getHeight());

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }

                }
            }

            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
      

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap; 
    }
	// ����QRͼƬ
    public String  scanningImage(Bitmap bitmap) {
    	String str="";
        Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

        // ��ô�������ͼƬ
   
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
       Result result;
       
            try {
				result = reader.decode(bitmap1, hints);
				 str=result.getText();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(MyApplication.mainactivity, "格式错误", Toast.LENGTH_LONG).show();
			} 
            // �õ������������
       
      
        return str;
    }
}
