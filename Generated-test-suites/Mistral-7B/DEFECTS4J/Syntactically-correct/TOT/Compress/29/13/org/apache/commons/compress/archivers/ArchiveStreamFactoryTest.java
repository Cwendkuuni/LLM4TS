package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test class for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)}
     * with valid parameters.
     */
    @Test
    public void testCreateArchiveInputStream() throws Exception {
        final InputStream zipStream = new ByteArrayInputStream(TestHelper.getZipSignature());
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream("zip", zipStream);
        assertSame(ZipArchiveInputStream.class, archiveInputStream.getClass());

        final InputStream tarStream = new ByteArrayInputStream(TestHelper.getTarSignature());
        final ArchiveInputStream archiveInputStreamTar = new ArchiveStreamFactory()
                .createArchiveInputStream("tar", tarStream);
        assertSame(TarArchiveInputStream.class, archiveInputStreamTar.getClass());

        final InputStream jarStream = new ByteArrayInputStream(TestHelper.getJarSignature());
        final ArchiveInputStream archiveInputStreamJar = new ArchiveStreamFactory()
                .createArchiveInputStream("jar", jarStream);
        assertSame(JarArchiveInputStream.class, archiveInputStreamJar.getClass());

        final InputStream arStream = new ByteArrayInputStream(TestHelper.getArSignature());
        final ArchiveInputStream archiveInputStreamAr = new ArchiveStreamFactory()
                .createArchiveInputStream("ar", arStream);
        assertSame(ArArchiveInputStream.class, archiveInputStreamAr.getClass());
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(InputStream)}
     * with valid parameters.
     */
    @Test
    public void testCreateArchiveInputStreamFromSignature() throws Exception {
        final InputStream zipStream = new ByteArrayInputStream(TestHelper.getZipSignature());
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(zipStream);
        assertSame(ZipArchiveInputStream.class, archiveInputStream.getClass());

        final InputStream tarStream = new ByteArrayInputStream(TestHelper.getTarSignature());
        final ArchiveInputStream archiveInputStreamTar = new ArchiveStreamFactory()
                .createArchiveInputStream(tarStream);
        assertSame(TarArchiveInputStream.class, archiveInputStreamTar.getClass());

        final InputStream jarStream = new ByteArrayInputStream(TestHelper.getJarSignature());
        final ArchiveInputStream archiveInputStreamJar = new ArchiveStreamFactory()
                .createArchiveInputStream(jarStream);
        assertSame(JarArchiveInputStream.class, archiveInputStreamJar.getClass());

        final InputStream arStream = new ByteArrayInputStream(TestHelper.getArSignature());
        final ArchiveInputStream archiveInputStreamAr = new ArchiveStreamFactory()
                .createArchiveInputStream(arStream);
        assertSame(ArArchiveInputStream.class, archiveInputStreamAr.getClass());

        final InputStream signatureStream = new ByteArrayInputStream(TestHelper.getUnknownSignature());
        final ArchiveInputStream archiveInputStreamUnknown = new ArchiveStreamFactory()
                .createArchiveInputStream(signatureStream);
        assertNull(archiveInputStreamUnknown);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)}
     * with valid parameters.
     */
    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        final OutputStream zipOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream("zip", zipOutputStream);
        assertSame(ZipArchiveOutputStream.class, archiveOutputStream.getClass());

        final OutputStream tarOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStreamTar = new ArchiveStreamFactory()
                .createArchiveOutputStream("tar", tarOutputStream);
        assertSame(TarArchiveOutputStream.class, archiveOutputStreamTar.getClass());

        final OutputStream jarOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStreamJar = new ArchiveStreamFactory()
                .createArchiveOutputStream("jar", jarOutputStream);
        assertSame(JarArchiveOutputStream.class, archiveOutputStreamJar.getClass());

        final OutputStream arOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStreamAr = new ArchiveStreamFactory()
                .createArchiveOutputStream("ar", arOutputStream);
        assertSame(ArArchiveOutputStream.class, archiveOutputStreamAr.getClass());

        archiveOutputStream.close();
        archiveOutputStreamAr.close();
        archiveOutputStreamJar.close();
        archiveOutputStreamTar.close();
        final ArchiveEntry entry = new JarEntry("test.txt");
        archiveOutputStreamJar.putNextEntry(entry);
        archiveOutputStreamJar.write("Test content.".getBytes(), 0, "Test content.".length());
        archiveOutputStreamJar.closeArchiveEntry();

        final InputStream testInputStream = new ByteArrayInputStream("Test content.".getBytes());
        final ArchiveInputStream archiveInputStreamJar = new ArchiveStreamFactory()
                .createArchiveInputStream("jar", testInputStream);
        final JarEntry entryJar = (JarEntry) archiveInputStreamJar.getNextJarEntry();
        assertTrue(entryJar.getName().equals("test.txt"));
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)}
     * with a null OutputStream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        new ArchiveStreamFactory().createArchiveOutputStream("zip", null);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)}
     * with an unknown archiver name.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() throws Exception {
        final OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("unknown", outputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(OutputStream)}
     * with a null OutputStream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamFromOutputStreamWithNullOutputStream() {
        new ArchiveStreamFactory().createArchiveOutputStream(null);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(OutputStream)}
     * with no archiver name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamFromOutputStreamWithNoArchiverName() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(outputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(OutputStream)}
     * with an unknown archiver.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromOutputStreamWithUnknownArchiver() throws Exception {
        final OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("unknown", outputStream);
    }

    /**
     * Helper class for test data.
     */
    private static class TestHelper {
        private static final String getZipSignature() {
            return "PK\u0005\u0004\u0014\u0000\u000a\u001b\u0000\u0018\u000bBLOB\u0005test.txt\u0000\u0002\u0014Test content.\u0000\u0000\u0000\u0000\u0000\u0000test.txt\u0000\u0014Test content.\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
        }

        private static final String getTarSignature() {
            return "\u0000\u0000\u0014\u0000\u0000\u0014\u0000\u0014\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
        }

        private static final String getJarSignature() {
            return "META-INF/\u0003\u0004\u000a\u0000\u001a\u0010Manifest-Version: 1.0\u001a\nManifest-Name: test.jar\u001a\nMain-Class: org.apache.commons.compress.archivers.test.ArchiveStreamFactoryTest\u001a";
        }

        private static final String getArSignature() {
            return "\u0001\u0001\u0002\u0001\u0000\u0000\u0002test\n\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000test\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
        }

        private static final String getUnknownSignature() {
            return "Unknown signature.";
        }
    }
}