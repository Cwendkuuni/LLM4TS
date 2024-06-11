import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctalPositive() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '4', '7', '6', '7', '6', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0175547765555555555555555555555547676555555555555555555555555555555555555555555555L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte1() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '4', '7', '6', '7', '6', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', 'A'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalMissingTrailingSpace() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '4', '7', '6', '7', '6', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abcdefgihjklmnopqrstuvwxyz", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcdefgihjklmnopqrstuvwxyz";
        byte[] buffer = new byte[25];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 0};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalStringPositive() {
        long value = 1234567890123L;
        byte[] buffer = new byte[13];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '1', '1', '1', '1', '1', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '0', '1', '2', '3', '0', '1', '2', '3', '0', '1', '2', '3', '0'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringOverflow() {
        long value = Long.MAX_VALUE + 1L;
        byte[] buffer = new byte[25];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytesPositive() {
        long value = 1234567890123L;
        byte[] buffer = new byte[13];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '1', '1', '1', '1', '1', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '0', '1', '2', '3', '0', '1', '2', '3', '0', '1', '2', '3', ' '};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesOverflow() {
        long value = Long.MAX_VALUE + 1L;
        byte[] buffer = new byte[25];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88, 0x99};
        long checksum = TarUtils.computeCheckSum(buffer);
        long computed = TarUtils.computeCheckSum(new byte[]{TarUtils.computeCheckSum(buffer) & 0xFF, ' '});
        assertEquals(checksum, computed);
    }

    @Test
    public void testParseOctalNegative() {
        byte[] buffer = new byte[]{'0', '1', '1', '1', '1', '1', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '0', '1', '2', '3', '0', '1', '2', '3', '0', '1', '2', '3', '5'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(-1234567890123L, result);
    }
}