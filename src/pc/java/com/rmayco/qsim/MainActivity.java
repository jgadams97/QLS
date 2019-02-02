package com.rmayco.qsim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainActivity extends JFrame {
	
	public MainActivity(String title) {
		super(title);
	}

	public static void main(String[] args) {
		JFrame frame = new MainActivity("QSim");

		frame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setUndecorated(true);

		JPanel panel = new DrawableView();
		frame.add(panel);

		frame.setVisible(true);
		Display.__init__(frame, panel);
		Core.main();
	}
}
