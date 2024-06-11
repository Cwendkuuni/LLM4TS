package org.scribe.model;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;//for assertX methods

public class VerifierTest {

    @Test
    public void testVerifierWithValidString() {
        String validVerifier = "abcdefg12345";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals(validVerifier, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierWithNullString() {
        String nullVerifier = null;
        try {
            new Verifier(nullVerifier);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            //expected exception
        }
    }

    @Test
    public void testVerifierGetValue() {
        String validVerifier = "abcdefg12345";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals(validVerifier, verifier.getValue());
    }
}