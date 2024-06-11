package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;;

public class VerifierTest {
    
    
    @Test
    public void testVerifierValue() {
        String verifier = "testVerifier";
        Verifier v = new Verifier(verifier);
        String valueFromVerifier = v.getValue();
        assertEquals(verifier, valueFromVerifier);
    }
    
    
}