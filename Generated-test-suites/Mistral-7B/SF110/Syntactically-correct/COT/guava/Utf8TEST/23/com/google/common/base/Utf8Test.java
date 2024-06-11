package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int length = Utf8.encodedLength(sequence);
        assertEquals(28, length);

        sequence = "\u00A0\u0393\u03B4\u03C1\u03C9\u03D1";
        length = Utf8.encodedLength(sequence);
        assertEquals(13, length);

        sequence = "\uD800\uDC00";
        try {
            length = Utf8.encodedLength(sequence);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int start = 10;
        int length = Utf8.encodedLengthGeneral(sequence, start);
        assertEquals(10, length);

        sequence = "\u0393\u03B4\u03C1\u03C9\u03D1\uD800\uDC00";
        try {
            length = Utf8.encodedLengthGeneral(sequence, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[0];
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[] {-72, -83, 193, -79, -93, -93, -127, -127, 230, 136, 106, 175, 148, 153, 234, 174, 147, 71, -128, 173, 172, 235, 139, 184, 237, 155, 232, 151, 235, 160, 159};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[] {-22, 191, -10, 242, 233, 180, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[] {'\u00A0'};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[] {'\uD800', '\uDC00'};
        assertFalse(Utf8.isWellFormed(bytes));
    }
}