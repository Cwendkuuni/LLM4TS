import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitInputStreamTest {
    @Test
    public void testBitInputStreamConstructor() throws IOException {
        InputStream testInput = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(testInput, byteOrder);
        assertEquals(testInput, bitInputStream.in);
        assertEquals(byteOrder, bitInputStream.byteOrder);
    }
}