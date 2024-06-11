import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * TarUtilsTest class for testing methods in TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test method for {@link TarUtils#parseOctal(byte[], int, int)}
     * with valid input.
     */
    @Test
    public void testParseOctalValidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        long expected = 0123456L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TarUtils#parseOctal(byte[], int, int)}
     * with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'1', '2', '3'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test method for {@link TarUtils#parseOctalOrBinary(byte[], int, int)}
     * with valid binary input.
     */
    @Test
    public void testParseOctalOrBinaryValidBinaryInput() {
        byte[] buffer = new byte[]{(byte) 0x81, 0x2a, 0x00, 0x40};
        long expected = 42L << 8 | 0x2a;
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TarUtils#parseOctalOrBinary(byte[], int, int)}
     * with valid octal input.
     */
    @Test
    public void testParseOctalOrBinaryValidOctalInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        long expected = 0123456L;
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TarUtils#parseOctalOrBinary(byte[], int, int)}
     * with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidInput() {
        byte[] buffer = new byte[]{'1', '2', '3'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test method for {@link TarUtils#parseBoolean(byte[], int)}
     * with true input.
     */
    @Test
    public void testParseBooleanTrueInput() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean actual = TarUtils.parseBoolean(buffer, 0);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TarUtils#parseBoolean(byte[], int)}
     * with false input.
     */
    @Test
    public void testParseBooleanFalseInput() {
        byte[] buffer = new byte[]{0};
        boolean expected = false;
        boolean actual = TarUtils.parseBoolean(buffer, 0);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TarUtils#parseName(byte[], int, int)}
     * with valid input.
     */
    @Test
    public void testParseNameValidInput() {
        byte[] buffer = new byte[]{(byte) 0x65, (byte) 0x6e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x65, (byte) 0x6e,
             (byte) 0x74, (byte) 0x2e, (byte) 0x74, (byte) 0x65, (byte) 0x78, (byte) 0x74, (byte) 0x2e, (byte) 0x63,
             (byte) 0x61, (byte) 0x6d, (byte) 0x65, (byte) 0x2e};
        String expected = "hello.tar.gz";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TarUtils#parseName(byte[], int, int)}
     * with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNameInvalidInput() {
        byte[] buffer = new byte[]{'1', '2', '3'};
        TarUtils.parseName(buffer, 0, buffer.length);
    }

    /**
     * Test method for {@link TarUtils#formatNameBytes(String, byte[], int, int)}
     * with valid input.
     */
    @Test
    public void testFormatNameBytesValidInput() {
        String name = "hello.tar.gz";
        byte[] buffer = new byte[TarConstants.NAMELEN];
        int offset = 0;
        int length = TarConstants.NAMELEN;
        TarUtils.formatNameBytes(name, buffer, offset, length);
        String expected = "hello.tar.gz";
        String actual = new String(buffer, 0, length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TarUtils#formatUnsignedOctalString(long, byte[], int, int)}
     * with valid input.
     */
    @Test
    public void testFormatUnsignedOctalStringValidInput() {
        long value = 0123456;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        int offset = 0;
        int length = TarConstants.UIDLEN;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expected = "0123456";
        String actual = new String(buffer, 0, length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TarUtils#formatOctalBytes(long, byte[], int, int)}
     * with valid input.
     */
    @Test
    public void testFormatOctalBytesValidInput() {
        long value = 0123456;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        int offset = 0;
        int length = TarConstants.UIDLEN + 2;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        String expected = "0123456 ";
        String actual = new String(buffer, 0, length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TarUtils#computeCheckSum(byte[])}
     * with valid input.
     */
    @Test
    public void testComputeCheckSumValidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5'};
        long checksumExpected = TarUtils.computeChecksum(buffer);
        long checksumActual = TarUtils.computeChecksum(buffer);
        Assert.assertEquals(checksumExpected, checksumActual);
    }
}