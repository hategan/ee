package ee.common;

import java.util.Collection;

/**
 * Interface representing an abstract quantum state in some basis. A state
 * is an object of the form &sum; c<sub>i</sub> |v<sub>i</sub>&gt;. Only real
 * coefficients are dealt with.
 *
 * @param <T>
 */
public interface State<T extends BasisVector> {
	int countNonZeroBasisVectors();

	Collection<T> vectors();
		
	double getCoefficient(T v);

	State<T> copy();

	void clear();

	State<T> ignore(SubspaceMatcher<T> m);
}
