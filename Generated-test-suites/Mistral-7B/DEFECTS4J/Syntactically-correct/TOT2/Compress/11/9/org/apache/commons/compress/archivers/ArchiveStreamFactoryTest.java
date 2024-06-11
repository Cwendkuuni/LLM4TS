import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.apache.commons.compress.streams.ByteArrayInputStream;
import org.apache.commons.compress.streams.ByteArrayOutputStream;
import org.apache.commons.compress.streams.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Test with valid arguments
        InputStream in = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        in = new ByteArrayInputStream(new byte[12]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        in = new ByteArrayInputStream(new byte[512]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        // Test with invalid arguments
        in = null;
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in));

        in = new ByteArrayInputStream(new byte[1024]);
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(null, in));

        // Test with unknown archive format
        in = new ByteArrayInputStream(new byte[12]);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream("unknown", in));
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test with valid arguments
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        out = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        // Test with invalid arguments
        out = null;
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out));

        out = new ByteArrayOutputStream();
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(null, out));

        // Test with unknown archive format
        out = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream("unknown", out));
    }

    @Test
    public void testCreateArchiveInputStreamFromByteArray() {
        // Test with valid arguments
        byte[] tarHeader = new byte[512];
        InputStream in = new ByteArrayInputStream(tarHeader);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertNotNull(archiveInputStream);
        TarArchiveEntry entry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertNotNull(entry);
        archiveInputStream.close();

        // Test with invalid arguments
        in = null;
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in));

        // Test with unknown archive format
        in = new ByteArrayInputStream(new byte[12]);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.UNKNOWN, in));
    }

    @Test
    public void testCreateArchiveOutputStreamFromByteArray() {
        // Test with valid arguments
        byte[] zipSignature = new byte[2];
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        archiveOutputStream.putArchiveEntry(new ZipArchiveEntry("test.txt"));
        IOUtils.copy(new ByteArrayInputStream(new byte[10]), archiveOutputStream);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        byte[] expected = new byte[12];
        System.arraycopy(zipSignature, 0, expected, 0, zipSignature.length);
        byte[] actual = out.toByteArray();
        assertArrayEquals(expected, actual);

        // Test with invalid arguments
        out = null;
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out));

        // Test with unknown archive format
        out = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.UNKNOWN, out));
    }
}