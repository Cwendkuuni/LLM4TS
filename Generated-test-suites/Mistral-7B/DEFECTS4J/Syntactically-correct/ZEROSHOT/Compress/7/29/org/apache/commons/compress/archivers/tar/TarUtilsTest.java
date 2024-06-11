import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01234567L, result);

        byte[] invalidBuffer = new byte[]{'0', '1', '2', 'A'};
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', '.', '0', '1', '2'};
        String name = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abcde..012", name);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcdefghijklmnopqrstuvwxyz";
        byte[] buffer = new byte[20];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(name.length(), result);
        String resultString = new String(buffer, 0, result);
        assertEquals(name, resultString);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345678901234567890123L;
        byte[] buffer = new byte[12];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "0123456775236166616565";
        String resultString = new String(buffer, 0, buffer.length);
        assertEquals(expected, resultString);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345678901234567890123L;
        byte[] buffer = new byte[12];
        int result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0123456775236166616565 ";
        String resultString = new String(buffer, 0, result);
        assertEquals(expected, resultString);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};
        long checksum = TarUtils.computeCheckSum(buffer);
        long expectedChecksum = 521L;
        assertEquals(expectedChecksum, checksum);
    }
}