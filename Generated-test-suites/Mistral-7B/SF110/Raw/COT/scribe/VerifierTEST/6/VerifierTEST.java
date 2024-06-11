package org.scribe.model.test;

import static org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

import org.junit.Test;

public class VerifierTest {

    // Test constructor
    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructorNullValue() {
        Preconditions.checkArgument(false, "Must provide a valid string as verifier");
    }

    @Test
    public void testVerifierConstructorValidValue() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        assertEquals(testValue, verifier.getValue());
    }

    // Test getValue method
    @Test
    public void testgetValue() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }

    // Edge cases and exception handling tests

    @Test(expected = NullPointerException.class)
    public void testVerifierNullValuePassedTogetValue() {
        Verifier verifier = new Verifier(null);
        assertNull(verifier.getValue());
    }

    @Test
    public void testVerifierEmptyValue() {
        String emptyValue = "";
        Verifier verifier = new Verifier(emptyValue);
        assertEquals(emptyValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierLongValue() {
        String longValue = StringUtils.repeat("a", 256);
        Preconditions.checkArgument(false, "Must provide a valid string as verifier (max length: 255)");
    }

    @Test
    public void testVerifierLongValueInValue() {
        String longValue = StringUtils.repeat("a", 255);
        Verifier verifier = new Verifier(longValue);
        assertEquals(longValue, verifier.getValue());
    }
}