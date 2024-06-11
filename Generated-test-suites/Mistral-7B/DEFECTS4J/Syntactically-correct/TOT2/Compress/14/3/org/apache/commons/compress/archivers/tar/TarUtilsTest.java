import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidOctalString() {
        byte[] buffer = "0777".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0777L, result);
    }

    @Test
    public void testParseOctal_InvalidOctalString() {
        byte[] buffer = "0123a".getBytes();
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctal_EmptyString() {
        byte[] buffer = new byte[0];
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result);
    }

    @Test
    public void testParseOctalOrBinary_ValidBinaryString() {
        byte[] buffer = {(byte) 0xF0, (byte) 0x5A};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(60484L, result);
    }

    @Test
    public void testParseOctalOrBinary_ValidOctalString() {
        byte[] buffer = "0777".getBytes();
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0777L, result);
    }

    @Test
    public void testParseOctalOrBinary_InvalidBinaryString() {
        byte[] buffer = {(byte) 0xF0, (byte) 0xAA};
        try {
            TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctalOrBinary_ValidOctalStringWithLeadingZero() {
        byte[] buffer = "0777".getBytes();
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0777L, result);
    }

    @Test
    public void testParseBoolean_True() {
        byte[] buffer = new byte[]{(byte) 0xFF};
        assertTrue(TarUtils.parseBoolean(buffer, 0));
    }

    @Test
    public void testParseBoolean_False() {
        byte[] buffer = new byte[]{(byte) 0x00};
        assertFalse(TarUtils.parseBoolean(buffer, 0));
    }

    @Test
    public void testParseName_ValidName() {
        byte[] buffer = "test.txt".getBytes();
        assertEquals("test.txt", TarUtils.parseName(buffer, 0, buffer.length));
    }

    @Test
    public void testParseName_InvalidName() {
        byte[] buffer = new byte[]{(byte) 0x00, (byte) 0x1A};
        try {
            TarUtils.parseName(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatNameBytes_ValidName() {
        String name = "test.txt";
        byte[] buffer = new byte[TarConstants.NAMELEN];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("test.txt", new String(buffer, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString_ValidValue() {
        long value = 12345;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length-2);
        assertEquals("012345", new String(buffer, 0, buffer.length));
    }

    @Test
    public void testFormatUnsignedOctalString_InvalidValue() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        try {
            TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length-2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatOctalBytes_ValidValue() {
        long value = 12345;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("012345 ", new String(buffer, 0, buffer.length));
    }

    @Test
    public void testFormatLongOctalBytes_ValidValue() {
        long value = 1234567890123L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("0123456757654312", new String(buffer, 0, buffer.length));
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes_ValidValue() {
        long value = 1234567890123L;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        byte lastByte = buffer[buffer.length - 1];
        // First byte should be 0x80 if long value exceeds octal string length
        assertTrue(lastByte > 0x7F);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes_ValidBinaryValue() {
        long value = 1234567890123L;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(value, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "tarfile".getBytes();
        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        for (byte b : buffer) {
            computedChecksum += TarUtils.BYTE_MASK & b;
        }
        assertEquals(checksum, computedChecksum);
    }
}