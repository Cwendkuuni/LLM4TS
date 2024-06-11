import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Test;
import org.junit.Assert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ByteArrayOutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        // Test creating an archive input stream from a known archive format and an InputStream.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        archiveOutputStream.putArchiveEntry(new ZipArchiveEntry("test.txt"));
        archiveOutputStream.write("Hello, world!".getBytes(StandardCharsets.UTF_8), 0, "Hello, world!".length());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        ZipArchiveEntry entry = (ZipArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", entry.getName());
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        IOUtils.copy(archiveInputStream, outputStream2);
        Assert.assertEquals("Hello, world!\n", new String(outputStream2.toByteArray(), StandardCharsets.UTF_8));
        archiveInputStream.close();
        inputStream.close();
        outputStream.close();
        outputStream2.close();

        // Test creating an archive input stream from an unknown format and an InputStream.
        ByteArrayOutputStream outputStream3 = new ByteArrayOutputStream();
        OutputStream unknownOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream2 = new ArchiveStreamFactory().createArchiveOutputStream("unknown", outputStream3);
        try {
            archiveOutputStream2.putArchiveEntry(new ZipArchiveEntry("test.txt"));
            Assert.fail("Should have thrown an ArchiveException.");
        } catch (ArchiveException e) {
            // Expected
        }
        archiveOutputStream2.finish();
        InputStream unknownInputStream = new ByteArrayInputStream(outputStream3.toByteArray());
        ArchiveInputStream unknownArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", unknownInputStream);
        try {
            ArchiveEntry entry2 = unknownArchiveInputStream.getNextEntry();
            Assert.fail("Should have thrown an ArchiveException.");
        } catch (ArchiveException e) {
            // Expected
        }
        unknownArchiveInputStream.close();
        unknownInputStream.close();
        outputStream3.close();

        // Test creating an archive input stream from a known archive format and bytes.
        byte[] unknownBytes = new byte[512];
        ArchiveInputStream unknownArchiveInputStream2 = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(unknownBytes));
        try {
            ArchiveEntry entry3 = unknownArchiveInputStream2.getNextEntry();
            Assert.fail("Should have thrown an ArchiveException.");
        } catch (ArchiveException e) {
            // Expected
        }
        unknownArchiveInputStream2.close();

        byte[] tarHeaderBytes = TarArchiveInputStream.createTarHeader("test.txt", 0, 10);
        ByteArrayOutputStream tarOutputStream = new ByteArrayOutputStream();
        tarOutputStream.write(tarHeaderBytes, 0, tarHeaderBytes.length);
        OutputStream tarOutputStream2 = new ByteArrayOutputStream();
        ArchiveOutputStream tarArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream2);
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.writeTo(tarOutputStream);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        InputStream tarInputStream = new ByteArrayInputStream(tarOutputStream.toByteArray());
        ArchiveInputStream tarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        ArchiveEntry entry4 = tarArchiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", entry4.getName());
        ByteArrayOutputStream outputStream4 = new ByteArrayOutputStream();
        IOUtils.copy(tarArchiveInputStream, outputStream4);
        Assert.assertEquals(tarHeaderBytes.length, outputStream4.size());
        tarArchiveInputStream.close();
        tarInputStream.close();
        outputStream4.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        // Test creating an archive output stream from a known archive format and an OutputStream.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        archiveOutputStream.putArchiveEntry(new ZipArchiveEntry("test.txt"));
        archiveOutputStream.write("Hello, world!".getBytes(StandardCharsets.UTF_8), 0, "Hello, world!".length());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        ZipArchiveEntry entry = (ZipArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", entry.getName());
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        IOUtils.copy(archiveInputStream, outputStream2);
        Assert.assertEquals("Hello, world!\n", new String(outputStream2.toByteArray(), StandardCharsets.UTF_8));
        archiveInputStream.close();
        inputStream.close();
        outputStream.close();
        outputStream2.close();

        // Test creating an archive output stream from an unknown archive format and an OutputStream.
        ByteArrayOutputStream outputStream3 = new ByteArrayOutputStream();
        OutputStream unknownOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream2 = new ArchiveStreamFactory().createArchiveOutputStream("unknown", outputStream3);
        try {
            archiveOutputStream2.putArchiveEntry(new ZipArchiveEntry("test.txt"));
            Assert.fail("Should have thrown an ArchiveException.");
        } catch (ArchiveException e) {
            // Expected
        }
        archiveOutputStream2.finish();
        InputStream unknownInputStream = new ByteArrayInputStream(outputStream3.toByteArray());
        ArchiveInputStream unknownArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", unknownInputStream);
        try {
            ArchiveEntry entry2 = unknownArchiveInputStream.getNextEntry();
            Assert.fail("Should have thrown an ArchiveException.");
        } catch (ArchiveException e) {
            // Expected
        }
        unknownArchiveInputStream.close();
        unknownInputStream.close();
        outputStream3.close();

        // Test creating an archive output stream from a known archive format and a byte array.
        byte[] unknownBytes = new byte[512];
        ArchiveOutputStream unknownArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
        try {
            unknownArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
            Assert.fail("Should have thrown an ArchiveException.");
        } catch (ArchiveException e) {
            // Expected
        }
        unknownArchiveOutputStream.finish();
        InputStream unknownArchiveInputStream2 = new ByteArrayInputStream(unknownArchiveOutputStream.toByteArray());
        ArchiveInputStream unknownTarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, unknownArchiveInputStream2);
        try {
            ArchiveEntry entry3 = unknownTarArchiveInputStream.getNextEntry();
            Assert.fail("Should have thrown an ArchiveException.");
        } catch (ArchiveException e) {
            // Expected
        }
        unknownTarArchiveInputStream.close();
        unknownArchiveInputStream2.close();

        byte[] tarHeaderBytes = TarArchiveInputStream.createTarHeader("test.txt", 0, 10);
        ByteArrayOutputStream tarOutputStream = new ByteArrayOutputStream();
        tarOutputStream.write(tarHeaderBytes, 0, tarHeaderBytes.length);
        ArchiveOutputStream tarArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.writeTo(tarOutputStream);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        InputStream tarInputStream = new ByteArrayInputStream(tarOutputStream.toByteArray());
        ArchiveInputStream tarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        ArchiveEntry entry4 = tarArchiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", entry4.getName());
        ByteArrayOutputStream outputStream4 = new ByteArrayOutputStream();
        IOUtils.copy(tarArchiveInputStream, outputStream4);
        Assert.assertEquals(tarHeaderBytes.length, outputStream4.size());
        tarArchiveInputStream.close();
        tarInputStream.close();
        outputStream4.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws IOException, ArchiveException {
        // Test creating an archive input stream from a known archive format and bytes.
        byte[] tarHeaderBytes = TarArchiveInputStream.createTarHeader("test.txt", 0, 10);
        ByteArrayInputStream tarInputStream = new ByteArrayInputStream(tarHeaderBytes);
        ArchiveInputStream tarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        ArchiveEntry entry = tarArchiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", entry.getName());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(tarArchiveInputStream, outputStream);
        Assert.assertEquals(tarHeaderBytes.length, outputStream.size());
        tarArchiveInputStream.close();
        tarInputStream.close();
        outputStream.close();

        // Test creating an archive input stream from an unknown archive format and bytes.
        byte[] unknownBytes = new byte[512];
        ArchiveInputStream unknownArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", new ByteArrayInputStream(unknownBytes));
        try {
            ArchiveEntry entry2 = unknownArchiveInputStream.getNextEntry();
            Assert.fail("Should have thrown an ArchiveException.");
        } catch (ArchiveException e) {
            // Expected
        }
        unknownArchiveInputStream.close();
    }
}