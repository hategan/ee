package ee.common;

import static org.junit.Assert.*;

import org.junit.Test;

import ee.common.linalg.Matrix;
import ee.common.linalg.Real;
import ee.common.linalg.ops.impl.RealMatrixOps;
import ee.common.linalg.ops.impl.RealScalarOps;
import ee.geometry.SequentialROI;
import ee.geometry.SequentialSpace;
import ee.z2.common.EncodedZ2Vector;
import ee.z2.common.EncodedZ2VectorMatcher;
import ee.z2.common.SpinsState;

public class EntanglementEntropyTest {
	public static final double DELTA = 1e-9;

	@Test
	public void test() {
		double s2 = 1.0 / Math.sqrt(2);
		// |--> |-+> |+-> |++>
		// |--> + |++>
		test(s2, 0, 0, s2, 1, DELTA, true);
		test(s2, 0, s2, 0, 0, DELTA, false);
		test(1, 0, 0, 0, 0, DELTA, true);
		
		double a = 0.1;
		double d = Math.sqrt(1 - a * a);
		double a2 = a * a;
		double d2 = d * d;
		double expected = - a2 * log2(a2) - d2 * log2(d2);
		// series expansion isn't very accurate
		test(a, 0, 0, d, expected, 1e-2, true);
	}

	private double log2(double x) {
		return Math.log(x) / Math.log(2);
	}

	private void test(double mm, double mp, double pm, double pp, double expected, double delta, boolean compareSeriesWithExact) {
		// TODO Auto-generated method stub
		SpinsState state = new SpinsState(2,  mm, mp, pm, pp);
		DensityMatrix<EncodedZ2Vector> rho = new SimpleDensityMatrix<>(state);
		SubspaceMatcher<EncodedZ2Vector> m = new EncodedZ2VectorMatcher(new SequentialSpace(2), new SequentialROI(0, 0));
		
		if (compareSeriesWithExact) {
			Matrix<Real> rhob = rho.reduce(m);
			Entropy<Real> e = new Entropy<Real>(rhob, new RealMatrixOps(), new RealScalarOps());
			
			double exact = e.diagonalEntropy().value();
			double series = e.seriesEntropyJBlas().value();
			assertEquals(exact, series, delta);
			System.out.println("Exact: " + exact + ", series: " + series);
		}
		EntanglementEntropy<EncodedZ2Vector> ee = new EntanglementEntropy<>(rho, m);
		
		assertEquals(ee.getValue(), expected, delta);
	}

}
