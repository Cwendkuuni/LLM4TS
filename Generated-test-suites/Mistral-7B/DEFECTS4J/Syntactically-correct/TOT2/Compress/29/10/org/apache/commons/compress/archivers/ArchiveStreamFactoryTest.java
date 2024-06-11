import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ARArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ARArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ARJArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        Assert.assertEquals("AR archiveInputStream class", ARArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AR_InvalidSignature() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AR_UnexpectedFormat() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_ARJ() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, inputStream);
        Assert.assertEquals("ARJ archiveInputStream class", ARJArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_ARJ_InvalidSignature() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_ARJ_UnexpectedFormat() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        Assert.assertEquals("CPIO archiveInputStream class", CpioArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_CPIO_InvalidSignature() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_CPIO_UnexpectedFormat() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Dump() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        Assert.assertEquals("DUMP archiveInputStream class", DumpArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Dump_InvalidSignature() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Dump_UnexpectedFormat() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        Assert.assertEquals("JAR archiveInputStream class", JarArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_JAR_InvalidSignature() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_JAR_UnexpectedFormat() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Tar() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        Assert.assertEquals("TAR archiveInputStream class", TarArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Tar_InvalidSignature() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Tar_UnexpectedFormat() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Zip() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        Assert.assertEquals("ZIP archiveInputStream class", ZipArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Zip_InvalidSignature() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Zip_UnexpectedFormat() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        archiveOutputStream.createArchiveEntry("test.txt", new byte[0]);
        archiveOutputStream.writeToArchive();
        archiveOutputStream.finish();
        Assert.assertEquals("AR archiveOutputStream class", ARArchiveOutputStream.class, archiveOutputStream.getClass());
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_AR_NullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_ARJ() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, outputStream);
        archiveOutputStream.createArchiveEntry("test.txt", new byte[0]);
        archiveOutputStream.writeToArchive();
        archiveOutputStream.finish();
        archiveOutputStream.close();
        Assert.assertEquals("ARJ archiveOutputStream class", ARJArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_ARJ_NullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, null);
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        archiveOutputStream.createArchiveEntry("test.txt", new byte[0]);
        archiveOutputStream.writeToArchive();
        archiveOutputStream.finish();
        archiveOutputStream.close();
        Assert.assertEquals("CPIO archiveOutputStream class", CpioArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_CPIO_NullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
    }

    @Test
    public void testCreateArchiveOutputStream_Dump() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        archiveOutputStream.createArchiveEntry("test.txt", new byte[0]);
        archiveOutputStream.writeToArchive();
        archiveOutputStream.finish();
        archiveOutputStream.close();
        Assert.assertEquals("DUMP archiveOutputStream class", DumpArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_Dump_NullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, null);
    }

    @Test
    public void testCreateArchiveOutputStream_Jar() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        archiveOutputStream.createArchiveEntry("test.txt", new byte[0]);
        archiveOutputStream.writeToArchive();
        archiveOutputStream.finish();
        archiveOutputStream.close();
        Assert.assertEquals("JAR archiveOutputStream class", JarArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_Jar_NullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_Tar() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        archiveOutputStream.createArchiveEntry("test.txt", new byte[0]);
        archiveOutputStream.writeToArchive();
        archiveOutputStream.finish();
        archiveOutputStream.close();
        Assert.assertEquals("TAR archiveOutputStream class", TarArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_Tar_NullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_Zip() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        archiveOutputStream.createArchiveEntry("test.txt", new byte[0]);
        archiveOutputStream.writeToArchive();
        archiveOutputStream.finish();
        archiveOutputStream.close();
        Assert.assertEquals("ZIP archiveOutputStream class", ZipArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_Zip_NullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

}