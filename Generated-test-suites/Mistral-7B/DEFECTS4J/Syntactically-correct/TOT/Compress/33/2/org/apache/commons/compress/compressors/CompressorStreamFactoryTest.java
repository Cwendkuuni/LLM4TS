package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test for {@link CompressorStreamFactory} class.
 */
public class CompressorStreamFactoryTest {

    private final static byte[] TEST_DATA = "Test data for CompressorStreamFactoryTest".getBytes();

    /**
     * Test creating compressor input stream from known signature.
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamFromSignature() throws Exception {
        InputStream in = new ByteArrayInputStream(TEST_DATA);

        CompressorInputStream cos = new CompressorStreamFactory()
                .createCompressorInputStream(BZIP2, in);
        assertEquals(BZip2CompressorInputStream.class, cos.getClass());
        cos.close();

        in.reset();
        cos = new CompressorStreamFactory()
                .createCompressorInputStream(GZIP, in);
        assertEquals(GzipCompressorInputStream.class, cos.getClass());
        cos.close();

        in.reset();
        cos = new CompressorStreamFactory()
                .createCompressorInputStream(XZ, in);
        assertEquals(XZCompressorInputStream.class, cos.getClass());
        cos.close();

        in.reset();
        cos = new CompressorStreamFactory()
                .createCompressorInputStream(LZMA, in);
        assertEquals(LZMACompressorInputStream.class, cos.getClass());
        cos.close();

        in.reset();
        cos = new CompressorStreamFactory()
                .createCompressorInputStream(PACK200, in);
        assertEquals(Pack200CompressorInputStream.class, cos.getClass());
        cos.close();

        in.reset();
        cos = new CompressorStreamFactory()
                .createCompressorInputStream(SNAPPY_RAW, in);
        assertEquals(SnappyCompressorInputStream.class, cos.getClass());
        cos.close();

        in.reset();
        cos = new CompressorStreamFactory()
                .createCompressorInputStream(Z, in);
        assertEquals(ZCompressorInputStream.class, cos.getClass());
        cos.close();

        in.reset();
        cos = new CompressorStreamFactory()
                .createCompressorInputStream(DEFLATE, in);
        assertEquals(DeflateCompressorInputStream.class, cos.getClass());
        cos.close();

        in.close();
    }

    /**
     * Test creating compressor input stream from unknown signature.
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamFromUnknownSignature() throws Exception {
        InputStream in = new ByteArrayInputStream(TEST_DATA);

        in.mark(12);
        byte[] signature = new byte[12];
        IOUtils.readFully(in, signature);
        in.reset();

        try {
            new CompressorStreamFactory()
                    .createCompressorInputStream("unknown", in);
            assertFalse("CompressorStreamFactory should have thrown an exception.", true);
        } catch (CompressorException e) {
            // expected
        }

        in.close();
    }

    /**
     * Test creating compressor input stream with non-mark-supporting input stream.
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamWithNonMarkSupportedInputStream() throws Exception {
        InputStream in = new ByteArrayInputStream(TEST_DATA);

        try {
            new CompressorStreamFactory()
                    .createCompressorInputStream(BZIP2, in);
            assertFalse("CompressorStreamFactory should have thrown an exception.", true);
        } catch (IllegalArgumentException e) {
            // expected
        }

        in.close();
    }

    /**
     * Test creating compressor output stream from known name.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamFromName() throws Exception {
        OutputStream out = new ByteArrayOutputStream();

        CompressorOutputStream cos = new CompressorStreamFactory()
                .createCompressorOutputStream(BZIP2, out);
        assertEquals(BZip2CompressorOutputStream.class, cos.getClass());
        cos.close();

        out.reset();
        cos = new CompressorStreamFactory()
                .createCompressorOutputStream(GZIP, out);
        assertEquals(GzipCompressorOutputStream.class, cos.getClass());
        cos.close();

        out.reset();
        cos = new CompressorStreamFactory()
                .createCompressorOutputStream(XZ, out);
        assertEquals(XZCompressorOutputStream.class, cos.getClass());
        cos.close();

        out.reset();
        cos = new CompressorStreamFactory()
                .createCompressorOutputStream(LZMA, out);
        assertEquals(LZMACompressorOutputStream.class, cos.getClass());
        cos.close();

        out.reset();
        cos = new CompressorStreamFactory()
                .createCompressorOutputStream(PACK200, out);
        assertEquals(Pack200CompressorOutputStream.class, cos.getClass());
        cos.close();

        out.reset();
        cos = new CompressorStreamFactory()
                .createCompressorOutputStream(DEFLATE, out);
        assertEquals(DeflateCompressorOutputStream.class, cos.getClass());
        cos.close();

        out.close();
    }

    /**
     * Test creating compressor output stream from unknown name.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamFromUnknownName() throws Exception {
        OutputStream out = new ByteArrayOutputStream();

        try {
            new CompressorStreamFactory()
                    .createCompressorOutputStream("unknown", out);
            assertFalse("CompressorStreamFactory should have thrown an exception.", true);
        } catch (CompressorException e) {
            // expected
        }

        out.close();
    }

    /**
     * Test creating compressor input stream from known name and non-mark-supporting input stream.
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamFromNameWithNonMarkSupportedInputStream() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        InputStream in = new ByteArrayInputStream(TEST_DATA);

        try {
            CompressorOutputStream cos = new CompressorStreamFactory()
                    .createCompressorOutputStream(BZIP2, out);
            cos.close();
            CompressorInputStream cis = new CompressorStreamFactory()
                    .createCompressorInputStream(BZIP2, in);
            assertNull("CompressorStreamFactory should have returned null.", cis);
        } finally {
            in.close();
            out.close();
        }

        // test other compressors
        out = new ByteArrayOutputStream();
        in = new ByteArrayInputStream(TEST_DATA);

        try {
            CompressorOutputStream cos = new CompressorStreamFactory()
                    .createCompressorOutputStream(GZIP, out);
            cos.close();
            CompressorInputStream cis = new CompressorStreamFactory()
                    .createCompressorInputStream(GZIP, in);
            assertNull("CompressorStreamFactory should have returned null.", cis);
        } finally {
            in.close();
            out.close();
        }

        out = new ByteArrayOutputStream();
        in = new ByteArrayInputStream(TEST_DATA);

        try {
            CompressorOutputStream cos = new CompressorStreamFactory()
                    .createCompressorOutputStream(XZ, out);
            cos.close();
            CompressorInputStream cis = new CompressorStreamFactory()
                    .createCompressorInputStream(XZ, in);
            assertNull("CompressorStreamFactory should have returned null.", cis);
        } finally {
            in.close();
            out.close();
        }

        out = new ByteArrayOutputStream();
        in = new ByteArrayInputStream(TEST_DATA);

        try {
            CompressorOutputStream cos = new CompressorStreamFactory()
                    .createCompressorOutputStream(LZMA, out);
            cos.close();
            CompressorInputStream cis = new CompressorStreamFactory()
                    .createCompressorInputStream(LZMA, in);
            assertNull("CompressorStreamFactory should have returned null.", cis);
        } finally {
            in.close();
            out.close();
        }

        out = new ByteArrayOutputStream();
        in = new ByteArrayInputStream(TEST_DATA);

        try {
            CompressorOutputStream cos = new CompressorStreamFactory()
                    .createCompressorOutputStream(PACK200, out);
            cos.close();
            CompressorInputStream cis = new CompressorStreamFactory()
                    .createCompressorInputStream(PACK200, in);
            assertNull("CompressorStreamFactory should have returned null.", cis);
        } finally {
            in.close();
            out.close();
        }

        out = new ByteArrayOutputStream();
        in = new ByteArrayInputStream(TEST_DATA);

        try {
            CompressorOutputStream cos = new CompressorStreamFactory()
                    .createCompressorOutputStream(SNAPPY_RAW, out);
            cos.close();
            CompressorInputStream cis = new CompressorStreamFactory()
                    .createCompressorInputStream(SNAPPY_RAW, in);
            assertNull("CompressorStreamFactory should have returned null.", cis);
        } finally {
            in.close();
            out.close();
        }

        out = new ByteArrayOutputStream();
        in = new ByteArrayInputStream(TEST_DATA);

        try {
            CompressorOutputStream cos = new CompressorStreamFactory()
                    .createCompressorOutputStream(SNAPPY_FRAMED, out);
            cos.close();
            CompressorInputStream cis = new CompressorStreamFactory()
                    .createCompressorInputStream(SNAPPY_FRAMED, in);
            assertNull("CompressorStreamFactory should have returned null.", cis);
        } finally {
            in.close();
            out.close();
        }

        out = new ByteArrayOutputStream();
        in = new ByteArrayInputStream(TEST_DATA);

        try {
            CompressorOutputStream cos = new CompressorStreamFactory()
                    .createCompressorOutputStream(DEFLATE, out);
            cos.close();
            CompressorInputStream cis = new CompressorStreamFactory()
                    .createCompressorInputStream(DEFLATE, in);
            assertNull("CompressorStreamFactory should have returned null.", cis);
        } finally {
            in.close();
            out.close();
        }
    }

    /**
     * Test creating compressor output stream with null name.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithNullName() throws Exception {
        OutputStream out = new ByteArrayOutputStream();

        assertThrows(NullPointerException.class, () -> 
                new CompressorStreamFactory().createCompressorOutputStream(null, out));
    }

    /**
     * Test creating compressor output stream with null output stream.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithNullOutputStream() throws Exception {
        String name = BZIP2;

        assertThrows(NullPointerException.class, () -> 
                new CompressorStreamFactory().createCompressorOutputStream(name, null));
    }
}