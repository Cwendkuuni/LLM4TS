import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Assert;
import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testReadBits() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream in = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        BitInputStream bitInputStream = new BitInputStream(in, byteOrder);

        // Test reading 1 bit
        long expected = 0x01 & 0x01;
        long result = bitInputStream.readBits(1);
        Assert.assertEquals(expected, result);

        // Test reading 2 bits
        expected = 0x03 & ((1L << 2) - 1);
        result = bitInputStream.readBits(2);
        Assert.assertEquals(expected, result);

        // Test reading 6 bits (maximum without shifting)
        expected = 0x24 & MASKS[6];
        result = bitInputStream.readBits(6);
        Assert.assertEquals(expected, result);

        // Test reading 7 bits (should throw an exception)
        try {
            bitInputStream.readBits(7);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test reading 64 bits (should read the whole stream)
        long expectedResult = 0x0102030405010203L;
        long receivedResult = 0L;
        while (bitInputStream.readBits(63) != -1) {
            receivedResult |= bitInputStream.readBits(1);
        }
        Assert.assertEquals(expectedResult, receivedResult);

        bitInputStream.close();
    }
}