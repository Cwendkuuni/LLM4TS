package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validVerifier = "validVerifier";
        Verifier verifier = new Verifier(validVerifier);
        Assert.assertEquals(validVerifier, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructorWithNullValue() {
        String nullValue = null;
        Verifier verifier = new Verifier(nullValue);
        // The constructor should throw an IllegalArgumentException
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        String returnedValue = verifier.getValue();
        Assert.assertEquals("testValue", returnedValue);
    }
}