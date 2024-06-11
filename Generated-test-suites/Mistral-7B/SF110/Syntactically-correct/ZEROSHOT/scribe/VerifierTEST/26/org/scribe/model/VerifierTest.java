package org.scribe.model.test;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

import static org.junit.Assert.*;

public class VerifierTest {

    private Verifier verifier;

    @Before
    public void setUp() {
        String validValue = "validVerifier";
        verifier = new Verifier(validValue);
    }

    @Test
    public void testVerifierWithValidValue() {
        assertEquals(verifier.getValue(), "validVerifier");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierWithNullValue() {
        String nullValue = null;
        Preconditions check = new Preconditions();
        check.checkNotNull(nullValue, "Must provide a valid string as verifier");
    }

    @Test
    public void testVerifierGetValue() {
        assertEquals(verifier.getValue(), "validVerifier");
    }
}