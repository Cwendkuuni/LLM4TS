import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {
    
    
    @Test
    public void testVerifierWithValidString() {
        String validString = "validVerifier123";
        Verifier verifier = new Verifier(validString);
        String actualValue = verifier.getValue();
        String expectedValue = validString;
        assertEquals(expectedValue, actualValue);
    }

    
}