package org.scribe.model;

import static org.junit.Assert.*;

import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructionWithNullValue() {
        try {
            new Verifier(null);
            fail("Expected Preconditions exception");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testVerifierGetValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}