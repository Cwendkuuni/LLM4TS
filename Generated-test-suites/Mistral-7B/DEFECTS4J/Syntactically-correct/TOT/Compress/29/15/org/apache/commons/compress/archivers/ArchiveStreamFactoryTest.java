package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test case for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creating an archive input stream from an archive name and an input stream.
     */
    @org.junit.Test
    public void testCreateArchiveInputStream() throws IOException {
        final String archiveName = "zip";
        final InputStream inputStream = new ByteArrayInputStream(
                "PK\u0007\u0001\u0001\u0000local-header\u0001\u0002".getBytes());
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(archiveName, inputStream);
        assertNotNull(archiveInputStream);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream("unknown", inputStream));
    }

    /**
     * Test creating an archive output stream from an archiver name and an output stream.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStream() throws IOException {
        final String archiveName = "zip";
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(archiveName, outputStream);
        assertNotNull(archiveOutputStream);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveOutputStream("unknown", outputStream));
        archiveOutputStream.finish();
        assertThrows(IOException.class, () -> archiveOutputStream.putNextEntry(null));
    }

    /**
     * Test creating an archive input stream from an input stream, autodetecting the archive type.
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamFromStream() throws IOException {
        final InputStream inputStreamZip = new ByteArrayInputStream(
                "PK\u0007\u0001\u0001\u0000local-header\u0001\u0002".getBytes());
        final InputStream inputStreamTar = new ByteArrayInputStream(
                "00000000 54686973 20313231 00000300 706c6173 73796261 68726573 "
                        + "73747262 61637475 74000000 5472616e 73766572 0001 0000\u0000"
                        + "75746464 7373616e 6574776f 0001 0000\u0000"
                        + "00000001 00000014 54797065 63746172 73617363 61637475 74000001"
                        + "63616d65 6e73616d 0000\u0000"
                        + "00000002 00000014 54797065 63746172 73617363 61637475 74000001"
                        + "6b657901 0000\u0000\u0000".getBytes());
        final InputStream inputStreamUnknown = new ByteArrayInputStream(
                "unknown signature".getBytes());

        // Autodetection, ZIP
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(inputStreamZip);
        assertNotNull(archiveInputStream);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream(inputStreamTar));
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream(inputStreamUnknown));

        // Autodetection, TAR
        final ArchiveInputStream tarInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(inputStreamTar);
        assertNotNull(tarInputStream);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream(inputStreamZip));
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream(inputStreamUnknown));

        // Autodetection, autodetecting a TAR archive with a broken signature
        final ArchiveInputStream brokenTarInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(inputStreamTar);
        assertNotNull(brokenTarInputStream);
        assertThrows(IOException.class, () -> brokenTarInputStream.getNextEntry());
    }
}