package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;;

public class VerifierTest {
    
    @Test
    public void testVerifierValue() {
        String verifier = "testVerifier";
        Verifier actualVerifier = new Verifier(verifier);
        String expectedValue = verifier;
        String actualValue = actualVerifier.getValue();
        assertEquals(expectedValue, actualValue);
    }
    
}