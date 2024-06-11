 package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;

public class VerifierTest {

    @Test
    public void testVerifierConstructorWithValidInput() {
        String validVerifier = "validVerifier123";
        Verifier verifier = new Verifier(validVerifier);
        assertEquals(validVerifier, verifier.getValue());
    }

    @Test
    public void testGetValueWithValidInput() {
        String verifierValue = "validVerifier123";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }

    @Test
    public void testGetValueWithEmptyInput() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testGetValueWithNullInput() {
        Verifier verifier = new Verifier(null);
    }

    @Test
    public void testGetValueWithWhitespaceInput() {
        String verifierValue = "   ";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }

    @Test
    public void testGetValueWithSpecialCharactersInput() {
        String verifierValue = "%^&*()_+-=!@#$?";
        Verifier verifier = new Verifier(verifierValue);
        assertEquals(verifierValue, verifier.getValue());
    }

    @Test
    public void testGetValueWithLongStringInput() {
        String longVerifierValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, elit quis volutpat hendrerit, arcu nisi bibendum ipsum, nec aliquet magna nisi eu nibh.";
        Verifier verifier = new Verifier(longVerifierValue);
        assertEquals(longVerifierValue, verifier.getValue());
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testGetValueWithOutOfBoundsInput() {
        String longVerifierValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, elit quis volutpat hendrerit, arcu nisi bibendum ipsum, nec aliquet magna nisi eu nibh.";
        Verifier verifier = new Verifier(longVerifierValue.substring(0, 100));
    }
}