package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class Utf8Test {

    private static final byte[] UTF8_INVALID = { 0xFF, 0xFF, 0xFF, 0xFF };
    private static final byte[] UTF8_SURROGATES = { 0xD800, 0xDC00, 0xD800, 0xDC00 };
    private static final byte[] UTF8_VALID_1 = { 72, 101, 108, 108, 111 }; // "Program"
    private static final byte[] UTF8_VALID_2 = { 72, 101, 304, 164, 164, 108, 108, 111 }; // "Program" in UTF-16
    private static final byte[] UTF8_VALID_3 = { (byte) 0xE2, (byte) 0x82, (byte) 0xAC }; // "€"

    @Test
    public void testEncodedLength() {
        for (String seq : new String[]{"", "\u0000", "\u007F", "\u07FF", "\u0800", "\uFFFF", "𠮷", "🐶", "🌎"}) {
            byte[] bytes = seq.getBytes(StandardCharsets.UTF_8);
            int length = Utf8.encodedLength(seq);
            assertEquals(length, bytes.length);
        }

        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength("𝄞")); // Code point above U+10FFFF

        for (String seq : new String[]{"\uD800\uDC00", "\uD800\uDC01", "\uD800\uDC0F", "\uD800\uDFFF", "\uD801\uDC00"}) {
            byte[] bytes = seq.getBytes(StandardCharsets.UTF_8);
            assertFalse(Utf8.isWellFormed(bytes));
        }

        for (int i = 0; i < UTF8_SURROGATES.length; ++i) {
            byte[] bytes = new byte[2];
            bytes[0] = UTF8_SURROGATES[i];
            bytes[1] = UTF8_SURROGATES[i + 1];
            assertFalse(Utf8.isWellFormed(bytes));
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        String seq = "🌏🐶🌎🐺🌏🐶🌎🐺";
        byte[] bytes = seq.getBytes(StandardCharsets.UTF_8);
        int len = Utf8.encodedLengthGeneral(seq, 0);
        assertEquals(bytes.length, len);
    }

    @Test
    public void testIsWellFormed() {
        for (byte[] bytes : new byte[][]{UTF8_VALID_1, UTF8_VALID_2, UTF8_VALID_3, UTF8_INVALID}) {
            assertEquals(true, Utf8.isWellFormed(bytes));
        }

        assertFalse(Utf8.isWellFormed(UTF8_INVALID));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        for (byte[] bytes : new byte[][]{UTF8_VALID_1, UTF8_VALID_2, UTF8_VALID_3, UTF8_INVALID}) {
            assertEquals(true, Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
        }

        assertFalse(Utf8.isWellFormedSlowPath(UTF8_INVALID, 0, UTF8_INVALID.length));
    }
}