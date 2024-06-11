import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal(){
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7'};
        long expected = 0175577L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidByte(){
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', 'A'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    // Add test cases for other methods and their edge cases.

    @Test
    public void testParseName(){
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'i', 'n', 'f', 'o', '.', 't', 'a', 'r', '.', 'g', 'z', ' ', '0'};
        String expected = "tar.info.";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    // Add test cases for other methods and their edge cases.

    @Test
    public void testFormatNameBytes(){
        String name = "Tar.Gz";
        byte[] buf = new byte[11];
        int offset = 0;
        int length = 11;
        int expected = 11;
        int actual = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(expected, actual);
    }

    // Add test cases for other methods and their edge cases.

    @Test
    public void testFormatUnsignedOctalString(){
        long value = 12345;
        byte[] buffer = new byte[11];
        int offset = 0;
        int length = 11;
        int expected = 11;
        int actual = TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals(expected, actual);
    }

    // Add test cases for other methods and their edge cases.

    @Test
    public void testFormatOctalBytes(){
        long value = 12345;
        byte[] buf = new byte[14];
        int offset = 0;
        int length = 14;
        int expected = 14;
        int actual = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(expected, actual);
    }

    // Add test cases for other methods and their edge cases.

    @Test
    public void testFormatLongOctalBytes(){
        long value = 123456789012345L;
        byte[] buf = new byte[20];
        int offset = 0;
        int length = 20;
        int expected = 20;
        int actual = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(expected, actual);
    }

    // Add test cases for other methods and their edge cases.

    @Test
    public void testFormatCheckSumOctalBytes(){
        long value = 123456789012345L;
        byte[] buf = new byte[13];
        int offset = 0;
        int length = 13;
        int expected = 13;
        int actual = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(expected, actual);
    }

    // Add test cases for other methods and their edge cases.

    @Test
    public void testComputeCheckSum(){
        byte[] buf = new byte[]{0x78, 0x5f, 0x43, 0x32, 0x21, 0x01};
        long checksum = TarUtils.computeCheckSum(buf);
        long expected = 0x3c9a1f37;
        assertEquals(expected, checksum);
    }

    // Add test cases for other methods and their edge cases.

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithEmptyBuffer(){
        byte[] buffer = new byte[0];
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    // Add test cases for other methods and their edge cases.

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWithInvalidLength(){
        long value = 12345;
        byte[] buffer = new byte[5];
        int offset = 0;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, buffer.length);
    }

    // Add test cases for other methods and their edge cases.

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesWithInvalidOffset(){
        long value = 12345;
        byte[] buf = new byte[11];
        int length = 11;
        TarUtils.formatOctalBytes(value, buf, -1, length);
    }

    // Add test cases for other methods and their edge cases.

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytesWithInvalidLength(){
        long value = 123456789012345L;
        byte[] buf = new byte[5];
        TarUtils.formatLongOctalBytes(value, buf, 0, buf.length);
    }

    // Add test cases for other methods and their edge cases.

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytesWithInvalidLength(){
        long value = 123456789012345L;
        byte[] buf = new byte[5];
        TarUtils.formatCheckSumOctalBytes(value, buf, 0, buf.length);
    }

    // Add test cases for other methods and their edge cases.

}

//