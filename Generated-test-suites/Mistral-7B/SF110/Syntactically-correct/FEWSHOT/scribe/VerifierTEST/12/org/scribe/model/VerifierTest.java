package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        Verifier verifier1 = new Verifier("value1");
        Verifier verifier2 = new Verifier("value2");

        assertEquals("value1", verifier1.getValue());
        assertEquals("value2", verifier2.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullConstruction() {
        new Verifier(null);
    }
}