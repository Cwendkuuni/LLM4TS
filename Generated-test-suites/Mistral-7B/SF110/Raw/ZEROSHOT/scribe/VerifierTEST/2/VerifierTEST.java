package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierInstance() {
        String validValue = "test12345";
        Verifier verifier = new Verifier(validValue);
        Assert.assertNotNull(verifier);
        Assert.assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierNullValue() {
        String nullValue = null;
        Verifier verifier = new Verifier(nullValue);
        Assert.fail("Expected IllegalArgumentException but was not thrown");
    }

    @Test
    public void testVerifierGetValue() {
        String validValue = "test12345";
        Verifier verifier = new Verifier(validValue);
        Assert.assertEquals(validValue, verifier.getValue());
    }
}