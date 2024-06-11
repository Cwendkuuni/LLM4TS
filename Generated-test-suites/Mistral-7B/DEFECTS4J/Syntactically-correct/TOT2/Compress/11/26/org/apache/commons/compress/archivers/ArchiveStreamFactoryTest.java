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

import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test with known archive types and correct input streams
        InputStream zipInputStream = new ByteArrayInputStream(ZipArchiveInputStream.TEST_DATA);
        ArchiveInputStream zipArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertNotNull(zipArchiveInputStream);

        InputStream jarInputStream = new ByteArrayInputStream(JarArchiveInputStream.TEST_DATA);
        ArchiveInputStream jarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        assertNotNull(jarArchiveInputStream);

        InputStream arInputStream = new ByteArrayInputStream(ArArchiveInputStream.TEST_DATA);
        ArchiveInputStream arArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertNotNull(arArchiveInputStream);

        InputStream cpioInputStream = new ByteArrayInputStream(CpioArchiveInputStream.TEST_DATA);
        ArchiveInputStream cpioArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        assertNotNull(cpioArchiveInputStream);

        InputStream dumpInputStream = new ByteArrayInputStream(DumpArchiveInputStream.TEST_DATA);
        ArchiveInputStream dumpArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        assertNotNull(dumpArchiveInputStream);

        InputStream tarInputStream = new ByteArrayInputStream(TarArchiveInputStream.TEST_DATA);
        ArchiveInputStream tarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        assertNotNull(tarArchiveInputStream);

        // Test with unknown archive type
        InputStream unknownInputStream = new ByteArrayInputStream("Unknown archive data".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream("Unknown", unknownInputStream);
            // This point should not reach
            throw new AssertionError("Unexpected success");
        } catch (ArchiveException e) {
            // Expected
        }

        // Test with incorrect input stream (not an archive)
        InputStream nonArchiveInputStream = new ByteArrayInputStream("Non-archive data".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream("Unknown", nonArchiveInputStream);
            // This point should not reach
            throw new AssertionError("Unexpected success");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test with known archive types and correct output streams
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        zipArchiveOutputStream.finish();
        assertNotNull(zipArchiveOutputStream);

        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        jarArchiveOutputStream.finish();
        assertNotNull(jarArchiveOutputStream);

        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        arArchiveOutputStream.finish();
        assertNotNull(arArchiveOutputStream);

        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        cpioArchiveOutputStream.finish();
        assertNotNull(cpioArchiveOutputStream);

        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream dumpArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
        dumpArchiveOutputStream.finish();
        assertNotNull(dumpArchiveOutputStream);

        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        tarArchiveOutputStream.finish();
        assertNotNull(tarArchiveOutputStream);

        // Test with unknown archive type
        OutputStream unknownOutputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("Unknown", unknownOutputStream);
            // This point should not reach
            throw new AssertionError("Unexpected success");
        } catch (ArchiveException e) {
            // Expected
        }

        // Test with incorrect output stream (not writable)
        OutputStream nonWritableOutputStream = new ByteArrayOutputStream() {
            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                // Do nothing
            }
        };
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("Unknown", nonWritableOutputStream);
            // This point should not reach
            throw new AssertionError("Unexpected success");
        } catch (ArchiveException e) {
            // Expected
        }
    }
}