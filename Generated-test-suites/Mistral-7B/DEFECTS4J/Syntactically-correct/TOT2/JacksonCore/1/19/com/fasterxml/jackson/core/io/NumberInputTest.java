package com.example.junit4; // Replace with your package name

import static org.junit.Assert.*;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] input = {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(input, 0, input.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithEdgeCases() {
        char[] inputMinus = {'-', '1', '2', '3'};
        int minusOneThousand = -1000;
        int actualMinus = NumberInput.parseInt(inputMinus, 0, inputMinus.length);
        assertEquals(minusOneThousand, actualMinus);

        char[] inputMaxLength = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int maxIntValue = Integer.MAX_VALUE;
        int actualMax = NumberInput.parseInt(inputMaxLength, 0, inputMaxLength.length);
        assertEquals(maxIntValue, actualMax);

        char[] inputLongerLength = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        int actualLonger = NumberInput.parseInt(inputLongerLength, 0, inputLongerLength.length);
        assertEquals(Integer.parseInt(NumberInput.MAX_LONG_STR), actualLonger);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidInput() {
        char[] invalidInput = {'a', 'b', 'c'};
        NumberInput.parseInt(invalidInput, 0, invalidInput.length);
    }

    // Test other methods (parseInt, parseLong, parseAsInt, etc.) in a similar way.
}