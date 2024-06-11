package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

/**
 * Test class for IOUtils
 */
public class IOUtilsTest {

    /**
     * Test copy method
     * @throws IOException
     */
    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long length = IOUtils.copy(input, output);
        assertEquals("Test String".getBytes().length, length);
        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        IOUtils.copy(input, expectedOutput);
        byte[] expectedBytes = expectedOutput.toByteArray();
        byte[] actualBytes = output.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test copy with different buffer size
     * @throws IOException
     */
    @Test
    public void testCopyWithDifferentBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = new ByteArrayOutputStream(10);
        long length = IOUtils.copy(input, output);
        assertEquals("Test String".getBytes().length, length);
        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        IOUtils.copy(input, expectedOutput, 10);
        byte[] expectedBytes = expectedOutput.toByteArray();
        byte[] actualBytes = output.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test skip method
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        long numToSkip = 5;
        long skipped = IOUtils.skip(input, numToSkip);
        assertEquals(numToSkip, skipped);
        byte[] expectedBytes = new byte[10];
        System.arraycopy("Test String".getBytes(), 0, expectedBytes, 0, "Test String".getBytes().length);
        byte[] actualBytes = new byte[10];
        IOUtils.skip(new ByteArrayInputStream(expectedBytes), 5);
        assertArrayEquals(new byte[5], actualBytes);
    }

    /**
     * Test skip when reaching the end of the stream
     * @throws IOException
     */
    @Test
    public void testSkipWhenEndReached() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        long numToSkip = "Test String".getBytes().length + 1;
        long skipped = IOUtils.skip(input, numToSkip);
        assertEquals(0, skipped);
        byte[] expectedBytes = "Test String".getBytes();
        byte[] actualBytes = new byte[expectedBytes.length + 1];
        IOUtils.skip(new ByteArrayInputStream(expectedBytes), numToSkip);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test readFully method
     * @throws IOException
     */
    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] expectedBytes = new byte[10];
        byte[] actualBytes = new byte[10];
        int length = IOUtils.readFully(input, expectedBytes);
        assertEquals(expectedBytes.length, length);
        assertEquals(expectedBytes, actualBytes);
    }

    /**
     * Test toByteArray method
     * @throws IOException
     */
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] expectedBytes = "Test String".getBytes();
        byte[] actualBytes = IOUtils.toByteArray(input);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test closing a closeable quietly
     */
    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            IOUtils.closeQuietly(output);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}