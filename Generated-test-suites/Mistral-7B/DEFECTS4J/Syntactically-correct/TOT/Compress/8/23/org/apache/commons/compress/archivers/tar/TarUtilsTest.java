import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '5', (byte) '5'};
        long expectedResult = 0177755L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, result);

        byte[] invalidBuffer = new byte[]{(byte) '0', (byte) '7', (byte) '8', (byte) '7', (byte) '7', (byte) '5', (byte) '5'};
        Assert.throwException(IllegalArgumentException.class, () -> TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length));

        byte[] emptyBuffer = new byte[0];
        Assert.assertEquals(0L, TarUtils.parseOctal(emptyBuffer, 0, emptyBuffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) 's', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '\0'};
        String expectedResult = "hello.tar.gz";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, result);

        byte[] invalidBuffer = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) 's', (byte) '\0'};
        Assert.assertNotEquals(expectedResult, TarUtils.parseName(invalidBuffer, 0, invalidBuffer.length));

        byte[] emptyBuffer = new byte[0];
        Assert.assertEquals("", TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello.tar.gz";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatNameBytes(name, buffer, offset, length);
        String result = new String(buffer, 0, length);
        Assert.assertEquals(name, result);

        name = "hello.tar.gz";
        buffer = new byte[5];
        offset = 0;
        length = 5;
        Assert.assertArrayEquals(new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) '.'}, buffer);
        result = new String(buffer, 0, length);
        Assert.assertEquals("hell.", result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456789;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String result = new String(buffer, 0, length);
        String expectedResult = "012345675";
        Assert.assertEquals(expectedResult, result);

        value = 12345;
        buffer = new byte[5];
        offset = 0;
        length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        result = new String(buffer, 0, length);
        expectedResult = "01234";
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[13];
        int offset = 0;
        int length = 13;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        String result = new String(buffer, 0, length - 2);
        String expectedResult = "012345675 ";
        Assert.assertEquals(expectedResult, result);

        value = 12345;
        buffer = new byte[8];
        offset = 0;
        length = 8;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        result = new String(buffer, 0, length - 2);
        expectedResult = "01234 ";
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 0x78, (byte) 0x9a, (byte) 0xba, (byte) 0xbe, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78};
        long expectedChecksum = 0x1b789abeL;
        long checksum = TarUtils.computeChecksum(buffer);
        Assert.assertEquals(expectedChecksum, checksum);

        byte[] emptyBuffer = new byte[0];
        long emptyChecksum = 0L;
        Assert.assertEquals(emptyChecksum, TarUtils.computeChecksum(emptyBuffer));
    }
}