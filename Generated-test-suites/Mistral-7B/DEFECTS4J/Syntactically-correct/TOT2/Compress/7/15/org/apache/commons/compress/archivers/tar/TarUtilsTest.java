import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * TarUtilsTest class provides JUnit test cases for the TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test for parseOctal method with valid input
     */
    @Test
    public void testParseOctalValidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '0'};
        long expectedResult = 012345670L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test for parseOctal method with invalid input
     */
    @Test
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', 'a', 'b', 'c'};
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    /**
     * Test for parseName method with valid input
     */
    @Test
    public void testParseNameValidInput() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'e', (byte) 'c', (byte) 'k', (byte) 's', (byte) 'u', (byte) 'm', (byte) 0};
        String expectedResult = "tar.archive.checksums.";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test for parseName method with invalid input
     */
    @Test
    public void testParseNameInvalidInput() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) 0};
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(buffer, 0, buffer.length));
    }

    /**
     * Test for formatNameBytes method with valid input
     */
    @Test
    public void testFormatNameBytesValidInput() {
        String name = "tar.archive.checksums.";
        byte[] buffer = new byte[32];
        int offset = 0;
        int length = name.length();
        offset = TarUtils.formatNameBytes(name, buffer, offset, length);
        String result = new String(buffer, 0, offset);
        assertEquals(name, result);
    }

    /**
     * Test for formatNameBytes method with buffer length smaller than name length
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesInvalidBufferSize() {
        String name = "tar.archive.checksums.";
        byte[] buffer = new byte[10];
        int offset = 0;
        TarUtils.formatNameBytes(name, buffer, offset, name.length());
    }

    /**
     * Test for formatUnsignedOctalString method with valid input
     */
    @Test
    public void testFormatUnsignedOctalStringValidInput() {
        long value = 123456789L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 9;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expectedResult = "012345675";
        String result = new String(buffer, 0, length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test for formatUnsignedOctalString method with value larger than buffer size
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalidBufferSize() {
        long value = 123456789L;
        byte[] buffer = new byte[5];
        int offset = 0;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, buffer.length);
    }

    /**
     * Test for formatOctalBytes method with valid input
     */
    @Test
    public void testFormatOctalBytesValidInput() {
        long value = 123456789L;
        byte[] buffer = new byte[13];
        int offset = 0;
        TarUtils.formatOctalBytes(value, buffer, offset, buffer.length);
        String expectedResult = "012345675  ";
        String result = new String(buffer, 0, offset + 4);
        assertEquals(expectedResult, result);
    }

    /**
     * Test for formatOctalBytes method with value larger than buffer size
     * (space and null characters are included)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesInvalidBufferSize() {
        long value = 123456789L;
        byte[] buffer = new byte[8];
        int offset = 0;
        TarUtils.formatOctalBytes(value, buffer, offset, buffer.length);
    }

    /**
     * Test for computeCheckSum method with valid input
     */
    @Test
    public void testComputeCheckSumValidInput() {
        byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x72, (byte) 0x6e, (byte) 0x63, (byte) 0x74, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x73, (byte) 0x68, (byte) 0x65, (byte) 0x6d, (byte) 0x65, (byte) 0x64, (byte) 0x79, (byte) 0x6f, (byte) 0x75, (byte) 0x74, (byte) 0x43, (byte) 0x0};
        long expectedResult = 0x22f0e7c4b1e8fb1fL;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expectedResult, result);
    }
}