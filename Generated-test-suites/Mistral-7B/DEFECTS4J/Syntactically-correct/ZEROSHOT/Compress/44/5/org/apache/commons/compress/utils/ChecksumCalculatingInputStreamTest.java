package org.apache.commons.compress.utils;

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

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        InputStream in = new ByteArrayInputStream(new byte[]{0x01});
        Checksum checksum = new Adler32(); // Or any other checksum implementation
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);
        Assert.assertNotNull(inputStream);
        Assert.assertSame(in, inputStream.in);
        Assert.assertSame(checksum, inputStream.checksum);
    }

    /**
     * Test reading a single byte.
     */
    @Test
    public void testReadSingleByte() throws IOException {
        byte[] inputData = {'A'};
        InputStream in = new ByteArrayInputStream(inputData);
        ChecksumMD5 checksum = new MessageDigest("MD5"); // Or any other checksum implementation
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        byte readByte = (byte) inputStream.read();

        // Verify the byte was read properly from the underlying stream
        Assert.assertEquals(inputData[0], readByte);

        // Verify the checksum was updated with the read byte
        byte[] expectedChecksum = new byte[16]; // MD5 checksum length
        inputStream.read(expectedChecksum, 0, expectedChecksum.length);
        byte[] resultChecksum = checksum.digest();

        Assert.assertArrayEquals(resultChecksum, expectedChecksum);
    }

    /**
     * Test reading a byte array.
     */
    @Test
    public void testReadByteArray() throws IOException, NoSuchAlgorithmException {
        byte[] inputData = {'A', 'B', 'C'};
        InputStream in = new ByteArrayInputStream(inputData);
        ChecksumMD5 checksum = new MessageDigest("MD5");
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] readData = new byte[inputData.length];
        int len = inputStream.read(readData);

        // Verify the bytes were read properly from the underlying stream
        Assert.assertEquals(inputData.length, len);
        Assert.assertArrayEquals(inputData, readData);

        // Verify the checksum was updated with the read bytes
        byte[] expectedChecksum = new byte[16];
        inputStream.read(expectedChecksum, 0, expectedChecksum.length);
        byte[] resultChecksum = checksum.digest(inputData);

        Assert.assertArrayEquals(resultChecksum, expectedChecksum);
    }

    /**
     * Test reading a byte array with an offset and length.
     */
    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException, NoSuchAlgorithmException {
        byte[] inputData = {'A', 'B', 'C'};
        InputStream in = new ByteArrayInputStream(inputData);
        ChecksumMD5 checksum = new MessageDigest("MD5");
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] readData = new byte[inputData.length];
        int offset = 0;
        int length = inputData.length;
        int len = inputStream.read(readData, offset, length);

        // Verify the bytes were read properly from the underlying stream
        Assert.assertEquals(inputData.length, len);
        Assert.assertArrayEquals(inputData, readData);

        // Verify the checksum was updated with the read bytes
        byte[] expectedChecksum = new byte[16];
        inputStream.read(expectedChecksum, 0, expectedChecksum.length);
        byte[] resultChecksum = checksum.digest(inputData);

        Assert.assertArrayEquals(resultChecksum, expectedChecksum);
    }

    /**
     * Test skipping bytes.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSkip() throws IOException {
        byte[] inputData = {'A', 'B', 'C'};
        InputStream in = new ByteArrayInputStream(inputData);
        ChecksumMD5 checksum = new MessageDigest("MD5");
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        inputStream.skip(1);
    }

    /**
     * Test getting the calculated checksum.
     */
    @Test
    public void testGetValue() throws IOException, NoSuchAlgorithmException {
        byte[] inputData = {'A', 'B', 'C'};
        InputStream in = new ByteArrayInputStream(inputData);
        ChecksumMD5 checksum = new MessageDigest("MD5");
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] expectedChecksum = new byte[16];
        inputStream.read(expectedChecksum, 0, expectedChecksum.length);

        long calculatedChecksum = inputStream.getValue();
        byte[] resultChecksum = checksum.digest(inputData);

        Assert.assertArrayEquals(resultChecksum, expectedChecksum);
        Assert.assertEquals(calculatedChecksum, ChecksumUtils.bytesToLong(expectedChecksum));
    }
}