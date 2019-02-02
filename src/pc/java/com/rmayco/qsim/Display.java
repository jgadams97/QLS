package com.rmayco.qsim;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;

//This is the Graphics library for PC.
public class Display {
	private static Graphics canvas;
	private static JFrame frame;
	private static JPanel panel;
	private static int width, height, customWidth, customHeight, style, previousTime, deltaTime;
	private static int fontSize;
	protected final static int STROKE = 1, FILL = 2;


	protected static void __init__(JFrame _f, JPanel _p) {
		frame = _f;
		panel = _p;
	}
	protected static void __draw__(Graphics _c) {
		width = (int)frame.getContentPane().getSize().getWidth();
		height = (int)frame.getContentPane().getSize().getHeight();
		if (width == 0 || height == 0) return;
		canvas = _c;
		canvas.setColor(Color.black);
		canvas.fillRect(0, 0, width, height);
		Core.draw();
	}

	public static void create(int w, int h) {
		customWidth = w;
		customHeight = h;
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
	protected static int shiftX() {
		if (customWidth != 0) {
			double scale = ((double)height / (double)customHeight);
			return (int)((width - (customWidth * scale)) / 2);
		}
		else return 0;
	}
	
	public static void setStyle(int s) {
		style = s;
	}

	public static void setColor(int r, int g, int b) {
		canvas.setColor(new Color(r, g, b));
	}

	public static void setColor(int r, int g, int b, int a) {
		canvas.setColor(new Color(r, g, b, a));
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

		if (style == FILL) canvas.fillRect(x1, y1, x2 - x1, y2 - y1);
		else canvas.drawRect(x1, y1, x2 - x1, y2 - y1);
	}

	public static void drawImage(String s, int x1, int y1, int x2, int y2) {
		x2 -= x1;
		y2 -= y1;
		x1 = adjust(x1) + shiftX();
		y1 = adjust(y1);
		x2 = adjust(x2);
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
	
		canvas.drawImage(DrawableView.SPRITES[id], x1, y1, x2, y2, null);
	} 

	public static int getWidth() {
		return customWidth;
	}
	
	public static int getHeight() {
		return customHeight;
	}
	
	public static void redraw() {
		int time = (int)System.currentTimeMillis();
		deltaTime = time - previousTime;
		previousTime = time;
		//try {Thread.sleep(1);} catch (InterruptedException e) {}
		panel.repaint();
	}
	public static int getDeltaTime() {
		return deltaTime < 0 ? 0 : deltaTime;
	}
	
	public static void setTextSize(int s) {
		fontSize = adjust(s);
		canvas.setFont(new Font("Times", 0, s));
	}
	
	public static void drawText(String t, int x, int y) {
		x = adjust(x) + shiftX();
		y = adjust(y);
		canvas.drawString(t, x, y);
	}
}
