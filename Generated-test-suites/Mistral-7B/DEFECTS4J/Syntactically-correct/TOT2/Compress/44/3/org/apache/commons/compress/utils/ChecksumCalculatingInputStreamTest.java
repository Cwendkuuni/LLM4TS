import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte testByte = 0x01;
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(Checksums.adler32(), new ByteArrayInputStream(new byte[] { testByte }));
        int readByte = inputStream.read();
        assertEquals(testByte, readByte);
        long checksum = inputStream.getValue();
        assertEquals(Checksums.ADLER32, checksum);
    }

    // Add additional test cases for other methods here
}