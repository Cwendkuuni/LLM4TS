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

/**
 * Test class for ArchiveStreamFactory
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test createArchiveInputStream method with valid arguments.
     */
    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test AR format
        InputStream arInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();

        // Test ZIP format
        InputStream zipInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStreamZip = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        Assert.assertNotNull(archiveInputStreamZip);
        archiveInputStreamZip.close();

        // Test TAR format
        InputStream tarInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStreamTar = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        Assert.assertNotNull(archiveInputStreamTar);
        archiveInputStreamTar.close();

        // Test JAR format
        InputStream jarInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStreamJar = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        Assert.assertNotNull(archiveInputStreamJar);
        archiveInputStreamJar.close();

        // Test CPIO format
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStreamCpio = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        Assert.assertNotNull(archiveInputStreamCpio);
        archiveInputStreamCpio.close();

        // Test DUMP format
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStreamDump = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        Assert.assertNotNull(archiveInputStreamDump);
        archiveInputStreamDump.close();
    }

    /**
     * Test createArchiveInputStream method with invalid arguments.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamInvalidArchiverName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        new ArchiveStreamFactory().createArchiveInputStream("invalid", inputStream);
    }

    /**
     * Test createArchiveInputStream method with null input stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullInputStream() throws Exception {
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    /**
     * Test createArchiveOutputStream method with valid arguments.
     */
    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test AR format
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        archiveOutputStream.putArchiveEntry(new ArArchiveEntry("test.txt"));
        archiveOutputStream.writeToArchive("Hello, World!".getBytes());
        archiveOutputStream.finish();
        archiveOutputStream.close();
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(arOutputStream.toByteArray()));
        Assert.assertEquals("Hello, World!", IOUtils.toString(archiveInputStream));

        // Test ZIP format
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStreamZip = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        archiveOutputStreamZip.putArchiveEntry(new JarArchiveEntry("test.txt"));
        archiveOutputStreamZip.writeToArchive("Hello, World!".getBytes());
        archiveOutputStreamZip.finish();
        archiveOutputStreamZip.close();
        ArchiveInputStream archiveInputStreamZip = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(zipOutputStream.toByteArray()));
        Assert.assertEquals("Hello, World!", IOUtils.toString(archiveInputStreamZip));

        // Test TAR format
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStreamTar = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        archiveOutputStreamTar.putArchiveEntry(new TarArchiveEntry("test.txt"));
        archiveOutputStreamTar.writeToArchive("Hello, World!".getBytes());
        archiveOutputStreamTar.finish();
        archiveOutputStreamTar.close();
        ArchiveInputStream archiveInputStreamTar = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(tarOutputStream.toByteArray()));
        Assert.assertEquals("Hello, World!", IOUtils.toString(archiveInputStreamTar));

        // Test JAR format
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStreamJar = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        archiveOutputStreamJar.putNextEntry(new JarEntry("test.txt"));
        archiveOutputStreamJar.write("Hello, World!".getBytes());
        archiveOutputStreamJar.finish();
        archiveOutputStreamJar.close();
        ArchiveInputStream archiveInputStreamJar = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(jarOutputStream.toByteArray()));
        Assert.assertEquals("Hello, World!", IOUtils.toString(archiveInputStreamJar));

        // Test CPIO format
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStreamCpio = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        archiveOutputStreamCpio.putNextEntry(new CpioArchiveEntry("test.txt"));
        archiveOutputStreamCpio.writeToArchive("Hello, World!".getBytes());
        archiveOutputStreamCpio.finish();
        archiveOutputStreamCpio.close();
        ArchiveInputStream archiveInputStreamCpio = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(cpioOutputStream.toByteArray()));
        Assert.assertEquals("Hello, World!", IOUtils.toString(archiveInputStreamCpio));

        // Test DUMP format
        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStreamDump = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
        archiveOutputStreamDump.putNextEntry(new DumpArchiveEntry("test.txt"));
        archiveOutputStreamDump.writeToArchive("Hello, World!".getBytes());
        archiveOutputStreamDump.finish();
        archiveOutputStreamDump.close();
        ArchiveInputStream archiveInputStreamDump = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(dumpOutputStream.toByteArray()));
        Assert.assertEquals("Hello, World!", IOUtils.toString(archiveInputStreamDump));
    }

    /**
     * Test createArchiveOutputStream method with invalid arguments.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamInvalidArchiverName() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("invalid", outputStream);
    }

    /**
     * Test createArchiveOutputStream method with null output stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() throws Exception {
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }
}