import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '0', '7', '7', '7', '0'};
        long expected = 0177544;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);

        byte[] invalidBuffer = new byte[]{'0', '8', '7', '5', '0', '7', '7', '7', '0'}; // Invalid octal digit
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException when parsing invalid octal digit");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', '0'};
        String expected = "abcde";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "TestName";
        byte[] buffer = new byte[16];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String actual = new String(buffer, 0, offset);
        assertEquals(name, actual);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String actual = new String(buffer, 0, offset);
        String expected = "012345";
        assertEquals(expected, actual);

        long maxValue = Long.MAX_VALUE;
        try {
            TarUtils.formatUnsignedOctalString(maxValue, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException when value too large for octal representation");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String actual = new String(buffer, 0, offset);
        String expected = "012345  ";
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '!'};
        long checkSum = TarUtils.computeCheckSum(buffer);
        long expected = 1703815;
        assertEquals(expected, checkSum);
    }
}