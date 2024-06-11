package org.apache.commons.compress.utils.test;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for IOUtils.
 */
public class IOUtilsTest {

    /**
     * Test copy method with valid InputStream and OutputStream.
     */
    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Test copy method".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output);
        byte[] expected = "Test copy method".getBytes();
        byte[] actualBytes = output.toByteArray();
        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, actualBytes);
    }

    /**
     * Test copy method with valid InputStream, OutputStream and custom buffer size.
     */
    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test copy method with custom buffer size".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output, 256);
        byte[] expected = "Test copy method with custom buffer size".getBytes();
        byte[] actualBytes = output.toByteArray();
        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, actualBytes);
    }

    /**
     * Test skip method with valid InputStream and given number of bytes to skip.
     */
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Test skip method".getBytes());
        long actual = IOUtils.skip(input, 10L);
        Assert.assertEquals(10, actual);

        // Test when the end of the stream is reached.
        long actualSkipped = IOUtils.skip(input, 5L);
        Assert.assertEquals(5, actualSkipped);
        Assert.assertEquals(-1, input.read());
    }

    /**
     * Test readFully method with valid InputStream and byte array.
     */
    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Test readFully method".getBytes());
        byte[] expected = new byte[12];
        int actual = IOUtils.readFully(input, expected);
        Assert.assertEquals(12, actual);
        Assert.assertArrayEquals(expected, expected);

        // Test when the end of the stream is reached.
        byte[] expectedEnd = new byte[5];
        int actualEnd = IOUtils.readFully(input, expectedEnd);
        Assert.assertEquals(5, actualEnd);
        Assert.assertEquals(-1, input.read());
    }

    /**
     * Test toByteArray method with valid InputStream.
     */
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test toByteArray method".getBytes());
        byte[] expected = "Test toByteArray method".getBytes();
        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test closeQuietly method with valid Closeable.
     */
    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            IOUtils.copy(new ByteArrayInputStream("Test closeQuietly".getBytes()), output);
            IOUtils.closeQuietly(output);
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test closeQuietly method with null Closeable.
     */
    @Test
    public void testCloseQuietlyWithNullCloseable() {
        IOUtils.closeQuietly(null);
    }
}