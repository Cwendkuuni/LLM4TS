import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatCheckSumOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatLongOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatNameBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatUnsignedOctalString;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctal;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseName;

/**
 * TarUtilsTest class contains JUnit test cases for TarUtils class methods.
 */
public class TarUtilsTest {

    // Test for parseOctal method
    @Test
    public void testParseOctal() {
        byte[] buffer = {(byte) '0', (byte) '7', (byte) '5', (byte) '5', (byte) '0'};
        long result = TarUtils.parseOctal(buffer, 0, 5);
        Assert.assertEquals("Check parseOctal result", 017550L, result);

        byte[] invalidBuffer = {(byte) '1', (byte) 'a', (byte) '2', (byte) '3'};
        try {
            TarUtils.parseOctal(invalidBuffer, 0, 4);
            Assert.fail("parseOctal should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test for parseName method
    @Test
    public void testParseName() {
        byte[] buffer = {(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) ' ', (byte) 't', (byte) 'a', (byte) 'r',
                (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'z',
                (byte) 'i', (byte) 'p', (byte) '\0'};
        String result = TarUtils.parseName(buffer, 0, 16);
        Assert.assertEquals("Check parseName result", "hello.gz.zip", result);
    }

    // Test for formatNameBytes method
    @Test
    public void testFormatNameBytes() {
        String name = "MyName.tar.gz";
        byte[] buffer = new byte[20];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 20);

        Assert.assertEquals("Check formatNameBytes offset", 20, offset);
        Assert.assertArrayEquals("Check formatNameBytes buffer", name.getBytes(), buffer, 0, name.length());
    }

    // Test for formatUnsignedOctalString method
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 65776;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 8);
        String expected = "02510202";
        Assert.assertEquals("Check formatUnsignedOctalString result", expected, new String(buffer));
    }

    // Test for formatOctalBytes method
    @Test
    public void testFormatOctalBytes() {
        long value = 65776;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, 10);
        String expected = "02510202  ";
        Assert.assertEquals("Check formatOctalBytes result", expected, new String(buffer, 0, offset));
    }

    // Test for formatCheckSumOctalBytes method
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 12);
        String expected = "123456777  ";
        Assert.assertEquals("Check formatCheckSumOctalBytes result", expected, new String(buffer, 0, offset));
    }

    // Test for computeCheckSum method
    @Test
    public void testComputeCheckSum() {
        byte[] testData = new byte[]{(byte) 0x78, (byte) 0x9c, (byte) 0xba, (byte) 0xdf};
        long checksum = TarUtils.computeCheckSum(testData);

        long expected = (0x78 & BYTE_MASK) + ((0x9c & BYTE_MASK) << 8) + ((0xba & BYTE_MASK) << 16) + ((0xdf & BYTE_MASK) << 24);
        Assert.assertEquals("Check computeCheckSum result", expected, checksum);
    }
}