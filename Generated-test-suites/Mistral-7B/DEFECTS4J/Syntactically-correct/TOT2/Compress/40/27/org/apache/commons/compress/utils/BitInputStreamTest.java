import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BitInputStreamTest {

    @Test
    public void testReadBits() throws Exception {
        BitInputStream bitInputStream = new BitInputStream(System.in, ByteOrder.LITTLE_ENDIAN);
        long expectedBits = 0b1111_1111_1111_1111; // 16 bits (0xFFFF)
        long actualBits = bitInputStream.readBits(16);
        assertEquals(expectedBits, actualBits);
    }
}