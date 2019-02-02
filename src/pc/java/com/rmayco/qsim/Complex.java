package com.rmayco.qsim;

public class Complex {
	private double real, imaginary;
	public Complex(double r, double i) {
		real = r;
		imaginary = i;
	}
	public double getReal() {
		return real;
	}
	public double getImaginary() {
		return imaginary;
	}
	public void setReal(double r) {
		real = r;
	}
	public void setImaginary(double i) {
		imaginary = i;
	}

	public String toString() {
		String ret = "";

		if (real == 0 && imaginary == 0) {
			ret = "0";
		} else if (real == 0) {
			if (Math.abs(imaginary) != 1) {
				ret += Double.toString(imaginary);
			} else {
				ret += imaginary == -1 ? "-" : "";
			}
			ret += "i";
		} else if (imaginary == 0) {
			ret += Double.toString(real);
		} else {
			ret += Double.toString(real);
			if (imaginary > 0) ret += " + ";
			if (imaginary < 0) ret += " - ";
			if (Math.abs(imaginary) != 1) {
				ret += Double.toString(Math.abs(imaginary));
			}
			ret += "i";
		}

		return ret;
	}

	public static Complex parseComplex(String s) {
		s = s.replace(" ", "").replace("-i", "-1i");
		Complex n = new Complex(0, 0);

		boolean containsImaginaryAndReal = false;
		boolean containsImaginary = false;
		boolean isOnlyImaginary = false;
		boolean isOnlyReal = false;
		boolean negativeReal = false;
		boolean negativeImaginary = false;

		if (s.equals("i")) {
			n.setImaginary(1);
			return n;
		}

		if (s.equals("")) {
			return n;
		}

		//Check if the string only contains an imaginary number...
		if (!s.replace("i", "").equals(s)) {
			containsImaginary = true;
		}

		//Check if the string only contains both an imaginary and a real number...
		if (containsImaginary) {
			for (int i = 0; i < s.length(); i++) {
				if (s.charAt(i) == '+' || s.charAt(i) == '-') {
					if (i != 0) {
						containsImaginaryAndReal = true;
						break;
					}
				}
			}
		}

		//Determine whether we have just a real number or just an imaginary number.
		if (!containsImaginaryAndReal && !containsImaginary) isOnlyReal = true;
		if (!containsImaginaryAndReal && containsImaginary) isOnlyImaginary = true;

		//Generate string if there's only a real number.
		if (isOnlyReal) {
			try {
				n.setReal(Double.parseDouble(s));
				return n;
			} catch (Exception e) {
				System.out.println("Error: Malformed complex string.");
				System.exit(1);
			}
		}

		//Generate string if there's only an imaginary number.
		if (isOnlyImaginary) {
			try {
				n.setImaginary(Double.parseDouble(s.replace("i", "")));
				return n;
			} catch (Exception e) {
				System.out.println("Error: Malformed complex string.");
				System.exit(1);
			}
		}

		//Determine if the real number is positive or negative...
		if (s.charAt(0) == '-') {
			negativeReal = true;
			s = s.substring(1, s.length() - 1);
		}

		//Determine if the imaginary number is positive or negative...
		if (!s.replace("-", "").equals(s)) {
			negativeImaginary = true;
		}

		//Split and parse the string appropriately... 
		String[] complexString;
		if (negativeImaginary) complexString = s.replace("i", "").split("-");
		else complexString = s.replace("i", "").split("\\+");

		//Negate as appropriate...
		if (negativeReal) n.setReal(-Double.parseDouble(complexString[0]));
		else n.setReal(Double.parseDouble(complexString[0]));
		if (negativeImaginary) n.setImaginary(-Double.parseDouble(complexString[1]));
		else n.setImaginary(Double.parseDouble(complexString[1]));

		//Return new complex number.
		return n;
	}

	public static Complex add(Complex n1, Complex n2) {
		return new Complex(n1.getReal() + n2.getReal(), n1.getImaginary() + n2.getImaginary());
	}

	public static Complex sub(Complex n1, Complex n2) {
		return new Complex(n1.getReal() - n2.getReal(), n1.getImaginary() - n2.getImaginary());
	}

	public static Complex mul(Complex n1, Complex n2) {
		double F, O, I, L;
		F = n1.getReal() * n2.getReal();
		O = n1.getReal() * n2.getImaginary();
		I = n1.getImaginary() * n2.getReal();
		L = n1.getImaginary() * n2.getImaginary();
		return new Complex(F - L, O + I);
	}

	public static Complex abs(Complex n) {
		return new Complex(Math.sqrt( (n.getReal() * n.getReal()) + (n.getImaginary() * n.getImaginary()) ), 0);
	}
}



