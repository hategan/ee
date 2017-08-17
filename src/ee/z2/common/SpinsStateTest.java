package ee.z2.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SpinsStateTest {
	public static final double DELTA = 1e-29;

	@Test
	public void test() {
		double a = 0.1;
		double b = 0.2;
		double c = 0.3;
		double d;
		
		d = Math.sqrt(1 - a * a - b * b - c * c);
		// 00, 01, 10, 11
		SpinsState s = new SpinsState(2, a, b, c, d);
		
		assertEquals(s.getCoefficient(EncodedZ2Vector.fromArray(0, 0)), a, DELTA);
		assertEquals(s.getCoefficient(EncodedZ2Vector.fromArray(0, 1)), b, DELTA);
		assertEquals(s.getCoefficient(EncodedZ2Vector.fromArray(1, 0)), c, DELTA);
		assertEquals(s.getCoefficient(EncodedZ2Vector.fromArray(1, 1)), d, DELTA);
		
		assertEquals(s.getCoefficient(EncodedZ2Vector.fromInteger(2, 0b00000000)), a, DELTA);
		assertEquals(s.getCoefficient(EncodedZ2Vector.fromInteger(2, 0b00000001)), b, DELTA);
		assertEquals(s.getCoefficient(EncodedZ2Vector.fromInteger(2, 0b00000010)), c, DELTA);
		assertEquals(s.getCoefficient(EncodedZ2Vector.fromInteger(2, 0b00000011)), d, DELTA);
	}
}
