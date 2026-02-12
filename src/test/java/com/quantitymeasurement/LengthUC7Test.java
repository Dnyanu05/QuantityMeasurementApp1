package com.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UC7 – Addition of lengths with explicit target unit (result unit chosen by caller).
 * This suite verifies correctness across units, rounding tolerance, and validations.
 */
public class LengthUC7Test {

    // 1) Validation: Null target unit should throw
    @Test
    public void testAddition_ExplicitTargetUnit_NullTargetUnit() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCH);
        assertThrows(IllegalArgumentException.class, () -> l1.add(l2, null));
    }

    // 2) Large -> Small scale conversion (Feet -> Inches)
    // (1000 ft + 500 ft = 1500 ft = 18000 in)
    @Test
    public void testAddition_ExplicitTargetUnit_LargeToSmallScale() {
        Length l1 = new Length(1000.0, LengthUnit.FEET);
        Length l2 = new Length(500.0, LengthUnit.FEET);

        Length result = l1.add(l2, LengthUnit.INCH);

        assertEquals(18000.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.INCH, result.getUnit());
    }

    // 3) Small -> Large scale conversion (Inches -> Yards)
    // 12 in + 12 in = 24 in = 24/36 yd = 0.666666... yd ~ 0.667 yd
    @Test
    public void testAddition_ExplicitTargetUnit_SmallToLargeScale() {
        Length l1 = new Length(12.0, LengthUnit.INCH);
        Length l2 = new Length(12.0, LengthUnit.INCH);

        Length result = l1.add(l2, LengthUnit.YARD);

        assertEquals(0.667, result.getValue(), 0.001);
        assertEquals(LengthUnit.YARD, result.getUnit());
    }

    // 4) Target matches the first operand's unit
    // (2 yd + 3 ft = 2 yd + 1 yd = 3 yd)
    @Test
    public void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {
        Length l1 = new Length(2.0, LengthUnit.YARD);
        Length l2 = new Length(3.0, LengthUnit.FEET);

        Length result = l1.add(l2, LengthUnit.YARD);

        assertEquals(3.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.YARD, result.getUnit());
    }

    // 5) Target matches the second operand's unit
    // (2 yd + 3 ft) in FEET => 2 yd = 6 ft, + 3 ft = 9 ft
    @Test
    public void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {
        Length l1 = new Length(2.0, LengthUnit.YARD);
        Length l2 = new Length(3.0, LengthUnit.FEET);

        Length result = l1.add(l2, LengthUnit.FEET);

        assertEquals(9.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    // 6) Commutativity: order should not change the numeric result in the same target unit
    @Test
    public void testAddition_ExplicitTargetUnit_Commutativity() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCH);

        Length r1 = l1.add(l2, LengthUnit.YARD);
        Length r2 = l2.add(l1, LengthUnit.YARD);

        assertEquals(r1.getValue(), r2.getValue(), 1e-9);
        assertEquals(r1.getUnit(), r2.getUnit());
    }

    // 7) Zero value does not change result
    @Test
    public void testAddition_ExplicitTargetUnit_WithZero() {
        Length l1 = new Length(5.0, LengthUnit.FEET);
        Length l2 = new Length(0.0, LengthUnit.INCH);

        Length result = l1.add(l2, LengthUnit.YARD);

        // 5 ft = 60 in = 60/36 yd = 1.666666... yd ~ 1.667 yd
        assertEquals(1.667, result.getValue(), 0.001);
        assertEquals(LengthUnit.YARD, result.getUnit());
    }

    // 8) Negative numbers (allowed unless your business rules forbid)
    // 5 ft + (-2 ft) = 3 ft = 36 in
    @Test
    public void testAddition_ExplicitTargetUnit_NegativeValues() {
        Length l1 = new Length(5.0, LengthUnit.FEET);
        Length l2 = new Length(-2.0, LengthUnit.FEET);

        Length result = l1.add(l2, LengthUnit.INCH);

        assertEquals(36.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.INCH, result.getUnit());
    }

    // 9) All-unit combination (sample) – feet + centimeters to inches
    // 1 ft + 30.48 cm (== 12 in) = 24 in
    @Test
    public void testAddition_ExplicitTargetUnit_AllUnitCombinations_Sample() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(30.48, LengthUnit.CENTIMETER);

        Length result = l1.add(l2, LengthUnit.INCH);

        assertEquals(24.0, result.getValue(), 0.01);
        assertEquals(LengthUnit.INCH, result.getUnit());
    }

    // 10) Precision tolerance – ensures floating point handling
    @Test
    public void testAddition_ExplicitTargetUnit_PrecisionTolerance() {
        Length l1 = new Length(1.333, LengthUnit.FEET); // 15.996 in
        Length l2 = new Length(2.222, LengthUnit.FEET); // 26.664 in
        // Sum = 42.66 in
        Length result = l1.add(l2, LengthUnit.INCH);

        assertEquals(42.66, result.getValue(), 0.02);
        assertEquals(LengthUnit.INCH, result.getUnit());
    }
}