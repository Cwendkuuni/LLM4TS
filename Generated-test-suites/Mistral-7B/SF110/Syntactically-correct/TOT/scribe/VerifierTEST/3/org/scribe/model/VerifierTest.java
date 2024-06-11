// VerifierTest.java (Expert 1) - Using only basic test cases
package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {
    @Test
    public void testVerifierValue() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }

    @Test
    public void testVerifierNullValue() {
        try {
            new Verifier(null);
            fail("Expected Preconditions exceptions.checkNotNull() to be thrown.");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}