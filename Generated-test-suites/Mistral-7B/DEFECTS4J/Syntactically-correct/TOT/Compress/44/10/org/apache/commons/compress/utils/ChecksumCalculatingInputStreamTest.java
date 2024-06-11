import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() throws IOException, NoSuchAlgorithmException {
        final byte[] testData = "Apache Commons Compress Test".getBytes(StandardCharsets.UTF_8);
        final Checksum expectedChecksum = new Adler32(); // Use any checksum algorithm of your choice
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(expectedChecksum, inputStream);

        // Test reading single bytes
        final byte[] readBytes = new byte[testData.length];
        int bytesRead = 0;
        while (bytesRead < testData.length) {
            final int readByte = checksumCalculatingInputStream.read();
            if (readByte >= 0) {
                readBytes[bytesRead] = (byte) readByte;
                bytesRead++;
            }
        }
        Assert.assertArrayEquals("Incorrect data read from stream", testData, readBytes);
        Assert.assertEquals("Checksum doesn't match expected value", expectedChecksum.getValue(), checksumCalculatingInputStream.getValue());

        // Test reading byte arrays
        checksumCalculatingInputStream.read(readBytes, 0, testData.length);
        Assert.assertArrayEquals("Incorrect data read from stream", testData, readBytes);
        Assert.assertEquals("Checksum doesn't match expected value", expectedChecksum.getValue(), checksumCalculatingInputStream.getValue());

        // Test reading byte arrays with offset
        final byte offsetByte = testData[0];
        checksumCalculatingInputStream.read(readBytes, 1, testData.length - 1);
        Assert.assertArrayEquals("Incorrect data read from stream", new byte[] {offsetByte}, new byte[] {readBytes[0]});
        System.arraycopy(readBytes, 1, readBytes, 0, testData.length - 1);
        Assert.assertArrayEquals("Incorrect data read from stream", new byte[]{testData[1]}, readBytes);
        Assert.assertEquals("Checksum doesn't match expected value", expectedChecksum.getValue(), checksumCalculatingInputStream.getValue());

        // Test reading byte arrays with negative length
        checksumCalculatingInputStream.read(readBytes, -1, testData.length);
        Assert.assertEquals("Unexpected data read from stream", -1, checksumCalculatingInputStream.read());

        // Test skipping
        checksumCalculatingInputStream.skip(testData.length);
        Assert.assertEquals("Unexpected data read from stream", -1, checksumCalculatingInputStream.read());

        // Test reading beyond EOF
        checksumCalculatingInputStream.skip(Long.MAX_VALUE);
        Assert.assertEquals("Unexpected data read from stream", -1, checksumCalculatingInputStream.read());

        // Test reading from empty stream
        final ChecksumCalculatingInputStream emptyChecksumCalculatingInputStream = new ChecksumCalculatingInputStream(Checksums.ADLER32, new ByteArrayInputStream(new byte[0]));
        Assert.assertEquals("Checksum of empty stream is not 0", 0, emptyChecksumCalculatingInputStream.getValue());
    }
}