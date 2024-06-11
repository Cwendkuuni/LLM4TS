import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctalHappyPath() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '5', (byte) '5', (byte) '5', (byte) '5'};
        int offset = 0;
        int length = buffer.length;
        long expectedResult = 017775555L;
        long result = TarUtils.parseOctal(buffer, offset, length);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testParseOctalTrailingNulMissing() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '5', (byte) '5', (byte) '5', (byte) '5'};
        int offset = 0;
        int length = buffer.length;
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, offset, length));
    }

    @Test
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '5', (byte) '5', (byte) 'A', (byte) '5'};
        int offset = 0;
        int length = buffer.length;
        String expectedMessage = "Invalid byte A at offset 7 in '...55777555A' len=9";
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, offset, length), expectedMessage);
    }

    @Test
    public void testParseNameHappyPath() {
        byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x73, (byte) 0x71, (byte) 0x6D, (byte) 0x65, (byte) 0x73, (byte) 0x73, (byte) 0x61, (byte) 0x6E, (byte) 0x64, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x64, (byte) 0x20, (byte) 0x74, (byte) 0x68, (byte) 0x69, (byte) 0x73, (byte) 0x20, (byte) 0x69, (byte) 0x6F, (byte) 0x6E, (byte) 0x65, (byte) 0x20, (byte) 0x73, (byte) 0x74, (byte) 0x72, (byte) 0x61, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x75, (byte) 0x69, (byte) 0x6F, (byte) 0x6E, (byte) 0x20, (byte) 0x63, (byte) 0x61, (byte) 0x6D, (byte) 0x65, (byte) 0x20, (byte) 0x63, (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x75, (byte) 0x74, (byte) 0x20, (byte) 0x65, (byte) 0x6E, (byte) 0x63, (byte) 0x6F, (byte) 0x64, (byte) 0x6D, (byte) 0x61, (byte) 0x74, (byte) 0x69, (byte) 0x6F, (byte) 0x6E, 0};
        int offset = 0;
        int length = buffer.length;
        String expectedResult = "user.name.tar. GNU-Tar";
        String result = TarUtils.parseName(buffer, offset, length);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testParseNameTrailingNulMissing() {
        byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x73, (byte) 0x71, (byte) 0x6D, (byte) 0x65, (byte) 0x73, (byte) 0x73, (byte) 0x61, (byte) 0x6E, (byte) 0x64, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x64, (byte) 0x20, (byte) 0x74, (byte) 0x68, (byte) 0x69, (byte) 0x73, (byte) 0x20, (byte) 0x69, (byte) 0x6F, (byte) 0x6E, (byte) 0x65, (byte) 0x20, (byte) 0x73, (byte) 0x74, (byte) 0x72, (byte) 0x61, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x75, (byte) 0x69, (byte) 0x6F, (byte) 0x6E, (byte) 0x20, (byte) 0x63, (byte) 0x61, (byte) 0x6D, (byte) 0x65};
        int offset = 0;
        int length = buffer.length;
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(buffer, offset, length));
    }

    @Test
    public void testFormatNameBytesHappyPath() {
        String name = "user.name.tar. GNU-Tar";
        byte[] buffer = new byte[20];
        int offset = 0;
        int length = name.length();
        offset = TarUtils.formatNameBytes(name, buffer, offset, length);
        String expectedResult = "user.name.tar. GNU-Tar";
        String result = new String(buffer, 0, offset);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatNameBytesNameLongerThanBuffer() {
        String name = "user.name.tar. GNU-Tar";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = name.length();
        offset = TarUtils.formatNameBytes(name, buffer, offset, length);
        String expectedResult = "user.name.tar...";
        String result = new String(buffer, 0, offset);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatUnsignedOctalStringHappyPath() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expectedResult = "012345777";
        String result = new String(buffer, 0, length);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatUnsignedOctalStringValueTooBig() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(value, buffer, offset, length));
    }

    @Test
    public void testComputeCheckSumHappyPath() {
        byte[] buf = new byte[]{0x75, 0x73, 0x71, 0x6D, 0x65, 0x73, 0x73, 0x61, 0x6E, 0x64, 0x61, 0x72, 0x65, 0x64, 0x20, 0x74, 0x68, 0x69, 0x73, 0x20, 0x69, 0x6F, 0x6E, 0x65, 0x20, 0x73, 0x74, 0x72, 0x61, 0x63, 0x68, 0x65, 0x72, 0x65, 0x61, 0x75, 0x69, 0x6F, 0x6E, 0x20, 0x63, 0x61, 0x6D, 0x65, 0x20, 0x63, 0x72, 0x65, 0x61, 0x75, 0x74, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x6D, 0x61, 0x74, 0x69, 0x6F, 0x6E, 0};
        long expectedChecksum = 123456789012345L;
        long result = TarUtils.computeCheckSum(buf);
        Assert.assertEquals(expectedChecksum, result);
    }

    @Test
    public void testComputeCheckSumEmptyBuffer() {
        byte[] buf = new byte[0];
        long expectedChecksum = 0;
        long result = TarUtils.computeCheckSum(buf);
        Assert.assertEquals(expectedChecksum, result);
    }

    @Test
    public void testComputeCheckSumBufferWithNull() {
        byte[] buf = new byte[]{0};
        long expectedChecksum = 0;
        long result = TarUtils.computeCheckSum(buf);
        Assert.assertEquals(expectedChecksum, result);
    }
}