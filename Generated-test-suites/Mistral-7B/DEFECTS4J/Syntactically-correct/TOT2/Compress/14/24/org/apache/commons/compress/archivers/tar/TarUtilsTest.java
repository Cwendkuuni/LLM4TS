package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;

/**
 * Test class for TarUtils methods.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctalValidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', ' '};
        long expected = 017550L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test parseOctal method with invalid input - trailing NUL missing.
     */
    @Test
    public void testParseOctalInvalidTrailingNUL() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0'};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctal(buffer, 0, buffer.length);
        });
        String expectedMessage = "Invalid byte ";
        assertTrue(exception.getMessage().startsWith(expectedMessage));
    }

    /**
     * Test parseOctal method with invalid input - invalid byte.
     */
    @Test
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', 'X', '0', ' '};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctal(buffer, 0, buffer.length);
        });
        String expectedMessage = "Invalid byte X at offset 3 in '075X0' len=6";
        assertTrue(exception.getMessage().equals(expectedMessage));
    }

    /**
     * Test parseOctalOrBinary method with valid binary input.
     */
    @Test
    public void testParseOctalOrBinaryValidBinaryInput() {
        byte[] buffer = new byte[]{0x81, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        long expected = 256 * 256 * 256 + 1;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test parseOctalOrBinary method with valid octal input.
     */
    @Test
    public void testParseOctalOrBinaryValidOctalInput() {
        byte[] buffer = new byte[]{'0', '1', '7', '0', '7', '7', '5', '5', '0'};
        long expected = 0117755L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test parseBoolean method with valid input.
     */
    @Test
    public void testParseBooleanValidInput() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expected, result);
    }

    /**
     * Test parseBoolean method with invalid input.
     */
    @Test
    public void testParseBooleanInvalidInput() {
        byte[] buffer = new byte[]{0};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseBoolean(buffer, 0);
        });
        String expectedMessage = "Invalid byte 0 at offset 0 in '0' len=1";
        assertTrue(exception.getMessage().equals(expectedMessage));
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseNameValidInput() {
        byte[] buffer = new byte[]{0x00, (byte) 'a', (byte) 'b', 0x00, (byte) 'c'};
        String expected = "abc";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytesValidInput() {
        String name = "myentry.txt";
        byte[] buffer = new byte[TarConstants.NAMELEN];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("myentry.txt".length(), offset);

        String result = new String(buffer, 0, offset);
        assertEquals("myentry.txt", result);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalStringValidInput() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "001261551";
        String result = new String(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test formatUnsignedOctalString method with value that will not fit in buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringValueTooLarge() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalBytes method with valid input.
     */
    @Test
    public void testParseOctalBytesValidInput() {
        byte[] buffer = new byte[]{'0', '1', '1', '6', '6', '1', '5', '5', '5', '0', ' '};
        long expected = 0116615550L;
        long result = TarUtils.parseOctalBytes(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test parseOctalBytes method with invalid input - missing trailing space.
     */
    @Test
    public void testParseOctalBytesMissingTrailingSpace() {
        byte[] buffer = new byte[]{'0', '1', '1', '6', '6', '1', '5', '5', '5', '0'};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctalBytes(buffer, 0, buffer.length);
        });
        String expectedMessage = "Invalid byte ";
        assertTrue(exception.getMessage().startsWith(expectedMessage));
    }

    /**
     * Test parseOctalBytes method with invalid input - missing trailing null.
     */
    @Test
    public void testParseOctalBytesMissingTrailingNull() {
        byte[] buffer = new byte[]{'0', '1', '1', '6', '6', '1', '5', '5', '5'};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctalBytes(buffer, 0, buffer.length);
        });
        String expectedMessage = "Invalid byte ";
        assertTrue(exception.getMessage().startsWith(expectedMessage));
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid input - fits in octal.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesValidOctalInput() {
        long value = 12345678L;
        byte[] buffer = new byte[10];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        String expected = "0123675456";
        String result = new String(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid input - fits in binary.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesValidBinaryInput() {
        long value = 123456789012345L;
        byte[] buffer = new byte[13];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        String expected = "0000000123456789012345";
        String result = new String(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with invalid input - value too large.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalOrBinaryBytesValueTooLarge() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
    }
}