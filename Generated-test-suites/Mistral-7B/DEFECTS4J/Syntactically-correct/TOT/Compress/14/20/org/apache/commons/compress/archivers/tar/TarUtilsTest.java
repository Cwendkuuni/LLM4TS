package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctalValidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', ' ', '0'};
        long expected = 017540L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parseOctal method with invalid input - missing trailing space.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalMissingTrailingSpace() {
        byte[] buffer = new byte[]{'0', '7', '5', '4'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctal method with invalid input - non-numeric character.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidCharacter() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', 'X'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalOrBinary method with valid binary input.
     */
    @Test
    public void testParseOctalOrBinaryValidBinaryInput() {
        byte[] buffer = new byte[]{0x81, 0x01, 0x02};
        long expected = 2051L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parseOctalOrBinary method with valid octal input.
     */
    @Test
    public void testParseOctalOrBinaryValidOctalInput() {
        byte[] buffer = new byte[]{'0', '1', '7', '0', '5'};
        long expected = 170L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parseBoolean method with valid input.
     */
    @Test
    public void testParseBooleanValidInput() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parseBoolean method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanInvalidInput() {
        byte[] buffer = new byte[]{0};
        TarUtils.parseBoolean(buffer, 0);
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseNameValidInput() {
        byte[] buffer = new byte[]{'a', 'b', 'c', ' ', 0};
        String expected = "abcs";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parseName method with incomplete input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNameIncompleteInput() {
        byte[] buffer = new byte[]{'a', 'b', 0};
        TarUtils.parseName(buffer, 0, buffer.length);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytesValidInput() {
        String name = "MyEntryName";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        offset = TarUtils.formatNameBytes(name, buffer, offset, length);
        Assert.assertEquals(name, new String(buffer, 0, offset));
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalStringValidInput() {
        byte[] buffer = new byte[10];
        long value = 12345;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        Assert.assertEquals("0026111101", new String(buffer));
    }

    /**
     * Test formatUnsignedOctalString method with too long input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringTooLongInput() {
        byte[] buffer = new byte[10];
        long value = 1234567890123L;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    /**
     * Test formatOctalBytes method with valid input.
     */
    @Test
    public void testFormatOctalBytesValidInput() throws IOException {
        byte[] buffer = new byte[13];
        long value = 12345;
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '0', '2', '6', '1', '1', '1', '1', '0', '1', ' ', ' ', '0'};
        Assert.assertArrayEquals(expected, buffer);
    }

    /**
     * Test formatOctalBytes method with too long input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesTooLongInput() throws IOException {
        byte[] buffer = new byte[10];
        long value = 1234567890123L;
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }

    /**
     * Test computeCheckSum method.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "TarFileHeader".getBytes();
        long expected = TarUtils.computeCheckSum(buffer);
        long result = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid octal input.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesValidOctalInput() throws IOException {
        byte[] buffer = new byte[10];
        long value = 12345;
        int offset = 0;
        int length = 10;
        offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, offset, length);
        byte[] expected = new byte[]{'0', '0', '2', '6', '1', '1', '1', '1', '0', '1'};
        Assert.assertArrayEquals(expected, buffer, 0, offset);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid binary input.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesValidBinaryInput() throws IOException {
        byte[] buffer = new byte[13];
        long value = 2051;
        int offset = 0;
        int length = 13;
        offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, offset, length);
        byte[] expected = new byte[]{0x81, 0x01, 0x02, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Assert.assertArrayEquals(expected, buffer, 0, offset);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with too long input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalOrBinaryBytesTooLongInput() throws IOException {
        byte[] buffer = new byte[5];
        long value = 1234567890123L;
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
    }
}