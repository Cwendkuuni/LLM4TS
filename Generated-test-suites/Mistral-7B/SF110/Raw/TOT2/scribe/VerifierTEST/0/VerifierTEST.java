import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {
    @Test
    public void testVerifierConstructor() {
        String validValue = "1234567890123456";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test
    public void testGetterMethod() {
        String validValue = "1234567890123456";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test
    public void testEmptyStringVerifier() {
        assertThrows(NullPointerException.class, () -> new Verifier(""));
    }

    @Test
    public void testNullStringVerifier() {
        assertThrows(NullPointerException.class, () -> new Verifier(null));
    }

    @Test
    public void testLongStringVerifier() {
        String longString = "1234567890123456789012345678901234567890";
        Verifier verifier = new Verifier(longString);
        assertEquals(longString, verifier.getValue());
    }
}