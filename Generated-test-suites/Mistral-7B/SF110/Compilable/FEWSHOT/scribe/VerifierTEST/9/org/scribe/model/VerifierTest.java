package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        String validValue = "validValue";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullValue() {
        new Verifier(null);
    }
}