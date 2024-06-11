import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers. JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @BeforeEach
    public void setup() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException, IOException {
        // Test AR
        InputStream arInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream arArchiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        Assertions.assertInstanceOf(ArArchiveInputStream.class, arArchiveInputStream);
        arArchiveInputStream.close();

        // Test CPIO
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream cpioArchiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        Assertions.assertInstanceOf(CpioArchiveInputStream.class, cpioArchiveInputStream);
        cpioArchiveInputStream.close();

        // Test Dump
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[32]);
        ArchiveInputStream dumpArchiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        Assertions.assertInstanceOf(DumpArchiveInputStream.class, dumpArchiveInputStream);
        dumpArchiveInputStream.close();

        // Test JAR
        InputStream jarInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream jarArchiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        Assertions.assertInstanceOf(JarArchiveInputStream.class, jarArchiveInputStream);
        jarArchiveInputStream.close();

        // Test TAR
        InputStream tarInputStream = new ByteArrayInputStream(new byte[512]);
        ArchiveInputStream tarArchiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        Assertions.assertInstanceOf(TarArchiveInputStream.class, tarArchiveInputStream);
        tarArchiveInputStream.close();

        // Test ZIP
        InputStream zipInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream zipArchiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        Assertions.assertInstanceOf(ZipArchiveInputStream.class, zipArchiveInputStream);
        zipArchiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
        // Test AR
        OutputStream arOutputStream = new ByteArrayOutputStream(1024);
        ArchiveOutputStream arArchiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        arArchiveOutputStream.finish();
        arArchiveOutputStream.close();
        Assertions.assertInstanceOf(ArArchiveOutputStream.class, arArchiveOutputStream);

        // Test CPIO
        OutputStream cpioOutputStream = new ByteArrayOutputStream(1024);
        ArchiveOutputStream cpioArchiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();
        Assertions.assertInstanceOf(CpioArchiveOutputStream.class, cpioArchiveOutputStream);

        // Test Dump
        OutputStream dumpOutputStream = new ByteArrayOutputStream(32);
        ArchiveOutputStream dumpArchiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
        dumpArchiveOutputStream.finish();
        dumpArchiveOutputStream.close();
        Assertions.assertInstanceOf(DumpArchiveOutputStream.class, dumpArchiveOutputStream);

        // Test JAR
        OutputStream jarOutputStream = new ByteArrayOutputStream(1024);
        ArchiveOutputStream jarArchiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        jarArchiveOutputStream.finish();
        jarArchiveOutputStream.close();
        Assertions.assertInstanceOf(JarArchiveOutputStream.class, jarArchiveOutputStream);

        // Test TAR
        OutputStream tarOutputStream = new ByteArrayOutputStream(512);
        ArchiveOutputStream tarArchiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        Assertions.assertInstanceOf(TarArchiveOutputStream.class, tarArchiveOutputStream);

        // Test ZIP
        OutputStream zipOutputStream = new ByteArrayOutputStream(1024);
        ArchiveOutputStream zipArchiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        zipArchiveOutputStream.finish();
        zipArchiveOutputStream.close();
        Assertions.assertInstanceOf(ZipArchiveOutputStream.class, zipArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithUnknownType() throws ArchiveException, IOException {
        InputStream unknownInputStream = new ByteArrayInputStream(new byte[1024]);
        Assertions.assertThrows(ArchiveException.class, () -> archiveStreamFactory.createArchiveInputStream("unknown", unknownInputStream));
    }

    @Test
    public void testCreateArchiveOutputStreamWithUnknownType() throws ArchiveException, IOException {
        OutputStream unknownOutputStream = new ByteArrayOutputStream(1024);
        Assertions.assertThrows(ArchiveException.class, () -> archiveStreamFactory.createArchiveOutputStream("unknown", unknownOutputStream));
    }
}