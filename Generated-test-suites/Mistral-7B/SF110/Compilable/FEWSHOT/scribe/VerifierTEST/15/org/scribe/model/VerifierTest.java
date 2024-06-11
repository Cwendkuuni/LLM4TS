package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        String validString = "validVerifier";
        Verifier verifier = new Verifier(validString);
        assertEquals(validString, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullInput() {
        String nullInput = null;
        Verifier verifier = new Verifier(nullInput);
    }
}