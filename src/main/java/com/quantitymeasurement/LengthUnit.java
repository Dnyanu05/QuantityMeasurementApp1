package com.quantitymeasurement;

/**
 * Length units with INCH as the base unit for normalization.
 *
 * Notes:
 * - Both singular and plural enum constants are provided to keep tests flexible:
 *   INCH/INCHES, FOOT/FEET, YARD/YARDS, CENTIMETER/CENTIMETERS.
 * - Internally we use conversion factors "to inches".
 */
public enum LengthUnit {

    // Base unit(s)
    INCH(1.0),
    INCHES(1.0),

    // Feet
    FOOT(12.0),   // 1 ft = 12 in
    FEET(12.0),

    // Yards
    YARD(36.0),   // 1 yd = 36 in
    YARDS(36.0),

    // Centimeters
    CENTIMETER(0.3937007874),   // 1 cm = 1/2.54 in (≈ 0.3937007874")
    CENTIMETERS(0.3937007874);

    /** multiplicative factor to convert THIS unit to base inches. */
    private final double toInchesFactor;

    LengthUnit(double toInchesFactor) {
        this.toInchesFactor = toInchesFactor;
    }

    /**
     * @return true if this unit is one of the base-unit aliases (INCH / INCHES).
     */
    public boolean isBase() {
        return this == INCH || this == INCHES;
    }

    /**
     * @return the multiplicative factor that converts THIS unit to inches.
     *         e.g., FOOT.factorToInches() == 12.0
     */
    public double factorToInches() {
        return toInchesFactor;
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