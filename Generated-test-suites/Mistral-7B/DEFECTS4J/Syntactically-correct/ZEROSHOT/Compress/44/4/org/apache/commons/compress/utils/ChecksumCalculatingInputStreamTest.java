package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test file for ChecksumCalculatingInputStream class
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() throws IOException, NoSuchAlgorithmException {
        final byte[] testData = "Test Data".getBytes(StandardCharsets.UTF_8);
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final Checksum expectedChecksum = new Checksums.Adler32();
        expectedChecksum.update(testData);

        final InputStream inputStream = new ByteArrayInputStream(testData);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(expectedChecksum, inputStream);

        final byte[] readData = new byte[testData.length];
        final int readLength = checksumInputStream.read(readData);
        Assert.assertEquals("Unexpected number of bytes read", testData.length, readLength);

        final long checksumValue = checksumInputStream.getValue();
        Assert.assertArrayEquals("Unexpected checksum value", expectedChecksum.getValue(), new byte[]{ (byte) 0x64, (byte) 0x8d, (byte) 0x47, (byte) 0x48, (byte) 0x53, (byte) 0x54, (byte) 0x4f, (byte) 0x4d, (byte) 0x31, (byte) 0x34, (byte) 0x38, (byte) 0x39, (byte) 0x36, (byte) 0x35, (byte) 0x32, (byte) 0x36, (byte) 0x38, (byte) 0x37 } );

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] readBuffer = new byte[1024];
        int bytesRead;
        do {
            bytesRead = checksumInputStream.read(readBuffer);
            if (bytesRead > 0) {
                outputStream.write(readBuffer, 0, bytesRead);
            }
        } while (bytesRead != -1);

        final byte[] outputData = outputStream.toByteArray();
        final MessageDigest mdInstance = MessageDigest.getInstance("MD5");
        mdInstance.update(outputData);
        final byte[] checksumBytes = mdInstance.digest();

        Assert.assertArrayEquals("Unexpected checksum for the output data", checksumBytes, new byte[]{ (byte) 0x64, (byte) 0x8d, (byte) 0x47, (byte) 0x48, (byte) 0x53, (byte) 0x54, (byte) 0x4f, (byte) 0x4d, (byte) 0x31, (byte) 0x34, (byte) 0x38, (byte) 0x39, (byte) 0x36, (byte) 0x35, (byte) 0x32, (byte) 0x36, (byte) 0x38, (byte) 0x37 } );
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenClosingStream() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(null, inputStream);
        checksumInputStream.close();
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerExceptionWhenCreatingChecksumCalculatingInputStreamWithNullChecksum() {
        final InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));
        new ChecksumCalculatingInputStream(null, inputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerExceptionWhenCreatingChecksumCalculatingInputStreamWithNullInputStream() {
        new ChecksumCalculatingInputStream(new Checksum(), null);
    }
}