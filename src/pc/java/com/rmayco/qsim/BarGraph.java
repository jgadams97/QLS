package com.rmayco.qsim;
import java.text.DecimalFormat;

public class BarGraph {
	private int x1, y1, x2, y2;
	private int offset, spacing, height;
	private int textSize = 1;
	private int textColorR, textColorG, textColorB;
	private int red, green, blue;
	private double[] data;

	private String dataToString(int index) {
		DecimalFormat df = new DecimalFormat("###.##");
		return df.format(data[index] * 100) + "%";
	}

	public BarGraph(int _x1, int _y1, int _x2, int _y2) {
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;
	}

	public void setData(double[] d) {
		data = d;
	}

	//The offset to start drawing bars.
	public void setOffset(int o) {
		offset = o;
	}

	//The spacing between bars.
	public void setSpacing(int s) {
		spacing = s;
	}

	//The height of a bar.
	public void setHeight(int h) {
		height = h;
	}

	//Sets the color of the bars.
	public void setColor(int r, int g, int b) {
		red = r;
		green = g;
		blue = b;
	}

	//Sets the color of the text.
	public void setTextColor(int r, int g, int b) {
		textColorR = r;
		textColorG = g;
		textColorB = b;
	}
	

	public void draw(int scroll) {
		//Draw the box containing the data.
		Display.setColor(255, 255, 255);
		Display.setStyle(Display.FILL);
		Display.drawRectangle(x1, y1, x2, y2);
		

		//Draw the bars.
		for (int i = 0; i < data.length; i++) {

			int barx1 = x1;
			int bary1 = y1 + offset + i * spacing + i * height;
			int barx2 = x1 + (int)( data[i] * (x2 - x1) );
			int bary2 = bary1 + height;

			bary1 -= scroll;
			bary2 -= scroll;

			Display.setColor(red, green, blue);
			Display.setStyle(Display.FILL);
			Display.drawRectangle(barx1, bary1, barx2, bary2);

			Display.setColor(0, 0, 0);
			Display.setStyle(Display.STROKE);
			Display.drawRectangle(barx1, bary1, barx2, bary2);

			if (data[i] <= 0.5) {
				barx1 += (barx2 - barx1);
				Display.setColor(255-textColorR, 255-textColorG, 255-textColorB);
			} else {
				Display.setColor(textColorR, textColorG, textColorB);
			}

			Display.setTextSize( ( bary2 - bary1 ) / 2 );
			Display.drawText(dataToString(i), barx1 + 1, bary2 - 1);			
		}


		//Draw the border of the box.
		Display.setColor(0, 0, 0);
		Display.setStyle(Display.STROKE);
		Display.drawRectangle(x1, y1, x2, y2);
	}

}
