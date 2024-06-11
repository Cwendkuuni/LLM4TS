import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.compress.archivers.tar.TarConstants.MAGIC_NUMBER;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01234567L, result);

        byte[] invalidBuffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '8'};
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] emptyBuffer = new byte[0];
        try {
            TarUtils.parseOctal(emptyBuffer, 0, emptyBuffer.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 'c', (byte) 'o', (byte) 'm', (byte) 'p', (byte) 'r', (byte) 'e', (byte) 's', (byte) 's', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'r', (byte) 'o', (byte) 'j', (byte) 'e', (byte) 'c', (byte) 't', (byte) 's', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tests.compress.project.tar.gz", result);

        byte[] emptyBuffer = new byte[0];
        try {
            TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tests.compress.project.tar.gz";
        byte[] buffer = new byte[name.length() + 1];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{(byte) 't', (byte) 'e', (byte) 's', (byte) 's', (byte) '.', (byte) 'c', (byte) 'o', (byte) 'm', (byte) 'p', (byte) 'r', (byte) 'e', (byte) 's', (byte) 's', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'r', (byte) 'o', (byte) 'j', (byte) 'e', (byte) 'c', (byte) 't', (byte) 's', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
        assertArrayEquals(expectedBuffer, buffer, 0, buffer.length);
        assertEquals(offset, buffer.length);

        String emptyName = "";
        offset = TarUtils.formatNameBytes(emptyName, buffer, 0, buffer.length);
        byte[] expectedEmptyBuffer = new byte[buffer.length];
        assertArrayEquals(expectedEmptyBuffer, buffer, 0, buffer.length);
        assertEquals(offset, buffer.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345678L;
        byte[] buffer = new byte[9];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{(byte) '0', (byte) '0', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8'};
        assertArrayEquals(expectedBuffer, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345678L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{(byte) '0', (byte) '0', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) ' ', (byte) ' '};
        assertArrayEquals(expectedBuffer, buffer, 0, buffer.length);
        assertEquals(offset, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[MAGIC_NUMBER.length + 1];
        System.arraycopy(MAGIC_NUMBER, 0, buffer, 0, MAGIC_NUMBER.length);
        long checkSum = TarUtils.computeCheckSum(buffer);
        assertEquals(MAGIC_NUMBER[0] ^ (MAGIC_NUMBER[1] << 8) ^ (MAGIC_NUMBER[2] << 16), checkSum);
    }
}