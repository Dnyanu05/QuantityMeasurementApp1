package com.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static com.quantitymeasurement.LengthUnit.*;
import static org.junit.jupiter.api.Assertions.*;

public class LengthUC6Test {

    private static final double EPSILON = 1e-4;

    // 1) Same unit addition: Feet + Feet
    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        Length a = new Length(1.0, FEET);
        Length b = new Length(2.0, FEET);
        Length sum = a.add(b);
        assertEquals(3.0, sum.getValue(), EPSILON);
        assertEquals(FEET, sum.getUnit());
    }

    // Same unit addition: Inches + Inches
    @Test
    void testAddition_SameUnit_InchPlusInch() {
        Length a = new Length(6.0, INCHES);
        Length b = new Length(6.0, INCHES);
        Length sum = a.add(b);
        assertEquals(12.0, sum.getValue(), EPSILON);
        assertEquals(INCHES, sum.getUnit());
    }

    // Same unit addition: Yards + Yards
    @Test
    void testAddition_SameUnit_YardPlusYard() {
        Length a = new Length(1.0, YARDS);
        Length b = new Length(2.0, YARDS);
        Length sum = a.add(b);
        assertEquals(3.0, sum.getValue(), EPSILON);
        assertEquals(YARDS, sum.getUnit());
    }

    // Same unit addition: Centimeters + Centimeters
    @Test
    void testAddition_SameUnit_CmPlusCm() {
        Length a = new Length(2.0, CENTIMETERS);
        Length b = new Length(3.0, CENTIMETERS);
        Length sum = a.add(b);
        assertEquals(5.0, sum.getValue(), EPSILON);
        assertEquals(CENTIMETERS, sum.getUnit());
    }

    // 2) Cross-unit: Feet + Inches = Feet
    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        Length a = new Length(1.0, FEET);
        Length b = new Length(12.0, INCHES);
        Length sum = a.add(b); // 1 ft + 12 in = 2 ft
        assertEquals(2.0, sum.getValue(), EPSILON);
        assertEquals(FEET, sum.getUnit());
    }

    // Cross-unit: Inches + Feet = Inches
    @Test
    void testAddition_CrossUnit_InchesPlusFeet() {
        Length a = new Length(12.0, INCHES);
        Length b = new Length(1.0, FEET);
        Length sum = a.add(b); // 12 in + 12 in = 24 in
        assertEquals(24.0, sum.getValue(), EPSILON);
        assertEquals(INCHES, sum.getUnit());
    }

    // Cross-unit: Yards + Feet (1 yd + 1 ft = 48 inches -> 48/36 = 1.333.. yd)
    @Test
    void testAddition_CrossUnit_YardPlusFeet() {
        Length a = new Length(1.0, YARDS);
        Length b = new Length(1.0, FEET);
        Length sum = a.add(b);
        // 1 yard (36 in) + 1 ft (12 in) = 48 in -> 48/36 = 1.333333 yd
        assertEquals(48.0 / 36.0, sum.getValue(), EPSILON);
        assertEquals(YARDS, sum.getUnit());
    }

    // Cross-unit: Centimeter + Inch
    @Test
    void testAddition_CrossUnit_CentimeterPlusInch() {
        Length a = new Length(2.54, CENTIMETERS); // 1 inch
        Length b = new Length(1.0, INCHES);       // 1 inch
        Length sum = a.add(b);                    // expect ~2 inches in CM -> 5.08 cm
        Length expected = new Length(5.08, CENTIMETERS);
        assertEquals(expected, sum);
        assertEquals(CENTIMETERS, sum.getUnit());
        assertEquals(5.08, sum.getValue(), 1e-2); // allow small rounding
    }

    // 3) Commutativity: a + b == b + a (values may differ in unit but equal in measure)
    @Test
    void testAddition_Commutativity() {
        Length a = new Length(1.0, FEET);
        Length b = new Length(12.0, INCHES);
        Length sum1 = a.add(b);   // 2 ft
        Length sum2 = b.add(a);   // 24 in
        assertEquals(sum1, sum2); // equal as quantities
    }

    // 4) Zero as identity element
    @Test
    void testAddition_WithZero() {
        Length a = new Length(5.0, FEET);
        Length zero = new Length(0.0, INCHES);
        Length sum = a.add(zero); // 5 ft + 0 in = 5 ft
        assertEquals(5.0, sum.getValue(), EPSILON);
        assertEquals(FEET, sum.getUnit());
    }

    // 5) Negative values
    @Test
    void testAddition_NegativeValues() {
        Length a = new Length(5.0, FEET);
        Length b = new Length(-2.0, FEET);
        Length sum = a.add(b);
        assertEquals(3.0, sum.getValue(), EPSILON);
        assertEquals(FEET, sum.getUnit());
    }

    // 6) Null second operand -> exception
    @Test
    void testAddition_NullSecondOperand() {
        Length a = new Length(1.0, FEET);
        assertThrows(IllegalArgumentException.class, () -> a.add(null));
    }

    // 7) Large values
    @Test
    void testAddition_LargeValues() {
        Length a = new Length(1_000_000.0, FEET);
        Length b = new Length(1_000_000.0, FEET);
        Length sum = a.add(b);
        assertEquals(2_000_000.0, sum.getValue(), 1e-2);
        assertEquals(FEET, sum.getUnit());
    }

    // 8) Small values (epsilon handling)
    @Test
    void testAddition_SmallValues() {
        Length a = new Length(0.001, FEET);
        Length b = new Length(0.002, FEET);
        Length sum = a.add(b);
        assertEquals(0.003, sum.getValue(), EPSILON);
        assertEquals(FEET, sum.getUnit());
    }
}