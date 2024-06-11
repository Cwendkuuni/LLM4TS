import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '0', ' '};
        long expected = 017750;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidDigit() {
        byte[] buffer = new byte[]{'0', '7', 'x', '5', '0', ' '};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithLengthExceeded() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '0'};
        TarUtils.parseOctal(buffer, 0, buffer.length + 1);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'T', 'e', 's', 't', '.', 'c', 'o', 'n', 'f', 'g', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', ' ', '0', ' ', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        String expected = "tar.Test.gzip 0 1234567890";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNameWithTrailingNul() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'T', 'e', 's', 't', '.', 'c', 'o', 'n', 'f', 'g', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', ' ', '0', ' ', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '\u0000'};
        TarUtils.parseName(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNameWithLengthExceeded() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'T', 'e', 's', 't', '.', 'c', 'o', 'n', 'f', 'g', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', ' ', '0', ' ', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        TarUtils.parseName(buffer, 0, buffer.length);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.Test.gzip";
        byte[] buffer = new byte[12];
        int expectedOffset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = {'t', 'a', 'r', '.', 'T', 'e', 's', 't', '.', 'g', 'z', 'i', 'p', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000'};
        assertArrayEquals(expected, buffer, 0, expected.length);
        assertEquals(expectedOffset, 12);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567;
        byte[] buffer = new byte[10];
        int offset = 8;
        int length = 3;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = {'0', '1', '1', '1', '1', '0', '5', '6', '7', '\u0000'};
        assertArrayEquals(expected, buffer, 0, expected.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWithValueTooLarge() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        int offset = 8;
        int length = 3;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234567;
        byte[] buffer = new byte[12];
        int offset = 8;
        int length = 3;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        byte[] expected = {'0', '1', '1', '1', '1', '0', '5', '6', '7', ' ', ' ', '\u0000'};
        assertArrayEquals(expected, buffer, 0, expected.length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567890;
        byte[] buffer = new byte[12];
        int offset = 8;
        int length = 3;
        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        byte[] expected = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' '};
        assertArrayEquals(expected, buffer, 0, expected.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890;
        byte[] buffer = new byte[12];
        int offset = 8;
        int length = 3;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        byte[] expected = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' '};
        byte[] checkSumBytes = TarUtils.computeCheckSum(buffer, 0, offset - 1) > 0 ? new byte[]{0} : new byte[0];
        byte[] expectedWithCheckSum = new byte[13];
        System.arraycopy(expected, 0, expectedWithCheckSum, 0, expected.length);
        System.arraycopy(checkSumBytes, 0, expectedWithCheckSum, expected.length, checkSumBytes.length);
        assertArrayEquals(expectedWithCheckSum, buffer, 0, expectedWithCheckSum.length);
    }
}