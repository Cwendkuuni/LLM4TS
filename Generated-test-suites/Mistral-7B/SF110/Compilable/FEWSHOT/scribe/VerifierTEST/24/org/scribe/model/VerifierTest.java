package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        Verifier verifier = new Verifier("abc123");
        assertEquals("abc123", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullConstruction() {
        new Verifier(null);
    }

    @Test
    public void testVerifierGetValue() {
        Verifier verifier = new Verifier("xyz456");
        assertEquals("xyz456", verifier.getValue());
    }
}