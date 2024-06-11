package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * CompressorStreamFactoryTest
 */
public class CompressorStreamFactoryTest {

    /**
     * Test createCompressorInputStream() for known compressor types.
     */
    @Test
    public void testCreateCompressorInputStreamForKnownTypes() throws IOException {
        // Test GZIP
        CompressorInputStream gzipInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP,
                        new ByteArrayInputStream(new byte[]{}));
        assertEquals("Expected GzipCompressorInputStream",
                GzipCompressorInputStream.class, gzipInputStream.getClass());

        // Test BZIP2
        CompressorInputStream bzip2InputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2,
                        new ByteArrayInputStream(new byte[]{}));
        assertEquals("Expected BZip2CompressorInputStream",
                BZip2CompressorInputStream.class, bzip2InputStream.getClass());

        // Test DEFLATE
        CompressorInputStream deflateInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.DEFLATE,
                        new ByteArrayInputStream(new byte[]{}));
        assertEquals("Expected DeflateCompressorInputStream",
                DeflateCompressorInputStream.class, deflateInputStream.getClass());

        // Test XZ
        CompressorInputStream xzInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.XZ,
                        new ByteArrayInputStream(new byte[]{}));
        assertEquals("Expected XZCompressorInputStream",
                XZCompressorInputStream.class, xzInputStream.getClass());
    }

    /**
     * Test createCompressorInputStream() for unknown compressor type.
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamForUnknownType() throws IOException {
        new CompressorStreamFactory()
                .createCompressorInputStream("UnknownCompressorType",
                        new ByteArrayInputStream(new byte[]{}));
    }

    /**
     * Test createCompressorOutputStream() for known compressor types.
     */
    @Test
    public void testCreateCompressorOutputStreamForKnownTypes() throws IOException {
        // Test GZIP
        CompressorOutputStream gzipOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP,
                        new ByteArrayOutputStream());
        assertEquals("Expected GzipCompressorOutputStream",
                GzipCompressorOutputStream.class, gzipOutputStream.getClass());

        // Test BZIP2
        CompressorOutputStream bzip2OutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2,
                        new ByteArrayOutputStream());
        assertEquals("Expected BZip2CompressorOutputStream",
                BZip2CompressorOutputStream.class, bzip2OutputStream.getClass());

        // Test DEFLATE
        CompressorOutputStream deflateOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.DEFLATE,
                        new ByteArrayOutputStream());
        assertEquals("Expected DeflateCompressorOutputStream",
                DeflateCompressorOutputStream.class, deflateOutputStream.getClass());

        // Test XZ
        CompressorOutputStream xzOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.XZ,
                        new ByteArrayOutputStream());
        assertEquals("Expected XZCompressorOutputStream",
                XZCompressorOutputStream.class, xzOutputStream.getClass());
    }

    /**
     * Test createCompressorOutputStream() for unknown compressor type.
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamForUnknownType() throws IOException {
        new CompressorStreamFactory()
                .createCompressorOutputStream("UnknownCompressorType",
                        new ByteArrayOutputStream());
    }

    /**
     * Test createCompressorInputStream() with malformed data.
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamWithMalformedData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2,
                        inputStream);
    }

    /**
     * Test createCompressorOutputStream() with null output stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamWithNullOutputStream() {
        new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, null);
    }

    /**
     * Test createCompressorOutputStream() with output stream that does not support writes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamWithOutputStreamThatDoesNotSupportWrites() {
        OutputStream outputStream = new ByteArrayOutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Cannot write to this OutputStream");
            }
        };

        new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
    }

    /**
     * Test setDecompressConcatenated() method.
     */
    @Test
    public void testSetDecompressConcatenated() {
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();
        assertFalse("Expected decompressConcatenated to be false",
                compressorStreamFactory.getDecompressConcatenated());

        CompressorStreamFactory compressorStreamFactory2 = new CompressorStreamFactory(true);
        assertTrue("Expected decompressConcatenated to be true",
                compressorStreamFactory2.getDecompressConcatenated());

        compressorStreamFactory.setDecompressConcatenated(true);
        assertTrue("Expected decompressConcatenated to be true",
                compressorStreamFactory.getDecompressConcatenated());
    }

    /**
     * Test setDecompressConcatenated() method with already set value.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetDecompressConcatenatedWithAlreadySetValue() {
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory(true);
        compressorStreamFactory.setDecompressConcatenated(true);
    }
}