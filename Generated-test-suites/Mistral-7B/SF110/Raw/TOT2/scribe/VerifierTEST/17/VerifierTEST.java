import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testConstructor() {
        String verifierValue = "abc123";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("abc123");
        assertEquals("abc123", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullValue() {
        new Verifier(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyString() {
        new Verifier("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithWhitespaceString() {
        new Verifier("   ");
    }

    @Test
    public void testGetValueWithEmptyVerifier() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }

    @Test
    public void testGetValueWithNullVerifier() {
        Verifier verifier = new Verifier(null);
        assertNull(verifier.getValue());
    }

    @Test
    public void testGetValueWithSpacesVerifier() {
        Verifier verifier = new Verifier("   ");
        assertEquals("   ", verifier.getValue());
    }

    @Test
    public void testGetValueWithValidVerifier() {
        Verifier verifier = new Verifier("abc123");
        assertEquals("abc123", verifier.getValue());
    }

    @Test
    public void testGetValueWithLongVerifier() {
        String longVerifierValue = "abc123def456ghij789klmnopqrstuvwxyz";
        Verifier verifier = new Verifier(longVerifierValue);
        assertEquals(longVerifierValue, verifier.getValue());
    }

    @Test
    public void testGetValueWithSpecialCharactersVerifier() {
        Verifier verifier = new Verifier("@#$%&*()_+-=[]{}|;:,.<>/?");
        assertEquals("@#$%&*()_+-=[]{}|;:,.<>/?", verifier.getValue());
    }

}