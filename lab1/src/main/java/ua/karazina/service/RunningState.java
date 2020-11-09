package ua.karazina.service;

import lombok.Getter;

@Getter
public class RunningState {

    private boolean isRunning = false;
    private boolean isPaused = false;
    private boolean isStopped = true;

    public static RunningState stoppedState() {
        return new RunningState();
    }

    private RunningState() {
    }

    public void run() {
        isRunning = true;
        isPaused = false;
        isStopped = false;
    }

    public void pause() {
        isRunning = false;
        isPaused = true;
        isStopped = false;
    }

    public void stop() {
        isRunning = false;
        isPaused = false;
        isStopped = true;
    }
}
