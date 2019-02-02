package com.rmayco.qsim;

public class Button {
	private int x1, y1, x2, y2;
	private String image;
	private boolean toggle, toggle2;
	private int timer;

	public Button(int _x1, int _y1, int _x2, int _y2) {
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;
	}

	public Button(String _image, int _x1, int _y1, int _x2, int _y2) {
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;
		image = _image;
	}

	public void draw() {
		Display.drawImage(image, x1, y1, x2, y2 + 1);
	}
	
	public void draw(int scroll) {
		Display.drawImage(image, x1, y1 - scroll, x2, y2 - scroll + 1);
	}

	public void setImage(String _image) {
		image = _image;
	}

	public void drawText(String text, int scroll) {
		int width = x2 - x1;
		int height = y2 - y1;
		Display.drawText(text, x1 + width/2, y1 - scroll + width/2);
	}

	public int getCenterX() {
		return x1 + (x2 - x1)/2;
	} 

	public int getCenterY() {
		return y1 + (y2 - y1)/2;
	}

	public boolean isClicked() {
		if (Mouse.getX() >= x1 && Mouse.getY() >= y1 &&
			Mouse.getX() <= x2 && Mouse.getY() <= y2 &&
			Mouse.isDown()) {
			Display.setColor(255, 0, 0, 100);
			Display.setStyle(Display.FILL);
			Display.drawRectangle(x1, y1, x2, y2);
		}
		if (Mouse.getX() >= x1 && Mouse.getY() >= y1 &&
			Mouse.getX() <= x2 && Mouse.getY() <= y2 &&
			Mouse.isDown() && !toggle) {
			toggle = true;
			return false;
		} else if (	Mouse.getX() >= x1 && Mouse.getY() >= y1 &&
					Mouse.getX() <= x2 && Mouse.getY() <= y2 &&
					!Mouse.isDown() && toggle) {
			toggle = false;
			return true;
		} else if (	!(Mouse.getX() >= x1 && Mouse.getY() >= y1 &&
					  Mouse.getX() <= x2 && Mouse.getY() <= y2) &&
					  Mouse.isDown()){
			toggle = false;
		}
		return false;
	}

	public boolean isClicked(int scroll) {
		if (Mouse.getX() >= x1 && Mouse.getY() >= ( y1 - scroll ) &&
			Mouse.getX() <= x2 && Mouse.getY() <= ( y2 - scroll ) &&
			Mouse.isDown()) {
			Display.setColor(255, 0, 0, 100);
			Display.setStyle(Display.FILL);
			Display.drawRectangle(x1, y1 - scroll, x2, y2 - scroll);
		}
		if (Mouse.getX() >= x1 && Mouse.getY() >= ( y1 - scroll ) &&
			Mouse.getX() <= x2 && Mouse.getY() <= ( y2 - scroll ) &&
			Mouse.isDown() && !toggle) {
			toggle = true;
			timer = (int)System.currentTimeMillis();
			return false;
		} else if (	Mouse.getX() >= x1 && Mouse.getY() >= ( y1 - scroll ) &&
					Mouse.getX() <= x2 && Mouse.getY() <= ( y2 - scroll ) &&
					!Mouse.isDown() && toggle) {
			toggle = false;
			int currentTime = (int)System.currentTimeMillis();
			if (currentTime - timer > 250) return false;
			return true;
		} else if (	!(Mouse.getX() >= x1 && Mouse.getY() >= ( y1 - scroll ) &&
					  Mouse.getX() <= x2 && Mouse.getY() <= ( y2 - scroll )) &&
					  Mouse.isDown()){
			toggle = false;
		}
		return false;
	}

	public boolean isLongPressed() {
		if (Mouse.getX() >= x1 && Mouse.getY() >= y1 &&
			Mouse.getX() <= x2 && Mouse.getY() <= y2 &&
			Mouse.isDown()) {
			Display.setColor(255, 0, 0, 100);
			Display.setStyle(Display.FILL);
			Display.drawRectangle(x1, y1, x2, y2);
		} else {
			timer = (int)System.currentTimeMillis();
		}

		int currentTime = (int)System.currentTimeMillis();
		if (currentTime - timer > 250) {
			return true;
		} else {
			return false;
		}

	}

	public boolean isLongPressed(int scroll) {
		if (Mouse.getX() >= x1 && Mouse.getY() >= ( y1 - scroll ) &&
			Mouse.getX() <= x2 && Mouse.getY() <= ( y2 - scroll ) &&
			Mouse.isDown()) {
			Display.setColor(255, 0, 0, 100);
			Display.setStyle(Display.FILL);
			Display.drawRectangle(x1, y1 - scroll, x2, y2 - scroll);
		} else {
			timer = (int)System.currentTimeMillis();
			toggle2 = true;
		}

		int currentTime = (int)System.currentTimeMillis();
		if (currentTime - timer > 250 && toggle2) {
			toggle2 = false;
			return true;
		} else {
			return false;
		}

	}

	public String getImage() {
		return image;
	}

}

