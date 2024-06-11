import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.JarArchiveEntry;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ArchiveStreamFactoryTest {

    private File tempDir;

    @Before
    public void setUp() throws IOException {
        tempDir = Files.createTempDirectory("test-archive-stream-factory").toFile();
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(tempDir.getAbsolutePath()));
    }

    @Test
    public void testCreateArchiveInputStreamCorrectFormat() throws IOException, ArchiveException {
        final InputStream testInput = new FileInputStream("test-files/test-archive.tar.gz");
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, testInput);
        Assert.assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        archiveInputStream.close();
        testInput.close();
    }

    @Test
    public void testCreateArchiveInputStreamWrongFormat() throws IOException, ArchiveException {
        final InputStream testInput = new FileInputStream("test-files/test-archive.zip");
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, testInput);
        Assert.assertFalse(archiveInputStream instanceof TarArchiveInputStream);
        archiveInputStream.close();
        testInput.close();

        try {
            final ArchiveInputStream archiveInputStream2 = new ArchiveStreamFactory().createArchiveInputStream(null, testInput);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        } finally {
            testInput.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStreamCorrectFormat() throws IOException, ArchiveException {
        final File outputFile = new File(tempDir, "test-archive.tar");
        try (final OutputStream outputStream = new FileOutputStream(outputFile)) {
            final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
            archiveOutputStream.putArchiveEntry(new TarArchiveEntry("testEntry.txt"));
            archiveOutputStream.write(new byte[100], 0, 100);
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            archiveOutputStream.close();
        }

        final InputStream testInput = new FileInputStream(outputFile);
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, testInput);
        Assert.assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        archiveInputStream.close();
        testInput.close();
        Files.deleteIfExists(Paths.get(outputFile.getAbsolutePath()));
    }

    @Test
    public void testCreateArchiveOutputStreamWrongFormat() throws IOException, ArchiveException {
        final File outputFile = new File(tempDir, "test-archive.zip");
        try (final OutputStream outputStream = new FileOutputStream(outputFile)) {
            final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
            Assert.assertFalse(archiveOutputStream instanceof TarArchiveOutputStream);
            archiveOutputStream.close();
        }

        Files.deleteIfExists(Paths.get(outputFile.getAbsolutePath()));
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws IOException, ArchiveException {
        final byte[] tarHeader = Files.readAllBytes(Paths.get("test-files/test-archive.tar.gz"));
        final InputStream testInput = new ByteArrayInputStream(tarHeader);
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, testInput);
        Assert.assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        archiveInputStream.close();
        testInput.close();
    }

    @Test
    public void testCreateZipArchiveOutputStream() throws IOException, ArchiveException {
        final File outputFile = new File(tempDir, "test-archive.zip");
        try (final OutputStream outputStream = new FileOutputStream(outputFile)) {
            final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
            archiveOutputStream.putArchiveEntry(new JarArchiveEntry("testEntry.txt"));
            archiveOutputStream.write(new byte[100], 0, 100);
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            archiveOutputStream.close();
        }

        final InputStream testInput = new FileInputStream(outputFile);
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, testInput);
        Assert.assertTrue(archiveInputStream instanceof JarArchiveInputStream);
        archiveInputStream.close();
        testInput.close();
        Files.deleteIfExists(Paths.get(outputFile.getAbsolutePath()));
    }

    @Test
    public void testCreateArArchiveInputStream() throws IOException, ArchiveException {
        final InputStream testInput = new FileInputStream("test-files/test-archive.ar");
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, testInput);
        Assert.assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();
        testInput.close();
    }

    @Test
    public void testCreateArArchiveOutputStream() throws IOException, ArchiveException {
        final File outputFile = new File(tempDir, "test-archive.ar");
        try (final OutputStream outputStream = new FileOutputStream(outputFile)) {
            final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
            archiveOutputStream.putArchiveEntry(new ArArchiveEntry("testEntry.txt"));
            archiveOutputStream.write(new byte[100], 0, 100);
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            archiveOutputStream.close();
        }

        final InputStream testInput = new FileInputStream(outputFile);
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, testInput);
        Assert.assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();
        testInput.close();
        Files.deleteIfExists(Paths.get(outputFile.getAbsolutePath()));
    }

    @Test
    public void testCreateJarArchiveInputStream() throws IOException, ArchiveException {
        final InputStream testInput = new FileInputStream("test-files/test-archive.jar");
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, testInput);
        Assert.assertTrue(archiveInputStream instanceof JarArchiveInputStream);
        archiveInputStream.close();
        testInput.close();
    }

    @Test
    public void testCreateJarArchiveOutputStream() throws IOException, ArchiveException {
        final File outputFile = new File(tempDir, "test-archive.jar");
        try (final OutputStream outputStream = new FileOutputStream(outputFile)) {
            final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
            archiveOutputStream.putArchiveEntry(new JarArchiveEntry("testEntry.txt"));
            archiveOutputStream.write(new byte[100], 0, 100);
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            archiveOutputStream.close();
        }

        final InputStream testInput = new FileInputStream(outputFile);
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, testInput);
        Assert.assertTrue(archiveInputStream instanceof JarArchiveInputStream);
        archiveInputStream.close();
        testInput.close();
        Files.deleteIfExists(Paths.get(outputFile.getAbsolutePath()));
    }

    @Test
    public void testCreateTarArchiveInputStream() throws IOException, ArchiveException {
        final InputStream testInput = new FileInputStream("test-files/test-archive.tar");
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, testInput);
        Assert.assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        archiveInputStream.close();
        testInput.close();
    }

    @Test
    public void testCreateTarArchiveOutputStream() throws IOException, ArchiveException {
        final File outputFile = new File(tempDir, "test-archive.tar");
        try (final OutputStream outputStream = new FileOutputStream(outputFile)) {
            final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
            archiveOutputStream.putArchiveEntry(new TarArchiveEntry("testEntry.txt"));
            archiveOutputStream.write(new byte[100], 0, 100);
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            archiveOutputStream.close();
        }

        final InputStream testInput = new FileInputStream(outputFile);
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, testInput);
        Assert.assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        archiveInputStream.close();
        testInput.close();
        Files.deleteIfExists(Paths.get(outputFile.getAbsolutePath()));
    }
}