package org.scribe.model.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validValue = "validVerifier";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructorWithNullValue() {
        String nullValue = null;
        new Verifier(nullValue);
    }

    @Test
    public void testGetValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test
    public void testGetValueWhenVerifierIsEmpty() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueWithNullVerifier() {
        Verifier verifier = null;
        String value = verifier.getValue();
    }

    @Test
    public void testGetValueWithLongVerifier() {
        String longValue = "longlonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglong";
        Verifier verifier = new Verifier(longValue);
        assertEquals(longValue, verifier.getValue());
    }

    @Test
    public void testGetValueWithSpecialCharacters() {
        String specialValue = "@#$%^&*()_+-={}[]|\\:\"<>\";'`~!";
        Verifier verifier = new Verifier(specialValue);
        assertEquals(specialValue, verifier.getValue());
    }
}