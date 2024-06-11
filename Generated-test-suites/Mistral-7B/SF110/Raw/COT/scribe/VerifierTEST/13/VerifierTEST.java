package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validValue = "abc123";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructorWithNullValue() {
        String nullValue = null;
        Verifier verifier = new Verifier(nullValue);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test
    public void testVerifierGetValue() {
        String validValue = "abc123";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }
}