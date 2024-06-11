package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import static org.scribe.utils.Preconditions.*;

public class VerifierTest {

    @Test
    public void testVerifierConstructorWithValidString() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        assertEquals(testValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructorWithNullString() {
        Verifier verifier = new Verifier(null);
    }

    @Test
    public void testVerifierGetValueWithValidValue() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        assertEquals(testValue, verifier.getValue());
    }

    @Test
    public void testVerifierGetValueWithInitiallySetValue() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        Verifier copy = new Verifier(verifier.getValue());
        assertEquals(testValue, copy.getValue());
    }
}