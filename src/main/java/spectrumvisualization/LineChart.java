package spectrumvisualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.quifft.output.FrequencyBin;

import javax.swing.*;
import java.text.DecimalFormat;

class LineChart extends JFrame {

    private XYSeriesCollection data = new XYSeriesCollection();

    JFreeChart chart;

    LineChart() {
        super("Frequency Spectrum");

        // Create empty series
        data.addSeries(new XYSeries("f", true, false));

        // Create chart
        chart = ChartFactory.createXYLineChart("", "Frequency", "Amplitude",
                data, PlotOrientation.VERTICAL, false, true, false);

        // Create panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    void updateChartData(FrequencyBin[] newBins, long timestamp) {
        chart.setTitle(getMMSSTimestamp(timestamp));

        XYSeries series = data.getSeries("f");
        for(FrequencyBin bin : newBins) {
            series.addOrUpdate(bin.frequency, bin.amplitude);
        }
    }

    private static String getMMSSTimestamp(double seconds) {
        int min = (int) Math.floor(seconds / 60);
        double s = seconds % 60;

        DecimalFormat df = new DecimalFormat("#.00");
        StringBuilder secsBuilder = new StringBuilder();
        if(s < 1) secsBuilder.append("0");
        if(s < 10) secsBuilder.append("0");
        secsBuilder.append(df.format(s));
        return String.format("%d:%s", min, secsBuilder);
    }
}