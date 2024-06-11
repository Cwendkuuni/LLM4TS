import static org.junit.Assert.*;
import org.junit.Test;

public class VerifierTest {
    @Test
    public void testVerifierConstruction() {
        String validValue = "validVerifier";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test
    public void testGetValue() {
        String expectedValue = "expectedValue";
        Verifier verifier = new Verifier(expectedValue);
        String actualValue = verifier.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructionWithNullValue() {
        String nullValue = null;
        new Verifier(nullValue);
    }

    @Test
    public void testVerifierConstructionWithEmptyString() {
        String emptyString = "";
        Verifier verifier = new Verifier(emptyString);
        assertEquals("", verifier.getValue());
    }
}