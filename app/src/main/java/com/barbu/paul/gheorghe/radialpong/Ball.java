package com.barbu.paul.gheorghe.radialpong;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

public class Ball extends Actor {
	private float radius;
	private Point pos = new Point(), displaySize;
	private Paint paint;
	private int vx, vy;

	private static final float FACTOR = 0.10f; //15% //TODO try on the phone dynamically
	private static final int SPEED = 64; //this is the squared speed TODO: set from outside, this is the number of pixels per 30th part of a second (see MAX_FPS)
	private static final String TAG = Ball.class.getSimpleName();
	
	public Ball(Point displaySize){
		this.displaySize = displaySize;
		
		paint = new Paint();
		paint.setColor(0xFF0000FF); //TODO: set it from outside
		paint.setStyle(Paint.Style.FILL);
				
		init();
		
		this.radius = Math.min(this.pos.x, this.pos.y) * FACTOR;
		Log.d(TAG, "Ball created!\n radius=" + this.radius + "\npos=" + this.pos);
		
	}
	
	public void init(){
		this.pos.x = displaySize.x/2;
		this.pos.y = displaySize.y/2;

		//set the direction
		Random r = new Random();
		
		int val = r.nextInt(SPEED+1);
		
		this.vx = (int) Math.sqrt(val) * Helpers.boolToSign(r.nextBoolean());
		this.vy = (int) Math.sqrt(SPEED-val) * Helpers.boolToSign(r.nextBoolean());
	}
	
	//TODO: set vx, vy device independent and FPS-aware in order to get a constant game speed
	//TODO: change vx and vy according to collisions
	
	public Point getPosition(){
		return this.pos;
	}
	
	public int getVelocityX(){
		return this.vx;
	}
	
	public int getVelocityY(){
		return this.vy;
	}

	public int getVelocity(){
		return (int) Math.sqrt(Math.pow(this.vx,2)+Math.pow(this.vy,2));
	}
	
	public void setVelocityX(int val){
		this.vx = val;
	}
	
	public void setVelocityY(int val){
		this.vy = val;
	}
	
	public float getRadius(){
		return this.radius;
	}

	public void bounce(){
		Log.d(TAG,"Bouncing!");
		double angle = Math.atan2(this.getVelocityY(), this.getVelocityX());
		double newAngle = angle + Math.PI*Math.random()*0.1;
		this.setVelocityX((int) (Math.cos(newAngle) * this.getVelocity() * -1.3));
		this.setVelocityY((int) (Math.sin(newAngle) * this.getVelocity() * -1.3));
	}

	@Override
	public void update() {
		this.pos.x += vx;
		this.pos.y += vy;
	}
	
	@Override
	public void draw(Canvas c) {
		c.drawCircle(this.pos.x, this.pos.y, this.radius, this.paint);
	}

	@Override
	public boolean handleTouchEvent(MotionEvent event) {
		return false;
	}

}
