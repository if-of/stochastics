package ua.karazina.service;

import lombok.Getter;
import ua.karazina.entity.Area;
import ua.karazina.entity.Point;
import ua.karazina.entity.Probability;

import java.util.function.Consumer;

public class StochasticCalculator {

    private int xLength;
    private int yLength;
    private int xPosition;
    private int yPosition;

    private double pLeft;
    private double pTop;
    private double pRight;
    private double pBottom;
    private double pStop;

    private int count;
    @Getter
    private int currentCount;

    private int updateInterval;
    private int nextUpdateValue;
    private Consumer<Double> progressUpdater;
    private Consumer<StochasticCalculator> doneConsumer;
    private Consumer<StochasticCalculator> pauseConsumer;


    private int qLeft = 0;
    private int qTop = 0;
    private int qRight = 0;
    private int qBottom = 0;
    private int qStop = 0;

    private int leftMove = 0;
    private int topMove = 0;
    private int rightMove = 0;
    private int bottomMove = 0;
    private int stopMove = 0;

    @Getter
    private double[] leftEnd;
    @Getter
    private double[] rightEnd;
    @Getter
    private double[] topEnd;
    @Getter
    private double[] bottomEnd;
    @Getter
    private double[][] stopValues;


    public StochasticCalculator(Area area, Point startPoint, Probability probability, int count,
                                Consumer<Double> progressUpdater, Consumer<StochasticCalculator> pauseConsumer,
                                Consumer<StochasticCalculator> doneConsumer) {
        xLength = area.getXLength();
        yLength = area.getYLength();
        xPosition = startPoint.getX();
        yPosition = startPoint.getY();

        pLeft = probability.getPLeft();
        pTop = probability.getPTop();
        pRight = probability.getPRight();
        pBottom = probability.getPBottom();
        pStop = probability.getPStop();

        this.count = count;
        updateInterval = count / 100;
        nextUpdateValue = updateInterval;
        this.progressUpdater = progressUpdater;
        this.doneConsumer = doneConsumer;
        this.pauseConsumer = pauseConsumer;

        leftEnd = new double[yLength];
        rightEnd = new double[yLength];
        topEnd = new double[xLength];
        bottomEnd = new double[xLength];
        stopValues = new double[xLength][yLength];

        currentCount = 1;
    }

    public Probability getQuiteProbability() {
        return new Probability(
                (double) qLeft / currentCount,
                (double) qTop / currentCount,
                (double) qRight / currentCount,
                (double) qBottom / currentCount,
                (double) qStop / currentCount
        );
    }

    public Probability getMoveProbability() {
        return new Probability(
                (double) leftMove / currentCount,
                (double) topMove / currentCount,
                (double) rightMove / currentCount,
                (double) bottomMove / currentCount,
                (double) stopMove / currentCount
        );
    }

    public boolean isDone() {
        return currentCount >= count;
    }

    public void start() {
        for (; currentCount <= count; currentCount++) {
            int xCurrent = xPosition;
            int yCurrent = yPosition;
            boolean stop = false;

            while (true) {
                double g = Math.random();

                if (g < pLeft) {
                    xCurrent--;
                    leftMove++;
                } else if (g < (pLeft + pTop)) {
                    yCurrent++;
                    topMove++;
                } else if (g < (pLeft + pTop + pRight)) {
                    xCurrent++;
                    rightMove++;
                } else if (g < (pLeft + pTop + pRight + pBottom)) {
                    yCurrent--;
                    bottomMove++;
                } else {
                    stop = true;
                    stopMove++;
                }

                if (stop) {
                    qStop++;
                    stopValues[xCurrent][yCurrent]++;
                    break;
                } else if (yCurrent >= yLength) {
                    qTop++;
                    topEnd[xCurrent]++;
                    break;
                } else if (yCurrent < 0) {
                    qBottom++;
                    bottomEnd[xCurrent]++;
                    break;
                } else if (xCurrent >= xLength) {
                    qRight++;
                    rightEnd[yCurrent]++;
                    break;
                } else if (xCurrent < 0) {
                    qLeft++;
                    leftEnd[yCurrent]++;
                    break;
                }
            }

            if (currentCount > nextUpdateValue) {
                nextUpdateValue += updateInterval;
                progressUpdater.accept((double) currentCount / count);
            }
            if (Thread.currentThread().isInterrupted()) {
                pauseConsumer.accept(this);
                return;
            }
        }

        progressUpdater.accept(1.0);
        currentCount = count;
        doneConsumer.accept(this);
    }
}
