package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierInstance() {
        String verifierValue = "testVerifier";
        Verifier verifier = new Verifier(verifierValue);
       assertEquals(verifierValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullValue() {
        String verifierValue = null;
        try {
            new Verifier(verifierValue);
        } catch (NullPointerException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
            throw e;
        }
    }
}