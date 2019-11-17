import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

public class RTXYChart extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1940395395807846995L;
	
	private JFreeChart chart;
	private ChartPanel chartPanel;
    public JLabel device_label;
    public JLabel lens_label;
    public JLabel hr_label;
    
    public XYSeries series1;		// dataset for all samples
    public XYSeries series2;		// dataset for raw peaks
    public XYSeries series3; 		// dataset for valid peaks
    public XYSeries series4;		// dataset for raw zero crossings
    public XYSeries series5;		// dataset for valid zero crossings
    public XYSeries series6;		// dataset for heart beat
    public XYSeries series7; 		// dataset for individual heart beat
    
    public Timer timer;
    public double maximum;
    public double minimum;
    
    private NumberAxis domainAxis;
	private ValueAxis rangeAxis;
	private XYPlot plot1;
	private String chart_title;
	private JPanel panel;
    
    public RTXYChart(String title){
    	super(title);
    	chart_title = title;
    	
    	maximum = 1000;
    	minimum = -1000;
    	
    	series1 = new XYSeries("Samples");
    	series2 = new XYSeries("Raw Peaks");
    	series3 = new XYSeries("Valid Peaks");
    	series4 = new XYSeries("Raw Zero Crossings");
    	series5 = new XYSeries("Valid Zero Crossings");
    	series6 = new XYSeries("Heart Beat");
    	series7 = new XYSeries("Raw Heart Beat");
    	
    	XYSeriesCollection dataset [] = new XYSeriesCollection[7];
    	dataset[0] = new XYSeriesCollection(series1);
    	dataset[1] = new XYSeriesCollection(series2);
    	dataset[2] = new XYSeriesCollection(series3);
    	dataset[3] = new XYSeriesCollection(series4);
    	dataset[4] = new XYSeriesCollection(series5);
    	dataset[5] = new XYSeriesCollection(series6);
    	dataset[6] = new XYSeriesCollection(series7);
    	
    	createPanel( title, dataset);
        
        // Add a timer to drive the refresh condition
    	@SuppressWarnings("serial")
		Action action = new AbstractAction() {
    		public void actionPerformed(ActionEvent e) {
    			if (Math.abs(minimum) >= Math.abs(maximum)) {
    				double bound = returnMaxValue(-minimum);
    				rangeAxis.setRange(-bound - 200, bound + 200);
    			} else {
    				double bound = returnMaxValue(maximum);
    				rangeAxis.setRange(-bound - 200, bound + 200);
    			}
    		}
    	};   
        
        timer = new Timer(2000, action);
        timer.start();
    }
    
    private double returnMaxValue (double d) {
    	if ( d < 5000 && d > 400 ) {
    		return d;
    	} else {
    		if ( d >= 5000) {
    			return 5000;
    		} else {
    			return 400;
    		}
    	}
    }
    
    private void createPanel ( String title,  XYDataset[] dataset ) {
    	
        chart = createRTChart( title, dataset );
        
        // Note: must enable using buffer. Otherwise, only two points are drawn. 
        chartPanel = new ChartPanel(chart, true);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 540));
        
        Border border = BorderFactory.createCompoundBorder(
        		BorderFactory.createEmptyBorder(4, 4, 4, 4),
        		BorderFactory.createEtchedBorder()		
        );
        chartPanel.setBorder(border);
        
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);
        
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridBagLayout());
        labelPanel.setPreferredSize(new Dimension(300, 150));
        
		String html1 = "<html><body style='width: ";
        String html2 = "px'>";
        device_label = new JLabel(html1 + "250" + html2 + "Not collected to the LivePulse application.", JLabel.CENTER);
        device_label.setForeground(Color.red);
        device_label.setFont(new Font("Arial", Font.BOLD, 25));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 0.25;
        c.gridx = 0;
        c.gridy = 0;
        labelPanel.add(device_label, c);
        lens_label = new JLabel(html1 + "250" + html2 + "No message received", JLabel.CENTER);
        lens_label.setForeground(Color.red);
        lens_label.setFont(new Font("Arial", Font.BOLD, 25));
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 0.25;
        c.gridx = 0;
        c.gridy = 1;
        labelPanel.add(lens_label, c);
        hr_label = new JLabel("  0", JLabel.RIGHT);
        hr_label.setFont(new Font("Arial", Font.BOLD, 80));
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 2;
        labelPanel.add(hr_label, c);
        labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(labelPanel, BorderLayout.EAST);
        
        setContentPane(panel);
    }
    
    public void reset() {
    	series1 = new XYSeries("Samples");
    	series2 = new XYSeries("Raw Peaks");
    	series3 = new XYSeries("Valid Peaks");
    	series4 = new XYSeries("Raw Zero Crossings");
    	series5 = new XYSeries("Valid Zero Crossings");
    	series6 = new XYSeries("Heart Beat");
    	series7 = new XYSeries("Raw Heart Beat");
    	
    	XYSeriesCollection dataset [] = new XYSeriesCollection[7];
    	dataset[0] = new XYSeriesCollection(series1);
    	dataset[1] = new XYSeriesCollection(series2);
    	dataset[2] = new XYSeriesCollection(series3);
    	dataset[3] = new XYSeriesCollection(series4);
    	dataset[4] = new XYSeriesCollection(series5);
    	dataset[5] = new XYSeriesCollection(series6);
    	dataset[6] = new XYSeriesCollection(series7);
    	
    	panel.removeAll();
        panel.revalidate();
        createPanel(chart_title, dataset);
    }
    
    private JFreeChart createRTChart(String t, XYDataset[] dataset ) {
    	
    	// Create a single plot containing both the scatter and line
		plot1 = new XYPlot();
		
		domainAxis = new NumberAxis();
		
		domainAxis.setAutoRange(true);
		domainAxis.setFixedAutoRange(5000);
		domainAxis.setLabelFont(new Font("Sans-serif", Font.PLAIN, 20));
		
		rangeAxis = new NumberAxis();
		rangeAxis.setLabelFont(new Font("Arial", Font.BOLD, 20));
		rangeAxis.setRange(-2000, 2000);
		
		plot1.setDomainAxis(0, domainAxis);
		plot1.setRangeAxis(0, rangeAxis);
		
		plot1.setDomainCrosshairVisible(true); 
        plot1.setRangeCrosshairVisible(true); 
		
		// Plot valid zero crossings
		XYItemRenderer renderer1 = new XYLineAndShapeRenderer(false, true);
		renderer1.setSeriesShape(0, ShapeUtilities.createDiagonalCross(3, 1));
		renderer1.setSeriesPaint(0, Color.cyan);
		renderer1.setBaseItemLabelsVisible(true);
		
		plot1.setDataset(0, dataset[4]);
		plot1.setRenderer(0, renderer1);
		
		plot1.mapDatasetToDomainAxis(0, 0);
		plot1.mapDatasetToRangeAxis(0, 0);
		
		// Plot raw zero crossings
		XYItemRenderer renderer2 = new XYLineAndShapeRenderer(false, false);
		renderer2.setSeriesShape(0, ShapeUtilities.createDiagonalCross(3, 1));
		renderer2.setSeriesPaint(0, Color.magenta);
		renderer2.setBaseItemLabelsVisible(false);
		
		plot1.setDataset(1, dataset[3]);
		plot1.setRenderer(1, renderer2);
		
		plot1.mapDatasetToDomainAxis(1, 0);
		plot1.mapDatasetToRangeAxis(1, 0);
		
		// Plot valid peaks
		XYItemRenderer renderer3 = new XYLineAndShapeRenderer(false, true);
		renderer3.setSeriesShape(0, new Ellipse2D.Double(-2.5,-2.5,5,5));
		renderer3.setSeriesPaint(0, Color.blue);
		renderer3.setBaseItemLabelsVisible(true);
		
		plot1.setDataset(2, dataset[2]);
		plot1.setRenderer(2, renderer3);
		
		plot1.mapDatasetToDomainAxis(2, 0);
		plot1.mapDatasetToRangeAxis(2, 0);
		
		// Plot raw peaks
		XYItemRenderer renderer4 = new XYLineAndShapeRenderer(false, false);
		renderer4.setSeriesShape(0, new Ellipse2D.Double(-2.5,-2.5,5,5));
		renderer4.setSeriesPaint(0, Color.green);
		
		plot1.setDataset(3, dataset[1] );
		plot1.setRenderer(3, renderer4 );
		
		plot1.mapDatasetToDomainAxis(3, 0);
		plot1.mapDatasetToRangeAxis(3, 0);

		// Plot samples
		XYItemRenderer renderer5 = new XYLineAndShapeRenderer(true, false);   // Shapes only
		renderer5.setSeriesShape(0, new Ellipse2D.Double(-1,-1,2,2));
		renderer5.setSeriesPaint(0, Color.red);
		renderer5.setSeriesStroke(0, new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		
		plot1.setDomainCrosshairVisible(true); 
		plot1.setRangeCrosshairVisible(true);
//		plot1.setBackgroundPaint(new Color(0, 51, 0));
		
		plot1.setDataset(4, dataset[0]);
		plot1.setRenderer(4, renderer5);
		
		plot1.mapDatasetToDomainAxis(4, 0);
		plot1.mapDatasetToRangeAxis(4, 0);
		
		XYPlot plot2 = new XYPlot();
		NumberAxis rangeAxis2 = new NumberAxis("Heart Rate");
		rangeAxis2.setLabelFont(new Font("Sans-serif", Font.PLAIN, 20));
		rangeAxis2.setRange(50, 120);
		rangeAxis2.setTickUnit(new NumberTickUnit(10));
		plot2.setDomainAxis(0, domainAxis);
		plot2.setRangeAxis(0, rangeAxis2);
		
		XYItemRenderer renderer6 = new XYLineAndShapeRenderer(false, true);
		renderer6.setSeriesPaint(0, Color.black);
		renderer6.setBaseItemLabelsVisible(true);
		renderer6.setSeriesShape(0, new Ellipse2D.Double(-4,-4,8,8));
		plot2.setDataset(1, dataset[5]);
		plot2.setRenderer(1, renderer6);
		
		plot2.mapDatasetToDomainAxis(1, 0);
		plot2.mapDatasetToRangeAxis(1, 0);
		
		XYItemRenderer renderer7 = new XYLineAndShapeRenderer(false, true);
		renderer7.setSeriesPaint(0, Color.green);
		renderer6.setBaseItemLabelsVisible(true);
		renderer7.setSeriesShape(0, new Ellipse2D.Double(-4,-4,8,8));
		plot2.setDataset(0, dataset[6]);
		plot2.setRenderer(0, renderer7);
		
		plot2.mapDatasetToDomainAxis(0, 0);
		plot2.mapDatasetToRangeAxis(0, 0);
		
		CombinedDomainXYPlot plot = new CombinedDomainXYPlot(domainAxis);
        plot.add(plot1, 8);
        plot.setGap(10.0);
        plot.add(plot2, 2);
        plot.setOrientation(PlotOrientation.VERTICAL);
		
		JFreeChart chart = new JFreeChart(t, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
		chart.removeLegend();
        
        return chart;
    }
}