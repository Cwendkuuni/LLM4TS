package org.apache.commons.compress.archivers.test;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test cases for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creating a valid AR archive output stream.
     * @throws ArchiveException if the test fails
     */
    @Test
    public void testCreateArArchiveOutputStream() throws ArchiveException {
        try (ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream())) {
            // Create an entry and write some data
            out.putArchiveEntry(new ArArchiveEntry("test.txt"));
            out.write(new byte[]{"T", "e", "s", "t", ".", "t", "x", "t"}, 0, 7);
            out.closeArchiveEntry();
            out.finish();

            // Create an input stream and read the data back
            ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(out.toByteArray()));
            Assert.assertNotNull(in.getNextEntry());
            byte[] buffer = new byte[8];
            int len = in.read(buffer, 0, 8);
            Assert.assertEquals(7, len);
            Assert.assertArrayEquals(new byte[]{"T", "e", "s", "t", ".", "t", "x", "t"}, buffer, 0, 7);
            in.close();
        } catch (IOException e) {
            throw new ArchiveException("Failed to write or read data.", e);
        }
    }

    /**
     * Test creating a valid ZIP archive output stream.
     * @throws ArchiveException if the test fails
     */
    @Test
    public void testCreateZipArchiveOutputStream() throws ArchiveException {
        try (ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream())) {
            // Create an entry and write some data
            out.putNextEntry(new ZipEntry("test.txt"));
            out.write(new byte[]{"T", "e", "s", "t", ".", "t", "x", "t"}, 0, 7);
            out.closeEntry();
            out.finish();

            // Create an input stream and read the data back
            ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(out.toByteArray()));
            Assert.assertNotNull(in.getNextEntry());
            byte[] buffer = new byte[8];
            int len = in.read(buffer, 0, 8);
            Assert.assertEquals(7, len);
            Assert.assertArrayEquals(new byte[]{"T", "e", "s", "t", ".", "t", "x", "t"}, buffer, 0, 7);
            in.close();
        } catch (IOException e) {
            throw new ArchiveException("Failed to write or read data.", e);
        }
    }

    /**
     * Test creating an invalid archive output stream.
     * @throws ArchiveException if the test fails
     */
    @Test(expected = ArchiveException.class)
    public void testCreateInvalidArchiveOutputStream() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveOutputStream("invalid", new ByteArrayOutputStream());
    }

    /**
     * Test creating a valid TAR archive input stream.
     * @throws ArchiveException if the test fails
     */
    @Test
    public void testCreateTarArchiveInputStream() throws ArchiveException {
        try (ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
             ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(out.toByteArray()))) {

            // Write a header and create an entry
            out.putNextEntry(new TarEntry("test.txt"));
            out.write(new byte[]{"T", "e", "s", "t", ".", "t", "x", "t"}, 0, 7);
            out.closeEntry();
            out.finish();

            // Read the header and entry data
            TarEntry entry = (TarEntry) in.getNextEntry();
            Assert.assertNotNull(entry);
            Assert.assertEquals("test.txt", entry.getName());
            byte[] buffer = new byte[8];
            int len = in.read(buffer, 0, 8);
            Assert.assertEquals(7, len);
            Assert.assertArrayEquals(new byte[]{"T", "e", "s", "t", ".", "t", "x", "t"}, buffer, 0, 7);
            in.close();
        } catch (IOException e) {
            throw new ArchiveException("Failed to write or read data.", e);
        }
    }

    /**
     * Test creating a valid JAR archive input stream.
     * @throws ArchiveException if the test fails
     */
    @Test
    public void testCreateJarArchiveInputStream() throws ArchiveException {
        try (ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream());
             ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(out.toByteArray()))) {

            // Write a manifest and create an entry
            out.putNextEntry(new JarEntry("test.txt"));
            out.write(new byte[]{"T", "e", "s", "t", ".", "t", "x", "t"}, 0, 7);
            out.closeEntry();
            out.finish();

            // Read the manifest and entry data
            JarEntry entry = (JarEntry) in.getNextEntry();
            Assert.assertNotNull(entry);
            Assert.assertEquals("test.txt", entry.getName());
            byte[] buffer = new byte[8];
            int len = in.read(buffer, 0, 8);
            Assert.assertEquals(7, len);
            Assert.assertArrayEquals(new byte[]{"T", "e", "s", "t", ".", "t", "x", "t"}, buffer, 0, 7);
            in.close();
        } catch (IOException e) {
            throw new ArchiveException("Failed to write or read data.", e);
        }
    }

    /**
     * Test creating an invalid archive input stream.
     * @throws ArchiveException if the test fails
     */
    @Test(expected = ArchiveException.class)
    public void testCreateInvalidArchiveInputStream() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[0]));
    }
}