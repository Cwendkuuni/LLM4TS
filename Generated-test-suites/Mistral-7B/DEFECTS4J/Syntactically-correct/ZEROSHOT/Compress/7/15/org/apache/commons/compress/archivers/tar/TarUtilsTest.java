import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5'};
        int offset = 0;
        int length = 6;

        long expected = 012345L;
        long actual = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidInput() {
        byte[] buffer = new byte[]{'1', '2', 'X'};
        int offset = 0;
        int length = 3;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'m', 'y', 'T', 'a', 'r', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', ' ', ' ', 0};
        int offset = 0;
        int length = 14;

        String expected = "my.tar.gz";
        String actual = TarUtils.parseName(buffer, offset, length);

        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "my.tar.gz";
        byte[] buffer = new byte[14];
        int offset = 0;
        int length = 14;

        int actual = TarUtils.formatNameBytes(name, buffer, offset, length);

        byte[] expected = new byte[]{'m', 'y', '.', 't', 'a', 'r', '.', 'g', 'z', ' ', ' ', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, buffer, 0, buffer.length);

        assertEquals(actual, offset + length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        byte[] expected = new byte[]{'0', '0', '0', '0', '1', '2', '3', '4', '5', 0};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 12;

        int actual = TarUtils.formatOctalBytes(value, buffer, offset, length);

        byte[] expected = new byte[]{'0', '0', '0', '0', '1', '2', '3', '4', '5', ' ', ' ', 0};
        assertArrayEquals(expected, buffer, 0, buffer.length);

        assertEquals(actual, offset + length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'m', 'y', 'T', 'a', 'r', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', ' ', ' ', 0};
        long expected = 2143856108L;
        long actual = TarUtils.computeCheckSum(buffer);

        assertEquals(expected, actual);
    }
}