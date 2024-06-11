import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        // Test creating an ArchiveInputStream from a known archive file
        InputStream fileInputStream = new FileInputStream("test-archive.tar.gz");
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, fileInputStream);
        TarArchiveEntry entry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        archiveInputStream.close();
        IOUtils.closeQuietly(fileInputStream);

        // Test creating an ArchiveInputStream from a null InputStream
        InputStream nullInputStream = null;
        ArchiveInputStream nullArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, nullInputStream);
        Assert.assertNull(nullArchiveInputStream);

        // Test creating an ArchiveInputStream from an unsupported archive
        InputStream unsupportedInputStream = new ByteArrayInputStream("Invalid bytes".getBytes());
        ArchiveInputStream unsupportedArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("unsupported", unsupportedInputStream);
        Assert.assertNull(unsupportedArchiveInputStream);

        // Test creating an ArchiveInputStream from a null archiver name
        ArchiveInputStream nullArchiverNameInputStream = new ArchiveStreamFactory().createArchiveInputStream(null, fileInputStream);
        Assert.assertNull(nullArchiverNameInputStream);
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        // Test creating an ArchiveOutputStream to a known archive type
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        archiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        archiveOutputStream.write(new byte[] {'T', 'e', 's', 't', '.', 't', 'x', 't'}, 0, 8);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        byte[] archiveBytes = outputStream.toByteArray();
        TarArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(archiveBytes));
        TarArchiveEntry entry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        archiveInputStream.close();
        IOUtils.closeQuietly(outputStream);

        // Test creating an ArchiveOutputStream to a null OutputStream
        OutputStream nullOutputStream = null;
        ArchiveOutputStream nullArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, nullOutputStream);
        Assert.assertNull(nullArchiveOutputStream);

        // Test creating an ArchiveOutputStream to an unsupported archive type
        OutputStream unsupportedOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream unsupportedArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("unsupported", unsupportedOutputStream);
        Assert.assertNull(unsupportedArchiveOutputStream);

        // Test creating an ArchiveOutputStream to a null archiver name
        ArchiveOutputStream nullArchiverNameOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(null, outputStream);
        Assert.assertNull(nullArchiverNameOutputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws IOException, ArchiveException {
        // Test creating an ArchiveInputStream from archive bytes
        byte[] archiveBytes = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(archiveBytes));
        JarArchiveEntry entry = (JarArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidbytes() throws IOException {
        // Test creating an ArchiveInputStream with invalid bytes
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x03});
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, invalidInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnsupportedArchive() throws IOException {
        // Test creating an ArchiveInputStream with an unsupported archive
        InputStream unsupportedInputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05});
        new ArchiveStreamFactory().createArchiveInputStream("unsupported", unsupportedInputStream);
    }
}