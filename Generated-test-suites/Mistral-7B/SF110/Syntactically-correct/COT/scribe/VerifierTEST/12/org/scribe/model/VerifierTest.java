package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validValue = "testVerifier";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructorNullValue() {
        String nullValue = null;
        new Verifier(nullValue);
    }

    @Test
    public void testVerifierGetValue() {
        String value = "testVerifier";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test
    public void testVerifierEmptyValue() {
        String emptyValue = "";
        Verifier verifier = new Verifier(emptyValue);
        assertEquals(emptyValue, verifier.getValue());
    }

    @Test
    public void testVerifierWhitespaceValue() {
        String whitespaceValue = "   ";
        Verifier verifier = new Verifier(whitespaceValue);
        assertEquals(whitespaceValue, verifier.getValue());
    }

    @Test
    public void testVerifierNullGetValue() {
        Verifier verifier = new Verifier("testVerifier");
        assertNull(verifier.getValue());
    }
}