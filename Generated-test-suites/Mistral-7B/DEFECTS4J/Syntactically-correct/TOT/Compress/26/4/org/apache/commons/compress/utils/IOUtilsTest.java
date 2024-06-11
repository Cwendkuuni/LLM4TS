package org.apache.commons.compress.utils.test;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class IOUtilsTest {

    private InputStream input;
    private OutputStream output;

    @Before
    public void setUp() throws IOException {
        input = new ByteArrayInputStream("Test data".getBytes(StandardCharsets.UTF_8));
        output = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }

    @Test
    public void testCopy() throws IOException {
        long copied = IOUtils.copy(input, output);
        byte[] expected = "Test data".getBytes(StandardCharsets.UTF_8);
        byte[] actual = output.toByteArray();

        Assert.assertEquals("Unexpected number of bytes copied", expected.length, copied);
        Assert.assertArrayEquals("Unexpected data copied", expected, actual);
    }

    @Test(expected = IOException.class)
    public void testCopyWithNullOutput() throws IOException {
        IOUtils.copy(input, null);
    }

    @Test(expected = IOException.class)
    public void testCopyWithNullInput() throws IOException {
        IOUtils.copy(null, output);
    }

    @Test
    public void testSkip() throws IOException {
        long numToSkip = 6;
        long skipped = IOUtils.skip(input, numToSkip);

        byte[] expected = "Test dat".getBytes(StandardCharsets.UTF_8);
        byte[] actual = new byte[input.available()];
        input.read(actual);

        Assert.assertArrayEquals("Unexpected data remaining in the input after skipping", expected, actual);
        Assert.assertEquals("Unexpected number of bytes skipped", numToSkip - (expected.length), skipped);
    }

    @Test(expected = IOException.class)
    public void testSkipWithNullInputStream() throws IOException {
        IOUtils.skip(null, 0);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] expected = "Test data".getBytes(StandardCharsets.UTF_8);
        byte[] actual = new byte[expected.length];

        IOUtils.readFully(input, actual);

        Assert.assertArrayEquals("Unexpected data read", expected, actual);
    }

    @Test(expected = IOException.class)
    public void testReadFullyWithNullInputStream() throws IOException {
        IOUtils.readFully(null, new byte[0]);
    }

    @Test
    public void testReadFullyWithNullOutput() throws IOException {
        byte[] actual = new byte[10];
        IOUtils.readFully(input, null);

        Assert.assertArrayEquals("Unexpected data read", actual, new byte[0]);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] expected = "Test data".getBytes(StandardCharsets.UTF_8);
        byte[] actual = IOUtils.toByteArray(input);

        Assert.assertArrayEquals("Unexpected data in byte array", expected, actual);
    }

    @Test(expected = IOException.class)
    public void testToByteArrayWithNullInputStream() throws IOException {
        IOUtils.toByteArray(null);
    }
}