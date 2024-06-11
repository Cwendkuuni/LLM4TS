import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creating an archive input stream from a known archive name and an input stream.
     */
    @Test
    public void testCreateArchiveInputStreamWithKnownName() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[10]);
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
        assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());
    }

    /**
     * Test creating an archive output stream from a known archive name and an output stream.
     */
    @Test
    public void testCreateArchiveOutputStreamWithKnownName() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        assertEquals(ZipArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    /**
     * Test creating an archive input stream from an unknown archive name and an input stream.
     */
    @Test
    public void testCreateArchiveInputStreamWithUnknownName() throws IOException, ArchiveException {
        final InputStream input = new ByteArrayInputStream(new byte[10]);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream("unknown", input));
    }

    /**
     * Test creating an archive output stream from an unknown archive name and an output stream.
     */
    @Test
    public void testCreateArchiveOutputStreamWithUnknownName() throws IOException, ArchiveException {
        final OutputStream output = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveOutputStream("unknown", output));
    }

    /**
     * Test creating an archive input stream from an input stream without mark support.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithoutMarkSupport() throws IOException, ArchiveException {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[10]);
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
    }

    /**
     * Test creating an archive input stream from a null input stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws IOException, ArchiveException {
        final ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    /**
     * Test creating an archive output stream from a null output stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws IOException, ArchiveException {
        final ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    /**
     * Test creating an archive input stream from a null archiver name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws IOException, ArchiveException {
        final InputStream input = new ByteArrayInputStream(new byte[10]);
        new ArchiveStreamFactory().createArchiveInputStream(null, input);
    }

    /**
     * Test creating an archive output stream from a null archiver name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws IOException, ArchiveException {
        final OutputStream output = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(null, output);
    }

    /**
     * Test creating an archive input stream using autodetection.
     */
    @Test
    public void testCreateArchiveInputStreamWithAutodetection() throws IOException, ArchiveException {
        final InputStream input = new ByteArrayInputStream(new byte[1]);
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(input);
        // This will fail if the autodetection does not work properly
        assertEquals(JarArchiveInputStream.class, archiveInputStream.getClass());
    }

    /**
     * Test creating an archive output stream using autodetection.
     */
    @Test
    public void testCreateArchiveOutputStreamWithAutodetection() throws IOException, ArchiveException {
        final OutputStream output = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(output);
        // This will fail if the autodetection does not work properly
        assertEquals(ZipArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    /**
     * Test creating an archive input stream using autodetection with an unknown archive type.
     */
    @Test
    public void testCreateArchiveInputStreamWithUnknownArchiveType() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[1]);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream(input));
    }

    /**
     * Test creating an archive output stream using autodetection with an unknown archive type.
     */
    @Test
    public void testCreateArchiveOutputStreamWithUnknownArchiveType() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveOutputStream(output));
    }
}