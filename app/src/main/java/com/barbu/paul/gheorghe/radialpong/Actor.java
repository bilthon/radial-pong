package com.barbu.paul.gheorghe.radialpong;

import android.graphics.Canvas;
import android.view.MotionEvent;

public abstract class Actor {
	public abstract void update();
	public abstract void draw(Canvas c);
	public abstract boolean handleTouchEvent(MotionEvent event);
}
