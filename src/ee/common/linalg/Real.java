package ee.common.linalg;

/*
 * Ok, this is where I develop the opinion that 
 * Java just isn't very good at nicely expressing mathy stuff, even
 * with generics.
 */
public class Real implements Scalar {
	private static class SpecialReal extends Real {
		public SpecialReal(double v) {
			super(v);
		}
		
		public boolean isSpecial() {
			return true;
		}
	}
	
	public static final Real ZERO = new SpecialReal(0);
	public static final Real ONE = new SpecialReal(1);
	public static final Real MINUS_ONE = new SpecialReal(-1);
	
	private final double value;

	public Real(double d) {
		this.value = d;
	}

	public double value() {
		return value;
	}

	public boolean isSpecial() {
		return false;
	}
	
	public String toString() {
		return String.valueOf(value);
	}
}
