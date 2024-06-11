package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneWithSimpleWords() {
        Metaphone metaphone = new Metaphone();
        String expected = "BAPE";
        String input = "Apple";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);

        expected = "BET";
        input = "Bettle";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        expected = "BIH";
        input = "Bite";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneWithComplexWords() {
        Metaphone metaphone = new Metaphone();
        String expected = "BARP";
        String input = "Barbecue";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);

        expected = "BERE";
        input = "Bereave";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        expected = "BIYG";
        input = "Bighouse";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneWithNullString() {
        Metaphone metaphone = new Metaphone();
        String input = null;
        String expectedErrorMessage = "Parameter supplied to Metaphone encode is null";
        metaphone.metaphone(input);
        fail(expectedErrorMessage);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneWithEmptyString() {
        Metaphone metaphone = new Metaphone();
        String input = "";
        String expectedErrorMessage = "Parameter supplied to Metaphone encode is empty";
        metaphone.metaphone(input);
        fail(expectedErrorMessage);
    }

    @Test
    public void testMetaphoneWithSpecialCharacters() {
        Metaphone metaphone = new Metaphone();
        String input = "Apple#";
        String expected = "BAPE";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneWithNumbers() {
        Metaphone metaphone = new Metaphone();
        String input = "Apple123";
        String expected = "BAPE";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }
}