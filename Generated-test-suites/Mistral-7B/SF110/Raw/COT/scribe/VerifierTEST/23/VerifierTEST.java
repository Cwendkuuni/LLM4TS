package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructionWithNullValue() {
        try {
            new Verifier(null);
            fail("Expected Preconditions exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testVerifierConstructionWithValidValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test
    public void testVerifierGetValueWithNullValue() {
        Verifier verifier = new Verifier(null);
        assertNull(verifier.getValue());
    }

    @Test
    public void testVerifierGetValueWithValidValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierGetValueWithNullVerifier() {
        Verifier verifier = null;
        String value = verifier.getValue();
    }

    @Test
    public void testVerifierGetValueWithEmptyValue() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }

    @Test
    public void testVerifierGetValueWithWhitespaceValue() {
        Verifier verifier = new Verifier(" ");
        assertEquals("", verifier.getValue());
    }

    @Test
    public void testVerifierGetValueWithLongValue() {
        String longValue = "a".repeat(1000);
        Verifier verifier = new Verifier(longValue);
        assertEquals(longValue, verifier.getValue());
    }
}