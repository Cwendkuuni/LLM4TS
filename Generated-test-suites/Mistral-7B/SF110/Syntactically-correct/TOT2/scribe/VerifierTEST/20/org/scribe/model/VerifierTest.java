package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.Verifier;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VerifierTest {
    private Verifier verifier;

    @Before
    public void setup() {
        verifier = new Verifier("testValue");
    }

    @Test
    public void testConstructor() {
        String validValue = "testValue";
        Verifier verifier = new Verifier(validValue);
        assertNotNull(verifier);
    }

    @Test
    public void testGetValue() {
        String expectedValue = "testValue";
        String actualValue = verifier.getValue();
        assert(expectedValue.equals(actualValue));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullArgument() {
        String nullValue = null;
        new Verifier(nullValue);
    }

    @Test
    public void testEmptyConstructorArgument() {
        String emptyValue = "";
        Verifier verifier = new Verifier(emptyValue);
        String actualValue = verifier.getValue();
        Assert.assertEquals("", actualValue);
    }

    @Test
    public void testGetValueWithEmptyVerifier() {
        String emptyValue = "";
        Verifier verifier = new Verifier(emptyValue);
        String actualValue = verifier.getValue();
        Assert.assertEquals("", actualValue);
    }
}