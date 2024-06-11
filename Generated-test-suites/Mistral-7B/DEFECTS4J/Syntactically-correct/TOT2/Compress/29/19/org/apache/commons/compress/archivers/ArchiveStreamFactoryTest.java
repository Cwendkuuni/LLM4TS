import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;
    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test with valid parameters
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();

        // Test with null archiverName
        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = factory.createArchiveInputStream(null, inputStream);
        Assert.assertNull(archiveInputStream);

        // Test with null inputStream
        archiverName = "zip";
        archiveInputStream = factory.createArchiveInputStream(archiverName, null);
        Assert.assertNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test with valid parameters
        String archiverName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        archiveOutputStream.finish();
        ArchiveEntry entry = new JarArchiveEntry("testEntry.txt");
        archiveOutputStream.putNextEntry(entry);
        IOUtils.write("Test entry content.", archiveOutputStream);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.close();

        // Test with null archiverName
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream(null, outputStream);
        Assert.assertNull(archiveOutputStream);

        // Test with null outputStream
        archiverName = "zip";
        archiveOutputStream = factory.createArchiveOutputStream(archiverName, null);
        Assert.assertNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Test with a valid ZIP archive
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("zip", outputStream);
        JarArchiveEntry entry = new JarArchiveEntry("testEntry.txt");
        archiveOutputStream.putNextEntry(entry);
        IOUtils.write("Test entry content.", archiveOutputStream);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("zip", testInputStream);
        Assert.assertNotNull(archiveInputStream);
        JarArchiveEntry testEntry = (JarArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertEquals("testEntry.txt", testEntry.getName());
        Assert.assertEquals("Test entry content.", IOUtils.toString(archiveInputStream));
        archiveInputStream.close();

        // Test with an invalid archive format
        ByteArrayOutputStream outputStreamInvalid = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStreamInvalid = factory.createArchiveOutputStream("invalidFormat", outputStreamInvalid);
        archiveOutputStreamInvalid.close();
        InputStream testInputStreamInvalid = new ByteArrayInputStream(outputStreamInvalid.toByteArray());
        ArchiveInputStream archiveInputStreamInvalid = factory.createArchiveInputStream("invalidFormat", testInputStreamInvalid);
        Assert.assertNull(archiveInputStreamInvalid);

        // Test with a null input stream
        ArchiveInputStream archiveInputStreamNull = factory.createArchiveInputStream("zip", null);
        Assert.assertNull(archiveInputStreamNull);
    }

    @Test
    public void testCreateArchiveOutputStreamFromOutputStream() throws Exception {
        // Test with a valid ZIP archive
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("zip", outputStream);
        archiveOutputStream.putArchiveEntry(new JarArchiveEntry("testEntry.txt"));
        IOUtils.copy(inputStream, archiveOutputStream);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("zip", testInputStream);
        JarArchiveEntry testEntry = (JarArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertEquals("testEntry.txt", testEntry.getName());
        Assert.assertEquals("Test entry content.", IOUtils.toString(archiveInputStream));
        archiveInputStream.close();

        // Test with a null output stream
        ArchiveOutputStream archiveOutputStreamNull = factory.createArchiveOutputStream("zip", null);
        Assert.assertNull(archiveOutputStreamNull);
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidArchive() throws Exception {
        // Test with an empty input stream
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("invalidFormat", inputStream);
        Assert.assertNull(archiveInputStream);

        // Test with a malformed archive
        ByteArrayOutputStream outputStreamMalformed = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStreamMalformed = factory.createArchiveOutputStream("malformedFormat", outputStreamMalformed);
        archiveOutputStreamMalformed.putNextEntry(new JarArchiveEntry("testEntry.txt"));
        Random random = new Random();
        byte[] testData = new byte[512];
        random.nextBytes(testData);
        archiveOutputStreamMalformed.write(testData, 0, testData.length);
        archiveOutputStreamMalformed.closeArchiveEntry();
        archiveOutputStreamMalformed.finish();
        archiveOutputStreamMalformed.close();
        InputStream testInputStreamMalformed = new ByteArrayInputStream(outputStreamMalformed.toByteArray());
        ArchiveInputStream archiveInputStreamMalformed = factory.createArchiveInputStream("malformedFormat", testInputStreamMalformed);
        Assert.assertNull(archiveInputStreamMalformed);
    }

    @Test
    public void testCreateArchiveOutputStreamWithInvalidArchive() throws Exception {
        // Test with an empty output stream
        OutputStream outputStreamEmpty = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStreamEmpty = factory.createArchiveOutputStream("zip", outputStreamEmpty);
        Assert.assertNull(archiveOutputStreamEmpty);

        // Test with a malformed archive
        ByteArrayInputStream inputStreamMalformed = new ByteArrayInputStream(new byte[0]);
        OutputStream outputStreamMalformed = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStreamMalformed = factory.createArchiveOutputStream("malformedFormat", outputStreamMalformed);
        archiveOutputStreamMalformed.putArchiveEntry(new JarArchiveEntry("testEntry.txt"));
        Random random = new Random();
        byte[] testData = new byte[512];
        random.nextBytes(testData);
        archiveOutputStreamMalformed.writeTo(testData, 0, testData.length);
        archiveOutputStreamMalformed.closeArchiveEntry();
        archiveOutputStreamMalformed.finish();
        archiveOutputStreamMalformed.close();
        InputStream testInputStreamMalformed = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStreamMalformed = factory.createArchiveInputStream("malformedFormat", testInputStreamMalformed);
        Assert.assertNull(archiveInputStreamMalformed);
    }
}