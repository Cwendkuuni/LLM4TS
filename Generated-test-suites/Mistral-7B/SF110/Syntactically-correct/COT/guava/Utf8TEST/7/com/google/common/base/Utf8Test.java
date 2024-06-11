package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
        int actual = Utf8.encodedLength(sequence);
        int expected = sequence.length() * 3 / 2;
        assertEquals("Incorrect UTF-8 length for 'abcdefghijklmnopqrstuvwxyz'", expected, actual);

        sequence = "\u00e1\u0305\u00f1";
        actual = Utf8.encodedLength(sequence);
        expected = sequence.length() * 3 / 2;
        assertEquals("Incorrect UTF-8 length for '\u00e1\u0305\u00f1'", expected, actual);

        sequence = "\uD800\uDC00";
        try {
            actual = Utf8.encodedLength(sequence);
            fail("Expected IllegalArgumentException for '\uD800\uDC00'");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
        int actual = Utf8.encodedLengthGeneral(sequence, 0);
        int expected = sequence.length() * 3 / 2;
        assertEquals("Incorrect UTF-8 length for 'abcdefghijklmnopqrstuvwxyz' with general approach", expected, actual);

        sequence = "\u00e1\u0305\u00f1";
        int utf16Length = sequence.length();
        int actual = 0;
        for (int i = 0; i < utf16Length; ++i) {
            final char c = sequence.charAt(i);
            if (c < '\u0800') {
                actual += '\u007f' - c >>> 31;
            } else {
                actual += 2;
                if ('\ud800' <= c && c <= '\udfff') {
                    final int cp = Character.codePointAt(sequence, i);
                    if (cp < 65536) {
                        fail("Expected IllegalArgumentException for unpaired surrogate at index " + i);
                    }
                    ++i;
                }
            }
        }
        assertEquals("Incorrect UTF-8 length for '\u00e1\u0305\u00f1' with general approach", actual, Utf8.encodedLengthGeneral(sequence, 0));
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[] {'a', 'b', 'c', 'd', 'e', 'f'};
        assertTrue("UTF-8 bytes 'abcdef' are not well-formed", Utf8.isWellFormed(bytes));

        bytes = new byte[] {'\u00e1', '\u0305', '\u00f1'};
        assertTrue("UTF-8 bytes '\u00e1\u0305\u00f1' are well-formed", Utf8.isWellFormed(bytes));

        bytes = new byte[] {'\uD800', '\uDC00'};
        assertFalse("UTF-8 bytes '\uD800\uDC00' are well-formed", Utf8.isWellFormed(bytes));

        bytes = new byte[] {'\uD800', '\uDC01'};
        assertTrue("UTF-8 bytes '\uD800\uDC01' are well-formed", Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[] {'a', '\u0080', '\u00e1', '\u0305', '\u00f1'};
        assertTrue("UTF-8 bytes 'a\u0080\u00e1\u0305\u00f1' are well-formed", Utf8.isWellFormed(bytes, 0, bytes.length));

        bytes = new byte[] {'a', '\u00e1', '\u0305', '\u00f1'};
        assertTrue("UTF-8 bytes 'a\u00e1\u0305\u00f1' are well-formed", Utf8.isWellFormed(bytes, 0, bytes.length));

        bytes = new byte[] {'\uD800', '\uDC00', '\uD800', '\uDC00'};
        assertFalse("UTF-8 bytes '\uD800\uDC00\uD800\uDC00' are well-formed", Utf8.isWellFormed(bytes, 0, bytes.length));

        bytes = new byte[] {'\uD800', '\uDC00', '\uD800', '\uDC01'};
        assertTrue("UTF-8 bytes '\uD800\uDC00\uD800\uDC01' are well-formed", Utf8.isWellFormed(bytes, 0, bytes.length));

        bytes = new byte[] {'\uD800', '\uDC00', '\u00e1', '\u0305', '\u00f1', '\uD800', '\uDC00'};
        assertFalse("UTF-8 bytes '\uD800\uDC00\u00e1\u0305\u00f1\uD800\uDC00' are well-formed", Utf8.isWellFormed(bytes, 0, bytes.length));

        bytes = new byte[] {'\uD800', '\uDC00', '\uD800', '\uDC01'};
        assertTrue("UTF-8 bytes '\uD800\uDC00\uD800\uDC01' are well-formed", Utf8.isWellFormed(bytes, 0, bytes.length));
    }
}