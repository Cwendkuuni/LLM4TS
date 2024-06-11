package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierInstanceWithValidString() {
        String verifierValue = "testVerifierValue";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierInstanceWithNullString() {
        String verifierValue = null;
        new Verifier(verifierValue);
    }

}