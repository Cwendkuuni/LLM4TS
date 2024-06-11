package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test for {@link CompressorStreamFactory}.
 */
public class CompressorStreamFactoryTest {

    /**
     * Test of {@link CompressorStreamFactory#createCompressorInputStream(String, InputStream)}.
     */
    @org.junit.Test
    public void testCreateCompressorInputStream() throws IOException {
        // Test Gzip case
        InputStream in = new ByteArrayInputStream(
                new byte[] { 0x1f, 0x8b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00,
                        0x02, 0x4c, 0x01, 0x01, 0x00, 0x00, 0x02, 0x4c });
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("gzip", in);
        assertTrue(compressorInputStream instanceof GzipCompressorInputStream);
        assertFalse(compressorInputStream instanceof BZip2CompressorInputStream);
        compressorInputStream.close();

        // Test Bzip2 case
        InputStream inb = new ByteArrayInputStream(
                new byte[] { 0x42, 0x5a, 0x68, 0x64, 0x67, 0x65, 0x20, 0x62,
                        0x79, 0x20, 0x1c, 0x6b, 0x75, 0x74, 0x49, 0x20,
                        0x1c, 0x63, 0x6f, 0x6d, 0x70, 0x65, 0x20, 0x63,
                        0x72, 0x41, 0x6e, 0x61, 0x53, 0x74, 0x72, 0x69,
                        0x6e, 0x67, 0x00, 0x00, 0x08, 0x01, 0x01, 0x00,
                        0x00, 0x02, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00,
                        0x02, 0x00, 0x01, 0x00, 0x01 });
        CompressorInputStream compressorInputStreamb = factory.createCompressorInputStream("bzip2", inb);
        assertTrue(compressorInputStreamb instanceof BZip2CompressorInputStream);
        assertFalse(compressorInputStreamb instanceof GzipCompressorInputStream);
        compressorInputStreamb.close();

        // Test unknown case
        InputStream invalid = new ByteArrayInputStream(
                new byte[] { 0x01, 0x02, 0x03, 0x04 });
        try {
            CompressorInputStream compressorInputStreamInvalid = factory
                    .createCompressorInputStream("unknown", invalid);
            assertFalse("Expected CompressorException", true);
        } catch (CompressorException e) {
            // Expected
        }

        // Test null InputStream case
        assertNull(factory.createCompressorInputStream(null));
    }

    /**
     * Test of {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStream() throws IOException {
        // Test Gzip case
        OutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("gzip", out);
        assertTrue(compressorOutputStream instanceof GzipCompressorOutputStream);
        assertFalse(compressorOutputStream instanceof BZip2CompressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();
        byte[] expectedGzipBytes = new byte[] { 0x1f, 0x8b, 0x08, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x02, 0x4c, 0x01, 0x01, 0x00, 0x00,
                0x02, 0x4c };
        byte[] actualGzipBytes = ((ByteArrayOutputStream) out).toByteArray();
        assertArrayEquals(expectedGzipBytes, actualGzipBytes);

        // Test Bzip2 case
        OutputStream outb = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStreamb = new CompressorStreamFactory()
                .createCompressorOutputStream("bzip2", outb);
        assertTrue(compressorOutputStreamb instanceof BZip2CompressorOutputStream);
        assertFalse(compressorOutputStreamb instanceof GzipCompressorOutputStream);
        compressorOutputStreamb.finish();
        compressorOutputStreamb.close();
        byte[] expectedBzip2Bytes = new byte[] { 0x42, 0x5a, 0x68, 0x64,
                0x67, 0x65, 0x20, 0x62, 0x79, 0x20, 0x1c, 0x6b, 0x75, 0x74,
                0x49, 0x20, 0x1c, 0x63, 0x6f, 0x6d, 0x70, 0x65, 0x20, 0x63,
                0x72, 0x41, 0x6e, 0x61, 0x53, 0x74, 0x72, 0x69, 0x6e, 0x67,
                0x00, 0x00, 0x08, 0x01, 0x01, 0x00, 0x00, 0x02, 0x00, 0x01,
                0x00, 0x01, 0x00, 0x00, 0x02, 0x00, 0x01, 0x00, 0x01 };
        byte[] actualBzip2Bytes = ((ByteArrayOutputStream) outb).toByteArray();
        assertArrayEquals(expectedBzip2Bytes, actualBzip2Bytes);

        // Test unknown case
        OutputStream outu = new ByteArrayOutputStream();
        try {
            CompressorOutputStream compressorOutputStreamu = new CompressorStreamFactory()
                    .createCompressorOutputStream("unknown", outu);
            assertFalse("Expected CompressorException", true);
        } catch (CompressorException e) {
            // Expected
        }

        // Test null OutputStream case
        assertSame("Expected null", null,
                new CompressorStreamFactory().createCompressorOutputStream(null));
    }

    /**
     * Test of {@link CompressorStreamFactory#setDecompressConcatenated(boolean)}.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testSetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(false);
        factory.setDecompressConcatenated(true);
    }

    /**
     * Test of {@link CompressorStreamFactory#getDecompressConcatenated()}.
     */
    @org.junit.Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
        CompressorStreamFactory factory2 = new CompressorStreamFactory();
        assertFalse(factory2.getDecompressConcatenated());
    }
}