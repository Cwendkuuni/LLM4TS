package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.compress.archivers.tar.TarUtils.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '4', '4', '0', '7', '5', '5', '0', '7'};
        long expected = 017447550L;
        long result = parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);

        byte[] invalidBuffer = new byte[]{'0', '7', '4', '4', '0', '7', '5', '5', '0', '8'}; // Invalid byte
        try {
            parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] emptyBuffer = new byte[0];
        try {
            parseOctal(emptyBuffer, 0, emptyBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'T', (byte) 'a', (byte) 'r', (byte) ' ', (byte) 'E', (byte) 'n',
                (byte) 't', (byte) 'r', (byte) 'y', (byte) ' ', (byte) 'L', (byte) 'e', (byte) 'v', (byte) 'e',
                (byte) 'l', (byte) ' ', (byte) 'A', (byte) 'r', (byte) 'c', (byte) 'h', (byte) 'i', (byte) 'v',
                (byte) 'e', (byte) 's', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g',
                (byte) 'z', (byte) '/'};
        String expected = "Tar Entry Level Archives";
        String result = parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);

        byte[] invalidBuffer = new byte[0];
        try {
            parseName(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Tar Entry Level Archives";
        byte[] buffer = new byte[256];
        int offset = 0;
        int length = name.length();
        offset = formatNameBytes(name, buffer, offset, length);

        for (int i = 0; i < length; ++i) {
            assertEquals((byte) name.charAt(i), buffer[offset + i]);
        }

        assertEquals(0, buffer[offset + length]); // Trailing NUL
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        formatUnsignedOctalString(value, buffer, offset, length);

        for (int i = 0; i < length; ++i) {
            if (i >= 6) {
                assertEquals((byte) '0', buffer[offset + i]);
            } else {
                int digit = (int) (value & 7);
                value >>= 3;
                assertEquals((byte) ('0' + digit), buffer[offset + i]);
            }
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 12;

        formatOctalBytes(value, buffer, offset, length);

        // Check trailing space and NUL
        assertEquals((byte) ' ', buffer[offset + length - 2]);
        assertEquals(0, buffer[offset + length - 1]);

        for (int i = offset; i < offset + length - 2; ++i) {
            int digit = (int) (value & 7);
            value >>= 3;
            assertEquals((byte) ('0' + digit), buffer[i]);
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[13];
        int offset = 0;
        int length = 13;

        formatLongOctalBytes(value, buffer, offset, length);

        // Check trailing space
        assertEquals((byte) ' ', buffer[offset + length - 1]);

        for (int i = offset; i < offset + length - 1; ++i) {
            int digit = (int) (value & 7);
            value >>= 3;
            assertEquals((byte) ('0' + digit), buffer[i]);
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 12;

        formatCheckSumOctalBytes(value, buffer, offset, length);

        // Check trailing NUL and space
        assertEquals(0, buffer[offset + length - 2]);
        assertEquals((byte) ' ', buffer[offset + length - 1]);

        for (int i = offset; i < offset + length - 2; ++i) {
            int digit = (int) (value & 7);
            value >>= 3;
            assertEquals((byte) ('0' + digit), buffer[i]);
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long expected = 511;
        long result = computeCheckSum(buffer);
        assertEquals(expected, result);
    }
}