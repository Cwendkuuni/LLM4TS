import org.apache.commons.compress.archivers.*;
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

import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test createArchiveInputStream with known archives
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("zip", inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("jar", inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("tar", inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("ar", inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("cpio", inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("dump", inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        // Test createArchiveInputStream with unknown archives
        inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveStreamFactory.newInstance().createArchiveInputStream("unknown", inputStream);
            assert false; // should throw exception
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test createArchiveOutputStream with known archives
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("zip", outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("jar", outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("tar", outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("ar", outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("cpio", outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("dump", outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        // Test createArchiveOutputStream with unknown archives
        outputStream = new ByteArrayOutputStream();
        try {
            ArchiveStreamFactory.newInstance().createArchiveOutputStream("unknown", outputStream);
            assert false; // should throw exception
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        // Test createArchiveInputStreamFromStream with known archives
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("zip", inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("jar", inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("tar", inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("ar", inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("cpio", inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("dump", inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();

        // Test createArchiveInputStreamFromStream with unknown archives
        inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveStreamFactory.newInstance().createArchiveInputStream(inputStream, "unknown");
            assert false; // should throw exception
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromStream() throws Exception {
        // Test createArchiveOutputStreamFromStream with known archives
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("zip", outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("jar", outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("tar", outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("ar", outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("cpio", outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("dump", outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        // Test createArchiveOutputStreamFromStream with unknown archives
        OutputStream output = new ByteArrayOutputStream();
        try {
            ArchiveStreamFactory.newInstance().createArchiveOutputStream(output, "unknown");
            assert false; // should throw exception
        } catch (ArchiveException e) {
            // expected
        }
    }
}