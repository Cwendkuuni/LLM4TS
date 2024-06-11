package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertNull;
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
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test class for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test method for {@link ArchiveStreamFactory#ArchiveStreamFactory()}.
     */
    @Test
    public void testArchiveStreamFactoryDefaultConstruction() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        assertNull(factory.getEntryEncoding());
    }

    /**
     * Test method for {@link ArchiveStreamFactory#ArchiveStreamFactory(String)}.
     */
    @Test
    public void testArchiveStreamFactoryWithEncodingConstruction() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory("UTF-8");
        assertEquals("UTF-8", factory.getEntryEncoding());
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)}.
     */
    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Create a ZIP archive output stream
        ArchiveOutputStream zipArchive = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        zipArchive.putNextEntry(new JarArchiveEntry("test.txt"));
        zipArchive.write("Test content".getBytes());
        zipArchive.closeArchiveEntry();
        zipArchive.finish();
        IOUtils.closeQuietly(zipArchive);

        final ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream archive = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(archive.getNextEntry());
        assertEquals("test.txt", archive.getNextEntry().getName());
        IOUtils.closeQuietly(archive);

        // Create a TAR archive output stream
        out.reset();
        ArchiveOutputStream tarArchive = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        tarArchive.putNextEntry(new TarArchiveEntry("test.txt"));
        tarArchive.write("Test content".getBytes());
        tarArchive.closeArchiveEntry();
        tarArchive.finish();
        IOUtils.closeQuietly(tarArchive);

        in.mark(0);
        archive = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertNotNull(archive.getNextEntry());
        assertEquals("test.txt", archive.getNextEntry().getName());
        IOUtils.closeQuietly(archive);

        in.reset();
        assertThrows(ArchiveException.class, () -> {
            new ArchiveStreamFactory()
                    .createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, in);
        });
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(InputStream)}.
     */
    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Write a ZIP archive to an output stream
        ArchiveOutputStream zipArchive = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        zipArchive.putNextEntry(new JarArchiveEntry("test.txt"));
        zipArchive.write("Test content".getBytes());
        zipArchive.closeArchiveEntry();
        zipArchive.finish();
        IOUtils.closeQuietly(zipArchive);

        final InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream archive = new ArchiveStreamFactory()
                .createArchiveInputStream(inputStream);
        assertNotNull(archive.getNextEntry());
        assertEquals("test.txt", archive.getNextEntry().getName());
        IOUtils.closeQuietly(archive);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)}.
     */
    @Test
    public void testCreateArchiveInputStreamFromStringAndInputStream() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Write a ZIP archive to an output stream
        ArchiveOutputStream zipArchive = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        zipArchive.putNextEntry(new JarArchiveEntry("test.txt"));
        zipArchive.write("Test content".getBytes());
        zipArchive.closeArchiveEntry();
        zipArchive.finish();
        IOUtils.closeQuietly(zipArchive);

        final InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream archive = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(archive.getNextEntry());
        assertEquals("test.txt", archive.getNextEntry().getName());
        IOUtils.closeQuietly(archive);

        // Test with invalid archive name
        assertThrows(ArchiveException.class, () -> {
            new ArchiveStreamFactory()
                    .createArchiveInputStream("INVALID", inputStream);
        });
    }

}