package spectrumvisualization;

import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.quifft.output.FrequencyBin;

import javax.swing.*;
import java.awt.Color;

class FFTGrapher {

    private LineChart chart;

    void initializeGraph() {
        // Initialize chart
        chart = new LineChart();
        chart.setSize(1280, 720);
        chart.setLocationRelativeTo(null);
        chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chart.setVisible(true);

        // Set chart color and axis scales
        XYPlot plot = (XYPlot) chart.chart.getPlot();

        plot.setBackgroundPaint(Color.WHITE);
        plot.getRenderer().setSeriesPaint(0, new Color(0, 128, 0));

        NumberAxis domainAxis = new LogarithmicAxis("Frequency (Hz)");
        domainAxis.setRange(5, 22000);
        plot.setDomainAxis(domainAxis);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(-80, 0);
    }

    void updateFFTData(FrequencyBin[] newBins, long timestamp) {
        chart.updateChartData(newBins, timestamp);
    }
}
