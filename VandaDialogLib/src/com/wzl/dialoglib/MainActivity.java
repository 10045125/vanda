package com.wzl.dialoglib;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.wzl.vandan.dialog.VandaAlert;
import com.wzl.vandan.dialog.VandaAlert.OnOk;
import com.wzl.vandan.dialog.VandaAlert.PhotoDialogInterface;
import com.wzl.vandan.dialog.VandaAlert.radioInterface;

public class MainActivity extends Activity {

	private ArrayList<String> mArrayListValues;
	private ArrayList<Integer> mArrayListCode;

	// @InjectView(R.id.tv_1)
	// TextView mTextView;

	// @OnClick
	// public void tv_1(View view) {
	// Toast.makeText(this, "jing", Toast.LENGTH_SHORT).show();
	// onTextViewOnClick(view);
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ButterKnife.inject(this);
		initData();
		setData();
	}

	private void initData() {
		mArrayListValues = new ArrayList<String>();
		mArrayListCode = new ArrayList<Integer>();
		mArrayListValues.add("中国");
		mArrayListValues.add("美国");
		mArrayListValues.add("英国");

		mArrayListCode.add(1);
		mArrayListCode.add(2);
		mArrayListCode.add(3);

	}

	private void setData() {

	}

	public void onTextViewOnClick(View view) {
		VandaAlert.createSelectWayDialog(this, mArrayListValues,
				mArrayListCode, "选择国家", new radioInterface<Integer>() {

					@Override
					public void setId(int position, String value, Integer id) {
						Toast.makeText(MainActivity.this,
								"value:" + value + "  code:" + id,
								Toast.LENGTH_SHORT).show();
					}
				}).show();
	}

	public void onTextViewOnClickTake(View view) {
		VandaAlert.createPhotoDialog(this, new PhotoDialogInterface() {

			@Override
			public void takingPicture(Dialog mDialog) {
				mDialog.dismiss();
				Toast.makeText(MainActivity.this, "拍照", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void takingCellPicture(Dialog mDialog) {
				mDialog.dismiss();
				Toast.makeText(MainActivity.this, "手机相册取照片", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void cancel(Dialog mDialog) {
				mDialog.dismiss();
				Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT)
						.show();
			}
		}).show();
	}

	public void onTextViewOnClickCancel(View view) {
		VandaAlert.CreateOKorNODialog(this, "是否取消？", new OnOk() {

			@Override
			public void setOk(Dialog dialog) {
				dialog.dismiss();
				Toast.makeText(MainActivity.this, "确定", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void setCancel(Dialog dialog) {
				dialog.dismiss();
				Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT)
						.show();
			}
		}).show();
	}

	public void onTextViewOnClickProgress(View view) {
		VandaAlert.createLoadingDialog(this, "").show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
