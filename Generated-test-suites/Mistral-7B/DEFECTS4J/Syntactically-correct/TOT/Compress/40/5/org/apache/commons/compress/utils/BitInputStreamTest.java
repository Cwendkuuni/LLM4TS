import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(new byte[]{0x0f, 0x0e, 0x0d, 0x0c, 0x0b, 0x0a, 0x09, 0x08, 0x01});
    }

    @After
    public void tearDown() throws IOException {
        outputStream.close();
        inputStream.close();
    }

    /**
     * Test reading 1 bit.
     * @throws IOException on error
     */
    @Test
    public void testReadOneBit() throws IOException {
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertEquals(1, bitInputStream.readBits(1));
        assertEquals(0, bitInputStream.readBits(1));
        assertEquals(-1, bitInputStream.readBits(1));
    }

    /**
     * Test reading multiple bits.
     * @throws IOException on error
     */
    @Test
    public void testReadMultipleBits() throws IOException {
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertEquals(0b1111_0010_0110_1100L, bitInputStream.readBits(8));
        assertEquals(0b0000_0111_1011_0010L, bitInputStream.readBits(8));
        assertEquals(-1, bitInputStream.readBits(9));
    }

    /**
     * Test clearing the cache.
     * @throws IOException on error
     */
    @Test
    public void testClearBitCache() throws IOException {
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();
        assertEquals(-1, bitInputStream.readBits(1));
    }

    /**
     * Test end of input.
     * @throws IOException on error
     */
    @Test(expected = IOException.class)
    public void testEndOfInput() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
    }
}