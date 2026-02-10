package com.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static org.junit.jupiter.api.Assertions.*;  // <-- brings assertEquals, assertNotEquals, etc.

class FeetTest {

    @Test
    void givenTwoFeetWithSameValue_whenCompared_thenTheyAreEqual() {
        Feet f1 = new Feet(3.0);
        Feet f2 = new Feet(3.0);

        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    void givenTwoFeetWithDifferentValue_whenCompared_thenTheyAreNotEqual() {
        Feet f1 = new Feet(3.0);
        Feet f2 = new Feet(4.0);

        assertNotEquals(f1, f2);
    }

    @Test
    void givenFeet_whenComparedWithNullOrDifferentType_thenNotEqual() {
        Feet f1 = new Feet(3.0);

        assertNotEquals(f1, null);
        assertNotEquals(f1, "3.0");
    }

    @Test
    void givenSameObjectReference_whenCompared_thenEqual() {
        Feet f1 = new Feet(5.0);
        assertEquals(f1, f1);
    }
}