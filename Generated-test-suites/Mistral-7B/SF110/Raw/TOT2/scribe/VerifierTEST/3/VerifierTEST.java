import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        String result = verifier.getValue();
        assert(result.equals("testValue"));
    }

    @Test
    public void testGetValueWithNullVerifier() {
        Verifier verifier = null;
        assert(verifier.getValue() == null);
    }

    @Test
    public void testGetValueWithEmptyStringVerifier() {
        Verifier verifier = new Verifier("");
        String result = verifier.getValue();
        assert(result.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testGetValueWithNullValue() {
        Verifier verifier = new Verifier(null);
    }

    @Test
    public void testGetValueMultipleTimes() {
        Verifier verifier = new Verifier("testValue");
        String value1 = verifier.getValue();
        String value2 = verifier.getValue();
        assert(value1.equals(value2));
    }

    @Test
    public void testGetValueWithLongString() {
        String longString = "This is a very long string that tests the getValue method with a long string as the verifier value.";
        Verifier verifier = new Verifier(longString);
        String result = verifier.getValue();
        assert(result.equals(longString));
    }
}