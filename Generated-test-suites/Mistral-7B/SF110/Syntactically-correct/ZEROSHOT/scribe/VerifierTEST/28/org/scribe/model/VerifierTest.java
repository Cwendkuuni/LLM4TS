package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierInstance() {
        String testValue = "testVerifier";
        Verifier verifier = new Verifier(testValue);
        assertEquals(testValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierNullValue() {
        String nullValue = null;
        Verifier verifier = new Verifier(nullValue);
    }

}