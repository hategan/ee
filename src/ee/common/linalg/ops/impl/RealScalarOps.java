package ee.common.linalg.ops.impl;

import ee.common.linalg.Real;
import ee.common.linalg.ops.ScalarOps;

public class RealScalarOps implements ScalarOps<Real> {

	@Override
	public Real zero() {
		return Real.ZERO;
	}
	
	@Override
	public boolean isZero(Real x) {
		if (x == Real.ZERO) {
			return true;
		}
		else {
			return x.value() == 0;
		}
	}



	@Override
	public Real neg(Real x) {
		if (x.isSpecial()) {
			if (x == Real.ONE) {
				return Real.MINUS_ONE;
			}
			else if (x == Real.MINUS_ONE) {
				return Real.ONE;
			}
			else if (x == Real.ZERO) {
				return x;
			}
		}
		return new Real(-x.value());
	}

	@Override
	public Real plus(Real x, Real y) {
		if (x == Real.ZERO) {
			return y;
		}
		else if (y == Real.ZERO) {
			return x;
		}
		else {
			return new Real(x.value() + y.value());
		}
	}

	@Override
	public Real times(Real x, Real y) {
		if (x.isSpecial()) {
			if (x == Real.ZERO) {
				return Real.ZERO;
			}
			else if (x == Real.ONE) {
				return y;
			}
		}
		else if (y.isSpecial()) {
			if (y == Real.ZERO) {
				return Real.ZERO;
			}
			else if (y == Real.ONE) {
				return x;
			}
		}
		return new Real(x.value() * y.value());
	}

	@Override
	public Real div(Real x, Real y) {
		if (x == Real.ZERO) {
			return Real.ZERO;
		}
		if (y.isSpecial()) {
			if (y == Real.ZERO) {
				throw new ArithmeticException("div by 0");
			}
			else if (y == Real.ONE) {
				return x;
			}
		}
		return new Real(x.value() / y.value());
	}

	@Override
	public Real log(double base, Real x) {
		return new Real(Math.log(x.value()) / Math.log(base));
	}

	@Override
	public Real minusOne() {
		return Real.MINUS_ONE;
	}

	@Override
	public Real fromNumber(Number n) {
		return new Real(n.doubleValue());
	}
}
