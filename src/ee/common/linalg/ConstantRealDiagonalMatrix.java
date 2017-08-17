package ee.common.linalg;

public class ConstantRealDiagonalMatrix implements Matrix<Real> {
	private final double c;
	private final Real cr;
	private final int n;
	
	public ConstantRealDiagonalMatrix(int n, double c) {
		this.c = c;
		this.n = n;
		this.cr = new Real(c);
	}

	@Override
	public boolean isDiagonal() {
		return true;
	}

	@Override
	public Real getDiagonalElement(int i) {
		return cr;
	}
	
	@Override
	public Real get(int row, int col) {
		if (row == col) {
			return cr;
		}
		else {
			return Real.ZERO;
		}
	}

	@Override
	public int rows() {
		return n;
	}
	
	@Override
	public int cols() {
		return n;
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
		return new Real(c * n);
	}

	@Override
	public Matrix<Real> plus(Matrix<Real> y) {
		if (y instanceof ConstantRealDiagonalMatrix) {
			ConstantRealDiagonalMatrix y2 = (ConstantRealDiagonalMatrix) y;
			if (n != y2.n) {
				throw new IllegalArgumentException();
			}
			return new ConstantRealDiagonalMatrix(n, c + y2.c);
		}
		else {
			return y.plus(this);
		}
	}

	@Override
	public Matrix<Real> times(Real s) {
		return new ConstantRealDiagonalMatrix(n, c * s.value());
	}

	@Override
	public Matrix<Real> times(Matrix<Real> y) {
		if (y instanceof ConstantRealDiagonalMatrix) {
			ConstantRealDiagonalMatrix y2 = (ConstantRealDiagonalMatrix) y;
			if (n != y2.n) {
				throw new IllegalArgumentException();
			}
			return new ConstantRealDiagonalMatrix(n, c * y2.c);
		}
		else {
			return y.times(this);
		}
	}

	@Override
	public Matrix<Real> transpose() {
		return this;
	}
}
