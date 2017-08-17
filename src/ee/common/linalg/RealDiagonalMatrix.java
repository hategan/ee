package ee.common.linalg;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RealDiagonalMatrix implements DoubleMatrix {
	private double[] diag;
	
	public RealDiagonalMatrix(double... x) {
		diag = x;
	}

	@Override
	public boolean isDiagonal() {
		return true;
	}

	@Override
	public Real getDiagonalElement(int i) {
		return new Real(diag[i]);
	}
	
	public double getDoubleDiagonalElement(int i) {
		return diag[i];
	}

	@Override
	public double getDouble(int row, int col) {
		if (row == col) {
			return diag[row];
		}
		else {
			return 0;
		}
	}

	@Override
	public int rows() {
		return diag.length;
	}
	
	@Override
	public int cols() {
		return diag.length;
	}

	@Override
	public Real get(int row, int col) {
		if (row == col) {
			return new Real(diag[row]);
		}
		else {
			return Real.ZERO;
		}
	}

	@Override
	public boolean isSquare() {
		return true;
	}

	@Override
	public void madd(Real factor, Matrix<Real> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Real tr() {
		double s = 0;
		for (double x : diag) {
			s += x;
		}
		return new Real(s);
	}

	@Override
	public Matrix<Real> plus(Matrix<Real> y) {
		if (y.rows() != rows() || y.cols() != rows()) {
			throw new IllegalArgumentException();
		}
				
		if (y.isDiagonal()) {
			double[] d = new double[diag.length];
			if (y instanceof RealDiagonalMatrix) {
				RealDiagonalMatrix y2 = (RealDiagonalMatrix) y;
				for (int i = 0; i < diag.length; i++) {
					d[i] = diag[i] + y2.getDoubleDiagonalElement(i);
				}
			}
			else {
				for (int i = 0; i < diag.length; i++) {
					d[i] = diag[i] + y.getDiagonalElement(i).value();
				}
			}
			return new RealDiagonalMatrix(d);
		}
		else {
			RealMatrix m = new RealMatrix(rows(), rows());
			for (int i = 0; i < rows(); i++) {
				m.set(i, i, diag[i]);
			}
			m.madd(y);
			return m;
		}
	}
	
	@Override
	public Matrix<Real> times(Real s) {
		double sd = s.value();
		double[] d = new double[rows()];
		for (int i = 0; i < rows(); i++) {
			d[i] = diag[i] * sd;
		}
		return new RealDiagonalMatrix(d);
	}
	
	@Override
	public Matrix<Real> times(Matrix<Real> y) {
		if (y.rows() != rows()) {
			throw new IllegalArgumentException();
		}
		
		if (y.isDiagonal()) {
			return timesDiag(y);
		}
		else {
			return y.times(this);
		}
	}

	private Matrix<Real> timesDiag(Matrix<Real> y) {
		if (y instanceof RealDiagonalMatrix) {
			return timesRDM((RealDiagonalMatrix) y);
		}
		else {
			double[] d = new double[rows()];
			for (int i = 0; i < rows(); i++) {
				d[i] = diag[i] * y.getDiagonalElement(i).value();
			}
			return new RealDiagonalMatrix(d);
		}
	}

	private Matrix<Real> timesRDM(RealDiagonalMatrix y) {
		double[] d = new double[rows()];
		for (int i = 0; i < rows(); i++) {
			d[i] = diag[i] * y.getDoubleDiagonalElement(i);
		}
		return new RealDiagonalMatrix(d);
	}
	
	public String toString() {
		NumberFormat nf = new DecimalFormat(" ##0.#####");
		String zero = "            ";
		StringBuilder sb = new StringBuilder();
		sb.append('\n');
		for (int i = 0; i < rows(); i++) {
			sb.append("(");
			for (int j = 0; j < rows(); j++) {
				if (i == j) {
					sb.append(nf.format(diag[i]));
				}
				else {
					sb.append(zero);
				}
			}
			sb.append(")\n");
		}
		return sb.toString();
	}

	@Override
	public Matrix<Real> transpose() {
		return this;
	}
}
