package com.quantitymeasurement;

public enum WeightUnit {
    KILOGRAM(1.0),     // base
    GRAM(0.001),       // 1 g = 0.001 kg
    POUND(0.45359237); // 1 lb = 0.45359237 kg (more precise)

    private final double toKg;

    WeightUnit(double toKg) {
        this.toKg = toKg;
    }

    /** Convert a value in this unit to base unit (kg). */
    public double toBase(double value) {
        return value * toKg;
    }

    /** Convert a base unit (kg) value to this unit. */
    public double fromBase(double baseValue) {
        return baseValue / toKg;
    }

    public double factorToKg() {
        return toKg;
    }
}