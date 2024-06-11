import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(07550L, result);

        byte[] invalidBuffer = new byte[]{'0', '7', '5', '5', 'x'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length));
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] binaryBuffer = new byte[]{(byte) 0x80, (byte) 0x1a, 0x2b, 0x05};
        long result = TarUtils.parseOctalOrBinary(binaryBuffer, 0, binaryBuffer.length);
        assertEquals(327661L, result);

        byte[] invalidOctalBuffer = new byte[]{'0', '7', '5', '5', 'x'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(invalidOctalBuffer, 0, invalidOctalBuffer.length));
    }

    @Test
    public void testParseBoolean() {
        byte[] bufferTrue = new byte[]{1};
        boolean resultTrue = TarUtils.parseBoolean(bufferTrue, 0);
        assertTrue(resultTrue);

        byte[] bufferFalse = new byte[]{0};
        boolean resultFalse = TarUtils.parseBoolean(bufferFalse, 0);
        assertFalse(resultFalse);
    }

    @Test
    public void testParseName() {
        byte[] nameBuffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', 0};
        String result = TarUtils.parseName(nameBuffer, 0, nameBuffer.length);
        assertEquals("tar file", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test name";
        byte[] buffer = new byte[TarConstants.NAME_LENGTH];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, TarConstants.NAME_LENGTH);
        assertEquals(TarConstants.NAME_LENGTH, offset);

        for (int i = 0; i < TarConstants.NAME_LENGTH; i++) {
            assertEquals(name.charAt(i), buffer[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, TarConstants.UIDLEN);

        for (int i = 0; i < TarConstants.UIDLEN; i++) {
            assertEquals('0', buffer[i]);
            if (i == TarConstants.UIDLEN - 4) {
                assertEquals('5', buffer[i]);
            }
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatOctalBytes(value, buffer, 0, TarConstants.UIDLEN + 2);

        for (int i = 0; i < TarConstants.UIDLEN; i++) {
            assertEquals('0', buffer[i]);
            if (i == TarConstants.UIDLEN - 4) {
                assertEquals('5', buffer[i]);
            }
        }
        assertEquals(' ', buffer[TarConstants.UIDLEN]);
        assertEquals(' ', buffer[TarConstants.UIDLEN + 1]);
        assertEquals(0, buffer[TarConstants.UIDLEN + 2]);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 1234567890123L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, TarConstants.UIDLEN + 2);

        if (value < 0) {
            for (int i = 0; i < TarConstants.UIDLEN; i++) {
                assertEquals('7', buffer[i]);
            }
            assertEquals(' ', buffer[TarConstants.UIDLEN]);
            assertEquals(' ', buffer[TarConstants.UIDLEN + 1]);
            assertEquals(' ', buffer[TarConstants.UIDLEN + 2]);
        } else {
            for (int i = 0; i < TarConstants.UIDLEN; i++) {
                assertEquals('0', buffer[i]);
                if (i == TarConstants.UIDLEN - 3) {
                    assertEquals('7', buffer[i]);
                }
            }
            assertEquals(' ', buffer[TarConstants.UIDLEN]);
            assertEquals(' ', buffer[TarConstants.UIDLEN + 1]);
            assertEquals('1', buffer[TarConstants.UIDLEN + 2]);
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e'};
        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        for (byte b : buffer) {
            computedChecksum += (long) (255 & b);
        }
        assertEquals(checksum, computedChecksum);
    }
}