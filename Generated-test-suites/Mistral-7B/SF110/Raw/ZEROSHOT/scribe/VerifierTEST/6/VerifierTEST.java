package org.scribe.model;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        String validVerifier = "testVerifier";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals(validVerifier, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierNullConstruction() {
        String nullVerifier = null;
        new Verifier(nullVerifier);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierEmptyConstruction() {
        String emptyVerifier = "";
        new Verifier(emptyVerifier);
    }

    @Test
    public void testVerifierValueAccess() {
        String validVerifier = "testVerifier";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals(validVerifier, verifier.getValue());
    }
}