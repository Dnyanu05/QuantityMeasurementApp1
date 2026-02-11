package com.quantitymeasurement;

/**
 * UC3/UC4/UC5/UC6: Generic, immutable length with a numeric value and a unit.
 * - Stores length as (value, unit)
 * - Uses INCH as the base unit for normalization (via LengthUnit helpers)
 * - Provides conversion, tolerant equality, and safe construction
 * - UC6: Adds addition of two Length values (across units), result in the unit of the first operand
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

    // ======================
    // UC6: Addition feature
    // ======================

    /**
     * Adds this Length with the given Length.
     * Rule: result is returned in the unit of the *first operand* (this.unit).
     *
     * Steps:
     * 1) Convert both operands to base inches
     * 2) Add in inches
     * 3) Convert the sum back to this.unit
     * 4) Return a NEW immutable Length
     *
     * @param other the second operand (must not be null; its unit must not be null)
     * @return new Length representing the sum, expressed in this.unit
     * @throws IllegalArgumentException if other is null or has null unit
     */
    public Length add(Length other) {
        if (other == null || other.unit == null) {
            throw new IllegalArgumentException("Second operand (Length) must not be null and must have a unit");
        }

        // 1) Convert both to base (inches)
        double sumInches = this.toBaseInches() + other.toBaseInches();

        // 2) Convert back to unit of the first operand
        double resultValueInThisUnit = this.unit.fromBaseInches(sumInches);

        // 3) Return a new Length
        return new Length(resultValueInThisUnit, this.unit);
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