package org.apache.commons.compress.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() throws IOException, NoSuchAlgorithmException {
        // Test with default MD5 checksum
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Checksum checksum = Checksums.md5();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] testData = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        outputStream.write(testData);
        outputStream.flush();

        byte[] expectedMd5 = {0x30, 0x36, 0x2d, 0x64, 0x68, 0x3e, 0x6b, 0x31};

        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = checksumCalculatingInputStream.read(buffer)) != -1) {
            // Do nothing, just read all data from the stream
        }

        Assert.assertArrayEquals("Checksum doesn't match", expectedMd5, checksum.getValue());

        // Test with SHA-1 checksum
        MessageDigest sha1MessageDigest = MessageDigest.getInstance("SHA-1");
        checksum = new ChecksumAdapters.ChecksumAdapter(sha1MessageDigest);
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] testDataSha1 = {0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        outputStream.write(testDataSha1);
        outputStream.flush();

        byte[] expectedSha1 = {0x31, 0x49, 0x21, 0x0e, 0x1b, 0x30, 0xd3, 0x0c, 0x4c, 0x1d, 0x85, 0x96, 0x05, 0x9d, 0x5b, 0xd2, 0x2d, 0x0b, 0x21, 0x0b};

        while ((read = checksumCalculatingInputStream.read(buffer)) != -1) {
            // Do nothing, just read all data from the stream
        }

        Assert.assertArrayEquals("Checksum doesn't match", expectedSha1, checksum.getValue());

        // Test reading from the stream with a custom checksum
        MessageDigest customChecksumMessageDigest = MessageDigest.getInstance("SHA-256");
        checksum = new ChecksumAdapters.ChecksumAdapter(customChecksumMessageDigest);
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(testDataSha1));

        byte[] result = new byte[16];
        int bytesRead = checksumCalculatingInputStream.read(result, 0, result.length);

        Assert.assertEquals("Unexpected number of bytes read", bytesRead, result.length);
        Assert.assertArrayEquals("Checksum doesn't match", expectedSha1, result);
    }

    /**
     * Helper class for creating checksum instances
     */
    private static class Checksums {
        public static Checksum md5() {
            try {
                return new ChecksumAdapters.ChecksumAdapter(MessageDigest.getInstance("MD5"));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }
}