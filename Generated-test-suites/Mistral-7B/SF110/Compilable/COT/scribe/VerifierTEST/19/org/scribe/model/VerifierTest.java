package org.scribe.model;
import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validVerifier = "validVerifier";
        Verifier verifier = new Verifier(validVerifier);
        Assert.assertEquals(validVerifier, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructorWithNullValue() {
        String nullValue = null;
        new Verifier(nullValue); // exception should be thrown
    }

    @Test
    public void testVerifierGetValue() {
        Verifier verifier = new Verifier("testVerifier");
        Assert.assertEquals("testVerifier", verifier.getValue());
    }

    @Test
    public void testVerifierGetValueEmptyString() {
        Verifier verifier = new Verifier("");
        Assert.assertEquals("", verifier.getValue());
    }

    @Test
    public void testVerifierGetValueWithWhitespace() {
        Verifier verifier = new Verifier("   ");
        Assert.assertEquals("", verifier.getValue());
    }

    @Test
    public void testVerifierGetValueWithSpecialCharacters() {
        Verifier verifier = new Verifier("@#%^&*()_+={}[]|\\:\"<>,.?/~`");
        Assert.assertEquals("@#%^&*()_+={}[]|\\:\"<>,.?/~`", verifier.getValue());
    }

}