package com.rmayco.qsim;
import java.util.ArrayList;

//Holds a column of buttons.
public class ButtonColumn {
	private ArrayList<Button> list = new ArrayList<>();
	private int x1, y1, x2, y2, scroll, count, height;
	private ScrollEvent se;

	public ButtonColumn(String image, int _x1, int _y1, int _x2, int _y2, int _height, int _count) {
		se = new ScrollEvent(_x1, _y1, _x2, _y2, ScrollEvent.VERTICAL);
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;
		height = _height;
		count = _count;
		scroll = 0;
		for (int i = 0; i < count; i++) {
			Button c = new Button(image, x1, y1 + i * height, x2, y1 + (i + 1) * height);
			list.add(c);
		}
	}

	public Button getButton(int elem) {
		return list.get(elem);
	}

	public int getSize() {
		return count;
	}

	public ButtonColumn(int _x1, int _y1, int _x2, int _y2) {
		se = new ScrollEvent(_x1, _y1, _x2, _y2, ScrollEvent.VERTICAL);
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;
		scroll = 0;
	}

	public void setMaxScroll(int ms) {
		se.setMaxScroll(ms);
	}

	public void add(Button b) {
		count += 1;
		list.add(b);
	}

	public void draw() {
		se.update();
		scroll = se.getScroll();
		for (int i = 0; i < count; i++) {
			list.get(i).draw(scroll);
		}
	}
	public void draw(int scroll) {
		for (int i = 0; i < count; i++) {
			list.get(i).draw(scroll);
		}
	}
	public void drawText(String text, int scroll) {
		for (int i = 0; i < count; i++) {
			list.get(i).drawText(text, scroll);
		}
	}

	public int getX() {
		return x1;
	}

	public int getY(int elem) {
		return y1 + elem * height;
	}

	public boolean isLongPressed(int elem) {
		if (Mouse.getX() < x1) return false;
		if (Mouse.getY() < y1) return false;
		if (Mouse.getX() > x2) return false;
		if (Mouse.getY() > y2) return false;
		return list.get(elem).isLongPressed(se.getScroll());
	}

	public boolean isLongPressed(int elem, int scroll) {
		if (Mouse.getX() < x1) return false;
		if (Mouse.getY() < y1) return false;
		if (Mouse.getX() > x2) return false;
		if (Mouse.getY() > y2) return false;
		return list.get(elem).isLongPressed(scroll);
	}

	public boolean isClicked(int elem) {
		if (Mouse.getX() < x1) return false;
		if (Mouse.getY() < y1) return false;
		if (Mouse.getX() > x2) return false;
		if (Mouse.getY() > y2) return false;
		return list.get(elem).isClicked(se.getScroll());
	}

	public boolean isClicked(int elem, int scroll) {
		if (Mouse.getX() < x1) return false;
		if (Mouse.getY() < y1) return false;
		if (Mouse.getX() > x2) return false;
		if (Mouse.getY() > y2) return false;
		return list.get(elem).isClicked(scroll);
	}
}
