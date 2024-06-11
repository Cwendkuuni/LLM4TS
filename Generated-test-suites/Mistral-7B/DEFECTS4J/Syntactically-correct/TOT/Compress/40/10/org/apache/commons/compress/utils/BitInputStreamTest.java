import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test suite for the BitInputStream class.
 */
public class BitInputStreamTest {

    private static final int TEST_DATA_SIZE = 1024;
    private ByteArrayOutputStream testDataStream;
    private ByteArrayInputStream testDataInput;

    @Before
    public void setUp() throws IOException {
        testDataStream = new ByteArrayOutputStream(TEST_DATA_SIZE);
        testDataInput = new ByteArrayInputStream(testDataStream.toByteArray());
    }

    @After
    public void tearDown() throws Exception {
        testDataStream.reset();
    }

    @Test
    public void testBitInputStreamWithLittleEndian() throws IOException {
        // Testing reading 1 bit
        BitInputStream input = new BitInputStream(testDataInput, ByteOrder.LITTLE_ENDIAN);
        input.readBits(1);
        Assert.assertEquals(-1, input.readBits(1));
        byte testDataByte = (byte) 0x01;
        testDataStream.write(testDataByte);
        testDataStream.flush();
        input = new BitInputStream(testDataInput, ByteOrder.LITTLE_ENDIAN);
        Assert.assertEquals(1L, input.readBits(1));
        Assert.assertEquals(-1, input.readBits(2));

        // Testing reading multiple bits
        input = new BitInputStream(testDataInput, ByteOrder.LITTLE_ENDIAN);
        testDataByte = (byte) 0b11110110;
        testDataStream.write(testDataByte);
        testDataStream.flush();
        Assert.assertEquals(12L, input.readBits(3));
        Assert.assertEquals(8L, input.readBits(4));
        Assert.assertEquals(15L, input.readBits(4));
        Assert.assertEquals(-1, input.readBits(5));

        // Testing reading maximum cache size
        testDataByte = (byte) 0b11111111;
        testDataStream.write(testDataByte);
        testDataStream.flush();
        input = new BitInputStream(testDataInput, ByteOrder.LITTLE_ENDIAN);
        Assert.assertEquals(63L, input.readBits(64));
        Assert.assertEquals(-1, input.readBits(1));
    }

    @Test
    public void testBitInputStreamWithBigEndian() throws IOException {
        // Testing reading 1 bit
        BitInputStream input = new BitInputStream(testDataInput, ByteOrder.BIG_ENDIAN);
        input.readBits(1);
        Assert.assertEquals(-1, input.readBits(1));
        byte testDataByte = (byte) 0x01;
        testDataStream.write(testDataByte);
        testDataStream.flush();
        input = new BitInputStream(testDataInput, ByteOrder.BIG_ENDIAN);
        Assert.assertEquals(1L, input.readBits(1));
        Assert.assertEquals(-1, input.readBits(2));

        // Testing reading multiple bits
        input = new BitInputStream(testDataInput, ByteOrder.BIG_ENDIAN);
        testDataByte = (byte) 0b11110110;
        testDataStream.write(testDataByte);
        testDataStream.flush();
        Assert.assertEquals(12L, input.readBits(3));
        Assert.assertEquals(8L, input.readBits(4));
        Assert.assertEquals(15L, input.readBits(4));
        Assert.assertEquals(-1, input.readBits(5));

        // Testing reading maximum cache size
        testDataByte = (byte) 0b11111111;
        testDataStream.write(testDataByte);
        testDataStream.flush();
        input = new BitInputStream(testDataInput, ByteOrder.BIG_ENDIAN);
        Assert.assertEquals(63L, input.readBits(64));
        Assert.assertEquals(-1, input.readBits(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() throws IOException {
        BitInputStream input = new BitInputStream(testDataInput, ByteOrder.LITTLE_ENDIAN);
        input.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountGreaterThanMax() throws IOException {
        BitInputStream input = new BitInputStream(testDataInput, ByteOrder.LITTLE_ENDIAN);
        input.readBits(64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        // Testing reading multiple bits before clearing the cache
        BitInputStream input = new BitInputStream(testDataInput, ByteOrder.LITTLE_ENDIAN);
        testDataStream.write(new byte[]{(byte) 0b11110110, (byte) 0b10101111, (byte) 0b11111111});
        testDataStream.flush();
        Assert.assertEquals(25L, input.readBits(5));
        Assert.assertEquals(27L, input.readBits(5));
        Assert.assertEquals(63L, input.readBits(64));

        // Testing reading after clearing the cache
        input.clearBitCache();
        Assert.assertEquals(-1, input.readBits(1));
    }
}