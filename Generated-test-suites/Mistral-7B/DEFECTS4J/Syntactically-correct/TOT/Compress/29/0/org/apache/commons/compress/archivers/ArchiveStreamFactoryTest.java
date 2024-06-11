package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.AR;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    private static final String TEST_FILE = "testdata/test.txt";
    private static final String ARCHIVE_NAME = "test.tar.gz";

    @org.junit.Test
    public void testCreateArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOS = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);

        // Add an entry
        ArArchiveEntry entry = new ArArchiveEntry("testdata/test.txt");
        archiveOS.putNextEntry(entry);

        // Write some data to the entry
        InputStream in = new FileInputStream(TEST_FILE);
        IOUtils.copy(in, archiveOS);
        archiveOS.closeArchiveEntry();
        in.close();

        archiveOS.finish();
        archiveOS.close();

        // Verify the output
        ArchiveInputStream archiveIS = factory.createArchiveInputStream(ARCHIVE_NAME, new ByteArrayInputStream(out.toByteArray()));
        ArArchiveEntry archiveEntry = (ArArchiveEntry) archiveIS.getNextEntry();
        assertEquals("testdata/test.txt", archiveEntry.getName());
        archiveIS.close();
    }

    @org.junit.Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Create an archive using the factory
        OutputStream out = new FileOutputStream(ARCHIVE_NAME);
        ArchiveOutputStream archiveOS = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);

        // Add an entry
        ArArchiveEntry entry = new ArArchiveEntry("testdata/test.txt");
        archiveOS.putNextEntry(entry);

        // Write some data to the entry
        InputStream in = new FileInputStream(TEST_FILE);
        IOUtils.copy(in, archiveOS);
        archiveOS.closeArchiveEntry();
        in.close();

        archiveOS.finish();
        archiveOS.close();

        // Create an input stream using the factory
        InputStream archiveIS = factory.createArchiveInputStream(ARCHIVE_NAME);

        // Verify the input
        ArArchiveEntry archiveEntry = (ArArchiveEntry) archiveIS.getNextEntry();
        assertEquals("testdata/test.txt", archiveEntry.getName());
        assertTrue(archiveEntry.isDirectory()); // This should be false

        // Read the data
        byte[] buffer = new byte[1024];
        int len;
        OutputStream out2 = new ByteArrayOutputStream();
        while ((len = archiveIS.read(buffer)) > 0) {
            out2.write(buffer, 0, len);
        }
        archiveIS.close();

        // Verify the data
        byte[] expected = new byte[IOUtils.size(new FileInputStream(TEST_FILE))];
        InputStream in2 = new FileInputStream(TEST_FILE);
        IOUtils.readFully(in2, expected);
        in2.close();

        assertEquals(expected, out2.toByteArray());
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateUnknownArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOS = factory.createArchiveOutputStream("unknown", out);
        fail(); // This should never be reached
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateUnknownArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        InputStream archiveIS = factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
        fail(); // This should never be reached
    }

    @org.junit.Test(expected = StreamingNotSupportedException.class)
    public void testCreateSevenZArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOS = factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, out);
        fail(); // This should never be reached
    }

    @org.junit.Test(expected = StreamingNotSupportedException.class)
    public void testCreateSevenZArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        InputStream archiveIS = factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, new ByteArrayInputStream(new byte[0]));
        fail(); // This should never be reached
    }

    @org.junit.Test(expected = StreamingNotSupportedException.class)
    public void testCreateCpioArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOS = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        fail(); // This should never be reached
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateCpioArchiveInputStreamWithInvalidMagic() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        InputStream archiveIS = factory.createArchiveInputStream(
                new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 }), ArchiveStreamFactory.CPIO);
        fail(); // This should never be reached
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateCpioArchiveInputStreamWithEmptyStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        InputStream archiveIS = factory.createArchiveInputStream(
                new ByteArrayInputStream(new byte[0]), ArchiveStreamFactory.CPIO);
        fail(); // This should never be reached
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateCpioArchiveInputStreamWithInvalidFormat() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        InputStream archiveIS = factory.createArchiveInputStream(
                new ByteArrayInputStream(new byte[] { 0x07, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x01, 0x0c, 0x0d }), ArchiveStreamFactory.CPIO);
        fail(); // This should never be reached
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateArjArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOS = factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, out);
        fail(); // This should never be reached
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateArjArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        InputStream archiveIS = factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, new ByteArrayInputStream(new byte[0]));
        fail(); // This should never be reached
    }

    @org.junit.Test
    public void testCreateJarArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOS = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);

        // Add a JAR entry
        JarArchiveEntry jarEntry = new JarArchiveEntry("testdata/test.txt");
        archiveOS.putNextEntry(jarEntry);

        // Write some data to the entry
        InputStream in = new FileInputStream(TEST_FILE);
        IOUtils.copy(in, archiveOS);
        archiveOS.closeArchiveEntry();
        in.close();

        archiveOS.finish();
        archiveOS.close();

        // Verify the output
        JarArchiveInputStream jarArchiveIS = new JarArchiveInputStream(new ByteArrayInputStream(out.toByteArray()));
        JarArchiveEntry jarArchiveEntry = (JarArchiveEntry) jarArchiveIS.getNextJarEntry();
        assertEquals("testdata/test.txt", jarArchiveEntry.getName());
        jarArchiveIS.close();
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateUnknownJarArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOS = factory.createArchiveOutputStream("unknown", out);
        fail(); // This should never be reached
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateUnknownJarArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        InputStream archiveIS = factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
        fail(); // This should never be reached
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateUnknownFormatArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        InputStream archiveIS = factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
        fail(); // This should never be reached
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateUnknownFormatArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOS = factory.createArchiveOutputStream("unknown", out);
        fail(); // This should never be reached
    }

    @org.junit.Test
    public void testCreateTarArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOS = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);

        // Add an entry
        TarArchiveEntry tarEntry = new TarArchiveEntry("testdata/test.txt");
        tarEntry.setSize(10);
        archiveOS.putNextEntry(tarEntry);

        archiveOS.finish();
        archiveOS.close();

        // Verify the output
        TarArchiveInputStream tarArchiveIS = new TarArchiveInputStream(new ByteArrayInputStream(out.toByteArray()));
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) tarArchiveIS.getNextEntry();
        assertEquals("testdata/test.txt", tarArchiveEntry.getName());
        assertEquals(10, tarArchiveEntry.getSizeLong());
        tarArchiveIS.close();
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateTarArchiveOutputStreamWithInvalidFormat() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOS = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);

        // Add an entry
        TarArchiveEntry tarEntry = new TarArchiveEntry("testdata/test.txt");
        archiveOS.putNextEntry(tarEntry);

        archiveOS.write(0x04); // Write a byte that is not the expected format byte
        archiveOS.finish();
        archiveOS.close();

        TarArchiveInputStream tarArchiveIS = new TarArchiveInputStream(new ByteArrayInputStream(out.toByteArray()));
        tarArchiveIS.getNextEntry();
        fail(); // This should never be reached
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateTarArchiveOutputStreamWithoutMagicByte() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOS = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);

        // Add an entry
        TarArchiveEntry tarEntry = new TarArchiveEntry("testdata/test.txt");
        archiveOS.putNextEntry(tarEntry);

        archiveOS.finish();
        archiveOS.close();

        TarArchiveInputStream tarArchiveIS = new TarArchiveInputStream(new ByteArrayInputStream(out.toByteArray()));
        tarArchiveIS.getNextEntry();
        fail(); // This should never be reached
    }

    @org.junit.Test
    public void testCreateZipArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOS = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);

        // Add a ZIP entry
        ZipArchiveEntry zipEntry = new ZipEntry("testdata/test.txt");
        archiveOS.putNextEntry(zipEntry);

        // Write some data to the entry
        InputStream in = new FileInputStream(TEST_FILE);
        IOUtils.copy(in, archiveOS);
        archiveOS.closeArchiveEntry();
        in.close();

        archiveOS.finish();
        archiveOS.close();

        // Verify the output
        ZipArchiveInputStream zipArchiveIS = new ZipArchiveInputStream(new ByteArrayInputStream(out.toByteArray()));
        ZipArchiveEntry zipArchiveEntry = (ZipArchiveEntry) zipArchiveIS.getNextEntry();
        assertEquals("testdata/test.txt", zipArchiveEntry.getName());
        zipArchiveIS.close();
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateUnknownFormatArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOS = factory.createArchiveOutputStream("unknown", out);
        fail(); // This should never be reached
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateUnknownFormatArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        InputStream archiveIS = factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
        fail(); // This should never be reached
    }
}