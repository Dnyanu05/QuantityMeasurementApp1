package com.quantitymeasurement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WeightUC9Test {

    // ==== Equality ====
    @Test
    void given1KgAnd1000Gram_whenCompared_shouldBeEqual() {
        Weight w1 = new Weight(1, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(1000, WeightUnit.GRAM);
        Assertions.assertEquals(w1, w2);
    }

    @Test
    void given1KgAnd2Pound_whenCompared_shouldNotBeEqual() {
        Weight w1 = new Weight(1, WeightUnit.KILOGRAM);
        Weight w2 = new Weight(2, WeightUnit.POUND);
        Assertions.assertNotEquals(w1, w2);
    }

    @Test
    void givenSameObject_whenCompared_shouldBeEqual() {
        Weight w = new Weight(2.5, WeightUnit.KILOGRAM);
        Assertions.assertEquals(w, w);
    }

    @Test
    void givenNull_whenCompared_shouldNotBeEqual() {
        Weight w = new Weight(500, WeightUnit.GRAM);
        Assertions.assertNotEquals(w, null);
    }

    // ==== Conversion ====
    @Test
    void given1Kg_whenConvertedToGrams_shouldBe1000() {
        Weight kg = new Weight(1, WeightUnit.KILOGRAM);
        Weight grams = kg.convertTo(WeightUnit.GRAM);
        Assertions.assertEquals(1000.0, grams.getValue(), 1e-6);
    }

    @Test
    void given1Pound_whenConvertedToKg_shouldBe0Point45359237() {
        Weight lb = new Weight(1, WeightUnit.POUND);
        Weight kg = lb.convertTo(WeightUnit.KILOGRAM);
        Assertions.assertEquals(0.45359237, kg.getValue(), 1e-8);
    }

    @Test
    void givenRoundTrip_kgToGramToKg_shouldReturnOriginal() {
        Weight kg = new Weight(2.345678, WeightUnit.KILOGRAM);
        Weight grams = kg.convertTo(WeightUnit.GRAM);
        Weight backToKg = grams.convertTo(WeightUnit.KILOGRAM);
        Assertions.assertEquals(kg.getValue(), backToKg.getValue(), 1e-6);
    }

    // ==== Addition (Implicit Target: this.unit) ====
    @Test
    void given1KgPlus500Gram_whenAdded_shouldBe1Point5Kg() {
        Weight kg = new Weight(1, WeightUnit.KILOGRAM);
        Weight g  = new Weight(500, WeightUnit.GRAM);
        Weight sum = kg.add(g);
        Assertions.assertEquals(1.5, sum.getValue(), 1e-6);
        Assertions.assertEquals(WeightUnit.KILOGRAM, sum.getUnit());
    }

    @Test
    void given500GramPlus1Pound_whenAdded_shouldBeCorrectInGramsWhenUsingLeftUnit() {
        Weight g = new Weight(500, WeightUnit.GRAM);
        Weight lb = new Weight(1, WeightUnit.POUND);
        Weight sum = g.add(lb); // result in grams
        // Expected: (500 g + 453.59237 g) = 953.59237 g
        Assertions.assertEquals(953.59237, sum.getValue(), 1e-5);
        Assertions.assertEquals(WeightUnit.GRAM, sum.getUnit());
    }

    // ==== Addition (Explicit Target) ====
    @Test
    void given1KgPlus1Pound_whenAddedWithTargetGram_shouldBe1453Point59237g() {
        Weight kg = new Weight(1, WeightUnit.KILOGRAM);
        Weight lb = new Weight(1, WeightUnit.POUND);
        Weight sum = kg.add(lb, WeightUnit.GRAM);
        Assertions.assertEquals(1453.59237, sum.getValue(), 1e-5);
        Assertions.assertEquals(WeightUnit.GRAM, sum.getUnit());
    }

    @Test
    void givenCommutativity_whenAPlusBEqualsBPlusA_inKg() {
        Weight a = new Weight(2, WeightUnit.KILOGRAM);
        Weight b = new Weight(1, WeightUnit.POUND);
        double sum1 = a.add(b, WeightUnit.KILOGRAM).getValue();
        double sum2 = b.add(a, WeightUnit.KILOGRAM).getValue();
        Assertions.assertEquals(sum1, sum2, 1e-6);
    }

    // ==== Edge cases ====
    @Test
    void givenZeroValues_whenCompared_shouldBeEqual() {
        Weight a = new Weight(0, WeightUnit.GRAM);
        Weight b = new Weight(0, WeightUnit.KILOGRAM);
        Assertions.assertEquals(a, b);
    }

    @Test
    void givenNegativeValues_whenAdded_shouldSumInTargetUnit() {
        Weight a = new Weight(-1, WeightUnit.KILOGRAM);
        Weight b = new Weight(500, WeightUnit.GRAM);
        Weight sum = a.add(b, WeightUnit.KILOGRAM);
        Assertions.assertEquals(-0.5, sum.getValue(), 1e-6);
    }
}