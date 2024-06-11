import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Tests for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;
    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
    }

    /**
     * Test creating an archive input stream from a valid archive name and input stream.
     */
    @Test
    public void testCreateArchiveInputStreamFromValidArchiveNameAndInputStream() throws ArchiveException {
        inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        // Perform tests on the archiveInputStream
        archiveInputStream.close();
    }

    /**
     * Test creating an archive input stream from an unknown archive name and input stream.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromUnknownArchiveNameAndInputStream() throws ArchiveException {
        inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("unknownformat", inputStream);
    }

    /**
     * Test creating an archive input stream from a stream with an invalid signature.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromInputStreamWithInvalidSignature() throws ArchiveException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = new ByteArrayOutputStream();
        byte[] signature = new byte[12];
        os.write(signature);
        os.close();
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(is);
        archiveInputStream.close();
    }

    /**
     * Test creating an archive output stream from a valid archive name and output stream.
     */
    @Test
    public void testCreateArchiveOutputStreamFromValidArchiveNameAndOutputStream() throws ArchiveException, IOException {
        outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        // Perform tests on the archiveOutputStream
        archiveOutputStream.close();
    }

    /**
     * Test creating an archive output stream from an unknown archive name and output stream.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromUnknownArchiveNameAndOutputStream() throws ArchiveException {
        outputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknownformat", outputStream);
    }

    /**
     * Test creating an archive input stream from an input stream using autodetection.
     */
    @Test
    public void testCreateArchiveInputStreamFromInputStreamUsingAutodetection() throws ArchiveException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = new ByteArrayOutputStream();
        byte[] signature = new byte[512];
        os.write(signature);
        os.close();
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(is);
        Assert.assertEquals("tar", archiveInputStream.getFormat().getName());
        archiveInputStream.close();
    }
}