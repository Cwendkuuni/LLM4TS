package com.google.common.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalseulf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {

    private static final byte[] INVALID_UTF8 = {-61, 128, 128, 128, 128, 128, 128, 128, 128, -62, -63};
    private static final byte[] VALID_UTF8 = {72, 65, 66, 65, 65, 65, 65, 65};

    @Test
    public void testEncodedLength() {
        assertEquals(1, Utf8.encodedLength("A"));
        assertEquals(2, Utf8.encodedLength("Ä"));
        assertEquals(3, Utf8.encodedLength("π"));
        assertEquals(4, Utf8.encodedLength("😀"));
        assertEquals(6, Utf8.encodedLength("😃"));
        assertEquals(7, Utf8.encodedLength("😋"));

        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(""));
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(null));

        final String testString = "👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋";
        assertEquals(1001, Utf8.encodedLength(testString));
    }

    @Test
    public void testEncodedLengthGeneral() {
        assertEquals(1, Utf8.encodedLengthGeneral("A", 0, 1));
        assertEquals(2, Utf8.encodedLengthGeneral("Ä", 0, 1));
        assertEquals(3, Utf8.encodedLengthGeneral("π", 0, 1));
        assertEquals(4, Utf8.encodedLengthGeneral("😀", 0, 1));
        assertEquals(6, Utf8.encodedLengthGeneral("😃", 0, 1));
        assertEquals(7, Utf8.encodedLengthGeneral("😋", 0, 1));

        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral("", 0, 0));
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral(null, 0, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> Utf8.encodedLengthGeneral("ABC", 3, 1));

        final String testString = "👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋👋";
        assertEquals(1001, Utf8.encodedLengthGeneral(testString, 0, testString.length()));
    }

    @Test
    public void testIsWellFormed() {
        assertTrue(Utf8.isWellFormed(VALID_UTF8));
        assertFalse(Utf8.isWellFormed(INVALID_UTF8));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        assertTrue(Utf8.isWellFormedSlowPath(VALID_UTF8, 0, VALID_UTF8.length));
        assertFalse(Utf8.isWellFormedSlowPath(INVALID_UTF8, 0, INVALID_UTF8.length));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckPositionIndexes() {
        Utf8.checkPositionIndexes(-1, 1, VALID_UTF8.length);
        Utf8.checkPositionIndexes(VALID_UTF8.length + 1, 1, VALID_UTF8.length);
    }

    @Test
    public void testIsWellFormedWithNull() {
        assertNull(Utf8.isWellFormed(null));
    }
}