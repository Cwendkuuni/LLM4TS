package org.junit.test; // Replace with the actual test package

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        String validVerifier = "abc123";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals(validVerifier, verifier.getValue());
    }

    @Test
    public void testVerifierGetValue() {
        String verifierValue = "xyz456";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }

    @Test
    public void testVerifierEmptyConstruction() {
        try {
            new Verifier("");
            fail("Expected Preconditions.checkNotNull exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }

    @Test
    public void testVerifierNullConstruction() {
        try {
            new Verifier(null);
            fail("Expected Preconditions.checkNotNull exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }

    @Test
    public void testVerifierLongValueConstruction() {
        String longVerifierValue = "1234567890123456789012345678901234567890";
        Verifier verifier = new Verifier(longVerifierValue);
        assertEquals(longVerifierValue, verifier.getValue());
    }

    @Test
    public void testVerifierGetValue_EmptyString() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }

    @Test
    public void testVerifierGetValue_NullValue() {
        Verifier verifier = new Verifier(null);
        assertNull(verifier.getValue());
    }
}