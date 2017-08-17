package ee.geometry;

public class Geometry {
	private int ndim;
	
	private int[] szi;
	
	private int[] multipliers;
	
	private int nsites;
	
	public Geometry(int... n) {
		ndim = n.length;
		
		nsites = 1;
		szi = new int[n.length];
		multipliers = new int[n.length];
		for (int i = 0; i < n.length; i++) {
			szi[i] = n[i];
			multipliers[i] = nsites;
			nsites *= n[i];
		}
	}
	
	public int getNDim() {
		return ndim;
	}
	
	public int getSize(int idim) {
		return szi[idim];
	}
	
	public int getTotalSites() {
		return nsites;
	}

	public int[] getSizes() {
		return szi;
	}
	
	public int getMultiplier(int dir) {
		return multipliers[dir];
	}
}
