package ee.z2.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ee.common.State;
import ee.common.SubspaceMatcher;

/**
 * Represents a spin-chain state.
 *
 */
public class SpinsState implements State<EncodedZ2Vector> {
	private final int nspins;
	private final Map<EncodedZ2Vector, Double> m;

	/**
	 * Constructs a Z2/spin chain state by specifying the state coefficients.
	 * The coefficients do not need to be normalized, since this constructor
	 * normalizes them. The coefficients are specified in the numeric order of
	 * the binary representation of the spins. For example, for two spins, the
	 * basis vectors are |-->, |-+>, |+->, |++>, corresponding to the integers
	 * 0, 1, 2, 3, whose binary representation is 00, 01, 10, 11.
	 * 
	 * @param nspins
	 * @param coeff
	 */
	public SpinsState(int nspins, double... coeff) {
		if (coeff.length != 1 << nspins) {
			throw new IllegalArgumentException("Expected " + (1 << nspins) + " coefficients");
		}
		this.nspins = nspins;
		this.m = new HashMap<>();
		normalize(coeff);
		for (int i = 0; i < coeff.length; i++) {
			m.put(constructVector(i), coeff[i]);
		}
	}

	private void normalize(double[] coeff) {
		double sum = 0;
		for (int i = 0; i < coeff.length; i++) {
			sum += coeff[i] * coeff[i];
		}
		double c = Math.sqrt(sum);
		System.out.println("Normalization: " + c);
		for (int i = 0; i < coeff.length; i++) {
			coeff[i] = coeff[i] / c;
		}
	}

	private EncodedZ2Vector constructVector(int v) {
		EncodedZ2Vector vec = new EncodedZ2Vector(nspins);
		for (int i = 0; i < nspins; i++) {
			vec.push((v & 0x00000001));
			v >>= 1;
		}
		return vec;
	}

	@Override
	public int countNonZeroBasisVectors() {
		return m.size();
	}

	@Override
	public Collection<EncodedZ2Vector> vectors() {
		return m.keySet();
	}

	@Override
	public double getCoefficient(EncodedZ2Vector v) {
		return m.get(v);
	}

	@Override
	public State<EncodedZ2Vector> copy() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		m.clear();
	}

	@Override
	public State<EncodedZ2Vector> ignore(SubspaceMatcher<EncodedZ2Vector> mask) {
		throw new UnsupportedOperationException();
	}
}
