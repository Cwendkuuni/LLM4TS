import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        Verifier verifier = new Verifier("testValue");
        String value = verifier.getValue();
        Assert.assertEquals("testValue", value);
    }

    @Test
    public void testGetterMethod() {
        Verifier verifier = new Verifier("testValue");
        String value = verifier.getValue();
        Assert.assertEquals("testValue", value);
    }
}