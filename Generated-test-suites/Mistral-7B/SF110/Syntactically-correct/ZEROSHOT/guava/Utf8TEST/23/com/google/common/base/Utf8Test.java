package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "Hello, World!";
        int length = Utf8.encodedLength(sequence);
        assertEquals(15, length);

        sequence = "\u00e9\u00e8\u0305\u0300\u2648\u2642\u265f";
        length = Utf8.encodedLength(sequence);
        assertEquals(13, length);

        sequence = "\uD800\uDC00";
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(sequence));
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "Hello, World!";
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(15, length);

        sequence = "\u00e9\u00e8\u0305\u0300\u2648\u2642\u265f";
        length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(13, length);

        sequence = "\uD800\uDC00";
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral(sequence, 0));
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = "Hello, World!".getBytes();
        boolean result = Utf8.isWellFormed(bytes);
        assertTrue(result);

        bytes = "Hello, 世界!".getBytes();
        result = Utf8.isWellFormed(bytes);
        assertTrue(result);

        bytes = new byte[]{(byte) 0xED, (byte) 0xA0, (byte) 0xED, (byte) 0xB0};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[]{0xED, 0xA0, 0xED, 0xB0};
        boolean result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertFalse(result);

        bytes = new byte[]{-61, -110, -62, (byte) 0x80, 0};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
    }

    @Test
    public void testIsWellFormedWithInvalidBytes() {
        byte[] bytes = new byte[]{0xED, 0xA0, 0xED, 0xB0, -1, -62, -1};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedWithEmptyArray() {
        assertTrue(Utf8.isWellFormed(new byte[]{}));
    }

    @Test
    public void testIsWellFormedWithNull() {
        assertNull(Utf8.isWellFormed((byte[]) null));
    }
}