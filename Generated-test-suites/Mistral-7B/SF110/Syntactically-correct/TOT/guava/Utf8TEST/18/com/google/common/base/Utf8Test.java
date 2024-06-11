package com.google.common.base.test;

import static com.google.common.base.Utf8.encodedLength;
import static com.google.common.base.Utf8.encodedLengthGeneral;
import static com.google.common.base.Utf8.isWellFormed;
import static com.google.common.base.Utf8.isWellFormedSlowPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.google.common.base.Utf8;
import com.google.common.base.Preconditions;

/**
 * Test class for {@link Utf8} class.
 */
public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
        int length = Utf8.encodedLength(sequence);
        assertEquals(26, length);

        sequence = "\u00A0";
        length = Utf8.encodedLength(sequence);
        assertEquals(1, length);

        sequence = "\u0410\u0411\u0412";
        length = Utf8.encodedLength(sequence);
        assertEquals(6, length);

        sequence = "\ud800\udc00";
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(sequence));
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(26, length);

        sequence = "\u0410\u0411\u0412";
        int start = 2;
        length = Utf8.encodedLengthGeneral(sequence, start);
        assertEquals(4, length);

        sequence = "\ud800\udc00";
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral(sequence, 0));
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = "abcdefghijklmnopqrstuvwxyz".getBytes();
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = "\u00A0".getBytes();
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = "\u0410\u0411\u0412".getBytes();
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = "\ud800\udc00".getBytes();
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0, -32, -63, -62, -64, -65};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[]{0, -62, -65, 0, -65, -65};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{0, -32, -111, -110, 0, -96, 0, -96, 0, -65, -65};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{0, -19, -111, -110, 0, -96, 0, -96, 0, -65, -65};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{0, -32, -96, -96, 0, -65, -65};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
    }

    @Test
    public void testPreconditions() {
        byte[] bytes = new byte[1024];
        String errorMessage = "Test error message";
        assertThrows(IllegalArgumentException.class, () -> Utf8.isWellFormed(bytes, -1, bytes.length, errorMessage));
        assertThrows(IllegalArgumentException.class, () -> Utf8.isWellFormed(bytes, bytes.length + 1, bytes.length, errorMessage));
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            fail("Expected true but was false");
        }
    }
}