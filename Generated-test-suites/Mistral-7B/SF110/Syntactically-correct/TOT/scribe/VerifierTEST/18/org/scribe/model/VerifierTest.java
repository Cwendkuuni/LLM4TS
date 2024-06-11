package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;

public class VerifierTest {
    
    
    @Test
    public void testVerifierValue() {
        Verifier verifier = new Verifier("testValue");
        String expectedValue = "testValue";
        String actualValue = verifier.getValue();
        assertEquals(expectedValue, actualValue);
    }
    
    
}