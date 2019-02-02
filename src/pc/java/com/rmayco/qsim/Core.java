package com.rmayco.qsim;
import java.util.ArrayList;
import java.util.List;

public class Core implements Runnable {

	private static final int SIZE_OF_MACHINE = 7;
	private static final int SCREEN_EDITOR = 0, SCREEN_RESULTS = 1, SCREEN_LOADING = 2;
	private static int screen; 
	private static ButtonGrid cellGrid;
	private static ButtonColumn[] cellColumn;
	private static Button saveButton, loadButton, runButton, backButton, saveButton2;
	private static ButtonColumn gateButtonColumn;
	private static Button[] gateButtons = new Button[12];
	private static QuantumComputer qc;
	private static ArrayList<Block> codeBlocks, binaryBlocks;
	private static BarGraph resultsGraph;
	private static String cursorState = "", cursorStateName = "";
	private static int previousCursorState = -1;
	private static Thread qcThread;
	private static int loadingCount = -1;
	private static int loadingCounter = 0;

	public Core() {}
	public static void main() {
		//This section has a lot of "magic numbers".
		//  Check the resources folder for the file "guide.png".
		//	This program directly implements the design, pixel-by-pixel.
		//	So the (x1, y1) of each item comes from that image.
		//	The (x2, y2) comes from the width and height of each item.

		//Cells are the squares that the user can place code blocks in.
		//cellColumn are each column grouped together.
		//cellGrid groups all the columns together. This single object
		//  contains every cell.
		cellColumn = new ButtonColumn[7];
		cellColumn[0] = new ButtonColumn("cell", 29, 127, 29 + 102, 720, 97, 256);
		cellColumn[1] = new ButtonColumn("cell", 150, 127, 150 + 102, 720, 97, 256);
		cellColumn[2] = new ButtonColumn("cell", 272, 127, 272 + 102, 720, 97, 256);
		cellColumn[3] = new ButtonColumn("cell", 394, 127, 394 + 102, 720, 97, 256);
		cellColumn[4] = new ButtonColumn("cell", 517, 127, 517 + 102, 720, 97, 256);
		cellColumn[5] = new ButtonColumn("cell", 640, 127, 640 + 102, 720, 97, 256);
		cellColumn[6] = new ButtonColumn("cell", 763, 127, 763 + 102, 720, 97, 256);
		cellGrid = new ButtonGrid(cellColumn, 29, 127, 862, 720);

		//Prevent the user from scrolling off the screen.
		cellGrid.setMaxScroll(97 * (256 - 6));

		//These are the buttons on the top-right corner.
		saveButton = new Button("save", 890, 0, 890 + 131, 0 + 111);
		loadButton = new Button("load", 1020, 0, 1020 + 130, 0 + 111);
		runButton = new Button("run", 1149, 0, 1149 + 131, 0 + 111);
		saveButton2 = new Button("save2", 890, 0, 890 + 195, 0 + 111);
		backButton = new Button("back", 890 + 195, 0, 1280, 0 + 111);

		//These are the right-hand scrollable column of buttons of code blocks.
		gateButtons[ 0] = new Button("x",        890, 147 + (76 * 0), 890 + 391, 147 + 76 + (76 * 0));
		gateButtons[ 1] = new Button("y",        890, 147 + (76 * 1), 890 + 391, 147 + 76 + (76 * 1));
		gateButtons[ 2] = new Button("z",        890, 147 + (76 * 2), 890 + 391, 147 + 76 + (76 * 2));
		gateButtons[ 3] = new Button("t",        890, 147 + (76 * 3), 890 + 391, 147 + 76 + (76 * 3));
		gateButtons[ 4] = new Button("tdg",       890, 147 + (76 * 4), 890 + 391, 147 + 76 + (76 * 4));
		gateButtons[ 5] = new Button("s",        890, 147 + (76 * 5), 890 + 391, 147 + 76 + (76 * 5));
		gateButtons[ 6] = new Button("sdg",       890, 147 + (76 * 6), 890 + 391, 147 + 76 + (76 * 6));
		gateButtons[ 7] = new Button("hadamard", 890, 147 + (76 * 7), 890 + 391, 147 + 76 + (76 * 7));
		gateButtons[ 8] = new Button("swap",     890, 147 + (76 * 8), 890 + 391, 147 + 76 + (76 * 8));
		gateButtons[ 9] = new Button("cnot",     890, 147 + (76 * 9), 890 + 391, 147 + 76 + (76 * 9));
		gateButtons[10] = new Button("fredkin",  890, 147 + (76 *10), 890 + 391, 147 + 76 + (76 *10));
		gateButtons[11] = new Button("toffoli",  890, 147 + (76 *11), 890 + 391, 147 + 76 + (76 *11));

		//Initialize our bar graph that displays our results.
		resultsGraph = new BarGraph(890, 147, 1280, 720);
		resultsGraph.setColor(0, 0, 255);
		resultsGraph.setTextColor(255, 255, 255);
		resultsGraph.setHeight(97);
		resultsGraph.setSpacing(97);
		resultsGraph.setOffset(97 - (147 - 127));
		

		//We group the gate buttons into a column to make scrolling easy.
		gateButtonColumn = new ButtonColumn(890, 147, 1280, 720);
		for (int i = 0; i < gateButtons.length; i++) {
			gateButtonColumn.add(gateButtons[i]);
		}

		//Prevent the user from scrolling off the screen.
		gateButtonColumn.setMaxScroll(76 * gateButtons.length - (720 - 147));

		//Initialize our code blocks array list.
		codeBlocks = new ArrayList<Block>();

		//We start in the editor screen.
		screen = SCREEN_EDITOR;

		//Base canvas is 720p.
		Display.create(1280, 720);
	}

	//Converts the code blocks into a program to run in the quantum computer simulator.
	//This runs in a thread so we can display the loading screen at the same time that
	//  this is running.
	@Override
	public void run() {
		//Make it run at least half a second, this is so that the loading screen does
		//  not flash to the screen a single frame (which may confuse the user).
		try {Thread.sleep(500);}catch(Exception e){}

		//If there's no blocks placed we don't need to run any of the other code here.
		if (codeBlocks.size() == 0) {
			Core.qc = new QuantumComputer(7);
			Core.generateBinaryBlocks();
			return;
		}

		//Let's generate the lines of code for our quantum computer simulator.
		String[] codeLines = new String[cellColumn[0].getSize()];

		//Default each line of code to an empty string.
		for (int i = 0; i < codeLines.length; i++) {
			codeLines[i] = "";
		}

		//Convert code blocks to code.
		for (int i = 0; i < codeBlocks.size(); i++) {
			int row = Core.codeBlocks.get(i).getRow();
			
			String code = Core.codeBlocks.get(i).getGateCode(SIZE_OF_MACHINE);
			codeLines[row] = codeLines[row].replace(";", "") + " + " + code + ";";
			if (codeLines[row].substring(0, 3).equals(" + ")) {
				codeLines[row] = codeLines[row].substring(3, codeLines[row].length());
			}

		}
		//Combine all the codeLines into a single string.
		String code = "";
		for (int i = 0; i < codeLines.length; i++) {
			if (!codeLines[i].equals("")) {
				code += codeLines[i];
				if (i != codeLines.length - 1) {
					code += "\n";
				}
			}
		}

		//Don't do anything if no code is generated.
		if (code.length() == 0) return;

		//Remove any extra newlines at the end of the code.
		while (code.charAt(code.length() - 1) == '\n') {
			code = code.substring(0, code.length() - 1);
		}

		//Remove any trailing plus signs.
		while (!code.replace(" + ;", "").equals(code)) {
			code = code.replace(" + ;", ";");
		}
		while (!code.replace("+ +", "").equals(code)) {
			code = code.replace("+ +", "+");
		}

		//Create a new quantum computer.
		Core.qc = new QuantumComputer(7);
		
		//Run our code.
		Core.qc.run(code);

		//Convert our output code into binary blocks.
		Core.generateBinaryBlocks();
	}

	//This generates 0 and 1 blocks to be placed on the qubit tracks.
	//  The blocks represent the bit patterns that occur in the
	//  resulting quantum computation.
	public static void generateBinaryBlocks() {
		binaryBlocks = new ArrayList<Block>();
		String[] qcData = qc.toString().split("\n");
		double[] probabilityList = new double[qcData.length];

		for (int row = 0; row < qcData.length; row++) {
			String binaryData = qcData[row].split(":")[0];
			double probability = Double.parseDouble(qcData[row].split(":")[1]);
			probabilityList[row] = probability;
			for (int col = 0; col < binaryData.length(); col++) {
				if (binaryData.charAt(col) == '1') {
					binaryBlocks.add(new Block(cellGrid, "one", row * 2 + 1, col, 102, 97));
				} else if (binaryData.charAt(col) == '0') {
					binaryBlocks.add(new Block(cellGrid, "zero", row * 2 + 1, col, 102, 97));
				}
			}
		}

		resultsGraph.setData(probabilityList);
	}

	//The cursor state is used to track what blocks the user is currently
	//  placing. setCursorState should be called immidiately after the
	//  the user selects a logic gate to place down.
	public static void setCursorState(int index) {

		//If the user is in the middle of placing multiple-input
		//  logic gates when they switch to a different logic
		//	gate, then the previous ones they placed need to be
		//  deleted.

		if (cursorState.equals("swap")) {
			codeBlocks.remove(codeBlocks.get(codeBlocks.size() - 1));
		}

		if (cursorState.equals("cnot_x")) {
			codeBlocks.remove(codeBlocks.get(codeBlocks.size() - 1));
		}

		if (cursorState.equals("fredkin_sw,fredkin_sw")) {
			codeBlocks.remove(codeBlocks.get(codeBlocks.size() - 1));
		}

		if (cursorState.equals("fredkin_sw")) {
			codeBlocks.remove(codeBlocks.get(codeBlocks.size() - 1));
			codeBlocks.remove(codeBlocks.get(codeBlocks.size() - 1));
		}

		if (cursorState.equals("toffoli_c,toffoli_c")) {
			codeBlocks.remove(codeBlocks.get(codeBlocks.size() - 1));
		}

		if (cursorState.equals("toffoli_c")) {
			codeBlocks.remove(codeBlocks.get(codeBlocks.size() - 1));
			codeBlocks.remove(codeBlocks.get(codeBlocks.size() - 1));
		}

		//Set the cursor state based on the index.
		switch (index) {
			case 0:
				cursorState = "x";
				break;
			case 1:
				cursorState = "y";
				break;
			case 2:
				cursorState = "z";
				break;
			case 3:
				cursorState = "t";
				break;
			case 4:
				cursorState = "tdg";
				break;
			case 5:
				cursorState = "s";
				break;
			case 6:
				cursorState = "sdg";
				break;
			case 7:
				cursorState = "h";
				break;
			case 8:
				cursorState = "swap,swap";
				break;
			case 9:
				cursorState = "cnot_c,cnot_x";
				break;
			case 10:
				cursorState = "fredkin_c,fredkin_sw,fredkin_sw";
				break;
			case 11:
				cursorState = "toffoli_x,toffoli_c,toffoli_c";
				break;
		}

		//cursorState may change over time, so cursorStateName keeps
		//  the original string, and previousCursorState keeps the
		//  original index used to generate that string.
		cursorStateName = cursorState;
		previousCursorState = index;
	}

	//Removes a logic block from the grid.
	private static void removeBlock(int row, int col) {
		for (int i = 0; i < codeBlocks.size(); i++) {
			Block blk = codeBlocks.get(i);
			if (blk.getRow() == row && blk.getCol() == col) {
				Block parent = blk.getParent();
				if (parent != null) {
					ArrayList<Block> children = parent.getChildren();
					for (int j = 0; j < children.size(); j++) {
						codeBlocks.remove(children.get(j));
					}
					codeBlocks.remove(parent);
				} else {
					ArrayList<Block> children = blk.getChildren();
					for (int j = 0; j < children.size(); j++) {
						codeBlocks.remove(children.get(j));
					}
					codeBlocks.remove(blk);
				}
			}
		}
	}

	//Adds a logic block to the grid.
	private static void addBlock(int row, int col) {
		if (cursorState.length() == 0) return;

		String[] cursorStateSplit = cursorState.split(",");
		String blockName;
		if (cursorStateSplit.length == 1) {
			blockName = cursorState;
			cursorState = "";
		} else {
			blockName = cursorStateSplit[0];
			cursorState = "";
			for (int i = 1; i < cursorStateSplit.length; i++) {
				cursorState += cursorStateSplit[i];
				if (i != cursorStateSplit.length - 1) {
					cursorState += ",";
				}
			}
		}
		
		//If you place a second block for swap or cnot and they are not in adjacent rows, cancel it!
		if ( (blockName.equals("swap") || blockName.equals("cnot_x")) && cursorState.length() == 0) {
			int linkIndex = codeBlocks.size() - 1;
			if (codeBlocks.get(linkIndex).getRow() != row) {
				codeBlocks.remove(codeBlocks.get(linkIndex));
				setCursorState(previousCursorState);
				return;
			}
		}

		//Fredkin's second block needs to be in the same row as the first..
		if (blockName.equals("fredkin_sw") && cursorState.equals("fredkin_sw")) {
			int linkIndex = codeBlocks.size() - 1;
			if (codeBlocks.get(linkIndex).getRow() != row) {
				codeBlocks.remove(codeBlocks.get(linkIndex));
				setCursorState(previousCursorState);
				return;
			}
		}

		//Fredkin's third block needs to be in the same row as the other two.
		if (blockName.equals("fredkin_sw") && cursorState.length() == 0) {
			int linkIndex = codeBlocks.size() - 2;
			Block p = codeBlocks.get(linkIndex);
			Block s = codeBlocks.get(linkIndex + 1);
			if (p.getRow() != row) {
				codeBlocks.remove(p);
				codeBlocks.remove(s);
				setCursorState(previousCursorState);
				return;
			}
		}


		//Toffoli's second block needs to be in the same row as the first..
		if (blockName.equals("toffoli_c") && cursorState.equals("toffoli_c")) {
			int linkIndex = codeBlocks.size() - 1;
			if (codeBlocks.get(linkIndex).getRow() != row) {
				codeBlocks.remove(codeBlocks.get(linkIndex));
				setCursorState(previousCursorState);
				return;
			}
		}

		//Toffoli's third block needs to be in the same row as the other two.
		if (blockName.equals("toffoli_c") && cursorState.length() == 0) {
			int linkIndex = codeBlocks.size() - 2;
			Block p = codeBlocks.get(linkIndex);
			Block s = codeBlocks.get(linkIndex + 1);
			if (p.getRow() != row) {
				codeBlocks.remove(p);
				codeBlocks.remove(s);
				setCursorState(previousCursorState);
				return;
			}
		}

		Block newBlock = new Block(cellGrid, blockName, row, col, 102, 97);
		removeBlock(row, col);
		codeBlocks.add(newBlock);

		//Swap and CNOT's second block need to link to the previous placed block (their first).
		if ( (blockName.equals("swap") || blockName.equals("cnot_x")) && cursorState.length() == 0) {
			int linkIndex = codeBlocks.size() - 2;

			newBlock.setParent(codeBlocks.get(linkIndex));
			codeBlocks.get(linkIndex).link(newBlock);
		}

		//Fredkin's second block need to link to the previous placed block (their first).
		if (blockName.equals("fredkin_sw") && cursorState.equals("fredkin_sw")) {
			int linkIndex = codeBlocks.size() - 2;
			newBlock.setParent(codeBlocks.get(linkIndex));
			codeBlocks.get(linkIndex).link(newBlock);
		}

		//Fredkin's third block need to link to the previous previous placed block (their first).
		if (blockName.equals("fredkin_sw") && cursorState.length() == 0) {
			int linkIndex = codeBlocks.size() - 3;
			newBlock.setParent(codeBlocks.get(linkIndex));
			codeBlocks.get(linkIndex).link(newBlock);
		}

		//Toffoli's second block need to link to the previous placed block (their first).
		if (blockName.equals("toffoli_c") && cursorState.equals("toffoli_c")) {
			int linkIndex = codeBlocks.size() - 2;
			newBlock.setParent(codeBlocks.get(linkIndex));
			codeBlocks.get(linkIndex).link(newBlock);
		}

		//Toffoli's third block need to link to the previous previous placed block (their first).
		if (blockName.equals("toffoli_c") && cursorState.length() == 0) {
			int linkIndex = codeBlocks.size() - 3;
			newBlock.setParent(codeBlocks.get(linkIndex));
			codeBlocks.get(linkIndex).link(newBlock);
		}

		if (cursorState.length() == 0) {
			setCursorState(previousCursorState);
		}

		
	}

	public static void draw() {

		if (screen == SCREEN_EDITOR) {
			Display.setColor(102, 102, 102);
			Display.setStyle(Display.FILL);
			Display.drawRectangle(0, 0, Display.getWidth(), Display.getHeight());
	
			cellGrid.draw();
			for (int i = 0; i < codeBlocks.size(); i++) codeBlocks.get(i).draw();
			gateButtonColumn.draw();
			for (int i = 0; i < gateButtonColumn.getSize(); i++) {
				if (gateButtonColumn.isClicked(i)) {
					setCursorState(i); 
				}
			}
			for (int i = 0; i < cellGrid.getRows(); i++) {
				for (int j = 0; j < cellGrid.getCols(); j++) {
					if (cellGrid.isClicked(i, j) && cursorState.length() > 0) {
						addBlock(i, j);
					}
					if (cellGrid.isLongPressed(i, j)) {
						//Only remove the block if...
						//  a. We don't have a multiple-input logic gate.
						//	b. Our multiple-input logic gate is on its first block.
						//     This is because "setCursorState" will handle the
						//       deletions in that case.
						if (previousCursorState < 8 || cursorStateName.equals(cursorState)) {
							removeBlock(i, j);
						} 
						setCursorState(previousCursorState);
					}
				}
			}
			Display.drawImage("qubit_row", 0, 0, 890, 147);
			Display.drawImage("black_separator", 890, 111, 890 + 391, 111 + 36); 
 
			saveButton.draw();
			loadButton.draw();
			runButton.draw();
			if (runButton.isClicked()) {
				qcThread = new Thread(new Core());
				qcThread.start();
				screen = SCREEN_LOADING;
				cellGrid.setScroll(0);
			}
			if (cursorState.length() > 0) {
				Block selected = new Block(cursorState.split(",")[0], 0, 720 - 97, 102, 720);
				selected.draw(); 
			}
		} else if (screen == SCREEN_RESULTS) {
			Display.setColor(102, 102, 102);
			Display.setStyle(Display.FILL);
			Display.drawRectangle(0, 0, Display.getWidth(), Display.getHeight());
	
			Core.cellGrid.draw();
			for (int i = 0; i < binaryBlocks.size(); i++) binaryBlocks.get(i).draw();
			resultsGraph.draw(cellGrid.getScroll());
			Display.drawImage("qubit_row", 0, 0, 890, 147);
			Display.drawImage("black_separator", 890, 111, 890 + 391, 111 + 36); 
			backButton.draw();
			saveButton2.draw();
			if (backButton.isClicked()) {
				screen = SCREEN_EDITOR;
				cellGrid.setScroll(0);
			}
		} else if (screen == SCREEN_LOADING) {
				Display.setColor(102, 102, 102);
				Display.setStyle(Display.FILL);
				Display.drawRectangle(0, 0, Display.getWidth(), Display.getHeight());
				
				int box_x1 = Display.getWidth() / 2 - 350;
				int box_y1 = Display.getHeight() / 2 - 75;
				int box_x2 = Display.getWidth() / 2 + 350;
				int box_y2 = Display.getHeight() / 2 + 75;

				Display.setColor(0, 0, 0);
				Display.setStyle(Display.FILL);
				Display.drawRectangle(box_x1 + 10, box_y1 + 10, box_x2 + 10, box_y2 + 10);

				Display.setColor(255, 255, 255);
				Display.setStyle(Display.FILL);
				Display.drawRectangle(box_x1, box_y1, box_x2, box_y2);

				Display.setColor(0, 0, 0);
				Display.setStyle(Display.STROKE);
				Display.drawRectangle(box_x1, box_y1, box_x2, box_y2);

				if (loadingCount == -1) {
					loadingCount = 0;
					loadingCounter = (int)System.currentTimeMillis();
				}

				if ((int)System.currentTimeMillis() - loadingCounter > 100) {
					loadingCount = (loadingCount + 1) % 4;
					loadingCounter = (int)System.currentTimeMillis();
				}

				String text = "Running program";
				for (int i = 0; i < loadingCount; i++) {
					text += ".";
				}
				
				int ts = 50;
				Display.setTextSize(ts);
				Display.drawText(text, box_x1 + 100, box_y1 + (box_y2 - box_y1) / 2 + ts / 2);
				
				if (!qcThread.isAlive()) {
					screen = SCREEN_RESULTS;
				}
		}
		Display.redraw();
		
	}
}
