package ee.common.linalg.ops;

import ee.common.linalg.Matrix;
import ee.common.linalg.Scalar;

public interface MatrixOps<T extends Scalar> {

	Matrix<T> zero(int rows, int cols);

	Matrix<T> minusOne(int n);

	Matrix<T> plus(Matrix<T> x, Matrix<T> y);
	
	Matrix<T> times(Matrix<T> x, Matrix<T> y);
}
