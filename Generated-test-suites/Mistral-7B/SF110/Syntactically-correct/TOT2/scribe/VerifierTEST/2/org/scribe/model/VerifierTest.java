import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {
    @Test
    public void testVerifierConstructor() {
        String expectedValue = "expectedValue";
        Verifier verifier = new Verifier(expectedValue);
        String actualValue = verifier.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testVerifierGetter() {
        Verifier verifier = new Verifier("testValue");
        String actualValue = verifier.getValue();
        String expectedValue = "testValue";
        assertEquals(expectedValue, actualValue);
    }
}