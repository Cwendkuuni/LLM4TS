package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierInstanceCreation() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        Assert.assertNotNull(verifier);
        Assert.assertEquals(testValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierNullValue() {
        String testValue = null;
        Verifier verifier = new Verifier(testValue);
        Assert.fail("Expected an IllegalArgumentException, but no exception was thrown");
    }

    @Test
    public void testVerifierGetValue() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        Assert.assertEquals(testValue, verifier.getValue());
    }
}