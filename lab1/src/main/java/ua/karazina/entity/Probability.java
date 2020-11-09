package ua.karazina.entity;

import lombok.Data;

@Data
public class Probability {
    private final double pLeft;
    private final double pTop;
    private final double pRight;
    private final double pBottom;
    private final double pStop;
}
