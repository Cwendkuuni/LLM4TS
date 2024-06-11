import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierWithValidValue() {
        String verifierValue = "testValue";
        Verifier verifier = new Verifier(verifierValue);
        String actualValue = verifier.getValue();
        String expectedValue = verifierValue;
        assertEquals(expectedValue, actualValue);
    }

    
}