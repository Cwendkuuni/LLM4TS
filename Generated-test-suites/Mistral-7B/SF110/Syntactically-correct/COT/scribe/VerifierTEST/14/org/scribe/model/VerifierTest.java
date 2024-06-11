import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructorWithValidString() {
        String validVerifier = "validVerifier";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals("validVerifier", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructorWithNullString() {
        String nullVerifier = null;
        new Verifier(nullVerifier);
    }

    @Test
    public void testVerifierGetValueWithValidInstance() {
        String validVerifier = "validVerifier";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals("validVerifier", verifier.getValue());
    }

    @Test
    public void testVerifierGetValueWithEmptyStringInstance() {
        String emptyVerifier = "";
        Verifier verifier = new Verifier(emptyVerifier);
        assertEquals("", verifier.getValue());
    }

    @Test
    public void testVerifierGetValueWithWhitespaceStringInstance() {
        String whitespaceVerifier = "   ";
        Verifier verifier = new Verifier(whitespaceVerifier);
        assertEquals("", verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierGetValueWithNullValueInstance() {
        Verifier verifier = new Verifier(null);
        Preconditions.checkNotNull(verifier.getValue(), "Verifier value should not be null");
    }
}