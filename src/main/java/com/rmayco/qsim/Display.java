package com.rmayco.qsim;
import android.graphics.*;
import android.view.*;

public class Display {
	private static Canvas canvas;
	private static Paint paint;
	private static int width, height, customWidth, customHeight, screenWidth, screenHeight, deltaTime, previousTime;
	private static View view;
	protected static int STROKE, FILL;
	
	protected static void __init__(View _v, Canvas _c, int _w, int _h, int _tw, int _th) {
		paint = new Paint();
		canvas = _c;
		width = _w;
		height = _h;
		screenWidth = _tw;
		screenHeight = _th;
		view = _v;
		STROKE = 1;
		FILL = 2;
		paint.setColor(Color.rgb(0, 0, 0));
		canvas.drawPaint(paint);
		Core.draw();
	}

	protected static int shiftX() {
		if (customWidth != 0) {
			double scale = ((double)height / (double)customHeight);
			return (int)((width - (customWidth * scale)) / 2);
		}
		else return 0;
	}
	protected static int adjust(int v) {
		//if (customWidth != 0) return (int)(v * ((double)width / (double)customWidth));
		if (customHeight != 0) return (int)(v * ((double)height / (double)customHeight));
		return v;
	}
	protected static int adjust2(int v) {
		//if (customWidth != 0) return (int)(v * ((double)customWidth / (double)width));
		if (customHeight != 0) return (int)(v * ((double)customHeight / (double)height));
		return v;
	}
	
	public static void create(int w, int h) {
		customWidth = w;
		customHeight = h;
	}
	
	public static void redraw() {
		int time = (int)System.currentTimeMillis();
		deltaTime = time - previousTime;
		previousTime = time;
		view.invalidate();
	}

	public static int getDeltaTime() {
		return deltaTime < 0 ? 0 : deltaTime;
	}
	
	public static void setColor(int r, int g, int b) {
		paint.setColor(Color.rgb(r, g, b));
	}

	public static void setColor(int r, int g, int b, int a) {
		paint.setColor(Color.argb(a, r, g, b));
	} 
	
	public static void setStyle(int style) {
		if (style == STROKE) paint.setStyle(Paint.Style.STROKE);
		else paint.setStyle(Paint.Style.FILL);
	}
	
	public static void drawRectangle(int x1, int y1, int x2, int y2) {
		if (x2 < x1) {
			int tmp = x2;
			x2 = x1;
			x1 = tmp;
		}
		if (y2 < y1) {
			int tmp = y2;
			y2 = y1;
			y1 = tmp;
		}

		x1 = adjust(x1) + shiftX();
		y1 = adjust(y1);
		x2 = adjust(x2) + shiftX();
		y2 = adjust(y2);
		canvas.drawRect(x1, y1, x2, y2, paint);
	}
	
	public static void drawCircle(int x, int y, int r) {
		x = adjust(x) + shiftX();
		y = adjust(y);
		r = adjust(r) + shiftX();
		canvas.drawCircle(x, y, r, paint);
	}
	
	public static void drawText(String t, int x, int y) {
		x = adjust(x) + shiftX();
		y = adjust(y);
		canvas.drawText(t, x, y, paint);
	}
	
	public static void setTextSize(int size) {
		size = adjust(size);
		paint.setTextSize(size);
	}
	
	public static void drawImage(String s, int x1, int y1, int x2, int y2) {
		x1 = adjust(x1) + shiftX();
		y1 = adjust(y1);
		x2 = adjust(x2) + shiftX();
		y2 = adjust(y2);
		
		int id = -1;
		if (s.equals("qubit_row")) id = 0;
		if (s.equals("cell")) id = 1;
		if (s.equals("save")) id = 2;
		if (s.equals("load")) id = 3;
		if (s.equals("run")) id = 4;
		if (s.equals("black_separator")) id = 5;
		if (s.equals("x")) id = 6;
		if (s.equals("y")) id = 7;
		if (s.equals("z")) id = 8;
		if (s.equals("t")) id = 9;
		if (s.equals("tdg")) id = 10;
		if (s.equals("s")) id = 11;
		if (s.equals("sdg")) id = 12;
		if (s.equals("hadamard")) id = 13;
		if (s.equals("swap")) id = 14;
		if (s.equals("cnot")) id = 15;
		if (s.equals("fredkin")) id = 16;
		if (s.equals("toffoli")) id = 17;
		if (s.equals("x_block")) id = 18;
		if (s.equals("y_block")) id = 19;
		if (s.equals("z_block")) id = 20;
		if (s.equals("t_block")) id = 21;
		if (s.equals("tdg_block")) id = 22;
		if (s.equals("s_block")) id = 23;
		if (s.equals("sdg_block")) id = 24;
		if (s.equals("hadamard_block")) id = 25;
		if (s.equals("swap_block")) id = 26;
		if (s.equals("cnot_block_c")) id = 27;
		if (s.equals("cnot_block_x")) id = 28;
		if (s.equals("fredkin_block_c")) id = 29;
		if (s.equals("fredkin_block_sw")) id = 30;
		if (s.equals("toffoli_block_c")) id = 31;
		if (s.equals("toffoli_block_x")) id = 32;
		if (s.equals("zero_block")) id = 33;
		if (s.equals("one_block")) id = 34;
		if (s.equals("back")) id = 35;
		if (s.equals("save2")) id = 36;
		if (id == -1) return;
		
		DrawableView.SPRITES[id].setBounds(x1, y1, x2, y2);
		DrawableView.SPRITES[id].draw(canvas);
	} 

	public static int getWidth() {
		return customWidth;
	}
	
	public static int getHeight() {
		return customHeight;
	}
	
	public static int getScreenWidth() {
		return screenWidth;
	}
	
	public static int getScreenHeight() {
		return screenHeight;
	}
}
