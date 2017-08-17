package ee.common;

/**
 * An interface that can be implemented to signal that the
 * implementing class can be seen as a basis vector
 */
public interface BasisVector {
	boolean equals(Object o);
	
	int hashCode();
}
