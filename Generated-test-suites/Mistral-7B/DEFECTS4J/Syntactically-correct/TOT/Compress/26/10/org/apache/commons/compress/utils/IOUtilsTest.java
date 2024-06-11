package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output);
        Assert.assertEquals("Incorrect byte count copied", 13, actual);

        byte[] expected = "Test Data".getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = output.toByteArray();
        Assert.assertArrayEquals("Data not copied correctly", expected, actualBytes);
    }

    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output, 5);
        Assert.fail("Expected an IOException with custom buffer size");
    }

    @Test(expected = IOException.class)
    public void testCopyWithNullInputs() throws IOException {
        InputStream input = null;
        OutputStream output = new ByteArrayOutputStream();

        IOUtils.copy(input, output);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));

        long actual = IOUtils.skip(input, 10);
        Assert.assertEquals("Incorrect number of bytes skipped", 10, actual);

        long remaining = 3;
        long actualRemaining = IOUtils.skip(input, remaining);
        Assert.assertEquals("Unexpected end of stream reached", remaining, actualRemaining);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));
        byte[] buffer = new byte[10];

        int actual = IOUtils.readFully(input, buffer);
        Assert.assertEquals("Incorrect number of bytes read", 13, actual);

        byte[] expected = "Test Data".getBytes(StandardCharsets.UTF_8);
        Assert.assertArrayEquals("Data not read correctly", expected, buffer, 0, 13);
    }

    @Test(expected = IOException.class)
    public void testReadFullyWithNullInputStream() throws IOException {
        byte[] buffer = new byte[10];
        InputStream input = null;

        IOUtils.readFully(input, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));
        byte[] expected = "Test Data".getBytes(StandardCharsets.UTF_8);

        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals("Data not converted to byte array correctly", expected, actual);
    }

    @Test(expected = IOException.class)
    public void testToByteArrayWithNullInputStream() throws IOException {
        InputStream input = null;

        byte[] actual = IOUtils.toByteArray(input);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            IOUtils.copy(new ByteArrayInputStream(new byte[10]), output);
            IOUtils.closeQuietly(output);
        } finally {
            Assert.assertFalse("Expected stream to be closed", output.isOpen());
        }
    }
}