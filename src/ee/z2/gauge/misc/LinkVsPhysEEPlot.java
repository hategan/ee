package ee.z2.gauge.misc;

import java.awt.Color;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class LinkVsPhysEEPlot extends ApplicationFrame {
	public static final Color COLORS[] = new Color[] {
		new Color(Integer.parseInt("1b9e77", 16)),
		new Color(Integer.parseInt("d95f02", 16)),
		new Color(Integer.parseInt("7570b3", 16)),
		new Color(Integer.parseInt("e7298a", 16))
	};
	
	private XYSeries s1, s2, s3, s4;
	
	public LinkVsPhysEEPlot() {
		super("Link EE and Physical EE for a|--> + b|++> states for a 2-plaquette "
				+ "Z2 gauge lattice");
		JFreeChart chart = makeChart();
		ChartPanel panel = new ChartPanel(chart, true, true, true, false, true);
        panel.setPreferredSize(new java.awt.Dimension(600, 800));
        setContentPane(panel);
        pack();
        setVisible(true);
	}
	
	private JFreeChart makeChart() {
		s1 = new XYSeries("Link EE; Partial gauge-fixing");
		s2 = new XYSeries("Physical EE; Partial gauge-fixing");
		s3 = new XYSeries("Link EE; No gauge-fixing");
		s4 = new XYSeries("Physical EE; No gauge-fixing");
		
		XYSeriesCollection c1 = new XYSeriesCollection();
		c1.addSeries(s1);
		XYSeriesCollection c2 = new XYSeriesCollection();
		c2.addSeries(s2);
		XYSeriesCollection c3 = new XYSeriesCollection();
		c3.addSeries(s3);
		XYSeriesCollection c4 = new XYSeriesCollection();
		c4.addSeries(s4);
		
		NumberAxis xAxis = new NumberAxis("a");
		NumberAxis yAxis1 = new NumberAxis("Link EE");
		NumberAxis yAxis2 = new NumberAxis("Phys. EE");
		xAxis.setRange(0, 1);
		yAxis1.setRange(0, 1.5);
		yAxis2.setRange(0, 1.5);
		
		CombinedDomainXYPlot pl = new CombinedDomainXYPlot(xAxis);
		pl.add(makePlot(c1, xAxis, yAxis2, COLORS[0]), 1);
		pl.add(makePlot(c2, xAxis, yAxis1, COLORS[1]), 1);
		pl.add(makePlot(c3, xAxis, yAxis2, COLORS[2]), 1);
		pl.add(makePlot(c4, xAxis, yAxis1, COLORS[3]), 1);
		pl.setGap(20);
		
		return new JFreeChart(this.getTitle(), JFreeChart.DEFAULT_TITLE_FONT, pl, true);
	}
	

	private XYPlot makePlot(XYSeriesCollection col, NumberAxis xAxis, NumberAxis yAxis, Color color) {
		XYDotRenderer r = new XYDotRenderer();
		r.setDotWidth(3);
		r.setDotHeight(3);
		r.setSeriesPaint(0, color);
		XYPlot pl = new XYPlot(col, xAxis, yAxis, r);
		return pl;
	}

	public void addPoint(double a, double eep, double eel, int series) {
		if (series == 0) {
			s1.add(a, eep);
			s2.add(a, eel);
		}
		else {
			s3.add(a, eep);
			s4.add(a, eel);
		}
	}
}
