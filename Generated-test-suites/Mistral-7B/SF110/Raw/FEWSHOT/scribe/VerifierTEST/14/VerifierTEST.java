package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testVerifierWithValidString() {
        String value = "myValidVerifier";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierWithNullString() {
        String value = null;
        Verifier verifier = new Verifier(value);
        fail("Expected NullPointerException but got: " + verifier.getValue());
    }

    @Test
    public void testVerifierEquals() {
        String value1 = "myFirstVerifier";
        Verifier verifier1 = new Verifier(value1);

        String value2 = "myFirstVerifier";
        Verifier verifier2 = new Verifier(value2);

        assertEquals(verifier1, verifier2);
        assertEquals(verifier1.hashCode(), verifier2.hashCode());
    }

    @Test
    public void testVerifierNotEquals() {
        String value1 = "myFirstVerifier";
        Verifier verifier1 = new Verifier(value1);

        String value2 = "mySecondVerifier";
        Verifier verifier2 = new Verifier(value2);

        assertNotEquals(verifier1, verifier2);
        assertNotEquals(verifier1.hashCode(), verifier2.hashCode());
    }

    @Test
    public void testToString() {
        String value = "myVerifier";
        Verifier verifier = new Verifier(value);
        String expected = "Verifier [value=" + value + "]";
        assertEquals(expected, verifier.toString());
    }
}