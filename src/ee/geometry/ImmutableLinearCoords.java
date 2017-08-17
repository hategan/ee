package ee.geometry;

public class ImmutableLinearCoords extends AbstractLinearCoords {
	private final int value;
	
	public ImmutableLinearCoords(int value) {
		super(null);
		this.value = value;
	}
	
	public ImmutableLinearCoords(Geometry geom, int value) {
		super(geom);
		this.value = value;
	}

	public int index() {
		return value;
	}
}
