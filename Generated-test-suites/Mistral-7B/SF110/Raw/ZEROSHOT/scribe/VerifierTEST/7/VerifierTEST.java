package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testVerifierInstance() {
        String verifierValue = "testVerifierValue";
        Verifier verifier = new Verifier(verifierValue);
        String actualValue = verifier.getValue();

        assertEquals(verifierValue, actualValue);
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullValue() {
        Verifier verifier = new Verifier(null);
    }

}