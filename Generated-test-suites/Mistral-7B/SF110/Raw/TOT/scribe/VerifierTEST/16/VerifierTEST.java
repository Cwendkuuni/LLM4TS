package org.scribe.model;

import org.junit.Test;
import org.scribe.utils.Preconditions;

import static org.junit.Assert.assertEquals;

public class VerifierTest {

    @Test
    public void testVerifierValue() {
        String verifier = "abc123";
        Verifier verifierObject = new Verifier(verifier);

        assertEquals("abc123", verifierObject.getValue());
    }
}