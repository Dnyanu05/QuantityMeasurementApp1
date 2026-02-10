package com.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LengthTest {

    // --- Same-unit equality ---
    @Test
    void testEquality_FeetToFeet_SameValue() {
        Length l1 = new Length(1.0, LengthUnit.FOOT);
        Length l2 = new Length(1.0, LengthUnit.FOOT);
        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        Length l1 = new Length(1.0, LengthUnit.INCH);
        Length l2 = new Length(1.0, LengthUnit.INCH);
        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
    }

    // --- Cross-unit equality (conversion) ---
    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        Length feet = new Length(1.0, LengthUnit.FOOT);
        Length inches = new Length(12.0, LengthUnit.INCH);
        assertEquals(feet, inches);
        assertEquals(inches, feet); // symmetry
    }

    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        Length inches = new Length(24.0, LengthUnit.INCH);
        Length feet = new Length(2.0, LengthUnit.FOOT);
        assertTrue(inches.equals(feet) && feet.equals(inches));
    }

    // --- Different values ---
    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        Length l1 = new Length(1.0, LengthUnit.FOOT);
        Length l2 = new Length(2.0, LengthUnit.FOOT);
        assertNotEquals(l1, l2);
    }

    @Test
    void testEquality_InchToInch_DifferentValue() {
        Length l1 = new Length(1.0, LengthUnit.INCH);
        Length l2 = new Length(2.0, LengthUnit.INCH);
        assertNotEquals(l1, l2);
    }

    // --- Null / type safety ---
    @Test
    void testEquality_NullComparison() {
        Length l1 = new Length(1.0, LengthUnit.FOOT);
        assertNotEquals(l1, null);
    }

    @Test
    void testEquality_SameReference() {
        Length l1 = new Length(1.0, LengthUnit.INCH);
        assertEquals(l1, l1);
    }

    @Test
    void testEquality_NullUnit_Throws() {
        assertThrows(NullPointerException.class, () -> new Length(1.0, null));
    }

    @Test
    void testEquality_InvalidNumber_Throws() {
        assertThrows(IllegalArgumentException.class, () -> new Length(Double.NaN, LengthUnit.FOOT));
        assertThrows(IllegalArgumentException.class, () -> new Length(Double.POSITIVE_INFINITY, LengthUnit.INCH));
    }
}