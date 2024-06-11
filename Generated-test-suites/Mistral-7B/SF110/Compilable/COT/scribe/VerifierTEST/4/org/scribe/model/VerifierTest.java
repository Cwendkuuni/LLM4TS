package org.scribe.model.test;

import static org.junit.Assert.*;

import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        String validValue = "testVerifier";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierNullValue() {
        String nullValue = null;
        Verifier verifier = new Verifier(nullValue);
        fail("Expected IllegalArgumentException but got: " + verifier);
    }

    @Test
    public void testVerifierEmptyValue() {
        String emptyValue = "";
        Verifier verifier = new Verifier(emptyValue);
        assertEquals(emptyValue, verifier.getValue());
    }

    @Test
    public void testVerifierWhitespaceValue() {
        String whitespaceValue = "   ";
        Verifier verifier = new Verifier(whitespaceValue);
        assertEquals(whitespaceValue, verifier.getValue());
    }

    @Test
    public void testVerifierSpecialCharacterValue() {
        String specialValue = "!@#$%^&*()_+-=[]{};:<>|.\",/?";
        Verifier verifier = new Verifier(specialValue);
        assertEquals(specialValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierLongValue() {
        String longValue = "1234567890123456789012345678901234567890123456789012345678901234567890";
        Verifier verifier = new Verifier(longValue);
        fail("Expected IllegalArgumentException but got: " + verifier);
    }
}