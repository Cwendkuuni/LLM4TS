import org.apache.commons.compress.archivers.ArchiveException;
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
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test AR
        InputStream arInputStream = new ByteArrayInputStream(ArArchiveFactory.AR_SIGNATURE.getBytes());
        ArchiveInputStream arInput = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        Assert.assertNotNull(arInput);
        ArArchiveInputStream arInputAr = (ArArchiveInputStream) arInput;
        Assert.assertNotNull(arInputAr.getNextEntry());
        arInput.close();

        // Test ZIP
        InputStream zipInputStream = new ByteArrayInputStream(ZipFile.ZIP_SIGNATURE.getBytes());
        ArchiveInputStream zipInput = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        Assert.assertNotNull(zipInput);
        ZipArchiveInputStream zipInputZip = (ZipArchiveInputStream) zipInput;
        Assert.assertNotNull(zipInputZip.getNextEntry());
        zipInput.close();

        // Test TAR
        InputStream tarInputStream = new ByteArrayInputStream(TarFile.TAR_SIGNATURE.getBytes());
        ArchiveInputStream tarInput = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        Assert.assertNotNull(tarInput);
        TarArchiveInputStream tarInputTar = (TarArchiveInputStream) tarInput;
        Assert.assertNotNull(tarInputTar.getNextEntry());
        tarInput.close();

        // Test JAR
        InputStream jarInputStream = new ByteArrayInputStream(JarFile.JAR_SIGNATURE.getBytes());
        ArchiveInputStream jarInput = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        Assert.assertNotNull(jarInput);
        JarArchiveInputStream jarInputJar = (JarArchiveInputStream) jarInput;
        Assert.assertNotNull(jarInputJar.getNextEntry());
        jarInput.close();

        // Test CPIO
        InputStream cpioInputStream = new ByteArrayInputStream(CpioFile.CPIO_SIGNATURE.getBytes());
        ArchiveInputStream cpioInput = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        Assert.assertNotNull(cpioInput);
        CpioArchiveInputStream cpioInputCpio = (CpioArchiveInputStream) cpioInput;
        Assert.assertNotNull(cpioInputCpio.getNextEntry());
        cpioInput.close();

        // Test DUMP
        InputStream dumpInputStream = new ByteArrayInputStream(DumpFile.DUMP_SIGNATURE.getBytes());
        ArchiveInputStream dumpInput = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        Assert.assertNotNull(dumpInput);
        DumpArchiveInputStream dumpInputDump = (DumpArchiveInputStream) dumpInput;
        Assert.assertNotNull(dumpInputDump.getNextEntry());
        dumpInput.close();

        // Test Unknown
        InputStream unknownInputStream = new ByteArrayInputStream("Unknown".getBytes());
        ArchiveInputStream unknownInput = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.UNKNOWN, unknownInputStream);
        Assert.assertNull(unknownInput);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test AR
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arOutput = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        arOutput.putNextEntry(new ArArchiveEntry("test.ar"));
        arOutput.finish();
        arOutput.closeArchiveEntry();
        arOutput.close();
        Assert.assertEquals("test.ar\0\0", new String(arOutputStream.toByteArray()));

        // Test ZIP
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutput = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        zipOutput.putNextEntry(new ZipEntry("test.zip"));
        zipOutput.finish();
        zipOutput.closeArchiveEntry();
        zipOutput.close();
        Assert.assertEquals(ZipFile.ZIP_SIGNATURE, zipOutputStream.toByteArray(), 0, ZipFile.ZIP_SIGNATURE.length);

        // Test TAR
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarOutput = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        tarOutput.putNextEntry(new TarEntry("test.tar"));
        tarOutput.finish();
        tarOutput.closeArchiveEntry();
        tarOutput.close();
        Assert.assertEquals(TarFile.TAR_SIGNATURE, tarOutputStream.toByteArray(), 0, TarFile.TAR_SIGNATURE.length);

        // Test JAR
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutput = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        jarOutput.putNextEntry(new JarEntry("test.jar"));
        jarOutput.finish();
        jarOutput.closeArchiveEntry();
        jarOutput.close();
        Assert.assertEquals(JarFile.JAR_SIGNATURE, jarOutputStream.toByteArray(), 0, JarFile.JAR_SIGNATURE.length);

        // Test CPIO
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOutput = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        cpioOutput.putNextEntry(new CpioHeader("test.cpio"));
        cpioOutput.finish();
        cpioOutput.closeArchiveEntry();
        cpioOutput.close();
        Assert.assertEquals(CpioFile.CPIO_SIGNATURE, cpioOutputStream.toByteArray(), 0, CpioFile.CPIO_SIGNATURE.length);

        // Test DUMP
        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream dumpOutput = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
        dumpOutput.putNextEntry(new DumpHeader("test.dump"));
        dumpOutput.finish();
        dumpOutput.closeArchiveEntry();
        dumpOutput.close();
        Assert.assertEquals(DumpFile.DUMP_SIGNATURE, dumpOutputStream.toByteArray(), 0, DumpFile.DUMP_SIGNATURE.length);

        // Test Unknown
        OutputStream unknownOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream unknownOutput = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.UNKNOWN, unknownOutputStream);
        Assert.assertNull(unknownOutput);
    }

    // Utility class for testing
    private static class ArArchiveEntry extends ArchiveStreamEntry {
        public ArArchiveEntry(String name) {
            super(name);
        }
    }

    // Utility class for testing
    private static class ArArchiveFactory {
        public static final String AR = "ar";
        public static final byte[] AR_SIGNATURE = new byte[6];
        static {
            System.arraycopy(new byte[]{0x41, 0x41, 0x4d, 0x45, 0x00, 0x02}, 0, AR_SIGNATURE, 0, AR_SIGNATURE.length);
        }
    }

    // Utility class for testing
    private static class ArArchiveInputStream extends ArchiveInputStream {
        public ArArchiveInputStream(InputStream in) {
            super(in);
        }

        public ArArchiveEntry getNextEntry() throws IOException {
            return new ArArchiveEntry("test.ar");
        }
    }

    // Utility class for testing
    private static class ArArchiveOutputStream extends ArchiveOutputStream {
        public ArArchiveOutputStream(OutputStream out) {
            super(out);
        }
    }

    // Utility class for testing
    private static class CpioArchiveInputStream extends ArchiveInputStream {
        public CpioArchiveInputStream(InputStream in) {
            super(in);
        }

        public CpioHeader getNextEntry() throws IOException {
            return new CpioHeader("test.cpio");
        }
    }

    // Utility class for testing
    private static class CpioArchiveOutputStream extends ArchiveOutputStream {
        public CpioArchiveOutputStream(OutputStream out) {
            super(out);
        }
    }

    // Utility class for testing
    private static class CpioHeader {
        private String name;

        public CpioHeader(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    // Utility class for testing
    private static class DumpArchiveInputStream extends ArchiveInputStream {
        public DumpArchiveInputStream(InputStream in) {
            super(in);
        }

        public DumpHeader getNextEntry() throws IOException {
            return new DumpHeader("test.dump");
        }
    }

    // Utility class for testing
    private static class DumpArchiveOutputStream extends ArchiveOutputStream {
        public DumpArchiveOutputStream(OutputStream out) {
            super(out);
        }
    }

    // Utility class for testing
    private static class DumpHeader {
        private String name;

        public DumpHeader(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    // Utility class for testing
    private static class JarArchiveEntry extends JAREntry {
        public JarArchiveEntry(String name) {
            super(name);
        }
    }

    // Utility class for testing
    private static class JarArchiveInputStream extends JarInputStream {
        public JarArchiveInputStream(InputStream in) {
            super(in);
        }

        public JarArchiveEntry getNextJarEntry() throws IOException {
            return new JarArchiveEntry("test.jar");
        }
    }

    // Utility class for testing
    private static class JarArchiveOutputStream extends JarOutputStream {
        public JarArchiveOutputStream(OutputStream out) {
            super(out);
        }
    }

    // Utility class for testing
    private static class TarArchiveEntry extends TarEntry {
        public TarArchiveEntry(String name) {
            super(name);
        }
    }

    // Utility class for testing
    private static class TarArchiveInputStream extends TarInputStream {
        public TarArchiveInputStream(InputStream in) {
            super(in);
        }

        public TarArchiveEntry getNextEntry() throws IOException {
            return new TarArchiveEntry("test.tar");
        }
    }

    // Utility class for testing
    private static class TarArchiveOutputStream extends TarOutputStream {
        public TarArchiveOutputStream(OutputStream out) {
            super(out);
        }
    }

    // Utility class for testing
    private static class ZipArchiveEntry extends ZipEntry {
        public ZipArchiveEntry(String name) {
            super(name);
        }
    }

    // Utility class for testing
    private static class ZipArchiveInputStream extends ZipInputStream {
        public ZipArchiveInputStream(InputStream in) {
            super(in);
        }

        public ZipArchiveEntry getNextEntry() throws IOException {
            return new ZipArchiveEntry("test.zip");
        }
    }

    // Utility class for testing
    private static class ZipArchiveOutputStream extends ZipOutputStream {
        public ZipArchiveOutputStream(OutputStream out) {
            super(out);
        }
    }

    // Utility class for testing
    private static ArchiveStreamFactory getInstance() {
        return ArchiveStreamFactory.class.newInstance();
    }

    // Utility class for testing
    private static final class ArchiveStreamFactory extends org.apache.commons.compress.archivers.ArchiveStreamFactory {
    }
}