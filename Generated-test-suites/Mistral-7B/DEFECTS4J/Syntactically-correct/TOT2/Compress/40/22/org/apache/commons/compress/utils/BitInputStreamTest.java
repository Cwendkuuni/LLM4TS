import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {

    private static final int TEST_STREAM_SIZE = 1024;
    private static final ByteOrder TEST_BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;

    private InputStream testInputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws Exception {
        testInputStream = new ByteArrayInputStream(new byte[TEST_STREAM_SIZE]);
        bitInputStream = new BitInputStream(testInputStream, TEST_BYTE_ORDER);
    }

    @Test
    public void testReadBits_NormalCase() throws Exception {
        long expected = 0b11011011_01110111_11011011_11110111L;
        long actual = bitInputStream.readBits(64);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReadBits_MinValue() throws Exception {
        long actual = bitInputStream.readBits(1);
        Assert.assertEquals(-1L, actual);
    }

    @Test
    public void testReadBits_MaxValue() throws Exception {
        long expected = Long.LONG_MAX;
        long actual = bitInputStream.readBits(64);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReadBits_NegativeValue() throws Exception {
        long actual = bitInputStream.readBits(-1);
        Assert.assertEquals(-1L, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_InvalidCount() throws Exception {
        bitInputStream.readBits(-1);
    }

    @Test
    public void testClearBitCache() throws Exception {
        long expected = 0L;
        long actual = bitInputStream.readBits(64);
        bitInputStream.clearBitCache();
        actual = bitInputStream.readBits(64);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testClose() throws Exception {
        bitInputStream.close();
        long actual = bitInputStream.readBits(64);
        Assert.assertEquals(-1L, actual);
    }
}