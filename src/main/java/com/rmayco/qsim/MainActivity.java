package com.rmayco.qsim;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.graphics.*;
import android.util.*;

public class MainActivity extends Activity {
	
	private static int screenWidth, screenHeight;
	LinearLayout ctnMain;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int)event.getX() - DrawableView.LEFT;
		int y = (int)event.getY() - DrawableView.TOP;
		boolean down = event.getAction() == MotionEvent.ACTION_MOVE;
		Mouse.__init__(x, y, down);
		
 		return super.onTouchEvent(event);
	}
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		MainActivity.screenWidth = displayMetrics.widthPixels;
		MainActivity.screenHeight = displayMetrics.heightPixels;
        setContentView(new DrawableView(this));//R.layout.main);
	}
	
	protected static int getScreenWidth() {
		return screenWidth;
	}
	
	protected static int getScreenHeight() {
		return screenHeight;
	}
}
