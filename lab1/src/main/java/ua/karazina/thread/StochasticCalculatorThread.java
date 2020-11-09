package ua.karazina.thread;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ua.karazina.service.StochasticCalculator;

@RequiredArgsConstructor
public class StochasticCalculatorThread extends Thread {

    @Getter
    private final StochasticCalculator stochasticCalculator;

    @Override
    public void run() {
        stochasticCalculator.start();
    }
}
