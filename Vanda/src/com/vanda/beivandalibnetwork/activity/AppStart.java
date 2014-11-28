package com.vanda.beivandalibnetwork.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.vanda.beivandalibnetworkdemo.R;

/**
 * 应用程序启动类：显示欢迎界面并跳转到主界面
 * 
 */
public class AppStart extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final View view = View.inflate(this, R.layout.start, null);
		// LinearLayout wellcome = (LinearLayout) view
		// .findViewById(R.id.app_start_view);
		setContentView(view);

		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(4000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});
	}

	/**
	 * 分析显示的时间
	 * 
	 * @param time
	 * @return
	 */
	// private long[] getTime(String time) {
	// long res[] = new long[2];
	// try {
	// time = time.substring(0, time.indexOf("."));
	// String t[] = time.split("-");
	// res[0] = Long.parseLong(t[0]);
	// if (t.length >= 2) {
	// res[1] = Long.parseLong(t[1]);
	// } else {
	// res[1] = Long.parseLong(t[0]);
	// }
	// } catch (Exception e) {
	// }
	// return res;
	// }

	/**
	 * 跳转到...
	 */
	private void redirectTo() {
		Intent intent = new Intent(this, FragmentMainActivity.class);
		startActivity(intent);
		finish();
	}

}