package com.rmayco.qsim;

public class Matrix {
	private int rows, cols;
	private Complex[] data;

	public Matrix(int r, int c) {
		data = new Complex[r*c];
		rows = r;
		cols = c;
		for (int i = 0; i < rows * cols; i++) {
			data[i] = new Complex(0, 0);
		}
	}

	public void setElement(int row, int col, Complex c) {
		int addr = col + row * cols;
		if (addr >= data.length || addr < 0) {
			System.out.println("Invalid call to Matrix.setElement(): Out of bounds exception.");
			System.exit(1);
		}
		data[addr] = c;
	}

	public Complex getElement(int row, int col) {
		int addr = col + row * cols;
		if (addr >= data.length || addr < 0) {
			System.out.println("Invalid call to Matrix.getElement(): Out of bounds exception.");
			System.exit(1);
		}
		return data[col + row * cols];
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public String toString() {
		String ret = "";
		for (int y = 0; y < rows; y++) {
			ret += "[";
			for (int x = 0; x < cols; x++) {
				ret += getElement(y, x).toString();
				if (x != cols - 1) ret += ", ";
			}
			ret += "]";
			if (y != rows - 1) ret += "\n";
		}
		return ret;
	}

	//Add two matrices.
	public static Matrix add(Matrix a, Matrix b) {
		if (a.getRows() != b.getRows() || a.getCols() != b.getCols()) {
			System.out.println("Invalid call to Matrix.add(): Inappropriate dimensions of input matrices.");
			System.exit(1);
		}
		Matrix ret = new Matrix(b.rows, b.cols);
		for (int i = 0; i < b.getRows(); i++) {
			for (int j = 0; j < b.getCols(); j++) {
				ret.setElement(i, j, Complex.add(a.getElement(i, j), b.getElement(i, j)));
			}
		}
		return ret;
	}

	//Subtract two matrices.
	public static Matrix sub(Matrix a, Matrix b) {
		if (a.getRows() != b.getRows() || a.getCols() != b.getCols()) {
			System.out.println("Invalid call to Matrix.add(): Inappropriate dimensions of input matrices.");
			System.exit(1);
		}
		Matrix ret = new Matrix(b.rows, b.cols);
		for (int i = 0; i < b.getRows(); i++) {
			for (int j = 0; j < b.getCols(); j++) {
				ret.setElement(i, j, Complex.sub(a.getElement(i, j), b.getElement(i, j)));
			}
		}
		return ret;
	}

	//Multiply two matrices.
	public static Matrix mul(Matrix a, Matrix b) {
		if (a.getCols() != b.getRows()) {
			System.out.println("Invalid call to Matrix.mul(): Inappropriate dimensions of input matrices.");
			System.exit(1);
		} 
		Matrix ret = new Matrix(a.getRows(), b.getCols());

		//Loop through all items of our output matrix.
		for (int i = 0; i < ret.getRows(); i++) {
			for (int j = 0; j < ret.getCols(); j++) {
				//Sum the products of the corresponding rows and columns.
				Complex sum = new Complex(0, 0);
				for (int k = 0; k < a.getCols(); k++) {
					sum = Complex.add( sum, ( Complex.mul(a.getElement(i, k), b.getElement(k, j)) ) );
				}
				//Set the item inside of C to our summation.
				ret.setElement(i, j, sum);
			}
		}
		return ret;
	}

	//Kronecker product of two matrices.
	public static Matrix knk(Matrix a, Matrix b) {
		Matrix ret = new Matrix(a.getRows() * b.getRows(), a.getCols() * b.getCols());
		for (int i = 0; i < ret.getRows(); i++) {
			for (int j = 0; j < ret.getCols(); j++) {
				int ai = i / b.getRows();
				int aj = j / b.getCols();
				int bi = i % b.getRows();
				int bj = j % b.getCols();
				ret.setElement(i, j, Complex.mul(a.getElement(ai, aj), b.getElement(bi, bj)));
			}
		}
		return ret;
	}
}
