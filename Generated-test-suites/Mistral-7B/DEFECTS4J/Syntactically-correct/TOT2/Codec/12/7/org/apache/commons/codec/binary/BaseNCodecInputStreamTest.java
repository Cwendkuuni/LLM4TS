import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] expectedByte = new byte[]{(byte) 0x31};
        int actualByte = baseNCodecInputStream.read();

        assertEquals(expectedByte[0], actualByte);
    }
}