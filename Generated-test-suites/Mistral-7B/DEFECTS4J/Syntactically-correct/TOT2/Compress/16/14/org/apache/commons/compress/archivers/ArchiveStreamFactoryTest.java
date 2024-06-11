import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test cases for ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    private static final String AR_TEST_FILE = "test.ar";
    private static final String JAR_TEST_FILE = "test.jar";
    private static final String ZIP_TEST_FILE = "test.zip";

    /**
     * Test creating archive input stream from known archiver name and input stream.
     */
    @Test
    public void testCreateArchiveInputStreamWithKnownFormat() throws IOException, ArchiveException {
        // Test AR format
        InputStream arInputStream = new ByteArrayInputStream(AR_TEST_FILE.getBytes());
        ArchiveInputStream arArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        Assert.assertNotNull(arArchiveInputStream);
        ArArchiveInputStream arArInput = (ArArchiveInputStream) arArchiveInputStream;
        Assert.assertEquals(AR, arArInput.getFormat());

        // Test JAR format
        InputStream jarInputStream = new ByteArrayInputStream(JAR_TEST_FILE.getBytes());
        ArchiveInputStream jarArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        Assert.assertNotNull(jarArchiveInputStream);
        JarArchiveInputStream jarArchiveInput = (JarArchiveInputStream) jarArchiveInputStream;
        Assert.assertEquals(JAR, jarArchiveInput.getFormat());

        // Test ZIP format
        InputStream zipInputStream = new ByteArrayInputStream(ZIP_TEST_FILE.getBytes());
        ArchiveInputStream zipArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        Assert.assertNotNull(zipArchiveInputStream);
        ZipArchiveInputStream zipArchiveInput = (ZipArchiveInputStream) zipArchiveInputStream;
        Assert.assertEquals(ZIP, zipArchiveInput.getFormat());

        IOUtils.closeQuietly(arArchiveInputStream);
        IOUtils.closeQuietly(jarArchiveInputStream);
        IOUtils.closeQuietly(zipArchiveInputStream);
    }

    /**
     * Test creating archive input stream from unknown archiver name and input stream.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownFormat() throws IOException {
        InputStream unknownFormatInputStream = new ByteArrayInputStream("Unknown format".getBytes());
        ArchiveStreamFactory archiveStreamFactory = ArchiveStreamFactory.newInstance();
        archiveStreamFactory.createArchiveInputStream("unknown", unknownFormatInputStream);
    }

    /**
     * Test creating archive output stream from known archiver name and output stream.
     */
    @Test
    public void testCreateArchiveOutputStreamWithKnownFormat() throws IOException, ArchiveException {
        // Test AR format
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        Assert.assertNotNull(arArchiveOutputStream);
        ArArchiveOutputStream arArOutput = (ArArchiveOutputStream) arArchiveOutputStream;
        Assert.assertEquals(AR, arArOutput.getFormat());

        // Test JAR format
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        Assert.assertNotNull(jarArchiveOutputStream);
        JarArchiveOutputStream jarArchiveOutput = (JarArchiveOutputStream) jarArchiveOutputStream;
        Assert.assertEquals(JAR, jarArchiveOutput.getFormat());

        // Test ZIP format
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        Assert.assertNotNull(zipArchiveOutputStream);
        ZipArchiveOutputStream zipArchiveOutput = (ZipArchiveOutputStream) zipArchiveOutputStream;
        Assert.assertEquals(ZIP, zipArchiveOutput.getFormat());

        IOUtils.closeQuietly(arArchiveOutputStream);
        IOUtils.closeQuietly(jarArchiveOutputStream);
        IOUtils.closeQuietly(zipArchiveOutputStream);
    }

    /**
     * Test creating archive output stream from unknown archiver name and output stream.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownFormat() throws IOException {
        OutputStream unknownFormatOutputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory archiveStreamFactory = ArchiveStreamFactory.newInstance();
        archiveStreamFactory.createArchiveOutputStream("unknown", unknownFormatOutputStream);
    }

    /**
     * Test creating archive input stream from input stream with unknown format.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownFormatFromInputStream() throws IOException {
        InputStream unknownFormatInputStream = new ByteArrayInputStream("Unknown format".getBytes());
        ArchiveStreamFactory archiveStreamFactory = ArchiveStreamFactory.newInstance();
        archiveStreamFactory.createArchiveInputStream(unknownFormatInputStream);
    }
}