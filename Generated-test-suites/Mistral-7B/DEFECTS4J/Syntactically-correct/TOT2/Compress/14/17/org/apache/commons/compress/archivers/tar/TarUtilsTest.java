import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(07550L, result);
    }

    @Test
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', 'X', ' '}; // X is an invalid byte
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctalMissingTrailingSpace() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            assertTrue(message.contains("trailing space/NUL is missing"));
        }
    }

    @Test
    public void testParseOctalAllNuls() {
        byte[] buffer = new byte[0];
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result);
    }

    @Test
    public void testParseOctalOrBinaryLeadingNul() {
        byte[] buffer = new byte[]{'0'};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0L, result);
    }

    @Test
    public void testParseOctalOrBinaryValidBinaryNumber() {
        byte[] buffer = new byte[]{(byte) 0xFF, (byte) 0x7F, (byte) 0x7F};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0xFF7F7FL, result);
    }

    @Test
    public void testParseOctalOrBinaryLongValue() {
        long value = 0xFFFF_FFFF_FFFF_FFFFL;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        for (int i = 0; i < TarConstants.UIDLEN; i++) {
            buffer[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        long result = TarUtils.parseOctalOrBinary(buffer, 0, TarConstants.UIDLEN);
        assertEquals(value, result);
    }

    @Test
    public void testParseOctalOrBinaryInvalidBinaryNumber() {
        long value = 0xFFFF_FFFF_FFFF_FFFFL + 1;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        for (int i = 0; i < TarConstants.UIDLEN; i++) {
            buffer[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        try {
            TarUtils.parseOctalOrBinary(buffer, 0, TarConstants.UIDLEN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            assertTrue(message.contains("At offset " + TarConstants.UIDLEN));
            assertTrue(message.contains("binary number exceeds maximum signed long value"));
        }
    }

    @Test
    public void testParseBooleanTrue() {
        byte[] buffer = new byte[]{1};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertTrue(result);
    }

    @Test
    public void testParseBooleanFalse() {
        byte[] buffer = new byte[]{0};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertFalse(result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{32, 'M', 'y', ' ', 'F', 'i', 'l', 'e', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("My File", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "My File";
        byte[] buffer = new byte[TarConstants.NAMELEN];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, TarConstants.NAMELEN);
        assertEquals(offset, 11);
        assertArrayEquals(new byte[]{"M", "y", " ", "F", "i", "l", "e", 0, 0, 0, 0, 0}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 07550L;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, TarConstants.UIDLEN);
        assertArrayEquals(new byte[]{'0', '1', '0', '1', '0', '1', '1', '0', '0', '7', '5', '5'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 07550L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatOctalBytes(value, buffer, 0, TarConstants.UIDLEN + 2);
        assertArrayEquals(new byte[]{'0', '1', '0', '1', '0', '1', '1', '0', '0', '7', '5', '5', ' ', ' ', 0}, buffer);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 0xFFFF_FFFF_FFFF_FFFFL;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, TarConstants.UIDLEN);
        assertArrayEquals(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}, buffer);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytesInvalidLength() {
        long value = 0xFFFF_FFFF_FFFF_FFFFL;
        byte[] buffer = new byte[TarConstants.UIDLEN - 1];
        try {
            TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, TarConstants.UIDLEN - 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};
        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        for (byte b : buffer) {
            computedChecksum += TarUtils.BYTE_MASK & b;
        }
        assertEquals(checksum, computedChecksum);
    }
}