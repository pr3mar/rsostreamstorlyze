package com.rsostream.storlyze.services;

import com.rsostream.storlyze.models.sensorReadings.SensorReading;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

public class ReadingFallback implements FallbackHandler<SensorReading> {
    @Override
    public SensorReading handle(ExecutionContext executionContext) {
        return new SensorReading();
    }
}
