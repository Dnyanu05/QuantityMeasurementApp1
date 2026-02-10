package com.quantitymeasurement;

public enum LengthUnit {
    INCH(1.0),
    FOOT(12.0);

    private final double toInchesFactor;

    LengthUnit(double toInchesFactor) {
        this.toInchesFactor = toInchesFactor;
    }

    public double toBaseInches(double value) {
        return value * toInchesFactor;
    }
}