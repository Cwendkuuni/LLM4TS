package org.apache.commons.compress.test.archivers;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Tests for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Tests creating an archive input stream from a known archive file.
     */
    @Test
    public void testCreateArchiveInputStreamFromFile() throws IOException, ArchiveException {
        final String archiveFileName = "testdata/test.tar.gz";
        final ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR_GZ, new FileInputStream(archiveFileName));
        TarEntry entry = null;
        while ((entry = (TarEntry) in.getNextEntry()) != null) {
            Assert.assertNotNull(entry);
            Assert.assertTrue(entry.isDirectory() == false);
            Assert.assertEquals("testdata/test1.xml", entry.getName());
        }
        in.close();
    }

    /**
     * Tests creating an archive output stream and writing an entry to it.
     */
    @Test
    public void testCreateArchiveOutputStreamAndWriteEntry() throws IOException, ArchiveException {
        final String outputFileName = "testdata/output.tar";
        final OutputStream out = new FileOutputStream(outputFileName);
        try {
            ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            os.putArchiveEntry(new TarEntry("testdata/test1.xml"));
            IOUtils.copy(new FileInputStream("testdata/test1.xml"), os);
            os.closeArchiveEntry();
            os.finish();
            os.close();

            final ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new FileInputStream(outputFileName));
            TarEntry entry = null;
            while ((entry = (TarEntry) in.getNextEntry()) != null) {
                Assert.assertNotNull(entry);
                Assert.assertTrue(entry.isDirectory() == false);
                Assert.assertEquals("testdata/test1.xml", entry.getName());
            }
            in.close();
            new File(outputFileName).delete();
        } finally {
            out.close();
        }
    }

    /**
     * Tests creating an archive input stream from an input stream.
     */
    @Test
    public void testCreateArchiveInputStreamFromStream() throws IOException, ArchiveException {
        final String testDataDir = "testdata/";
        final String archiveName = "test.tar";

        final OutputStream out = new FileOutputStream(testDataDir + archiveName);
        try {
            ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            ArchiveEntry entry = new TarEntry("testdata/test1.xml");
            os.putArchiveEntry(entry);
            IOUtils.copy(new FileInputStream("testdata/test1.xml"), os);
            os.closeArchiveEntry();
            os.finish();
            os.close();

            final InputStream in = new FileInputStream(testDataDir + archiveName);
            TarEntry entryFromInputStream = null;
            while ((entryFromInputStream = (TarEntry) new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in).getNextEntry()) != null) {
                Assert.assertNotNull(entryFromInputStream);
                Assert.assertTrue(entryFromInputStream.isDirectory() == false);
                Assert.assertEquals("testdata/test1.xml", entryFromInputStream.getName());
            }
            in.close();
            new File(testDataDir + archiveName).delete();
        } finally {
            out.close();
        }
    }

    /**
     * Tests creating a ZIP archive output stream and writing an entry to it.
     */
    @Test
    public void testCreateZipOutputStreamAndWriteEntry() throws IOException, ArchiveException {
        final String outputFileName = "testdata/output.zip";
        final OutputStream out = new FileOutputStream(outputFileName);
        try {
            ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            os.putNextEntry(new ZipEntry("testdata/test1.xml"));
            IOUtils.copy(new FileInputStream("testdata/test1.xml"), os);
            os.closeEntry();
            os.finish();
            os.close();

            final InputStream in = new FileInputStream(outputFileName);
            ZipEntry entryFromInputStream = null;
            while ((entryFromInputStream = (ZipEntry) new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in).getNextEntry()) != null) {
                Assert.assertNotNull(entryFromInputStream);
                Assert.assertTrue(entryFromInputStream.isDirectory() == false);
                Assert.assertEquals("testdata/test1.xml", entryFromInputStream.getName());
            }
            in.close();
            new File(outputFileName).delete();
        } finally {
            out.close();
        }
    }

    /**
     * Tests creating a ZIP archive input stream from an input stream.
     */
    @Test
    public void testCreateZipInputStreamFromStream() throws IOException, ArchiveException {
        final String testDataDir = "testdata/";
        final String archiveName = "test.zip";

        final OutputStream out = new FileOutputStream(testDataDir + archiveName);
        try {
            ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            ArchiveEntry entry = new ZipEntry("testdata/test1.xml");
            os.putNextEntry(entry);
            IOUtils.copy(new FileInputStream("testdata/test1.xml"), os);
            os.closeEntry();
            os.finish();
            os.close();

            final InputStream in = new FileInputStream(testDataFileName);
            ZipEntry entryFromInputStream = null;
            while ((entryFromInputStream = (ZipEntry) new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in).getNextEntry()) != null) {
                Assert.assertNotNull(entryFromInputStream);
                Assert.assertTrue(entryFromInputStream.isDirectory() == false);
                Assert.assertEquals("testdata/test1.xml", entryFromInputStream.getName());
            }
            in.close();
            new File(testDataFileName).delete();
        } finally {
            out.close();
        }
    }

    /**
     * Tests creating an archive input stream from a byte array.
     */
    @Test
    public void testCreateArchiveInputStreamFromByteArray() throws IOException, ArchiveException {
        final byte[] archiveBytes = new byte[1024];
        try {
            System.arraycopy(new TarEntry("testdata/test1.xml").getHeader().toByteArray(), 0, archiveBytes, 0, archiveBytes.length);
            final InputStream in = new ByteArrayInputStream(archiveBytes);
            TarEntry entryFromInputStream = null;
            while ((entryFromInputStream = (TarEntry) new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in).getNextEntry()) != null) {
                Assert.assertNotNull(entryFromInputStream);
                Assert.assertTrue(entryFromInputStream.isDirectory() == false);
                Assert.assertEquals("testdata/test1.xml", entryFromInputStream.getName());
            }
            in.close();
        } catch (IOException e) {
            throw new ArchiveException("Unexpected error", e);
        }
    }

    /**
     * Tests creating an archive output stream with an invalid format.
     */
    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveOutputStreamWithInvalidFormat() throws IOException, ArchiveException {
        final OutputStream out = new FileOutputStream("testdata/output.invalid");
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.INVALID, out);
    }

    /**
     * Tests creating an archive input stream with an invalid format.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidFormat() throws IOException, ArchiveException {
        final InputStream in = new ByteArrayInputStream(new byte[512]);
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.INVALID, in);
    }
}