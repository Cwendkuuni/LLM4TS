import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test with known archive names
        InputStream in = new ByteArrayInputStream("archiver-magic-bytes".getBytes());
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream("tar", in);
        assertTrue(ais instanceof TarArchiveInputStream);
        ais.close();

        in = new ByteArrayInputStream("META-INF/MANIFEST.MF".getBytes());
        ais = new ArchiveStreamFactory().createArchiveInputStream("jar", in);
        assertTrue(ais instanceof JarArchiveInputStream);
        ais.close();

        // Test with unknown archive name
        in = new ByteArrayInputStream("archiver-magic-bytes".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream("unknown", in);
            fail();
        } catch (ArchiveException e) {
            // expected
        }

        // Test with null input stream
        try {
            new ArchiveStreamFactory().createArchiveInputStream("tar", null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test with invalid input stream
        InputStream is = new ByteArrayInputStream(new byte[0]);
        is.mark(1);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("tar", is);
            fail();
        } catch (IOException e) {
            // expected
        }
        is.reset();
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test with known archive names
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream oos = new ArchiveStreamFactory().createArchiveOutputStream("tar", out);
        assertTrue(oos instanceof TarArchiveOutputStream);
        oos.close();

        out = new ByteArrayOutputStream();
        oos = new ArchiveStreamFactory().createArchiveOutputStream("jar", out);
        assertTrue(oos instanceof JarArchiveOutputStream);
        oos.close();

        // Test with unknown archive name
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("unknown", new ByteArrayOutputStream());
            fail();
        } catch (ArchiveException e) {
            // expected
        }

        // Test with null output stream
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("tar", null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        // Test with known archive names
        InputStream in = new ByteArrayInputStream("archiver-magic-bytes".getBytes());
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertTrue(ais instanceof TarArchiveInputStream);
        ais.close();

        in = new ByteArrayInputStream("META-INF/MANIFEST.MF".getBytes());
        ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertTrue(ais instanceof JarArchiveInputStream);
        ais.close();

        // Test with unknown archive name
        InputStream is = new ByteArrayInputStream("archiver-magic-bytes".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream(is, "unknown");
            fail();
        } catch (ArchiveException e) {
            // expected
        }

        is.reset();

        // Test with null input stream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null, "tar");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        is.mark(1);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(is, "tar");
            fail();
        } catch (IOException e) {
            // expected
        }
        is.reset();
    }

    @Test
    public void testCreateArchiveOutputStreamFromStream() throws Exception {
        // Test with known archive names
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream oos = new ArchiveStreamFactory().createArchiveOutputStream("tar", out);
        assertTrue(oos instanceof TarArchiveOutputStream);
        oos.close();

        out = new ByteArrayOutputStream();
        oos = new ArchiveStreamFactory().createArchiveOutputStream("jar", out);
        assertTrue(oos instanceof JarArchiveOutputStream);
        oos.close();

        // Test with unknown archive name
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(new ByteArrayOutputStream(), "unknown");
            fail();
        } catch (ArchiveException e) {
            // expected
        }

        // Test with null output stream
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(null, "tar");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}