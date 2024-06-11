package org.scribe.model.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validVerifier = "validVerifier";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals(validVerifier, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructorWithNullArgument() {
        String nullVerifier = null;
        Verifier verifier = new Verifier(nullVerifier);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructorWithEmptyString() {
        String emptyString = "";
        Verifier verifier = new Verifier(emptyString);
        Preconditions.checkArgument(verifier.getValue().length() > 0, "Verifier cannot be an empty string.");
    }

    @Test
    public void testVerifierGetValue() {
        String verifierValue = "testVerifier";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }
}