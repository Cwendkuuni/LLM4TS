package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * Test class for {@link ChecksumCalculatingInputStream}
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() throws IOException {
        // Given
        final Checksum checksum = Checksums.create("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        // When
        final int firstByte = checksumCalculatingInputStream.read();

        // Then
        Assert.assertEquals(-1, firstByte);
        Assert.assertEquals("5C4DCA6E52BDD1B7F5B34D621D6B3202", checksumCalculatingInputStream.getValue().toString());

        // Given
        final byte[] testByteArray = new byte[]{'A', 'B', 'C', 'D', 'E'};
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ChecksumCalculatingInputStream checksumCalculatingOutputStream = new ChecksumCalculatingInputStream(checksum, outputStream);

        // When
        checksumCalculatingOutputStream.write(testByteArray, 0, testByteArray.length);

        // Then
        Assert.assertEquals(testByteArray.length, checksumCalculatingOutputStream.available());
        Assert.assertEquals("6E05B3202D6B32021D6B32025C4DCA6E52BDD1B7F5B34D62", checksumCalculatingOutputStream.getValue().toString());

        // Given
        final byte[] testByteArray2 = new byte[]{'F', 'F', 'F'};
        final ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        final ChecksumCalculatingInputStream checksumCalculatingOutputStream2 = new ChecksumCalculatingInputStream(checksum, outputStream2);

        // When
        checksumCalculatingOutputStream2.write(testByteArray2, 0, testByteArray2.length);

        // Then
        Assert.assertEquals(testByteArray2.length, checksumCalculatingOutputStream2.available());
        Assert.assertNotEquals("5C4DCA6E52BDD1B7F5B34D621D6B3202", checksumCalculatingOutputStream2.getValue().toString());

        // Given
        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(testByteArray);
        final byte[] expectedMD5 = md.digest();

        // When
        final byte[] testByteArray3 = testByteArray;
        final ByteArrayOutputStream outputStream3 = new ByteArrayOutputStream();
        final ChecksumCalculatingInputStream checksumCalculatingOutputStream3 = new ChecksumCalculatingInputStream(checksum, outputStream3);

        // When
        checksumCalculatingOutputStream3.write(testByteArray3, 0, testByteArray3.length);

        // Then
        Assert.assertEquals(testByteArray3.length, checksumCalculatingOutputStream3.available());
        Assert.assertArrayEquals(expectedMD5, checksumCalculatingOutputStream3.getValue());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenStreamIsExhausted() throws IOException {
        // Given
        final Checksum checksum = Checksums.create("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        // When
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();

        // Then - Expected IOException
    }
}