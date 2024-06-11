package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.io.input.NullInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream.
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test for constructor.
     */
    @Test
    public void testConstructor() {
        final InputStream inputStream = new NullInputStream();
        final Checksum checksum = new Adler32(); // Use any checksum implementation
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        Assert.assertNotNull(checksumCalculatingInputStream);
        Assert.assertEquals(inputStream, checksumCalculatingInputStream.in);
        Assert.assertEquals(checksum, checksumCalculatingInputStream.checksum);
    }

    /**
     * Test for read method with single byte.
     * @throws IOException if the stream is exhausted and the Checksum doesn't match the expected value
     */
    @Test
    public void testReadSingleByte() throws IOException {
        final byte expectedByte = 0x01;
        final byte testByte = 0x02;
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{expectedByte});
        final Checksum checksum = new Adler32();
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        final int readByte = checksumCalculatingInputStream.read();
        Assert.assertEquals(expectedByte, readByte);
        Assert.assertArrayEquals(new byte[]{expectedByte}, checksum.getValue());

        // Test with testByte instead of expectedByte
        checksumCalculatingInputStream.read();
        Assert.assertEquals(testByte, readByte);
        Assert.assertArrayEquals(new byte[]{expectedByte, testByte}, checksum.getValue());

        // Test with end of stream
        Assert.assertEquals(-1, checksumCalculatingInputStream.read());
        Assert.assertArrayEquals(new byte[]{expectedByte}, checksum.getValue());
    }

    /**
     * Test for read method with byte array.
     * @throws IOException if the stream is exhausted and the Checksum doesn't match the expected value
     */
    @Test
    public void testReadByteArray() throws IOException, NoSuchAlgorithmException {
        final byte[] expectedBytes = new byte[]{0x01, 0x02};
        final InputStream inputStream = new ByteArrayInputStream(expectedBytes);
        final MessageDigest md = MessageDigest.getInstance("MD5"); // Use any checksum algorithm
        final Checksum checksum = new MessageDigestChecksum(md);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int readBytes = checksumCalculatingInputStream.read(new byte[expectedBytes.length]);
        Assert.assertEquals(readBytes, expectedBytes.length);
        Assert.assertArrayEquals(expectedBytes, checksum.getValue());

        // Test with larger byte array
        final byte[] largerByteArray = new byte[expectedBytes.length + 1];
        System.arraycopy(expectedBytes, 0, largerByteArray, 0, expectedBytes.length);
        largerByteArray[expectedBytes.length] = 0x03;
        final int readLargerByteArray = checksumCalculatingInputStream.read(largerByteArray, 0, largerByteArray.length);
        Assert.assertEquals(readLargerByteArray, expectedBytes.length);
        Assert.assertArrayEquals(expectedBytes, checksum.getValue());

        // Test with end of stream
        final int readEndOfStream = checksumCalculatingInputStream.read();
        Assert.assertEquals(-1, readEndOfStream);
        Assert.assertArrayEquals(expectedBytes, checksum.getValue());
    }

    /**
     * Test for read method with byte array and offset.
     * @throws IOException if the stream is exhausted and the Checksum doesn't match the expected value
     */
    @Test
    public void testReadByteArrayWithOffset() throws IOException, NoSuchAlgorithmException {
        final byte[] expectedBytes = new byte[]{0x01, 0x02};
        final InputStream inputStream = new ByteArrayInputStream(expectedBytes);
        final MessageDigest md = MessageDigest.getInstance("MD5"); // Use any checksum algorithm
        final Checksum checksum = new MessageDigestChecksum(md);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int offset = 1;
        final int readBytes = checksumCalculatingInputStream.read(expectedBytes, offset, expectedBytes.length - offset);
        Assert.assertEquals(expectedBytes.length - offset, readBytes);
        Assert.assertArrayEquals(Arrays.copyOfRange(expectedBytes, offset, expectedBytes.length), checksum.getValue());

        // Test with negative offset
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> checksumCalculatingInputStream.read(expectedBytes, -1, expectedBytes.length));

        // Test with end of stream
        final int readEndOfStream = checksumCalculatingInputStream.read();
        Assert.assertEquals(-1, readEndOfStream);
        Assert.assertArrayEquals(expectedBytes, checksum.getValue());
    }

    /**
     * Test for skip method.
     * @throws IOException if the stream is exhausted and the Checksum doesn't match the expected value
     */
    @Test
    public void testSkip() throws IOException, NoSuchAlgorithmException {
        final byte[] expectedBytes = new byte[]{0x01, 0x02};
        final InputStream inputStream = new ByteArrayInputStream(expectedBytes);
        final MessageDigest md = MessageDigest.getInstance("MD5"); // Use any checksum algorithm
        final Checksum checksum = new MessageDigestChecksum(md);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        // Test with positive number
        checksumCalculatingInputStream.skip(1);
        Assert.assertEquals(expectedBytes[0], checksumCalculatingInputStream.read());

        // Test with negative number
        Assert.assertThrows(NegativeArraySizeException.class, () -> checksumCalculatingInputStream.skip(-1));

        // Test with end of stream
        checksumCalculatingInputStream.skip(expectedBytes.length);
        Assert.assertEquals(-1, checksumCalculatingInputStream.read());
        Assert.assertArrayEquals(expectedBytes, checksum.getValue());
    }

    /**
     * Test for getValue method.
     */
    @Test
    public void testGetValue() {
        final byte[] expectedBytes = new byte[]{0x01, 0x02};
        final InputStream inputStream = new ByteArrayInputStream(expectedBytes);
        final MessageDigest md = MessageDigest.getInstance("MD5"); // Use any checksum algorithm
        final Checksum checksum = new MessageDigestChecksum(md);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        checksumCalculatingInputStream.close();
        Assert.assertArrayEquals(expectedBytes, checksum.getValue());
    }

}