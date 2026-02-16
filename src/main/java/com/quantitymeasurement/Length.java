package com.quantitymeasurement;

/**
 * UC3/UC4/UC5/UC6/UC7/UC8: Immutable Length with numeric value + unit.
 * - Base normalization uses INCH/INCHES via the standalone LengthUnit enum.
 * - All conversion math is delegated to LengthUnit (UC8 refactor).
 * - Exception types are aligned with existing UC1–UC7 test expectations:
 *      * Constructor: null unit → NullPointerException
 *      * add(null): IllegalArgumentException
 *      * add(x) with x.unit == null → NullPointerException
 *      * add(x, target=null) → IllegalArgumentException
 *      * Invalid numeric (NaN/∞) → IllegalArgumentException
 */
public final class Length {

    /** Tolerance for floating-point comparisons (in base inches). */
    private static final double EPS = 1e-6;

    private final double value;
    private final LengthUnit unit;

    public Length(double value, LengthUnit unit) {
        // Backward compatibility with older tests: expect NPE when unit is null
        if (unit == null) {
            throw new NullPointerException("Unit must not be null");
        }
        // UC8: reject invalid numeric values
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() { return value; }
    public LengthUnit getUnit() { return unit; }

    /** Convert this Length to the target unit and return a NEW Length. */
    public Length convertTo(LengthUnit to) {
        // Keep IAE for null target (older suites typically use IAE here)
        if (to == null) throw new IllegalArgumentException("Target unit must not be null");
        if (this.unit == to) return this; // small optimization
        double baseInches = toBaseInches();
        double targetValue = to.fromBaseInches(baseInches);
        return new Length(targetValue, to);
    }

    /** Convert this Length to base inches (helper). Package-private for tests. */
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
        // Your UC6 test expects IllegalArgumentException when second operand is null
        if (other == null) {
            throw new IllegalArgumentException("Second operand (Length) must not be null");
        }
        // Older tests expect NPE when inner unit is null
        if (other.unit == null) {
            throw new NullPointerException("Second operand must have a unit");
        }
        double sumInches = this.toBaseInches() + other.toBaseInches();
        double resultValueInThisUnit = this.unit.fromBaseInches(sumInches);
        return new Length(resultValueInThisUnit, this.unit);
    }

    // UC7: Addition with explicit target unit
    public Length add(Length other, LengthUnit targetUnit) {
        // Keep consistent with UC6: IAE for null other
        if (other == null) {
            throw new IllegalArgumentException("Second operand (Length) must not be null");
        }
        // NPE for missing unit inside 'other'
        if (other.unit == null) {
            throw new NullPointerException("Second operand must have a unit");
        }
        // IAE for null target unit (typical expectation)
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
        // Hash on normalized base inches (rounded to match equals tolerance)
        double base = toBaseInches();
        long rounded = Math.round(base * 1_000_000d); // 1e-6 precision
        return Long.hashCode(rounded);
    }

    @Override
    public String toString() {
        return value + " " + unit.name().toLowerCase();
    }
}