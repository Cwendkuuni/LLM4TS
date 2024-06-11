package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierInstanceCreation() {
        String validVerifier = "1234567890";
        Verifier verifier = new Verifier(validVerifier);
        assertNotNull(verifier);
        assertEquals(validVerifier, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierInstanceCreationWithNullValue() {
        String nullValue = null;
        new Verifier(nullValue);
    }

    @Test
    public void testVerifierValueGetMethod() {
        String validVerifier = "1234567890";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals(validVerifier, verifier.getValue());
    }
}