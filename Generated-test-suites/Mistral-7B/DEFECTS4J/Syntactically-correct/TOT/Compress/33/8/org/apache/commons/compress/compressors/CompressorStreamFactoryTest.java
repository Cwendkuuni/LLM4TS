package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import org.apache.commons.compress.compressors.InvalidFormatException;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * Test the CompressorStreamFactory.
 */
public class CompressorStreamFactoryTest {

    /**
     * Test creating a compressor input stream with a known format from an InputStream.
     */
    @Test
    public void testCreateCompressorInputStream() throws Exception {
        InputStream in = new ByteArrayInputStream(
                new byte[]{"ID1B", "ID2B", "ID3B", "ID4B", "ID5B"}); // bzip2 signature

        CompressorInputStream is = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, in);
        assertNotNull(is);
        assertTrue(is instanceof BZip2CompressorInputStream);

        InputStream inGzip = new ByteArrayInputStream(
                new byte[]{0x1F, 0x8B, 0x08, 0x00, 0x1B, 0xF9, 0x44, 0x44, 0x12, 0x21, 0x0B}); // gzip signature

        CompressorInputStream isGzip = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, inGzip);
        assertNotNull(isGzip);
        assertTrue(isGzip instanceof GzipCompressorInputStream);

        InputStream inUnknown = new ByteArrayInputStream(
                new byte[]{0x12, 0x34, 0x56, 0x78, 0x90}); // unknown signature

        try {
            CompressorInputStream isUnknown = new CompressorStreamFactory()
                    .createCompressorInputStream(CompressorStreamFactory.BZIP2, inUnknown);
            assertFalse(true);
        } catch (CompressorException e) {
            // expected
        }

        try {
            CompressorInputStream isUnknown = new CompressorStreamFactory()
                    .createCompressorInputStream(CompressorStreamFactory.GZIP, inUnknown);
            assertFalse(true);
        } catch (CompressorException e) {
            // expected
        }

        // Test unknown compressor
        try {
            CompressorInputStream isUnknown = new CompressorStreamFactory()
                    .createCompressorInputStream("unknown", in);
            assertNull(isUnknown);
        } catch (CompressorException e) {
            // expected
        }
    }

    /**
     * Test creating a compressor input stream from a compressed data using a known format.
     */
    @Test
    public void testCreateCompressorInputStreamFromData() throws Exception {
        byte[] data = new byte[]{0x42, 0x5A, 0x68, 0x64, 0xCA, 0xDE, 0xDB, 0x62, 0xF2, 0x0A, 0x1A, 0x01, 0x03, 0x04, 0x14};

        InputStream in = new ByteArrayInputStream(data);

        CompressorInputStream is = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, in);
        assertNotNull(is);
        assertTrue(is instanceof GzipCompressorInputStream);

        CompressorInputStream isBzip2 = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, in);
        assertNotNull(isBzip2);
        assertTrue(isBzip2 instanceof BZip2CompressorInputStream);

        CompressorInputStream isUnknown = new CompressorStreamFactory()
                .createCompressorInputStream("unknown", in);
        assertNull(isUnknown);

        try {
            CompressorInputStream isInvalid = new CompressorStreamFactory()
                    .createCompressorInputStream(CompressorStreamFactory.GZIP, new ByteArrayInputStream(new byte[]{}));
            assertFalse(true);
        } catch (CompressorException e) {
            // expected
        }

        try {
            CompressorInputStream isInvalid = new CompressorStreamFactory()
                    .createCompressorInputStream(CompressorStreamFactory.BZIP2, new ByteArrayInputStream(new byte[]{}));
            assertFalse(true);
        } catch (CompressorException e) {
            // expected
        }
    }

    /**
     * Test creating a compressor output stream with a known format.
     */
    @Test
    public void testCreateCompressorOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        CompressorOutputStream os = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, out);
        assertNotNull(os);
        assertTrue(os instanceof BZip2CompressorOutputStream);

        CompressorOutputStream osGzip = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, out);
        assertNotNull(osGzip);
        assertTrue(osGzip instanceof GzipCompressorOutputStream);

        try {
            CompressorOutputStream osUnknown = new CompressorStreamFactory()
                    .createCompressorOutputStream(CompressorStreamFactory.BZIP2, out);
            assertFalse(true);
        } catch (CompressorException e) {
            // expected
        }

        try {
            CompressorOutputStream osUnknown = new CompressorStreamFactory()
                    .createCompressorOutputStream(CompressorStreamFactory.GZIP, out);
            assertFalse(true);
        } catch (CompressorException e) {
            // expected
        }

        // Test unknown compressor
        try {
            CompressorOutputStream osUnknown = new CompressorStreamFactory()
                    .createCompressorOutputStream("unknown", out);
            assertNull(osUnknown);
        } catch (CompressorException e) {
            // expected
        }

        try {
            CompressorOutputStream os = new CompressorStreamFactory()
                    .createCompressorOutputStream(CompressorStreamFactory.BZIP2, null);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            CompressorOutputStream os = new CompressorStreamFactory()
                    .createCompressorOutputStream(CompressorStreamFactory.GZIP, null);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test creating a compressor output stream writing data using a known format.
     */
    @Test
    public void testCreateCompressorOutputStreamWriteData() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        CompressorOutputStream os = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, out);

        byte[] data = new byte[]{0x1, 0x2, 0x3, 0x4};

        os.write(data, 0, data.length);
        os.finish();
        os.close();

        byte[] result = out.toByteArray();

        byte[] expectedBzip2 = new byte[]{0x42, 0x5A, 0x68, 0x64, 0xCA, 0xDE, 0xDB, 0x62, 0xF2, 0x0A, 0x1A, 0x01, (byte) 0x03, (byte) 0x04, 0x14, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x03, 0x04, 0x14, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x02, 0x03, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x03, 0x04};

        assertArrayEquals(expectedBzip2, result);

        os = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, out);
        os.write(data, 0, data.length);
        os.finish();
        os.close();

        result = out.toByteArray();

        byte[] expectedGzip = new byte[]{0x1F, 0x8B, 0x08, 0x00, 0x1B, 0xF9, 0x44, 0x44, 0x12, 0x21, 0x0B, 0x00, 0x00, 0x00, 0x00, 0x02, 0x00, 0x03, 0x04, 0x00, 0x00, 0x00, 0x00, 0x0B, 0x1F, 0xDB, 0x78, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x03, 0x04};

        assertArrayEquals(expectedGzip, result);

        try {
            CompressorOutputStream osUnknown = new CompressorStreamFactory()
                    .createCompressorOutputStream(CompressorStreamFactory.BZIP2, out);
            os.write(data, 0, data.length);
            os.finish();
            os.close();
            assertFalse(true);
        } catch (CompressorException e) {
            // expected
        }

        try {
            CompressorOutputStream osUnknown = new CompressorStreamFactory()
                    .createCompressorOutputStream(CompressorStreamFactory.GZIP, out);
            os.write(data, 0, data.length);
            os.finish();
            os.close();
            assertFalse(true);
        } catch (CompressorException e) {
            // expected
        }

        try {
            CompressorOutputStream os = new CompressorStreamFactory()
                    .createCompressorOutputStream(CompressorStreamFactory.BZIP2, out);
            os.write(data, 0, data.length);
            assertFalse(true);
        } catch (InvalidFormatException e) {
            // expected
        }

        try {
            CompressorOutputStream os = new CompressorStreamFactory()
                    .createCompressorOutputStream(CompressorStreamFactory.GZIP, out);
            os.write(data, 0, data.length);
            assertFalse(true);
        } catch (InvalidFormatException e) {
            // expected
        }
    }
}