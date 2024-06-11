package org.apache.commons.compress.utils.test;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for IOUtils
 */
public class IOUtilsTest {

    /**
     * Test copy method with valid InputStream and OutputStream.
     */
    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'T', 'e', 'st'});
        long copiedBytes = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals(3, copiedBytes);
        byte[] result = outputStream.toByteArray();
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, result);
    }

    /**
     * Test copy method with null InputStream and OutputStream.
     */
    @Test(expected = NullPointerException.class)
    public void testCopyNullInputOrOutput() throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream);
    }

    /**
     * Test copy method with InputStream that returns -1 on read.
     */
    @Test
    public void testCopyEndOfStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        inputStream.mark(1);
        long copiedBytes = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals(3, copiedBytes);
        inputStream.reset();
        long copiedBytes2 = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals(0, copiedBytes2);
    }

    /**
     * Test skip method with valid InputStream and number to skip.
     */
    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        long skippedBytes = IOUtils.skip(inputStream, 2);
        Assert.assertEquals(1, skippedBytes);
        long skippedBytes2 = IOUtils.skip(inputStream, 1);
        Assert.assertEquals(0, skippedBytes2);
    }

    /**
     * Test skip method with null InputStream.
     */
    @Test(expected = NullPointerException.class)
    public void testSkipNullInputStream() throws IOException {
        InputStream inputStream = null;
        long numToSkip = 1;
        IOUtils.skip(inputStream, numToSkip);
    }

    /**
     * Test skip method with InputStream that returns -1 on skip.
     */
    @Test
    public void testSkipEndOfStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        long skippedBytes = IOUtils.skip(inputStream, 3);
        Assert.assertEquals(3, skippedBytes);
        long skippedBytes2 = IOUtils.skip(inputStream, 1);
        Assert.assertEquals(0, skippedBytes2);
    }

    /**
     * Test readFully method with valid InputStream and byte array.
     */
    @Test
    public void testReadFully() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        byte[] result = new byte[3];
        IOUtils.readFully(inputStream, result);
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, result);
    }

    /**
     * Test readFully method with null InputStream.
     */
    @Test(expected = NullPointerException.class)
    public void testReadFullyNullInputStream() throws IOException {
        InputStream inputStream = null;
        byte[] result = new byte[3];
        IOUtils.readFully(inputStream, result);
    }

    /**
     * Test readFully method with InputStream that returns -1 on read.
     */
    @Test
    public void testReadFullyEndOfStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        byte[] result = new byte[4];
        IOUtils.readFully(inputStream, result);
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, result, 0, 3);
    }

    /**
     * Test toByteArray method with valid InputStream.
     */
    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        byte[] result = IOUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, result);
    }

    /**
     * Test toByteArray method with null InputStream.
     */
    @Test(expected = NullPointerException.class)
    public void testToByteArrayNullInputStream() throws IOException {
        InputStream inputStream = null;
        IOUtils.toByteArray(inputStream);
    }

    /**
     * Test closeQuietly method with valid Closeable.
     */
    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.closeQuietly(outputStream);
    }

    /**
     * Test closeQuietly method with null Closeable.
     */
    @Test
    public void testCloseQuietlyNullCloseable() {
        IOUtils.closeQuietly(null);
    }
}