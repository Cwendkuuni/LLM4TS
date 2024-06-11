package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '0', '7', '5', '5', '0', '1', '2', '0', ' '};
        long expected = 01007550120L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseOctal_InvalidInput() {
        byte[] buffer = new byte[]{'0', '0', '7', '5', '5', 'X', '1', '2', '0', ' '};
        Assert.throwException(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseOctal_LengthExceeded() {
        byte[] buffer = new byte[1024];
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) ' ', (byte) 'w', (byte) 'o', (byte) 'r', (byte) 'd', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '\u0000'};
        String expected = "hello.tar.gz.zip";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseName_LengthExceeded() {
        byte[] buffer = new byte[1024];
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(buffer, 0, buffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello.tar.gz.zip";
        byte[] buffer = new byte[16];
        int offset = 0;
        int length = 16;
        int actual = TarUtils.formatNameBytes(name, buffer, offset, length);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
        ByteStreams.write(buffer, offset, length - (actual - offset));
        String actualName = bos.toString(StandardCharsets.UTF_8);
        Assert.assertEquals(name, actualName);
    }

    @Test
    public void testFormatNameBytes_LengthExceeded() {
        String name = "hello.tar.gz.zip";
        byte[] buffer = new byte[1];
        int offset = 0;
        int length = 1;
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.formatNameBytes(name, buffer, offset, length));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 01007550120L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = new byte[]{'0', '1', '0', '0', '7', '5', '5', '0', '1', '2'};
        Assert.assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString_LengthExceeded() {
        long value = 01007550120L;
        byte[] buffer = new byte[1];
        int offset = 0;
        int length = 1;
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(value, buffer, offset, length));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 01007550120L;
        byte[] buffer = new byte[13];
        int offset = 0;
        int length = 13;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        byte[] expected = new byte[]{'0', '1', '0', '0', '7', '5', '5', '0', '1', '2', ' ', '0'};
        Assert.assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatOctalBytes_LengthExceeded() {
        long value = 01007550120L;
        byte[] buffer = new byte[1];
        int offset = 0;
        int length = 1;
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.formatOctalBytes(value, buffer, offset, length));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o'};
        long expected = 12345;
        long actual = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testComputeCheckSum_EmptyBuffer() {
        byte[] buffer = {};
        long expected = 0;
        long actual = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, actual);
    }
}