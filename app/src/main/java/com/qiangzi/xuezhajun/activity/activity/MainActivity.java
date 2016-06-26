package com.qiangzi.xuezhajun.activity.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.GsonBuilder;
import com.qiangzi.xuezhajun.activity.util.CreateAndJiexi;
import com.qiangzi.xuezhajun.activity.util.MyThread;
import com.qiangzi.xuezhajun.activity.util.WriteAanRead;
import com.qiangzi.xuezhajun.dcloud.R;
import com.qiangzi.xuezhajun.activity.util.Core;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private TextView qr_result;
	private ImageView QQ_picture,WEIXIN_picture,WEIBO_picture,SHOW_picture;
	private EditText qr_text;
	private Button shengcheng,jiexi,picture1,picture2,picture3;
	private CreateAndJiexi cj;
	Bitmap myBitmap;
	private byte[] mContent;
	private WriteAanRead wr ;
	private int CHOOSE;
	private String coreJson,str;
	public Myhandle myhandle = new Myhandle();
	public class Myhandle extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				str =(String) msg.obj;
				System.out.println("handle"+str);
				SHOW_picture.setImageBitmap(cj.createImage(str));
			}
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//͸��״̬��
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//͸��������
		setContentView(R.layout.activity_main);
		MyApplication.mainactivity=this;
		qr_result=(TextView) findViewById(R.id.qr_result);
		QQ_picture=(ImageView) findViewById(R.id.QQ_picture);
		WEIXIN_picture=(ImageView) findViewById(R.id.WEIXIN_picture);
		WEIBO_picture=(ImageView) findViewById(R.id.WEIBO_picture);
		SHOW_picture=(ImageView) findViewById(R.id.SHOW_picture);
	

		qr_text=(EditText) findViewById(R.id.editText1);
		shengcheng=(Button) findViewById(R.id.shengcheng);
		jiexi=(Button) findViewById(R.id.jiexi);
		picture1=(Button) findViewById(R.id.picture1);
		picture2=(Button) findViewById(R.id.picture2);
		picture3=(Button) findViewById(R.id.picture3);
		shengcheng.setOnClickListener(this);
		jiexi.setOnClickListener(this);
		picture1.setOnClickListener(this);
		picture2.setOnClickListener(this);
		picture3.setOnClickListener(this);
		cj= new CreateAndJiexi();
		wr=new WriteAanRead();
		
		
		SHOW_picture.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				  AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				  builder.setItems(new String[]{"保存到文件夹"},new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						SHOW_picture.setDrawingCacheEnabled(true);
	                        Bitmap imageBitmap = SHOW_picture.getDrawingCache();
	                        if (imageBitmap != null) {
	                            new SaveImageTask().execute(imageBitmap);
	                        }
	                    }
					
				  });
	                  
				  builder.show();
				return true;
			}
		});
			
		
	
	}
	
	
	
	public void initBuilder(){
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				MainActivity.this);
		builder.setTitle("");

		builder.setPositiveButton("拍照",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface

					dialog, int which) {
				Intent intent = new Intent(
						"android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, 0);

			}
		});
		builder.setNegativeButton("图库",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface

					dialog, int which) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 1);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	//������ά��
	public void jiexi(){
		Bitmap bitmap1 = ((BitmapDrawable)QQ_picture.getDrawable()).getBitmap();
		System.out.println("QQ-------"+cj.scanningImage(bitmap1));
		Bitmap bitmap2 = ((BitmapDrawable)WEIXIN_picture.getDrawable()).getBitmap();
		System.out.println("微信"+cj.scanningImage(bitmap2));
		Bitmap bitmap3 = ((BitmapDrawable)WEIBO_picture.getDrawable()).getBitmap();
		System.out.println("微博----"+cj.scanningImage(bitmap3));
		Core c = new Core();
		c.setQq(cj.scanningImage(bitmap1));
		c.setWeixin(cj.scanningImage(bitmap2));
		c.setWeibo(cj.scanningImage(bitmap3));
		System.out.println("GETQQ"+c.getQq());
		//Gson g = new Gson();
		//coreJson= g.toJson(c);
		 GsonBuilder gb =new GsonBuilder();
		 gb.disableHtmlEscaping();
		 coreJson= gb.create().toJson(c);
		MyThread mythread = new MyThread(coreJson);
		mythread.start();
		System.out.println("ת���󡪡�����������������"+coreJson);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.jiexi:
			Toast.makeText(this, "生成成功", Toast.LENGTH_LONG).show();
			jiexi();
			
			break;
		case R.id.shengcheng:
			if(qr_text==null||qr_text.length()<=0){
				qr_text.requestFocus();
				qr_text.setError("格式错误");
				return;
			}
			SHOW_picture.setImageBitmap(cj.createImage(qr_text.getText().toString()));
		
			break;
		case R.id.picture1:
			CHOOSE=0;
			initBuilder();
			break;
		case R.id.picture2:
			CHOOSE=1;
			initBuilder();

			break;
		case R.id.picture3:
			CHOOSE=2;
			initBuilder();

			break;
		default:

			break;
		}
	}





	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		ContentResolver resolver = getContentResolver();
		/**
		 * ��������� ���߲�ѡ��ͼƬ���� ��ִ���κβ���
		 */

		if (data != null) {
			/**
			 * ��Ϊ���ַ�ʽ���õ���startActivityForResult�������������ִ����󶼻�ִ��onActivityResult����
			 * �� ����Ϊ�����𵽵�ѡ�����Ǹ���ʽ��ȡͼƬҪ�����ж�
			 * �������requestCode��startActivityForResult����ڶ���������Ӧ 1== ��� 2 ==���
			 */
			if (requestCode == 1) {

				try {
					// ���ͼƬ��uri
					Uri originalUri = data.getData();
					// ��ͼƬ���ݽ������ֽ�����
					mContent =WriteAanRead.readStream(resolver.openInputStream(Uri
							.parse(originalUri.toString())));
					// ���ֽ�����ת��ΪImageView�ɵ��õ�Bitmap����
					OutputStream os = new ByteArrayOutputStream();
					myBitmap =WriteAanRead.getPicFromBytes(mContent, null);
					
					
					// //�ѵõ���ͼƬ���ڿؼ�����ʾ
					if(CHOOSE==0){
						QQ_picture.setImageBitmap(myBitmap);
					}else if(CHOOSE==1){
						WEIXIN_picture.setImageBitmap(myBitmap);
					}else if(CHOOSE==2){
						WEIBO_picture.setImageBitmap(myBitmap);
					}else{

						System.out.println("�Ǻ�");
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

			} else if (requestCode == 0) {

				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
					return;
				}
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
				FileOutputStream b = null;
				File file = new File("/sdcard/myImage/");
				file.mkdirs();// �����ļ��У�����Ϊmyimage

				// ��Ƭ��������Ŀ���ļ����£��Ե�ǰʱ�����ִ�Ϊ���ƣ�����ȷ��ÿ����Ƭ���Ʋ���ͬ��
				String str = null;
				Date date = null;
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// ��ȡ��ǰʱ�䣬��һ��ת��Ϊ�ַ���
				date = new Date();
				str = format.format(date);
				String fileName = "/sdcard/myImage/" + str + ".jpg";
				try {
					b = new FileOutputStream(fileName);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (data != null) {
						Bitmap cameraBitmap = (Bitmap) data.getExtras().get(
								"data");
						System.out.println("fdf================="
								+ data.getDataString());
						if(CHOOSE==0){
							QQ_picture.setImageBitmap(cameraBitmap);
						}else if(CHOOSE==1){
							WEIXIN_picture.setImageBitmap(cameraBitmap);
						}else if(CHOOSE==2){
							WEIBO_picture.setImageBitmap(cameraBitmap);
						}else{

							System.out.println("�Ǻ�");
						}


						System.out.println("�ɹ�======" + cameraBitmap.getWidth()
						+ cameraBitmap.getHeight());
					}

				}
			}
		}
	}

	class SaveImageTask extends AsyncTask<Bitmap, Void, String>{

		@Override
		protected String doInBackground(Bitmap... params) {
			  String result = "保存失败";
	           try {
	               String sdcard = Environment.getExternalStorageDirectory().toString();

	               File file = new File(sdcard + "/Download");
	               if (!file.exists()) {
	                   file.mkdirs();
	               }

	               File imageFile = new File(file.getAbsolutePath(),new Date().getTime()+".jpg");
	               FileOutputStream outStream = null;
	               outStream = new FileOutputStream(imageFile);
	               Bitmap image = params[0];
	               image.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
	               outStream.flush();
	               outStream.close();
	               result = "保存成功";
	           } catch (Exception e) {
	               e.printStackTrace();
	           }
	           return result;
	       }
		@Override
		protected void onPostExecute(String result) {
			
			  Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
			  SHOW_picture.setDrawingCacheEnabled(true);
			
		}
		}
	
		
	}
	  

