package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validValue = "validVerifier";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullConstructor() {
        new Verifier(null);
    }

    @Test
    public void testVerifierGetValue() {
        String verifierValue = "testVerifier";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }
}