import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BitInputStreamTest {
    @Test
    public void testReadBits() throws Exception {
        BitInputStream bitInputStream = new BitInputStream(System.in, ByteOrder.LITTLE_ENDIAN);
        long expectedBits = 0b11111111; // 7 bits
        long actualBits = bitInputStream.readBits(7);
        assertEquals(expectedBits, actualBits);
    }
}