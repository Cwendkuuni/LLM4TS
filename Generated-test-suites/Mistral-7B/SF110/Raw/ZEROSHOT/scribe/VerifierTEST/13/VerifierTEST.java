package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        String validValue = "testVerifierValue";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierNullValue() {
        String nullValue = null;
        new Verifier(nullValue);
    }

    @Test
    public void testVerifierGetValue() {
        String validValue = "testVerifierValue";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test
    public void testVerifierEmptyValue() {
        String emptyValue = "";
        Verifier verifier = new Verifier(emptyValue);
        assertEquals(emptyValue, verifier.getValue());
    }

    @Test
    public void testVerifierPreconditionsCheck() {
        String nullValue = null;
        try {
            new Verifier(nullValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String emptyValue = "";
        try {
            new Verifier(emptyValue);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception for empty string");
        }
    }
}