package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testGetValue() {
        String expectedValue = "expected_value";
        Verifier verifier = new Verifier(expectedValue);
        String actualValue = verifier.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetValue_null() {
        // Test with null object
        Verifier verifier = new Verifier(null);
        assertNull(verifier.getValue());
    }

    @Test
    public void testGetValue_emptyString() {
        // Test with empty string
        String emptyString = "";
        Verifier verifier = new Verifier(emptyString);
        assertEquals(emptyString, verifier.getValue());
    }
}