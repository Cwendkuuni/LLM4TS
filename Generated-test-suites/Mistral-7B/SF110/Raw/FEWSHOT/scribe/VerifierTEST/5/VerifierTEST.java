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
    public void testVerifierConstructorNullValue() {
        new Verifier(null);
    }

    @Test
    public void testVerifierGetValue() {
        String validValue = "validVerifier";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }
}