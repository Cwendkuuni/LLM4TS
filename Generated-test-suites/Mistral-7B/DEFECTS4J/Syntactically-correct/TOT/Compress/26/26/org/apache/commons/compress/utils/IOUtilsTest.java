package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for IOUtils.
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        final String testData = "Test data for copy method.";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        long copiedBytes = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals("Incorrect number of bytes copied.", testData.length(), copiedBytes);

        byte[] actual = outputStream.toByteArray();
        byte[] expected = testData.getBytes();
        Assert.assertArrayEquals("Data not copied correctly.", expected, actual);
    }

    @Test
    public void testSkip() throws IOException {
        final String testData = "Test data for skip method.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        long numToSkip = 5;
        long skippedBytes = IOUtils.skip(inputStream, numToSkip);

        Assert.assertEquals("Incorrect number of bytes skipped.", numToSkip, skippedBytes);

        byte[] actual = outputStream.toByteArray();
        byte[] expected = testData.substring(5).getBytes();
        Assert.assertArrayEquals("Data not skipped correctly.", expected, actual);
    }

    @Test
    public void testReadFully() throws IOException {
        final String testData = "Test data for readFully method.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        // readFully with full input
        byte[] buffer = new byte[testData.length()];
        int readBytes = IOUtils.readFully(inputStream, buffer);
        Assert.assertEquals("Incorrect number of bytes read.", testData.length(), readBytes);
        Assert.assertArrayEquals("Data not read correctly.", testData.getBytes(), buffer);

        // readFully with partial input
        buffer = new byte[testData.length() / 2];
        readBytes = IOUtils.readFully(inputStream, buffer);
        Assert.assertArrayEquals("Data not read correctly (partial).", testData.substring(0, readBytes).getBytes(), buffer);
    }

    @Test
    public void testCopyWithError() throws IOException {
        final String testData = "Test data for copy method with error.";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        OutputStream errorStream = new ByteArrayOutputStream();

        try {
            IOUtils.copy(inputStream, errorStream);
            Assert.fail("Expected IOException.");
        } catch (IOException e) {
            // Expected
        }

        byte[] actualError = errorStream.toByteArray();
        Assert.assertEquals("Error stream should be empty.", 0, actualError.length);

        byte[] actual = outputStream.toByteArray();
        byte[] expected = testData.getBytes();
        Assert.assertArrayEquals("Data not copied correctly.", expected, actual);
    }

    @Test
    public void testSkipWithError() throws IOException {
        final String testData = "Test data for skip method with error.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        try {
            IOUtils.skip(inputStream, Long.MAX_VALUE);
            Assert.fail("Expected IOException.");
        } catch (IOException e) {
            // Expected
        }

        byte[] actual = outputStream.toByteArray();
        Assert.assertEquals("Output stream should be empty.", 0, actual.length);
    }

    @Test
    public void testReadFullyWithError() throws IOException {
        final String testData = "Test data for readFully method with error.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        try {
            IOUtils.readFully(inputStream, new byte[1000]);
            Assert.fail("Expected IOException.");
        } catch (IOException e) {
            // Expected
        }

        byte[] actual = outputStream.toByteArray();
        Assert.assertEquals("Output stream should be empty.", 0, actual.length);
    }
}