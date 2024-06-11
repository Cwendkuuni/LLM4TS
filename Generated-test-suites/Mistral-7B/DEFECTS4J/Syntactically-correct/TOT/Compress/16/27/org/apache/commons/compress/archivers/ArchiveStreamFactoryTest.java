import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.apache.commons.compress.archivers.ArchiveStreamFactory.AR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.CPIO;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.DUMP;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.JAR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.TAR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.ZIP;

/**
 * Test class for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException, IOException {
        // Given
        String archiveName = JAR;
        InputStream inputStream = new ByteArrayInputStream("Jar archive bytes".getBytes());

        // When
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, inputStream);

        // Then
        Assert.assertNotNull(archiveInputStream);
        Assert.assertEquals(archiveName, archiveInputStream.getFormat().getName());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
        // Given
        String archiveName = TAR;
        OutputStream outputStream = new ByteArrayOutputStream();

        // When
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, outputStream);

        // Then
        Assert.assertNotNull(archiveOutputStream);
        Assert.assertEquals(archiveName, archiveOutputStream.getFormat().getName());
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testUnknownArchiveFormat() throws ArchiveException, IOException {
        // Given
        String archiveName = "unknown";
        InputStream inputStream = new ByteArrayInputStream("Archive bytes".getBytes());

        // When
        new ArchiveStreamFactory().createArchiveInputStream(archiveName, inputStream);

        // Then - an exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArchiverName() {
        // Given
        String archiveName = null;
        InputStream inputStream = new ByteArrayInputStream("Archive bytes".getBytes());

        // When and Then - an IllegalArgumentException should be thrown
        new ArchiveStreamFactory().createArchiveInputStream(archiveName, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputStream() {
        // Given
        String archiveName = JAR;
        InputStream inputStream = null;

        // When and Then - an IllegalArgumentException should be thrown
        new ArchiveStreamFactory().createArchiveInputStream(archiveName, inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws ArchiveException, IOException {
        // Given
        String archiveName = ZIP;
        byte[] archiveBytes = new byte[]{(byte) 0x50, (byte) 0x4b, (byte) 0x05, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x4b, (byte) 0x50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // When
        InputStream inputStream = new ByteArrayInputStream(archiveBytes);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, inputStream);

        // Then
        Assert.assertNotNull(archiveInputStream);
        Assert.assertEquals(archiveName, archiveInputStream.getFormat().getName());
        archiveInputStream.close();
    }

    @Test
    public void testCreateJarArchiveOutputStream() throws ArchiveException, IOException {
        // Given
        String archiveName = JAR;
        OutputStream outputStream = new ByteArrayOutputStream();

        // When
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, outputStream);
        archiveOutputStream.putNextEntry(new JarArchiveEntry("test.txt"));
        archiveOutputStream.write("Test content".getBytes());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        // Then
        Assert.assertNotNull(archiveOutputStream);
        Assert.assertEquals(JAR, archiveOutputStream.getFormat().getName());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(archiveOutputStream.getBuffer());
        JarArchiveInputStream jarArchiveInputStream = new JarArchiveInputStream(inputStream);
        Assert.assertNotNull(jarArchiveInputStream.getNextEntry());
        Assert.assertEquals("test.txt", jarArchiveInputStream.getNextEntry().getName());
        Assert.assertEquals("Test content", new String(jarArchiveInputStream.readAllBytes()));
        jarArchiveInputStream.close();
        inputStream.close();
    }
}