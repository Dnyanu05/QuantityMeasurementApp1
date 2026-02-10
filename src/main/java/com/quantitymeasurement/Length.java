package com.quantitymeasurement;

/**
 * UC3: Generic length with a numeric value and a unit (LengthUnit).
 * Eliminates duplication from separate Feet/Inches classes.
 */
public final class Length {
    private static final double EPS = 1e-9; // tolerance for floating-point comparisons

    private final double value;
    private final LengthUnit unit;

    public Length(double value, LengthUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit must not be null");
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("value must be a finite number");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    /** Convert this Length to base inches. */
    private double toBaseInches() {
        return unit.toBaseInches(value);
    }

    /** Convenience equality check (same logic as equals). */
    public boolean sameAs(Length other) {
        if (other == null) return false;
        return Math.abs(this.toBaseInches() - other.toBaseInches()) < EPS;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;                  // same reference
        if (obj == null || getClass() != obj.getClass()) return false;
        Length other = (Length) obj;
        return Math.abs(this.toBaseInches() - other.toBaseInches()) < EPS;
    }

    @Override
    public int hashCode() {
        // Round base inches to reduce floating-point noise
        double base = toBaseInches();
        long rounded = Math.round(base * 1_000_000_000d); // 1e-9 precision
        return Long.hashCode(rounded);
    }

    @Override
    public String toString() {
        return value + " " + unit.name().toLowerCase();
    }
}