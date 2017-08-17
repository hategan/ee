package ee.common.linalg;


public class RealMatrix implements DoubleMatrix {
	private final double m[][];
	private final int rows, cols;
	
	private RealMatrix transpose;
	
	public RealMatrix(double m[][]) {
		this.m = m;
		this.rows = m.length;
		this.cols = m[0].length;
	}
	
	public RealMatrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.m = new double[rows][cols];
	}

	@Override
	public boolean isDiagonal() {
		return false;
	}

	@Override
	public Real getDiagonalElement(int i) {
		return new Real(m[i][i]);
	}
	
	public void set(int row, int col, double val) {
		m[row][col] = val;
	}

	@Override
	public Real get(int row, int col) {
		return new Real(m[row][col]);
	}
	
	public double getDouble(int row, int col) {
		return m[row][col];
	}

	@Override
	public int rows() {
		return rows;
	}

	@Override
	public int cols() {
		return cols;
	}

	@Override
	public boolean isSquare() {
		return rows == cols;
	}

	@Override
	public void madd(Real factor, Matrix<Real> m) {
		if (m instanceof DoubleMatrix) {
			madd(factor.value(), (DoubleMatrix) m);
		}
		else {
			throw new UnsupportedOperationException();
		}
	}

	public void madd(double factor, DoubleMatrix rm) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				m[i][j] += factor * rm.getDouble(i, j);
			}
		}
	}

	@Override
	public Real tr() {
		double s = 0;
		if (!isSquare()) {
			throw new UnsupportedOperationException();
		}
		for (int i = 0; i < rows; i++) {
			s += m[i][i];
		}
		return new Real(s);
	}

	@Override
	public Matrix<Real> plus(Matrix<Real> y) {
		return plus(m, y);
	}

	public Matrix<Real> plus(double[][] m, Matrix<Real> y) {
		if (rows != y.rows() || cols != y.cols()) {
			throw new IllegalArgumentException();
		}
		
		RealMatrix r = new RealMatrix(rows, cols);
		if (y instanceof RealMatrix) {
			RealMatrix y2 = (RealMatrix) y;
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					r.m[i][j] = m[i][j] + y2.getDouble(i, j); 
				}
			}
		}
		else {
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					r.m[i][j] = m[i][j] + y.get(i, j).value(); 
				}
			}
		}
		return r;
	}

	@Override
	public Matrix<Real> times(Real s) {
		double sd = s.value();
		RealMatrix r = new RealMatrix(rows, cols);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				r.m[i][j] = m[i][j] * sd;
			}
		}
		return r;
	}

	@Override
	public Matrix<Real> times(Matrix<Real> y) {
		if (cols != y.rows()) {
			throw new IllegalArgumentException();
		}
		if (y.isDiagonal()) {
			if (y instanceof RealDiagonalMatrix) {
				return timesRDM((RealDiagonalMatrix) y);
			}
			else {
				return timesDiagonal(y);
			}
		}
		else if (y instanceof RealMatrix) {
			return timesRM((RealMatrix) y);
		}
		else {
			RealMatrix r = new RealMatrix(rows, y.cols());
			
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < y.cols(); j++) {
					double s = 0;
					for (int k = 0; k < cols; k++) {
						s += m[i][k] * y.get(k, j).value();
					}
					r.m[i][j] = s;
				}
			}
			
			return r;
		}
	}
	
	private Matrix<Real> timesRM(RealMatrix y) {
		RealMatrix r = new RealMatrix(rows, y.cols);
		for (int i = 0; i < rows; i++) {
			double[] m1 = m[i];
			for (int j = 0; j < y.cols(); j++) {
				double s = 0;
				double[] m2 = ((RealMatrix) y.transpose()).m[j];
				for (int k = 0; k < cols; k++) {
					s += m1[k] * m2[k];
				}
				r.m[i][j] = s;
			}
		}
		return r;
	}


	private Matrix<Real> timesRM2(RealMatrix y) {
		RealMatrix r = new RealMatrix(rows, y.cols);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < y.cols(); j++) {
				double s = 0;
				for (int k = 0; k < cols; k++) {
					s += m[i][k] * y.m[k][j];
				}
				r.m[i][j] = s;
			}
		}
		return r;
	}

	private Matrix<Real> timesDiagonal(Matrix<Real> y) {
		RealMatrix r = new RealMatrix(rows, y.cols());
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < y.cols(); j++) {
				r.m[i][j] = m[i][j] * y.getDiagonalElement(j).value();
			}
		}
		return r;
	}

	private Matrix<Real> timesRDM(RealDiagonalMatrix y) {
		RealMatrix r = new RealMatrix(rows, y.cols());
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < y.cols(); j++) {
				r.m[i][j] = m[i][j] * y.getDoubleDiagonalElement(j);
			}
		}
		return r;
	}

	public void madd(Matrix<Real> y) {
		plus(m, y);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('\n');
		for (int i = 0; i < rows; i++) {
			sb.append("(");
			for (int j = 0; j < cols; j++) {
				sb.append(String.format("%12.4f", m[i][j]));
			}
			sb.append(")\n");
		}
		return sb.toString();
	}
	
	@Override
	public Matrix<Real> transpose() {
		if (transpose == null) {
			RealMatrix r = new RealMatrix(rows, cols);
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					r.m[i][j] = m[j][i];
				}
			}
			transpose = r;
		}
		return transpose;
	}
}
