package com.rmayco.qsim;

public class Mouse {
	private static int x, y;
	private static boolean down;

	protected static void __init__(int _x, int _y, boolean _down) {
		x = Display.adjust2(_x - Display.shiftX());
		y = Display.adjust2(_y);
		down = _down;
	}	
	
	public static int getX() {
		if (x <= 0) return 0;
		if (x > Display.getWidth()) return Display.getWidth();
		return x;
	}
	public static int getY() {
		if (y <= 0) return 0;
		if (y > Display.getHeight()) return Display.getHeight();
		return y;
	}
	public static boolean isDown() {
		return down;
	}
	
	
}
