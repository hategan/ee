package ee.geometry;

import ee.common.RegionOfInterest;

public class SequentialROI implements RegionOfInterest {
	private final int start, end;
	
	public SequentialROI(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public boolean isInside(Coords c) {
		LinearCoords lc = (LinearCoords) c;
		return lc.index() >= start && lc.index() <= end;
	}

}
