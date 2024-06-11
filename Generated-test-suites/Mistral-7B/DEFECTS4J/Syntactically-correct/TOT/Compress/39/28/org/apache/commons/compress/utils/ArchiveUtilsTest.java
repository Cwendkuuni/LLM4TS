package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.test.AbstractArchiveTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test case for {@link ArchiveUtils} class
 */
public class ArchiveUtilsTest extends AbstractArchiveTestCase {

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        final ArchiveEntry entry = createTestEntry("testfile.txt", false, 1024);
        final String result = ArchiveUtils.toString(entry);
        assertEquals("- 1024 testfile.txt", result);
    }

    /**
     * Test matchAsciiBuffer method with valid input
     */
    @Test
    public void testMatchAsciiBufferValid() {
        final String expected = "Test String";
        final byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    /**
     * Test matchAsciiBuffer method with invalid input
     */
    @Test(expected = RuntimeException.class)
    public void testMatchAsciiBufferInvalid() {
        final String expected = "Test String";
        final byte[] buffer = new byte[10];
        ArchiveUtils.matchAsciiBuffer(expected, buffer);
    }

    /**
     * Test isEqual method with valid input
     */
    @Test
    public void testIsEqualValid() {
        final byte[] buffer1 = new byte[]{'T', 'e', 's', 't', ' ', 'S', 't', 'r', 'i', 'n', 'g'};
        final byte[] buffer2 = new byte[]{'T', 'e', 's', 't', ' ', 'S', 't', 'r', 'i', 'n', 'g'};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    /**
     * Test isEqual method with invalid input
     */
    @Test
    public void testIsEqualInvalid() {
        final byte[] buffer1 = new byte[]{'T', 'e', 's', 't', ' ', 'S', 't', 'r', 'i', 'n', 'g'};
        final byte[] buffer2 = new byte[]{'T', 'e', 's', 't', ' ', 'S', 't', 'r', 'i', 'n', 'h'};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    /**
     * Test isEqual method with trailing nulls
     */
    @Test
    public void testIsEqualWithTrailingNulls() {
        final byte[] buffer1 = new byte[]{'T', 'e', 's', 't', ' ', 'S', 't', 'r', 'i', 'n', 'g', 0};
        final byte[] buffer2 = new byte[]{'T', 'e', 's', 't', ' ', 'S', 't', 'r', 'i', 'n', 'g', 0};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    /**
     * Test isEqual method with different sizes
     */
    @Test
    public void testIsEqualDifferentSizes() {
        final byte[] buffer1 = new byte[]{'T', 'e', 's', 't', ' ', 'S', 't', 'r', 'i', 'n', 'g'};
        final byte[] buffer2 = new byte[]{'T', 'e', 's', 't', ' ', 'S', 't', 'r', 'i', 'n', 'g', 'h'};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    /**
     * Test isArrayZero method
     */
    @Test
    public void testIsArrayZero() {
        final byte[] buffer = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(buffer, 5));
    }

    /**
     * Test sanitize method
     */
    @Test
    public void testSanitize() {
        final String testString = "TestString with  spécial chrêctêrs: !@#$%^&*()_+{}|<>:\"?[]\\;,./~`";
        final String sanitizedString = ArchiveUtils.sanitize(testString);
        assertTrue(sanitizedString.length() <= 255);
        for (int i = 0; i < sanitizedString.length(); i++) {
            final char c = sanitizedString.charAt(i);
            if (!Character.isISOControl(c) && Character.UnicodeBlock.of(c) != null) {
                continue;
            }
            assertEquals("?", Character.toString(c), Character.toString(sanitizedString.charAt(i)));
        }
    }
}