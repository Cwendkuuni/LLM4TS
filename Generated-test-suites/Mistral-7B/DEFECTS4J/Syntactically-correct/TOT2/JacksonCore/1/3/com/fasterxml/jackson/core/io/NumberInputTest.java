package com.fasterxml.jackson.core.io; // import statements omitted for brevity

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, digitChars.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithEdgeCases() {
        // Testing the edge cases of length 1 and 9 digits
        char[] digitChars1 = {'1'};
        int expected1 = 1;
        int actual1 = NumberInput.parseInt(digitChars1, 0, digitChars1.length);
        assertEquals(expected1, actual1);

        char[] digitChars9 = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int expected9 = 123456789;
        int actual9 = NumberInput.parseInt(digitChars9, 0, digitChars9.length);
        assertEquals(expected9, actual9);

        // Testing negative numbers
        char[] digitCharsNegative = {'-', '1', '2', '3', '4', '5'};
        int expectedNegative = -12345;
        int actualNegative = NumberInput.parseInt(digitCharsNegative, 0, digitCharsNegative.length);
        assertEquals(expectedNegative, actualNegative);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidInput() {
        char[] digitCharsInvalid = {'A', '1', '2', '3', '4'};
        NumberInput.parseInt(digitCharsInvalid, 0, digitCharsInvalid.length);
    }

    // Add test cases for other methods, such as parseLong, parseBigDecimal, etc.
}