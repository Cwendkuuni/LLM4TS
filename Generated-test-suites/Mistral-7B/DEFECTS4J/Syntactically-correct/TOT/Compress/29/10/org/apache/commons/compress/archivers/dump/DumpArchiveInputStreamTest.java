package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DumpArchiveInputStreamTest {
    private static final String TEST_DATA = "testData.dump";
    private DumpArchiveInputStream inputStream;
    private ArchiveInputStream expectedInputStream;
    private ByteArrayOutputStream outputStream;
    private byte[] testDataBytes;

    @Before
    public void setUp() throws IOException {
        testDataBytes = getTestDataBytes();
        outputStream = new ByteArrayOutputStream();
        expectedInputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testDataBytes), null);
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testDataBytes), null);
    }

    @Test
    public void testNextEntry() throws IOException {
        DumpArchiveEntry actualEntry = null;
        DumpArchiveEntry expectedEntry = null;

        while ((actualEntry = inputStream.getNextEntry()) != null) {
            expectedEntry = expectedInputStream.getNextEntry();
            Assert.assertNotNull(actualEntry);
            Assert.assertNotNull(expectedEntry);
            Assert.assertEquals(expectedEntry.getName(), actualEntry.getName());
        }

        // Ensure that the end of the input stream has been reached
        Assert.assertNull(inputStream.getNextEntry());
        Assert.assertNull(expectedInputStream.getNextEntry());
    }

    @Test(expected = EOFException.class)
    public void testNextEntryAfterEOF() throws IOException {
        inputStream.close();
        inputStream.getNextEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int actualBytesRead = inputStream.read(buffer, 0, buffer.length);
        int expectedBytesRead = expectedInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(expectedBytesRead, actualBytesRead);
    }

    @Test(expected = IOException.class)
    public void testReadAfterEOF() throws IOException {
        inputStream.close();
        byte[] buffer = new byte[1];
        inputStream.read(buffer, 0, buffer.length);
    }

    @Test
    public void testClose() throws IOException {
        inputStream.close();
        expectedInputStream.close();
    }

    @Test
    public void testVerifyBytes() {
        Assert.assertTrue(DumpArchiveInputStream.matches(testDataBytes, testDataBytes.length));
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidData() throws IOException {
        ByteArrayInputStream invalidDataStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        try {
            new DumpArchiveInputStream(invalidDataStream, null);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    private byte[] getTestDataBytes() throws IOException {
        return IOUtils.toByteArray(getClass().getResourceAsStream(TEST_DATA));
    }
}