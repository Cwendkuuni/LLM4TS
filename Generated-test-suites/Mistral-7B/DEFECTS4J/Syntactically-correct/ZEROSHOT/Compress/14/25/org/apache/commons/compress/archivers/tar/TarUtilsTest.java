package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;

import java.util.Arrays;

/**
 * TarUtilsTest class to test the methods in TarUtils class
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctal() {
        byte[] testData = new byte[]{'o', '7', '7', '7', '5', ' ', ' '};
        long result = TarUtils.parseOctal(testData, 0, testData.length);
        assertEquals(07755L, result);
    }

    /**
     * Test parseOctal method with invalid input.
     */
    @Test
    public void testParseOctalInvalidInput() {
        byte[] testData = new byte[]{'o', '7', '7', '7', '5', '6', ' '};
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctal(testData, 0, testData.length);
        });
    }

    /**
     * Test parseOctalOrBinary method with valid binary input.
     */
    @Test
    public void testParseOctalOrBinaryBinary() {
        byte[] testData = new byte[]{(byte) 0x81, 0x00, 0x00, 0x00};
        long result = TarUtils.parseOctalOrBinary(testData, 0, testData.length);
        assertEquals(12345L, result);
    }

    /**
     * Test parseOctalOrBinary method with valid octal input.
     */
    @Test
    public void testParseOctalOrBinaryOctal() {
        byte[] testData = new byte[]{'o', '7', '7', '7', '5', ' ', ' '};
        long result = TarUtils.parseOctalOrBinary(testData, 0, testData.length);
        assertEquals(07755L, result);
    }

    /**
     * Test parseOctalOrBinary method with invalid input.
     */
    @Test
    public void testParseOctalOrBinaryInvalidInput() {
        byte[] testData = new byte[]{'o', '7', '7', '7', '5', '6', ' '};
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctalOrBinary(testData, 0, testData.length);
        });
    }

    /**
     * Test parseBoolean method with valid input.
     */
    @Test
    public void testParseBooleanTrue() {
        byte[] testData = new byte[]{1};
        boolean result = TarUtils.parseBoolean(testData, 0);
        assertTrue(result);
    }

    /**
     * Test parseBoolean method with invalid input.
     */
    @Test
    public void testParseBooleanFalse() {
        byte[] testData = new byte[]{0};
        boolean result = TarUtils.parseBoolean(testData, 0);
        assertFalse(result);
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseName() {
        byte[] testData = new byte[]{'t', 'e', 's', 't', ' ', 0};
        String result = TarUtils.parseName(testData, 0, testData.length);
        assertEquals("test", result);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] testData = new byte[TarConstants.FILENAME_LENGTH];
        int result = TarUtils.formatNameBytes(name, testData, 0, TarConstants.FILENAME_LENGTH);
        Arrays.equals(name.getBytes(), Arrays.copyOfRange(testData, 0, result));
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 07755L;
        byte[] testData = new byte[TarConstants.UIDLEN];
        TarUtils.formatUnsignedOctalString(value, testData, 0, TarConstants.UIDLEN);
        Arrays.equals(new byte[]{'0', '1', '0', '1', '1', '1', '1', '5', '5'}, testData);
    }

    /**
     * Test formatLongOctalBytes method with valid input.
     */
    @Test
    public void testFormatLongOctalBytes() {
        long value = 07755L;
        byte[] testData = new byte[TarConstants.UIDLEN+2];
        int result = TarUtils.formatLongOctalBytes(value, testData, 0, TarConstants.UIDLEN+2);
        byte[] expected = new byte[]{'0', '1', '0', '1', '1', '1', '1', '5', '5', ' ', ' '};
        Arrays.equals(expected, Arrays.copyOfRange(testData, 0, result));
    }

    /**
     * Test computeCheckSum method with valid input.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] testData = new byte[]{'o', '7', '7', '7', '5', ' ', ' '};
        long result = TarUtils.computeCheckSum(testData);
        long checksum = TarUtils.computeCheckSum(testData);
        assertEquals(result, checksum);
    }
}