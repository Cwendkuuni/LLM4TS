import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {
    @Test
    public void testVerifierConstructor() {
        String validValue = "testValue";
        Verifier verifier = new Verifier(validValue);
        String returnedValue = verifier.getValue();
        assertEquals(validValue, returnedValue);
    }

    
    @Test
    public void testVerifierNullValue() {
        try {
            new Verifier(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testVerifierEmptyValue() {
        String emptyValue = "";
        Verifier verifier = new Verifier(emptyValue);
        String returnedValue = verifier.getValue();
        assertEquals(emptyValue, returnedValue);
    }

    @Test
    public void testVerifierWhiteSpaceValue() {
        String whiteSpaceValue = "   ";
        Verifier verifier = new Verifier(whiteSpaceValue);
        String returnedValue = verifier.getValue();
        assertEquals(whiteSpaceValue, returnedValue.trim());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierLongValue() {
        String longValue = "X".repeat(1000);
        new Verifier(longValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierValueWithSpecialCharacters() {
        String specialCharactersValue = "@#$%^&*()_+-={}[]|\\:\"'<>,.?/";
        new Verifier(specialCharactersValue);
    }
    
}