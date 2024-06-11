import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
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
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test for ArchiveStreamFactory class
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        // Basic functionality test for createArchiveInputStream(String, InputStream) method
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        Assert.assertNotNull(archiveInputStream);

        // Test with different archive formats
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        Assert.assertNotNull(archiveInputStream);
        Assert.assertTrue(archiveInputStream instanceof ArArchiveInputStream);

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        Assert.assertNotNull(archiveInputStream);
        Assert.assertTrue(archiveInputStream instanceof CpioArchiveInputStream);

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        Assert.assertNotNull(archiveInputStream);
        Assert.assertTrue(archiveInputStream instanceof DumpArchiveInputStream);

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        Assert.assertNotNull(archiveInputStream);
        Assert.assertTrue(archiveInputStream instanceof JarArchiveInputStream);

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        Assert.assertNotNull(archiveInputStream);
        Assert.assertTrue(archiveInputStream instanceof TarArchiveInputStream);

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        Assert.assertNotNull(archiveInputStream);
        Assert.assertTrue(archiveInputStream instanceof ZipArchiveInputStream);

        // Test with invalid archiver name
        try {
            archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream("invalid", inputStream);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: invalid not found.", e.getMessage());
        }

        // Test with null input stream
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("InputStream must not be null.", e.getMessage());
        }

        // Test with null archiver name
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(null, inputStream);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Archivername must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        // Basic functionality test for createArchiveOutputStream(String, OutputStream) method
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        Assert.assertNotNull(archiveOutputStream);

        // Test with different archive formats
        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        Assert.assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);

        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        Assert.assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);

        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        Assert.assertTrue(archiveOutputStream instanceof DumpArchiveOutputStream);

        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        Assert.assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);

        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        Assert.assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);

        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        Assert.assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);

        // Test with invalid archiver name
        try {
            archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream("invalid", outputStream);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: invalid not found.", e.getMessage());
        }

        // Test with null output stream
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("OutputStream must not be null.", e.getMessage());
        }

        // Test with null archiver name
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, outputStream);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Archivername must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws IOException, ArchiveException {
        // Basic functionality test for createArchiveInputStream(InputStream) method
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        Assert.assertNotNull(archiveInputStream);

        // Test with ZIP archive
        ArchiveArchiveEntry zipEntry = new JarArchiveEntry("test.txt");
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        archiveOutputStream.putNextEntry(zipEntry);
        archiveOutputStream.write("Test Data".getBytes());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        InputStream zipInputStream = new ByteArrayInputStream(outputStream.toByteArray());

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(zipInputStream);
        Assert.assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        Assert.assertEquals("test.txt", archiveInputStream.getNextEntry().getName());

        // Test with JAR archive
        InputStream jarInputStream = new ByteArrayInputStream("Test-Class: com.test.Test\nTest-Method: testMethod\n".getBytes());
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(jarInputStream);
        Assert.assertTrue(archiveInputStream instanceof JarArchiveInputStream);
        Assert.assertEquals("META-INF/MANIFEST.mf", archiveInputStream.getNextEntry().getName());

        // Test with AR archive
        InputStream arInputStream = new ByteArrayInputStream("! <TRN> <NAME> test.txt <MODE> 0644 <UID> 1001 <GID> 1001 <SIZE> 13 <MOD_TIME> 199612311200.000000000Z\n<TRN> <NAME> test2.txt <MODE> 0644 <UID> 1001 <GID> 1001 <SIZE> 22 <MOD_TIME> 199612311201.000000000Z\n".getBytes());
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(arInputStream);
        Assert.assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        Assert.assertEquals("test.txt", archiveInputStream.getNextEntry().getName());

        // Test with CPIO archive
        InputStream cpioInputStream = new ByteArrayInputStream("04070201010100000100000000000000 test.txt\n602001010101000101000000 test data\n".getBytes());
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(cpioInputStream);
        Assert.assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
        Assert.assertEquals("test.txt", archiveInputStream.getNextEntry().getName());

        // Test with DUMP archive
        InputStream dumpInputStream = new ByteArrayInputStream("07070110000000000001 test.txt\n65 test data\n".getBytes());
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(dumpInputStream);
        Assert.assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
        Assert.assertEquals("test.txt", archiveInputStream.getNextEntry().getName());

        // Test with TAR archive
        InputStream tarInputStream = new ByteArrayInputStream("123456789000 test.txt\n00 size\n".getBytes());
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(tarInputStream);
        Assert.assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        Assert.assertEquals("test.txt", archiveInputStream.getNextEntry().getName());

        // Test with other archives or non-archives
        InputStream randomInputStream = new ByteArrayInputStream("Test Data".getBytes());
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(randomInputStream);
        Assert.assertNull(archiveInputStream.getNextEntry());

        try {
            archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }

    @Test
    public void testGetInstance() {
        ArchiveStreamFactory factory1 = ArchiveStreamFactory.getInstance();
        ArchiveStreamFactory factory2 = ArchiveStreamFactory.getInstance();
        Assert.assertEquals(factory1, factory2);
    }
}