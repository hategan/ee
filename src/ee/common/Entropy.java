package ee.common;

import ee.common.linalg.Matrix;
import ee.common.linalg.Real;
import ee.common.linalg.Scalar;
import ee.common.linalg.ops.MatrixOps;
import ee.common.linalg.ops.ScalarOps;

public class Entropy<T extends Scalar> {
	static final int ITERATIONS = 200;
	
	private Matrix<T> m;
	private MatrixOps<T> mops;
	private ScalarOps<T> sops;
	
	public Entropy(Matrix<T> m, MatrixOps<T> mops, ScalarOps<T> sops) {
		this.m = m;
		this.mops = mops;
		this.sops = sops;
	}
	
	public T getEntropy() {
		if (m.isDiagonal()) {
			return diagonalEntropy();
		}
		else {
			return seriesEntropyJBlas();
		}
	}

	/**
	 * A version that doesn't use JBLAS
	 * @return
	 */
	@SuppressWarnings("unused")
	private T seriesEntropy() {
		/*
		 * x ln(x) = -x sum(k = 1, +inf) (-1)^k (-1 + x)^k / k
		 * 
		 * so -x ln(x) = x sum(...
		 */
		
		if (!m.isSquare()) {
			throw new IllegalArgumentException("Must be a square matrix");
		}
		
		int n = m.rows();
		
		// S = 0
		Matrix<T> s = mops.zero(n, n);
		
		// IM = M - 1
		Matrix<T> im = mops.plus(m, mops.minusOne(n));
		
		// MK = IM
		Matrix<T> mk = im;
		
		// m1 = -1
		T m1 = sops.minusOne();
		
		long start = System.currentTimeMillis();
		for (int k = 1; k <= ITERATIONS; k++) {
			// S = S + m1 / k * MK = S + (-1)^k / k * (M - 1)^k 
			s.madd(sops.div(m1, sops.fromNumber(k)), mk);
			// m1 = -m1 = -1^k
			m1 = sops.neg(m1);
			// MK = MK * IM = IM^k
			mk = mops.times(mk, im);
			System.out.print("+");
		}
		long end = System.currentTimeMillis();
		long nops = ((long) n) * n * n * ITERATIONS / 1000000 * 1000 / (end - start);
				
		System.out.println(" " + nops + " Mops/s (n = " + n + ")");
		
		s = m.times(s).times(sops.fromNumber(1 / Math.log(2)));
		
		return s.tr();
	}
	
	private org.jblas.DoubleMatrix getJBlasM() {
		org.jblas.DoubleMatrix dm = new org.jblas.DoubleMatrix(m.rows(), m.cols());
		
		for (int i = 0; i < m.rows(); i++) {
			for (int j = 0; j < m.cols(); j++) {
				dm.put(i, j, ((Real) m.get(i, j)).value());
			}
		}
		
		return dm;
	}
	
	public T seriesEntropyJBlas() {		
		if (!m.isSquare()) {
			throw new IllegalArgumentException("Must be a square matrix");
		}
		
		org.jblas.DoubleMatrix dm = getJBlasM();
		
		int n = m.rows();
		
		// S = 0
		org.jblas.DoubleMatrix s = org.jblas.DoubleMatrix.zeros(n, n);
				
		// IM = M - 1
		org.jblas.DoubleMatrix im = dm.sub(org.jblas.DoubleMatrix.eye(n));
		
		// MK = IM
		org.jblas.DoubleMatrix mk = im;
		
		// m1 = -1
		double m1 = -1;
		
		long start = System.currentTimeMillis();
		for (int k = 1; k <= ITERATIONS; k++) {
			// S = S + m1 / k * MK = S + (-1)^k / k * (M - 1)^k
			double f = m1 / k;
			s.addi(mk.mul(f), s);
			
			// m1 = -m1 = -1^k
			m1 = -m1;
			// MK = MK * IM = IM^k
			mk = mk.mmul(im);
			System.out.print("+");
		}
		long end = System.currentTimeMillis();
		long nops = ((long) n) * n * n * ITERATIONS / 1000000 * 1000 / (end - start + 1);
				
		System.out.println(" " + nops + " Mops/s (n = " + n + ")");
		
		s = dm.mmul(s);
		double f2 = 1 / Math.log(2);
		s.muli(f2, s);
		
		double tr = 0;
		for (int i = 0; i < n; i++) {
			tr += s.get(i,  i);
		}
		
		return sops.fromNumber(tr);
	}


	public T diagonalEntropy() {
		T e = sops.zero();
		for (int i = 0; i < m.rows(); i++) {
			T d = m.getDiagonalElement(i);
			if (!sops.isZero(d)) {
				e = sops.plus(e, sops.times(sops.neg(d), sops.log(2, d)));
			}
		}
		return e; 
	}
}
