package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        String verifierValue = "testVerifier";
        Verifier verifier = new Verifier(verifierValue);
        assertNotNull(verifier);
        assertEquals(verifierValue, verifier.getValue());
    }

}