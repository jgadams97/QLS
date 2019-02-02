package com.rmayco.qsim;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

public class DrawableView extends JPanel implements MouseMotionListener, MouseListener {

	public static Image[] SPRITES = new Image[37];
	private static int x, y;
	private boolean down;

	@Override
	public void paint(Graphics g) {
		try {
			SPRITES[ 0] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/qubit_row.png"));
			SPRITES[ 1] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/cell.png"));
			SPRITES[ 2] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/save.png"));
			SPRITES[ 3] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/load.png"));
			SPRITES[ 4] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/run.png"));
			SPRITES[ 5] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/black_separator.png"));
			SPRITES[ 6] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/x.png"));
			SPRITES[ 7] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/y.png"));
			SPRITES[ 8] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/z.png"));
			SPRITES[ 9] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/t.png"));
			SPRITES[10] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/tdg.png"));
			SPRITES[11] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/s.png"));
			SPRITES[12] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/sdg.png"));

			SPRITES[13] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/hadamard.png"));
			SPRITES[14] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/swap.png"));
			SPRITES[15] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/cnot.png"));
			SPRITES[16] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/fredkin.png"));
			SPRITES[17] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/toffoli.png"));

			SPRITES[18] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/x_block.png"));
			SPRITES[19] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/y_block.png"));
			SPRITES[20] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/z_block.png"));
			SPRITES[21] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/t_block.png"));
			SPRITES[22] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/tdg_block.png"));
			SPRITES[23] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/s_block.png"));
			SPRITES[24] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/sdg_block.png"));
			SPRITES[25] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/hadamard_block.png"));
			SPRITES[26] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/swap_block.png"));
			SPRITES[27] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/cnot_block_c.png"));
			SPRITES[28] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/cnot_block_x.png"));
			SPRITES[29] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/fredkin_block_c.png"));
			SPRITES[30] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/fredkin_block_sw.png"));
			SPRITES[31] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/toffoli_block_c.png"));
			SPRITES[32] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/toffoli_block_x.png"));

			SPRITES[33] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/zero_block.png"));
			SPRITES[34] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/one_block.png"));

			SPRITES[35] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/back.png"));
			SPRITES[36] = ImageIO.read(this.getClass().getResourceAsStream("/res/drawable-hdpi/save2.png"));

		} catch (IOException e) {
			System.out.println("Resource files are missing.");
			System.exit(1);
		}

		Display.__draw__(g);
		Mouse.__init__(x, y, down);
	}

	public DrawableView() {
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		down = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		down = true;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}
}
