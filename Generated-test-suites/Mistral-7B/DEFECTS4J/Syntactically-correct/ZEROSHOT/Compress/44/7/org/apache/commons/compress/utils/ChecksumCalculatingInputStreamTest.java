package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() throws IOException, NoSuchAlgorithmException {
        // Test with default MD5 checksum
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md, inputStream);

        final byte[] buffer = new byte[inputStream.available()];
        checksumCalculatingInputStream.read(buffer, 0, buffer.length);

        final long expectedChecksum = Checksums.checksum(buffer);
        final long actualChecksum = checksumCalculatingInputStream.getValue();
        Assert.assertEquals("Expected and actual checksums do not match.", expectedChecksum, actualChecksum);

        // Test with custom checksum
        final Checksum customChecksum = new Adler32();
        final InputStream customInputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final ChecksumCalculatingInputStream customChecksumCalculatingInputStream = new ChecksumCalculatingInputStream(customChecksum, customInputStream);

        final byte[] customBuffer = new byte[inputStream.available()];
        customChecksumCalculatingInputStream.read(customBuffer, 0, customBuffer.length);

        final long expectedCustomChecksum = customChecksum.getValue();
        final long actualCustomChecksum = customChecksumCalculatingInputStream.getValue();
        Assert.assertEquals("Expected and actual custom checksums do not match.", expectedCustomChecksum, actualCustomChecksum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChecksumCalculatingInputStreamNullChecksum() {
        new ChecksumCalculatingInputStream(null, new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5}));
    }

    @Test(expected = NullPointerException.class)
    public void testChecksumCalculatingInputStreamNullInputStream() {
        new ChecksumCalculatingInputStream(new Adler32(), null);
    }

    @Test(expected = IOException.class)
    public void testChecksumCalculatingInputStreamIOException() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md, inputStream);

        final byte[] buffer = new byte[inputStream.available()];
        checksumCalculatingInputStream.read(buffer, 0, buffer.length);

        outputStream.write(buffer);
        inputStream.close();
        checksumCalculatingInputStream.close();

        final ByteArrayInputStream expectedChecksumInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md, expectedChecksumInputStream);

        final byte[] buffer2 = new byte[10];
        checksumCalculatingInputStream.read(buffer2, 0, buffer2.length);
        checksumCalculatingInputStream.close();
    }

    @Test
    public void testChecksumCalculatingInputStreamClosed() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md, inputStream);

        final byte[] buffer = new byte[inputStream.available()];
        checksumCalculatingInputStream.read(buffer, 0, buffer.length);
        checksumCalculatingInputStream.close();

        final ByteArrayInputStream expectedChecksumInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final ChecksumCalculatingInputStream closedChecksumCalculatingInputStream = new ChecksumCalculatingInputStream(md, expectedChecksumInputStream);

        try {
            closedChecksumCalculatingInputStream.read();
            Assert.fail("Expected IOException as stream is closed.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testChecksumCalculatingInputStreamClose() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md, inputStream);

        final byte[] buffer = new byte[inputStream.available()];
        checksumCalculatingInputStream.read(buffer, 0, buffer.length);

        checksumCalculatingInputStream.close();
        inputStream.close();

        final ByteArrayInputStream expectedChecksumInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final long expectedChecksum = Checksums.checksum(buffer);
        final long actualChecksum = ChecksumCalculatingInputStream.class.newInstance().getValue();

        Assert.assertEquals("Expected and actual checksums do not match.", expectedChecksum, actualChecksum);
    }
}