package ee.geometry;

import java.util.Iterator;
import java.util.stream.IntStream;

public class SequentialSpace implements Iterable<Coords> {
	private final int n;
	
	public SequentialSpace(int n) {
		this.n = n;
	}

	@Override
	public Iterator<Coords> iterator() {
		return IntStream.range(0, n).mapToObj(
			n -> 
				(Coords) new ImmutableLinearCoords(n)).iterator();
	}
}
