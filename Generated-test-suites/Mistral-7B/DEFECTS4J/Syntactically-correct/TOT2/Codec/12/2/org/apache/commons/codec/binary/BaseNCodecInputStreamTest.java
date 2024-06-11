import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        BaseNCodec codec = new BaseNCodec(5, 32); // BaseNCodec instance with 5 bytes per decoded character and base 32
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(inputStream, codec, false);

        assertEquals(48, testStream.read()); // Test reading valid byte
        assertEquals(EOF, testStream.read()); // Test reaching EOF
    }
}