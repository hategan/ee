package ee.common;

import ee.common.linalg.Matrix;
import ee.common.linalg.Real;

public interface DensityMatrix<T extends BasisVector> extends Matrix<Real> {
	Matrix<Real> reduce(SubspaceMatcher<T> m);
}
