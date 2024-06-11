package org.scribe.model;
import org.junit.Test;
import org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testConstructorWithValidValue() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        assertEquals(testValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullValue() {
        String testValue = null;
        Verifier verifier = new Verifier(testValue);
    }

    @Test
    public void testGetValue() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        assertEquals(testValue, verifier.getValue());
    }

    @Test
    public void testSetValue() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        String newValue = "newValue";
        verifier.setValue(newValue);
        assertEquals(newValue, verifier.getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetValueWithImmutableObject() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        try {
            verifier.setValue("newValue");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot modify value of immutable Verifier object", e.getMessage());
        }
    }

    @Test
    public void testClone() {
        String testValue = "testValue";
        Verifier originalVerifier = new Verifier(testValue);
        Verifier clonedVerifier = originalVerifier.clone();
        assertEquals(testValue, clonedVerifier.getValue());
    }

    @Test
    public void testEquals() {
        String testValue = "testValue";
        Verifier verifier1 = new Verifier(testValue);
        Verifier verifier2 = new Verifier(testValue);
        assertTrue(verifier1.equals(verifier2));
        assertFalse(verifier1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        String testValue = "testValue";
        Verifier verifier1 = new Verifier(testValue);
        Verifier verifier2 = new Verifier(testValue);
        assertEquals(verifier1.hashCode(), verifier2.hashCode());
    }

    @Test
    public void testToString() {
        String testValue = "testValue";
        Verifier verifier = new Verifier(testValue);
        String expectedString = "Verifier{value='" + testValue + "'}";
        assertEquals(expectedString, verifier.toString());
    }
}