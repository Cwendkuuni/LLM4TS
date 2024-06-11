import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test with valid archives
        InputStream testStream = new ByteArrayInputStream(new byte[12]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream("zip", testStream);
        assertNotNull(ais);
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[24]);
        ais = new ArchiveStreamFactory().createArchiveInputStream("tar", testStream);
        assertNotNull(ais);
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[6]);
        ais = new ArchiveStreamFactory().createArchiveInputStream("ar", testStream);
        assertNotNull(ais);
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[8]);
        ais = new ArchiveStreamFactory().createArchiveInputStream("cpio", testStream);
        assertNotNull(ais);
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[4]);
        ais = new ArchiveStreamFactory().createArchiveInputStream("jar", testStream);
        assertNotNull(ais);
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[10]);
        ais = new ArchiveStreamFactory().createArchiveInputStream("dump", testStream);
        assertNotNull(ais);
        testStream.close();

        // Test with invalid archives
        testStream = new ByteArrayInputStream(new byte[12]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("sevenz", testStream);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[1]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("arj", testStream);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[1]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("seventz", testStream);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[1]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("invalid", testStream);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
        testStream.close();

        // Test with null InputStream
        testStream = null;
        try {
            new ArchiveStreamFactory().createArchiveInputStream("zip", testStream);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test with valid archivers
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream("zip", out);
        assertNotNull(aos);
        aos.close();

        out = new ByteArrayOutputStream();
        aos = new ArchiveStreamFactory().createArchiveOutputStream("tar", out);
        assertNotNull(aos);
        aos.close();

        out = new ByteArrayOutputStream();
        aos = new ArchiveStreamFactory().createArchiveOutputStream("ar", out);
        assertNotNull(aos);
        aos.close();

        out = new ByteArrayOutputStream();
        aos = new ArchiveStreamFactory().createArchiveOutputStream("cpio", out);
        assertNotNull(aos);
        aos.close();

        out = new ByteArrayOutputStream();
        aos = new ArchiveStreamFactory().createArchiveOutputStream("jar", out);
        assertNotNull(aos);
        aos.close();

        // Test with invalid archivers
        out = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("sevenz", out);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
        out.close();

        out = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("arj", out);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
        out.close();

        out = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("seventz", out);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
        out.close();

        out = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("invalid", out);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
        out.close();

        // Test with null OutputStream
        out = null;
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("zip", out);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testDetectArchiveFormat() throws Exception {
        InputStream testStream = new ByteArrayInputStream(new byte[12]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(testStream);
        assertNotNull(ais);
        String format = ais.getFormat();
        assertEquals("zip", format);
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[24]);
        ais = new ArchiveStreamFactory().createArchiveInputStream(testStream);
        assertNotNull(ais);
        format = ais.getFormat();
        assertEquals("tar", format);
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[6]);
        ais = new ArchiveStreamFactory().createArchiveInputStream(testStream);
        assertNotNull(ais);
        format = ais.getFormat();
        assertEquals("ar", format);
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[8]);
        ais = new ArchiveStreamFactory().createArchiveInputStream(testStream);
        assertNotNull(ais);
        format = ais.getFormat();
        assertEquals("cpio", format);
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[4]);
        ais = new ArchiveStreamFactory().createArchiveInputStream(testStream);
        assertNotNull(ais);
        format = ais.getFormat();
        assertEquals("jar", format);
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[10]);
        ais = new ArchiveStreamFactory().createArchiveInputStream(testStream);
        assertNotNull(ais);
        format = ais.getFormat();
        assertEquals("dump", format);
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[12]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(testStream);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[1]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("arj", testStream);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[1]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("seventz", testStream);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
        testStream.close();

        testStream = new ByteArrayInputStream(new byte[1]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("invalid", testStream);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
        testStream.close();

        // Test with null InputStream
        testStream = null;
        try {
            new ArchiveStreamFactory().createArchiveInputStream(testStream);
            fail();
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testArchiveInputStreamClose() throws Exception {
        InputStream testStream = new ByteArrayInputStream(new byte[12]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream("zip", testStream);
        try {
            ZipArchiveEntry entry = ais.getNextEntry();
            IOUtils.copy(ais, new ByteArrayOutputStream());
            ais.close();
            fail();
        } catch (IOException e) {
            // Expected
        }
        testStream.close();
    }

    @Test
    public void testArchiveOutputStreamPutArchiveEntry() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream("zip", out);
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        aos.putArchiveEntry(entry);
        aos.close();

        InputStream is = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream("zip", is);
        ZipArchiveEntry entry2 = (ZipArchiveEntry) ais.getNextEntry();
        assertEquals("test.txt", entry2.getName());
        ais.close();
        is.close();
    }

    @Test
    public void testArchiveOutputStreamCloseArchiveEntry() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream("zip", out);
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        aos.putArchiveEntry(entry);
        try {
            aos.closeArchiveEntry();
            fail();
        } catch (IOException e) {
            // Expected
        }
        aos.close();
    }

}