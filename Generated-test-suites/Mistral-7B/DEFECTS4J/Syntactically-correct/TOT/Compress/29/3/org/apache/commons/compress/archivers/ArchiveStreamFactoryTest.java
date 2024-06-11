package org.apache.commons.compress.test.archivers;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Test class for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    private static final String TEST_DATA = "testData";
    private static final String TEST_DATA_ENTRY = "testDataEntry";
    private static final String TEST_DATA_CONTENT = "Test data.";
    private static final String TEST_OUTPUT = "testOutput.tar";
    private static final String TEST_INPUT = "testInput.tar";

    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setup() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    /**
     * Test creation of archive input stream from archive name and input stream.
     *
     * @throws IOException if an error occurs while creating or reading streams
     * @throws ArchiveException if an error occurs while creation the archive input stream
     */
    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        // Create archive output stream
        File testOutputFile = new File(TEST_OUTPUT);
        try (OutputStream out = new FileOutputStream(testOutputFile)) {
            ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            archiveOutputStream.putNextEntry(new TarArchiveEntry(TEST_DATA_ENTRY));
            archiveOutputStream.write(TEST_DATA_CONTENT.getBytes(StandardCharsets.UTF_8));
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
        }

        // Create archive input stream
        try (InputStream input = new FileInputStream(testOutputFile)) {
            ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, input);
            ArchiveEntry entry = archiveInputStream.getNextEntry();
            Assert.assertEquals(TEST_DATA_ENTRY, entry.getName());
            Assert.assertArrayEquals(TEST_DATA_CONTENT.getBytes(StandardCharsets.UTF_8), IOUtils.toByteArray(archiveInputStream));
            archiveInputStream.close();
        }
        testOutputFile.delete();
    }

    /**
     * Test creation of archive input stream from input stream and autodetection.
     *
     * @throws IOException if an error occurs while creating or reading streams
     * @throws ArchiveException if an error occurs while creation the archive input stream
     */
    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws IOException, ArchiveException {
        String testData = TEST_DATA_CONTENT;

        // Create input stream with test data
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        byteArrayInputStream.mark(512); // Mark the stream for signature check

        // Create archive input stream
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(byteArrayInputStream);
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertNotNull(tarArchiveEntry);
        Assert.assertEquals(TEST_DATA_ENTRY, tarArchiveEntry.getName());
        Assert.assertEquals("0" + TEST_DATA_CONTENT.length(), tarArchiveEntry.getSize());
        Assert.assertTrue(tarArchiveEntry.isCheckSumOK()); // Check the signature
        archiveInputStream.close();
    }
}