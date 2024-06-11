import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        String archiverName = "ZIP";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(ArchiveInputStream.class, archiveInputStream.getClass());
        if (ZipArchiveInputStream.class.isInstance(archiveInputStream)) {
            ZipArchiveInputStream zipArchiveInputStream = (ZipArchiveInputStream) archiveInputStream;
            assertEquals(ArchiveInputStream.END_OF_ARCHIVE, zipArchiveInputStream.getNextEntry());
        }
        inputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_AR() throws Exception {
        String archiverName = "AR";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(ArArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws Exception {
        String archiverName = "CPIO";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(CpioArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_Dump() throws Exception {
        String archiverName = "DUMP";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(DumpArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws Exception {
        String archiverName = "JAR";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(JarArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_TAR() throws Exception {
        String archiverName = "TAR";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(TarArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_invalidFormat() throws Exception {
        String archiverName = "INVALID";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
            fail("ArchiveException should have been thrown");
        } catch (ArchiveException e) {
            // Expected
        }
        inputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        String archiverName = "ZIP";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
        assertEquals(ArchiveOutputStream.class, archiveOutputStream.getClass());
        archiveOutputStream.finish();
        outputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws Exception {
        String archiverName = "AR";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
        assertEquals(ArArchiveOutputStream.class, archiveOutputStream.getClass());
        archiveOutputStream.finish();
        outputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() throws Exception {
        String archiverName = "CPIO";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream
                = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
        assertEquals(CpioArchiveOutputStream.class, archiveOutputStream.getClass());
        archiveOutputStream.finish();
        outputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Dump() throws Exception {
        String archiverName = "DUMP";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
        assertEquals(DumpArchiveOutputStream.class, archiveOutputStream.getClass());
        archiveOutputStream.finish();
        outputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() throws Exception {
        String archiverName = "JAR";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
        assertEquals(JarArchiveOutputStream.class, archiveOutputStream.getClass());
        archiveOutputStream.finish();
        outputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() throws Exception {
        String archiverName = "TAR";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
        assertEquals(TarArchiveOutputStream.class, archiveOutputStream.getClass());
        archiveOutputStream.finish();
        outputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_invalidFormat() throws Exception {
        String archiverName = "INVALID";
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
            fail("ArchiveException should have been thrown");
        } catch (ArchiveException e) {
            // Expected
        }
        outputStream.close();
    }
}