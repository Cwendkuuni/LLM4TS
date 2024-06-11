import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '0', '1'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0175401L, result);

        byte[] invalidBuffer = new byte[]{'0', '8'}; // invalid octal digit
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'T', 'a', 'r', ' ', 'F', 'i', 'l', 'e', '.', 't', 'a', 'r', '\u0000'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("Tar.file", result);

        byte[] invalidBuffer = new byte[]{'T', 'a', 'r', '\u0000'}; // missing terminating null
        try {
            TarUtils.parseName(invalidBuffer, 0, invalidBuffer.length);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "MyFile.tar.gz";
        byte[] buffer = new byte[32];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        // Check that all bytes have been written
        Assert.assertEquals(name.length(), offset);

        // Check that the correct bytes have been written
        for (int i = 0; i < name.length(); ++i) {
            Assert.assertEquals((byte) name.charAt(i), buffer[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 01234567;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        // Check that all bytes have been written
        Assert.assertEquals(10, offset);

        // Check that the correct bytes have been written
        Assert.assertEquals(new String(buffer).trim().equals("01234567"), true);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 01234567;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        // Check that all bytes have been written
        Assert.assertEquals(12, offset);

        // Check that the correct bytes have been written
        String expected = "01234567 \u0020\u0000";
        Assert.assertEquals(new String(buffer, 0, offset).trim(), expected);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'T', 'a', 'r', ' ', 'F', 'i', 'l', 'e', '.', 't', 'a', 'r', '\u0000'};
        long checksum = TarUtils.computeChecksum(buffer);
        long expectedChecksum = 0x14C2F191;
        Assert.assertEquals(expectedChecksum, checksum);
    }
}