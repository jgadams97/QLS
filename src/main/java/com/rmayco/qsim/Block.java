package com.rmayco.qsim;
import java.util.ArrayList;

public class Block {
	private ButtonGrid tiedGrid;
	private Button button;
	private ArrayList<Block> children;
	private Block parent;
	private int row, col, width, height, tx1, ty1, tx2, ty2;
	private String type;

	public String getGateCode(int sizeOfMachine) {
		int[] qbit = new int[children.size() + 1];
		qbit[0] = getIndex(sizeOfMachine);;
		for (int i = 0; i < children.size(); i++) {
			qbit[i + 1] = children.get(i).getIndex(sizeOfMachine);
		}

		if (type.equals("x")) return "():X(" + qbit[0] + ")";
		else if (type.equals("y")) return "():Y(" + qbit[0] + ")";
		else if (type.equals("z")) return "():Z(" + qbit[0] + ")";
		else if (type.equals("t")) return "():T(" + qbit[0] + ")";
		else if (type.equals("tdg")) return "():Tdg(" + qbit[0] + ")";
		else if (type.equals("s")) return "():S(" + qbit[0] + ")";
		else if (type.equals("sdg")) return "():Sdg(" + qbit[0] + ")";
		else if (type.equals("h")) return "():H(" + qbit[0] + ")";
		else if (type.equals("swap")) {
			if (children.size() == 1) {
				String ret = "";
				ret += "(!" + qbit[0] + "," + qbit[1] + "):X(" + qbit[0] + ")";
				ret += " + "; 
				ret += "(" + qbit[0] + ",!" + qbit[1] + "):X(" + qbit[0] + ")";
				ret += " + "; 
				ret += "(!" + qbit[0] + "," + qbit[1] + "):X(" + qbit[1] + ")";
				ret += " + "; 
				ret += "(" + qbit[0] + ",!" + qbit[1] + "):X(" + qbit[1] + ")";
				return ret;
			}
		}
		else if (type.equals("cnot_c")) {
			if (children.size() == 1) {
				return "(" + qbit[0] + "):X(" + qbit[1] + ")";
			}
		}
		else if (type.equals("toffoli_x")) {
			if (children.size() == 2) {
				return "(" + qbit[1] + "," + qbit[2] + "):X(" + qbit[0] + ")";
			}
		}
		else if (type.equals("fredkin_c")) {
			if (children.size() == 2) {
				String ret = "";
				ret += "(" + qbit[0] + ",!" + qbit[1] + "," + qbit[2] + "):X(" + qbit[1] + ")";
				ret += " + "; 
				ret += "(" + qbit[0] + "," + qbit[1] + ",!" + qbit[2] + "):X(" + qbit[1] + ")";
				ret += " + "; 
				ret += "(" + qbit[0] + ",!" + qbit[1] + "," + qbit[2] + "):X(" + qbit[2] + ")";
				ret += " + "; 
				ret += "(" + qbit[0] + "," + qbit[1] + ",!" + qbit[2] + "):X(" + qbit[2] + ")";
				return ret;
			}
		}

		return "";
	}

	//The tiedGrid is the grid the blocks are to be layed on.
	//  It is important to a tie a grid to the blocks in order for the blocks to scroll with the grid.
	//Type is the logic gate type.
	//Row and column is where the block lies on the grid.
	//Width and height is the width and height of the block image.
	public Block(ButtonGrid _tiedGrid, String _type, int _row, int _col, int _width, int _height) {
		row = _row;
		col = _col;
		type = _type;
		tiedGrid = _tiedGrid;
		width = _width;
		height = _height;
		setButton();
	}
	public Block(String _type, int x1, int y1, int x2, int y2) {
		tx1 = x1;
		ty1 = y1;
		tx2 = x2;
		ty2 = y2;	
		type = _type;
		tiedGrid = null;
		width = (x2 - x1);
		height = (y2 - y1);
		setButton();
	}

	public String getType() {
		return type;
	}

	private void setButton() {
		if (tiedGrid != null) {
			int x1 = tiedGrid.getColCoord(col);
			int y1 = tiedGrid.getRowCoord(row);
			children = new ArrayList<Block>();
			String blockName = "x";

			if (type.equals("x")) blockName = "x_block";
			else if (type.equals("y")) blockName = "y_block";
			else if (type.equals("z")) blockName = "z_block";
			else if (type.equals("t")) blockName = "t_block";
			else if (type.equals("tdg")) blockName = "tdg_block";
			else if (type.equals("s")) blockName = "s_block";
			else if (type.equals("sdg")) blockName = "sdg_block";
			else if (type.equals("swap")) blockName = "swap_block";
			else if (type.equals("h")) blockName = "hadamard_block";
			else if (type.equals("cnot_c")) blockName = "cnot_block_c";
			else if (type.equals("cnot_x")) blockName = "cnot_block_x";
			else if (type.equals("toffoli_c")) blockName = "toffoli_block_c";
			else if (type.equals("toffoli_x")) blockName = "toffoli_block_x";
			else if (type.equals("fredkin_sw")) blockName = "fredkin_block_sw";
			else if (type.equals("fredkin_c")) blockName = "fredkin_block_c";
			else if (type.equals("zero")) blockName = "zero_block";
			else if (type.equals("one")) blockName = "one_block";
			button = new Button(blockName, x1, y1, x1 + width, y1 + height);
		} else {
			children = new ArrayList<Block>();
			String blockName = "x";

			if (type.equals("x")) blockName = "x_block";
			else if (type.equals("y")) blockName = "y_block";
			else if (type.equals("z")) blockName = "z_block";
			else if (type.equals("t")) blockName = "t_block";
			else if (type.equals("tdg")) blockName = "tdg_block";
			else if (type.equals("s")) blockName = "s_block";
			else if (type.equals("sdg")) blockName = "sdg_block";
			else if (type.equals("swap")) blockName = "swap_block";
			else if (type.equals("h")) blockName = "hadamard_block";
			else if (type.equals("cnot_c")) blockName = "cnot_block_c";
			else if (type.equals("cnot_x")) blockName = "cnot_block_x";
			else if (type.equals("toffoli_c")) blockName = "toffoli_block_c";
			else if (type.equals("toffoli_x")) blockName = "toffoli_block_x";
			else if (type.equals("fredkin_sw")) blockName = "fredkin_block_sw";
			else if (type.equals("fredkin_c")) blockName = "fredkin_block_c";
			else if (type.equals("zero")) blockName = "zero_block";
			else if (type.equals("one")) blockName = "one_block";
			button = new Button(blockName, tx1, ty1, tx1 + width, ty1 + height);
		}
	}

	public int getCenterX() {
		return button.getCenterX();
	}

	public int getCenterY() {
		return button.getCenterX();
	}

	public void link(Block blk) {
		children.add(blk);
	}

	public Block getParent() {
		return parent;
	}

	public void setParent(Block p) {
		parent = p;
	}

	public ArrayList<Block> getChildren() {
		return children;
	}

	public Block getFirstChild() {
		if (children.size() == 0) return null;
		return children.get(0);
	}

	public void removeFirstChild() {
		children.remove(0);
	}

	public void removeParent() {
		parent = null;
	}

	public void draw() {
		if (tiedGrid != null) {
			for (int i = 0; i < children.size(); i++) {
				int x1 = button.getCenterX();
				int y1 = button.getCenterY() - 2 - tiedGrid.getScroll();
				int x2 = children.get(i).getCenterX();
				int y2 = button.getCenterY() + 2 - tiedGrid.getScroll();
				Display.setColor(0, 0, 0);
				Display.setStyle(Display.FILL);
				Display.drawRectangle(x1, y1, x2, y2);
			}
			button.draw(tiedGrid.getScroll());
		} else {
			button.draw();
		}
	}

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}

	public int getIndex(int sizeOfMachine) {
		return sizeOfMachine - col - 1;
	}

}
