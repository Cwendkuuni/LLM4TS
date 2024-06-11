package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.test.TestUtils;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Test for ArchiveStreamFactory class
 */
public class ArchiveStreamFactoryTest {

    @org.junit.Test
    public void testCreateInputStream() throws Exception {
        String archiveName = "zip";
        InputStream in = new ByteArrayInputStream(TestUtils.getTestFileBytes("test-data/test.zip"));
        ArchiveInputStream expected = new ZipArchiveInputStream(in);
        ArchiveInputStream actual = new ArchiveStreamFactory().createArchiveInputStream(archiveName, in);
        assertSame(expected.getClass(), actual.getClass());
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateInputStreamUnknownFormat() throws Exception {
        String archiveName = "foobar";
        InputStream in = new ByteArrayInputStream(TestUtils.getTestFileBytes("test-data/test.zip"));
        new ArchiveStreamFactory().createArchiveInputStream(archiveName, in);
    }

    @org.junit.Test
    public void testCreateOutputStream() throws Exception {
        String archiveName = "tar";
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream expected = new TarArchiveOutputStream(out);
        ArchiveOutputStream actual = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, out);
        assertSame(expected.getClass(), actual.getClass());
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateOutputStreamUnsupportedFormat() throws Exception {
        String archiveName = "7z";
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, out);
    }

    @org.junit.Test
    public void testCreateInputStreamFromUnknownFormatAutoDetect() throws Exception {
        String archiveName = null;
        InputStream in = new ByteArrayInputStream(TestUtils.getTestFileBytes("test-data/test.zip"));
        ArchiveInputStream actual = new ArchiveStreamFactory().createArchiveInputStream(archiveName, in);
        assertInstanceOf(ZipArchiveInputStream.class, actual);
    }

    @org.junit.Test
    public void testCreateInputStreamFromValidArchiveHeader() throws Exception {
        String archiveName = null;
        InputStream in = new ByteArrayInputStream(TestUtils.getTestFileBytes("test-data/test.tar"));
        ArchiveInputStream actual = new ArchiveStreamFactory().createArchiveInputStream(archiveName, in);
        assertInstanceOf(TarArchiveInputStream.class, actual);
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateInputStreamFromInvalidArchiveHeader() throws Exception {
        String archiveName = null;
        InputStream in = new ByteArrayInputStream(TestUtils.getTestFileBytes("test-data/test.dat"));
        new ArchiveStreamFactory().createArchiveInputStream(archiveName, in);
    }

    @org.junit.Test
    public void testCreateOutputStreamWithEncoding() throws Exception {
        String archiveName = "zip";
        String encoding = "UTF-8";
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream expected = new ZipArchiveOutputStream(out, encoding);
        ArchiveOutputStream actual = new ArchiveStreamFactory(encoding).createArchiveOutputStream(archiveName, out);
        assertSame(expected.getClass(), actual.getClass());
        assertEquals(encoding, actual.getEncoding());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateOutputStreamWithNullOutputStream() throws Exception {
        String archiveName = "zip";
        OutputStream out = null;
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, out);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateOutputStreamWithNullArchiverName() throws Exception {
        String archiveName = null;
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, out);
    }

    @org.junit.Test
    public void testCloseArchiveInputStream() throws Exception {
        String archiveName = "zip";
        InputStream in = new ByteArrayInputStream(TestUtils.getTestFileBytes("test-data/test.zip"));
        ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, in);
        inputStream.close();
        try {
            inputStream.getNextEntry();
            fail();
        } catch (EOFException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testCloseArchiveOutputStream() throws Exception {
        String archiveName = "tar";
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, out);
        outputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        outputStream.closeArchiveEntry();
        outputStream.close();
        byte[] bytes = out.toByteArray();
        byte[] expected = TestUtils.getTestFileBytes("test-data/test.tar");
        // test that all bytes are equal
        byte[] result = new byte[bytes.length < expected.length ? bytes.length : expected.length];
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        System.arraycopy(expected, 0, result, bytes.length, expected.length - bytes.length);
        assertArrayEquals(expected, result);
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateInputStreamFromNullInputStream() throws Exception {
        String archiveName = "zip";
        InputStream in = null;
        new ArchiveStreamFactory().createArchiveInputStream(archiveName, in);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateOutputStreamFromNullInputStream() throws Exception {
        String archiveName = "zip";
        InputStream in = new ByteArrayInputStream(TestUtils.getTestFileBytes("test-data/test.zip"));
        OutputStream out = null;
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, out, in);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateOutputStreamWithNullArchiverName() throws Exception {
        String archiveName = null;
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, out);
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateOutputStreamFromNonWritableOutputStream() throws Exception {
        String archiveName = "zip";
        OutputStream out = new ByteArrayOutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("write operation not supported");
            }
        };
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, out);
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateInputStreamFromInvalidStreamSignature() throws Exception {
        String archiveName = null;
        InputStream in = new ByteArrayInputStream(TestUtils.getTestFileBytes("test-data/test.dat"));
        new ArchiveStreamFactory().createArchiveInputStream(archiveName, in);
    }

    @org.junit.Test
    public void testCreateInputStreamFromValidStreamSignature() throws Exception {
        String archiveName = null;
        InputStream in = new ByteArrayInputStream(TestUtils.getTestFileBytes("test-data/test.zip"));
        ArchiveInputStream actual = new ArchiveStreamFactory().createArchiveInputStream(archiveName, in);
        assertInstanceOf(ZipArchiveInputStream.class, actual);
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateOutputStreamWithInvalidArchiverName() throws Exception {
        String archiveName = "foobar";
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, out);
    }

    @org.junit.Test
    public void testCreateOutputStreamWithValidArchiverName() throws Exception {
        String archiveName = "tar";
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream actual = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, out);
        assertInstanceOf(TarArchiveOutputStream.class, actual);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateOutputStreamWithNullEntryName() throws Exception {
        String archiveName = "tar";
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, out).putNextEntry(null);
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateOutputStreamWithNullInputStream() throws Exception {
        String archiveName = "tar";
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, null, new ByteArrayInputStream(TestUtils.getTestFileBytes("test-data/test.tar")));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateOutputStreamWithNullInputStream2() throws Exception {
        String archiveName = "tar";
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateOutputStreamWithNullOutputStream() throws Exception {
        String archiveName = "tar";
        InputStream in = new ByteArrayInputStream(TestUtils.getTestFileBytes("test-data/test.tar"));
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, null, in);
    }

    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateOutputStreamWithInvalidFormat() throws Exception {
        String archiveName = "7z";
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, out);
    }

}