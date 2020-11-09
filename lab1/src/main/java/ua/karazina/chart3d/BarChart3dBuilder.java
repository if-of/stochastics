package ua.karazina.chart3d;

import org.jfree.chart3d.Chart3D;
import org.jfree.chart3d.Chart3DFactory;
import org.jfree.chart3d.Colors;
import org.jfree.chart3d.data.xyz.XYZDataset;
import org.jfree.chart3d.data.xyz.XYZSeries;
import org.jfree.chart3d.data.xyz.XYZSeriesCollection;
import org.jfree.chart3d.fx.Chart3DViewer;
import org.jfree.chart3d.graphics3d.Dimension3D;
import org.jfree.chart3d.graphics3d.ViewPoint3D;
import org.jfree.chart3d.legend.LegendAnchor;
import org.jfree.chart3d.plot.XYZPlot;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BarChart3dBuilder {

    public static Chart3DViewer createChartNode(double[][] data) {
        XYZDataset dataset = createDataset(data);
        Chart3D chart = createChart(dataset, data.length, 3, data[0].length);
        return new Chart3DViewer(chart);
    }

    private static XYZDataset<Integer> createDataset(double[][] data) {
        long sum = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                sum += data[i][j];
            }
        }

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] /= sum;
            }
        }

        List<XYZSeries<Integer>> seriesList = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            XYZSeries<Integer> series = new XYZSeries<>(i + 1);
            for (int j = 0; j < data[i].length; j++) {
                series.add(i + 1, data[i][j], j + 1);
            }
            seriesList.add(series);
        }

        XYZSeriesCollection<Integer> dataset = new XYZSeriesCollection<>();
        seriesList.forEach(dataset::add);
        return dataset;
    }

    private static Chart3D createChart(XYZDataset dataset, int width, int height, int depth) {
        Chart3D chart = Chart3DFactory.createXYZBarChart(
                null,
                null,
                dataset,
                "X",
                null,
                "Y"
        );

        ViewPoint3D viewPoint3D = ViewPoint3D.createAboveRightViewPoint(40);
        viewPoint3D.panLeftRight(Math.PI / 30);

        chart.setChartBoxColor(new Color(255, 255, 255, 127));
        chart.setLegendAnchor(LegendAnchor.BOTTOM_RIGHT);
        chart.setViewPoint(viewPoint3D);
        chart.setLegendBuilder(null);

        XYZPlot plot = (XYZPlot) chart.getPlot();
        plot.getRenderer().setColors(Colors.createShadesColors());
        plot.setDimensions(new Dimension3D(width, height, depth));

        return chart;
    }
}
