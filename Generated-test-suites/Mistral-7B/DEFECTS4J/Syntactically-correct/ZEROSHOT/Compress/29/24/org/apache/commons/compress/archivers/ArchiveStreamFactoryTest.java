import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.apache.commons.compress.archivers.ArchiveStreamFactory.AR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.ARJ;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.CPIO;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.DUMP;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.JAR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.TAR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.ZIP;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        // Test AR
        InputStream arInputStream = createTestArInput();
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(AR, arInputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();

        // Test ARJ
        InputStream arjInputStream = createTestArjInput();
        ArchiveInputStream arjArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ARJ, arjInputStream);
        Assert.assertNotNull(arjArchiveInputStream);
        arjArchiveInputStream.close();

        // Test CPIO
        InputStream cpioInputStream = createTestCpioInput();
        ArchiveInputStream cpioArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(CPIO, cpioInputStream);
        Assert.assertNotNull(cpioArchiveInputStream);
        cpioArchiveInputStream.close();

        // Test Dump
        InputStream dumpInputStream = createTestDumpInput();
        ArchiveInputStream dumpArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(DUMP, dumpInputStream);
        Assert.assertNotNull(dumpArchiveInputStream);
        dumpArchiveInputStream.close();

        // Test Jar
        InputStream jarInputStream = createTestJarInput();
        ArchiveInputStream jarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(JAR, jarInputStream);
        Assert.assertNotNull(jarArchiveInputStream);
        jarArchiveInputStream.close();

        // Test Tar
        InputStream tarInputStream = createTestTarInput();
        ArchiveInputStream tarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(TAR, tarInputStream);
        Assert.assertNotNull(tarArchiveInputStream);
        tarArchiveInputStream.close();

        // Test Zip
        InputStream zipInputStream = createTestZipInput();
        ArchiveInputStream zipArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ZIP, zipInputStream);
        Assert.assertNotNull(zipArchiveInputStream);
        zipArchiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        // Test AR
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(AR, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        // Test ARJ
        OutputStream arjOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arjArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ARJ, arjOutputStream);
        Assert.assertNotNull(arjArchiveOutputStream);
        arjArchiveOutputStream.close();

        // Test CPIO
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(CPIO, cpioOutputStream);
        Assert.assertNotNull(cpioArchiveOutputStream);
        cpioArchiveOutputStream.close();

        // Test Dump
        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream dumpArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(DUMP, dumpOutputStream);
        Assert.assertNotNull(dumpArchiveOutputStream);
        dumpArchiveOutputStream.close();

        // Test Jar
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(JAR, jarOutputStream);
        Assert.assertNotNull(jarArchiveOutputStream);
        jarArchiveOutputStream.close();

        // Test Tar
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(TAR, tarOutputStream);
        Assert.assertNotNull(tarArchiveOutputStream);
        tarArchiveOutputStream.close();

        // Test Zip
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ZIP, zipOutputStream);
        Assert.assertNotNull(zipArchiveOutputStream);
        zipArchiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownFormat() throws IOException {
        InputStream inputStream = createTestZipInput();
        new ArchiveStreamFactory().createArchiveInputStream("UnknownFormat", inputStream);
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveInputStreamSevenZ() throws IOException {
        InputStream sevenZInputStream = createTestSevenZInput();
        new ArchiveStreamFactory().createArchiveInputStream(SEVEN_Z, sevenZInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownFormat() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("UnknownFormat", outputStream);
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveOutputStreamSevenZ() throws IOException {
        OutputStream sevenZOutputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(SEVEN_Z, sevenZOutputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws IOException {
        InputStream inputStream = createTestZipInput();
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    private InputStream createTestArInput() throws IOException {
        return new ArArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }

    private InputStream createTestArjInput() throws IOException {
        return new ArjArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }

    private InputStream createTestCpioInput() throws IOException {
        return new CpioArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }

    private InputStream createTestDumpInput() throws IOException {
        return new DumpArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }

    private InputStream createTestJarInput() throws IOException {
        return new JarArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }

    private InputStream createTestTarInput() throws IOException {
        return new TarArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }

    private InputStream createTestZipInput() throws IOException {
        return new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }

    private InputStream createTestSevenZInput() throws IOException {
        return new SevenZFile("7zTest.7z").getInputStream();
    }

}