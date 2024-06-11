import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Assert;
import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testReadBits() throws Exception {
        BitInputStream bitInputStream = new BitInputStream(System.in, ByteOrder.LITTLE_ENDIAN);
        long expected = 123; // Arbitrary value
        long actual = bitInputStream.readBits(6);
        Assert.assertEquals(expected, actual);
        bitInputStream.close();
    }
}