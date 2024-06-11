package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "Hello World!";
        int length = Utf8.encodedLength(sequence);
        assertEquals(13 + 2 * 3, length); // 13 ASCII characters + 3 multi-byte characters

        sequence = "\u00A9 Hello World!"; // Copyright symbol is a single-byte UTF-8 character
        length = Utf8.encodedLength(sequence);
        assertEquals(14, length); // 13 ASCII characters + 1 single-byte UTF-8 character

        sequence = "\uD800\uDC00 Hello World!"; // Surrogate pair at the beginning
        try {
            length = Utf8.encodedLength(sequence);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "Mojito 🍹";
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(13 + 1 * 2 + 1, length); // 13 ASCII characters + 1 UTF-16 character (mojito) + 1 emoji character

        sequence = "\uD800\uDC00 Mojito 🍹";
        try {
            length = Utf8.encodedLengthGeneral(sequence, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[]{0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x2C, 0x20, 0x57, 0x6F, 0x72, 0x6C, 0x64, 0x21};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x2C, 0x20, 0x57, 0x6F, 0x72, 0x6C, 0x64, 0xEF, 0xBB, 0xBF};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{-6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0xED, 0xAD, 0xBE, 0xEF, 0xBB, 0xBF};
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[]{0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x2C, 0x20, 0x57, 0x6F, 0x72, 0x6C, 0x64, 0xEF, 0xBB, 0xBF};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{-6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{0xED, 0xAD, 0xBE, 0xEF, 0xBB, 0xBF};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
    }
}