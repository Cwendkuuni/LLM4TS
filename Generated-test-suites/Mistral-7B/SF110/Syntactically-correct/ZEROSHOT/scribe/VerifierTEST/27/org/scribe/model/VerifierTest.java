package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierInstance() {
        String verifierValue = "testVerifierValue";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierNullValue() {
        Preconditions mockPreconditions = new Preconditions() {
            @Override
            public void checkNotNull(Object obj, String message) {
                throw new IllegalArgumentException(message);
            }
        };
        Verifier verifier = new Verifier(null);
        // The test should throw an IllegalArgumentException
    }
}