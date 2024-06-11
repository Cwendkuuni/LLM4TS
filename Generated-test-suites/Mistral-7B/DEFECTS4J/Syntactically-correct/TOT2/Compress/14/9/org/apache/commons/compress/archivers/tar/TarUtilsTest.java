import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * TarUtilsTest class
 */
public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(017755555555555555L, result);

        byte[] invalidBuffer = new byte[]{'0', '0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6'}; // invalid byte
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] missingTrail = new byte[]{'0', '0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5'}; // missing trailing space or NUL
        try {
            TarUtils.parseOctal(missingTrail, 0, missingTrail.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "name";
        byte[] buf = new byte[32];
        int offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        Assert.assertEquals(offset, 31); // checks that the string was copied correctly

        String tooLongName = "thisisatestnamethatissupposedtobeenolong";
        try {
            TarUtils.formatNameBytes(tooLongName, buf, 0, buf.length);
            Assert.fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] bufferTrue = new byte[]{1};
        boolean result = TarUtils.parseBoolean(bufferTrue, 0);
        Assert.assertTrue(result);

        byte[] bufferFalse = new byte[]{0};
        boolean resultFalse = TarUtils.parseBoolean(bufferFalse, 0);
        Assert.assertFalse(resultFalse);

        byte[] bufferInvalid = new byte[]{2};
        try {
            TarUtils.parseBoolean(bufferInvalid, 0);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] binaryBuffer = new byte[]{0x80, 0x01, 0x2a};
        long result = TarUtils.parseOctalOrBinary(binaryBuffer, 0, binaryBuffer.length);
        Assert.assertEquals(0x012a01L, result);

        byte[] octalBuffer = new byte[]{'0', '1', '2', '7', '5'};
        long resultOctal = TarUtils.parseOctalOrBinary(octalBuffer, 0, octalBuffer.length);
        Assert.assertEquals(01275L, resultOctal);

        byte[] invalidLengthBuffer = new byte[]{0x80, 0x01};
        try {
            TarUtils.parseOctalOrBinary(invalidLengthBuffer, 0, invalidLengthBuffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] invalidByteBuffer = new byte[]{0x80, 0x01, 0x81}; //invalid byte
        try {
            TarUtils.parseOctalOrBinary(invalidByteBuffer, 0, invalidByteBuffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buf = new byte[11];
        TarUtils.formatUnsignedOctalString(value, buf, 0, buf.length);
        Assert.assertArrayEquals(new byte[]{"0", "0", "0", "1", "2", "3", "4", "5", "5", "5", "5"}, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[10];
        TarUtils.formatLongOctalBytes(value, buf, 0, buf.length);
        Assert.assertArrayEquals(new byte[]{"0", "0", "0", "1", "2", "3", "4", "5", "5", "5"}, buf);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 12345;
        byte[] buf = new byte[10];
        TarUtils.formatLongOctalOrBinaryBytes(value, buf, 0, buf.length);
        Assert.assertArrayEquals(new byte[]{0x80, (byte) 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f}, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[5];
        TarUtils.formatCheckSumOctalBytes(value, buf, 0, buf.length);
        Assert.assertArrayEquals(new byte[]{"0", "5", "5", "5", "5"}, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{'0', '0', '7', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5'};
        long sum = TarUtils.computeCheckSum(buf);
        long expectedSum = 0x44656361775555555555555555555555L;
        Assert.assertEquals(expectedSum, sum);
    }
}