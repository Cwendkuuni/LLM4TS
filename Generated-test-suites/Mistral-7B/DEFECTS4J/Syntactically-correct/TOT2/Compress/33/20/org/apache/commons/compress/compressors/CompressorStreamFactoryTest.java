package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test cases for {@link CompressorStreamFactory}.
 */
public class CompressorStreamFactoryTest {

    /**
     * Test that createCompressorInputStream correctly identifies different
     * compression methods based on InputStream signature.
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamFromSignature() throws IOException {
        final String testData = "Test data.";

        // Test BZip2
        final InputStream bzip2Is = new ByteArrayInputStream(BZip2CompressorInputStream.createTestData());
        try (CompressorInputStream in = CompressorStreamFactory.getInstance().createCompressorInputStream(bzip2Is)) {
            assertEquals(BZip2CompressorInputStream.class, in.getClass());
        }

        // Test Gzip
        final InputStream gzipIs = new ByteArrayInputStream(GzipCompressorInputStream.createTestData());
        try (CompressorInputStream in = CompressorStreamFactory.getInstance().createCompressorInputStream(gzipIs)) {
            assertEquals(GzipCompressorInputStream.class, in.getClass());
        }

        // Test XZ
        final InputStream xzIs = new ByteArrayInputStream(XZCompressorInputStream.createTestData());
        try (CompressorInputStream in = CompressorStreamFactory.getInstance().createCompressorInputStream(xzIs)) {
            assertEquals(XZCompressorInputStream.class, in.getClass());
        }

        // Test Z
        final InputStream zIs = new ByteArrayInputStream(ZCompressorInputStream.createTestData());
        try (CompressorInputStream in = CompressorStreamFactory.getInstance().createCompressorInputStream(zIs)) {
            assertEquals(ZCompressorInputStream.class, in.getClass());
        }

        // Test Deflate
        final InputStream deflateIs = new ByteArrayInputStream(DeflateCompressorInputStream.createTestData());
        try (CompressorInputStream in = CompressorStreamFactory.getInstance().createCompressorInputStream(deflateIs)) {
            assertEquals(DeflateCompressorInputStream.class, in.getClass());
        }

        // Test invalid signature
        final InputStream invalidIs = new ByteArrayInputStream(new byte[13]);
        assertThrows(CompressorException.class, () -> {
            CompressorStreamFactory.getInstance().createCompressorInputStream(invalidIs);
        });
    }

    /**
     * Test that createCompressorOutputStream correctly creates output streams for
     * different compression methods.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStream() throws IOException {
        final String testData = "Test data.";

        // Test BZip2
        try (final OutputStream out = new ByteArrayOutputStream()) {
            try (final CompressorOutputStream compressorOutputStream = CompressorStreamFactory.getInstance()
                    .createCompressorOutputStream(CompressorStreamFactory.BZIP2, out)) {
                compressorOutputStream.write(testData.getBytes());
                compressorOutputStream.finish();
                assertArrayEquals(BZip2CompressorOutputStream.createTestData(), out.toByteArray());
            }
        }

        // Test Gzip
        try (final OutputStream out = new ByteArrayOutputStream()) {
            try (final CompressorOutputStream compressorOutputStream = CompressorStreamFactory.getInstance()
                    .createCompressorOutputStream(CompressorStreamFactory.GZIP, out)) {
                compressorOutputStream.write(testData.getBytes());
                compressorOutputStream.finish();
                assertArrayEquals(GzipCompressorOutputStream.createTestData(), out.toByteArray());
            }
        }

        // Test XZ
        try (final OutputStream out = new ByteArrayOutputStream()) {
            try (final CompressorOutputStream compressorOutputStream = CompressorStreamFactory.getInstance()
                    .createCompressorOutputStream(CompressorStreamFactory.XZ, out)) {
                compressorOutputStream.write(testData.getBytes());
                compressorOutputStream.finish();
                assertArrayEquals(XZCompressorOutputStream.createTestData(), out.toByteArray());
            }
        }

        // Test Deflate
        try (final OutputStream out = new ByteArrayOutputStream()) {
            try (final CompressorOutputStream compressorOutputStream = CompressorStreamFactory.getInstance()
                    .createCompressorOutputStream(CompressorStreamFactory.DEFLATE, out)) {
                compressorOutputStream.write(testData.getBytes());
                compressorOutputStream.finish();
                assertArrayEquals(DeflateCompressorOutputStream.createTestData(), out.toByteArray());
            }
        }
    }

    /**
     * Test that createCompressorInputStream returns null when the given compressor
     * name is not known.
     */
    @org.junit.Test
    public void testCreateUnknownCompressorInputStream() throws IOException {
        assertNull(CompressorStreamFactory.getInstance().createCompressorInputStream("unknown", new ByteArrayInputStream(new byte[0])));
    }

    /**
     * Test that createCompressorOutputStream returns null when the given compressor
     * name is not known.
     */
    @org.junit.Test
    public void testCreateUnknownCompressorOutputStream() throws IOException {
        assertNull(CompressorStreamFactory.getInstance().createCompressorOutputStream("unknown", new ByteArrayOutputStream()));
    }

    /**
     * Test that setDecompressConcatenated can be set and gets properly propagated.
     */
    @org.junit.Test
    public void testSetDecompressConcatenated() throws IOException {
        final InputStream bzip2Is = new ByteArrayInputStream(BZip2CompressorInputStream.createTestData());
        final CompressorStreamFactory factory = new CompressorStreamFactory(true);

        try (final CompressorInputStream in = factory.createCompressorInputStream(bzip2Is)) {
            assertTrue(factory.getDecompressConcatenated());
        }

        final CompressorStreamFactory factory2 = new CompressorStreamFactory();
        try (final CompressorInputStream in = factory2.createCompressorInputStream(bzip2Is)) {
            assertFalse(factory2.getDecompressConcatenated());
        }
    }
}