package com.rmayco.qsim;
import java.util.ArrayList;

public class QuantumComputer {
	private int count;
	private Matrix memory;
	private ArrayList<String> matrixNames;
	private ArrayList<Matrix> logicMatrices;
	private Matrix logicFilter;
	//private Function[] functions;

	public QuantumComputer(int c) {
		count = c;
		memory = new Matrix(1, 2);
		memory.setElement(0, 0, new Complex(1, 0));
		memory.setElement(0, 1, new Complex(0, 0));

		Matrix tmp = new Matrix(1, 2);
		tmp.setElement(0, 0, new Complex(1, 0));
		tmp.setElement(0, 1, new Complex(0, 0));
		
		for (int i = 1; i < count; i++) {
			memory = Matrix.knk(memory, tmp);
		}

		logicFilter = new Matrix((int)Math.pow(2, count),(int)Math.pow(2, count));
		setMatrices("ID = [[1,0],[0,1]]\nX = [[0,1],[1,0]]\nY = [[0,-i],[i,0]]\nZ = [[1,0],[0,-1]]\nH = [[0.707106781,0.707106781],[0.707106781,-0.707106781]]\nT = [[1,0],[0,0.707106781 + 0.707106781i]]\nTdg = [[1,0],[0,0.707106781 - 0.707106781i]]\nS = [[1,0],[0,i]]\nSdg = [[1,0],[0,-i]]");		
	}
/*

0 1
1 0
[[1/sqrt(2),1/sqrt(2)],[1/sqrt(2),-1/sqrt(2)]]


*/

	public void setMatrices(String data) {
		data = data.replace(" ", "").replace("\t", "");
		matrixNames = new ArrayList<String>();
		logicMatrices = new ArrayList<Matrix>();

		String[] matricesString = data.split("\\]\\]\n");
		int lastIndex = matricesString.length - 1;
		matricesString[lastIndex] = matricesString[lastIndex].substring(0, matricesString[lastIndex].length() - 2);
		//Loop through individual matrices...
		for (int i = 0; i < matricesString.length; i++) {
			Matrix m = new Matrix(2, 2);
			String name = matricesString[i].split("=")[0];
			matricesString[i] = matricesString[i].split("=")[1];
			String[] rowsString = matricesString[i].split("\\],\\[");
			//Loop through individual rows...
			for (int j = 0; j < rowsString.length; j++) {
				String[] colsString = rowsString[j].split(",");
				colsString[0] = colsString[0].replace("[[", "");
				//Loop through individual columns...
				for (int k = 0; k < colsString.length; k++) {
					m.setElement(j, k, Complex.parseComplex(colsString[k]));
				}
			}
			matrixNames.add(name);
			logicMatrices.add(m);
		}
	}

	/*public void setGates(String s) {
		s = s.replace(" ", "").replace("\n", "").replace("\t", "");
		String[] fs = s.split(">");
		functions = new Function[fs.length];
		for (int i = 0; i < fs.length; i++) {
			String fname = fs[i].split("<")[0];
			String fdef = fs[i].split("<")[1];
			functions[i] = new Function(fname, fdef);
		}
	}*/

	//Generate a binary representation of a number.
	private String bin(int dec) {
		String s = Integer.toString(dec, 2);
		while (s.length() < count) s = "0" + s;
		return s;
	}

	//Apply a named logic gate...
	private Matrix applyGate(Matrix m, String gateName) {
			//Search through known matrices...
			boolean foundMatrix = false;
			for (int k = 0; k < matrixNames.size(); k++) {
				//Check if command name is a known matrix...
				if (gateName.equals(matrixNames.get(k))) {
					//Apply matrix command.
					m = Matrix.mul(m, logicMatrices.get(k));
					foundMatrix = true;
				}
			}
			//Exit if the matrix command was invalid.
			if (!foundMatrix) {
				System.out.println("Error: Unknown command `" + gateName + "`.");
				System.exit(1);
			}
			return m;
	}


	//Generates the logic filter from a logic command.
	private void generateLogicFilter(String commandString) {
		String[] commands;
		if (commandString.replace("+", "").equals(commandString)) {
			commands = new String[1];
			commands[0] = commandString;
		} else {
			commands = commandString.split("\\+");
		}

		Matrix[][] truthTable = new Matrix[(int)Math.pow(2, count)][count];		
		for (int i = 0; i < (int)Math.pow(2, count); i++) {
			for (int j = 0; j < count; j++) {
				Matrix m = new Matrix(1, 2);
				//Is our current element 1 or 0?
				if ( ( i & (int)Math.pow(2, count-j-1) ) != 0) {
					//Generate |1>...
					m.setElement(0, 0, new Complex(0, 0));
					m.setElement(0, 1, new Complex(1, 0));
				} else {
					//Generate |0>...
					m.setElement(0, 0, new Complex(1, 0));
					m.setElement(0, 1, new Complex(0, 0));
				}

				for (int c = 0; c < commands.length; c++) {
					String command = commands[c];

					boolean shouldExecuteCommand = false;
					if (command.split(":")[0].equals("()")) {
						shouldExecuteCommand = true;
					} else {
						String[] truthIndices = command.split(":")[0].split("\\(")[1].split("\\)")[0].split(",");
						shouldExecuteCommand = true;
						for (int ti = 0; ti < truthIndices.length; ti++) {
							if (truthIndices[ti].charAt(0) == '!') {
								int truthIndex = Integer.parseInt(truthIndices[ti].substring(1, truthIndices[ti].length()));
								if ( (i & (int)Math.pow(2, truthIndex)) != 0) {
									shouldExecuteCommand = false;
									break;
								}
							} else {
								int truthIndex = Integer.parseInt(truthIndices[ti]);
								if ( (i & (int)Math.pow(2, truthIndex)) == 0) {
									shouldExecuteCommand = false;
									break;
								}
							}
						}
					}				

					String commandName = command.split(":")[1].split("\\(")[0];
					int index = Integer.parseInt(command.split(":")[1].split("\\(")[1].split("\\)")[0]);
	
					//Check to see if this is the appropriate qubit to apply the matrix command...
					if (index == count-j-1 && shouldExecuteCommand) {
						m = applyGate(m, commandName);
					}
				}

				truthTable[i][j] = m;
			}
		}

		//
		Matrix[] rowsTable = new Matrix[(int)Math.pow(2, count)];
		for (int i = 0; i < (int)Math.pow(2, count); i++) {
			Matrix row = truthTable[i][0];
			for (int j = 1; j < count; j++) {
				row = Matrix.knk(row, truthTable[i][j]);
			}
			rowsTable[i] = row;
		}

		//Copy the rows to the logicFilter...
		for (int i = 0; i < (int)Math.pow(2, count); i++) {
			for (int j = 0; j < (int)Math.pow(2, count); j++) {
				logicFilter.setElement(i, j, rowsTable[i].getElement(0, j));
			}
		}
	}

	public void run(String script) {
		if (script.length() == 0) return;
		script = script.replace(" ", "").replace("\n", "").replace("\t", "");
		if (script.charAt(script.length() - 1) == ';') {
			script = script.substring(0, script.length() - 1);
		}
		String[] commands = script.split(";");
		for (int i = 0; i < commands.length; i++) {
			generateLogicFilter(commands[i]);
			memory = Matrix.mul(memory, logicFilter);
		}
	}

	/*public void runScript(String script) {
		String[] lines = script.split("\n");
		String prog = "";
		for (int i = 0; i < lines.length; i++) {
			String name = lines[i].split(" ")[0];
			for (int j = 0; j < functions.length; j++) {
				if (functions[j].getName().equals(name)) {
					prog += functions[j].parse(lines[i].replace(name, ""));
				}
			}
		}
		execute(prog);
	}*/

	public String toString() {
		String s = "";
		for (int i = 0; i < Math.pow(2, count); i++) {
			double prob = Math.pow(Complex.abs(memory.getElement(0, i)).getReal(), 2);
			if (prob != 0) {
				s += bin(i) + ": " + Double.toString(prob) + "\n";	
			}
		}
		return s;
	}

	/*
		Q-Script is a simple scripting language to accomplish logic operations.
		Examples:
			NOT gate applied to a...
				(): X(a);
			CNOT gate with a as the controller and b as the controlled... 
				(a): X(b);
			SWAP gate between a and b...
				(!a,b): X(a) + (a,!b):X(a) + (!a,b):X(b) + (a,!b):X(b);
	*/
	/*public static void main(String[] args) {
		
		//qc.setGates("id <():ID(a);>\nx <():X(a);>\ny <():Y(a);>\nz <():Z(a);>\nh <():H(a);>\nt <():T(a);>\ntdg <():Tdg(a);>\ns <():S(a);>\nsdg <():Sdg(a);>\ncx <(a):X(b);>\nccx <(a, b):X(c)>\nswap <(!a, b):X(a) + (a, !b):X(a) + (!a, b):X(b) + (a, !b):X(b);>\ncswap <(a, !b, c):X(b) + (a, b, !c):X(b) + (a, !b, c):X(c) + (a, b, !c):X(c);>");
		//qc.run("h 1\nh 2\ns 1\nh 2\ncx 1, 2\nh 2\ns 1\nh 1\nh 2\nx 1\nx 2\nh 2\ncx 1, 2\nh 2\nx 1\nx 2\nh 1\nh 2");

		//qc.run("h 1\nh 2\ns 2\nh 2\ncx 1, 2\nh 2\ns 2\nh 1\nh 2\nx 1\nx 2\nh 2\ncx 1, 2\nh 2\nx 1\nx 2\nh 1\nh 2");
		

		//Functions fs = new Function[5];
		//fs[0] = new Function("swap", "");
		//qc.defineFunctions("x(a)='():X(a);',h(a)='():H(a);'");
		//qc.execute("():X(2) + ():X(1);       ");
		
	}*/
}


