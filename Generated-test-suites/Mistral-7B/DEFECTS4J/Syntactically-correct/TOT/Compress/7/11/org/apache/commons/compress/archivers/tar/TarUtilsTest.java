package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals("Parsing valid octal string", 01755L, result);

        buffer = new byte[]{'0', '1', '0', ' ', '2', '5'};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals("Parsing invalid octal string", -1, result);

        buffer = new byte[]{'0', '1', '0', '1', '2', '5'};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals("Parsing valid octal string with trailing space", 010125L, result);

        buffer = new byte[]{'5', ' ', '2', '5'};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Parsing octal string with leading space should have failed");
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{' '};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Parsing empty string should have failed");
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{'A'};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Parsing non-octal string should have failed");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'m', 'y', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Parsing valid entry name", "my file.tar.gz", result);

        buffer = new byte[]{'m', 'y', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.'};
        try {
            result = TarUtils.parseName(buffer, 0, buffer.length);
            fail("Parsing entry name without extension should have failed");
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{'m', 'y', ' ', 'f', 'i', 'l', 'e', '.'};
        try {
            result = TarUtils.parseName(buffer, 0, buffer.length);
            fail("Parsing entry name without extension and trailing null should have failed");
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{' '};
        try {
            result = TarUtils.parseName(buffer, 0, buffer.length);
            fail("Parsing empty string should have failed");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "my file.tar.gz";
        byte[] buffer = new byte[256];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'m', 'y', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals("Formatting valid entry name", expected, buffer, 0, offset);

        name = "";
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals("Formatting empty string", new byte[0], buffer, 0, offset);

        name = "long name";
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expectedShortName = new byte[]{'l', 'o', 'n', 'g', ' ', 'n', 'a', 'm', 0, 0, 0, 0};
        assertArrayEquals("Formatting entry name longer than buffer", expectedShortName, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x12345678;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '1', '7', '4', '6', '7', '5', '6', '7', '8', 0, 0, 0};
        assertArrayEquals("Formatting valid unsigned octal number", expected, buffer, 0, offset);

        value = 0xFFFFFFFFL;
        try {
            offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
            fail("Formatting unsigned octal number larger than buffer should have failed");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0x12345678;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '7', '4', '6', '7', '5', '6', '7', '8', ' ', 0, 0};
        assertArrayEquals("Formatting valid octal number with trailing space", expected, buffer, 0, offset);

        value = 0xFFFFFFFFL;
        try {
            offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
            fail("Formatting octal number larger than buffer should have failed");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0x1234567890ABCDEF;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '7', '7', '5', '6', '7', '8', '9', '0', ' ', 0};
        assertArrayEquals("Formatting valid long octal number with trailing space", expected, buffer, 0, offset);

        value = 0xFFFFFFFFFFFFFFFFL;
        try {
            offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
            fail("Formatting long octal number larger than buffer should have failed");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0x12345678;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', 0, ' ', 0};
        assertArrayEquals("Formatting valid checksum octal number with trailing space and null", expected, buffer, 0, offset);

        value = 0xFFFFFFFFFFFFFFFFL;
        try {
            offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
            fail("Formatting checksum octal number larger than buffer should have failed");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04};
        long checksum = TarUtils.computeCheckSum(buffer);
        long expected = (0x01 << 8) | (0x02 & 0xFF) | ((0x03 & 0xFF) << 8) | ((0x04 & 0xFF) << 16);
        assertEquals("Computing checksum of valid buffer", expected, checksum);

        buffer = new byte[0];
        checksum = TarUtils.computeCheckSum(buffer);
        assertEquals("Computing checksum of empty buffer", 0, checksum);

        buffer = new byte[]{0x01, (byte) 0xFF};
        try {
            checksum = TarUtils.computeCheckSum(buffer);
            fail("Computing checksum of buffer larger than byte should have failed");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}