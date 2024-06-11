package org.scribe.model;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

import static org.junit.Assert.*;

public class VerifierTest {

    private Verifier verifier;

    @Before
    public void setup() {
        verifier = new Verifier("testVerifier");
    }

    @Test
    public void testVerifierConstructor() {
        assertNotNull(verifier);
        assertEquals("testVerifier", verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierNullConstructor() {
        Preconditions.checkArgument(verifier != null, "Verifier must not be null");
    }

    @Test
    public void testVerifierValueGet() {
        assertEquals("testVerifier", verifier.getValue());
    }
}