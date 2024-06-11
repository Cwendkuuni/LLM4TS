package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Basic functionality test
        assertEquals(2, Utf8.encodedLength("AB"));
        assertEquals(3, Utf8.encodedLength("ABC"));
        assertEquals(4, Utf8.encodedLength("ABCDEF"));

        // Edge cases and exception handling tests
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(null));
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(""));
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength("AB\uD800"));
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength("\uD800AB"));
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength("AB\uD800AB"));
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength("\uD800AB\uD800"));

        // Test with long strings
        final String longString = "A" + new String(new char[1000]);
        assertEquals(1000 + encodedLength(longString.substring(0, 1000 / 2)), Utf8.encodedLength(longString));
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Basic functionality test, test private method with test case class
        final String testString = "\uD800\uDC00\uD800\uDC01";
        assertEquals(6, Utf8.encodedLengthGeneral(testString, 0));

        // Edge cases and exception handling tests, test private method with test case class
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral(null, 0));
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral("", 0));
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral("AB", 1));
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral("AB\uD800", 0));
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral("\uD800AB\uD800", 0));
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral("AB\uD800AB", 0));
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral("\uD800AB\uD800AB", 0));
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral("\uD800AB\uD800AB\uD800", 0));
    }

    @Test
    public void testIsWellFormed() {
        // Basic functionality test
        assertFalse(Utf8.isWellFormed(new byte[0]));
        assertFalse(Utf8.isWellFormed(new byte[]{-1, -85}));
        assertFalse(Utf8.isWellFormed(new byte[]{0, -69}));
        assertFalse(Utf8.isWellFormed(new byte[]{-1, 0, -69}));
        assertFalse(Utf8.isWellFormed(new byte[]{-1, 0, 0, 0}));

        assertTrue(Utf8.isWellFormed(new byte[]{72, 65, 76}));
        assertTrue(Utf8.isWellFormed(new byte[]{72, 65, 76, 75}));
        assertTrue(Utf8.isWellFormed(new byte[]{72, 65, 76, 75, 74}));

        // Edge cases and exception handling tests
        assertFalse(Utf8.isWellFormed(new byte[]{128, 128}));
        assertFalse(Utf8.isWellFormed(new byte[]{128, 128, 128}));
        assertFalse(Utf8.isWellFormed(new byte[]{128, 128, 128, 128, 128}));
        assertFalse(Utf8.isWellFormed(new byte[]{128, 65, 65}));
        assertFalse(Utf8.isWellFormed(new byte[]{-1, 128, 65, 65}));
        assertFalse(Utf8.isWellFormed(new byte[]{-1, 128, 65, 65, 128}));

        assertFalse(Utf8.isWellFormed(new byte[]{72, 195, 128}));
        assertFalse(Utf8.isWellFormed(new byte[]{72, 195, 128, 128}));
        assertFalse(UtfToIntTest.isWellFormed(new byte[]{72, 195, 128, 128, 128, 128}));

        assertFalse(Utf8.isWellFormed(new byte[]{72, 195, -63, 65}));
        assertFalse(Utf8.isWellFormed(new byte[]{72, 195, -63, 65, 65}));
        assertFalse(Utf8.isWellFormed(new byte[]{72, 195, -63, 65, 65, 65}));

        assertFalse(Utf8.isWellFormed(new byte[]{72, 224, 168, -112, 168, 163, 167, 165}));
        assertFalse(Utf8.isWellFormed(new byte[]{72, 224, 168, -112, 168, 163, 167, 165, 168}));
        assertFalse(Utf8.isWellFormed(new byte[]{72, 224, 168, -112, 168, 163, 167, 165, 168, 168}));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Test private method with test case class
        final byte[] testBytes = new byte[]{72, 195, -63, 65};
        assertFalse(Utf8.isWellFormedSlowPath(testBytes, 0, testBytes.length));

        final byte[] testBytes2 = new byte[]{72, 195, 195, 65};
        assertFalse(Utf8.isWellFormedSlowPath(testBytes2, 0, testBytes2.length));

        final byte[] testBytes3 = new byte[]{72, 224, -126, 129, 168, 163, 167, 165};
        assertFalse(Utf8.isWellFormedSlowPath(testBytes3, 0, testBytes3.length));

        final byte[] testBytes4 = new byte[]{72, 224, 168, -112, 168, 163, 167, 165, 168, 168, 168};
        assertFalse(Utf8.isWellFormedSlowPath(testBytes4, 0, testBytes4.length));
    }
}