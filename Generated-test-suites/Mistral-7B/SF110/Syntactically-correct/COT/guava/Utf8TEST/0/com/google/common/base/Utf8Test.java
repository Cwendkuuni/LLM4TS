package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "ABCDEFG";
        int expected = 9;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);

        sequence = "\u00A0\u0393\u03A3";
        expected = 5;
        actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);

        sequence = "\uD800\uDC00";
        try {
            actual = Utf8.encodedLength(sequence);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "ABCDEFG";
        int expected = 9;
        int actual = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(expected, actual);

        sequence = "\u00A0\u0393\u03A3";
        int utf16Length = sequence.length();
        int expectedGeneral = 5;
        int actualGeneral = 0;
        for (int i = 0; i < utf16Length; ++i) {
            final char c = sequence.charAt(i);
            if (c < '\u0800') {
                actualGeneral += '\u007f' - c >>> 31;
            } else {
                actualGeneral += 2;
                if ('\ud800' <= c && c <= '\udfff') {
                    ++i;
                }
            }
        }
        assertEquals(expectedGeneral, actualGeneral);

        sequence = "\uD800\uDC00";
        try {
            actualGeneral = Utf8.encodedLengthGeneral(sequence, 0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);

        bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x80, (byte) 0x01};
        expected = false;
        actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);

        bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0xEF, (byte) 0xBF, (byte) 0xBD};
        expected = false;
        actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);

        bytes = new byte[]{(byte) 0xD8, (byte) 0x00, (byte) 0xDC, (byte) 0x00};
        try {
            actual = Utf8.isWellFormed(bytes);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}