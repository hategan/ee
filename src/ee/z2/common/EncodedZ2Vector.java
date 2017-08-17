package ee.z2.common;

import java.util.BitSet;

import ee.common.BasisVector;

/**
 * A Z2 vector stored in a BitSet. A spin up is represented by a bit of 1,
 * while a spin down is represented by a bit of 0
 */
public class EncodedZ2Vector implements BasisVector, Comparable<EncodedZ2Vector> {
	protected final BitSet a;
	protected int crt;
	protected final int len;
	
	/**
	 * Construct a vector from another vector
	 * @param v
	 */
	private EncodedZ2Vector(EncodedZ2Vector v) {
		this.a = (BitSet) v.a.clone();
		this.crt = v.crt;
		this.len = v.len;
	}
	
	/**
	 * Construct an empty Z2 vector of a specified dimension
	 * @param len
	 */
	protected EncodedZ2Vector(int len) {
		this.len = len;
		this.a = new BitSet(len);
	}
	
	/**
	 * Returns an all-spins-down vector of a specified length
	 * @param len
	 * @return
	 */
	public static EncodedZ2Vector empty(int len) {
		EncodedZ2Vector v = new EncodedZ2Vector(len);
		for (int i = 0; i < len; i++) {
			v.push(0);
		}
		return v;
	}
	
	/**
	 * Constructs a vector from an array of integers. The integers
	 * can be 1 to signify a spin-up or not 1 for spin-down. The
	 * first element of the array corresponds to the leftmost spin in
	 * a vector |xxxxx>. In principle, the order could be arbitrary. 
	 * However, this restriction is imposed such that 
	 * 	EncodedZ2Vector.fromArray(1, 0, 0)
	 *   and
	 *  EncodedZ2Vector.fromBits(3, 0b00000100)
	 * produce the same vector.
	 * @param bits
	 * @return
	 */
	public static EncodedZ2Vector fromArray(int... bits) {
		EncodedZ2Vector v = new EncodedZ2Vector(bits.length);
		for (int i = bits.length - 1; i >= 0; i--) {
			v.push(bits[i]);
		}
		return v;
	}
	
	
	/**
	 * Constructs a vector from the bits of an integer. Each bit
	 * represents either a spin-up (1) or a spin-down (0)
	 * @param len
	 * @param n
	 * @return
	 */
	public static EncodedZ2Vector fromInteger(int len, int n) {
		EncodedZ2Vector v = new EncodedZ2Vector(len);
		for (int i = 0; i < len; i++) {
			v.push(n & 1);
			n >>= 1;
		}
		return v;
	}

	/**
	 * Adds a spin state to a partially constructed vector
	 * @param v
	 */
	public void push(int v) {
		a.set(crt++, v == 1);
		if (crt > len) {
			throw new IllegalArgumentException("Too many bits for state of length " + len);
		}
	}
		
	public int size() {
		return len;
	}

	@Override
	public int hashCode() {
		check();
		return a.hashCode();
	}
	
	protected void check() {
		if (crt != len) {
			throw new IllegalArgumentException("hashCode() called on an incomplete vector; crt: " + crt + ", len: " + len);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EncodedZ2Vector) {
			EncodedZ2Vector es = (EncodedZ2Vector) obj;
			
			if (es.len != len) {
				return false;
			}
			
			return a.equals(es.a);
		}
		else {
			return false;
		}
	}
		
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(a.get(i) ? "1" : "0");
		}
		return sb.toString();
	}

	/**
	 * Returns a vector that is a copy of this vector with all spins flipped.
	 * @return
	 */
	public EncodedZ2Vector complement() {
		EncodedZ2Vector v2 = new EncodedZ2Vector(this);
		v2.a.flip(0, len);
		return v2;
	}

	@Override
	public int compareTo(EncodedZ2Vector o) {
		int crt = 0;
		while (true) {
			int p1 = o.a.nextSetBit(crt);
			int p2 = a.nextSetBit(crt);
			if (p1 == p2) {
				if (p1 == -1) {
					return o.len - len;
				}
				crt = p1 + 1;
			}
			else {
				if (p1 == -1) {
					return 1;
				}
				if (p2 == -1) {
					return -1;
				}
				return p1 - p2;
			}
		}
	}
}
