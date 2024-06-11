import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
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
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for ArchiveStreamFactory
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() throws IOException, ArchiveException {
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream expectedArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveInputStream actualArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);

        Assert.assertNotNull("createArchiveInputStreamFromNameAndInputStream should not return null", actualArchiveInputStream);
        Assert.assertEquals("createArchiveInputStreamFromNameAndInputStream should return expected ArchiveInputStream", expectedArchiveInputStream.getClass(), actualArchiveInputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromUnknownNameAndInputStream() throws IOException {
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);

        ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws IOException, ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream expectedArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveInputStream actualArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);

        Assert.assertNotNull("createArchiveInputStreamFromInputStream should not return null", actualArchiveInputStream);
        Assert.assertEquals("createArchiveInputStreamFromInputStream should return expected ArchiveInputStream", expectedArchiveInputStream.getClass(), actualArchiveInputStream.getClass());
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() throws IOException, ArchiveException {
        String archiverName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream expectedArchiveOutputStream = new ZipArchiveOutputStream(outputStream);

        ArchiveOutputStream actualArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);

        Assert.assertNotNull("createArchiveOutputStreamFromNameAndOutputStream should not return null", actualArchiveOutputStream);
        Assert.assertEquals("createArchiveOutputStreamFromNameAndOutputStream should return expected ArchiveOutputStream", expectedArchiveOutputStream.getClass(), actualArchiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromUnknownNameAndOutputStream() throws IOException {
        String archiverName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();

        ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamFromOutputStream() throws IOException, ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream expectedArchiveOutputStream = new ZipArchiveOutputStream(outputStream);

        ArchiveOutputStream actualArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(outputStream);

        Assert.assertNotNull("createArchiveOutputStreamFromOutputStream should not return null", actualArchiveOutputStream);
        Assert.assertEquals("createArchiveOutputStreamFromOutputStream should return expected ArchiveOutputStream", expectedArchiveOutputStream.getClass(), actualArchiveOutputStream.getClass());
    }
}