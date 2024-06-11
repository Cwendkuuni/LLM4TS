package org.apache.commons.compress.archivers;

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
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Test class for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamFromName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
            Assert.fail("Expected ArchiveException for unsupported input stream.");
        } catch (ArchiveException e) {
            // Expected
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        inputStream = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));
        try {
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
            Assert.assertNotNull(archiveInputStream);
            archiveInputStream.close();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        inputStream = new ByteArrayInputStream(new byte[1024]);
        try {
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
            Assert.assertNotNull(archiveInputStream);
            archiveInputStream.close();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, inputStream);
            Assert.fail("Expected StreamingNotSupportedException for SevenZ.");
        } catch (StreamingNotSupportedException e) {
            // Expected
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        final byte[] data = new byte[]{"header\0".getBytes(StandardCharsets.UTF_8)};
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data)) {
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
            Assert.assertNotNull(archiveInputStream);
            ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
            Assert.assertNotNull(archiveEntry);
            Assert.assertEquals("header", archiveEntry.getName());
            archiveInputStream.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromName() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
            archiveOutputStream.putNextEntry(new JarArchiveEntry("test.txt"));
            archiveOutputStream.write(new byte[]{0x68, 0x65, 0x6C, 0x6C, 0x6F});
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            Assert.assertEquals("test.txt\0", new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
        } finally {
            IOUtils.closeQuietly(outputStream);
        }

        outputStream = new ByteArrayOutputStream();
        try {
            ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, outputStream);
            Assert.fail("Expected StreamingNotSupportedException for SevenZ.");
        } catch (StreamingNotSupportedException e) {
            // Expected
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromStream() throws Exception {
        final String fileName = "test.txt";
        OutputStream outputStream = new FileOutputStream(fileName);
        try {
            ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
            archiveOutputStream.putNextEntry(new JarArchiveEntry(fileName));
            archiveOutputStream.write(new byte[]{0x68, 0x65, 0x6C, 0x6C, 0x6F});
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            Assert.assertTrue(new File(fileName).exists());
        } finally {
            IOUtils.closeQuietly(outputStream);
            new File(fileName).delete();
        }
    }

    @Test
    public void testCreateInputStreamFromInvalidArchive() throws Exception {
        try (InputStream inputStream = new FileInputStream("non-existent-file.tar")) {
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
            Assert.assertNull(archiveInputStream.getNextEntry());
            archiveInputStream.close();
        }
    }

    @Test
    public void testCreateOutputStreamFromInvalidArchive() throws Exception {
        try (OutputStream outputStream = new FileOutputStream("non-existent-file.tar")) {
            ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
            archiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
        }
    }
}