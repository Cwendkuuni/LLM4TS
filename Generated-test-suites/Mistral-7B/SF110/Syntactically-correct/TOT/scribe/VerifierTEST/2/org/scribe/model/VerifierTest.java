
import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import static org.scribe.utils.Preconditions.*;

public class VerifierTest {
    @Test
    public void testConstructorValidValue() {
        String validValue = "validValue";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullValue() {
        String nullValue = null;
        try {
            new Verifier(nullValue);
        } catch (NullPointerException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testGetValueSameValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test
    public void testGetValueEmptyString() {
        String emptyString = "";
        Verifier verifier = new Verifier(emptyString);
        assertEquals(emptyString, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueNullValue() {
        Verifier verifier = new Verifier(null);
        try {
            verifier.getValue();
        } catch (IllegalArgumentException e) {
            assertEquals("Must provide a valid string as verifier", e.getMessage());
            throw e;
        }
    }
}