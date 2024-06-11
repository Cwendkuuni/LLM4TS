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
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() {
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromUnknownNameAndInputStream() {
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromNullInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() {
        String archiverName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromUnknownNameAndOutputStream() {
        String archiverName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamFromOutputStream() {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromNullOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(null);
    }
}