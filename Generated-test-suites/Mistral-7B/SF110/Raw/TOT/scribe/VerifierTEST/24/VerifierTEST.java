
import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {
    @Test
    public void testNullVerifier() {
        try {
            new Verifier(null);
            fail("Expected NullPointerException due to null value");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyVerifier() {
        String verifierValue = "";
        try {
            new Verifier(verifierValue);
            fail("Expected IllegalArgumentException due to empty string value");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testVerifierValue() {
        String verifierValue = "testVerifierValue";
        Verifier verifier = new Verifier(verifierValue);
        String actualValue = verifier.getValue();
        String expectedValue = verifierValue;
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testLongVerifierValue() {
        String verifierValue = "This is a very long verifier value that tests the limit of the Verifier class";
        Verifier verifier = new Verifier(verifierValue);
        String actualValue = verifier.getValue();
        String expectedValue = verifierValue;
        assertEquals(expectedValue, actualValue);
    }
}