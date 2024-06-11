import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {
    
    
    @Test
    public void testVerifierValue() {
        String expectedVerifier = "testVerifier";
        Verifier verifier = new Verifier(expectedVerifier);
        String actualVerifier = verifier.getValue();
        assertEquals(expectedVerifier, actualVerifier);
    }

    
}