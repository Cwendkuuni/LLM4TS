import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidData() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0'};
        int offset = 0;
        int length = buffer.length;
        long expectedResult = 06445L; // Octal representation of rwxr-xr-x
        long actualResult = TarUtils.parseOctal(buffer, offset, length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidData() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '8'}; // An invalid byte is present
        int offset = 0;
        int length = buffer.length;
        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName_ValidData() {
        byte[] buffer = new byte[]{'.' , 't', 'a', 'r', '.', 'e', 'x', 'a', 'm', 'p', 'l', 'e', 0};
        int offset = 0;
        int length = buffer.length;
        String expectedResult = "tar.example.mple";
        String actualResult = TarUtils.parseName(buffer, offset, length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatNameBytes_ValidData() {
        String name = "tar.example.mple";
        byte[] buffer = new byte[32];
        int offset = 0;
        int length = name.length();
        int actualResult = TarUtils.formatNameBytes(name, buffer, offset, length);
        byte[] expectedResult = new byte[]{'.' , 't', 'a', 'r', '.', 'e', 'x', 'a', 'm', 'p', 'l', 'e', 0};
        Assert.assertArrayEquals(expectedResult, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidData() {
        long value = 06445L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expectedResult = new byte[]{'0', '6', '4', '4', '5', ' ', '0', 0, 0, 0};
        Assert.assertArrayEquals(expectedResult, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes_ValidData() {
        long value = 06445L;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = buffer.length;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        byte[] expectedResult = new byte[]{'0', '6', '4', '4', '5', ' ', '0', 0, 0, 0, 0, 0};
        Assert.assertArrayEquals(expectedResult, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum_ValidData() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'h', 'e', 'a', 'd', 'e', 'r', 0x07, 'e', 'n', 'd', '.', 't', 'a', 'r'};
        long expectedCheckSum = 0x21C19578C1B0003L;
        long actualCheckSum = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expectedCheckSum, actualCheckSum);
    }

    @Test
    public void testFormatCheckSumOctalBytes_ValidData() {
        long value = 0x21C19578C1B0003L;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = buffer.length;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        byte[] expectedResult = new byte[]{'0', '2', '1', '0', '4', '2', '1', '2', '1', '5', '7', '8', '0', '5', '7', '8', '0', '3', ' ', '0'};
        Assert.assertArrayEquals(expectedResult, buffer, 0, buffer.length);
    }
}