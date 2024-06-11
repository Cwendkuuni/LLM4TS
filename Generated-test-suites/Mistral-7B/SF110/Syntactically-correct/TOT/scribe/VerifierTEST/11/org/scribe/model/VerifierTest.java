package org.scribe.model.test;

import org.junit.Test;
import org.scribe.model.Verifier;
import static org.junit.Assert.*;

public class VerifierTest {
    
    @Test
    public void testVerifierWithValidValue() {
        Verifier verifier = new Verifier("abcdef12345");
        String actualValue = verifier.getValue();
        String expectedValue = "abcdef12345";
        assertEquals(expectedValue, actualValue);
    }
    
}