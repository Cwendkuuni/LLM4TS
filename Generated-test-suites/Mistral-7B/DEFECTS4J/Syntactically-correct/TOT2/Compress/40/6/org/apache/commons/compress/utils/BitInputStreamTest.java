import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BitInputStreamTest {
    @Test
    public void testReadBits() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01}); // Test with a single byte
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits = bitInputStream.readBits(5); // Test reading 5 bits
        assertEquals(0b11111, bits); // Expected output: 11111 (binary)
    }
}