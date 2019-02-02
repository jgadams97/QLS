package com.rmayco.qsim;

//Holds a grid of buttons made up of columns.
public class ButtonGrid {
	private ButtonColumn[] cols;
	private ScrollEvent se;
	private int x1, y1, x2, y2;

	public ButtonGrid(ButtonColumn[] bc, int _x1, int _y1, int _x2, int _y2) {
		se = new ScrollEvent(_x1, _y1, _x2, _y2, ScrollEvent.VERTICAL);
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;
		cols = bc;
	}

	public void draw() {
		se.update();
		for (int i = 0; i < cols.length; i++) {
			cols[i].draw(se.getScroll());
		}
	}

	public void drawText(String text) {
		for (int i = 0; i < cols.length; i++) {
			cols[i].drawText(text, se.getScroll());
		}
	}

	public boolean isClicked(int row, int col) {
		return cols[col].isClicked(row, se.getScroll());
	}

	public boolean isLongPressed(int row, int col) {
		return cols[col].isLongPressed(row, se.getScroll());
	}

	public int getScroll() {
		return se.getScroll();
	}

	public int getRowCoord(int row) {
			return cols[0].getY(row);
	}

	public int getColCoord(int col) {
			return cols[col].getX();
	} 

	public int getRows() {
		if (cols.length == 0) return 0;
		return cols[0].getSize();
	}

	public int getCols() {
		return cols.length;
	}

	public void setMaxScroll(int ms) {
		se.setMaxScroll(ms);
	}

	public void setScroll(int s) {
		se.setScroll(s);
	}
}


