package com.quantitymeasurement;

/**
 * UC2: Equality for Inch measurements (similar to Feet).
 * This class is intentionally separate from Feet for UC2,
 * even though the logic is similar (we'll remove duplication in UC3).
 */
public class Inches {
    private final double value;

    public Inches(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    /**
     * Two Inches objects are equal if their numeric values are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;                      // same reference
        if (obj == null || getClass() != obj.getClass())   // null or different class
            return false;
        Inches other = (Inches) obj;
        return Double.compare(this.value, other.value) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public String toString() {
        return value + " in";
    }
}