import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.BaseNCodecs;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("abcd".getBytes()); // Initialize input stream
        BaseNCodec baseNCodec = BaseNCodecs.BASE32; // Initialize BaseNCodec
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false); // Create BaseNCodecInputStream

        int readByte = inputStreamFilter.read(); // Read a byte
        assertEquals("First read byte should be 'a'.", 'a', readByte);
    }
}