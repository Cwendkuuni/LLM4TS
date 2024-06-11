package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String value = "valid_string";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructorWithNullValue() {
        new Verifier(null);
    }

    @Test
    public void testVerifierGetValue() {
        Verifier verifier = new Verifier("test_value");
        assertEquals("test_value", verifier.getValue());
    }
}