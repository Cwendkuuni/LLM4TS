package org.junit.platform.commons.testing.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validValue = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0-9";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test
    public void testVerifierConstructorEdgeCases() {
        String emptyString = "";
        String nullString = null;
        String invalidValue = "!@#$%^&*()_+-=<>?";

        try {
            new Verifier(emptyString);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new Verifier(nullString);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            new Verifier(invalidValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testgetValue() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        assertEquals(testValue, verifier.getValue());
    }

    @Test
    public void testgetValueEmptyVerifier() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }
}