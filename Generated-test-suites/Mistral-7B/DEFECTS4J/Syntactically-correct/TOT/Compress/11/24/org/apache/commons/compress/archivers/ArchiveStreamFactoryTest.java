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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test for ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() {
        // Test with valid archiver name
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream("Zip signature".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, inputStream);
        assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());

        // Test with invalid archiver name
        archiverName = "invalid";
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, inputStream));
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() {
        // Test with valid archiver name
        String archiverName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(archiverName, outputStream);
        assertEquals(ZipArchiveOutputStream.class, archiveOutputStream.getClass());

        // Test with invalid archiver name
        archiverName = "invalid";
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveOutputStream(archiverName, outputStream));
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() {
        // Test with valid input stream
        InputStream inputStream = new ByteArrayInputStream("Zip signature".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(inputStream);
        assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());

        // Test with empty input stream
        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(inputStream);
        assertNull(archiveInputStream);

        // Test with null input stream
        inputStream = null;
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream(inputStream));

        // Test with invalid input stream
        inputStream = new ByteArrayInputStream("Invalid signature".getBytes());
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream(inputStream));
    }

    @Test
    public void testCreateArchiveOutputStreamFromOutputStream() {
        // Test with valid output stream
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream("zip", outputStream);
        assertEquals(ZipArchiveOutputStream.class, archiveOutputStream.getClass());

        // Test with null output stream
        outputStream = null;
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory()
                .createArchiveOutputStream("zip", outputStream));

        // Test with empty output stream
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream("zip", outputStream);
        assertFalse(archiveOutputStream.isOpen());
    }
}