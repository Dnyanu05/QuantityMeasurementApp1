package com.quantitymeasurement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LengthUC5Test {

    // Keep your 6 existing passing tests above or here.
    // Below are the additional 6 tests with proper Java syntax (no HTML entities).

    @Test
    void foot_to_yard() {
        double result = LengthConversion.convert(6.0, LengthUnit.FOOT, LengthUnit.YARD);
        Assertions.assertEquals(2.0, result);
    }

    @Test
    void yard_to_foot() {
        double result = LengthConversion.convert(3.0, LengthUnit.YARD, LengthUnit.FOOT);
        Assertions.assertEquals(9.0, result);
    }

    @Test
    void small_value_precision_cm_to_in() {
        double result = LengthConversion.convert(1.0, LengthUnit.CENTIMETER, LengthUnit.INCH);
        // 1 cm ≈ 0.3937007874 in → we allow 1e-6 tolerance
        Assertions.assertTrue(Math.abs(result - 0.393701) < 1e-6);
    }

    @Test
    void roundtrip_inch_to_cm_back_to_inch() {
        double start = 17.0;
        double cm = LengthConversion.convert(start, LengthUnit.INCH, LengthUnit.CENTIMETER);
        double back = LengthConversion.convert(cm, LengthUnit.CENTIMETER, LengthUnit.INCH);
        Assertions.assertTrue(LengthConversion.almostEqual(start, back));
    }

    @Test
    void same_unit_no_change() {
        double val = 7.25;
        double result = LengthConversion.convert(val, LengthUnit.INCH, LengthUnit.INCH);
        Assertions.assertEquals(val, result);
    }

    @Test
    void invalid_inputs_throw() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> LengthConversion.convert(Double.NaN, LengthUnit.FOOT, LengthUnit.INCH));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> LengthConversion.convert(Double.POSITIVE_INFINITY, LengthUnit.FOOT, LengthUnit.INCH));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> LengthConversion.convert(1.0, null, LengthUnit.INCH));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> LengthConversion.convert(1.0, LengthUnit.FOOT, null));
    }
}