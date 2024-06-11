import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Test class for ChecksumCalculatingInputStream.
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test method for {@link ChecksumCalculatingInputStream#ChecksumCalculatingInputStream(Checksum, InputStream)}.
     */
    @Test
    public void testChecksumCalculatingInputStream() {
        final byte[] inputData = "Test data".getBytes(StandardCharsets.UTF_8);
        final Checksum checksum = Checksums.adler32();
        final InputStream inputStream = new ByteArrayInputStream(inputData);

        // Basic use case
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        final int read;
        final byte[] buffer = new byte[inputData.length];
        do {
            read = checksumCalculatingInputStream.read(buffer);
        } while (read > 0);

        Assert.assertEquals("Checksum doesn't match", checksum.getValue(), 1652431348L); // Adler32 checksum for "Test data"

        // Edge case: empty input
        final ChecksumCalculatingInputStream emptyChecksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(new byte[]{}));
        try {
            emptyChecksumCalculatingInputStream.read();
        } catch (IOException e) {
            // Expected
        }
        Assert.assertEquals("Checksum value should be 0 for empty input", checksum.getValue(), 0L);

        // Error case: IOException from underlying InputStream
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final InputStream errorInput = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        errorInput.wrap(byteArrayOutputStream); // Wrap it to ByteArrayOutputStream for easy validation
        final ChecksumCalculatingInputStream errorChecksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, errorInput);
        try {
            errorChecksumCalculatingInputStream.read();
            errorChecksumCalculatingInputStream.read();
            errorChecksumCalculatingInputStream.read();
        } catch (IOException e) {
            Assert.assertEquals("Unexpected exception type", "Unexpected EOF", e.getMessage());
        }
        Assert.assertEquals("Checksum value should be 0 for any errors", checksum.getValue(), 0L);
    }

}