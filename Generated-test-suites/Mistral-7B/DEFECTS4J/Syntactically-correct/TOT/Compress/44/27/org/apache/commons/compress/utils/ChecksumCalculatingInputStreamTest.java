package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.io.input.ByteArrayInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.util.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test cases for ChecksumCalculatingInputStream class.
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test read method with single byte.
     */
    @Test
    public void testReadSingleByte() throws IOException, NoSuchAlgorithmException {
        byte[] testData = {0x01};
        InputStream inputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), new ByteArrayInputStream(testData));
        int readByte = inputStream.read();
        byte expectedByte = testData[0];
        Assert.assertEquals("Unexpected byte read.", expectedByte, readByte);
        byte[] checksum = inputStream.getValue();
        Assert.assertArrayEquals("Unexpected checksum.", testData, checksum);
    }

    /**
     * Test read method with byte array.
     */
    @Test
    public void testReadByteArray() throws IOException, NoSuchAlgorithmException {
        byte[] testData = {0x01, 0x02, 0x03};
        InputStream inputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), new ByteArrayInputStream(testData));
        byte[] readBytes = new byte[testData.length];
        int bytesRead = inputStream.read(readBytes);
        Assert.assertEquals("Unexpected bytes read.", testData.length, bytesRead);
        byte[] checksum = inputStream.getValue();
        Assert.assertArrayEquals("Unexpected checksum.", testData, checksum);
    }

    /**
     * Test read method with zero-length byte array.
     */
    @Test
    public void testReadZeroLengthByteArray() throws IOException {
        byte[] testData = {};
        InputStream inputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), new ByteArrayInputStream(testData));
        byte[] readBytes = new byte[0];
        int bytesRead = inputStream.read(readBytes);
        Assert.assertEquals("Unexpected bytes read.", 0, bytesRead);
        byte[] checksum = inputStream.getValue();
        Assert.assertArrayEquals("Unexpected checksum.", testData, checksum);
    }

    /**
     * Test skip method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSkip() throws IOException {
        byte[] testData = {0x01, 0x02, 0x03};
        InputStream inputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), new ByteArrayInputStream(testData));
        inputStream.skip(Long.MAX_VALUE);
    }

    /**
     * Test getting checksum value after reading all data.
     */
    @Test
    public void testGetChecksumValue() throws IOException, NoSuchAlgorithmException {
        byte[] testData = {0x01, 0x02, 0x03};
        InputStream inputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), new ByteArrayInputStream(testData));
        byte[] readBytes = new byte[testData.length];
        inputStream.read(readBytes);
        byte[] expectedChecksum = MessageDigest.getInstance("MD5").digest(testData);
        byte[] checksum = inputStream.getValue();
        Assert.assertArrayEquals("Unexpected checksum.", expectedChecksum, checksum);
    }
}