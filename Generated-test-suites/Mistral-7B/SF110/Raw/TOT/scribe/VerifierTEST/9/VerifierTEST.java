import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {
    
    
    @Test
    public void testVerifierWithValidValue() {
        String verifier = "testverifier123";
        Verifier verifierObj = new Verifier(verifier);
        String result = verifierObj.getValue();
        assertEquals(verifier, result);
    }

    
}