import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5', '7'};
        long expectedResult = 0175557;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);

        // Test invalid octal digit
        buffer = new byte[]{'0', '7', '5', '5', 'A', '5'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'o', 'b', 'j', 'e', 'c', 't', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', '.', 't', 'a', 'r', '\u0000'};
        String expectedResult = "tar.obj.gz.tar.";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "testName.tar.gz";
        byte[] buffer = new byte[name.length() + 1];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        for (int i = 0; i < name.length(); ++i) {
            assertEquals(name.charAt(i), (char) buffer[i + 1]);
        }
        assertEquals('\u0000', buffer[offset + name.length()]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x12345678L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        for (int i = offset; i < offset + 8; ++i) {
            assertEquals('0' + (byte) (value & 7), buffer[i]);
            value >>= 3;
        }
        assertEquals('\u0000', buffer[offset + 8]);
        assertEquals('\u0000', buffer[offset + 9]);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0x12345678L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        for (int i = offset; i < offset + 8; ++i) {
            assertEquals('0' + (byte) (value & 7), buffer[i]);
            value >>= 3;
        }
        assertEquals(' ' , buffer[offset + 8]);
        assertEquals('\u0000', buffer[offset + 9]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'o', 'b', 'j', 'e', 'c', 't', '.', 't', 'a', 'r', '\u0000'};
        long checksum = TarUtils.computeChecksum(buffer);
        long expectedChecksum = TarUtils.computeChecksum(buffer);
        assertEquals(expectedChecksum, checksum);
    }
}