import org.apache.commons.compress.ArchiveException;
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
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateArchiveInputStream() throws Exception {
        String[] archiverNames = {ArchiveStreamFactory.AR, ArchiveStreamFactory.ZIP, ArchiveStreamFactory.TAR,
                ArchiveStreamFactory.JAR, ArchiveStreamFactory.CPIO, ArchiveStreamFactory.DUMP};
        InputStream inputStream = new ByteArrayInputStream(new byte[256]);
        for (String archiverName : archiverNames) {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
            try {
                archiveInputStream.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        String[] archiverNames = {ArchiveStreamFactory.AR, ArchiveStreamFactory.ZIP, ArchiveStreamFactory.TAR,
                ArchiveStreamFactory.JAR, ArchiveStreamFactory.CPIO, ArchiveStreamFactory.DUMP};
        OutputStream outputStream = new ByteArrayOutputStream();
        for (String archiverName : archiverNames) {
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
            try {
                archiveOutputStream.finish();
                archiveOutputStream.close();
            } catch (IOException e) {
                // ignore
            }
        }

        // Check created archive files
        for (String archiverName : archiverNames) {
            ArchiveInputStream archiveInputStream = null;
            try {
                switch (archiverName) {
                    case ArchiveStreamFactory.AR:
                        archiveInputStream = new ArArchiveInputStream(new ByteArrayInputStream(((ByteArrayOutputStream)outputStream).toByteArray()));
                        break;
                    case ArchiveStreamFactory.ZIP:
                        archiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(((ByteArrayOutputStream)outputStream).toByteArray()));
                        break;
                    case ArchiveStreamFactory.TAR:
                        archiveInputStream = new TarArchiveInputStream(new ByteArrayInputStream(((ByteArrayOutputStream)outputStream).toByteArray()));
                        break;
                    case ArchiveStreamFactory.JAR:
                        archiveInputStream = new JarArchiveInputStream(new ByteArrayInputStream(((ByteArrayOutputStream)outputStream).toByteArray()));
                        break;
                    case ArchiveStreamFactory.CPIO:
                        archiveInputStream = new CpioArchiveInputStream(new ByteArrayInputStream(((ByteArrayOutputStream)outputStream).toByteArray()));
                        break;
                    case ArchiveStreamFactory.DUMP:
                        archiveInputStream = new DumpArchiveInputStream(new ByteArrayInputStream(((ByteArrayOutputStream)outputStream).toByteArray()));
                        break;
                }
                ArchiveEntry entry = archiveInputStream.getNextEntry();
                Assert.assertNotNull("No entry found in the archive.", entry);
                Assert.assertEquals("The entry name is incorrect.", "", entry.getName());
                IOUtils.closeQuietly(archiveInputStream);
            } catch (IOException e) {
                if (archiveInputStream != null) {
                    IOUtils.closeQuietly(archiveInputStream);
                }
                throw e;
            }
        }
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(InputStream)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws Exception {
        String[] archiverNames = {"zip", "jar", "tar.gz", "gz"};
        InputStream inputStream = new ByteArrayInputStream(new byte[256]);
        for (String archiverName : archiverNames) {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(inputStream);
            try {
                if (ArchiveStreamFactory.ZIP.equalsIgnoreCase(archiverName)) {
                    Assert.assertTrue("Expected a ZIP archive.", ZipArchiveInputStream.class.isInstance(archiveInputStream));
                } else if (ArchiveStreamFactory.JAR.equalsIgnoreCase(archiverName)) {
                    Assert.assertTrue("Expected a JAR archive.", JarArchiveInputStream.class.isInstance(archiveInputStream));
                } else if (ArchiveStreamFactory.TAR.equalsIgnoreCase(archiverName) || ArchiveStreamFactory.GZ.equalsIgnoreCase(archiverName)) {
                    Assert.assertTrue("Expected a TAR or GZ archive.", TarArchiveInputStream.class.isInstance(archiveInputStream) || GzipInputStream.class.isInstance(archiveInputStream));
                }
                archiveInputStream.close();
            } catch (IOException e) {
                throw new AssertionError("Failed to create archive: " + e.getMessage());
            }
        }
    }
}