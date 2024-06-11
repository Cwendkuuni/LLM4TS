package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    // Test constructor
    @Test(expected = NullPointerException.class)
    public void testVerifierNullConstructor() {
        try {
            new Verifier(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testVerifierWithValidString() {
        String testString = "testVerifier";
        Verifier verifier = new Verifier(testString);
        assertEquals(testString, verifier.getValue());
    }

    // Test getValue method
    @Test
    public void testGetterValue() {
        String testString = "testVerifier";
        Verifier verifier = new Verifier(testString);
        assertEquals(testString, verifier.getValue());
    }

    // Edge cases for getValue method
    @Test
    public void testEmptyStringGetterValue() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }

    @Test
    public void testWhiteSpaceStringGetterValue() {
        Verifier verifier = new Verifier("   ");
        assertEquals("", verifier.getValue());
    }

    @Test
    public void testLongStringGetterValue() {
        String testString = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        Verifier verifier = new Verifier(testString);
        assertEquals(testString, verifier.getValue());
    }
}