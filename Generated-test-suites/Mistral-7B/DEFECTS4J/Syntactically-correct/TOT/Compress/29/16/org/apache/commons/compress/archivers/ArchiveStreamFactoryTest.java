package org.apache.commons.compress.archivers.test;

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
import java.util.zip.DataFormatException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveFormatException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.format.CpioFormat;
import org.apache.commons.compress.archivers.format.TarFormat;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Tests for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test createArchiveInputStream.
     */
    @org.junit.Test
    public void testCreateArchiveInputStream() throws Exception {
        final InputStream testStream = new ByteArrayInputStream(
                new byte[] { 0x07, 'P', 'K', 'd', 'a', 't', 'a', ' ', '1', '.', '0', 'b', ' ', '7', 'z', ' ', 'i', 'n', 'f',
                             'o', 'r', 'm', 'a', 't', 'i', 'o', 'n' });

        final ArchiveInputStream in = new ArchiveStreamFactory()
                .createArchiveInputStream("tar", testStream);
        assertNotNull(in);
        assertTrue(in instanceof TarArchiveInputStream);
        assertFalse(in.hasNextEntry());

        in.close();
    }

    /**
     * Test createArchiveInputStream with unknown archive format.
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamWithUnknownFormat() throws Exception {
        final InputStream testStream = new ByteArrayInputStream(new byte[0]);

        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream("unknownformat", testStream));
    }

    /**
     * Test createArchiveOutputStream.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStream() throws Exception {
        final OutputStream out = new ByteArrayOutputStream();
        final ArchiveOutputStream os = new ArchiveStreamFactory()
                .createArchiveOutputStream("tar", out);
        assertNotNull(os);
        assertTrue(os instanceof TarArchiveOutputStream);
        assertNull(os.getNextEntry());

        os.finish();
        os.close();

        final InputStream in = new ByteArrayInputStream(out.toByteArray());
        final TarArchiveInputStream tarIn = (TarArchiveInputStream) new ArchiveStreamFactory()
                .createArchiveInputStream("tar", in);
        assertNotNull(tarIn);
        assertFalse(tarIn.hasNextEntry());
    }

    /**
     * Test createArchiveOutputStream with unknown archive format.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamWithUnknownFormat() throws Exception {
        final OutputStream out = new ByteArrayOutputStream();

        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveOutputStream("unknownformat", out));
    }

    /**
     * Test createArchiveOutputStream with encoding.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamWithEncoding() throws Exception {
        final OutputStream out = new ByteArrayOutputStream();

        final ArchiveOutputStream os = new ArchiveStreamFactory()
                .createArchiveOutputStream("tar", out, "UTF-8");
        assertNotNull(os);
        assertTrue(os instanceof TarArchiveOutputStream);
        assertNull(os.getNextEntry());

        os.finish();
        os.close();

        final InputStream in = new ByteArrayInputStream(out.toByteArray());
        final TarArchiveInputStream tarIn = (TarArchiveInputStream) new ArchiveStreamFactory()
                .createArchiveInputStream("tar", in, "UTF-8");
        assertNotNull(tarIn);
        assertFalse(tarIn.hasNextEntry());
    }

    /**
     * Test createArchiveInputStream from stream with unknown format.
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamFromStreamWithUnknownFormat() throws Exception {
        final InputStream testStream = new ByteArrayInputStream(new byte[0]);

        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream("", testStream));
    }

    /**
     * Test createArchiveInputStream from stream with CPIO format.
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamFromStreamWithCpioFormat() throws Exception {
        final InputStream testStream = new ByteArrayInputStream(
                new byte[] { '0', '1', '0', '0', '0', '1', '1', '0', '1', '2', '0', '0', '7', '5', 'c', 'p', 'i', 'o',
                             '1', '0', '2', '0', '1', '1', '0', '1' });
        final ArchiveInputStream in = new ArchiveStreamFactory()
                .createArchiveInputStream("cpio", testStream);
        assertNotNull(in);
        assertTrue(in instanceof CpioArchiveInputStream);
        final ArchiveEntry entry = in.getNextEntry();
        assertNotNull(entry);
        assertTrue(entry.getName().equals(""));
        assertTrue(entry.getSize() == 1);
        assertTrue(entry.getCompressedSize() == 0);
        assertFalse(in.hasNextEntry());

        in.close();
    }

    /**
     * Test createArchiveInputStream from stream with invalid CPIO format.
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamFromStreamWithInvalidCpioFormat() throws Exception {
        final InputStream testStream = new ByteArrayInputStream(
                new byte[] { '0', '1', '0', '0', '0', '1', '1', '0', '1', '2', '1', '0', '7', '5', 'c', 'p', 'i', 'o',
                             '1', '0', '2', '0', '1', '1', '0', '1' });
        assertThrows(ArchiveFormatException.class, () -> new ArchiveStreamFactory()
                .createArchiveInputStream("cpio", testStream));
    }

    /**
     * Test createArchiveOutputStream with compress stream.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamWithCompressStream() throws Exception {
        final OutputStream out = new ByteArrayOutputStream();
        final ArchiveOutputStream os = new ArchiveStreamFactory()
                .createArchiveOutputStream("zip", out);
        assertNotNull(os);
        assertTrue(os instanceof ZipArchiveOutputStream);
        assertNull(os.getNextEntry());

        final InputStream is = new ByteArrayInputStream(
                new byte[] { 'P', 'K', 'd', 'a', 't', 'a', ' ', '1', '.', '0', 'b' });
        os.putArchiveEntry(new ZipArchiveEntry("test.txt"), is);
        os.finish();
        os.close();

        final InputStream testStream = new ByteArrayInputStream(out.toByteArray());
        final ZipArchiveInputStream zipIn = (ZipArchiveInputStream) new ArchiveStreamFactory()
                .createArchiveInputStream("zip", testStream);
        assertNotNull(zipIn);
        assertFalse(zipIn.hasNextEntry());

        final ArchiveEntry entry = zipIn.getNextEntry();
        assertNotNull(entry);
        assertTrue(entry.getName().equals("test.txt"));
        assertFalse(entry.isDirectory());
        assertTrue(entry.getSize() == 5);
        assertTrue(entry.getCompressedSize() == 5);

        final byte[] data = new byte[5];
        IOUtils.readFully(zipIn, data, 0, 5);
        assertTrue(new String(data).equals("datatest"));

        zipIn.close();
    }

    /**
     * Test createArchiveOutputStream with invalid compress stream.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamWithInvalidCompressStream() throws Exception {
        final OutputStream out = new ByteArrayOutputStream();

        assertThrows(DataFormatException.class, () -> new ArchiveStreamFactory()
                .createArchiveOutputStream("zip", out, new ByteArrayInputStream(new byte[0])));
    }
}