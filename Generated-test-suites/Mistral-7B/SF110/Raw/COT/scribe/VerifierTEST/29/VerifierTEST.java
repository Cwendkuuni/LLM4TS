package org.scribe.model.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validString = "validVerifier";
        Verifier verifier = new Verifier(validString);
        assertEquals(validString, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructorWithNullValue() {
        String nullValue = null;
        new Verifier(nullValue);
    }

    @Test
    public void testVerifierGetter() {
        String verifierValue = "testVerifier";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }

    @Test
    public void testEmptyVerifier() {
        Verifier emptyVerifier = new Verifier("");
        assertEquals("", emptyVerifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierWithEmptyString() {
        new Verifier("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierWithNullCharacter() {
        String nullCharacter = null;
        String verifierValue = "verifier" + nullCharacter;
        new Verifier(verifierValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierWithLongString() {
        String longString = "longStringWithMaxLength";
        for (int i = 0; i < 1000; i++) {
            longString += "A";
        }
        new Verifier(longString);
    }
}