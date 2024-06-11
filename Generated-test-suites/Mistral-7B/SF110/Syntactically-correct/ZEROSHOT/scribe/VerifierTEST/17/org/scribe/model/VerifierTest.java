package org.scribe.model.test;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

import static org.junit.Assert.*;

public class VerifierTest {

    private Verifier verifier;

    @Before
    public void setUp() {
        String validValue = "validVerifierValue";
        verifier = new Verifier(validValue);
    }

    @Test
    public void testVerifierConstructor() {
        assertNotNull(verifier);
        assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructorWithNullValue() {
        String nullValue = null;
        Verifier nullVerifier = new Verifier(nullValue);
        fail("Expected an IllegalArgumentException when constructing Verifier with null value");
    }

    @Test
    public void testVerifiergetValue() {
        assertEquals(validValue, verifier.getValue());
    }

    @Test
    public void testVerifiergetterValueWhenValueIsModified() {
        String newValue = "newVerifierValue";
        verifier.setValue(newValue);
        assertEquals(newValue, verifier.getValue());
    }
}