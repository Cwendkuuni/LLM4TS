package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for Metaphone class
 */
public class MetaphoneTest {

    @Test
    public void testMetaphoneEncodesSimpleWords() {
        Metaphone metaphone = new Metaphone();
        String input = "apple";
        String expected = "AA01";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneEncodesWordsWithInitialExceptions() {
        Metaphone metaphone = new Metaphone();
        String input = "banana";
        String expected = "AA02";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "knight";
        expected = "KH02";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "appleman";
        expected = "AA01M";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneEncodesWordsWithFinalExceptions() {
        Metaphone metaphone = new Metaphone();
        String input = "bath";
        String expected = "B301";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "rough";
        expected = "R201";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "oughter";
        expected = "R201T";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneEncodesWordsWithVowels() {
        Metaphone metaphone = new Metaphone();
        String input = "eagle";
        String expected = "E001G";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "ocean";
        expected = "O001S";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "aardvark";
        expected = "AA01RK";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneEncodesWordsWithSilentG() {
        Metaphone metaphone = new Metaphone();
        String input = "beggar";
        String expected = "B201G";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "debt";
        expected = "D201B";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "bought";
        expected = "B201G";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneEncodesWordsWithDoubleLetters() {
        Metaphone metaphone = new Metaphone();
        String input = "letter";
        String expected = "L201T";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "butter";
        expected = "B201T";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "potato";
        expected = "P201T";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneThrowsEncoderExceptionForNullInput() {
        Metaphone metaphone = new Metaphone();
        String input = null;
        metaphone.metaphone(input);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneThrowsEncoderExceptionForEmptyInput() {
        Metaphone metaphone = new Metaphone();
        String input = "";
        metaphone.metaphone(input);
    }

    @Test
    public void testMetaphoneEncodesWordsWithInitialX() {
        Metaphone metaphone = new Metaphone();
        String input = "xylophone";
        String expected = "KSX1F";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneEncodesWordsWithFinalS() {
        Metaphone metaphone = new Metaphone();
        String input = "racecar";
        String expected = "R201S";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }
}