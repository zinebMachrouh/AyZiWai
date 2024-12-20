package org.example.ayziwai.actuator;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {
    private final Counter deviceCreationCounter;
    private final Counter measurementCounter;
    private final Counter alertCounter;

    public CustomMetrics(MeterRegistry registry) {
        this.deviceCreationCounter = Counter.builder("ayziwai.devices.created")
                .description("Number of devices created")
                .register(registry);
        
        this.measurementCounter = Counter.builder("ayziwai.measurements.recorded")
                .description("Number of measurements recorded")
                .register(registry);
        
        this.alertCounter = Counter.builder("ayziwai.alerts.generated")
                .description("Number of alerts generated")
                .register(registry);
    }

    public void incrementDeviceCreations() {
        deviceCreationCounter.increment();
    }

    public void incrementMeasurements() {
        measurementCounter.increment();
    }

    public void incrementAlerts() {
        alertCounter.increment();
    }
} 