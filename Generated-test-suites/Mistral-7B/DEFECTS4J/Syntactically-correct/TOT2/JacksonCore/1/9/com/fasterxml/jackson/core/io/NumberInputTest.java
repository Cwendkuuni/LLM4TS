package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_ValidInput() {
        char[] digitChars = new char[] {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int result = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, result);
    }

    @Test
    public void testParseInt_InvalidInput_NegativeIndex() {
        char[] digitChars = new char[] {'1', '2', '3', '4', '5'};
        assertThrows(IndexOutOfBoundsException.class, () -> NumberInput.parseInt(digitChars, -1, 5));
    }

    @Test
    public void testParseInt_InvalidInput_LengthLessThanOne() {
        char[] digitChars = new char[] {};
        assertThrows(IllegalArgumentException.class, () -> NumberInput.parseInt(digitChars, 0, 0));
    }

    @Test
    public void testParseInt_InvalidInput_NonDigitCharacters() {
        char[] digitChars = new char[] {'1', '2', 'a', '4', '5'};
        assertThrows(NumberFormatException.class, () -> NumberInput.parseInt(digitChars, 0, 5));
    }

    @Test
    public void testParseInt_ValidInput_NegativeNumber() {
        String str = "-123";
        int expected = -123;
        int result = NumberInput.parseInt(str);
        assertEquals(expected, result);
    }

    // Add more test cases for other methods similarly

    
    // Complete test file goes here
    
}