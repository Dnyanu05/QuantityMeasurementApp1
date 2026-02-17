package com.quantitymeasurement;

import java.util.Objects;

public final class Weight {

    private static final double EPS = 1e-6;

    private final double value;
    private final WeightUnit unit;

    public Weight(double value, WeightUnit unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Invalid numeric value");
        }
        this.value = value;
        this.unit = unit;
    }

    /** Convert this weight to base (kg). */
    private double toBaseKg() {
        return unit.toBase(value);
    }

    /** Public convert to another unit. */
    public Weight convertTo(WeightUnit targetUnit) {
        if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
        double base = toBaseKg();
        double converted = targetUnit.fromBase(base);
        return new Weight(converted, targetUnit);
    }

    /** Addition – result in this object's unit. */
    public Weight add(Weight other) {
        Objects.requireNonNull(other, "Other weight cannot be null");
        double sumBase = this.toBaseKg() + other.toBaseKg();
        double result = this.unit.fromBase(sumBase);
        return new Weight(result, this.unit);
    }

    /** Addition – result in explicit target unit. */
    public Weight add(Weight other, WeightUnit targetUnit) {
        Objects.requireNonNull(other, "Other weight cannot be null");
        if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
        double sumBase = this.toBaseKg() + other.toBaseKg();
        double result = targetUnit.fromBase(sumBase);
        return new Weight(result, targetUnit);
    }

    public double getValue() { return value; }
    public WeightUnit getUnit() { return unit; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Weight other = (Weight) obj;
        return Math.abs(this.toBaseKg() - other.toBaseKg()) < EPS;
    }

    @Override
    public int hashCode() {
        double base = toBaseKg();
        long rounded = Math.round(base * 1_000_000L); // align with EPS 1e-6
        return Long.hashCode(rounded);
    }

    @Override
    public String toString() {
        return value + " " + unit.name().toLowerCase();
    }
}