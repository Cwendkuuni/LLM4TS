package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtils Test Cases
 */
public class IOUtilsTest {

    private InputStream input;
    private OutputStream output;
    private ByteArrayOutputStream actualOutput;

    /**
     * Sets up test environment
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        input = new ByteArrayInputStream(new byte[0]);
        output = new ByteArrayOutputStream();
        actualOutput = new ByteArrayOutputStream();
    }

    /**
     * Cleans up test environment
     * @throws IOException
     */
    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
        IOUtils.closeQuietly(actualOutput);
    }

    /**
     * Test copy method with empty InputStream and OutputStream.
     * @throws IOException
     */
    @Test
    public void testCopyEmptyStreams() throws IOException {
        long actual = IOUtils.copy(input, output);
        Assert.assertEquals(0, actual);
    }

    /**
     * Test copy method with InputStream containing data and OutputStream.
     * @throws IOException
     */
    @Test
    public void testCopyDataStreams() throws IOException {
        byte[] testData = new byte[] {1, 2, 3, 4};
        input = new ByteArrayInputStream(testData);
        output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output);
        byte[] expected = IOUtils.toByteArray(input);

        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, output.toByteArray());
    }

    /**
     * Test copy method with InputStream containing data and Null OutputStream.
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testCopyNullOutputStream() throws IOException {
        byte[] testData = new byte[] {1, 2, 3, 4};
        input = new ByteArrayInputStream(testData);

        IOUtils.copy(input, null);
    }

    /**
     * Test skip method with InputStream containing data and given number to skip.
     * @throws IOException
     */
    @Test
    public void testSkipDataStream() throws IOException {
        byte[] testData = new byte[] {1, 2, 3, 4};
        input = new ByteArrayInputStream(testData);

        long numToSkip = 2;
        long actual = IOUtils.skip(input, numToSkip);
        byte[] expected = new byte[] {3, 4};

        Assert.assertEquals(expected.length, input.available());
        Assert.assertArrayEquals(expected, new byte[expected.length]);
    }

    /**
     * Test skip method with InputStream containing less data than to skip.
     * @throws IOException
     */
    @Test
    public void testSkipLessDataStream() throws IOException {
        byte[] testData = new byte[] {1, 2};
        input = new ByteArrayInputStream(testData);

        long numToSkip = 3;
        long actual = IOUtils.skip(input, numToSkip);
        byte[] expected = new byte[]{1};

        Assert.assertEquals(expected.length, input.available());
        Assert.assertArrayEquals(expected, new byte[expected.length]);
    }

    /**
     * Test skip method with Null InputStream.
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testSkipNullInputStream() throws IOException {
        IOUtils.skip(null, 0);
    }

    /**
     * Test readFully method with InputStream containing less data than requested.
     * @throws IOException
     */
    @Test
    public void testReadFullyLessData() throws IOException {
        byte[] testData = new byte[] {1};
        input = new ByteArrayInputStream(testData);
        byte[] buffer = new byte[3];

        int actual = IOUtils.readFully(input, buffer);
        byte[] expected = new byte[] {1};

        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, buffer);
    }

    /**
     * Test readFully method with InputStream containing enough data.
     * @throws IOException
     */
    @Test
    public void testReadFullyEnoughData() throws IOException {
        byte[] testData = new byte[] {1, 2, 3};
        input = new ByteArrayInputStream(testData);
        byte[] buffer = new byte[3];

        int actual = IOUtils.readFully(input, buffer);
        byte[] expected = testData;

        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, buffer);
    }

    /**
     * Test readFully method with Null InputStream.
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testReadFullyNullInputStream() throws IOException {
        byte[] buffer = new byte[3];
        IOUtils.readFully(null, buffer);
    }

    /**
     * Test copy method to test copying data from a ByteArrayInputStream to a ByteArrayOutputStream.
     * @throws IOException
     */
    @Test
    public void testCopyByteArrayStreams() throws IOException {
        byte[] testData = new byte[] {1, 2, 3, 4};
        byte[] expected = new byte[] {1, 2, 3, 4};

        input = new ByteArrayInputStream(testData);
        output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output);
        byte[] actualData = IOUtils.toByteArray(output);

        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, actualData);
    }

    /**
     * Test copy method to test copying data from a InputStream to a ByteArrayOutputStream.
     * @throws IOException
     */
    @Test
    public void testCopyInputStreamByteArrayOutputStream() throws IOException {
        byte[] testData = new byte[] {1, 2, 3, 4};
        byte[] expected = new byte[] {1, 2, 3, 4};

        input = new ByteArrayInputStream(testData);
        output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output);
        byte[] actualData = IOUtils.toByteArray(output);

        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, actualData);
    }

    /**
     * Test copy method to test copying data from a ByteArrayOutputStream to a OutputStream.
     * @throws IOException
     */
    @Test
    public void testCopyByteArrayOutputStreamOutputStream() throws IOException {
        byte[] testData = new byte[] {1, 2, 3, 4};
        byte[] expected = new byte[] {1, 2, 3, 4};

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(testData, output.getOutputStream());

        byte[] actualData = output.toByteArray();

        Assert.assertArrayEquals(expected, actualData);
    }

    /**
     * Test skip method to test skipping data from a InputStream.
     * @throws IOException
     */
    @Test
    public void testSkipInputStream() throws IOException {
        byte[] testData = new byte[] {1, 2, 3, 4};
        byte[] expected = new byte[] {3, 4};

        input = new ByteArrayInputStream(testData);

        long numToSkip = 2;
        long actual = IOUtils.skip(input, numToSkip);

        byte[] actualData = new byte[expected.length];
        IOUtils.readFully(input, actualData);

        Assert.assertArrayEquals(expected, actualData);
    }

    /**
     * Test skip method to test skipping data from a ByteArrayInputStream.
     * @throws IOException
     */
    @Test
    public void testSkipByteArrayInputStream() throws IOException {
        byte[] testData = new byte[] {1, 2, 3, 4};
        byte[] expected = new byte[] {3, 4};

        input = new ByteArrayInputStream(testData);
        long numToSkip = 2;

        long actual = IOUtils.skip(input, numToSkip);

        byte[] actualData = new byte[expected.length];
        IOUtils.readFully(input, actualData);

        Assert.assertArrayEquals(expected, actualData);
    }

    /**
     * Test skip method to test skipping data from a ByteArrayOutputStream.
     * @throws IOException
     */
    @Test
    public void testSkipByteArrayOutputStream() throws IOException {
        byte[] testData = new byte[] {1, 2, 3, 4};
        byte[] expected = new byte[] {3, 4};

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(testData, output.getOutputStream());

        long numToSkip = 2;
        long actual = IOUtils.skip(output.getInputStream(), numToSkip);

        byte[] actualData = new byte[expected.length];
        IOUtils.readFully(output.getInputStream(), actualData);

        Assert.assertArrayEquals(expected, actualData);
    }
}