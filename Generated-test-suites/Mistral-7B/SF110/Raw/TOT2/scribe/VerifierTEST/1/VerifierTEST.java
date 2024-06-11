import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test
    public void testVerifierGetterMethod() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructorNullString() {
        String value = null;
        new Verifier(value);
    }

    @Test
    public void testVerifierGetterMethodSameValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
        verifier.setValue("newValue"); // setting a new value is not supported in the given class
        assertEquals(value, verifier.getValue());
    }

}