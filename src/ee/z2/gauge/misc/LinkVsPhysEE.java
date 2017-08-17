package ee.z2.gauge.misc;

import ee.common.DensityMatrix;
import ee.common.EntanglementEntropy;
import ee.common.SimpleDensityMatrix;
import ee.common.SubspaceMatcher;
import ee.geometry.BitMaskROI;
import ee.geometry.SequentialROI;
import ee.geometry.SequentialSpace;
import ee.z2.common.EncodedZ2Vector;
import ee.z2.common.EncodedZ2VectorMatcher;
import ee.z2.common.SpinsState;

/**
 * Compares the physical entanglement entropy for a 2-plaquette Z2 gauge field
 * with the link entanglement entropy (electric center choice) for both the full
 * space and a partially gauge-fixed case.
 * 
 * It does so by plotting the link EE vs physical EE for physical states of the
 * form &Alpha;|++&gt; + &Beta;|--&gt;, where |++&gt; and |--&gt; are
 * eigenstates of the left and right plaquette operators with eigenvalues +1 and
 * -1, respectively. The state coefficients (&Alpha; and &Beta;) are chosen
 * randomly, but they are constrained by the normalization condition
 * &Alpha;<sup>2</sup> + &Beta;<sup>2</sup> = 1.
 * 
 * The identification of the link splitting scheme to the electric center choice
 * is made based on https://arxiv.org/abs/1501.02593 and, in this case, consists
 * of a region taken to be the right plaquette, while its complement is formed
 * by the remaining links (the left plaquette without the shared link).
 * 
 * Partial gauge fixing is implemented by fixing two of the free links on each
 * plaquette to 1 (or |+&gt;) such that each plaquette has one independent link
 * as well as the shared link.
 */
public class LinkVsPhysEE {

	/**
	 * The number of samples for &Alpha; and &Beta;
	 */
	public static final int N = 200;

	private LinkVsPhysEEPlot plot;

	public LinkVsPhysEE() {
		plot = new LinkVsPhysEEPlot();
	}

	public void run() throws Exception {
		gaugeFixed();
		fullSpace();
	}

	private void gaugeFixed() {
		run(3, 0b00000110, 0b00000011, 0);
	}

	private void fullSpace() {
		run(7, 0b01111000, 0b00001111, 1);
	}

	/**
	 * Generates N random &Alpha; and &Beta; and computes the entanglement
	 * entropy for both the physical state and the electric center choice.
	 * 
	 * @param bitMaskLeft
	 * @param bitMaskRight
	 * @param plotOffset
	 */
	private void run(int nLinks, int bitMaskLeft, int bitMaskRight, int plotSeries) {
		for (int i = 0; i <= N; i++) {
			double alpha = 1.0 * i / N;
			double beta = Math.sqrt(1 - alpha * alpha);

			double[] ee = computeEE(nLinks, bitMaskLeft, bitMaskRight, alpha, beta);
			/*
			 * ee[0] is the physical EE
			 */
			plot.addPoint(alpha, ee[0], ee[1], plotSeries);
		}
	}

	private double[] computeEE(int nLinks, int bitMaskLeft, int bitMaskRight, double alpha, double beta) {
		System.out.println(alpha);
		SpinsState s0 = new SpinsState(2, beta, 0, 0, alpha);
		DensityMatrix<EncodedZ2Vector> rho0 = new SimpleDensityMatrix<>(s0);
		System.out.println(rho0);
		SubspaceMatcher<EncodedZ2Vector> m0 = new EncodedZ2VectorMatcher(new SequentialSpace(2),
				new SequentialROI(0, 0));
		EntanglementEntropy<EncodedZ2Vector> ee0 = new EntanglementEntropy<>(rho0, m0);

		double e0 = ee0.getValue();
		System.out.println("EEA: " + e0);

		final int nVectors = 1 << nLinks;

		double[] coeffs = new double[nVectors];

		// Generate all integers between 0 and nVectors.
		// This gives all the possible bit combinations
		// of nLinks bits. Then test if the configuration
		// corresponds to a |++> or a |--> physical state
		// and set the coefficient accordingly. We only care
		// about the overall ratio of coefficients, since they
		// get normalized by SpinsState
		for (int i = 0; i < nVectors; i++) {
			int pL = plaq(nLinks, i, bitMaskLeft);
			int pR = plaq(nLinks, i, bitMaskRight);

			double coeff;

			if (pL == 1 && pR == 1) {
				coeff = alpha;
			}
			else if (pL == -1 && pR == -1) {
				coeff = beta;
			}
			else {
				coeff = 0;
			}

			coeffs[i] = coeff;
			if (coeff != 0) {
				System.out.print(pad(nLinks, Integer.toString(i, 2)));
				System.out.println(" -> " + coeff);
			}
		}

		SpinsState s1 = new SpinsState(nLinks, coeffs);
		DensityMatrix<EncodedZ2Vector> rho = new SimpleDensityMatrix<>(s1);

		SubspaceMatcher<EncodedZ2Vector> m = new EncodedZ2VectorMatcher(new SequentialSpace(nLinks), new BitMaskROI(
				nLinks, bitMaskRight));
		EntanglementEntropy<EncodedZ2Vector> ee1 = new EntanglementEntropy<>(rho, m);
		double e1 = ee1.getValue();
		System.out.println(pad(nLinks, Integer.toString(bitMaskRight, 2)) + " -> " + e0 + " <-> " + e1);

		return new double[] { e0, e1 };
	}

	private String pad(int n, String s) {
		while (s.length() < n) {
			s = "0" + s;
		}
		return s;
	}

	private int plaq(int n, int v, int mask) {
		int p = 1;
		// only look at the bits indicated by mask
		v = v & mask;
		// this calculates (-1)^b, where b is the
		// number of bits that are 1 (representing a link l = -1)
		for (int i = 0; i < n; i++) {
			if ((v & 1) == 1) {
				p = -p;
			}
			v >>= 1;
		}
		return p;
	}

	public static void main(String[] args) {
		try {
			new LinkVsPhysEE().run();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
