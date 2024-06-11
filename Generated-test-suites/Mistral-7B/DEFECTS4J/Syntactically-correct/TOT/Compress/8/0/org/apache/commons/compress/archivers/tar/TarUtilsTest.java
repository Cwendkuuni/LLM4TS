import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
        long expected = 01234567;
        long actual = TarUtils.parseOctal(buffer, 0, 9);
        assertEquals(expected, actual);

        byte[] invalidBuffer = new byte[]{'0', '1', '2', '3', '4', '8', '5', '6', '7'};
        try {
            TarUtils.parseOctal(invalidBuffer, 0, 9);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] emptyBuffer = new byte[0];
        try {
            TarUtils.parseOctal(emptyBuffer, 0, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'};
        String expected = "abcdefghi";
        String actual = TarUtils.parseName(buffer, 0, 9);
        assertEquals(expected, actual);

        byte[] invalidBuffer = new byte[]{'a', 'b'};
        try {
            TarUtils.parseName(invalidBuffer, 0, 2);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // expected
        }

        byte[] truncatedBuffer = new byte[]{'a', 'b', 'c'};
        String actualTruncated = TarUtils.parseName(truncatedBuffer, 0, 3);
        assertEquals("abc", actualTruncated);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcdefghij";
        byte[] buffer = new byte[15];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, buffer, 0, offset);

        String truncatedName = "abcdef";
        TarUtils.formatNameBytes(truncatedName, buffer, 0, buffer.length);
        byte[] expectedTruncated = new byte[]{'a', 'b', 'c', 'd', 'e', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedTruncated, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567;
        byte[] buffer = new byte[11];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '0', '1', '2', '3', '4', '5', '6', '7', '0', '1'};
        assertArrayEquals(expected, buffer, 0, buffer.length);

        long valueTooBig = Long.MAX_VALUE + 1;
        try {
            TarUtils.formatUnsignedOctalString(valueTooBig, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234567;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '0', '1', '2', '3', '4', '5', '6', '7', ' ', '0'};
        assertArrayEquals(expected, buffer, 0, offset);

        long valueTooBig = Long.MAX_VALUE + 1;
        try {
            TarUtils.formatOctalBytes(valueTooBig, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};
        long expected = TarUtils.computeCheckSum(buffer);
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, actual);
    }
}