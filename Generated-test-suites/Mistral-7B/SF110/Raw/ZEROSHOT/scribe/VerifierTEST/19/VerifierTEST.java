package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierInstanceCreation() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        assertEquals(testValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullValueException() {
        String nullValue = null;
        Verifier verifier = new Verifier(nullValue);
    }
}