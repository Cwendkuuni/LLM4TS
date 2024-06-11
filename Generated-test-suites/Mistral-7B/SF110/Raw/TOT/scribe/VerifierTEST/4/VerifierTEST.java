package org.scribe.model;
import org.junit.Test;
import org.scribe.utils.Preconditions;
import static org.junit.Assert.*;;

public class VerifierTest {
    
    @Test
    public void testVerifierConstructor() {
        String validVerifier = "testVerifier";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals(validVerifier, verifier.getValue());
    }

    
}