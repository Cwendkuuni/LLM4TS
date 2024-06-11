package org.apache.commons.compress.test. ArchiveStreamFactory;

import static org.apache.commons.compress.utils.IOUtils.closeQuietly;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveOutputStream; // Since it's not an output stream type, we won't test it here
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing ArchiveStreamFactory class
 */
public class ArchiveStreamFactoryTest {

    private InputStream testInput;
    private OutputStream testOutput;

    @Before
    public void setUp() throws Exception {
        testInput = new ByteArrayInputStream("Test Input".getBytes());
        testOutput = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws Exception {
        closeQuietly(testInput);
        closeQuietly(testOutput);
    }

    /**
     * Test createArchiveInputStream for AR format
     */
    @Test
    public void testCreateArchiveInputStreamAR() throws IOException {
        ArchiveInputStream archiveInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, testInput);
        assertEquals("AR format", archiveInput.getFormat(), ArchiveFormat.AR);
        assertFalse(archiveInput.hasNextEntry());
        archiveInput.close();
    }

    /**
     * Test createArchiveInputStream for CPIO format
     */
    @Test
    public void testCreateArchiveInputStreamCpio() throws IOException {
        InputStream cpioInput = new CpioArchiveInputStream(new ByteArrayInputStream("Test Input".getBytes()));
        ArchiveInputStream archiveInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInput);
        assertEquals("CPIO format", archiveInput.getFormat(), ArchiveFormat.CPIO);
        assertTrue(archiveInput.hasNextEntry());
        archiveInput.close();
        IOUtils.closeQuietly(cpioInput);
    }

    /**
     * Test createArchiveInputStream for dump format (not supported as an input stream type)
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamDumpInput() throws IOException {
        InputStream dumpInput = new DumpArchiveInputStream(new ByteArrayInputStream("Test Input".getBytes()));
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInput);
    }

    /**
     * Test createArchiveInputStream for JAR format
     */
    @Test
    public void testCreateArchiveInputStreamJar() throws IOException {
        ArchiveInputStream archiveInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, testInput);
        assertEquals("JAR format", archiveInput.getFormat(), ArchiveFormat.JAR);
        assertTrue(archiveInput.hasNextEntry());
        archiveInput.close();
    }

    /**
     * Test createArchiveInputStream for TAR format
     */
    @Test
    public void testCreateArchiveInputStreamTar() throws IOException {
        ArchiveInputStream archiveInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, testInput);
        assertEquals("TAR format", archiveInput.getFormat(), ArchiveFormat.TAR);
        assertTrue(archiveInput.hasNextEntry());
        archiveInput.close();
    }

    /**
     * Test createArchiveInputStream for ZIP format
     */
    @Test
    public void testCreateArchiveInputStreamZip() throws IOException {
        ArchiveInputStream archiveInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, testInput);
        assertEquals("ZIP format", archiveInput.getFormat(), ArchiveFormat.ZIP);
        assertTrue(archiveInput.hasNextEntry());
        archiveInput.close();
    }

    /**
     * Test createArchiveOutputStream for AR format
     */
    @Test
    public void testCreateArchiveOutputStreamAR() throws IOException {
        ArchiveOutputStream archiveOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, testOutput);
        assertNull(archiveOutput.getNextArchiveEntry());
        archiveOutput.close();
    }

    /**
     * Test createArchiveOutputStream for CPIO format
     */
    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveOutputStreamCpio() throws IOException {
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, testOutput);
    }

    /**
     * Test createArchiveOutputStream for dump format
     */
    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveOutputStreamDump() throws IOException {
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, testOutput);
    }

    /**
     * Test createArchiveOutputStream for JAR format
     */
    @Test
    public void testCreateArchiveOutputStreamJar() throws IOException {
        ArchiveOutputStream archiveOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, testOutput);
        archiveOutput.putNextEntry(new JarEntry("TestEntry.txt"));
        testOutput.write("Test Output".getBytes());
        archiveOutput.closeArchiveEntry();
        archiveOutput.finish();
        archiveOutput.close();
        String result = new String(testOutput.toByteArray());
        assertEquals("Test Output", result);
    }

    /**
     * Test createArchiveOutputStream for TAR format
     */
    @Test
    public void testCreateArchiveOutputStreamTar() throws IOException {
        ArchiveOutputStream archiveOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, testOutput);
        archiveOutput.putNextEntry(new TarEntry("TestEntry.txt"));
        testOutput.write("Test Output".getBytes());
        archiveOutput.closeArchiveEntry();
        archiveOutput.finish();
        archiveOutput.close();
        String result = new String(testOutput.toByteArray());
        assertEquals("Test Output", result);
    }

    /**
     * Test createArchiveOutputStream for zip format
     */
    @Test
    public void testCreateArchiveOutputStreamZip() throws IOException {
        ArchiveOutputStream archiveOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, testOutput);
        archiveOutput.putNextEntry(new ZipEntry("TestEntry.txt"));
        testOutput.write("Test Output".getBytes());
        archiveOutput.closeArchiveEntry();
        archiveOutput.finish();
        archiveOutput.close();
        String result = new String(testOutput.toByteArray());
        assertEquals("Test Output", result);
    }

    /**
     * Test createArchiveInputStream for 7z format (not supported as an input stream type)
     */
    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveInputStreamSevenZ() throws IOException {
        InputStream sevenzInput = new SevenZFile("Test SevenZ Input.7z");
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, sevenzInput);
    }

    /**
     * Test createArchiveOutputStream for 7z format (not supported as an output stream type)
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateArchiveOutputStreamSevenZ() throws IOException {
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, testOutput);
    }

    /**
     * Test createArchiveInputStream for unknown format
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownFormat() throws IOException {
        InputStream unknownFormatInput = new ByteArrayInputStream("Test Input".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream("UnknownFormat", unknownFormatInput);
    }

    /**
     * Test createArchiveOutputStream for unknown format
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownFormat() throws IOException {
        OutputStream unknownFormatOutput = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("UnknownFormat", unknownFormatOutput);
    }
}