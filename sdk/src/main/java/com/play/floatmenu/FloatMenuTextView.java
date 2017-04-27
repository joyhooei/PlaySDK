package com.play.floatmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class FloatMenuTextView extends TextView {
	public FloatMenuTextView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public FloatMenuTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FloatMenuTextView(Context context) {
		super(context);
	}

	private Drawable drawableTop;

	@Override
	public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
		if (top != null) {
			drawableTop = top;
			return;
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (drawableTop != null) {
			int size = (int) (getWidth() * 0.50);
			drawableTop.setBounds(0, 0, size, size);
			super.setCompoundDrawables(null, drawableTop, null, null);
		}
	}

	public int dip2px(float dpValue) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	private Paint paint;
	private int num;

	private boolean isRedOnly;

	public void setNum(int num) {
		this.num = num;
		postInvalidate();
	}

	public int getNum() {
		if (getVisibility() == View.GONE)
			return 0;
		return num;
	}

	public boolean isRedPointShowing() {
		if (getVisibility() == View.GONE)
			return false;
		boolean b = isRedOnly || num > 0;
		return b;
	}

	public void setRedPointOnly() {
		this.isRedOnly = true;
		postInvalidate();
	}

	public void cancalAll() {
		this.isRedOnly = false;
		this.num = 0;
		postInvalidate();
	}

	private Rect textRect;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int radius = dip2px(7);
		if (isRedPointShowing()) {
			if (paint == null)
				paint = new Paint();
			paint.setColor(Color.RED);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(getWidth() - radius, radius, radius, paint);
		}
		if (num > 0) {
			if (textRect == null)
				textRect = new Rect(getWidth() - radius * 2, 0, getWidth(), radius * 2);
			String numberStr = String.valueOf(num);
			if (numberStr.length() > 1) {
				paint.setTextSize(dip2px(10));
			} else {
				paint.setTextSize(dip2px(8));
			}
			paint.setColor(Color.WHITE);
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setTypeface(Typeface.DEFAULT);
			FontMetricsInt fontMetrics = paint.getFontMetricsInt();
			int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
			canvas.drawText(numberStr, textRect.centerX(), baseline, paint);
		}
	}

}
