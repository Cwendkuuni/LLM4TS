package test;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
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

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        String encoding = "UTF-8";
        ArchiveStreamFactory factory = new ArchiveStreamFactory(encoding);

        // Test AR Archive
        InputStream arInputStream = new ByteArrayInputStream(testResources("ar-archive.bin"));
        ArchiveInputStream arInputStreamResult = factory.createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        Assert.assertTrue(arInputStreamResult instanceof ArArchiveInputStream);

        arInputStream.close();

        // Test ARJ Archive
        InputStream arjInputStream = new ByteArrayInputStream(testResources("arj-archive.bin"));
        ArchiveInputStream arjInputStreamResult = factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, arjInputStream);
        Assert.assertTrue(arjInputStreamResult instanceof ArjArchiveInputStream);

        arjInputStream.close();

        // Test CPIO Archive
        InputStream cpioInputStream = new ByteArrayInputStream(testResources("cpio-archive.bin"));
        ArchiveInputStream cpioInputStreamResult = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        Assert.assertTrue(cpioInputStreamResult instanceof CpioArchiveInputStream);

        cpioInputStream.close();

        // Test Dump Archive
        InputStream dumpInputStream = new ByteArrayInputStream(testResources("dump-archive.bin"));
        ArchiveInputStream dumpInputStreamResult = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        Assert.assertTrue(dumpInputStreamResult instanceof DumpArchiveInputStream);

        dumpInputStream.close();

        // Test Jar Archive
        InputStream jarInputStream = new ByteArrayInputStream(testResources("jar-archive.bin"));
        ArchiveInputStream jarInputStreamResult = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        Assert.assertTrue(jarInputStreamResult instanceof JarArchiveInputStream);

        jarInputStream.close();

        // Test Tar Archive
        InputStream tarInputStream = new ByteArrayInputStream(testResources("tar-archive.bin"));
        ArchiveInputStream tarInputStreamResult = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        Assert.assertTrue(tarInputStreamResult instanceof TarArchiveInputStream);

        tarInputStream.close();

        // Test Zip Archive
        InputStream zipInputStream = new ByteArrayInputStream(testResources("zip-archive.bin"));
        ArchiveInputStream zipInputStreamResult = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        Assert.assertTrue(zipInputStreamResult instanceof ZipArchiveInputStream);

        zipInputStream.close();

        // Test SevenZ Archive
        InputStream sevenZInputStream = new ByteArrayInputStream(testResources("7z-archive.bin"));
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, sevenZInputStream);
            Assert.fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }

        sevenZInputStream.close();

        // Test unknown Archive
        InputStream unknownInputStream = new ByteArrayInputStream(testResources("unknown-archive.bin"));
        try {
            factory.createArchiveInputStream("unknown", unknownInputStream);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }

        unknownInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        String encoding = "UTF-8";
        ArchiveStreamFactory factory = new ArchiveStreamFactory(encoding);

        // Test AR Archive
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arOutputStreamResult = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        arOutputStreamResult.putArchiveEntry(new org.apache.commons.compress.archivers.ar.ARFileHeader("test.txt", ""));
        arOutputStreamResult.write(testResources("test.txt"), 0, testResources("test.txt").length);
        arOutputStreamResult.closeArchiveEntry();
        arOutputStreamResult.finish();
        arOutputStream.close();

        byte[] arOutput = ((ByteArrayOutputStream) arOutputStream).toByteArray();
        Assert.assertArrayEquals(testResources("ar-archive-output.bin"), arOutput);

        // Test ARJ Archive
        OutputStream arjOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arjOutputStreamResult = factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, arjOutputStream);
        arjOutputStreamResult.putNextEntry(new org.apache.commons.compress.archivers.arj.ARJFileHeader("test.txt", ""));
        arjOutputStreamResult.write(testResources("test.txt"), 0, testResources("test.txt").length);
        arjOutputStreamResult.closeEntry();
        arjOutputStreamResult.finish();
        arjOutputStream.close();

        byte[] arjOutput = ((ByteArrayOutputStream) arjOutputStream).toByteArray();
        Assert.assertArrayEquals(testResources("arj-archive-output.bin"), arjOutput);

        // Test CPIO Archive
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOutputStreamResult = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        cpioOutputStreamResult.putNextEntry(new org.apache.commons.compress.archivers.cpio.CpioHeader(
                "test.txt", 0, testResources("test.txt").length, CpioHeader.MAGIC_BINARY, new long[0]));
        cpioOutputStreamResult.write(testResources("test.txt"), 0, testResources("test.txt").length);
        cpioOutputStreamResult.closeEntry();
        cpioOutputStreamResult.finish();
        cpioOutputStream.close();

        byte[] cpioOutput = ((ByteArrayOutputStream) cpioOutputStream).toByteArray();
        Assert.assertArrayEquals(testResources("cpio-archive-output.bin"), cpioOutput);

        // Test Dump Archive
        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream dumpOutputStreamResult = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
        dumpOutputStreamResult.putNextEntry(new org.apache.commons.compress.archivers.dump.DumpHeader(
                "test.txt", 0, testResources("test.txt").length, new long[0]));
        dumpOutputStreamResult.write(testResources("test.txt"), 0, testResources("test.txt").length);
        dumpOutputStreamResult.closeEntry();
        dumpOutputStreamResult.finish();
        dumpOutputStream.close();

        byte[] dumpOutput = ((ByteArrayOutputStream) dumpOutputStream).toByteArray();
        Assert.assertArrayEquals(testResources("dump-archive-output.bin"), dumpOutput);

        // Test Jar Archive
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        JarArchiveOutputStream jarOutputStreamResult = (JarArchiveOutputStream) factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        jarOutputStreamResult.putNextEntry(new JarEntry("test.txt", new byte[0][], 0, 0, null));
        jarOutputStreamResult.write(testResources("test.txt"), 0, testResources("test.txt").length);
        jarOutputStreamResult.closeEntry();
        jarOutputStreamResult.finish();
        jarOutputStream.close();

        byte[] jarOutput = ((ByteArrayOutputStream) jarOutputStream).toByteArray();
        Assert.assertArrayEquals(testResources("jar-archive-output.bin"), jarOutput);

        // Test Tar Archive
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStreamResult = (TarArchiveOutputStream) factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        tarOutputStreamResult.putNextEntry(new TarEntry("test.txt", null));
        tarOutputStreamResult.write(testResources("test.txt"), 0, testResources("test.txt").length);
        tarOutputStreamResult.closeEntry();
        tarOutputStreamResult.finish();
        tarOutputStream.close();

        byte[] tarOutput = ((ByteArrayOutputStream) tarOutputStream).toByteArray();
        Assert.assertArrayEquals(testResources("tar-archive-output.bin"), tarOutput);

        // Test Zip Archive
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOutputStreamResult = (ZipArchiveOutputStream) factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        zipOutputStreamResult.putNextEntry(new ZipEntry("test.txt", new byte[0][], 0, 0, null));
        zipOutputStreamResult.write(testResources("test.txt"), 0, testResources("test.txt").length);
        zipOutputStreamResult.closeEntry();
        zipOutputStreamResult.finish();
        zipOutputStream.close();

        byte[] zipOutput = ((ByteArrayOutputStream) zipOutputStream).toByteArray();
        Assert.assertArrayEquals(testResources("zip-archive-output.bin"), zipOutput);

        // Test SevenZ Archive
        try {
            OutputStream sevenZOutputStream = new ByteArrayOutputStream();
            ArchiveOutputStream sevenZOutputStreamResult = factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, sevenZOutputStream);
            // SevenZFile does not support writing through the outputstream
            Assert.fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }

        // Test unknown Archive
        OutputStream unknownOutputStream = new ByteArrayOutputStream();
        try {
            factory.createArchiveOutputStream("unknown", unknownOutputStream);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }

        unknownOutputStream.close();
    }

    private byte[] testResources(String fileName) throws IOException {
        InputStream resourceInputStream = this.getClass().getResourceAsStream(fileName);
        byte[] buffer = new byte[resourceInputStream.available()];
        resourceInputStream.read(buffer);
        resourceInputStream.close();
        return buffer;
    }
}