package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierConstructionWithValidString() {
        String verifierValue = "testVerifierValue";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructionWithNullString() {
        String verifierValue = null;
        Verifier verifier = new Verifier(verifierValue);
    }

}