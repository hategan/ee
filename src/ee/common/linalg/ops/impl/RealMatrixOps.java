package ee.common.linalg.ops.impl;

import ee.common.linalg.ConstantRealDiagonalMatrix;
import ee.common.linalg.Matrix;
import ee.common.linalg.Real;
import ee.common.linalg.RealMatrix;
import ee.common.linalg.ops.MatrixOps;

public class RealMatrixOps implements MatrixOps<Real> {
	@Override
	public Matrix<Real> zero(int rows, int cols) {
		return new RealMatrix(rows, cols);
	}

	@Override
	public Matrix<Real> minusOne(int n) {
		return new ConstantRealDiagonalMatrix(n, -1);
	}

	@Override
	public Matrix<Real> plus(Matrix<Real> x, Matrix<Real> y) {
		return x.plus(y);
	}

	@Override
	public Matrix<Real> times(Matrix<Real> x, Matrix<Real> y) {
		return x.times(y);
	}

}
