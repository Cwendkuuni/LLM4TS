package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validVerifier = "valid_verifier";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals(validVerifier, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructorWithNullValue() {
        String nullValue = null;
        Verifier verifier = new Verifier(nullValue);
        fail("Expected IllegalArgumentException, but was not thrown");
    }

    @Test
    public void testGetValue() {
        String verifierValue = "test_verifier_value";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }

    @Test
    public void testEmptyStringAsVerifier() {
        String emptyVerifier = "";
        Verifier verifier = new Verifier(emptyVerifier);
        assertEquals(emptyVerifier, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierWithEmptyString() {
        String emptyVerifier = "";
        Verifier verifier = new Verifier(emptyVerifier);
        fail("Expected IllegalArgumentException, but was not thrown");
    }

    @Test
    public void testVerifierWithWhitespaceString() {
        String whitespaceVerifier = "   ";
        Verifier verifier = new Verifier(whitespaceVerifier);
        assertEquals(whitespaceVerifier, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierWithWhitespaceOnlyString() {
        String whitespaceVerifier = "   ";
        Verifier verifier = new Verifier(whitespaceVerifier);
        fail("Expected IllegalArgumentException, but was not thrown");
    }
}