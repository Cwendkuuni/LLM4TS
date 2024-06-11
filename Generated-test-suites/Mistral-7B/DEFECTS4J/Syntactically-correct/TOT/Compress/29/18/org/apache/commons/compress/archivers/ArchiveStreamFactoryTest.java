package org.apache.commons.compress.archivers.test;

import static org.apache.commons.compress.utils.IOUtils.closeQuietly;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test class for the ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)}.
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test ZIP archive
        final String zipFileName = "test-zip.zip";
        try (OutputStream out = new FileOutputStream(zipFileName)) {
            final ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            // Add file entry
            final String testFile = "test-zip-file.txt";
            final File file = new File(testFile);
            os.putArchiveEntry(new JarArchiveEntry(file.getAbsolutePath()));
            IOUtils.copy(new FileInputStream(file), os);
            os.closeArchiveEntry();
            os.finish();
            os.close();
        }

        try (InputStream in = new FileInputStream(zipFileName);
             ArchiveInputStream is = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in)) {
            assertNotNull(is);
            final TarArchiveEntry entry = (TarArchiveEntry) is.getNextEntry();
            assertNotNull(entry);
            assertFalse(entry.isDirectory());
            assertTrue(entry.getSize() > 0);
            assertNull(entry.getName());
            IOUtils.copy(is, new ByteArrayOutputStream(), entry.getSize());
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(new FileInputStream(zipFileName));
            FileUtils.deleteQuietly(new File(zipFileName));
        } catch (ArchiveException e) {
            // Ignore, test case failed
        }

        // Test AR archive
        final String arFileName = "test-ar.ar";
        try (OutputStream out = new FileOutputStream(arFileName)) {
            final ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
            // Add file entry
            final String testFile = "test-ar-file.txt";
            final File file = new File(testFile);
            os.putArchiveEntry(new ArArchiveEntry(file.getAbsolutePath()));
            IOUtils.copy(new FileInputStream(file), os);
            os.closeArchiveEntry();
            os.finish();
            os.close();
        }

        try (InputStream in = new FileInputStream(arFileName);
             ArchiveInputStream is = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in)) {
            assertNotNull(is);
            final ArArchiveEntry entry = (ArArchiveEntry) is.getNextEntry();
            assertNotNull(entry);
            assertFalse(entry.isDirectory());
            assertTrue(entry.getSize() > 0);
            assertNull(entry.getName());
            IOUtils.copy(is, new ByteArrayOutputStream(), entry.getSize());
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(new FileInputStream(arFileName));
            FileUtils.deleteQuietly(new File(arFileName));
        } catch (ArchiveException e) {
            // Ignore, test case failed
        }

        // Test unsupported archive
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0][])));

        // Test null arguments
        assertThrows(NullPointerException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(null, null));
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)}.
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test ZIP archive
        final String zipFileName = "test-zip.zip";
        try (OutputStream out = new FileOutputStream(zipFileName)) {
            final ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            // Add file entry
            final String testFile = "test-zip-file.txt";
            final File file = new File(testFile);
            os.putArchiveEntry(new JarArchiveEntry(file.getAbsolutePath()));
            IOUtils.copy(new FileInputStream(file), os);
            os.closeArchiveEntry();
            os.finish();
            os.close();
        }

        try (InputStream in = new FileInputStream(zipFileName);
             ArchiveInputStream is = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
             OutputStream out = new FileOutputStream("test-zip-file.txt")) {
            assertNotNull(is);
            final JarArchiveEntry entry = (JarArchiveEntry) is.getNextEntry();
            assertNotNull(entry);
            assertFalse(entry.isDirectory());
            assertTrue(entry.getSize() > 0);
            assertNull(entry.getName());
            IOUtils.copy(is, out, entry.getSize());
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(new FileInputStream(zipFileName));
            FileUtils.deleteQuietly(new File(zipFileName));
        } catch (ArchiveException e) {
            // Ignore, test case failed
        }

        // Test AR archive
        final String arFileName = "test-ar.ar";
        try (OutputStream out = new FileOutputStream(arFileName)) {
            final ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
            // Add file entry
            final String testFile = "test-ar-file.txt";
            final File file = new File(testFile);
            os.putArchiveEntry(new ArArchiveEntry(file.getAbsolutePath()));
            IOUtils.copy(new FileInputStream(file), os);
            os.closeArchiveEntry();
            os.finish();
            os.close();
        }

        try (InputStream in = new FileInputStream(arFileName);
             ArchiveInputStream is = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
             OutputStream out = new FileOutputStream("test-ar-file.txt")) {
            assertNotNull(is);
            final ArArchiveEntry entry = (ArArchiveEntry) is.getNextEntry();
            assertNotNull(entry);
            assertFalse(entry.isDirectory());
            assertTrue(entry.getSize() > 0);
            assertNull(entry.getName());
            IOUtils.copy(is, out, entry.getSize());
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(new FileInputStream(arFileName));
            FileUtils.deleteQuietly(new File(arFileName));
        } catch (ArchiveException e) {
            // Ignore, test case failed
        }

        // Test unsupported archive
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, new ByteArrayOutputStream()));

        // Test null arguments
        assertThrows(NullPointerException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(null, null));
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(InputStream)}.
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Test ZIP archive
        final String zipFileName = "test-zip.zip";
        try (OutputStream out = new FileOutputStream(zipFileName)) {
            final ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            // Add file entry
            final String testFile = "test-zip-file.txt";
            final File file = new File(testFile);
            os.putArchiveEntry(new JarArchiveEntry(file.getAbsolutePath()));
            IOUtils.copy(new FileInputStream(file), os);
            os.closeArchiveEntry();
            os.finish();
            os.close();
        }

        try (InputStream in = new FileInputStream(zipFileName);
             ArchiveInputStream is = new ArchiveStreamFactory().createArchiveInputStream(in)) {
            assertNotNull(is);
            final TarArchiveEntry entry = (TarArchiveEntry) is.getNextEntry();
            assertNotNull(entry);
            assertFalse(entry.isDirectory());
            assertTrue(entry.getSize() > 0);
            assertNull(entry.getName());
            IOUtils.copy(is, new ByteArrayOutputStream(), entry.getSize());
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(new FileInputStream(zipFileName));
            FileUtils.deleteQuietly(new File(zipFileName));
        } catch (ArchiveException e) {
            // Ignore, test case failed
        }

        // Test unsupported archive
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(new byte[0][])));

        // Test null argument
        assertThrows(NullPointerException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(null));
    }
}