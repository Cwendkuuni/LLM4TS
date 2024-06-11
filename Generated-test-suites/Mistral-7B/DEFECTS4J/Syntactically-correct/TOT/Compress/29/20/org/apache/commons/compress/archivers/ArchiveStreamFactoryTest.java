package org.apache.commons.compress.archivers.test;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.cpio.CpioEntry;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    private ByteArrayOutputStream outputStream;
    private InputStream inputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(new byte[0]);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * Test creating a tar archive output stream.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testCreateTarOutputStream() throws IOException {
        final ArchiveOutputStream tarOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertNotNull(tarOutputStream);
        final TarEntry entry = new TarEntry("test.txt");
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        final ArchiveInputStream tarInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.TAR,
                        new ByteArrayInputStream(outputStream.toByteArray()));
        assertNotNull(tarInputStream);
        final ArchiveEntry archiveEntry = tarInputStream.getNextEntry();
        assertNotNull(archiveEntry);
        assertEquals("test.txt", archiveEntry.getName());
        assertEquals(1, tarInputStream.getNumFiles());
        assertNull(tarInputStream.getNextEntry());
        IOUtils.closeQuietly(tarInputStream);
    }

    /**
     * Test creating a cpio archive output stream.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testCreateCpioOutputStream() throws IOException {
        final ArchiveOutputStream cpioOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertNotNull(cpioOutputStream);
        final CpioEntry entry = new CpioEntry("test.txt", "test data");
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("Hello World!".getBytes());
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();
        final ArchiveInputStream cpioInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.CPIO,
                        new ByteArrayInputStream(outputStream.toByteArray()));
        assertNotNull(cpioInputStream);
        assertNotNull(cpioInputStream.getNextEntry());
        assertEquals("test.txt", ((CpioEntry) cpioInputStream.getNextEntry()).getName());
        assertEquals("test data", ((CpioEntry) cpioInputStream.getNextEntry()).getComment());
        assertEquals(1, cpioInputStream.getNumFiles());
        assertNull(cpioInputStream.getNextEntry());
        IOUtils.closeQuietly(cpioInputStream);
    }

    /**
     * Test creating a ZIP archive output stream.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testCreateZipOutputStream() throws IOException {
        final ArchiveOutputStream zipOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(zipOutputStream);
        final ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        zipOutputStream.putNextEntry(entry);
        zipOutputStream.write("Hello World!".getBytes());
        zipOutputStream.closeArchiveEntry();
        zipOutputStream.finish();
        final ArchiveInputStream zipInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP,
                        new ByteArrayInputStream(outputStream.toByteArray()));
        assertNotNull(zipInputStream);
        assertNotNull(zipInputStream.getNextEntry());
        assertEquals("test.txt", ((ZipArchiveEntry) zipInputStream.getNextEntry()).getName());
        assertEquals(1, zipInputStream.getNumFiles());
        assertNull(zipInputStream.getNextEntry());
        IOUtils.closeQuietly(zipInputStream);
    }

    /**
     * Test creating an AR archive output stream.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testCreateArOutputStream() throws IOException {
        final ArchiveOutputStream arOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertNotNull(arOutputStream);
        final ArchiveEntry entry = new ArArchiveEntry("test.txt");
        arOutputStream.putNextEntry(entry);
        arOutputStream.write("Hello World!".getBytes());
        arOutputStream.closeArchiveEntry();
        arOutputStream.finish();
        final ArchiveInputStream arInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.AR,
                        new ByteArrayInputStream(outputStream.toByteArray()));
        assertNotNull(arInputStream);
        assertNotNull(arInputStream.getNextEntry());
        assertEquals("test.txt", ((ArArchiveEntry) arInputStream.getNextEntry()).getName());
        assertEquals(1, arInputStream.getNumFiles());
        assertNull(arInputStream.getNextEntry());
        IOUtils.closeQuietly(arInputStream);
    }

    /**
     * Test creating a JAR archive output stream.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testCreateJarOutputStream() throws IOException {
        final ArchiveOutputStream jarOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertNotNull(jarOutputStream);
        final JarArchiveEntry entry = new JarArchiveEntry("test.txt");
        jarOutputStream.putNextEntry(entry);
        jarOutputStream.write("Hello World!".getBytes());
        jarOutputStream.closeArchiveEntry();
        jarOutputStream.finish();
        final ArchiveInputStream jarInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.JAR,
                        new ByteArrayInputStream(outputStream.toByteArray()));
        assertNotNull(jarInputStream);
        assertNotNull(jarInputStream.getNextEntry());
        assertEquals("test.txt", ((JarArchiveEntry) jarInputStream.getNextEntry()).getName());
        assertEquals(1, jarInputStream.getNumFiles());
        assertNull(jarInputStream.getNextEntry());
        IOUtils.closeQuietly(jarInputStream);
    }

    /**
     * Test creating an ARJ archive output stream.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testCreateArjOutputStream() {
        // ARJ is not supported as an output stream
        assertThrows(UnsupportedOperationException.class, () -> {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ARJ, outputStream);
        });
    }

    /**
     * Test creating a SEVEN_Z archive output stream.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testCreateSevenZOutputStream() {
        // SEVEN_Z is not supported as an output stream
        assertThrows(UnsupportedOperationException.class, () -> {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, outputStream);
        });
    }

    /**
     * Test creating a dump archive input stream.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testCreateDumpInputStream() throws IOException {
        final byte[] dumpSignature = new byte[32];
        IOUtils.writeBytes(dumpSignature, 0, "magic1234567890123456789012345678901234".getBytes());
        final InputStream dumpInputStream = new ByteArrayInputStream(dumpSignature);
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        assertNotNull(archiveInputStream);
        assertNull(archiveInputStream.getNextEntry());
        IOUtils.closeQuietly(archiveInputStream);
    }

    /**
     * Test creating an archive input stream from a null input stream.
     */
    @Test
    public void testCreateArchiveInputStreamFromNullInputStream() {
        assertThrows(NullPointerException.class, () -> {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, null);
        });
    }

    /**
     * Test creating an archive output stream from a null output stream.
     */
    @Test
    public void testCreateArchiveOutputStreamFromNullOutputStream() {
        assertThrows(NullPointerException.class, () -> {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
        });
    }

    /**
     * Test creating an archive output stream with an unknown archive name.
     */
    @Test
    public void testCreateUnknownArchiveOutputStream() {
        assertThrows(ArchiveException.class, () -> {
            new ArchiveStreamFactory().createArchiveOutputStream("unknown", outputStream);
        });
    }
}