package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

@RunWith(org.junit.runner.JUnit4.class)
public class VerifierTest {

    @Test
    public void testVerifierConstructionWithNullValueThrowsException() {
        try {
            new Verifier(null);
            fail("Expected Preconditions exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }

    @Test
    public void testVerifierConstructionWithValidValue() {
        String testValue = "testVerifierValue";
        Verifier verifier = new Verifier(testValue);
        assertEquals(testValue, verifier.getValue());
    }
}