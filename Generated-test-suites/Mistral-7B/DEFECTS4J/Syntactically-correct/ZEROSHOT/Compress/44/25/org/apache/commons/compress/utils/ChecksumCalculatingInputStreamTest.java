package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test file for ChecksumCalculatingInputStream class
 */
public class ChecksumCalculatingInputStreamTest {

    private static final int TEST_DATA_LENGTH = 1024;
    private static final byte[] TEST_DATA = new byte[TEST_DATA_LENGTH];

    static {
        // Initialize test data with some value
        for (int i = 0; i < TEST_DATA_LENGTH; i++) {
            TEST_DATA[i] = (byte) (i % 256);
        }
    }

    @Test
    public void testChecksumCalculatingInputStream() throws IOException, NoSuchAlgorithmException {
        // Test with default MD5 checksum
        Checksum checksum = Checksums.createChecksum("MD5");
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int read = 0;
        byte[] buffer = new byte[TEST_DATA_LENGTH];
        while ((read = checksumInputStream.read(buffer)) > 0) {
            // Do something with the data, for example, verify the checksum
        }
        long calculatedChecksum = checksum.getValue();
        long expectedChecksum = getExpectedChecksum("MD5");
        Assert.assertEquals("Calculated checksum doesn't match the expected value.", expectedChecksum, calculatedChecksum);

        // Test with custom checksum (SHA-256 in this example)
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        checksum = new Checksum() {
            @Override
            public int calcSize() {
                return md.digest().length * 8;
            }

            @Override
            public void reset() {
                md.reset();
            }

            @Override
            public void update(byte b) {
                md.update(new byte[]{b});
            }

            @Override
            public void update(byte[] b, int off, int len) {
                md.update(new ByteBuffer.allocate(len).put(ByteBuffer.wrap(b), off, len).array());
            }

            @Override
            public long getValue() {
                byte[] digest = md.digest();
                return ByteBuffer.wrap(digest).getLong(0);
            }
        };
        checksumInputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(TEST_DATA));

        while ((read = checksumInputStream.read(buffer)) > 0) {
            // Do something with the data, for example, verify the checksum
        }
        calculatedChecksum = checksum.getValue();
        expectedChecksum = getExpectedChecksum("SHA-256");
        Assert.assertEquals("Calculated checksum doesn't match the expected value.", expectedChecksum, calculatedChecksum);
    }

    private long getExpectedChecksum(String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(TEST_DATA);
        byte[] digest = md.digest();
        return ByteBuffer.wrap(digest).getLong(0);
    }
}