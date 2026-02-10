package com.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InchesTest {

    @Test
    void givenTwoInchesWithSameValue_whenCompared_thenTheyAreEqual() {
        Inches i1 = new Inches(1.0);
        Inches i2 = new Inches(1.0);

        assertEquals(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());
    }

    @Test
    void givenTwoInchesWithDifferentValue_whenCompared_thenTheyAreNotEqual() {
        Inches i1 = new Inches(1.0);
        Inches i2 = new Inches(2.0);

        assertNotEquals(i1, i2);
    }

    @Test
    void givenInches_whenComparedWithNullOrDifferentType_thenNotEqual() {
        Inches i1 = new Inches(1.0);

        assertNotEquals(i1, null);
        assertNotEquals(i1, "1.0");
    }

    @Test
    void givenSameObjectReference_whenCompared_thenEqual() {
        Inches i1 = new Inches(2.5);
        assertEquals(i1, i1);
    }
}