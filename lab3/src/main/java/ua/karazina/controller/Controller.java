package ua.karazina.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;


public class Controller {

    @FXML
    private TextField angleOfIncidenceField;
    @FXML
    private TextField layerThicknessField;
    @FXML
    private TextField statisticsField;

    @FXML
    private TextField meanFreePathField;
    @FXML
    private TextField absorptionProbField;
    @FXML
    private TextField scatterParameterField;

    @FXML
    private TextField transmissionRatioField;
    @FXML
    private TextField reflectionCoeffField;
    @FXML
    private TextField absorptionCoeffField;

    @FXML
    private TextField uncertaintyZeroField;
    @FXML
    private TextField uncertaintyOneField;
    @FXML
    private TextField uncertaintySecondField;

    double x0 = 0;
    double tet0 = 0;
    double cos0 = 1.0;

    double h = 2;
    double mu = 1;
    double kr = 1;
    double pa = 0.001;

    double Ns = 10000;

    double F = 0;
    double B = 0;
    double Ab = 0;

    @FXML
    private void onStartButtonClick(ActionEvent event) {
        tet0 = parseDoubleOrShowErrorMessage(angleOfIncidenceField.getText(), "angleOfIncidenceField");
        cos0 = Math.cos(3.1415927 * tet0 / 180);
        h = parseDoubleOrShowErrorMessage(layerThicknessField.getText(), "layerThicknessField");
        mu = parseDoubleOrShowErrorMessage(meanFreePathField.getText(), "meanFreePathField");
        if (mu > 0) mu = 1 / mu;
        else {
            mu = 1;
            meanFreePathField.setText(String.valueOf(mu));
        }
        pa = parseDoubleOrShowErrorMessage(absorptionProbField.getText(), "absorptionProbField");
        kr = parseDoubleOrShowErrorMessage(scatterParameterField.getText(), "scatterParameterField");
        Ns = parseDoubleOrShowErrorMessage(statisticsField.getText(), "statisticField");

        F = 0;
        B = 0;
        Ab = 0;
        PasRad();
    }

    void PasRad() {
        for (var i = 1; i <= Ns; i++) {
            var x = x0;
            var cs = cos0;
            while (true) {
                double g = random();
                double l = -log(g) / mu; //On slide number 3 Lec_11

                double x1 = x + l * cs; //On slide number 4 Lec_11

                if (x1 < 0) {
                    B++;
                    break;
                } //On slide number 5 Lec_11
                if (x1 > h) {
                    F++;
                    break;
                }

                g = random();
                if (g < pa) {
                    Ab++;
                    break;
                } //On slide number 6 Lec_11

                g = random();
                double Com = pow(g, 1 / (kr + 1)); //On slide number 7 Lec_11
                g = random();
                double Sfi = sin(2 * 3.1415927 * g);
                double cos1 = cs * Com - sqrt((1 - cs * cs) * (1 - Com * Com)) * Sfi;    //On slide number 8 Lec_11

                x = x1;
                cs = cos1;
            }
        }
        transmissionRatioField.setText(roundDouble(F / Ns, 3));
        reflectionCoeffField.setText(roundDouble(B / Ns, 3));
        absorptionCoeffField.setText(roundDouble(Ab / Ns, 3));

        double Fns = F / Ns;
        double SF = sqrt(Fns * (1 - Fns) / Ns);
        uncertaintyZeroField.setText(roundDouble(SF, 4));

        double Bns = B / Ns;
        double SB = sqrt(Bns * (1 - Bns) / Ns);
        uncertaintyOneField.setText(roundDouble(SB, 4));

        double Ans = Ab / Ns;
        double SAb = sqrt(Ans * (1 - Ans) / Ns);
        uncertaintySecondField.setText(roundDouble(SAb, 4));
    }

    private String roundDouble(double value, int accuracy) {
        return BigDecimal.valueOf(value)
                .setScale(accuracy, RoundingMode.HALF_EVEN)
                .toString();
    }

/*
if (C1P1>0) C10P1 =SiCP1/C1P1*Nst*100; else C10P1 =0;
document.frm.DP1.value = MytoStr(C10P1,3);
*/

    private double parseDoubleOrShowErrorMessage(String value, String fieldName) {
        double d;
        try {
            d = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            Alert.AlertType alertAlertType;
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Wrong value for field: " + fieldName);
            alert.show();
            throw new RuntimeException();
        }
        return d;
    }
}

