import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class VerifierTest {
    @Test
    public void testVerifierConstructor() {
        String verifier = "testVerifier";
        Verifier verifierObject = new Verifier(verifier);
        assertEquals(verifier, verifierObject.getValue());
    }

    @Test
    public void testGetValue() {
        String verifier = "testVerifier";
        Verifier verifierObject = new Verifier(verifier);
        assertEquals(verifier, verifierObject.getValue());
    }
}