package com.rmayco.qsim;

//Handles scrolling items.
public class ScrollEvent {
	private double x1, y1, x2, y2, type, velocity;
	private double scroll, scrollStart, scrollStartTime, scrollStartX, scrollStartY, maxScroll;
	private boolean previousMouseDown;
	private boolean scrolling; 
	public static final int HORIZONTAL = 0, VERTICAL = 1;

	public ScrollEvent(int _x1, int _y1, int _x2, int _y2, int _type) {
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;
		type = _type;
		scrolling = false;
	}

	public void setMaxScroll(int ms) {
		maxScroll = ms;
	}
	
	//Gets how "scrolled" the item should be.
	public void update() {
		
		int mx = Mouse.getX();
		int my = Mouse.getY();
		boolean md = Mouse.isDown();

		if (type == VERTICAL) {

			//If you clicked...
			if (md && !scrolling && !previousMouseDown) {
				//...and if your mouse is over the item...
				if (mx >= x1 && mx <= x2 && my >= y1 && my <= y2) {
					//Begin scrolling.
					scrolling = true;
					scrollStart = scroll;
					scrollStartX = mx;
					scrollStartY = my;
					scrollStartTime = (int)System.currentTimeMillis();
				}
			}
			//If you stop clicking or the mouse is no longer over the item...
			if ((!md || mx < x1 || mx > x2 || my < y1 || my > y2) && scrolling) {
				//Stop scrolling.
				scrolling = false;
				int time = (int)System.currentTimeMillis();
				velocity = (scrollStartY - my) / (time - scrollStartTime) * 25;
			}
			

			if (scrolling) {
				scroll = scrollStartY - my + scrollStart;
			}

			if (velocity != 0) {
				double dtf = Display.getDeltaTime() / 15.0;
				scroll += velocity * dtf;
				double previousVelocity = velocity;
				if (velocity < 0) velocity += dtf;
				else if (velocity > 0) velocity -= dtf;
				if (previousVelocity > 0 && velocity < 0) velocity = 0;
				if (previousVelocity < 0 && velocity > 0) velocity = 0;
				
			}
		}
	
		if (scroll < 0) {
			scroll = 0;
			velocity = 0;
		}
		if (scroll > maxScroll && maxScroll != 0) {
			scroll = maxScroll;
			velocity = 0;
		}

		previousMouseDown = md;
	}

	//Gets how "scrolled" the item should be.
	public int getScroll() {
		return (int)scroll;
	}

	//Sets how "scrolled" the item should be.
	public void setScroll(int s) {
		scroll = (double)s;
	}
}

