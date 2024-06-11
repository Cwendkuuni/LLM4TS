package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '7', '7', '7', '1', '0', '1', '1', '0'};
        long expected = 0175507771101L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidInput() {
        byte[] buffer = new byte[]{'1', '2', '3'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'o', 'p', 't', 'i', 'o', 'n', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        String expected = "tar.option.tar.gz";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "testName";
        byte[] buffer = new byte[20];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String actual = new String(buffer, 0, offset);
        assertEquals(name, actual);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567890;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "01235670541";
        String actual = new String(buffer, 0, offset);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234567890;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "01235670541  ";
        String actual = new String(buffer, 0, offset);
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'o', 'p', 't', 'i', 'o', 'n', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        long checksum = TarUtils.computeChecksum(buffer);
        long expected = 0x73695468746f2e6f6f6463612e626173656e7465722e677a69736861;
        assertEquals(expected, checksum);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0x73695468746f2e6f6f6463612e626173656e7465722e677a69736861;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        String expected = "115510155436157453654331256314243635703451";
        String actual = new String(buffer, 0, offset);
        assertEquals(expected, actual);
    }
}