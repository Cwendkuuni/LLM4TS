package org.scribe.model.test;

import static org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validValue = "validValue";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierNullConstructor() {
        String nullValue = null;
        Verifier verifier = new Verifier(nullValue);
        fail("Expected IllegalArgumentException but got: " + verifier.getValue());
    }

    @Test
    public void testVerifierValueGet() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test
    public void testEmptyVerifier() {
        String emptyValue = "";
        Verifier verifier = new Verifier(emptyValue);
        assertEquals(emptyValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierWithWhiteSpace() {
        String whitespaceValue = "   ";
        Verifier verifier = new Verifier(whitespaceValue);
        fail("Expected IllegalArgumentException but got: " + verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierWithSpecialCharacters() {
        String specialValue = "!@#$%^&*()_+={}[]|\\:\"<>?,./;'\\`-";
        Verifier verifier = new Verifier(specialValue);
        fail("Expected IllegalArgumentException but got: " + verifier.getValue());
    }

    @Test
    public void testVerifierLongValue() {
        String longValue = "long long value";
        Verifier verifier = new Verifier(longValue);
        assertEquals(longValue, verifier.getValue());
    }
}