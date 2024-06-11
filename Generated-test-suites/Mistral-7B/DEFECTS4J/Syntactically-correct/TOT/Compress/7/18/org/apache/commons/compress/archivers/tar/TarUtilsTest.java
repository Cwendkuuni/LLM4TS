package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '7', '5', '6', '0'};
        int offset = 0;
        int length = buffer.length;
        long expected = 011237560L;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }

    /**
     * Test parseOctal method with invalid input - non-octal digit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', 'x', '5', '6', '0'};
        int offset = 0;
        int length = buffer.length;
        TarUtils.parseOctal(buffer, offset, length);
    }

    /**
     * Test parseOctal method with leading and trailing spaces.
     */
    @Test
    public void testParseOctal_Spaces() {
        byte[] buffer = new byte[]{' ', '0', '1', '2', '3', '7', '5', '6', '0', ' '};
        int offset = 0;
        int length = buffer.length;
        long expected = 011237560L;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }

    /**
     * Test parseOctal method with offset out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseOctal_OffsetOutOfBounds() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '7', '5', '6', '0'};
        int offset = 8;
        int length = 1;
        TarUtils.parseOctal(buffer, offset, length);
    }

    /**
     * Test parseOctal method with length out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseOctal_LengthOutOfBounds() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '7', '5', '6', '0'};
        int offset = 0;
        int length = -1;
        TarUtils.parseOctal(buffer, offset, length);
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[]{'.'.byteValue(), 't', 'a', 'r', ' ', 'i', 'n', 't', 'e', 'r', 'n', 'a', 'm', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', '.', 'p', 'm', 'g'};
        int offset = 0;
        int length = buffer.length;
        String expected = "tar.internal.name.tar.gz.pmg";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }

    /**
     * Test parseName method with length out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseName_LengthOutOfBounds() {
        byte[] buffer = new byte[]{'.'.byteValue(), 't', 'a', 'r', '.'};
        int offset = 0;
        int length = 2;
        TarUtils.parseName(buffer, offset, length);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "tar.internal.name.tar.gz.pmg";
        byte[] buf = new byte[12];
        int offset = 0;
        int length = 12;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{116, 114, 114, 105, 78, 116, 101, 117, 115, 108, 101, 114, 46, 84, 111, 112, 108, 105, 116, 46, 103, 111, 111, 110, 111, 117, 108, 99, 111}, buf);
    }

    /**
     * Test formatNameBytes method with name longer than buffer.
     */
    @Test
    public void testFormatNameBytes_NameLongerThanBuffer() {
        String name = "tar.internal.name.tar.gz.pmg.longer";
        byte[] buf = new byte[12];
        int offset = 0;
        int length = 12;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{116, 114, 114, 105, 78, 116, 101, 117, 115, 108, 101, 114, 46, 84, 111, 112, 108, 105, 116, 46, 103, 111, 111, 110, 111, 117, 108, 99, 111, 0, 0, 0, 0, 0, 0, 0, 0, 0}, buf);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        byte[] buffer = new byte[10];
        long value = 1234567890L;
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 4, 5, 6, 7}, buffer);
    }

    /**
     * Test formatUnsignedOctalString method with value longer than buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_ValueLongerThanBuffer() {
        byte[] buffer = new byte[5];
        long value = 123456789012345L;
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    /**
     * Test formatOctalBytes method with valid input.
     */
    @Test
    public void testFormatOctalBytes_ValidInput() {
        byte[] buffer = new byte[13];
        long value = 1234567890123L;
        int offset = 0;
        int length = 13;
        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 5, 6}, buffer);
    }

    /**
     * Test formatOctalBytes method with value longer than buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_ValueLongerThanBuffer() {
        byte[] buffer = new byte[7];
        long value = 123456789012345L;
        int offset = 0;
        int length = 7;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
    }

    /**
     * Test computeCheckSum method with valid input.
     */
    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e'};
        long expected = (255 & 0x61) + ((255 & 0x62) << 8) + ((255 & 0x63) << 16) + ((255 & 0x64) << 24) + ((255 & 0x65) << 32);
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }

    /**
     * Test computeCheckSum method with empty buffer.
     */
    @Test
    public void testComputeCheckSum_EmptyBuffer() {
        byte[] buffer = new byte[0];
        long expected = 0;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }
}