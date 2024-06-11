package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierWithNullValue() {
        try {
            new Verifier(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testVerifierWithValidValue() {
        String value = "abc123";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test
    public void testVerifierWithEmptyValue() {
        String value = "";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test
    public void testVerifierWithWhiteSpaceValue() {
        String value = "   ";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test
    public void testVerifierWithLongValue() {
        String value = "1234567890123456789012345678901234567890123456789012345678901234567890";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}