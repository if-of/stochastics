package ua.karazina.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ua.karazina.service.GeometricMonteCarloIntegrator;
import ua.karazina.service.MyFunction;
import ua.karazina.service.RealIntegrator;
import ua.karazina.service.RectangleIntegrator;
import ua.karazina.service.SimpleMonteCarloIntegrator;

import static java.lang.Math.abs;


public class Controller {

    private static final String RESULT_TEMPLATE = ""
            + "Analitic:         S=%.6f\n"
            + "Rectangle method: S=%.6f :: error=%.6f :: sigma=%.6f\n"
            + "Simple M-K:       S=%.6f :: error=%.6f :: sigma=%.6f\n"
            + "Geometric M-K:    S=%.6f :: error=%.6f :: sigma=%.6f\n";

    @FXML
    private TextField aParameterField;
    @FXML
    private TextField uParameterField;
    @FXML
    private TextField kParameterField;
    @FXML
    private TextField methodRectangleNField;
    @FXML
    private TextField methodMKNField;

    @FXML
    private TextArea resultTextArea;

    @FXML
    private void onCalculateButtonClick(ActionEvent event) {
        try {
            double aParameter = Double.parseDouble(aParameterField.getText());
            double bParameter = Double.parseDouble(uParameterField.getText());
            double cParameter = Double.parseDouble(kParameterField.getText());
            int methodRectangleN = Integer.parseInt(methodRectangleNField.getText());
            int methodMKN = Integer.parseInt(methodMKNField.getText());

            RealIntegrator realIntegrator = new RealIntegrator(aParameter, bParameter, cParameter);
            double realIntegratorResult = realIntegrator.integrate();

            MyFunction myFunction = new MyFunction(aParameter, bParameter, cParameter);
            RectangleIntegrator rectangleIntegrator = new RectangleIntegrator(myFunction);
            double rectangleIntegratorResult = rectangleIntegrator.integrate(methodRectangleN);
            double rectangleIntegratorError = abs(realIntegratorResult - rectangleIntegratorResult);
            double rectangleIntegratorSigma = rectangleIntegrator.calculateSigma();

            SimpleMonteCarloIntegrator simpleMonteCarloIntegrator = new SimpleMonteCarloIntegrator(myFunction);
            double simpleMonteCarloResult = simpleMonteCarloIntegrator.integrate(methodMKN);
            double simpleMonteCarloError = abs(realIntegratorResult - simpleMonteCarloResult);
            double simpleMonteCarloSigma = simpleMonteCarloIntegrator.calculateSigma();

            GeometricMonteCarloIntegrator geometricMonteCarloIntegrator = new GeometricMonteCarloIntegrator(myFunction);
            double geometricMonteCarloResult = geometricMonteCarloIntegrator.integrate(300, methodMKN);
            double geometricMonteCarloError = abs(realIntegratorResult - geometricMonteCarloResult);
            double geometricMonteCarloSigma = geometricMonteCarloIntegrator.calculateSigma();


            resultTextArea.setText(
                    String.format(RESULT_TEMPLATE, realIntegratorResult,
                            rectangleIntegratorResult, rectangleIntegratorError, rectangleIntegratorSigma,
                            simpleMonteCarloResult, simpleMonteCarloError, simpleMonteCarloSigma,
                            geometricMonteCarloResult, geometricMonteCarloError, geometricMonteCarloSigma)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

