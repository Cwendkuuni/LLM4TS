import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        String validValue = "validValue";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructionWithNullValue() {
        String nullValue = null;
        new Verifier(nullValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructionWithEmptyString() {
        String emptyString = "";
        new Verifier(emptyString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructionWithWhitespaceString() {
        String whitespaceString = "   ";
        new Verifier(whitespaceString);
    }

    @Test
    public void testGetValue() {
        String verifierValue = "verifierValue";
        Verifier verifier = new Verifier(verifierValue);
        String result = verifier.getValue();
        assertEquals(verifierValue, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetValueWithNullVerifier() {
        Verifier nullVerifier = null;
        String result = nullVerifier.getValue();
    }

    @Test
    public void testGetValueAfterChange() {
        String verifierValue = "verifierValue";
        Verifier verifier = new Verifier(verifierValue);
        verifier.value = "changedValue";
        String result = verifier.getValue();
        assertEquals(verifierValue, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetValueWithNullResult() {
        Verifier verifier = new Verifier("verifierValue");
        String nullResult = null;
        assertThrows(NullPointerException.class, () -> verifier.getValue(nullResult));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueWithEmptyStringResult() {
        Verifier verifier = new Verifier("verifierValue");
        String emptyResult = "";
        assertThrows(IllegalArgumentException.class, () -> verifier.getValue(emptyResult));
    }
}