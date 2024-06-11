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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link ChecksumCalculatingInputStream}.
 */
public class ChecksumCalculatingInputStreamTest {

    private static final byte[] TEST_DATA = "Test data".getBytes();
    private static final long TEST_CHECKSUM;

    static {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(TEST_DATA);
            TEST_CHECKSUM = messageDigest.digest()[0] & 0xFFL; // First byte of the checksum
            for (int i = 1; i < messageDigest.digest().length; i++) {
                TEST_CHECKSUM = (TEST_CHECKSUM << 8) | (messageDigest.digest()[i] & 0xFF);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm is not supported.", e);
        }
    }

    private InputStream inputStream;
    private ChecksumCalculatingInputStream checksumCalculatingInputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(TEST_DATA);
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(Checksums.MD5, inputStream);
    }

    @After
    public void tearDown() throws IOException {
        outputStream.close();
        inputStream.close();
        checksumCalculatingInputStream.close();
    }

    /**
     * Test reading a single byte.
     * @throws IOException if an error occurs
     */
    @Test
    public void testReadByte() throws IOException {
        final int readByte = checksumCalculatingInputStream.read();
        assertEquals(TEST_DATA[0], readByte);
    }

    /**
     * Test reading a byte array.
     * @throws IOException if an error occurs
     */
    @Test
    public void testReadByteArray() throws IOException {
        final byte[] readByteArray = new byte[TEST_DATA.length];
        final int readBytes = checksumCalculatingInputStream.read(readByteArray);
        assertEquals(TEST_DATA.length, readBytes);
        assertArrayEquals(TEST_DATA, readByteArray);
    }

    /**
     * Test reading a byte array with an offset and length.
     * @throws IOException if an error occurs
     */
    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        final byte[] readByteArray = new byte[TEST_DATA.length];
        final int readBytes = checksumCalculatingInputStream.read(readByteArray, 0, TEST_DATA.length);
        assertEquals(TEST_DATA.length, readBytes);
        assertArrayEquals(TEST_DATA, readByteArray);
    }

    /**
     * Test reading no data.
     * @throws IOException if an error occurs
     */
    @Test
    public void testReadNoData() throws IOException {
        final int readBytes = checksumCalculatingInputStream.read(outputStream.toByteArray(), 0, outputStream.size());
        assertEquals(0, readBytes);
    }

    /**
     * Test reading past the end of the stream.
     * @throws IOException if an error occurs
     */
    @Test(expected = IOException.class)
    public void testReadPastEndOfStream() throws IOException {
        checksumCalculatingInputStream.skip(TEST_DATA.length + 1);
    }

    /**
     * Test getting the calculated checksum.
     */
    @Test
    public void testGetValue() throws IOException {
        final long checksum = checksumCalculatingInputStream.getValue();
        assertEquals(TEST_CHECKSUM, checksum);
    }
}