import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ARArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ARArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Test for {@link ArchiveStreamFactory} class
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        final String archiveType = "zip";
        final OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveType, out);
        assertNotNull(archiveOutputStream);
        assertEquals(archiveType, archiveOutputStream.getFormat());
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        final byte[] zipSignature = new byte[0]; // Empty byte array for ZIP signature
        final InputStream in = new ByteArrayInputStream(zipSignature);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertFalse(archiveInputStream.hasNextEntry());
        assertThrows(ArchiveException.class, () -> archiveInputStream.getNextEntry());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromAr() throws IOException, ArchiveException {
        final String archiveType = "ar";
        final byte[] arSignature = new byte[]{"!", "archive\0", "1.0\0", "Sun Nov 17 17:32:53 2002\0", "AR\0"};
        final InputStream inputStream = new ByteArrayInputStream(arSignature);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveType, inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(archiveType, archiveInputStream.getFormat());
        assertNotNull(archiveInputStream.getNextEntry());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromCpio() throws IOException, ArchiveException {
        final String archiveType = "cpio";
        final byte[] cpioSignature = new byte[]{"070707", "1234567890ABCDEF", "0123456789ABCDEF\0"};
        final InputStream inputStream = new ByteArrayInputStream(cpioSignature);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveType, inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(archiveType, archiveInputStream.getFormat());
        assertNotNull(archiveInputStream.getNextEntry());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromUnknown() throws IOException, ArchiveException {
        final String archiveType = "unknown";
        final InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveType, in);
        assertFalse(archiveInputStream.hasNextEntry());
        assertThrows(ArchiveException.class, () -> archiveInputStream.getNextEntry());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamAndArchiveInputstream() throws IOException, ArchiveException {
        final String archiveType = "zip";
        final OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveType, out);
        byte[] testData = "Test data".getBytes();
        archiveOutputStream.putArchiveEntry(new JarArchiveEntry("test.txt"));
        archiveOutputStream.write(testData, 0, testData.length);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        final InputStream inputStream = new ByteArrayInputStream(archiveOutputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveType, inputStream);
        JarArchiveEntry jarArchiveEntry = (JarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", jarArchiveEntry.getName());
        assertEquals(testData.length, archiveInputStream.read(new byte[testData.length]));
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromDump() throws IOException, ArchiveException {
        final String archiveType = "dump";
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveType, inputStream);
        assertFalse(archiveInputStream.hasNextEntry());
        assertThrows(ArchiveException.class, () -> archiveInputStream.getNextEntry());
        archiveInputStream.close();
    }
}