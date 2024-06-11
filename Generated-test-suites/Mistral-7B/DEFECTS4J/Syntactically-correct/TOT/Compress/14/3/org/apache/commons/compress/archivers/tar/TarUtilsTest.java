package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.compress.archivers.tar.TarConstants.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '4', '6', '2', '7', ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0x1d2, result);

        buffer = new byte[]{'0', '7', '5', '5', '4', '6', '2', '7', ' '};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0x1d2, result);

        buffer = new byte[]{'0', '1'};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'0', '7', '5'};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[0];
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '4', '6', '2', '7', ' '};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0x1d2, result);

        buffer = new byte[]{'0', '1', '0', '0', '1', '1', '1', '1', '1'};
        result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0x01010101, result);

        buffer = new byte[]{'0', 0x81, '1', '1', '1', '1', '1', '1', '1'};
        result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0x8001010101010101L, result);

        buffer = new byte[]{'0', '1', '0', '0', '1'};
        try {
            result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[0];
        try {
            result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{1};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertTrue(result);

        buffer = new byte[]{0};
        result = TarUtils.parseBoolean(buffer, 0);
        assertFalse(result);

        buffer = new byte[]{1, 1};
        try {
            result = TarUtils.parseBoolean(buffer, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abcdefgih", result);

        buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 0, 0};
        result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abcdefgh", result);

        buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 1};
        try {
            result = TarUtils.parseName(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[0];
        try {
            result = TarUtils.parseName(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcdefghijklmnopqrstuvwxyz";
        byte[] result = new byte[20];

        int offset = TarUtils.formatNameBytes(name, result, 0, result.length);
        assertArrayEquals(new byte[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", 0, 0}, result, 0, offset);

        name = "abc";
        offset = TarUtils.formatNameBytes(name, result, 0, result.length);
        assertArrayEquals(new byte[]{"a", "b", "c", 0, 0}, result, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x123;
        byte[] result = new byte[14];

        TarUtils.formatUnsignedOctalString(value, result, 0, result.length);
        assertArrayEquals(new byte[]{"0", "011", "013", "017", "021", "025", "031", "035", "041", "045", "051", "055", "061", "065"}, result, 0, result.length);

        value = 0;
        TarUtils.formatUnsignedOctalString(value, result, 0, result.length);
        assertArrayEquals(new byte[]{"0"}, result, 0, result.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0x123;
        byte[] result = new byte[11];

        TarUtils.formatOctalBytes(value, result, 0, result.length);
        assertArrayEquals(new byte[]{"0", "011", "013", "017", "021", "025", "031", "035", "041", "045", "051"}, result, 0, result.length);

        value = 0;
        TarUtils.formatOctalBytes(value, result, 0, result.length);
        assertArrayEquals(new byte[0], result, 0, result.length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0x1234567890ABCDEF;
        byte[] result = new byte[11];

        TarUtils.formatLongOctalBytes(value, result, 0, result.length);
        assertArrayEquals(new byte[]{"017", "020", "023", "030", "037", "044", "051", "056", "063", "066", "075"}, result, 0, result.length);

        value = 0;
        TarUtils.formatLongOctalBytes(value, result, 0, result.length);
        assertArrayEquals(new byte[0], result, 0, result.length);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 0x1234567890ABCDEF;
        byte[] result = new byte[11];

        TarUtils.formatLongOctalOrBinaryBytes(value, result, 0, result.length);
        assertArrayEquals(new byte[]{"017", "020", "023", "030", "037", "044", "051", "056", "063", "066", "075"}, result, 0, result.length);

        value = 0x8001234567890ABC;
        TarUtils.formatLongOctalOrBinaryBytes(value, result, 0, result.length);
        assertArrayEquals(new byte[]{"0177", "0200", "0230", "0310", "0377", "0440", "0510", "0557", "0630", "0664", "0775"}, result, 0, result.length);

        value = 0x8000000000000000;
        TarUtils.formatLongOctalOrBinaryBytes(value, result, 0, result.length);
        assertArrayEquals(new byte[]{0x80, 0}, result, 0, result.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{'a', 'b', 'c'};
        long checksum = TarUtils.computeCheckSum(buf);
        assertEquals(0x54c, checksum);

        buf = new byte[]{'a', 'b', 'c', 'd'};
        checksum = TarUtils.computeCheckSum(buf);
        assertEquals(0x195d, checksum);
    }
}