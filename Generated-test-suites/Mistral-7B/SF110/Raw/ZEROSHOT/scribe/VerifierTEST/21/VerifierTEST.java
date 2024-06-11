import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VerifierTest {

    private Verifier verifier;

    @Before
    public void setUp() {
        this.verifier = new Verifier("testVerifier");
    }

    @Test
    public void testVerifierConstructor() {
        assertEquals("testVerifier", this.verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierConstructorWithNullValue() {
        Preconditions test = new Preconditions();
        test.checkNotNull(this.verifier, "Verifier object was created with null value");
    }

    @Test
    public void testGetValue() {
        assertEquals("testVerifier", this.verifier.getValue());
    }

    @Test
    public void testVerifierValueEquals() {
        Verifier otherVerifier = new Verifier("testVerifier");
        assertEquals(this.verifier, otherVerifier);
        assertEquals(this.verifier.hashCode(), otherVerifier.hashCode());
    }

    @Test
    public void testVerifierValueNotEquals() {
        Verifier otherVerifier = new Verifier("differentVerifier");
        assertFalse(this.verifier.equals(otherVerifier));
        assertNotSame(this.verifier.hashCode(), otherVerifier.hashCode());
    }

    @Test
    public void testVerifierWithNullValueThrowsIllegalArgumentException() {
        Preconditions test = new Preconditions();
        test.checkArgument(this.verifier, "Verifier object was created with null value");
    }

    @Test
    public void testVerifierValueIsEmptyStringThrowsIllegalArgumentException() {
        Preconditions test = new Preconditions();
        test.checkArgument(new Verifier(""), "Verifier object was created with an empty string as value");
    }

    @Test
    public void testGetValueReturnsNull() {
        assertNull(new Verifier(null).getValue());
    }
}