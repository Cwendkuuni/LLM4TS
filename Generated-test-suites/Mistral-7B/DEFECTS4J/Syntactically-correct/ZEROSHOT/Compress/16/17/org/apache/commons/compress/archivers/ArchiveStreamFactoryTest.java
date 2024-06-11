import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        String archiverName = "tar";
        InputStream inputStream = Mockito.mock(InputStream.class);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);

        assertNotNull(archiveInputStream);
        assertEquals(TarArchiveInputStream.class, archiveInputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownArchiver() throws Exception {
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullArchiverName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullInputStream() throws Exception {
        String archiverName = "tar";
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, null);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        String archiverName = "zip";
        OutputStream outputStream = Mockito.mock(OutputStream.class);
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);

        assertNotNull(archiveOutputStream);
        assertEquals(ZipArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownArchiver() throws Exception {
        String archiverName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullArchiverName() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() throws Exception {
        String archiverName = "tar";
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, null);
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);

        assertNotNull(archiveInputStream);
        assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());

        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromStreamUnknownArchiver() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream("unknown", inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamFromStreamNullArchiverName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamFromStreamNullInputStream() throws Exception {
        new ArchiveStreamFactory().createArchiveInputStream("zip", null);
    }
}