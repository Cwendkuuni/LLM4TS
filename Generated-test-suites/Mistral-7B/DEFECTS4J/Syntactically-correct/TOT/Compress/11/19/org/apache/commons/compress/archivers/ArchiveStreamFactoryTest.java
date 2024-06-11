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
import org.junit.Assert;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test with valid arguments
        InputStream input = new ByteArrayInputStream("Zip signature".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
        Assert.assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

        input = new ByteArrayInputStream("Tar signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, input);
        Assert.assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);

        input = new ByteArrayInputStream("Ar signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, input);
        Assert.assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);

        input = new ByteArrayInputStream("Cpio signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, input);
        Assert.assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);

        input = new ByteArrayInputStream("Jar signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, input);
        Assert.assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);

        input = new ByteArrayInputStream("Dump signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, input);
        Assert.assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);

        // Test with invalid archiver name
        Assert.assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream("invalid", input));

        // Test with null input stream
        Assert.assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, null));
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test with valid arguments
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        Assert.assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);

        output = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
        Assert.assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);

        output = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        Assert.assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);

        output = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
        Assert.assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);

        output = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
        Assert.assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);

        output = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, output);
        Assert.assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);

        // Test with invalid archiver name
        Assert.assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream("invalid", output));

        // Test with null output stream
        Assert.assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null));
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        // Test with valid arguments and known archive types
        InputStream input = new ByteArrayInputStream("Zip signature".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(input);
        Assert.assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

        input = new ByteArrayInputStream("Tar signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(input);
        Assert.assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);

        input = new ByteArrayInputStream("Ar signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(input);
        Assert.assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);

        input = new ByteArrayInputStream("Cpio signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(input);
        Assert.assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);

        input = new ByteArrayInputStream("Jar signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(input);
        Assert.assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);

        input = new ByteArrayInputStream("Dump signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(input);
        Assert.assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);

        // Test with unknown archive type
        input = new ByteArrayInputStream("Unknown signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(input);
        Assert.assertInstanceOf(ArchiveInputStream.class, archiveInputStream); // Will throw ArchiveException in production code

        // Test with null input stream
        Assert.assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream((InputStream) null));
    }
}