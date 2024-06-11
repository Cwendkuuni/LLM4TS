package org.apache.commons.compress.utils;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * IOUtils Test Cases
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        long result = IOUtils.copy(input, output);
        Assert.assertEquals(13, result); // 13 bytes in "Hello World"
        byte[] expected = "Hello World".getBytes();
        byte[] actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        long result = IOUtils.copy(input, output, 2);
        Assert.fail("Expected an IOException with a buffer size of 2");
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World Hello World".getBytes());
        long expected = 11;
        long result = IOUtils.skip(input, 22);
        Assert.assertEquals(expected, result); // skip 11 bytes, leaving "Hello Wo"
        long skipped = input.skip(5);
        Assert.assertEquals(5, skipped); // skip 5 more bytes, leaving "Hello W"
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        int expected = 13;
        byte[] buffer = new byte[expected];
        int result = IOUtils.readFully(input, buffer);
        Assert.assertEquals(expected, result); // read 13 bytes
        byte[] actual = buffer;
        byte[] expectedByteArray = "Hello World".getBytes();
        Assert.assertArrayEquals(expectedByteArray, actual);
    }

    @Test
    public void testReadFullyWithBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        int expected = 13;
        byte[] buffer = new byte[expected + 1];
        int result = IOUtils.readFully(input, buffer, 0, expected);
        Assert.assertEquals(expected, result); // read 13 bytes
        byte[] actual = new byte[expected];
        System.arraycopy(buffer, 0, actual, 0, expected);
        Assert.assertArrayEquals("Hello World".getBytes(), actual);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        byte[] expected = "Hello World".getBytes();
        byte[] result = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            IOUtils.copy(new ByteArrayInputStream(new byte[0]), output);
            IOUtils.closeQuietly(output);
        } catch (IOException e) {
            Assert.fail("Expected no IOException");
        }
    }

    @Test(expected = IOException.class)
    public void testCopyWithNullArguments() throws IOException {
        InputStream input = null;
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(input, output);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyWithNullOutputStream() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        OutputStream output = null;
        IOUtils.copy(input, output);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testReadFullyWithNegativeLength() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        byte[] buffer = new byte[5];
        IOUtils.readFully(input, buffer, 10, -5);
    }

    @Test(expected = IOException.class)
    public void testCopyWithIOException() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes()) {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        OutputStream output = new ByteArrayOutputStream();
        long result = IOUtils.copy(input, output);
        Assert.assertEquals(-1, result);
    }
}