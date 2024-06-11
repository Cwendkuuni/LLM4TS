import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithKnownFormat() throws IOException, ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        Assert.assertEquals(ArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownFormat() throws IOException {
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        factory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetect() throws IOException, ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamWithKnownFormat() throws IOException, ArchiveException {
        String archiverName = ArchiveStreamFactory.JAR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        Assert.assertEquals(ArchiveOutputStream.class, archiveOutputStream.getClass());
        archiveOutputStream.finish();
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownFormat() throws IOException {
        String archiverName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        factory.createArchiveOutputStream(archiverName, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWritesData() throws IOException, ArchiveException {
        String archiverName = ArchiveStreamFactory.JAR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        archiveOutputStream.putArchiveEntry(new JarArchiveEntry("test.txt"));
        archiveOutputStream.writeTo(new ByteArrayInputStream("Hello World!".getBytes()), 0, "Hello World!".length());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();
        ByteArrayOutputStream actual = (ByteArrayOutputStream) outputStream;
        byte[] expected = new byte[]{"PK\u0005\u0004\n\u0002\u0014\u0000\u000a\u0000\u0000\u0000local\u0002test.txt\u0000\u0012\u000bHello World!\u0000\u0012\u0015Hello World!\n"};
        Assert.assertArrayEquals(expected, actual.toByteArray());
    }
}