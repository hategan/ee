package ee.common;

import ee.geometry.Coords;

public interface RegionOfInterest {
	boolean isInside(Coords t);
}
