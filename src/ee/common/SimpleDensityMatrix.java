package ee.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ee.common.linalg.DoubleMatrix;
import ee.common.linalg.Matrix;
import ee.common.linalg.Real;
import ee.common.linalg.RealMatrix;

public class SimpleDensityMatrix<T extends BasisVector> implements DoubleMatrix, DensityMatrix<T> {
	private final List<T> lstate;
	private final double[] coeffs;
		
	public SimpleDensityMatrix(State<T> state) {
		int sz = state.countNonZeroBasisVectors();
		this.lstate = new ArrayList<>(state.vectors());
		if (this.lstate.size() != sz) {
			throw new RuntimeException("Weird state");
		}
		this.coeffs = new double[sz];
		for (int i = 0; i < sz; i++) {
			this.coeffs[i] = state.getCoefficient(this.lstate.get(i));
		}
	}
	
	@Override
	public boolean isDiagonal() {
		return false;
	}

	@Override
	public Real getDiagonalElement(int i) {
		return new Real(sq(coeffs[i]));
	}

	private double sq(double d) {
		return d * d;
	}

	@Override
	public int rows() {
		return coeffs.length;
	}

	@Override
	public int cols() {
		return coeffs.length;
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
		double s = 0;
		for (double d : coeffs) {
			s += d * d;
		}
		return new Real(s);
	}

	@Override
	public Matrix<Real> plus(Matrix<Real> y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Matrix<Real> times(Matrix<Real> y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Real get(int row, int col) {
		return new Real(coeffs[row] * coeffs[col]);
	}
	
	public Real get(T row, T col) {
		return get(getRow(row), getCol(col));
	}
	
	public int getRow(T vector) {
		return getIndex(vector, "row ");
	}
	
	public int getCol(T vector) {
		return getIndex(vector, "column ");
	}
	
	public int getIndex(T vector) {
		return getIndex(vector, "");
	}
	
	private int getIndex(T vector, String what) {
		int ix = lstate.indexOf(vector);
		if (ix == -1) {
			throw new IllegalArgumentException("No such " + what + "vector: " + ix);
		}
		return ix;
	}

	@Override
	public Matrix<Real> times(Real s) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Matrix<Real> transpose() {
		return this;
	}

	@Override
	public double getDouble(int row, int col) {
		return coeffs[row] * coeffs[col];
	}
	
	private class Entry {
		public int ix;
		public double coeff;
		public T complement;
	}
	
	public DoubleMatrix reduce(SubspaceMatcher<T> m) {
		// l = c1|e1> + ... + cn|en>
		Map<Integer, Entry> em = new HashMap<Integer, Entry>();
		
		Map<T, List<Entry>> em2 = new HashMap<>();
		Map<T, Integer> ixm = new HashMap<>();
		int ix = 0;
		int steps = coeffs.length / 100 + 1;
		int cs = 0;
		for (int i = 0; i < coeffs.length; i++) {
			if (cs++ % steps == 0) {
				System.out.print(".");
			}
			Entry e = new Entry();
			em.put(i, e);
			e.coeff = coeffs[i];
			T ei = lstate.get(i);
			T reduced = m.reduce(ei);
			
			Integer ixc = ixm.get(reduced);
			if (ixc == null) {
				ixc = ix++;
				ixm.put(reduced, ixc);
			}
			e.ix = ixc;
			e.complement = m.complement(ei);
			
			add(em2, e);
		}
		System.out.println();
	
		// dumb O(n^2)
		// psi = c_ij |a_i>|b_j>
		// count all |a_i> and give each an index
		
		RealMatrix r = new RealMatrix(ixm.size(), ixm.size());
		steps = em2.size() / 100 + 1;
		cs = 0;
		for (List<Entry> l : em2.values()) {
			if (cs++ % steps == 0) {
				System.out.print("o");
			}
			for (int i1 = 0; i1 < l.size(); i1++) {
				Entry e1 = l.get(i1);
				for (int i2 = i1; i2 < l.size(); i2++) {
					Entry e2 = l.get(i2);
					double v = r.getDouble(e1.ix, e2.ix);
					// this won't actually double-count when ix1 == ix2
					double nv = v + e1.coeff * e2.coeff;
					r.set(e1.ix, e2.ix, nv);
					r.set(e2.ix, e1.ix, nv);
				}
			}
		}
		
		System.out.println();
		
		return r;
	}
	
	private void add(Map<T, List<Entry>> em2, Entry e) {
		List<Entry> l = em2.get(e.complement);
		if (l == null) {
			l = new ArrayList<Entry>();
			em2.put(e.complement, l);
		}
		l.add(e);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('\n');
		for (int i = 0; i < rows(); i++) {
			sb.append("(");
			for (int j = 0; j < cols(); j++) {
				sb.append(String.format("%12.4f", getDouble(i, j)));
			}
			sb.append(")\n");
		}
		return sb.toString();
	}
}
