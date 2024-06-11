package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierConstructorWithValidString() {
        String verifierValue = "testVerifierValue";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructorWithNullString() {
        String verifierValue = null;
        new Verifier(verifierValue);
    }

    @Test
    public void testVerifierGetValue() {
        String verifierValue = "testVerifierValue";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }
}