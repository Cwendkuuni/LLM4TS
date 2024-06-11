import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

/**
 * Test class for ChecksumCalculatingInputStream.
 */
public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStream;
    private ChecksumCalculatingInputStream checksumInputStream;
    private long expectedChecksum;

    /**
     * Sets up the test environment.
     * @throws IOException if the input stream creation fails
     * @throws NoSuchAlgorithmException if the checksum algorithm is not supported
     */
    @Before
    public void setUp() throws IOException, NoSuchAlgorithmException {
        final byte[] inputData = new byte[]{'A', 'B', 'C'};
        inputStream = new ByteArrayInputStream(inputData);
        checksumInputStream = new ChecksumCalculatingInputStream(Checksums.adler32(), inputStream);
        expectedChecksum = Checksums.adler32().getValue();
    }

    /**
     * Tests the read method with a single byte.
     * @throws IOException if the stream is exhausted or the checksum doesn't match
     */
    @Test
    public void testReadByte() throws IOException {
        final int readByte = checksumInputStream.read();
        assertEquals((int) inputData[0], readByte);
        assertEquals(expectedChecksum, checksumInputStream.getValue());
    }

    /**
     * Tests the read method with a byte array.
     * @throws IOException if the stream is exhausted or the checksum doesn't match
     */
    @Test
    public void testReadByteArray() throws IOException {
        final byte[] outputData = new byte[3];
        final int length = checksumInputStream.read(outputData);

        assertEquals(3, length);
        assertEquals(inputData[0], outputData[0]);
        assertEquals(inputData[1], outputData[1]);
        assertEquals(inputData[2], outputData[2]);
        assertEquals(expectedChecksum, checksumInputStream.getValue());
    }

    /**
     * Tests the skip method.
     * @throws IOException if the stream is exhausted or the checksum doesn't match
     */
    @Test
    public void testSkip() throws IOException {
        checksumInputStream.skip(1);
        final int readByte = checksumInputStream.read();

        assertEquals((int) inputData[1], readByte);
        assertEquals(expectedChecksum, checksumInputStream.getValue());
    }

    /**
     * Cleans up the test environment.
     */
    @After
    public void tearDown() throws IOException {
        inputStream.close();
        checksumInputStream.close();
    }
}