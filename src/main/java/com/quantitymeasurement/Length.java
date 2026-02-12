package com.quantitymeasurement;

/**
 * UC3/UC4/UC5/UC6/UC7: Generic, immutable length with a numeric value and a unit.
 * - Stores length as (value, unit)
 * - Uses INCH as the base unit for normalization (via LengthUnit helpers)
 * - Provides conversion, tolerant equality, and safe construction
 * - UC6: Adds addition of two Length values (across units), result in the unit of the first operand
 * - UC7: Adds addition with an explicit target unit (caller chooses result unit)
 */
public final class Length {

    /** Tolerance for floating-point comparisons (in base inches). */
    private static final double EPS = 1e-6;

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

    public double getValue() { return value; }
    public LengthUnit getUnit() { return unit; }

    /** Convert this Length to the target unit and return a NEW Length. */
    public Length convertTo(LengthUnit to) {
        if (to == null) throw new IllegalArgumentException("target unit must not be null");
        if (this.unit == to) return this;
        double baseInches = toBaseInches();
        double targetValue = to.fromBaseInches(baseInches);
        return new Length(targetValue, to);
    }

    /** Convert this Length to base inches (helper). */
    double toBaseInches() {
        return unit.toBaseInches(value);
    }

    /** Convenience equality check with tolerance. */
    public boolean sameAs(Length other) {
        if (other == null) return false;
        return Math.abs(this.toBaseInches() - other.toBaseInches()) < EPS;
    }

    // UC6: Addition – result in this.unit
    public Length add(Length other) {
        if (other == null || other.unit == null) {
            throw new IllegalArgumentException("Second operand (Length) must not be null and must have a unit");
        }
        double sumInches = this.toBaseInches() + other.toBaseInches();
        double resultValueInThisUnit = this.unit.fromBaseInches(sumInches);
        return new Length(resultValueInThisUnit, this.unit);
    }

    // UC7: Addition with explicit target unit
    public Length add(Length other, LengthUnit targetUnit) {
        if (other == null || other.unit == null) {
            throw new IllegalArgumentException("Second operand (Length) must not be null and must have a unit");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null");
        }
        double baseSum = this.toBaseInches() + other.toBaseInches();
        double resultValue = targetUnit.fromBaseInches(baseSum);
        return new Length(resultValue, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Length)) return false;
        Length other = (Length) obj;
        return Math.abs(this.toBaseInches() - other.toBaseInches()) < EPS;
    }

    @Override
    public int hashCode() {
        double base = toBaseInches();
        long rounded = Math.round(base * 1_000_000d); // 1e-6 precision
        return Long.hashCode(rounded);
    }

    @Override
    public String toString() {
        return value + " " + unit.name().toLowerCase();
    }
}