package org.scribe.model;
import org.junit.Test;
import org.scribe.model.Verifier;
import static org.junit.Assert.*;

public class VerifierTest {
    
    
    @Test
    public void testVerifierValue() {
        String verifierValue = "testVerifierValue";
        Verifier verifier = new Verifier(verifierValue);
        String actualValue = verifier.getValue();
        String expectedValue = verifierValue;
        assertEquals(expectedValue, actualValue);
    }

    
}