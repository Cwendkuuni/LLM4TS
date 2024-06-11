package org.apache.commons.compress.archivers.test;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Test class for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;
    private final static String TEST_FILE = "testFile.bin";
    private final static String TEST_ARCHIVE = "testArchive.tar";

    @Before
    public void setup() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStreamFromName() throws IOException, ArchiveException {
        // Test creating an archive input stream from a name
        InputStream input = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        TarArchiveInputStream tar = (TarArchiveInputStream) input;
        TarArchiveEntry entry = tar.getNextTarEntry();
        Assert.assertNull(entry);

        input.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws IOException, ArchiveException {
        // Test creating an archive input stream from an input stream
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        FileOutputStream output = new FileOutputStream(TEST_FILE);
        OutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(testData.length);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        output.close();
        FileInputStream fileInput = new FileInputStream(TEST_FILE);

        ArchiveInputStream archiveInput = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, fileInput);
        TarArchiveEntry archiveEntry = (TarArchiveEntry) archiveInput.getNextTarEntry();
        Assert.assertNotNull(archiveEntry);
        Assert.assertEquals("testFile.txt", archiveEntry.getName());

        archiveInput.close();
        fileInput.close();
        new File(TEST_FILE).delete();
    }

    @Test
    public void testCreateArchiveOutputStreamFromName() throws IOException {
        // Test creating an archive output stream from a name
        File outputFile = new File(TEST_ARCHIVE);
        ArchiveOutputStream archiveOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputFile);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        archiveOutput.putNextEntry(entry);
        archiveOutput.finish();
        archiveOutput.close();
        Assert.assertTrue(outputFile.exists());
        Assert.assertFalse(outputFile.isDirectory());
        new File(TEST_ARCHIVE).delete();
    }

    @Test
    public void testCreateArchiveOutputStreamFromOutputStream() throws IOException {
        // Test creating an archive output stream from an output stream
        File outputFile = new File(TEST_ARCHIVE);
        OutputStream outputStream = new FileOutputStream(outputFile);
        ArchiveOutputStream archiveOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        archiveOutput.putNextEntry(entry);
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        archiveOutput.write(testData, 0, testData.length);
        archiveOutput.closeArchiveEntry();
        archiveOutput.finish();
        archiveOutput.close();
        Assert.assertTrue(outputFile.exists());
        Assert.assertFalse(outputFile.isDirectory());
        new File(TEST_ARCHIVE).delete();
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidFormat() throws IOException, ArchiveException {
        // Test creating an archive input stream with an invalid format
        InputStream input = factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, new ByteArrayInputStream(new byte[0]));
        SevenZFile sevenZFile = new SevenZFile(input);
        Assert.assertThrows(StreamingNotSupportedException.class, sevenZFile::getNextEntry);
        input.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() {
        // Test creating an archive input stream with a null input stream
        Assert.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null));
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        // Test creating an archive output stream with a null output stream
        Assert.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null));
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidArchiveName() throws IOException, ArchiveException {
        // Test creating an archive input stream with an invalid archive name
        InputStream input = factory.createArchiveInputStream("invalidFormat", new ByteArrayInputStream(new byte[0]));
        Assert.assertThrows(ArchiveException.class, () -> input.getNextEntry());
        input.close();
    }

    @Test
    public void testCreateArchiveOutputStreamWithInvalidArchiveName() throws IOException {
        // Test creating an archive output stream with an invalid archive name
        OutputStream outputStream = new ByteArrayOutputStream();
        Assert.assertThrows(ArchiveException.class, () -> factory.createArchiveOutputStream("invalidFormat", outputStream));
    }

    @Test
    public void testCreateArchiveInputStreamWithEmptyInputStream() throws IOException, ArchiveException {
        // Test creating an archive input stream with an empty input stream
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInput = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, input);
        TarArchiveEntry entry = (TarArchiveEntry) archiveInput.getNextTarEntry();
        Assert.assertNull(entry);
        archiveInput.close();
    }

    @Test
    public void testCreateArchiveOutputStreamWithEmptyOutputStream() throws IOException {
        // Test creating an archive output stream with an empty output stream
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        archiveOutput.putNextEntry(entry);
        archiveOutput.closeArchiveEntry();
        archiveOutput.finish();
        archiveOutput.close();
        byte[] result = ((ByteArrayOutputStream) outputStream).toByteArray();
        Assert.assertEquals(0, result.length);
    }
}