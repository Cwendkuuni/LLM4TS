package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksum;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test suite for class ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test method for {@link ChecksumCalculatingInputStream#ChecksumCalculatingInputStream(Checksum, InputStream)}
     * constructor.
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testChecksumCalculatingInputStreamConstructor() throws IOException {
        final Checksum checksum = new Adler32();
        final byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        assertEquals(data.length, checksumInputStream.available());
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#read()} method.
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testRead() throws IOException {
        final Checksum checksum = new Adler32();
        final byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int readByte = checksumInputStream.read();
        assertEquals(-1, readByte);

        final byte[] buffer = new byte[data.length];
        final int bytesRead = checksumInputStream.read(buffer);
        assertEquals(data.length, bytesRead);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#read(byte[], int, int)} method.
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testReadByteArray() throws IOException {
        final Checksum checksum = new Adler32();
        final byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        final byte[] buffer = new byte[data.length];

        final int bytesRead = checksumInputStream.read(buffer);
        assertEquals(data.length, bytesRead);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#skip(long)} method.
     * @throws IOException if an error occurs during the test
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSkip() throws IOException {
        final Checksum checksum = new Adler32();
        final byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        checksumInputStream.skip(data.length);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#getValue()} method.
     * @throws IOException if an error occurs during the test
     * @throws NoSuchAlgorithmException if an unsupported algorithm is used
     */
    @Test
    public void testGetValue() throws IOException, NoSuchAlgorithmException {
        final Checksum checksum = new MessageDigest("MD5");
        final byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int bytesRead = checksumInputStream.read(new byte[data.length]);
        final byte[] expectedValue = checksum.digest();

        final byte[] actualValue = new byte[16];
        System.arraycopy(checksumInputStream.getValue(), 0, actualValue, 0, actualValue.length);

        assertArrayEquals(expectedValue, actualValue);
    }
}