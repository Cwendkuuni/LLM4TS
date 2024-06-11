package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for IOUtils
 */
public class IOUtilsTest {

    /**
     * Test copy method
     */
    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{'a', 'b', 'c', 'd'});
        long bytesCopied = IOUtils.copy(input, output);
        Assert.assertEquals(4, bytesCopied);
        byte[] expected = new byte[]{'a', 'b', 'c', 'd'};
        byte[] actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test copy method with different buffer sizes
     */
    @Test
    public void testCopyWithDifferentBufferSizes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{'a', 'b', 'c', 'd'});
        long bytesCopiedWithDefaultBufferSize = IOUtils.copy(input, output);
        long bytesCopiedWithCustomBufferSize = IOUtils.copy(input, output, 1024);
        Assert.assertEquals(4, bytesCopiedWithDefaultBufferSize);
        Assert.assertEquals(4, bytesCopiedWithCustomBufferSize);
    }

    /**
     * Test skip method
     */
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{'a', 'b', 'c', 'd', 'e'});
        long numToSkip = 2;
        long bytesSkipped = IOUtils.skip(input, numToSkip);
        Assert.assertEquals(2, bytesSkipped);
        long availableBytes = input.available();
        Assert.assertEquals(3, availableBytes);
    }

    /**
     * Test readFully method
     */
    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{'a', 'b', 'c', 'd', 'e'});
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(input, buffer);
        Assert.assertEquals(5, bytesRead);
        byte[] expected = new byte[]{'a', 'b', 'c', 'd', 'e'};
        byte[] actual = new byte[5];
        System.arraycopy(buffer, 0, actual, 0, 5);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test readFully method with less bytes to read than available
     */
    @Test
    public void testReadFullyWithLessBytesToRead() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{'a', 'b', 'c', 'd', 'e'});
        byte[] buffer = new byte[3];
        int bytesRead = IOUtils.readFully(input, buffer);
        Assert.assertEquals(3, bytesRead);
        byte[] expected = new byte[]{'a', 'b', 'c'};
        byte[] actual = new byte[3];
        System.arraycopy(buffer, 0, actual, 0, 3);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test toByteArray method
     */
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{'a', 'b', 'c'});
        byte[] expected = new byte[]{'a', 'b', 'c'};
        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test closeQuietly method
     */
    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            IOUtils.copy(new ByteArrayInputStream(new byte[0]), output);
            IOUtils.closeQuietly(output);
        } catch (IOException e) {
            // Ignore
        }
    }
}