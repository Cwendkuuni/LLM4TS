import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long result = TarUtils.parseOctal(buffer, 0, 8);
        Assert.assertEquals(01234567L, result);

        buffer = new byte[]{'0', '\u0000'};
        try {
            TarUtils.parseOctal(buffer, 0, 2);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{'1', '2'};
        try {
            TarUtils.parseOctal(buffer, 0, 2);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid byte 2 at offset 1 in '12' len=2", e.getMessage());
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long result1 = TarUtils.parseOctalOrBinary(buffer1, 0, 8);
        Assert.assertEquals(01234567L, result1);

        byte[] buffer2 = new byte[]{'0', '\u0080', '1', '2', '3'};
        long result2 = TarUtils.parseOctalOrBinary(buffer2, 0, 5);
        Assert.assertEquals(1023L, result2);

        byte[] buffer3 = new byte[]{'0', '\u0080'};
        try {
            TarUtils.parseOctalOrBinary(buffer3, 0, 2);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("At offset 0, 2 byte binary number exceeds maximum signed long value", e.getMessage());
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer1 = new byte[]{1};
        boolean result1 = TarUtils.parseBoolean(buffer1, 0);
        Assert.assertTrue(result1);

        byte[] buffer2 = new byte[]{0};
        boolean result2 = TarUtils.parseBoolean(buffer2, 0);
        Assert.assertFalse(result2);

        byte[] buffer3 = new byte[]{'\u0000'};
        try {
            TarUtils.parseBoolean(buffer3, 0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer1 = new byte[]{'a', 'b', 'c', 'd', '\u0000'};
        String result1 = TarUtils.parseName(buffer1, 0, 5);
        Assert.assertEquals("abcd", result1);

        byte[] buffer2 = new byte[]{'a', '\u0000'};
        String result2 = TarUtils.parseName(buffer2, 0, 2);
        Assert.assertEquals("a", result2);

        byte[] buffer3 = new byte[]{'a', 'b', 'c', '\u0001'};
        try {
            TarUtils.parseName(buffer3, 0, 4);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcdef";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 10);
        Assert.assertEquals(offset, 10);
        Assert.assertArrayEquals(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', '\u0000', '\u0000', '\u0000', '\u0000'}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 10);
        Assert.assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'}, buffer);

        value = 0;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 10);
        Assert.assertArrayEquals(new byte[]{'0'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234567;
        byte[] buffer = new byte[11];
        TarUtils.formatOctalBytes(value, buffer, 0, 11);
        Assert.assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' ', '0'}, buffer);

        value = 0;
        TarUtils.formatOctalBytes(value, buffer, 0, 11);
        Assert.assertArrayEquals(new byte[]{'0'}, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567;
        byte[] buffer = new byte[11];
        TarUtils.formatLongOctalBytes(value, buffer, 0, 11);
        Assert.assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' ', '0'}, buffer);

        value = 0;
        TarUtils.formatLongOctalBytes(value, buffer, 0, 11);
        Assert.assertArrayEquals(new byte[]{'0'}, buffer);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 1234567;
        byte[] buffer = new byte[11];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, 11);
        Assert.assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' ', '0'}, buffer);

        value = Long.MAX_VALUE;
        byte[] buffer2 = new byte[11];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer2, 0, 11);
        Assert.assertArrayEquals(new byte[]{'7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7'}, buffer2);

        value = Long.MIN_VALUE;
        byte[] buffer3 = new byte[11];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer3, 0, 11);
        Assert.assertArrayEquals(new byte[]{'3', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7'}, buffer3);
    }
}