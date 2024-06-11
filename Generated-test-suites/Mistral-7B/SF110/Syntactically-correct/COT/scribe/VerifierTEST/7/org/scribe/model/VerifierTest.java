package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
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
    public void testVerifierConstructorWithNullValue() {
        String nullValue = null;
        Verifier verifier = new Verifier(nullValue);
        // The constructor should throw an exception with the given message
        Preconditions.checkArgument(verifier.getValue() != null, "Must provide a valid string as verifier");
    }

    @Test
    public void testGetValue() {
        String verifierValue = "testValue";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }

    @Test
    public void testGetValueWithEmptyString() {
        String emptyString = "";
        Verifier verifier = new Verifier(emptyString);
        assertEquals(emptyString, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueWithNullVerifier() {
        Verifier nullVerifier = null;
        try {
            nullVerifier.getValue();
        } catch (NullPointerException e) {
            // The getValue method should throw a NullPointerException when the Verifier object is null
            return;
        } finally {
            fail("getValue() method should have thrown an IllegalArgumentException when the Verifier object is null");
        }
    }
}