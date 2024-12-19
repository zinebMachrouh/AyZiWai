package org.example.ayziwai.utils;

import org.example.ayziwai.entities.enums.AlertSeverity;
import org.springframework.data.util.Pair;

public class AlertEvaluator {

    public static Pair<AlertSeverity, String> evaluateAlert(String deviceType, double value) {
        if ("Capteur de température".equals(deviceType)) {
            return evaluateTemperature(value);
        } else if ("Capteur d'humidité".equals(deviceType)) {
            return evaluateHumidity(value);
        } else {
            throw new IllegalArgumentException("Unsupported device type");
        }
    }

    private static Pair<AlertSeverity, String> evaluateTemperature(double value) {
        if (value > 40 || value < -10) {
            return Pair.of(AlertSeverity.CRITICAL, "Risque immédiat pour les équipements");
        } else if (value >= 35 || value <= -5) {
            return Pair.of(AlertSeverity.HIGH, "Situation préoccupante nécessitant une action rapide");
        } else if (value >= 30 || value <= 0) {
            return Pair.of(AlertSeverity.MEDIUM, "Situation à surveiller");
        } else if (value >= 25 || value <= 20) {
            return Pair.of(AlertSeverity.LOW, "Légère déviation des valeurs optimales");
        } else {
            return Pair.of(AlertSeverity.NORMAL, "Température dans la plage optimale");
        }
    }

    private static Pair<AlertSeverity, String> evaluateHumidity(double value) {
        if (value > 90 || value < 20) {
            return Pair.of(AlertSeverity.CRITICAL, "Risque de dommages matériels");
        } else if (value >= 80 || value <= 30) {
            return Pair.of(AlertSeverity.HIGH, "Conditions défavorables");
        } else if (value >= 70 || value <= 40) {
            return Pair.of(AlertSeverity.MEDIUM, "Situation à surveiller");
        } else if (value >= 65 || value <= 45) {
            return Pair.of(AlertSeverity.LOW, "Légère déviation");
        } else {
            return Pair.of(AlertSeverity.NORMAL, "Humidité dans la plage optimale");
        }
    }
}
