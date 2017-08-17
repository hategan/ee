package ee.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ee.common.linalg.Matrix;
import ee.common.linalg.Real;
import ee.common.linalg.RealDiagonalMatrix;
import ee.common.linalg.RealMatrix;
import ee.common.linalg.ops.MatrixOps;
import ee.common.linalg.ops.ScalarOps;
import ee.common.linalg.ops.impl.RealMatrixOps;
import ee.common.linalg.ops.impl.RealScalarOps;

public class EntropyTest {
	public static final int ITERATIONS = Entropy.ITERATIONS;
	
	@Test
	public void testSeries() {
		double exact = 0.6 * Math.log(0.6) / Math.log(2);
		double series = series(0.6);
		
		assertEquals(exact, series, 1e-6);
	}

	@Test
	public void test() {		
		Matrix<Real> m = new RealDiagonalMatrix(0.5, 0.5);
		ScalarOps<Real> sops = new RealScalarOps();
		MatrixOps<Real> mops = new RealMatrixOps();
		
		Entropy<Real> e = new Entropy<>(m, mops, sops);
		
		double ee = e.getEntropy().value();
		System.out.println(ee);
		assertEquals(ee, 1.0, 1e-9);
	}
	
	@Test
	public void testSimilarityInvariance() {
		double a = 0.3;
		double b = Math.sqrt(1 - a * a);
		Matrix<Real> m = new RealDiagonalMatrix(a * a, b * b);
		
		Matrix<Real> s = rotationMatrix(0.2);
		Matrix<Real> m2 = s.transpose().times(m).times(s);
		System.out.println(m.toString());
		System.out.println(m2.toString());
		ScalarOps<Real> sops = new RealScalarOps();
		MatrixOps<Real> mops = new RealMatrixOps();
		
		Entropy<Real> e = new Entropy<>(m, mops, sops);
		Entropy<Real> f = new Entropy<>(m2, mops, sops);
		
		double ee = e.getEntropy().value();
		double fe = f.getEntropy().value();
		System.out.println(ee);
		System.out.println(fe);
		assertEquals(ee, fe, 1e-5);
	}

	private Matrix<Real> rotationMatrix(double alpha) {
		double sa = Math.sin(alpha);
		double ca = Math.cos(alpha);
		return new RealMatrix(new double[][] {{ca, sa}, {-sa, ca}});
	}

	private double series(double m) {
		double s = 0;
		
		double im = m - 1;
		
		double mk = im;
		
		double m1 = -1;
		
		for (int k = 1; k <= ITERATIONS; k++) {
			s = s + m1 / k * mk;
			m1 = -m1;
			mk = mk * im;
		}
		
		s = - m * s / Math.log(2);
		
		System.out.println(s);
		return s;
	}
}
