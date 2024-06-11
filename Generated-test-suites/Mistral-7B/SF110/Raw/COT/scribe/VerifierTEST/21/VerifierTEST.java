package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testVerifierConstructor() {
        String validValue = "validVerifier";
        Verifier verifier = new Verifier(validValue);
        assertEquals(validValue, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructorWithNullValue() {
        String nullValue = null;
        new Verifier(nullValue);
    }

    @Test
    public void testGetValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test
    public void testEmptyValue() {
        String emptyValue = "";
        Verifier verifier = new Verifier(emptyValue);
        assertEquals(emptyValue, verifier.getValue());
    }

    @Test
    public void testWhiteSpaceValue() {
        String whiteSpaceValue = "   ";
        Verifier verifier = new Verifier(whiteSpaceValue);
        assertEquals(whiteSpaceValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLengthValue() {
        String negativeLengthValue = Preconditions.checkNotNull(new String(new char[-1]));
        new Verifier(negativeLengthValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCharacterValue() {
        String nullCharacterValue = new String(new char[]{'\0'});
        new Verifier(nullCharacterValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueWithSpecialCharacters() {
        String specialCharactersValue = "!@#$%^&*()_+=-[]{}|\\:;\"'<>,.?/~`";
        new Verifier(specialCharactersValue);
    }

    @Test
    public void testValueWithCustomSpecialCharacters() {
        String customSpecialCharactersValue = "!@#_%^&*()+=[]{}|\\:;\"'<>,.?/~`~!";
        Verifier verifier = new Verifier(customSpecialCharactersValue);
        assertEquals(customSpecialCharactersValue, verifier.getValue());
    }

    @Test
    public void testValueWithLongLength() {
        String longValue = new String(new char[1024 * 1024]);
        Verifier verifier = new Verifier(longValue);
        assertEquals(longValue, verifier.getValue());
    }
}