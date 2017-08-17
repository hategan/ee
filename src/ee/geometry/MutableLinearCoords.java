package ee.geometry;

public class MutableLinearCoords extends AbstractLinearCoords {
	private int value;
	
	public MutableLinearCoords(Geometry geom) {
		super(geom);
	}

	public int index() {
		return value;
	}

	public LinearCoords inc() {
		value++;
		return this;
	}
	
	public void set(int value) {
		this.value = value;
	}

	public void setCoords(int... x) {
		if (geom.getNDim() != x.length) {
			throw new IllegalArgumentException("Invalid number of coordinates");
		}
		
		int ix = 0;
		for (int i = 0; i < x.length; i++) {
			ix += x[i] * geom.getMultiplier(i);
		}
		this.value = ix;
	}
}
