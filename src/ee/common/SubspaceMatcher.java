package ee.common;

public interface SubspaceMatcher<T extends BasisVector> {
	
	/** returns a vector in a space of a dimension that
	 * matches some ROI
	 * ex: |11> -> |1>, |10> -> |1>, |00> -> |0>
	 * 
	 * It may be possible for this method to simply set the vectors
	 * outside the ROI to a specific value:
	 * |11> -> |10>, |10> -> |10>
	 */
	T reduce(T x);
	
	/**
	 * The opposite of reduce(). Alternatively, reduce with a complement ROI
	 */
	T complement(T x);
}
