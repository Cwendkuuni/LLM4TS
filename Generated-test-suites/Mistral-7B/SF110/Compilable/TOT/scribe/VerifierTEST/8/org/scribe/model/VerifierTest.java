package org.scribe.model;
import org.junit.Test;
import org.scribe.model.Verifier;
import static org.junit.Assert.*;

public class VerifierTest {
    
    @Test
    public void testVerifierValue() {
        Verifier verifier = new Verifier("testVerifierValue");
        String actualValue = verifier.getValue();
        String expectedValue = "testVerifierValue";
        assertEquals(expectedValue, actualValue);
    }
    
}