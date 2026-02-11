package com.quantitymeasurement;

public enum LengthUnit {
    INCH(1.0),                 // base unit
    FOOT(12.0),                // 1 ft = 12 in
    YARD(36.0),                // 1 yd = 36 in
    CENTIMETER(0.3937007874);  // 1 cm = 1/2.54 in

    private final double toInchesFactor;

    LengthUnit(double toInchesFactor) {
        this.toInchesFactor = toInchesFactor;
    }

    /** Convert a value of THIS unit to base inches. */
    public double toBaseInches(double value) {
        return value * toInchesFactor;
    }

    /** Convert a value in base inches to THIS unit. */
    public double fromBaseInches(double inches) {
        return inches / toInchesFactor;
    }
}