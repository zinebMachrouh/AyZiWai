package org.example.ayziwai.utils;

import org.example.ayziwai.entities.enums.AlertSeverity;
import org.example.ayziwai.entities.enums.DeviceType;

public class AlertEvaluator {

    public static AlertSeverity evaluateAlert(DeviceType deviceType, double value) {
        return switch (deviceType) {
            case TEMPERATURE -> evaluateTemperature(value);
            case HUMIDITY -> evaluateHumidity(value);
            case PRESSURE -> evaluatePressure(value);
            case CO2 -> evaluateCO2(value);
            case MOTION -> evaluateMotion(value);
            default -> AlertSeverity.NORMAL;
        };
    }

    private static AlertSeverity evaluateTemperature(double value) {
        if (value > 35) return AlertSeverity.CRITICAL;
        if (value > 30) return AlertSeverity.HIGH;
        if (value > 25) return AlertSeverity.MEDIUM;
        if (value < 15) return AlertSeverity.LOW;
        return AlertSeverity.NORMAL;
    }

    private static AlertSeverity evaluateHumidity(double value) {
        if (value > 80) return AlertSeverity.CRITICAL;
        if (value > 70) return AlertSeverity.HIGH;
        if (value > 60) return AlertSeverity.MEDIUM;
        if (value < 30) return AlertSeverity.LOW;
        return AlertSeverity.NORMAL;
    }

    private static AlertSeverity evaluatePressure(double value) {
        if (value > 1100) return AlertSeverity.HIGH;
        if (value < 900) return AlertSeverity.LOW;
        return AlertSeverity.NORMAL;
    }

    private static AlertSeverity evaluateCO2(double value) {
        if (value > 2000) return AlertSeverity.CRITICAL;
        if (value > 1500) return AlertSeverity.HIGH;
        if (value > 1000) return AlertSeverity.MEDIUM;
        return AlertSeverity.NORMAL;
    }

    private static AlertSeverity evaluateMotion(double value) {
        return value > 0 ? AlertSeverity.HIGH : AlertSeverity.NORMAL;
    }
}
