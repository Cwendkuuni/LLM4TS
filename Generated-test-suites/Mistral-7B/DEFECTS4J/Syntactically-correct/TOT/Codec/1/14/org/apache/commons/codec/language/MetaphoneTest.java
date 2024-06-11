package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneSimple() {
        Metaphone encoder = new Metaphone();
        String input = "apple";
        String expected = "A0P0L1E0";
        String result = encoder.metaphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneWithInitialExceptions() {
        Metaphone encoder = new Metaphone();
        String input = "knights";
        String expected = "K1N1T1S2";
        String result = encoder.metaphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneWithDoubleLetterExceptions() {
        Metaphone encoder = new Metaphone();
        String input = "banana";
        String expected = "B2N1N1A0";
        String result = encoder.metaphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneWithComplexExceptions() {
        Metaphone encoder = new Metaphone();
        String input = "schadenfreude";
        String expected = "S0X2D1E0R2U1D1E";
        String result = encoder.metaphone(input);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneWithInvalidInput() {
        Metaphone encoder = new Metaphone();
        String input = "123apple";
        encoder.metaphone(input);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneWithEmptyInput() {
        Metaphone encoder = new Metaphone();
        String input = "";
        encoder.metaphone(input);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneWithNullInput() {
        Metaphone encoder = new Metaphone();
        String input = null;
        encoder.metaphone(input);
    }
}