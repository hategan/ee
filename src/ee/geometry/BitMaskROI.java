package ee.geometry;

import ee.common.RegionOfInterest;

public class BitMaskROI implements RegionOfInterest {
	private final int n, mask;
	
	public BitMaskROI(int n, int mask) {
		this.n = n;
		this.mask = mask;
	}

	@Override
	public boolean isInside(Coords c) {
		LinearCoords lc = (LinearCoords) c;
		return ((1 << lc.index()) & mask) != 0;
	}
}
