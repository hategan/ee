package ee.common;

import ee.common.linalg.Matrix;
import ee.common.linalg.Real;
import ee.common.linalg.ops.impl.RealMatrixOps;
import ee.common.linalg.ops.impl.RealScalarOps;

public class EntanglementEntropy<B extends BasisVector> {
	private final DensityMatrix<B> rho;
	private final SubspaceMatcher<B> m;
	
	public EntanglementEntropy(DensityMatrix<B> rho, SubspaceMatcher<B> m) {
		this.rho = rho;
		this.m = m;
	}
	
	public double getValue() {
		Matrix<Real> rhob = rho.reduce(m);
		//System.out.println("Reduced:\n" + rhob);
		Entropy<Real> e = new Entropy<Real>(rhob, new RealMatrixOps(), new RealScalarOps());
		return e.getEntropy().value();
	}
}
