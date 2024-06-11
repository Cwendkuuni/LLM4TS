package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierInstance() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        assertEquals(testValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullValue() {
        Preconditions.checkNotNull(new Verifier(null), "Must not create Verifier with null value");
        fail("Expected NullPointerException when creating Verifier with null value");
    }
}