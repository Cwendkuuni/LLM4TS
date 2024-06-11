package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierInstance() {
        String testValue = "testVerifierValue";
        Verifier verifier = new Verifier(testValue);
        assertEquals(testValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierNullValue() {
        String nullValue = null;
        Verifier verifier = new Verifier(nullValue);
        fail("An IllegalArgumentException was expected but not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierEmptyValue() {
        String emptyValue = "";
        Verifier verifier = new Verifier(emptyValue);
        fail("An IllegalArgumentException was expected but not thrown.");
    }

    @Test
    public void testVerifierValueGetting() {
        String testValue = "testVerifierValue";
        Verifier verifier = new Verifier(testValue);
        assertEquals(testValue, verifier.getValue());
    }
}