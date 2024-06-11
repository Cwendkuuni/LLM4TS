import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierConstructionWithValidString() {
        String testValue = "valid_verifier";
        Verifier verifier = new Verifier(testValue);
        String actualValue = verifier.getValue();
        String expectedValue = testValue;

        assertEquals(expectedValue, actualValue);
    }

    
}