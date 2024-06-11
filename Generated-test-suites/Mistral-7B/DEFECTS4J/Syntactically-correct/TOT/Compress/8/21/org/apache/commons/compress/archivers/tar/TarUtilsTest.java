package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(017550L, result);

        byte[] invalidBuffer = new byte[]{'0', '7', '5', '5', '8'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length));

        byte[] emptyBuffer = new byte[0];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(emptyBuffer, 0, emptyBuffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tar file", result);

        byte[] invalidBuffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', ' '};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(invalidBuffer, 0, invalidBuffer.length));

        byte[] emptyBuffer = new byte[0];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "testFile";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, offset);
        String result = new String(buffer, 0, 10);
        assertEquals("testFile", result);

        byte[] invalidBuffer = new byte[1];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatNameBytes(name, invalidBuffer, 0, invalidBuffer.length));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 17550;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals("017550", new String(buffer));

        long invalidValue = 17550L + 1;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(invalidValue, buffer, 0, buffer.length));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 17550;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(12, offset);
        String result = new String(buffer, 0, 11);
        assertEquals("017550 ", result);

        long invalidValue = 17550L + 1;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatOctalBytes(invalidValue, buffer, 0, buffer.length));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0, 1, 2, 3, 4};
        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        for (byte b : buffer) {
            computedChecksum += TarUtils.BYTE_MASK & b;
        }
        assertEquals(checksum, computedChecksum);

        byte[] emptyBuffer = new byte[0];
        long emptyChecksum = TarUtils.computeChecksum(emptyBuffer);
        assertEquals(0, emptyChecksum);
    }
}