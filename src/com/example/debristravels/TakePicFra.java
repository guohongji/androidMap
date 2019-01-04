package com.example.debristravels;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.helper.DBOpenHelper;
import com.example.database.helper.MyDBHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static android.app.Activity.RESULT_OK;


public class TakePicFra extends Fragment{
	//����ͼƬ�󷵻صĽ������
	private ImageView imageView;
	private ImageButton openCameraBtn;
	private ImageButton clearBackground;
	//���浽��¼
	private ImageButton saveToRecord;

	//���ݿ�
	SQLiteDatabase db;

    MyDBHelper myDBHelper = null;


	//�����������
	private RelativeLayout showTextInput;
	private ImageButton confirm;
	private ImageButton cancel;
	private EditText describe;
	String fileName;

	private final  int REQ_1 = 1;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.take_picture, container, false);

		//��ʼ�����ݿ�
		myDBHelper = new MyDBHelper(getActivity());
		db =  myDBHelper.getDB();


		//��ʼ���ؼ�
		initView(view);

		//��ʼ������
		initLayoutListener(view);



		clearBackground.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imageView.setImageDrawable(null);

			}
		});


		openCameraBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getActivity(),MyCamera.class),REQ_1);
			}
		});
		return view;
	}

	private void initLayoutListener(View view) {
		confirm = (ImageButton) view.findViewById(R.id.confirm);
		cancel = (ImageButton) view.findViewById(R.id.cancel);
		//
		confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO ��������
				String des = describe.getText().toString();

				ContentValues values = new ContentValues();

					values.put("fileName",fileName);
				values.put("describe",des);


					db.insert(MyDBHelper.TBL_NAME, null, values);
					values.clear();
				showTextInput.setVisibility(View.GONE);
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showTextInput.setVisibility(View.GONE);

			}
		});


	}

	private void initView(View view) {
        //��ʼ����ʾ��
		imageView = (ImageView) view.findViewById(R.id.imageViewTkPic);
		//��ʼ���������
		openCameraBtn = (ImageButton) view.findViewById(R.id.openCameraBtn);
		clearBackground = (ImageButton) view.findViewById(R.id.clearBackground);
		saveToRecord = (ImageButton) view.findViewById(R.id.saveToRecord);

		//��ʼ�������������

		describe = (EditText) view.findViewById(R.id.describe);
		showTextInput = (RelativeLayout) view.findViewById(R.id.show_text_input);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			if(requestCode == REQ_1){
				Log.i("selfCamera","executed1");
				//Bundle bundle = data.getExtras();
				String path = data.getStringExtra("picPath");
				fileName = data.getStringExtra("fileName");
				try {
					//����������90�Ƚ��ٷ�����
					FileInputStream fis = new FileInputStream(path);
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					Matrix matrix = new Matrix();
					matrix.setRotate(90);
					bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
					imageView.setImageBitmap(bitmap);

					//�õ�ͼƬ��ͼƬ��id���浽�����ĵ���������,�������ݿ�����Ӹ���ͼƬ��id��¼
					//��������id��¼��ʱ���浽�����ĵĴ洢��ContentValues
					//������,ͼƬ��������Ϣ,ͨ��ContentValues�����ݿ�洢������Ϣ



//					ContentValues values = new ContentValues();
//					Log.i("fileName",fileName);
//					values.put("fileName",fileName);
//
//
//					//�������ݿ⹤����
//
//					db.insert(TBL_NAME, null, values);
//					values.clear();


					//����:


					Cursor cursor   = db.rawQuery("select * from pic_des_tb", null);
					if(cursor!=null) {
						Log.i("cursor","cursor2");
						cursor.moveToFirst();

						String [] columns = cursor.getColumnNames();
						Log.i("test",columns[0]);
						while(cursor.moveToNext()) {
							for(String columnName : columns) {
								Log.i("cursor",cursor.getString(cursor.getColumnIndex(columnName))+"");
							}
						}
						cursor.close();
					}else{
						Log.i("cursor","cursor3");
						Toast.makeText(getActivity(),"ľ������", Toast.LENGTH_LONG).show();
					}








					//TODO ��ʾ��������� DONE

					getInputText();

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getInputText(){
    showTextInput.setVisibility(View.VISIBLE);
		return null;

	}



}
