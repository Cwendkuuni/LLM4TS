package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validVerifier = "abc123";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals("Expected valid verifier", validVerifier, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullConstructor() {
        String nullValue = null;
        new Verifier(nullValue); // Should throw NullPointerException
    }

    @Test
    public void testVerifierGetter() {
        String validVerifier = "abc123";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals("Expected valid verifier", validVerifier, verifier.getValue());
    }

    @Test
    public void testEmptyVerifier() {
        String emptyVerifier = "";
        Verifier verifier = new Verifier(emptyVerifier);
        assertEquals("Expected empty verifier", emptyVerifier, verifier.getValue());
    }

    @Test
    public void testWhitespaceVerifier() {
        String whitespaceVerifier = "   ";
        Verifier verifier = new Verifier(whitespaceVerifier);
        assertEquals("Expected whitespace verifier", whitespaceVerifier, verifier.getValue());
    }

    @Test
    public void testLongVerifier() {
        String longVerifier = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Verifier verifier = new Verifier(longVerifier);
        assertEquals("Expected long verifier", longVerifier, verifier.getValue());
    }
}