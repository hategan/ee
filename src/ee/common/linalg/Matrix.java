package ee.common.linalg;

/**
 * A generic matrix implementation over some field T
 *
 * @param <T>
 */
public interface Matrix<T extends Scalar> {

	boolean isDiagonal();

	T getDiagonalElement(int i);

	int rows();
	
	int cols();

	boolean isSquare();
	
	/**
	 * Multiply and add in-place
	 *  
	 * @param factor
	 * @param m
	 */
	void madd(T factor, Matrix<T> m);
	
	T tr();

	Matrix<T> plus(Matrix<T> y);

	Matrix<T> times(Matrix<T> y);

	T get(int row, int col);

	Matrix<T> times(T s);

	Matrix<T> transpose();
}
