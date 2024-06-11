package org.apache.commons.compress.test.archivers;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;

/**
 * Tests for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test createArchiveOutputStream() with AR.
     */
    @Test
    public void testCreateArchiveOutputStreamAR() throws IOException {
        final File tempFile = File.createTempFile("test", "ar");
        InputStream input = new ByteArrayInputStream("Test data".getBytes());
        OutputStream output = new FileOutputStream(tempFile);
        ArchiveOutputStream archiveOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        archiveOutput.putArchiveEntry(new ArchiveEntry("test.txt") {
            @Override
            public String getName() {
                return "test.txt";
            }
        });
        archiveOutput.putStream(input);
        archiveOutput.closeArchiveEntry();
        archiveOutput.finish();
        archiveOutput.close();
        ArchiveInputStream archiveInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new FileInputStream(tempFile));
        ArchiveEntry entry = archiveInput.getNextEntry();
        assertEquals("test.txt", entry.getName());
        assertTrue(entry.isDirectory());
        input = new ByteArrayInputStream("Test data".getBytes());
        assertEquals("Test data", IOUtils.toString(archiveInput, IOUtils.DEFAULT_BUFFER_SIZE));
        archiveInput.close();
        tempFile.delete();
    }

    /**
     * Test createArchiveOutputStream() with JAR.
     */
    @Test
    public void testCreateArchiveOutputStreamJAR() throws IOException {
        final File tempFile = File.createTempFile("test", "jar");
        OutputStream output = new FileOutputStream(tempFile);
        ArchiveOutputStream archiveOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
        archiveOutput.putNextEntry(new JarArchiveEntry("test.txt"));
        IOUtils.copy(new ByteArrayInputStream("Test data".getBytes()), archiveOutput);
        archiveOutput.finish();
        archiveOutput.close();
        ArchiveInputStream archiveInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, new FileInputStream(tempFile));
        ArchiveEntry entry = archiveInput.getNextEntry();
        assertEquals("test.txt", entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals("Test data", IOUtils.toString(archiveInput, IOUtils.DEFAULT_BUFFER_SIZE));
        archiveInput.close();
        tempFile.delete();
    }

    /**
     * Test createArchiveInputStream() with AR.
     */
    @Test
    public void testCreateArchiveInputStreamAR() throws IOException {
        final File testFile = new File("test.ar");
        OutputStream output = new FileOutputStream(testFile);
        ArchiveOutputStream archiveOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        archiveOutput.putArchiveEntry(new ArchiveEntry("test.txt") {
            @Override
            public String getName() {
                return "test.txt";
            }
        });
        archiveOutput.putStream(new ByteArrayInputStream("Test data".getBytes()));
        archiveOutput.finish();
        archiveOutput.close();

        InputStream input = new FileInputStream(testFile);
        ArchiveInputStream archiveInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, input);
        ArchiveEntry entry = archiveInput.getNextEntry();
        assertEquals("test.txt", entry.getName());
        assertTrue(entry.isDirectory());
        assertEquals("Test data", IOUtils.toString(archiveInput, IOUtils.DEFAULT_BUFFER_SIZE));
        archiveInput.close();
        testFile.delete();
    }

    /**
     * Test createArchiveInputStream() with JAR.
     */
    @Test
    public void testCreateArchiveInputStreamJAR() throws IOException {
        final File tempFile = File.createTempFile("test", "jar");
        ArchiveOutputStream archiveOutput = new JarArchiveOutputStream(new FileOutputStream(tempFile), null);
        archiveOutput.putNextEntry(new JarArchiveEntry("test.txt"));
        archiveOutput.putStream(new ByteArrayInputStream("Test data".getBytes()));
        archiveOutput.finish();
        archiveOutput.close();

        InputStream input = new FileInputStream(tempFile);
        ArchiveInputStream archiveInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, input);
        ArchiveEntry entry = archiveInput.getNextEntry();
        assertEquals("test.txt", entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals("Test data", IOUtils.toString(archiveInput, IOUtils.DEFAULT_BUFFER_SIZE));
        archiveInput.close();
        tempFile.delete();
    }

    /**
     * Test createArchiveInputStream() with unknown format.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownFormat() throws IOException {
        final File testFile = new File("test.unknownformat");
        OutputStream output = new FileOutputStream(testFile);
        ArchiveOutputStream archiveOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.UNKNOWN_FORMAT, output);
        // Test creation fails
        assertNull(archiveOutput);

        InputStream input = new FileInputStream(testFile);
        ArchiveInputStream archiveInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.UNKNOWN_FORMAT, input);
        // Test reading also fails
        archiveInput.close();
        testFile.delete();
    }

}