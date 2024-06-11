package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '7', '6', '5', '4', '3', '2', '1', '0'};
        long expected = 0177543210L; // octal representation of the number
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalid() {
        byte[] buffer = new byte[]{'0', '1', '2', 'A'}; // invalid octal digit 'A'
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'a', 'r', 'c', 'h', 'i', 'v', 'e', 's', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        String expected = "tar.archives.gz";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test.tar.gz";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        offset = TarUtils.formatNameBytes(name, buffer, offset, length);
        byte[] expected = new byte[]{'t', 'e', 's', 't', '.', 't', 'a', 'r', '.', 'g', 'z'};
        assertArrayEquals(expected, buffer, 0, expected.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        offset = TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '1', '2'};
        assertArrayEquals(expected, buffer, 0, expected.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{'T', 'a', 'r', ' ', 'H', 'e', 'a', 'd', 'e', 'r', ' ', 'F', 'i', 'l', 'e', ' ', 'C', 'h', 'e', 'c', 'k', 's', 'u', 'm', ' ', '0', '1', '2', '3', '4', '5', '6', '7'};
        long expected = 398582453095641L; // computed checksum for the given header
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesTooLong() {
        long value = Long.MAX_VALUE + 1L;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatOctalBytes(value, buf, offset, length);
    }
}