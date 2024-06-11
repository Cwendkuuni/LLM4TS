package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        String validValue = "ValidVerifier";
        Verifier verifier = new Verifier(validValue);
        assertNotNull(verifier);
        assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullConstruction() {
        new Verifier(null);
    }
}