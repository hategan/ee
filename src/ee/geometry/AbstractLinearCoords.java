package ee.geometry;

public abstract class AbstractLinearCoords implements LinearCoords {
	protected final Geometry geom;
	
	protected AbstractLinearCoords(Geometry geom) {
		this.geom = geom;
	}

	@Override
	public int clamp(int dir, int n) {
		int ix = this.index();
		int m = geom.getMultiplier(dir);
		int ix2;
		int delta = m * n;
		int limit;
		if (dir == geom.getNDim() - 1) {
			limit = geom.getTotalSites();
			ix2 = ix;
		}
		else {
			limit = geom.getMultiplier(dir + 1);
			ix2 = ix % limit;
		}
		int iy = ix2 + delta;
		if (iy >= limit) {
			return -(ix + delta - limit);
		}
		if (iy < 0) {
			return -(ix + delta + limit);
		}
		else {
			return ix + delta;
		}
	}

	@Override
	public int getCoordinate(int dir) {
		int ix = index();
		
		int m = geom.getMultiplier(dir);
		int c = ix / m;
		
		if (dir == geom.getNDim() - 1) {
			return c;
		}
		else {
			return c % geom.getMultiplier(dir + 1);
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		int ndd = geom.getNDim() - 1;
		int v = index();
		for (int d = 0; d < ndd; d++) {
			int c = v % geom.getMultiplier(d + 1);
			v = v - c;
			c = c / geom.getMultiplier(d);
			sb.append(c);
			sb.append(", ");
		}
		sb.append(v / geom.getMultiplier(ndd));
		sb.append(")");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		MutableLinearCoords c = new MutableLinearCoords(new Geometry(64, 16, 16));
		c.setCoords(0, 15, 15);
		System.out.println(c);
		int ix = c.clamp(1, 1);
		c.set(ix);
		System.out.println(c);
	}

	@Override
	public int hashCode() {
		return index();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LinearCoords) {
			LinearCoords l = (LinearCoords) obj;
			return l.index() == index();
		}
		else {
			return false;
		}
	}
}
