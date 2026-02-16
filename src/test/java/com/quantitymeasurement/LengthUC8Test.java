package com.quantitymeasurement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UC8 Test Suite – Refactor: Unit enum extracted to standalone class (LengthUnit)
 * Base unit = INCH/INCHES (toBaseInches/fromBaseInches).
 * Ensures: enum conversion works, Length delegates, validations, and backward-compatibility.
 *
 * NOTE: This project’s older tests (UC1–UC7) expect NullPointerException for nulls.
 * Therefore, UC8 null tests also assert NPE to keep consistency.
 */
public class LengthUC8Test {

    /* small assert helper */
    private static void approx(double expected, double actual) {
        assertEquals(expected, actual, 1e-6);
    }

    /* ========= 1) Enum constants & conversion factors ========= */

    @Test @DisplayName("UC8-01: FEET factor = 12.0 (to inches)")
    void feetFactor() {
        approx(12.0, LengthUnit.FEET.getConversionFactor());
        approx(12.0, LengthUnit.FOOT.getConversionFactor());
    }

    @Test @DisplayName("UC8-02: INCH/INCHES factor = 1.0 (base)")
    void inchesFactor() {
        approx(1.0, LengthUnit.INCH.getConversionFactor());
        approx(1.0, LengthUnit.INCHES.getConversionFactor());
        assertTrue(LengthUnit.INCH.isBase());
        assertTrue(LengthUnit.INCHES.isBase());
    }

    @Test @DisplayName("UC8-03: YARDS factor = 36.0 (to inches)")
    void yardsFactor() {
        approx(36.0, LengthUnit.YARD.getConversionFactor());
        approx(36.0, LengthUnit.YARDS.getConversionFactor());
    }

    @Test @DisplayName("UC8-04: CENTIMETERS factor ≈ 0.3937007874 (to inches)")
    void centimetersFactor() {
        approx(0.3937007874, LengthUnit.CENTIMETER.getConversionFactor());
        approx(0.3937007874, LengthUnit.CENTIMETERS.getConversionFactor());
    }

    /* ========= 2) convertToBase (inches) ========= */

    @Test @DisplayName("UC8-05: Feet → Inches")
    void feetToInches_toBase() {
        approx(12.0, LengthUnit.FEET.toBaseInches(1.0));
        approx(24.0, LengthUnit.FEET.convertToBaseUnit(2.0)); // alias name
    }

    @Test @DisplayName("UC8-06: Inches → Inches (identity)")
    void inchesToInches_toBase() {
        approx(12.0, LengthUnit.INCHES.toBaseInches(12.0));
    }

    @Test @DisplayName("UC8-07: Yards → Inches")
    void yardsToInches_toBase() {
        approx(36.0, LengthUnit.YARDS.toBaseInches(1.0));
    }

    @Test @DisplayName("UC8-08: Centimeters → Inches (~30.48 cm ≈ 12 in)")
    void centimetersToInches_toBase() {
        approx(12.0, LengthUnit.CENTIMETERS.toBaseInches(30.48));
    }

    /* ========= 3) convertFromBase (inches) ========= */

    @Test @DisplayName("UC8-09: Inches → Feet")
    void inchesToFeet_fromBase() {
        approx(1.0, LengthUnit.FEET.fromBaseInches(12.0));
        approx(2.5, LengthUnit.FEET.convertFromBaseUnit(30.0));
    }

    @Test @DisplayName("UC8-10: Inches → Inches (identity)")
    void inchesToInches_fromBase() {
        approx(10.0, LengthUnit.INCH.fromBaseInches(10.0));
    }

    @Test @DisplayName("UC8-11: Inches → Yards")
    void inchesToYards_fromBase() {
        approx(1.0 / 36.0, LengthUnit.YARDS.fromBaseInches(1.0));
        approx(1.0, LengthUnit.YARDS.fromBaseInches(36.0));
    }

    @Test @DisplayName("UC8-12: Inches → Centimeters (1 in = 2.54 cm)")
    void inchesToCentimeters_fromBase() {
        approx(2.54, LengthUnit.CENTIMETERS.fromBaseInches(1.0));
        approx(30.48, LengthUnit.CENTIMETERS.fromBaseInches(12.0));
    }

    /* ========= 4) Refactored Length behavior ========= */

    @Test @DisplayName("UC8-13: Equality across units (delegates to enum)")
    void lengthEqualityRefactored() {
        assertEquals(new Length(1.0, LengthUnit.FEET),
                new Length(12.0, LengthUnit.INCHES));
        assertEquals(new Length(36.0, LengthUnit.INCH),
                new Length(1.0, LengthUnit.YARD));
    }

    @Test @DisplayName("UC8-14: convertTo delegates correctly")
    void lengthConvertToRefactored() {
        Length out = new Length(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCHES);
        assertEquals(new Length(12.0, LengthUnit.INCHES), out);
    }

    @Test @DisplayName("UC8-15: add(other, targetUnit) delegates correctly")
    void lengthAddWithTargetRefactored() {
        Length sum = new Length(1.0, LengthUnit.FEET)
                .add(new Length(12.0, LengthUnit.INCHES), LengthUnit.INCHES);
        assertEquals(new Length(24.0, LengthUnit.INCHES), sum);
    }

    @Test @DisplayName("UC8-16: add(other) keeps 'this' unit (UC6 compatibility)")
    void lengthAddDefaultUnitRefactored() {
        Length sum = new Length(1.0, LengthUnit.FEET)
                .add(new Length(12.0, LengthUnit.INCHES));
        assertEquals(new Length(2.0, LengthUnit.FEET), sum);
    }

    /* ========= 5) Type safety (null/NaN/∞) ========= */

    @Test
    @DisplayName("UC8-17: Constructor rejects null unit (expects NPE to match UC1–UC7)")
    void nullUnitRejected() {
        assertThrows(NullPointerException.class, () -> new Length(1.0, null));
    }

    @Test @DisplayName("UC8-18: Constructor rejects NaN/Infinity")
    void invalidValueRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Length(Double.NaN, LengthUnit.FEET));
        assertThrows(IllegalArgumentException.class, () -> new Length(Double.POSITIVE_INFINITY, LengthUnit.FEET));
        assertThrows(IllegalArgumentException.class, () -> new Length(Double.NEGATIVE_INFINITY, LengthUnit.INCHES));
    }

    @Test @DisplayName("UC8-19: Enum conversion rejects NaN/Infinity")
    void enumRejectsInvalidNumbers() {
        assertThrows(IllegalArgumentException.class, () -> LengthUnit.FEET.toBaseInches(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> LengthUnit.FEET.toBaseInches(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> LengthUnit.INCHES.fromBaseInches(Double.NEGATIVE_INFINITY));
    }

    /* ========= 6) Backward-compatibility spot checks (UC1/UC3/UC5/UC6/UC7) ========= */

    @Test @DisplayName("UC8-20: UC1 – Same unit equality still works")
    void uc1Equality() {
        assertEquals(new Length(2.0, LengthUnit.FEET), new Length(2.0, LengthUnit.FEET));
        assertNotEquals(new Length(2.0, LengthUnit.FEET), new Length(3.0, LengthUnit.FEET));
    }

    @Test @DisplayName("UC8-21: UC3 – Cross-unit equality")
    void uc3CrossUnitEquality() {
        assertEquals(new Length(3.0, LengthUnit.FEET), new Length(1.0, LengthUnit.YARD));
        assertEquals(new Length(36.0, LengthUnit.INCH), new Length(1.0, LengthUnit.YARDS));
    }

    @Test @DisplayName("UC8-22: UC5 – Conversions")
    void uc5Conversions() {
        assertEquals(new Length(12.0, LengthUnit.INCHES),
                new Length(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCHES));
        assertEquals(new Length(1.0, LengthUnit.FEET),
                new Length(12.0, LengthUnit.INCHES).convertTo(LengthUnit.FEET));
    }

    @Test @DisplayName("UC8-23: UC6 – Add without target unit")
    void uc6Add() {
        Length sum = new Length(1.0, LengthUnit.FEET)
                .add(new Length(12.0, LengthUnit.INCHES));
        assertEquals(new Length(2.0, LengthUnit.FEET), sum);
    }

    @Test @DisplayName("UC8-24: UC7 – Add with target unit")
    void uc7AddWithTarget() {
        Length sumInInches = new Length(1.0, LengthUnit.FEET)
                .add(new Length(12.0, LengthUnit.INCHES), LengthUnit.INCHES);
        assertEquals(new Length(24.0, LengthUnit.INCHES), sumInInches);
    }

    /* ========= 7) Round-trip & immutability ========= */

    @Test @DisplayName("UC8-25: Round-trip convert keeps numeric precision")
    void roundTripPrecision() {
        double v = 37.25; // inches
        double feet = LengthUnit.FEET.fromBaseInches(v);
        double inchesBack = LengthUnit.FEET.toBaseInches(feet);
        approx(v, inchesBack);
    }

    @Test @DisplayName("UC8-26: Enum is immutable/stable")
    void enumImmutability() {
        double before = LengthUnit.YARD.getConversionFactor();
        double after  = LengthUnit.YARD.getConversionFactor();
        approx(before, after);
        assertSame(LengthUnit.YARD, LengthUnit.YARD.canonical());
    }

    /* ========= 8) sameAs helper basic check ========= */

    @Test @DisplayName("UC8-27: sameAs works across units")
    void sameAsCheck() {
        assertTrue(new Length(1.0, LengthUnit.FEET).sameAs(new Length(12.0, LengthUnit.INCH)));
        assertFalse(new Length(1.0, LengthUnit.FEET).sameAs(new Length(11.9, LengthUnit.INCH)));
    }
}