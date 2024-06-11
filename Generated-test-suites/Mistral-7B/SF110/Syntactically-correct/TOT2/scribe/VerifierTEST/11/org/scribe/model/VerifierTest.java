import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testGetValue() {
        String verifierValue = "testVerifier";
        Verifier verifier = new Verifier(verifierValue);
        String actualValue = verifier.getValue();
        Assert.assertEquals(verifierValue, actualValue);
    }
}