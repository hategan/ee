package ee.z2.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ee.common.RegionOfInterest;
import ee.common.SubspaceMatcher;
import ee.geometry.Coords;

public class EncodedZ2VectorMatcher implements SubspaceMatcher<EncodedZ2Vector> {
	private EncodedZ2VectorMask mask;
	
	public EncodedZ2VectorMatcher(Iterable<? extends Coords> slice, RegionOfInterest roi) {
		init(slice.iterator(), roi);
	}
	
	public EncodedZ2VectorMatcher(Iterator<? extends Coords> slice, RegionOfInterest roi) {
		init(slice, roi);
	}
	
	private void init(Iterator<? extends Coords> slice, RegionOfInterest roi) {
		List<Boolean> l = new ArrayList<Boolean>();
		while (slice.hasNext()) {
			if (roi.isInside(slice.next())) {
				l.add(true);
			}
			else {
				l.add(false);
			}
		}
		mask = new EncodedZ2VectorMask(l.size());
		for (Boolean b : l) {
			if (b) {
				mask.push(1);
			}
			else {
				mask.push(0);
			}
		}
	}

	@Override
	public EncodedZ2Vector reduce(EncodedZ2Vector x) {
		return mask.reduce(x);
	}

	@Override
	public EncodedZ2Vector complement(EncodedZ2Vector x) {
		return mask.complement(x);
	}
}
