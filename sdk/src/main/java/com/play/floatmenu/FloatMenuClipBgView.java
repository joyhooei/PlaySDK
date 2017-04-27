package com.play.floatmenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class FloatMenuClipBgView extends LinearLayout {

	@SuppressLint("NewApi")
	public FloatMenuClipBgView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@SuppressLint("NewApi")
	public FloatMenuClipBgView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public FloatMenuClipBgView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FloatMenuClipBgView(Context context) {
		super(context);
	}

	private Path clipPath;

	private Path makePath(int width, int height, int radius) {
		if (clipPath == null)
			clipPath = new Path();
		clipPath.reset();
		clipPath.addRoundRect(new RectF(0.0F, 0.0F, width, height), radius, radius, Path.Direction.CW);
		return clipPath;
	}

	public void setClipPath(Path clipPath) {
		this.clipPath = clipPath;
	}

	public Path getClipPath() {
		return clipPath;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// if (clipPath != null) {
		// canvas.clipPath(clipPath);
		// }
		canvas.clipPath(makePath(getWidth(), getHeight(), getHeight() / 2 + 1));
		super.dispatchDraw(canvas);
	}
}
