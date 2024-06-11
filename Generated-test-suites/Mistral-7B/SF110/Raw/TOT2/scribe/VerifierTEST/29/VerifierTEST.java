package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {
    @Test
    public void testVerifierNullValue() {
        String verifierValue = null;
        try {
            new Verifier(verifierValue);
            fail("Expected Preconditions exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        assertNull(new Verifier(verifierValue));
    }

    @Test
    public void testVerifierEmptyValue() {
        String verifierValue = "";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals("", verifier.getValue());
    }

    @Test
    public void testVerifierTrimmedValue() {
        String verifierValue = "   testValue   ";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals("testValue", verifier.getValue());
    }

    @Test
    public void testVerifierValue() {
        String verifierValue = "testValue";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }
}