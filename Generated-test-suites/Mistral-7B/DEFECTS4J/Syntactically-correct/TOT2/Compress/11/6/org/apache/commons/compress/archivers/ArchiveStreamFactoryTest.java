import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private static final String TEST_FILE = "testFile.txt";
    private static final String TEST_ARCHIVE = "testArchive.tar.gz";
    private static final String TEST_ARCHIVE_JAR = "testArchive.jar";

    @BeforeClass
    public static void setup() throws Exception {
        writeTestFile(TEST_FILE);
    }

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test with known archive types
        InputStream inZip = new FileInputStream(TEST_ARCHIVE_JAR);
        ArchiveInputStream aisZip = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inZip);
        Assert.assertEquals("Expected ZIP archive", ArchiveStreamFactory.ZIP, aisZip.getFormat());
        aisZip.close();
        inZip.close();

        InputStream inTar = new FileInputStream(TEST_ARCHIVE);
        ArchiveInputStream aisTar = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, inTar);
        Assert.assertEquals("Expected TAR archive", ArchiveStreamFactory.TAR, aisTar.getFormat());
        aisTar.close();
        inTar.close();

        // Test with unknown archive type
        InputStream inUnknown = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream aisUnknown = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.UNKNOWN, inUnknown);
        Assert.fail("Expected ArchiveException for unknown archive type");
        aisUnknown.close();
        inUnknown.close();

        // Test with null archiver name
        ArchiveInputStream aisNull = new ArchiveStreamFactory().createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
        Assert.fail("Expected IllegalArgumentException for null archiver name");
        aisNull.close();

        // Test with null input stream
        ArchiveInputStream aisNullInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
        Assert.fail("Expected IllegalArgumentException for null input stream");
        aisNullInput.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test with known archive types
        OutputStream outZip = new FileOutputStream(TEST_ARCHIVE_JAR);
        ArchiveOutputStream oosZip = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outZip);
        oosZip.putNextEntry(new JarArchiveEntry("testEntry.txt"));
        IOUtils.write("Test content.", oosZip.getOutputStream());
        oosZip.closeArchiveEntry();
        oosZip.finish();
        outZip.close();

        OutputStream outTar = new FileOutputStream(TEST_ARCHIVE);
        ArchiveOutputStream oosTar = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outTar);
        oosTar.putNextEntry(new TarEntry("testEntry.txt"));
        IOUtils.write("Test content.", oosTar.getOutputStream());
        oosTar.closeArchiveEntry();
        oosTar.finish();
        outTar.close();

        // Test with unknown archive type
        OutputStream outUnknown = new FileOutputStream(TEST_ARCHIVE_JAR.substring(0, TEST_ARCHIVE_JAR.length() - 3) + "foo");
        ArchiveOutputStream oosUnknown = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.UNKNOWN, outUnknown);
        Assert.fail("Expected ArchiveException for unknown archive type");
        oosUnknown.close();
        outUnknown.close();

        // Test with null archiver name
        ArchiveOutputStream oosNull = new ArchiveStreamFactory().createArchiveOutputStream(null, new ByteArrayOutputStream());
        Assert.fail("Expected IllegalArgumentException for null archiver name");
        oosNull.close();

        // Test with null output stream
        ArchiveOutputStream oosNullOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
        Assert.fail("Expected IllegalArgumentException for null output stream");
        oosNullOutput.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Test with known archive types
        InputStream inZip = new FileInputStream(TEST_ARCHIVE_JAR);
        ArchiveInputStream aisZip = new ArchiveStreamFactory().createArchiveInputStream(inZip);
        Assert.assertEquals("Expected ZIP archive", ArchiveStreamFactory.ZIP, aisZip.getFormat());
        aisZip.close();
        inZip.close();

        InputStream inTar = new FileInputStream(TEST_ARCHIVE);
        ArchiveInputStream aisTar = new ArchiveStreamFactory().createArchiveInputStream(inTar);
        Assert.assertEquals("Expected TAR archive", ArchiveStreamFactory.TAR, aisTar.getFormat());
        aisTar.close();
        inTar.close();

        // Test with unknown archive type
        InputStream inUnknown = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream aisUnknown = new ArchiveStreamFactory().createArchiveInputStream(inUnknown);
        Assert.fail("Expected ArchiveException for unknown archive type");
        aisUnknown.close();
        inUnknown.close();

        // Test with null input stream
        ArchiveInputStream aisNullInput = new ArchiveStreamFactory().createArchiveInputStream((InputStream) null);
        Assert.fail("Expected IllegalArgumentException for null input stream");
        aisNullInput.close();

        // Test with empty input stream
        InputStream inEmpty = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream aisEmpty = new ArchiveStreamFactory().createArchiveInputStream(inEmpty);
        Assert.assertNull("Expected null for empty input stream", aisEmpty.getNextEntry());
        aisEmpty.close();
        inEmpty.close();
    }

    private static void writeTestFile(String fileName) throws IOException {
        OutputStream out = new FileOutputStream(fileName);
        IOUtils.write("Test content.", out);
        out.close();
    }
}