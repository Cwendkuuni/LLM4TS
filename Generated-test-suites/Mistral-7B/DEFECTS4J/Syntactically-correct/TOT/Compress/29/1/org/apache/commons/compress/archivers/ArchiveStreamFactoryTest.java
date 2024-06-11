package org.apache.commons.compress.archivers.test;

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

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;

/**
 * Test class for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test createArchiveOutputStream() for AR format.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamAr() throws Exception {
        final String archiverName = ArchiveStreamFactory.AR;
        final OutputStream out = new ByteArrayOutputStream();
        try (ArchiveOutputStream archive = new ArchiveStreamFactory()
                .createArchiveOutputStream(archiverName, out)) {
            archive.putArchiveEntry(new ArchiveEntry("test.txt"));
            archive.write(("Hello World!").getBytes());
            archive.finish();
            archive.close();
            final InputStream in = new ByteArrayInputStream(out.toByteArray());
            try (ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                    .createArchiveInputStream(archiverName, in)) {
                assertFalse(archiveIn.hasNextEntry());
                archiveIn.close();
                final TarArchiveInputStream tarIn = new TarArchiveInputStream(in);
                final TarArchiveEntry entry = tarIn.getNextTarEntry();
                assertEquals("test.txt", entry.getName());
                assertEquals("test.txt", entry.getSize());
                assertEquals("text/plain", entry.getSizeExact());
                assertEquals(false, entry.isDirectory());
                assertEquals("Hello World!", IOUtils.toString(tarIn, StandardCharsets.UTF_8));
                tarIn.close();
            }
        }
    }

    /**
     * Test createArchiveInputStream() for AR format.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamAr() throws Exception {
        final String archiverName = ArchiveStreamFactory.AR;
        final String inputFileName = "test.ar";
        final byte[] expectedData = "Hello World!".getBytes();
        final byte[] arSignature = ArArchiveInputStream.createMagicSignature();

        try (OutputStream out = new FileOutputStream(inputFileName)) {
            final ArchiveOutputStream archive = new ArchiveStreamFactory()
                    .createArchiveOutputStream(archiverName, out);
            archive.putArchiveEntry(new ArchiveEntry("test.txt"));
            archive.write(expectedData, 0, expectedData.length);
            archive.finish();
            archive.close();
        }

        try (ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, new FileInputStream(inputFileName))) {
            final TarArchiveInputStream tarIn = new TarArchiveInputStream(archiveIn);
            final TarArchiveEntry entry = tarIn.getNextTarEntry();
            assertEquals("test.txt", entry.getName());
            assertEquals("test.txt", entry.getSize());
            assertEquals("test.txt", entry.getSizeExact());
            assertEquals(false, entry.isDirectory());
            assertEquals("Hello World!", IOUtils.toString(tarIn, StandardCharsets.UTF_8));
            tarIn.close();
        }

        try (ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, new ByteArrayInputStream(arSignature))) {
            assertThrows(ArchiveException.class, () -> archiveIn.getNextEntry());
            archiveIn.close();
        }

        try (ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, new ByteArrayInputStream(new byte[0]))) {
            assertThrows(ArchiveException.class, () -> archiveIn.getNextEntry());
            archiveIn.close();
        }

        try (ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, null)) {
            assertNull(archiveIn);
        }

        closeQuietly(new File(inputFileName).delete());
    }

    /**
     * Test createArchiveOutputStream() for TAR format.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamTar() throws Exception {
        final String archiverName = ArchiveStreamFactory.TAR;
        final OutputStream out = new ByteArrayOutputStream();
        try (ArchiveOutputStream archive = new ArchiveStreamFactory()
                .createArchiveOutputStream(archiverName, out)) {
            archive.putArchiveEntry(new ArchiveEntry("test.txt"));
            archive.write("Hello World!".getBytes());
            archive.finish();
            archive.close();
            final InputStream in = new ByteArrayInputStream(out.toByteArray());
            try (ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                    .createArchiveInputStream(archiverName, in)) {
                assertFalse(archiveIn.hasNextEntry());
                archiveIn.close();
            }
        }
    }

    /**
     * Test createArchiveInputStream() for TAR format.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamTar() throws Exception {
        final String archiverName = ArchiveStreamFactory.TAR;
        final String inputFileName = "test.tar";
        final byte[] expectedData = "Hello World!".getBytes();

        try (OutputStream out = new FileOutputStream(inputFileName)) {
            final ArchiveOutputStream archive = new ArchiveStreamFactory()
                    .createArchiveOutputStream(archiverName, out);
            archive.putArchiveEntry(new ArchiveEntry("test.txt"));
            archive.write(expectedData, 0, expectedData.length);
            archive.finish();
            archive.close();
        }

        try (ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, new FileInputStream(inputFileName))) {
            final TarArchiveInputStream tarIn = new TarArchiveInputStream(archiveIn);
            final TarArchiveEntry entry = tarIn.getNextTarEntry();
            assertEquals("test.txt", entry.getName());
            assertEquals("test.txt", entry.getSize());
            assertEquals("test.txt", entry.getSizeExact());
            assertEquals(false, entry.isDirectory());
            assertEquals("Hello World!", IOUtils.toString(tarIn, StandardCharsets.UTF_8));
            tarIn.close();
        }

        try (ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, null)) {
            assertNull(archiveIn);
        }

        closeQuietly(new File(inputFileName).delete());
    }

    /**
     * Test createArchiveInputStream() with an unknown archive format.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamUnknown() throws Exception {
        final String archiverName = "unknown";
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, input));
    }

    /**
     * Test createArchiveOutputStream() with an unknown archive format.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamUnknown() throws Exception {
        final String archiverName = "unknown";
        final OutputStream out = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveOutputStream(archiverName, out));
    }

    /**
     * Test createArchiveInputStream() with a null output stream.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamNull() throws Exception {
        final String archiverName = ArchiveStreamFactory.AR;
        assertThrows(NullPointerException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, null));
    }

    /**
     * Test createArchiveOutputStream() with a null input stream.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamNull() throws Exception {
        final String archiverName = ArchiveStreamFactory.AR;
        assertThrows(NullPointerException.class, () -> new ArchiveStreamFactory()
                .createArchiveOutputStream(archiverName, null));
    }

    /**
     * Test createArchiveInputStream() with an input stream without mark support.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamNoMark() throws Exception {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.AR, input));
    }
}