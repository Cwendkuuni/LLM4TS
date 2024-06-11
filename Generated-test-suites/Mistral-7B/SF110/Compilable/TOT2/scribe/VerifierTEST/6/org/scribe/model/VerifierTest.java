package org.scribe.model;
import org.junit.Test;
import org.scribe.model.Verifier;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        Verifier verifier = new Verifier("valid string");
        String expectedValue = "valid string";
        String actualValue = verifier.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetterValue() {
        Verifier verifier = new Verifier("valid string");
        String expectedValue = "valid string";
        String actualValue = verifier.getValue();
        assertEquals(expectedValue, actualValue);
    }
}