package org.apache.commons.compress.archivers.tar.test;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.tar.TarUtils;

import org.junit.Test;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '4', '4', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(017440L, result);

        buffer = new byte[]{'0', '1', '7', '7', '5', '5', '0'};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01177550L, result);

        buffer = new byte[]{'0'};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }

        buffer = new byte[]{'5', '5', '5', '5', '5', (byte) 0x80};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'T','a','r','.'};
        String name = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tar.", name);

        buffer = new byte[]{'T','a','r','/','M','y','/','S','p','a','c','e','/','M','y','File'};
        name = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tar/My/Space/MyFile", name);

        buffer = new byte[]{'T','a','r','.'};
        try {
            TarUtils.parseName(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "MyFile";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, offset);
        for (int i = 0; i < 10; i++) {
            assertEquals(name.charAt(i), (char) (buffer[i] & 0xFF));
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0123456;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals("123456", new String(buffer, 0, offset));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0123456;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("0123456 ", new String(buffer, 0, offset));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'T','a','r','.'};
        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        for (int i = 0; i < buffer.length; i++) {
            computedChecksum += TarUtils.BYTE_MASK & buffer[i];
        }
        assertEquals(checksum, computedChecksum);
    }
}