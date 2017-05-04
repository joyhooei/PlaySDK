package com.play.floatmenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.play.sdk.MobUser;
import com.play.sdk.MobUserManager;
import com.play.sdk.R;

public class FloatMenu extends FrameLayout {
	private Activity mActivity;
	private FloatMenuClipBgView mainView;

	private int isAccountAvaiable;
	private int isHomePageAvaiable;
	private int isServiceAvaiable;
	private int isFBAvaiable;

	private FloatMenuHorizontalScrollView menuScroll;
	private FloatMenuTextView fbView;
	private FloatMenuTextView serviceView;
	private FloatMenuTextView accountView;
	private FloatMenuTextView homePageView;


	public void setServiceNum(int num) {
		if (num > 0) {
			serviceView.setNum(num);
		} else {
			serviceView.setNum(0);
		}
	}

	public int getNoteNum() {
		return accountView.getNum() + homePageView.getNum() + fbView.getNum() + serviceView.getNum();
	}

	public boolean isRedPoint() {
		return accountView.isRedPointShowing() || homePageView.isRedPointShowing() || fbView.isRedPointShowing() || serviceView.isRedPointShowing();
	}

	@SuppressLint("NewApi")
	public FloatMenu(Activity activity) {
		super(activity);
		if (Build.VERSION.SDK_INT >= 11) {
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		this.mActivity = activity;
		init(activity);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.tobin_float_menu, this);
		mainView = (FloatMenuClipBgView) findViewById(R.id.menu_main_view);
		try {
			mainView.setBackgroundResource(R.drawable.tobin_float_menu_bg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		menuScroll = (FloatMenuHorizontalScrollView) findViewById(R.id.menu_scroll);
		accountView = (FloatMenuTextView) findViewById(R.id.tvAccount);
		accountView.setVisibility(View.VISIBLE);
		homePageView = (FloatMenuTextView) findViewById(R.id.tvHomePage);
		homePageView.setVisibility(View.VISIBLE);
		fbView = (FloatMenuTextView) findViewById(R.id.tvFB);
		fbView.setVisibility(View.VISIBLE);
		serviceView = (FloatMenuTextView) findViewById(R.id.tvService);
		serviceView.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	public int getMenuContentWidth() {
		if (menuScroll == null) {
			return getMeasuredWidth();
		}
		return menuScroll.getMeasuredWidth();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if ((w != oldw) || (h != oldh)) {
			makePath(w, h, h / 2);
		}
	}

	private Path makePath(int width, int height, int radius) {
		if (p == null)
			p = new Path();
		p.reset();
		p.addRoundRect(new RectF(0.0F, 0.0F, width, height), radius, radius, Path.Direction.CW);
		return p;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (mainView != null && mainView.getClipPath() != null) {
			mainView.setClipPath(makePath(menuScroll.getMeasuredWidth(), getHeight(), getHeight() / 2 + 1));
			Path p = mainView.getClipPath();
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.parseColor("#CC000000"));
			paint.setFlags(Paint.ANTI_ALIAS_FLAG);
			paint.setAntiAlias(true);
			canvas.drawPath(p, paint);
		}
		super.dispatchDraw(canvas);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	private Path p;

	public void setItemVisible(int visible) {
//		if (isAccountAvaiable == View.VISIBLE) {
//			findViewById(R.id.tvAccount).setVisibility(visible);
//		}
//		if (isHomePageAvaiable == View.VISIBLE) {
//			findViewById(R.id.tvHomePage).setVisibility(visible);
//		}
//		if (isServiceAvaiable == View.VISIBLE) {
//			findViewById(R.id.tvService).setVisibility(visible);
//		}
//		if (isFBAvaiable == View.VISIBLE) {
//			findViewById(R.id.tvFB).setVisibility(visible);
//		}
		findViewById(R.id.tvAccount).setVisibility(visible);
		findViewById(R.id.tvHomePage).setVisibility(visible);
		findViewById(R.id.tvService).setVisibility(visible);
		findViewById(R.id.tvFB).setVisibility(visible);
		updateMenuIcon(visible);
	}

	public void setDirection(boolean isLeft) {
		invalidate();
	}

	public void updateMenuIcon(int visible) {
		MobUserManager mobUserManager = MobUserManager.getInstance();
		MobUser user = mobUserManager.getCurrentUser();

		View view = findViewById(R.id.tvAccount);
		if (user != null) {
			if (visible != 99)
				view.setVisibility(visible);
		} else {
			view.setVisibility(View.GONE);
		}

		measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		invalidate();
	}

	public void showAnim(boolean isLeft) {
		menuScroll.fullScroll(View.FOCUS_LEFT);
		setDirection(isLeft);
		setItemVisible(View.INVISIBLE);
		setItemVisible(View.VISIBLE);
		float pivotX = isLeft ? 0.1f : 0.9f, pivotY = 0.5f;
		ScaleAnimation a1 = new ScaleAnimation(0f, 1.1f, 0f, 1f, Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY);
		a1.setDuration(150);
		final ScaleAnimation a2 = new ScaleAnimation(1.1f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY);
		a2.setDuration(100);
		a1.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				clearAnimation();
				startAnimation(a2);
			}
		});
		startAnimation(a1);
	}

	public void hideAnim(boolean isLeft) {
		setItemVisible(View.INVISIBLE);
		float pivotX = isLeft ? 0.1f : 0.9f, pivotY = 0.5f;
		ScaleAnimation a1 = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY);
		a1.setDuration(150);
		a1.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				clearAnimation();
			}
		});
		startAnimation(a1);
	}

}
