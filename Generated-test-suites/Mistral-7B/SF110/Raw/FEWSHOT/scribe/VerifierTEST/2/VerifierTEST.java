package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String value = "somevalue";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullConstructor() {
        new Verifier(null);
    }
}