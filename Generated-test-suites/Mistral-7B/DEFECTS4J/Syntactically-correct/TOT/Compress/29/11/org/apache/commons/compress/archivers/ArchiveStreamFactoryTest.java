package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Tests for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creating an archive input stream for a known archive format from a name and an input stream.
     */
    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() throws Exception {
        final String archiveName = "tar";
        final InputStream archiveStream = new ByteArrayInputStream(new byte[] { 0x00, 0x00, // magic number
                (byte) TarArchiveEntry.STANDARD, 0x00, // header version
                0x00, 0x00, 0x00, 0x00, // header size
                (byte) 5, (byte) 5, // number of fields in the header
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // name length
                0x00, 0x00, // modification time
                0x00, 0x01, // file size
                0x00, 0x00 // file permissions
        });
        final ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, archiveStream);
        assertNotNull(inputStream);
        assertTrue(inputStream instanceof TarArchiveInputStream);
    }

    /**
     * Test creating an archive input stream for an unknown archive format from a name and an input stream.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromUnknownNameAndInputStream() throws Exception {
        final String archiveName = "unknown";
        final InputStream archiveStream = new ByteArrayInputStream(new byte[] { 0x00, 0x00, // magic number
                (byte) TarArchiveEntry.STANDARD, 0x00, // header version
                0x00, 0x00, 0x00, 0x00, // header size
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // name length
                0x00, 0x00, // modification time
                0x00, 0x01, // file size
                0x00, 0x00 // file permissions
        });
        new ArchiveStreamFactory().createArchiveInputStream(archiveName, archiveStream);
    }

    /**
     * Test creating an archive output stream for a known archive format from a name and an output stream.
     */
    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() throws Exception {
        final String archiveName = "tar";
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(archiveName, outputStream);
        assertNotNull(archiveOutputStream);
        assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
    }

    /**
     * Test creating an archive output stream for an unknown archive format from a name and an output stream.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromUnknownNameAndOutputStream() throws Exception {
        final String archiveName = "unknown";
        final OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, outputStream);
    }

    /**
     * Test creating an archive input stream for a known archive format from an input stream.
     */
    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        final InputStream archiveStream = new ByteArrayInputStream(new byte[] { 0x00, 0x00, // magic number
                (byte) TarArchiveEntry.STANDARD, 0x00, // header version
                0x00, 0x00, 0x00, 0x00, // header size
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // name length
                0x00, 0x00, // modification time
                0x00, 0x01, // file size
                0x00, 0x00 // file permissions
        });
        final ArchiveInputStream inputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(archiveStream);
        assertNotNull(inputStream);
        assertTrue(inputStream instanceof TarArchiveInputStream);
    }

    /**
     * Test creating an archive input stream for a known archive format from an input stream with
     * a custom entry encoding.
     */
    @Test
    public void testCreateArchiveInputStreamFromInputStreamWithCustomEncoding() throws Exception {
        final String encoding = "utf-8";
        final InputStream archiveStream = new ByteArrayInputStream(new byte[] { 0x00, 0x00, // magic number
                (byte) TarArchiveEntry.STANDARD, 0x00, // header version
                0x00, 0x00, 0x00, 0x00, // header size
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // name length
                0x00, 0x00, // modification time
                0x00, 0x01, // file size
                0x00, 0x00 // file permissions
        });
        final ArchiveInputStream inputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(TarArchiveInputStream.class.getName(), archiveStream, encoding);
        assertNotNull(inputStream);
        assertTrue(inputStream instanceof TarArchiveInputStream);
    }

    /**
     * Test creating an archive input stream for an unknown archive format from an input stream.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromUnknownFormatInputStream() throws Exception {
        final InputStream archiveStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02,
                0x03, 0x04, // not a magic number
                0x00, 0x00 // header version
        });
        new ArchiveStreamFactory().createArchiveInputStream(archiveStream);
    }

    /**
     * Test creating an archive input stream for a tar archive with a custom encoding from an input stream.
     */
    @Test
    public void testCreateTarArchiveInputStreamFromInputStreamWithCustomEncoding() throws Exception {
        final String encoding = "utf-8";
        final InputStream archiveStream = new ByteArrayInputStream(new byte[] { 0x00, 0x00, // magic number
                (byte) TarArchiveEntry.STANDARD, 0x00, // header version
                0x00, 0x00, 0x00, 0x00, // header size
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // name length
                0x00, 0x00, // modification time
                0x00, 0x01, // file size
                0x00, 0x00 // file permissions
        });
        final ArchiveInputStream inputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(TarArchiveInputStream.class.getName(), archiveStream, encoding);
        assertNotNull(inputStream);
        assertTrue(inputStream instanceof TarArchiveInputStream);
    }

    /**
     * Test creating an archive input stream with a custom encoding for a tar archive from a name and an input stream.
     */
    @Test
    public void testCreateTarArchiveInputStreamFromNameAndInputStreamWithCustomEncoding() throws Exception {
        final String encoding = "utf-8";
        final InputStream archiveStream = new ByteArrayInputStream(new byte[] { 0x00, 0x00, // magic number
                (byte) TarArchiveEntry.STANDARD, 0x00, // header version
                0x00, 0x00, 0x00, 0x00, // header size
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // name length
                0x00, 0x00, // modification time
                0x00, 0x01, // file size
                0x00, 0x00 // file permissions
        });
        final ArchiveInputStream inputStream = new ArchiveStreamFactory()
                .createArchiveInputStream("tar", archiveStream, encoding);
        assertNotNull(inputStream);
        assertTrue(inputStream instanceof TarArchiveInputStream);
    }

    /**
     * Test creating an archive output stream for a known archive format from a name and an output stream with a custom encoding.
     */
    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStreamWithCustomEncoding() throws Exception {
        final String encoding = "utf-8";
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream("tar", outputStream, encoding);
        assertNotNull(archiveOutputStream);
        assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
    }

    /**
     * Test creating an archive output stream for a known archive format from a name and an output stream.
     */
    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() throws Exception {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream("tar", outputStream);
        assertNotNull(archiveOutputStream);
        assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
    }
}