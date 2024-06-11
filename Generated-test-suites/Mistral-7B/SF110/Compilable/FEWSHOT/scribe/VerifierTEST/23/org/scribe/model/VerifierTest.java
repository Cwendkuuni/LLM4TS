package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        String value = "abc123";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullConstruction() {
        new Verifier(null);
    }

    @Test
    public void testVerifierGetValue() {
        String value = "abc123";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}