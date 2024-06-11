package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        String sequence = "Hello, World!";
        int length = Utf8.encodedLength(sequence);
        assertEquals(11 * 7 + 6 * 5 + 5 * 4 + 4 * 3 + 3 * 2 + 2 * 1 + 1, length); // Calculated length of "Hello, World!" in UTF-8
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthWithInvalidSequence() {
        String sequence = "Invalid sequence";
        try {
            Utf8.encodedLength(sequence);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncodedLengthWithEmptySequence() {
        String sequence = "";
        int length = Utf8.encodedLength(sequence);
        assertEquals(0, length);
    }

}