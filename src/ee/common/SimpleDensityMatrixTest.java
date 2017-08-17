package ee.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import ee.common.linalg.DoubleMatrix;
import ee.common.linalg.Matrix;
import ee.common.linalg.Real;
import ee.common.linalg.RealMatrix;
import ee.geometry.SequentialROI;
import ee.geometry.SequentialSpace;
import ee.z2.common.EncodedZ2Vector;
import ee.z2.common.EncodedZ2VectorMatcher;
import ee.z2.common.SpinsState;

public class SimpleDensityMatrixTest {
	public static final double DELTA = 1e-8;
	
	@Test
	public void testSimpleState() {
		// check that we get the correct density matrix
		// for &alpha;|+> + &beta;|->

		double alpha = 0.1;
		double beta = Math.sqrt(1 - alpha * alpha);

		SimpleDensityMatrix<EncodedZ2Vector> rho = new SimpleDensityMatrix<>(new SpinsState(1, beta, alpha));
		
		assertEquals(rho.tr().value(), 1.0, DELTA);
		
		// check that rho^2 = rho; sadly, SimpleDensityMatrix doesn't support multiplication directly
		Matrix<Real> m = new RealMatrix(2, 2);
		m = m.plus(rho);
		m = m.times(m);
		
		for (int r = 0; r < 2; r++) {
			for (int c = 0; c < 2; c++) {
				assertEquals(m.get(r, c).value(), rho.getDouble(r, c), DELTA);
			}
		}
	}
	
	@Test
	public void testReduce() {
		// test the partial trace implementation
		
		/*
		 *  Start with
		 *  a|--> + b|-+> + c|+-> + d|-->
		 *  
		 *  (using the convention |ab>^\dagger = <ba|)
		 *  
		 *  This leads to rho =
		 *        |-->   |-+>   |+->   |++>
		 *        ------------------------- 
		 *  <--| | a^2     ab     ac     ad
		 *  <+-| |  ba    b^2     bc     bd
		 *  <-+| |  ca     cb    c^2     cd
		 *  <++| |  da     db     dc    d^2
		 *  
		 *  This can be written as:
		 *  
		 *  (a^2 |--><--| + ca |--><-+| + ac |+-><--| + c^2 |+-><-+|) + 
		 *  (b^2 |-+><+-| + db |-+><++| + bd |++><+-| + d^2 |++><++|) +
		 *  other terms
		 *  
		 *  Then trace over the second spin (the one next to ">") to get:
		 *  
		 *  (a^2 |-><-| + ca |-><+| + ac |+><-| + c^2 |+><+|) +
		 *  (b^2 |-><-| + db |-><+| + bd |+><-| + d^2 |+><+|) =
		 *  (a^2 + b^2)|-><-| + (ca + db) |-><+| + (ac + bd) |+><-| + (c^2 + d^2) |+><+|
		 */
		
		double a = 0.1;
		double b = 0.2;
		double c = 0.3;
		double d = Math.sqrt(1 - a * a - b * b - c * c);
		
		// |-->, |-+>, |+->, |++>
		SpinsState state = new SpinsState(2, a, b, c, d);
		SimpleDensityMatrix<EncodedZ2Vector> rho = new SimpleDensityMatrix<>(state);
		
		// must translate between vectors and corresponding indices
		// in the density matrix since SimpleDensityMatrix does not
		// enforce a particular mapping
		EncodedZ2Vector[] vectors = new EncodedZ2Vector[] {
				EncodedZ2Vector.fromArray(0, 0),
				EncodedZ2Vector.fromArray(0, 1),
				EncodedZ2Vector.fromArray(1, 0),
				EncodedZ2Vector.fromArray(1, 1),
		};
		
		int[] indices = new int[vectors.length];
		for (int i = 0; i < vectors.length; i++) {
			indices[i] = rho.getIndex(vectors[i]);
		}
		
		double[] s = new double[] {a, b, c, d};
		
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s.length; j++) {
				assertEquals(rho.get(indices[i], indices[j]).value(), s[i] * s[j], DELTA);
			}
		}
		
		SubspaceMatcher<EncodedZ2Vector> m = new EncodedZ2VectorMatcher(new SequentialSpace(2), new SequentialROI(0, 0));
		DoubleMatrix rhoA = rho.reduce(m);
		
		// we don't know at this point what the row/column order is
		double diag1 = a * a + b * b;
		double diag2 = c * c + d * d;
		double offdiag = c * a + d * b;
		
		int[] mapping;
		
		System.out.println(rhoA);
		System.out.println(diag1);
		System.out.println(diag2);
		System.out.println(offdiag);
		
		if (equals(rhoA.getDouble(0, 0), diag1)) {
			mapping = new int[] {0, 1};
		}
		else if (equals(rhoA.getDouble(0, 0), diag2)) {
			mapping = new int[] {1, 0};
		}
		else {
			fail("None of the diagonal elements match");
			// fail may always throw an exception, but the compiler doesn't know that 
			mapping = new int[] {0, 0};
		}
		
		assertEquals(rhoA.getDouble(mapping[0], mapping[0]), diag1, DELTA);
		assertEquals(rhoA.getDouble(mapping[1], mapping[1]), diag2, DELTA);
		assertEquals(rhoA.getDouble(mapping[0], mapping[1]), offdiag, DELTA);
		assertEquals(rhoA.getDouble(mapping[1], mapping[0]), offdiag, DELTA);
	}

	private boolean equals(double a, double b) {
		return Math.abs(a - b) < DELTA;
	}
}
