import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '0', '0'};
        long expected = 017540L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);

        buffer = new byte[]{'0', '1', '7', '7', '5', '5', '4', '0', '1'};
        expected = 017755401L;
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);

        buffer = new byte[]{'0', '0', '0', '0'};
        long result2 = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0, result2);

        buffer = new byte[]{'1', '2', '3'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'x', 'm', 'l', '.', 't', 'x', 't', ' ', 't', 'e', 's', 't', '.', 'c', 'o', 'm', 'p', 'r', 'e', 's', 's', '.', 'z', 'i', 'p'};
        String expected = "file.tar.xtx test.compress.zip";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);

        buffer = new byte[]{'a', '.', '.'};
        String result2 = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("", result2);

        buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'z'};
        String result3 = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("", result3);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "file.tar.xtx test.compress.zip";
        byte[] buffer = new byte[25];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] result = new byte[offset];
        System.arraycopy(buffer, 0, result, 0, offset);
        String resultStr = new String(result, 0, offset);
        Assert.assertEquals(name, resultStr);

        name = "";
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertEquals(0, offset);

        name = "abcdefghijklmnopqrstuvwxyz";
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] result2 = new byte[offset];
        System.arraycopy(buffer, 0, result2, 0, offset);
        Assert.assertEquals(name, new String(result2, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int length = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] result = new byte[length];
        System.arraycopy(buffer, 0, result, 0, length);
        String expected = "01235";
        String resultStr = new String(result, 0, length);
        Assert.assertEquals(expected, resultStr);

        value = 0;
        length = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        result = new byte[length];
        System.arraycopy(buffer, 0, result, 0, length);
        Assert.assertEquals("", new String(result, 0, length));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[11];
        int length = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] result = new byte[length];
        System.arraycopy(buffer, 0, result, 0, length);
        String expected = "01235 \0";
        String resultStr = new String(result, 0, length);
        Assert.assertEquals(expected, resultStr);

        value = 0;
        try {
            TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
            Assert.fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};
        long expected = 65535 & (0x0000000001020304L + 0x0000000001020304L + 0x0000000001020304L + 0x0000000001020304L + 0x0000000001020304L);
        long result = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, result);

        buffer = new byte[0];
        long result2 = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(0, result2);

        buffer = new byte[]{0};
        long result3 = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(0, result3);
    }
}