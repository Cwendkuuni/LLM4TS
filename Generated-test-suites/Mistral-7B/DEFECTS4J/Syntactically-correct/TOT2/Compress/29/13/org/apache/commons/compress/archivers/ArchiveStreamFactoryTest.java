import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for {@link ArchiveStreamFactory}
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Create ArchiveInputStream from a known archive format and input stream
        String archiverName = "zip";
        InputStream in = new ByteArrayInputStream(new byte[0]); // empty byte array as a test
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);

        // Basic functionality test
        Assert.assertNotNull(ais);
        ArchiveEntry entry = ais.getNextEntry();
        Assert.assertNull(entry);

        // Edge case testing - empty input stream
        in = new ByteArrayInputStream(new byte[0]);
        ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        Assert.assertNotNull(ais);
        entry = ais.getNextEntry();
        Assert.assertNull(entry);

        // Exception handling testing - unknown archiver format
        ais.close();
        InputStream unknownFormatInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveException exception = null;
        try {
            new ArchiveStreamFactory().createArchiveInputStream("UnknownFormat", unknownFormatInputStream);
        } catch (ArchiveException e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Create ArchiveOutputStream from a known archive format and output stream
        String archiverName = "zip";
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);

        // Basic functionality test
        Assert.assertNotNull(aos);
        aos.putArchiveEntry(new JarArchiveEntry("testEntry.txt"));
        aos.write("Test content".getBytes());
        aos.finishStream();

        // Edge case testing - empty output stream
        ByteArrayOutputStream emptyOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream emptyArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, emptyOutputStream);
        emptyArchiveOutputStream.close();

        // Exception handling testing - unknown archiver format
        aos.close();
        OutputStream unknownFormatOutputStream = new ByteArrayOutputStream();
        ArchiveException exception = null;
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("UnknownFormat", unknownFormatOutputStream);
        } catch (ArchiveException e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Create ArchiveInputStream from an unknown archive format and input stream
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream("UnknownFormat", in);

        // Exception handling testing - unknown archiver format
        ArchiveException exception = null;
        try {
            ais.getNextEntry();
        } catch (ArchiveException e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
    }

    @Test
    public void testCreateArchiveOutputStreamFromOutputStream() throws Exception {
        // Create ArchiveOutputStream from an unknown archive format and output stream
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream("UnknownFormat", out);

        // Exception handling testing - unknown archiver format
        ArchiveException exception = null;
        try {
            aos.putNextEntry(new JarArchiveEntry("testEntry.txt"));
        } catch (ArchiveException e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
    }
}