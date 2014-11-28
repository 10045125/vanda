package com.vanda.vandalib.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.vanda.vandalibnetwork.R;
import com.vanda.vandalibnetwork.application.AppData;
import com.vanda.vandalibnetwork.utils.BitmapUtils;
import com.vanda.vandalibnetwork.utils.CacheUtils;
import com.wzl.vandan.dialog.VandaAlert;
import com.wzl.vandan.dialog.VandaAlert.PhotoDialogInterface;

/**
 * @author vanda 伍中联
 *
 *         处理摄像拍照逻辑，主要是将获取的相片进行处理，以文件路径的形式。处理过程中将图片进行压缩处理
 */
public class MyCamera {

	private static final int CAMERA_RESULT = 0;

	private static final int PIC_RESULT = 1;

	private static final int PIC_RESULT_KK = 2;

	private Uri mUri;
	private Context mContext;
	private UpdataUiForPhoto mUpdataUiForPhoto = null;
	private Object object;

	public MyCamera(Context context, Object object) {
		mContext = context;
		this.object = object;
	}

	/**
	 * @param l
	 *            处理成功后的UI 更新接口回调。失败无任何返回
	 */
	public void setUpdataUiForPhotoInterface(UpdataUiForPhoto l) {
		mUpdataUiForPhoto = l;
	}

	/**
	 * 显示弹框 和 拍照初始化
	 */
	public void showPhotoAndProcess() {
		VandaAlert.createPhotoDialog(mContext, new PhotoDialogInterface() {

			@Override
			public void takingPicture(Dialog mDialog) {
				mDialog.dismiss();

				mUri = mContext.getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						new ContentValues());
				if (mUri != null) {
					Intent i = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mUri);
					if (BitmapUtils.isIntentSafe((Activity) mContext, i)) {
						((Activity) mContext).startActivityForResult(i,
								CAMERA_RESULT);
					} else {
						Toast.makeText(mContext, R.string.camera_no,
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(mContext, R.string.camera_no_link,
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void takingCellPicture(Dialog mDialog) {
				mDialog.dismiss();
				if (BitmapUtils.isKK()) {
					Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					if (object instanceof Fragment) {
						((Fragment) object).startActivityForResult(intent,
								PIC_RESULT_KK);
					} else {
						((Activity) mContext).startActivityForResult(intent,
								PIC_RESULT_KK);
					}

				} else {
					Intent choosePictureIntent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					if (object instanceof Fragment) {
						((Fragment) object).startActivityForResult(
								choosePictureIntent, PIC_RESULT);
					} else {
						((Activity) mContext).startActivityForResult(
								choosePictureIntent, PIC_RESULT);
					}
				}
			}

			@Override
			public void cancel(Dialog mDialog) {
				mDialog.dismiss();
			}
		}).show();
	}

	/**
	 * @author vanda
	 *
	 *         回调自定义路径，如果不设置，将使用默认设置
	 */
	public interface TakePhotoInterface {
		public String getTakingPicturePath();

		public String getTakingCellPicturePath();
	}

	public void processonActivityResult(int requestCode, int resultCode,
			Intent data, TakePhotoInterface l) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case CAMERA_RESULT:
				new ConvertTask(mUri, l != null ? l.getTakingPicturePath()
						: CacheUtils.getExternalCachePath(AppData.getContext())
								+ "myCameraPhoto.jpg").execute();
				break;
			case PIC_RESULT:
			case PIC_RESULT_KK:
				mUri = data.getData();
				new ConvertTask(mUri, l != null ? l.getTakingCellPicturePath()
						: CacheUtils.getExternalCachePath(AppData.getContext())
								+ "myCameraPhoto.jpg").execute();
				break;
			}
		}
	}

	public interface UpdataUiForPhoto {
		public void updataUiForPhotoPath(String path);
	}

	private class ConvertTask extends AsyncTask<Void, Void, String> {

		ContentResolver mContentResolver;
		Uri uri;
		String path;

		public ConvertTask(Uri uri, String path) {
			this.uri = uri;
			this.path = path;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mContentResolver = mContext.getContentResolver();
		}

		@Override
		protected String doInBackground(Void... params) {
			if (uri == null)
				return null;
			try {
				InputStream inputStream = mContentResolver.openInputStream(uri);
				if (TextUtils.isEmpty(path)) {
					return null;
				}
				File file = new File(path);
				file.getParentFile().mkdirs();
				if (file.exists() || file.length() > 0) {
					file.delete();
				}
				file.createNewFile();
				BitmapUtils.copyFile(inputStream, file);
				BitmapUtils.compressImageFromFile(path);
				if (BitmapUtils.isThisBitmapCanRead(path)) {
					return path;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			if (TextUtils.isEmpty(s)) {
				Toast.makeText(mContext, R.string.camera_no_file,
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (mUpdataUiForPhoto != null) {
				mUpdataUiForPhoto.updataUiForPhotoPath(s);
			}
		}
	}

}
