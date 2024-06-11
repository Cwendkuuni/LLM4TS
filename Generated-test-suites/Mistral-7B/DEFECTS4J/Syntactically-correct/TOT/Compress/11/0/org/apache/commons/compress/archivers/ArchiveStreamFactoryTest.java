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
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() {
        String archiverName = "zip";
        InputStream in = new ByteArrayInputStream("ZSTD\u0000PK\u0005\u0006\u0014\u0000\u000a\u0000\u0000\u000a\u0000\u0000local\u0003test.txt\u0000\u0014test.txt\u0000\u0000\u0001test.txt\u0000\u0005local\u0002test2.txt\u0000\u0014test2.txt\u0000\u0001test2.txt".getBytes());
        ArchiveInputStream expectedArchiveInput = new ZipArchiveInputStream(in);

        ArchiveInputStream archInput = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        Assert.assertSame(expectedArchiveInput, archInput);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromUnknownNameAndInputStream() {
        String archiverName = "foo";
        InputStream in = new ByteArrayInputStream("ZSTD\u0000PK\u0005\u0006\u0014\u0000\u000a\u0000\u0000\u000a\u0000\u0000local\u0003test.txt\u0000\u0014test.txt\u0000\u0000\u0001test.txt\u0000\u0005local\u0002test2.txt\u0000\u0014test2.txt\u0000\u0001test2.txt".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() {
        String archiverName = "tar";
        OutputStream out = new ByteArrayOutputStream();

        ArchiveOutputStream expectedArchiveOutput = new TarArchiveOutputStream(out);

        ArchiveOutputStream archOutput = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
        Assert.assertSame(expectedArchiveOutput, archOutput);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromUnknownNameAndOutputStream() {
        String archiverName = "foo";
        OutputStream out = new ByteArrayOutputStream();

        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStreamAndAutodetection() throws IOException {
        byte[] expectedSignature = "PK\u0005\u0006\u0014\u0000\u000a\u0000\u0000\u000a\u0000\u0000local\u0003test.txt\u0000\u0014test.txt\u0000\u0000\u0001test.txt\u0000\u0005local\u0002test2.txt\u0000\u0014test2.txt\u0000\u0001test2.txt".getBytes();
        InputStream in = new ByteArrayInputStream(expectedSignature);

        ArchiveInputStream archInput = new ArchiveStreamFactory().createArchiveInputStream(in);
        Assert.assertInstanceOf(ZipArchiveInputStream.class, archInput);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromNullInputStream() {
        new ArchiveStreamFactory().createArchiveInputStream((InputStream) null);
    }

    @Test
    public void testCreateArchiveOutputStreamFromOutputStreamAndAutodetection() {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archOutput = new ArchiveStreamFactory().createArchiveOutputStream(out);
        Assert.assertTrue(archOutput instanceof TarArchiveOutputStream);

        archOutput.putArchiveEntry(new TarArchiveEntry("test.txt"));
        archOutput.write(new byte[1024], 0, 1024);
        archOutput.closeArchiveEntry();
        archOutput.finish();

        ArchiveInputStream archInput = new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(out.toByteArray()));
        Assert.assertInstanceOf(TarArchiveInputStream.class, archInput);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromNullOutputStream() {
        new ArchiveStreamFactory().createArchiveOutputStream((OutputStream) null);
    }
}