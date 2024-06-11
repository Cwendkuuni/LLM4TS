package org.scribe.model;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotSame;

public class VerifierTest {
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        String actualValue = verifier.getValue();
        String expectedValue = "testValue";

        // Assert that the value returned by getValue() matches the value passed to the constructor
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetValueWithEmptyStringVerifier() {
        Verifier verifier = new Verifier("");
        String actualValue = verifier.getValue();
        String expectedValue = "";

        // Assert that the value returned by getValue() matches the value passed to the constructor
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetValueWithNullVerifier() {
        Verifier verifier = null;
        String actualValue = verifier.getValue();

        // Assert that a NullPointerException is thrown when attempting to get the value of a null Verifier object
        assertThrows(NullPointerException.class, () -> actualValue);
    }

    @Test
    public void testGetValueWithNullVerifierValue() {
        Verifier verifier = new Verifier("testValue");
        verifier.value = null;
        String actualValue = verifier.getValue();

        // Assert that a NullPointerException is thrown when the value field of a Verifier object is null
        assertThrows(NullPointerException.class, () -> actualValue);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullValue() {
        // Assert that a NullPointerException is thrown when a null value is passed to the constructor
        assertThrows(NullPointerException.class, () -> new Verifier(null));
    }

    @Test
    public void testConstructorWithEmptyStringValue() {
        // Assert that an empty string is a valid value to be passed to the constructor
        Verifier verifier = new Verifier("");

        // Assert that the value returned by getValue() matches the value passed to the constructor
        assertEquals("", verifier.getValue());
    }

    @Test
    public void testValueEquality() {
        Verifier verifier1 = new Verifier("testValue1");
        Verifier verifier2 = new Verifier("testValue1");
        assertEquals(verifier1, verifier2);
        Verifier verifier3 = new Verifier("testValue2");
        assertNotSame(verifier1, verifier3);
    }

    @Test
    public void testValueHashCode() {
        Verifier verifier1 = new Verifier("testValue1");
        Verifier verifier2 = new Verifier("testValue1");
        assertEquals(verifier1.hashCode(), verifier2.hashCode());
        Verifier verifier3 = new Verifier("testValue2");
        assertNotEquals(verifier1.hashCode(), verifier3.hashCode());
    }
}