import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {
    private ArchiveStreamFactory archiveStreamFactory;
    private byte[] arSignature = {(byte) 0x78, (byte) 0x41, (byte) 0x53, (byte) 0x70, (byte) 0x45, (byte) 0x54, (byte) 0x4c, (byte) 0x45, (byte) 0x43, (byte) 0x54, (byte) 0x52, (byte) 0x31, (byte) 0x41, (byte) 0x52, (byte) 0x45, (byte) 0x53, (byte) 0x54};
    private byte[] arjSignature = new byte[0];
    private byte[] cpioSignature = {(byte) 0x71, (byte) 0x75, (byte) 0x69, (byte) 0x6f, (byte) 0x74, (byte) 0x65};
    private byte[] dumpSignature = {(byte) 0x64, (byte) 0x6d, (byte) 0x6f, (byte) 0x65, (byte) 0x78, (byte) 0x63, (byte) 0x61, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x73, (byte) 0x74, (byte) 0x72, (byte) 0x75, (byte) 0x6e, (byte) 0x74, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x65};
    private byte[] tarSignature = {(byte) 0x54, (byte) 0x73, (byte) 0x74, (byte) 0x65, (byte) 0x78, (byte) 0x63, (byte) 0x72, (byte) 0x65, (byte) 0x20, (byte) 0x54, (byte) 0x68, (byte) 0x69, (byte) 0x73, (byte) 0x20, (byte) 0x6d, (byte) 0x61, (byte) 0x69, (byte) 0x6e, (byte) 0x69, (byte) 0x7a, (byte) 0x65, (byte) 0x64};
    private byte[] zipSignature = {(byte) 0x50, (byte) 0x4b, (byte) 0x03, (byte) 0x04};

    @Before
    public void setUp() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertNotNull(archiveOutputStream);
        assertEquals("tar", archiveOutputStream.getFormatName());
        IOUtils.closeQuietly(archiveOutputStream);

        archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(archiveOutputStream);
        assertEquals("zip", archiveOutputStream.getFormatName());
        IOUtils.closeQuietly(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveInputStream_knownFormat() throws IOException {
        InputStream arInputStream = new ByteArrayInputStream(arSignature);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertNotNull(archiveInputStream);
        assertEquals("ar", archiveInputStream.getFormatName());
        IOUtils.closeQuietly(archiveInputStream);

        InputStream cpioInputStream = new ByteArrayInputStream(cpioSignature);
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        assertNotNull(archiveInputStream);
        assertEquals("cpio", archiveInputStream.getFormatName());
        IOUtils.closeQuietly(archiveInputStream);

        InputStream dumpInputStream = new ByteArrayInputStream(dumpSignature);
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        assertNotNull(archiveInputStream);
        assertEquals("dump", archiveInputStream.getFormatName());
        IOUtils.closeQuietly(archiveInputStream);

        InputStream tarInputStream = new ByteArrayInputStream(tarSignature);
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        assertNotNull(archiveInputStream);
        assertEquals("tar", archiveInputStream.getFormatName());
        IOUtils.closeQuietly(archiveInputStream);

        InputStream zipInputStream = new ByteArrayInputStream(zipSignature);
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertNotNull(archiveInputStream);
        assertEquals("zip", archiveInputStream.getFormatName());
        IOUtils.closeQuietly(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_unknownFormat() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ARJ, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_arjFormat() throws IOException {
        InputStream arjInputStream = new ByteArrayInputStream(arjSignature);
        ArchiveInputStream archiveInputStream = null;
        try {
            archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ARJ, arjInputStream);
            assertNotNull(archiveInputStream);
            assertEquals("arj", archiveInputStream.getFormatName());
        } finally {
            IOUtils.closeQuietly(archiveInputStream);
        }
    }

    @Test
    public void testCreateArchiveInputStream_autodetection() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(arSignature);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertEquals("ar", archiveInputStream.getFormatName());
        IOUtils.closeQuietly(archiveInputStream);

        inputStream = new ByteArrayInputStream(tarSignature);
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertEquals("tar", archiveInputStream.getFormatName());
        IOUtils.closeQuietly(archiveInputStream);

        inputStream = new ByteArrayInputStream(zipSignature);
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertEquals("zip", archiveInputStream.getFormatName());
        IOUtils.closeQuietly(archiveInputStream);

        inputStream = new ByteArrayInputStream(arSignature);
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream, "test.tar");
        assertNotNull(archiveInputStream);
        assertEquals("tar", archiveInputStream.getFormatName());
        IOUtils.closeQuietly(archiveInputStream);

        inputStream = new ByteArrayInputStream(tarSignature);
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream, "test.tar.gz");
        assertNotNull(archiveInputStream);
        assertEquals("tar.gz", archiveInputStream.getFormatName());
        IOUtils.closeQuietly(archiveInputStream);

        inputStream = new ByteArrayInputStream(zipSignature);
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream, "test.zip.gz");
        assertNotNull(archiveInputStream);
        assertEquals("gzip", archiveInputStream.getFormatName());
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiveInputStream);
        assertNotNull(archiveInputStream);
        assertEquals("zip", archiveInputStream.getFormatName());
        IOUtils.closeQuietly(archiveInputStream);
    }
}