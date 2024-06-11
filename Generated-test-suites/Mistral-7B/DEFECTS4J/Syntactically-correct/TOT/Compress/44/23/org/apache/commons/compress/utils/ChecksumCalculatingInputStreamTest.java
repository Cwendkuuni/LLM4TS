import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculation() throws IOException, NoSuchAlgorithmException {
        // Given
        final byte[] testData = "Test data".getBytes();
        final Checksum checksum = new Adler32(); // using Adler32 checksum for simplicity
        final InputStream testInputStream = new ByteArrayInputStream(testData);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, testInputStream);

        // When
        final int readByte;
        byte[] testByteArray = new byte[testData.length];
        int testBytesRead;

        // Test reading single bytes
        while ((readByte = checksumInputStream.read()) >= 0) {
            testBytesRead++;
        }

        // Test reading byte arrays
        testBytesRead = checksumInputStream.read(testByteArray, 0, testData.length);

        // Then
        Assert.assertEquals("Checksum calculation is incorrect", checksum.getValue(), testByteArray[0] & 0xffL); // first byte for simplicity
        Assert.assertEquals("Unexpected number of bytes read", testData.length, testBytesRead);

        // Cleanup
        checksumInputStream.close();

        // Given
        final InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream emptyChecksumInputStream = new ChecksumCalculatingInputStream(checksum, emptyInputStream);

        // When
        final int emptyRead = emptyChecksumInputStream.read();

        // Then
        Assert.assertEquals("Unexpected non-negative value when reading from empty stream", -1, emptyRead);

        // Given
        final InputStream nullInputStream = null;
        final ChecksumCalculatingInputStream nullChecksumInputStream = new ChecksumCalculatingInputStream(checksum, nullInputStream);

        // When/Then
        Assert.assertThrows(IOException.class, nullChecksumInputStream::read);

        // Given
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream mdInputStream = new ByteArrayInputStream(md.digest(testData));
        final ChecksumCalculatingInputStream mdChecksumInputStream = new ChecksumCalculatingInputStream(md, mdInputStream);

        // When
        final int readMdByte;
        byte[] mdTestByteArray = new byte[md.getDigestSize()];
        int mdTestBytesRead;

        // Test reading single bytes
        while ((readMdByte = mdChecksumInputStream.read()) >= 0) {
            mdTestBytesRead++;
            mdTestArray[mdTestBytesRead - 1] = (byte) readMdByte;
        }

        // Then
        Assert.assertArrayEquals("Checksum calculation is incorrect", md.digest(testData), mdTestArray);

        // Cleanup
        mdChecksumInputStream.close();
    }
}