import org.apache.commons.compress.archivers.*;
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

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test creating an archive input stream from a known archive type
        ByteArrayInputStream testInput = new ByteArrayInputStream(ZipArchiveInputStream.getMagicBytes());
        ArchiveInputStream archiveInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, testInput);
        Assert.assertEquals("ZipArchiveInputStream", archiveInput.getClass().getSimpleName());
        archiveInput.close();

        // Test creating an archive input stream from an unknown archive type
        ByteArrayInputStream unknownInput = new ByteArrayInputStream(new byte[10]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, unknownInput);
            Assert.fail("Should have thrown an ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
        unknownInput.close();

        // Test creating an archive input stream from a null InputStream
        InputStream nullInput = null;
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, nullInput);
            Assert.fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test creating an archive output stream to a known archive type
        ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, testOutput);
        archiveOutput.putArchiveEntry(new JarArchiveEntry("test.txt"));
        archiveOutput.write(new byte[10], 0, 10);
        archiveOutput.finishStream();
        archiveOutput.close();

        JarArchiveInputStream testInput = new JarArchiveInputStream(new ByteArrayInputStream(testOutput.toByteArray()));
        JarArchiveEntry testEntry = testInput.getNextJarEntry();
        Assert.assertEquals("test.txt", testEntry.getName());
        Assert.assertEquals("application/octet-stream", testEntry.getSize(), 10L);
        testInput.close();

        // Test creating an archive output stream to an unknown archive type
        ByteArrayOutputStream unknownOutput = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ARJ, unknownOutput);
            Assert.fail("Should have thrown an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
        unknownOutput.close();

        // Test creating an archive output stream to a null OutputStream
        OutputStream nullOutput = null;
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, nullOutput);
            Assert.fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithMagicBytes() throws Exception {
        // Test creating an archive input stream from an archive type detected from the magic bytes
        InputStream input = new FileInputStream("test.zip");
        ArchiveInputStream archiveInput = new ArchiveStreamFactory().createArchiveInputStream(input);
        Assert.assertEquals("ZipArchiveInputStream", archiveInput.getClass().getSimpleName());
        archiveInput.close();
        input.close();
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveInputStreamWithInvalidMagicBytes() throws Exception {
        // Test creating an archive input stream from an invalid magic bytes
        InputStream input = new ByteArrayInputStream(new byte[10]);
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, input);
    }
}