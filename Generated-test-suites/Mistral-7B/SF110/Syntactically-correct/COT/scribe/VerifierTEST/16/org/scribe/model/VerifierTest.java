import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validValue = "validVerifier";
        Verifier verifier = new Verifier(validValue);
        Assert.assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructorWithNullValue() {
        String nullValue = null;
        Verifier verifier = new Verifier(nullValue);
        // JUnit will throw exception if the expected exception is not thrown
    }

    @Test
    public void testVerifierGetValue() {
        String validValue = "validVerifier";
        Verifier verifier = new Verifier(validValue);
        String returnedValue = verifier.getValue();
        Assert.assertEquals(validValue, returnedValue);
    }

    @Test
    public void testVerifierGetValueWithEmptyString() {
        String emptyString = "";
        Verifier verifier = new Verifier(emptyString);
        String returnedValue = verifier.getValue();
        Assert.assertEquals(emptyString, returnedValue);
    }
}