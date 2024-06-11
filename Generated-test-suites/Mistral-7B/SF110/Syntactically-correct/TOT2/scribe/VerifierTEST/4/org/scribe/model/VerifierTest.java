import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String value = "abc123";
        Verifier verifier = new Verifier(value);
        String expectedValue = value;
        String actualValue = verifier.getValue();
        Assert.assertEquals(expectedValue, actualValue);
    }
}