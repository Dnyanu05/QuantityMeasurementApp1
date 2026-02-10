package com.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LengthUC4Test {

    // 1) Yard-to-Yard equality
    @Test
    void testEquality_YardToYard_SameValue() {
        Length y1 = new Length(1.0, LengthUnit.YARD);
        Length y2 = new Length(1.0, LengthUnit.YARD);
        assertEquals(y1, y2);
        assertEquals(y1.hashCode(), y2.hashCode());
    }

    // 2) Yard-to-Yard different
    @Test
    void testEquality_YardToYard_DifferentValue() {
        Length y1 = new Length(1.0, LengthUnit.YARD);
        Length y2 = new Length(2.0, LengthUnit.YARD);
        assertNotEquals(y1, y2);
    }

    // 3) Yard ↔ Feet (1 yd = 3 ft)
    @Test
    void testEquality_YardToFeet_EquivalentValue() {
        Length yard = new Length(1.0, LengthUnit.YARD);
        Length feet = new Length(3.0, LengthUnit.FOOT);
        assertEquals(yard, feet);
        assertEquals(feet, yard); // symmetry
    }

    // 4) Feet ↔ Yard (symmetry check again)
    @Test
    void testEquality_FeetToYard_EquivalentValue() {
        Length feet = new Length(3.0, LengthUnit.FOOT);
        Length yard = new Length(1.0, LengthUnit.YARD);
        assertEquals(feet, yard);
    }

    // 5) Yard ↔ Inches (1 yd = 36 in)
    @Test
    void testEquality_YardToInches_EquivalentValue() {
        Length yard = new Length(1.0, LengthUnit.YARD);
        Length inches = new Length(36.0, LengthUnit.INCH);
        assertEquals(yard, inches);
        assertEquals(inches, yard);
    }

    // 6) Inches ↔ Yard (symmetry)
    @Test
    void testEquality_InchesToYard_EquivalentValue() {
        Length inches = new Length(36.0, LengthUnit.INCH);
        Length yard = new Length(1.0, LengthUnit.YARD);
        assertEquals(inches, yard);
    }

    // 7) Yard ↔ Feet non-equivalent (1 yd != 2 ft)
    @Test
    void testEquality_YardToFeet_NonEquivalentValue() {
        Length yard = new Length(1.0, LengthUnit.YARD);
        Length feet = new Length(2.0, LengthUnit.FOOT);
        assertNotEquals(yard, feet);
    }

    // 8) Centimeter ↔ Inch equivalence (1 cm = 0.393701 in)
    @Test
    void testEquality_CentimetersToInches_EquivalentValue() {
        Length cm = new Length(1.0, LengthUnit.CENTIMETER);
        Length inch = new Length(0.393701, LengthUnit.INCH);
        assertEquals(cm, inch);
        assertEquals(inch, cm);
    }

    // 9) Centimeter ↔ Feet non-equivalent (1 cm != 1 ft)
    @Test
    void testEquality_CentimetersToFeet_NonEquivalentValue() {
        Length cm = new Length(1.0, LengthUnit.CENTIMETER);
        Length feet = new Length(1.0, LengthUnit.FOOT);
        assertNotEquals(cm, feet);
    }

    // 10) Multi-unit transitive property:
    // 1 yd == 3 ft and 3 ft == 36 in → therefore 1 yd == 36 in
    @Test
    void testEquality_MultiUnit_TransitiveProperty() {
        Length yd = new Length(1.0, LengthUnit.YARD);
        Length ft = new Length(3.0, LengthUnit.FOOT);
        Length in = new Length(36.0, LengthUnit.INCH);

        assertEquals(yd, ft);
        assertEquals(ft, in);
        assertEquals(yd, in);
    }

    // 11) Null unit should throw (defensive check still valid)
    @Test
    void testEquality_NullUnit_Throws() {
        assertThrows(NullPointerException.class, () -> new Length(1.0, null));
    }

    // 12) Same reference in new units also stays true
    @Test
    void testEquality_Yard_SameReference() {
        Length y = new Length(2.0, LengthUnit.YARD);
        assertEquals(y, y);
    }
}