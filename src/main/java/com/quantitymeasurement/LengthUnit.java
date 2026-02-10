package com.quantitymeasurement;

public enum LengthUnit {
    INCH(1.0),
    FOOT(12.0),
    YARD(36.0),          // 1 yd = 36 in (3 ft)
    CENTIMETER(0.393701);// 1 cm = 0.393701 in

    private final double toInchesFactor;

    LengthUnit(double toInchesFactor) {
        this.toInchesFactor = toInchesFactor;
    }

    /** Converts a value of THIS unit to base inches. */
    public double toBaseInches(double value) {
        return value * toInchesFactor;
    }
}