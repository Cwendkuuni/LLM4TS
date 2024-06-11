import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksum;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ChecksumCalculatingInputStreamTest {
    @Test
    public void testReadMethod() throws Exception {
        byte[] testBytes = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04};
        Checksum checksum = new Adler32(); // Adler32 checksum algorithm
        InputStream inputStream = new ByteArrayInputStream(testBytes);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readByte = checksumCalculatingInputStream.read();
        byte expectedByte = testBytes[0];
        assertEquals(expectedByte, readByte);

        checksumCalculatingInputStream.read(); // read second byte

        long checksumValue = checksumCalculatingInputStream.getValue();
        long expectedChecksumValue = checksum.getValue(testBytes);
        assertEquals(expectedChecksumValue, checksumValue);
    }
}