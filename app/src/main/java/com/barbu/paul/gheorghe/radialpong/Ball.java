package com.barbu.paul.gheorghe.radialpong;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

public class Ball extends Actor {
	private double radius;
	private Point pos = new Point(), displaySize;
	private Paint paint;
	private double vx, vy;

	private static final float FACTOR = 0.10f; //15% //TODO try on the phone dynamically
	private static double MIN_INITIAL_SPEED = 8;
	private static double MAX_INITIAL_SPEED = 12; //this is the squared speed TODO: set from outside, this is the number of pixels per 30th part of a second (see MAX_FPS)
	private static double TOP_SPEED = 24;
	private static double SPEED_INCREMENT = 0.5;
	private static final String TAG = Ball.class.getSimpleName();
	private double mInitialSpeed;
	private double mCurrentSpeed;
	private int bounceCount = 0;

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
		
		mInitialSpeed = MIN_INITIAL_SPEED + r.nextDouble()*(MAX_INITIAL_SPEED - MIN_INITIAL_SPEED);
		double initialAngle = r.nextDouble() * 2 * Math.PI - Math.PI;
		this.vx = Math.cos(initialAngle) * mInitialSpeed;
		this.vy = Math.sin(initialAngle) * mInitialSpeed;
		mCurrentSpeed = mInitialSpeed;
		this.bounceCount = 0;
	}

	//TODO: set vx, vy device independent and FPS-aware in order to get a constant game speed
	//TODO: change vx and vy according to collisions
	
	public Point getPosition(){
		return this.pos;
	}
	
	public double getVelocityX(){
		return this.vx;
	}
	
	public double getVelocityY(){
		return this.vy;
	}

	public double getVelocity(){
		return this.mCurrentSpeed;
	}
	
	public void setVelocityX(double val){
		this.vx = val;
	}
	
	public void setVelocityY(double val){
		this.vy = val;
	}
	
	public double getRadius(){
		return this.radius;
	}

	public void bounce(){
		Log.d(TAG,"Bouncing!");
		double angle = Math.atan2(this.getVelocityY(), this.getVelocityX());
		double newAngle = angle + Math.PI*Math.random()*0.1;
		this.setVelocityX(Math.cos(newAngle) * this.getVelocity() * -1);
		this.setVelocityY(Math.sin(newAngle) * this.getVelocity() * -1);
		this.bounceCount++;
		increaseSpeed();
	}

	/**
	 * The speed is increased slowly at first and then it reaches a maximum speed increment, until it
	 * asymptotically reaches the TOP_SPEED value. In order to get the desired function we use a displaced
	 * sigmoid function.
	 *
	 * @see http://en.wikipedia.org/wiki/Sigmoid_function
	 */
	private void increaseSpeed(){
		double sigmoid = (1 / (1 + Math.pow(Math.E, -(this.bounceCount * this.SPEED_INCREMENT - 6 ))));
		this.mCurrentSpeed = this.mCurrentSpeed + sigmoid * (this.TOP_SPEED - this.mCurrentSpeed);
		Log.d(TAG,"speed increased to: "+mCurrentSpeed);
	}

	@Override
	public void update() {
		this.pos.x += vx;
		this.pos.y += vy;
	}
	
	@Override
	public void draw(Canvas c) {
		c.drawCircle((float)this.pos.x, (float)this.pos.y, (float)this.radius, this.paint);
	}

	@Override
	public boolean handleTouchEvent(MotionEvent event) {
		return false;
	}

}
