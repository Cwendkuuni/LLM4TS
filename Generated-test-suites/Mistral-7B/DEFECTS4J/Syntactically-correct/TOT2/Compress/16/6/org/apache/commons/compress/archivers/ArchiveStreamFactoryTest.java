import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.Test;

import java.io.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test creating an archive input stream for a known archive type using a name
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        ArchiveInputStream arInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(arInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, arInputStream);

        // Test creating an archive input stream for an unknown archive type using a name
        in = new ByteArrayInputStream("test data".getBytes());
        assertThrows(ArchiveException.class, () -> {
            ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", in);
        });

        // Test creating an archive input stream for a known archive type using an InputStream
        in = new ByteArrayInputStream("test data".getBytes());
        ArchiveInputStream arInputStream2 = ArchiveStreamFactory.getInstance().createArchiveInputStream(in);
        assertNotNull(arInputStream2);
        assertInstanceOf(ZipArchiveInputStream.class, arInputStream2);

        // Test creating an archive input stream for an unknown archive type using an InputStream (auto-detection)
        InputStream unknownStream = new ByteArrayInputStream("test data".getBytes());
        unknownStream.mark(unknownStream.available());
        byte[] unknownSignature = new byte[unknownStream.available()];
        int readBytes = unknownStream.read(unknownSignature);
        unknownStream.reset();
        ArchiveInputStream unknownArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(unknownStream);
        assertNull(unknownArchiveInputStream);

        // Test creating an archive input stream from a null InputStream
        assertThrows(IllegalArgumentException.class, () -> {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(null);
        });
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test creating an archive output stream for a known archive type using a name and an OutputStream
        OutputStream os = new ByteArrayOutputStream();
        ArchiveOutputStream arOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, os);
        assertNotNull(arOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, arOutputStream);

        // Test creating an archive output stream for a known archive type using a name
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        assertNotNull(outputStream);
        assertInstanceOf(ArArchiveOutputStream.class, outputStream);

        // Test creating an archive output stream for an unknown archive type using a name
        OutputStream output2 = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknown", output2);
        });

        // Test creating an archive output stream for a known archive type using an OutputStream
        OutputStream output3 = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream2 = ArchiveStreamFactory.getInstance().createArchiveOutputStream(output3);
        assertNotNull(outputStream2);
        assertInstanceOf(ArArchiveOutputStream.class, outputStream2);

        // Test creating an archive output stream for an unknown archive type using an OutputStream (auto-detection)
        OutputStream output4 = new ByteArrayOutputStream();
        output4.write("test data".getBytes());
        output4.mark(output4.size());
        ArchiveOutputStream outputStream3 = ArchiveStreamFactory.getInstance().createArchiveOutputStream(output4);
        assertNull(outputStream3);

        // Test creating an archive output stream from a null OutputStream
        assertThrows(IllegalArgumentException.class, () -> {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(null);
        });
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Test creating an archive input stream for a known archive type using an InputStream
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        ArchiveInputStream arInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(input);
        assertNotNull(arInputStream);

        // Test creating an archive input stream for an unknown archive type using an InputStream (auto-detection)
        InputStream input2 = new ByteArrayInputStream("test data".getBytes());
        assertThrows(ArchiveException.class, () -> {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(input2);
        });

        // Test creating an archive input stream from a null InputStream
        assertThrows(IllegalArgumentException.class, () -> {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(null);
        });
    }

    @Test
    public void testGetInstance() throws Exception {
        ArchiveStreamFactory factory1 = ArchiveStreamFactory.getInstance();
        ArchiveStreamFactory factory2 = ArchiveStreamFactory.getInstance();
        assertEquals(factory1, factory2);
    }
}