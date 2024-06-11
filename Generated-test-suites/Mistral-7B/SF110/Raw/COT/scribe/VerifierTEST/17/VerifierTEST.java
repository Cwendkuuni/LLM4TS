import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validValue = "validVerifier";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructorWithNullValue() {
        String nullValue = null;
        Verifier verifier = new Verifier(nullValue);
        fail("Expected IllegalArgumentException, but got: " + verifier.getValue());
    }

    @Test
    public void testVerifierGetValue() {
        String verifierValue = "verifierValue";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }

    @Test
    public void testVerifierEmptyValue() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }

    @Test
    public void testVerifierWhiteSpaceValue() {
        Verifier verifier = new Verifier("   ");
        assertEquals("", verifier.getValue());
    }

    @Test
    public void testVerifierNullValueInGetValue() {
        Verifier verifier = new Verifier(null);
        assertNull(verifier.getValue());
    }
}