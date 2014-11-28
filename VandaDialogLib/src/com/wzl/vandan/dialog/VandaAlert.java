package com.wzl.vandan.dialog;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.wzl.dialoglib.R;

/**
 * @author vanda 伍中联
 *
 */
public final class VandaAlert {
	private static int checkId = 0;

	/**
	 * @author vanda 伍中联
	 *
	 * @param <T>
	 *            条目代码的泛型话
	 */
	public interface radioInterface<T> {
		/**
		 * @param position
		 *            选中条目的位置
		 * @param value
		 *            选中的条目值
		 * @param id
		 *            对应选中条目的代码
		 */
		public void setId(int position, String value, T id);
	}

	/**
	 * @param <T>
	 * @param <T>
	 * @param context
	 *            上下文
	 * @param mListValue
	 *            显示单选条目的值
	 * @param mListCode
	 *            选中条目后的返回代码，参数是T，必须保证mListValue的大小和mListCode的大小一致
	 * @param title
	 *            Dialog 的标题参数
	 * @param l
	 *            选中后的回调接口
	 * @return 创建的Dialog
	 */
	public static <T> Dialog createSelectWayDialog(final Context context,
			final List<String> mListValue, final List<T> mListCode,
			String title, final radioInterface<T> l) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_select_way, null);
		TextView mTextViewTitle = (TextView) v.findViewById(R.id.title);
		mTextViewTitle.setText(title);
		final Dialog mCreateSelectWayDialog = new Dialog(context,
				R.style.DialogStyleForRadio);
		mCreateSelectWayDialog.setCancelable(false);
		mCreateSelectWayDialog.setContentView(v);
		LayoutParams params = mCreateSelectWayDialog.getWindow()
				.getAttributes();
		@SuppressWarnings("deprecation")
		int width = (int) (((Activity) context).getWindowManager()
				.getDefaultDisplay().getWidth());
		params.width = (int) (width * 0.9);
		RadioGroup mRadioGroup = (RadioGroup) v.findViewById(R.id.radiogroup1);
		if (mListValue != null)
			for (int i = 0; i < mListValue.size(); i++) {
				RadioButton v_ = (RadioButton) inflater.inflate(
						R.layout.radio_button_item, null);
				if (i == 0) {
					v_.setChecked(true);
					checkId = 0;
				}
				v_.setId(i);
				v_.setText(mListValue.get(i));
				mRadioGroup.addView(v_);
			}
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				checkId = checkedId;
			}
		});
		Button mBtnOk = (Button) v.findViewById(R.id.dialog_select_way_btn);
		Button mBtnClose = (Button) v
				.findViewById(R.id.dialog_select_way_colse);
		mBtnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCreateSelectWayDialog.dismiss();
			}
		});
		mBtnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCreateSelectWayDialog.dismiss();
				if (l != null) {
					l.setId(checkId, mListValue.get(checkId),
							mListCode.get(checkId));
				}
			}
		});
		return mCreateSelectWayDialog;
	}

	/**
	 * @author vanda 伍中联
	 *
	 */
	public interface PhotoDialogInterface {
		/**
		 * @param mDialog
		 *            拍照
		 */
		public void takingPicture(Dialog mDialog);

		/**
		 * @param mDialog
		 *            从手机里获取图片
		 */
		public void takingCellPicture(Dialog mDialog);

		/**
		 * @param mDialog
		 *            取消操作
		 */
		public void cancel(Dialog mDialog);
	}

	/**
	 * @param context
	 *            上下文
	 * @param l
	 *            回调接口
	 * @return Dialog
	 */
	public static Dialog createPhotoDialog(final Context context,
			final PhotoDialogInterface l) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.my_photo_dialog, null);
		Button mButtonTakePic = (Button) v
				.findViewById(R.id.photo_take_picture);
		Button mButtonTakeCellPic = (Button) v
				.findViewById(R.id.photo_take_cell_picture);
		Button mButtonCancel = (Button) v.findViewById(R.id.photo_cancel);

		final Dialog mcreatePhotoDialog = new Dialog(context,
				R.style.photo_dialog);
		Window w = mcreatePhotoDialog.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.y = 20;
		mcreatePhotoDialog.setCancelable(true);
		mcreatePhotoDialog.setContentView(v);
		mButtonTakePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (l != null) {
					l.takingPicture(mcreatePhotoDialog);
				}
			}
		});
		mButtonTakeCellPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (l != null) {
					l.takingCellPicture(mcreatePhotoDialog);
				}
			}
		});
		mButtonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (l != null) {
					l.cancel(mcreatePhotoDialog);
				}
			}
		});
		return mcreatePhotoDialog;
	}

	/**
	 * @author vanda 伍中联
	 *
	 */
	public interface OnOk {
		/**
		 * @param dialog
		 *            确定方法
		 */
		public void setOk(Dialog dialog);

		/**
		 * @param dialog
		 *            取消方法
		 */
		public void setCancel(Dialog dialog);
	}

	/**
	 * @param context
	 *            上下文
	 * @param title
	 *            Dialog 的标题
	 * @param l
	 *            回调接口
	 * @return Dialog
	 */
	public static Dialog CreateOKorNODialog(Context context, String title,
			final OnOk l) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_yes_or_no, null);
		Button mButtonOk = (Button) v.findViewById(R.id.dialog_ok);
		Button mButtonCancel = (Button) v.findViewById(R.id.dialog_cancel);
		TextView tipTextView = (TextView) v
				.findViewById(R.id.dialog_ok_or_cancel_title);
		tipTextView.setText(title);
		final Dialog mCreateOKorNODialog = new Dialog(context,
				R.style.yes_or_no_dialog);
		mCreateOKorNODialog.setCancelable(false);
		mCreateOKorNODialog.setContentView(v);
		LayoutParams params = mCreateOKorNODialog.getWindow().getAttributes();
		@SuppressWarnings("deprecation")
		int width = (int) (((Activity) context).getWindowManager()
				.getDefaultDisplay().getWidth());
		params.width = (int) (width * 0.8);
		mButtonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (l != null)
					l.setOk(mCreateOKorNODialog);
			}
		});
		mButtonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (l != null)
					l.setCancel(mCreateOKorNODialog);
			}
		});
		return mCreateOKorNODialog;
	}

	public static Dialog createLoadingDialog(final Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.progress, null);
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
		loadingDialog.setCancelable(true);
		loadingDialog.setContentView(v);
		return loadingDialog;
	}
}
