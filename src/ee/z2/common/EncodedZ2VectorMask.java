package ee.z2.common;

public class EncodedZ2VectorMask extends EncodedZ2Vector {

	public EncodedZ2VectorMask(int len) {
		super(len);
	}

	public EncodedZ2Vector reduce(EncodedZ2Vector x) {
		check();
		EncodedZ2Vector r = EncodedZ2Vector.empty(this.size());
		r.a.or(x.a);
		r.a.or(a);
		return r;
	}


	public EncodedZ2Vector complement(EncodedZ2Vector x) {
		check();
		EncodedZ2Vector r = EncodedZ2Vector.empty(this.size());
		r.a.or(x.a);
		r.a.and(a);
		return r;
	}
}
