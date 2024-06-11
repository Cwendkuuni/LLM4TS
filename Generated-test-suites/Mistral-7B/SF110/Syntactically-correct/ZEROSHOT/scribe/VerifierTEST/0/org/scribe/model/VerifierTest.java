package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import static org.scribe.utils.Preconditions.checkNotNull;

public class VerifierTest {

    @Test
    public void testVerifierInstanceCreation() {
        String validVerifier = "testVerifier";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals(validVerifier, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierNullValue() {
        String nullValue = null;
        try {
            new Verifier(nullValue);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
        }
    }

    @Test
    public void testVerifierEmptyValue() {
        String emptyValue = "";
        Verifier verifier = new Verifier(emptyValue);
        assertEquals(emptyValue, verifier.getValue());
        assertFalse(verifier.getValue().isEmpty());
    }

    @Test
    public void testVerifierValueLength() {
        String longValue = "12345678901234567890123456789012345678901234567890123456789012345678901234567890";
        Verifier verifier = new Verifier(longValue);
        assertEquals(longValue, verifier.getValue());
        assertTrue(verifier.getValue().length() > 0);
    }

    @Test
    public void testVerifierValueEquals() {
        String value1 = "testVerifier1";
        String value2 = "testVerifier2";
        Verifier verifier1 = new Verifier(value1);
        Verifier verifier2 = new Verifier(value2);
        assertNotSame(verifier1, verifier2);
        assertFalse(verifier1.equals(verifier2));
        assertFalse(verifier1.getValue().equals(verifier2.getValue()));
    }

    @Test
    public void testVerifierValueEqualsSelf() {
        String value = "testVerifier";
        Verifier verifier = new Verifier(value);
        assertTrue(verifier.equals(verifier));
        assertTrue(verifier.getValue().equals(verifier.getValue()));
    }

    @Test
    public void testVerifierValueHashCode() {
        String value1 = "testVerifier1";
        String value2 = "testVerifier2";
        Verifier verifier1 = new Verifier(value1);
        Verifier verifier2 = new Verifier(value2);
        int hashCode1 = verifier1.hashCode();
        int hashCode2 = verifier2.hashCode();
        assertNotEquals(hashCode1, hashCode2);
        assertEquals(value1.hashCode(), hashCode1);
        assertEquals(value2.hashCode(), hashCode2);
    }
}