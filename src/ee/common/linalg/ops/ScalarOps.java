package ee.common.linalg.ops;

import ee.common.linalg.Scalar;

public interface ScalarOps<T extends Scalar> {

	T zero();
	
	boolean isZero(T x);

	T neg(T x);
	
	T plus(T x, T y);
	
	T times(T x, T y);
	
	T div(T x, T y);
	
	T log(double base, T x);

	T minusOne();

	T fromNumber(Number n);
}
