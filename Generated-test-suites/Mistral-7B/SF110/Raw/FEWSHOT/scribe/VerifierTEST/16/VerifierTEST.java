package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        Verifier verifier = new Verifier("testVerifier");
        assertEquals("testVerifier", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullConstruction() {
        new Verifier(null);
    }

    @Test
    public void testVerifierGetValue() {
        Verifier verifier = new Verifier("testVerifier");
        assertEquals("testVerifier", verifier.getValue());
    }
}