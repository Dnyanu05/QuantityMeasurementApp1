package com.quantitymeasurement;

/**
 * UC3/UC4/UC5: Generic, immutable length with a numeric value and a unit.
 * - Stores length as (value, unit)
 * - Uses INCH as the base unit for normalization (via LengthUnit helpers)
 * - Provides conversion, tolerant equality, and safe construction
 */
public final class Length {

    /** Tolerance for floating-point comparisons (in base inches). */
    private static final double EPS = 1e-6;

    private final double value;
    private final LengthUnit unit;

    /**
     * Creates a Length with the specified value and unit.
     *
     * @param value numeric magnitude; must be finite (not NaN/Infinity)
     * @param unit  measurement unit; must not be null
     * @throws NullPointerException     if unit is null
     * @throws IllegalArgumentException if value is NaN or infinite
     */
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

    /** @return the stored numeric value in this instance's unit */
    public double getValue() {
        return value;
    }

    /** @return the unit associated with this length */
    public LengthUnit getUnit() {
        return unit;
    }

    /**
     * Converts this Length to the target unit and returns a NEW Length.
     * If the target unit equals this unit, returns this instance.
     *
     * @param to target unit (must not be null)
     * @return converted Length in the target unit
     * @throws IllegalArgumentException if target unit is null
     */
    public Length convertTo(LengthUnit to) {
        if (to == null) {
            throw new IllegalArgumentException("target unit must not be null");
        }
        if (this.unit == to) {
            return this; // already in target unit
        }
        // Normalize to base inches, then convert to target unit
        double baseInches = toBaseInches();
        double targetValue = to.fromBaseInches(baseInches);
        return new Length(targetValue, to);
    }

    /** Convert this Length to base inches (helper used internally). */
    double toBaseInches() {
        return unit.toBaseInches(value);
    }

    /** Convenience equality check (same logic as equals). */
    public boolean sameAs(Length other) {
        if (other == null) return false;
        return Math.abs(this.toBaseInches() - other.toBaseInches()) < EPS;
    }

    /**
     * Equality uses base-inch normalization with a small tolerance, so
     * values like 12 INCH and 1 FOOT are considered equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;                  // same reference
        if (!(obj instanceof Length)) return false;
        Length other = (Length) obj;
        return Math.abs(this.toBaseInches() - other.toBaseInches()) < EPS;
    }

    /**
     * Hash code is derived from the normalized (base inches) value rounded
     * to 1e-6 to align with equals' tolerance.
     */
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