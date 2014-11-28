package com.johnliu.swipefinish.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;

import com.vanda.vandalibnetwork.R;

/**
 * SwipeFinishLayout
 * <p>
 * Use this Layout,you can make a Activity with swiping to finish.
 * <p>
 * Then thanks for xiaanming's blog.
 * 
 * Created by John on 2014-6-18
 */
@SuppressLint("NewApi")
public class SwipeFinishLayout extends FrameLayout implements OnTouchListener {

	private final String TAG = "SwipeFinish";

	private ViewGroup root;

	private final int DURATION = 400;

	/**
	 * If the speed of moving Activity is more than MIN_SPEED,then finish the
	 * Activity.
	 */
	private final int MIN_SPEED = 500;

	/**
	 * The speed of moving at the horizontal direction.
	 */
	private float speedX = 0;

	private float downX = 0;

	private float lastX = 0;

	private float lastY;

	private Scroller mScroller;

	private Activity mActivity;

	/**
	 * A VelocityTracker.
	 */
	private VelocityTracker mVelocityTracker = null;

	private ViewPager mViewPager;

	private ImageView ivShadow;

	/**
	 * Distance in pixels a touch can wander before we think the user is
	 * scrolling. {@link ViewConfiguration#getScaledTouchSlop()}
	 */
	private int mScaledTouchSlop;

	/**
	 * Whether finish the Activity after scrolling.
	 */
	private boolean isFinish = false;

	public SwipeFinishLayout(Context context) {
		super(context);
		init();
	}

	public SwipeFinishLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SwipeFinishLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		this.setOnTouchListener(this);
		mScroller = new Scroller(getContext());
		mScaledTouchSlop = ViewConfiguration.get(getContext())
				.getScaledTouchSlop();
	}

	public void init(Activity act) {
		// get the root view of Activity
		mActivity = act;
		root = (ViewGroup) act.getWindow().getDecorView();
		addShadow();
	}

	/**
	 * Add a shadow for layout.
	 * 
	 * @param height
	 */
	private void addShadow() {
		Display dis = mActivity.getWindowManager().getDefaultDisplay();
		Point outSize = new Point(0, 0);
		dis.getSize(outSize);
		ivShadow = new ImageView(getContext());
		ivShadow.setBackgroundResource(R.drawable.vanda_shadow_left);
		FrameLayout.LayoutParams params = new LayoutParams(40, outSize.y);
		params.leftMargin = -40;
		root.addView(ivShadow, params);
		ivShadow = new ImageView(getContext());
		ivShadow.setBackgroundResource(R.drawable.vanda_shadow_right);
		FrameLayout.LayoutParams params1 = new LayoutParams(40, outSize.y);
		params1.rightMargin = -40;
		params1.gravity = Gravity.CENTER | Gravity.RIGHT;
		root.addView(ivShadow, params1);
	}

	/**
	 * Intercept a ViewPager.
	 * <p>
	 * To avoid conflict.
	 * 
	 * @param viewPager
	 */
	public void interceptViewPager(ViewPager viewPager) {
		this.mViewPager = viewPager;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (mViewPager != null && mViewPager.getCurrentItem() != 0) {
				// If the ViewPager isn't selected the first item.
				// Return false.
				// Then all events will passed to the ViewPager continue.
				return false;
			}
			downX = event.getRawX();
			lastX = downX;
			lastY = event.getRawY();
			createVelocityTracker(event);
			break;
		case MotionEvent.ACTION_MOVE:
			float deltaX = event.getRawX() - lastX;
			float deltaY = event.getRawY() - lastY;
			deltaY = Math.abs(deltaY);
			if (Math.abs(deltaX) > mScaledTouchSlop // 设置事件拦截条件
					&& Math.abs(deltaY) < mScaledTouchSlop) {// deltaX
				// >
				// mScaledTouchSlop
				// &&
				// Return true.
				// Then all events will passed to the method "onTouch".
				return true;
			}
			break;

		default:
			break;
		}

		return super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i(TAG, "onTouch:ACTION_DOWN");
			downX = event.getRawX();
			lastX = downX;
			createVelocityTracker(event);
			break;
		case MotionEvent.ACTION_UP:
			Log.i(TAG, "onTouch:ACTION_UP");
			if (Math.abs(root.getScrollX()) > Math.abs(getWidth() / 2))
				if (root.getScrollX() < 0) {
					System.out.println("向右滑动！");
					scrollRightToFinish();
				} else {
					System.out.println("向左滑动！");
					scrollLeftToFinish();
				}
			// scrollToFinish();
			else {
				// if (speedX > MIN_SPEED) {
				// // scrollToFinish();
				// }
				//
				// else
				// scrollToOrigin();
				if (speedX <= 0) {
					if (Math.abs(speedX) > MIN_SPEED) {
						scrollLeftToFinish();
					} else {
						scrollToOrigin();
					}
				} else {
					if (Math.abs(speedX) > MIN_SPEED) {
						scrollRightToFinish();
					} else {
						scrollToOrigin();
					}
				}
			}
			// mVelocityTracker.recycle();
			break;
		case MotionEvent.ACTION_MOVE:
			float newX = event.getRawX();
			// if (newX < downX) {
			// System.out.println("滑动了！！！");
			// break;
			// }

			float deltaX = lastX - newX;
			lastX = newX;
			root.scrollBy((int) deltaX, 0);
			mVelocityTracker.computeCurrentVelocity(1000);
			mVelocityTracker.addMovement(event);
			// if (mVelocityTracker.getXVelocity() > 0)
			speedX = mVelocityTracker.getXVelocity();
			System.out.println("speedX->" + speedX);
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * Create a VelocityTracker.
	 * 
	 * @param event
	 */
	@SuppressLint("Recycle")
	public void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		} else {
			mVelocityTracker.clear();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * scroll to finish
	 */
	@SuppressWarnings("unused")
	private void scrollToFinish() {
		isFinish = true;
		System.out.println("root.getScrollX()->" + root.getScrollX());
		System.out.println("getWidth()->" + getWidth());
		final int delta = (getWidth() + root.getScrollX());
		mScroller.startScroll(root.getScrollX(), 0, -delta + 1, 0, DURATION);
		postInvalidate();
	}

	private void scrollRightToFinish() {
		isFinish = true;
		final int delta = (getWidth() + root.getScrollX());
		mScroller.startScroll(root.getScrollX(), 0, -delta + 1, 0, DURATION);
		postInvalidate();
	}

	private void scrollLeftToFinish() {
		isFinish = true;
		final int delta = (getWidth() - root.getScrollX());
		mScroller.startScroll(root.getScrollX(), 0, delta - 1, 0, DURATION);
		postInvalidate();
	}

	/**
	 * scroll to origin
	 */
	private void scrollToOrigin() {
		isFinish = false;
		final int delta = root.getScrollX();
		mScroller.startScroll(root.getScrollX(), 0, -delta, 0, DURATION);
		postInvalidate();
	}

	@Override
	public void computeScroll() {
		/*
		 * When call Scroller.startScroll, the method "computeScrollOffset()"
		 * will return true.
		 */
		if (mScroller.computeScrollOffset()) {
			root.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			// System.out.println("mScroller.getFinalX():" +
			// mScroller.getFinalX());
			// System.out.println("mScroller.getCurrX():" +
			// mScroller.getCurrX());
			// System.out.println("mScroller.isFinished():" +
			// mScroller.isFinished());
			postInvalidate();
			if (mScroller.isFinished() && isFinish) {
				mActivity.finish();
				System.out.println("Activity 销毁了！");
				mVelocityTracker.recycle();
			}

		}
	}

}
