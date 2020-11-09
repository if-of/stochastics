package ua.karazina.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.jfree.chart3d.fx.Chart3DViewer;
import ua.karazina.chart3d.BarChart3dBuilder;
import ua.karazina.entity.Area;
import ua.karazina.entity.Point;
import ua.karazina.entity.Probability;
import ua.karazina.service.RunningState;
import ua.karazina.service.StochasticCalculator;
import ua.karazina.thread.StochasticCalculatorThread;

import java.util.Arrays;


public class Controller {

    @FXML
    private TextField countField;
    @FXML
    private TextField upProbabilityField;
    @FXML
    private TextField downProbabilityField;
    @FXML
    private TextField leftProbabilityField;
    @FXML
    private TextField rightProbabilityField;
    @FXML
    private TextField stopProbabilityField;

    @FXML
    private TextField xLengthField;
    @FXML
    private TextField yLengthField;
    @FXML
    private TextField xCoordinateField;
    @FXML
    private TextField yCoordinateField;

    @FXML
    private Label countLabel;
    @FXML
    private Label upPercentageLabel;
    @FXML
    private Label downPercentageLabel;
    @FXML
    private Label leftPercentageLabel;
    @FXML
    private Label rightPercentageLabel;
    @FXML
    private Label stopPercentageLabel;

    @FXML
    private AnchorPane chart3dAnchorPane;
    @FXML
    private LineChart leftRightChart;
    @FXML
    private LineChart upDownChart;

    @FXML
    private ProgressBar progressBar;

    private RunningState runningState = RunningState.stoppedState();
    private StochasticCalculatorThread stochasticCalculatorThread;


    @FXML
    private void onStartButtonClick(ActionEvent event) {
        if (!runningState.isStopped()) {
            return;
        }

        runningState.run();
        StochasticCalculator stochasticCalculator = new StochasticCalculator(
                getArea(),
                getStartPoint(),
                getProbability(),
                getCount(),
                progressBar::setProgress,
                this::onPause,
                this::onDone
        );
        stochasticCalculatorThread = new StochasticCalculatorThread(stochasticCalculator);
        stochasticCalculatorThread.start();
    }

    @FXML
    private void onPauseButtonClick(ActionEvent event) {
        if (runningState.isStopped()) {
            return;
        }

        if (runningState.isRunning()) {
            runningState.pause();
            stochasticCalculatorThread.interrupt();
        } else {
            runningState.run();
            StochasticCalculator stochasticCalculator = stochasticCalculatorThread.getStochasticCalculator();
            stochasticCalculatorThread = new StochasticCalculatorThread(stochasticCalculator);
            stochasticCalculatorThread.start();
        }
    }

    @FXML
    private void onStopButtonClick(ActionEvent event) {
        if (runningState.isStopped()) {
            return;
        }

        runningState.stop();
        if (stochasticCalculatorThread != null) {
            stochasticCalculatorThread.interrupt();
            stochasticCalculatorThread = null;
        }
    }


    private void onDone(StochasticCalculator stochasticCalculator) {
        Platform.runLater(() -> {
            runningState.stop();
            showResult(stochasticCalculator);
        });
    }

    private void onPause(StochasticCalculator stochasticCalculator) {
        Platform.runLater(() -> {
            runningState.pause();
            showResult(stochasticCalculator);
        });
    }


    private void showResult(StochasticCalculator stochasticCalculator) {
        upDownChart.getData().clear();
        leftRightChart.getData().clear();

        countLabel.setText("" + stochasticCalculator.getCurrentCount());
        printStatistics(stochasticCalculator.getQuiteProbability());
        draw2dChart(stochasticCalculator.getTopEnd(), "top", upDownChart);
        draw2dChart(stochasticCalculator.getBottomEnd(), "down", upDownChart);
        draw2dChart(stochasticCalculator.getLeftEnd(), "left", leftRightChart);
        draw2dChart(stochasticCalculator.getRightEnd(), "right", leftRightChart);
        drawStopValues3dChart(stochasticCalculator.getStopValues());
    }

    private void printStatistics(Probability probability) {
        upPercentageLabel.setText("" + probability.getPTop());
        downPercentageLabel.setText("" + probability.getPBottom());
        leftPercentageLabel.setText("" + probability.getPLeft());
        rightPercentageLabel.setText("" + probability.getPRight());
        stopPercentageLabel.setText("" + probability.getPStop());
    }

    private void draw2dChart(double[] values, String seriesName, LineChart lineChart) {
        normalizeArray(values);
        XYChart.Series series = new XYChart.Series();
        series.setName(seriesName);
        for (int i = 0; i < values.length; i++) {
            series.getData().add(new XYChart.Data("" + (i + 1), values[i]));
        }
        lineChart.getData().add(series);
    }

    private void normalizeArray(double[] arr) {
        double sum = Arrays.stream(arr).sum();
        if (sum == 0) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] /= sum;
        }
    }

    private void drawStopValues3dChart(double[][] stopValues) {
        Chart3DViewer chart3DViewer = BarChart3dBuilder.createChartNode(stopValues);
        chart3dAnchorPane.getChildren().clear();
        chart3dAnchorPane.getChildren().add(chart3DViewer);
        AnchorPane.setLeftAnchor(chart3DViewer, 0.0);
        AnchorPane.setTopAnchor(chart3DViewer, 0.0);
        AnchorPane.setRightAnchor(chart3DViewer, 0.0);
        AnchorPane.setBottomAnchor(chart3DViewer, 0.0);
        chart3DViewer.getCanvas().zoomToFit(chart3dAnchorPane.getWidth(), chart3dAnchorPane.getHeight());
    }


    private Point getStartPoint() {
        try {
            return new Point(
                    Integer.parseInt(xCoordinateField.getText()) - 1,
                    Integer.parseInt(yCoordinateField.getText()) - 1
            );
        } catch (Exception e) {
            throw new RuntimeException("Coordinates of start point should be integers");
        }
    }

    private Area getArea() {
        try {
            return new Area(
                    Integer.parseInt(xLengthField.getText()),
                    Integer.parseInt(yLengthField.getText())
            );
        } catch (Exception e) {
            throw new RuntimeException("Lengths of area should be integers");
        }
    }

    private Probability getProbability() {
        Probability probability;
        try {
            probability = new Probability(
                    Double.parseDouble(leftProbabilityField.getText()),
                    Double.parseDouble(upProbabilityField.getText()),
                    Double.parseDouble(rightProbabilityField.getText()),
                    Double.parseDouble(downProbabilityField.getText()),
                    Double.parseDouble(stopProbabilityField.getText())
            );
        } catch (Exception e) {
            throw new RuntimeException("Probabilities should be doubles");
        }

        return probability;
    }

    private int getCount() {
        try {
            return Integer.parseInt(countField.getText());
        } catch (Exception e) {
            throw new RuntimeException("Count should be integer");
        }
    }

}

