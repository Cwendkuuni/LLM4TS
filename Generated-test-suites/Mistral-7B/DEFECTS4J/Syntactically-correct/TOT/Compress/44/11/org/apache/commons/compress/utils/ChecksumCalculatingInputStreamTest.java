package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.io.input.ByteArrayInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.util.ByteArrays;
import org.apache.commons.io.util.IOUtils;
import org.apache.commons.testing.TestUtils;
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
 * Test cases for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test that the checksum is calculated correctly on a single byte.
     * @throws IOException if the stream fails
     */
    @Test
    public void testSingleByte() throws IOException {
        InputStream inputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), new ByteArrayInputStream(new byte[]{0x1}));
        long checksum = inputStream.getValue();
        Assert.assertArrayEquals(TestUtils.getExpectedByteArrayForChecksumAlgorithm("MD5", "01"), ByteArrays.toByteArray(Long.toByteArray(checksum)));
    }

    /**
     * Test that the checksum is calculated correctly on a byte array.
     * @throws IOException if the stream fails
     */
    @Test
    public void testByteArray() throws IOException {
        InputStream inputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), new ByteArrayInputStream(TestData.BYTE_ARRAY));
        long checksum = inputStream.getValue();
        Assert.assertArrayEquals(TestData.MD5_CHECKSUM, ByteArrays.toByteArray(Long.toByteArray(checksum)));
    }

    /**
     * Test that the checksum is calculated correctly on a large byte array.
     * @throws IOException if the stream fails
     * @throws NoSuchAlgorithmException if the algorithm is not supported
     */
    @Test
    public void testLargeByteArray() throws IOException, NoSuchAlgorithmException {
        int size = 1024 * 1024; // 1MB
        byte[] data = new byte[size];
        if (TestUtils.isWindows()) {
            // Windows has a limit on the size of a Java InputStream
            size = 1024 * 1024 / 2;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(size);
        outputStream.write(TestData.BYTE_ARRAY, 0, TestData.BYTE_ARRAY.length);
        inputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), new ByteArrayInputStream(outputStream.toByteArray()));
        long checksum = inputStream.getValue();
        Assert.assertArrayEquals(TestData.MD5_CHECKSUM, ByteArrays.toByteArray(Long.toByteArray(checksum)));
    }

    /**
     * Test that the checksum is calculated correctly on an empty input stream.
     * @throws IOException if the stream fails
     */
    @Test
    public void testEmptyStream() throws IOException {
        InputStream inputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), new ByteArrayInputStream(new byte[0][]));
        long checksum = inputStream.getValue();
        Assert.assertArrayEquals(new byte[0][], ByteArrays.toByteArray(Long.toByteArray(checksum)));
    }

    /**
     * Test that the checksum is calculated correctly on a failing input stream.
     * @throws IOException if the stream fails
     */
    @Test(expected = IOException.class)
    public void testFailingStream() throws IOException {
        InputStream inputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), new ByteArrayInputStream(new byte[]{0x1}));
        inputStream.close();
        long checksum = inputStream.getValue();
    }

    /**
     * Test data for the tests.
     */
    private static class TestData {
        static final byte[] BYTE_ARRAY = "Test data".getBytes(StandardCharsets.UTF_8);
        static final byte[] MD5_CHECKSUM = new byte[]{(byte) 0x30, (byte) 0xcc, (byte) 0x1d, (byte) 0x69, (byte) 0x9d, (byte) 0x6f, (byte) 0x2e, (byte) 0x5e, (byte) 0x57, (byte) 0x0e, (byte) 0x36, (byte) 0x07, (byte) 0x34, (byte) 0x1f, (byte) 0xac};
    }

}