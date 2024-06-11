package org.example.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validValue = "validValue";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test
    public void testGetValue() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        assertEquals(testValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructorNullValue() {
        String nullValue = null;
        new Verifier(nullValue);
    }

    @Test public void testVerifierConstructorEmptyValue() {
        String emptyValue = "";
        Verifier verifier = new Verifier(emptyValue);
        assertEquals(emptyValue, verifier.getValue());
    }

    @Test public void testVerifierConstructorLongValue() {
        String longValue = "a".repeat(1000);
        Verifier verifier = new Verifier(longValue);
        assertEquals(longValue, verifier.getValue());
    }

}