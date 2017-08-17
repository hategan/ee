package ee.geometry;

public interface LinearCoords extends Coords {
	int index();

	int clamp(int dir, int n);
}
