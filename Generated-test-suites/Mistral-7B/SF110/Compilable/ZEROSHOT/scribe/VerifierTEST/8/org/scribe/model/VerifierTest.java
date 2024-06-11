package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructionWithValidString() {
        String verifierValue = "testVerifierValue";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructionWithNullString() {
        String verifierValue = null;
        Verifier verifier = new Verifier(verifierValue);
        fail("Expected an IllegalArgumentException but was not thrown.");
    }

    @Test
    public void testVerifierGetValue() {
        String verifierValue = "testVerifierValue";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }
}