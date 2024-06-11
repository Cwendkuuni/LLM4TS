package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertArrayEquals;
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

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZUtils;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreams;
import org.apache.commons.compress.utils.IOUtils;

public class CompressorStreamFactoryTest {

    private static final byte[] GZIP_SIGNATURE = new byte[] { 0x1f, 0x8b, 0x08, 0x00, 0x00, 0x1c, 0x00, 0x00, 0xff, 0xfb, 0x1f, 0xf8 };
    private static final byte[] BZIP2_SIGNATURE = new byte[] { 0x42, 0x5a, 0x68, 0x64, 0x6d, 0x6f, 0x00, 0x02, 0x01, 0x0b, 0x05 };
    private static final byte[] XZ_SIGNATURE = new byte[] { 0xfd, 0x37, 0x7a, 0x58, 0x5a, 0x42, 0x60, 0x82, 0x0a, 0x01 };
    private static final byte[] PACK200_SIGNATURE = new byte[] { 0x4d, 0x54, 0x68, 0x76, 0x69, 0x63, 0x2e, 0x74, 0x65, 0x78, 0x74, 0x20, 0x6d, 0x6f, 0x6f, 0x64, 0x65, 0x64, 0x2e, 0x74, 0x65, 0x78, 0x74 };

    @org.junit.Test
    public void testCreateCompressorInputStream() throws IOException {
        byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };

        // Gzip
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream gzipInput = factory.createCompressorInputStream(GZipCompressorInputStream.class, new ByteArrayInputStream(testData));
        assertEquals(GzipCompressorInputStream.class, gzipInput.getClass());
        assertFalse(gzipInput.isReadOnly());
        assertArrayEquals(testData, IOUtils.toByteArray(gzipInput));
        gzipInput.close();

        // Bzip2
        CompressorInputStream bzip2Input = factory.createCompressorInputStream(BZip2CompressorInputStream.class, new ByteArrayInputStream(testData));
        assertEquals(BZip2CompressorInputStream.class, bzip2Input.getClass());
        assertFalse(bzip2Input.isReadOnly());
        assertArrayEquals(testData, IOUtils.toByteArray(bzip2Input));
        bzip2Input.close();

        // XZ
        CompressorInputStream xzInput = factory.createCompressorInputStream(XZCompressorInputStream.class, new ByteArrayInputStream(testData));
        assertEquals(XZCompressorInputStream.class, xzInput.getClass());
        assertFalse(xzInput.isReadOnly());
        assertArrayEquals(testData, IOUtils.toByteArray(xzInput));
        xzInput.close();

        // Invalid signature
        InputStream invalidSignatureInput = new ByteArrayInputStream(new byte[] { 0x01 });
        try {
            factory.createCompressorInputStream(XZCompressorInputStream.class, invalidSignatureInput);
            assertFalse("Expected CompressorException, but nothing was thrown.", false);
        } catch (CompressorException e) {
            // Expected
        }
        invalidSignatureInput.close();

        // Pack200
        CompressorInputStream pack200Input = factory.createCompressorInputStream(Pack200CompressorInputStream.class, new ByteArrayInputStream(testData));
        assertEquals(Pack200CompressorInputStream.class, pack200Input.getClass());
        assertFalse(pack200Input.isReadOnly());
        assertArrayEquals(testData, IOUtils.toByteArray(pack200Input));
        pack200Input.close();

        // Detected signature
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream gzipOutput = CompressorStreams.gzip(output);
        gzipOutput.write(testData);
        gzipOutput.finish();
        gzipOutput.close();

        CompressorInputStream detectedSignatureInput = factory.createCompressorInputStream(InputStream.class, new ByteArrayInputStream(output.toByteArray()));
        assertEquals(GzipCompressorInputStream.class, detectedSignatureInput.getClass());
        assertFalse(detectedSignatureInput.isReadOnly());
        assertArrayEquals(testData, IOUtils.toByteArray(detectedSignatureInput));
        detectedSignatureInput.close();

        // No signature
        InputStream noSignatureInput = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createCompressorInputStream(InputStream.class, noSignatureInput);
            assertFalse("Expected CompressorException, but nothing was thrown.", false);
        } catch (CompressorException e) {
            // Expected
        }
        noSignatureInput.close();
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamWithName() throws IOException {
        byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };

        // Gzip
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream gzipInput = factory.createCompressorInputStream("gzip", new ByteArrayInputStream(testData));
        assertEquals(GzipCompressorInputStream.class, gzipInput.getClass());
        assertFalse(gzipInput.isReadOnly());
        assertArrayEquals(testData, IOUtils.toByteArray(gzipInput));
        gzipInput.close();

        // Bzip2
        CompressorInputStream bzip2Input = factory.createCompressorInputStream("bzip2", new ByteArrayInputStream(testData));
        assertEquals(BZip2CompressorInputStream.class, bzip2Input.getClass());
        assertFalse(bzip2Input.isReadOnly());
        assertArrayEquals(testData, IOUtils.toByteArray(bzip2Input));
        bzip2Input.close();

        // XZ
        CompressorInputStream xzInput = factory.createCompressorInputStream("xz", new ByteArrayInputStream(testData));
        assertEquals(XZCompressorInputStream.class, xzInput.getClass());
        assertFalse(xzInput.isReadOnly());
        assertArrayEquals(testData, IOUtils.toByteArray(xzInput));
        xzInput.close();

        // Invalid name
        InputStream invalidNameInput = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createCompressorInputStream("invalidName", invalidNameInput);
            assertFalse("Expected CompressorException, but nothing was thrown.", false);
        } catch (CompressorException e) {
            // Expected
        }
        invalidNameInput.close();

        // No signature
        InputStream noSignatureInput = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createCompressorInputStream("noSignature", noSignatureInput);
            assertFalse("Expected CompressorException, but nothing was thrown.", false);
        } catch (CompressorException e) {
            // Expected
        }
        noSignatureInput.close();
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream() throws IOException {
        byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };

        // Gzip
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream gzipOutput = factory.createCompressorOutputStream("gzip", output);
        gzipOutput.write(testData);
        gzipOutput.finish();
        assertEquals(testData.length + GzipCompressorOutputStream.DEFAULT_BUFFER_SIZE, output.size());
        assertArrayEquals(testData, IOUtils.toByteArray(new ByteArrayInputStream(output.toByteArray())));
        gzipOutput.close();

        // Bzip2
        OutputStream bzip2Output = new ByteArrayOutputStream();
        CompressorOutputStream bzip2Output2 = factory.createCompressorOutputStream("bzip2", bzip2Output);
        bzip2Output2.write(testData);
        bzip2Output2.finish();
        assertEquals(testData.length + BZip2CompressorOutputStream.DEFAULT_BUFFER_SIZE, output.size());
        assertArrayEquals(testData, IOUtils.toByteArray(new ByteArrayInputStream(bzip2Output.toByteArray())));
        bzip2Output2.close();

        // XZ
        OutputStream xzOutput = new ByteArrayOutputStream();
        CompressorOutputStream xzOutput2 = factory.createCompressorOutputStream("xz", xzOutput);
        xzOutput2.write(testData);
        xzOutput2.finish();
        assertArrayEquals(testData, IOUtils.toByteArray(new ByteArrayInputStream(xzOutput.toByteArray())));
        xzOutput2.close();

        // Detected signature
        ByteArrayOutputStream detectedSignatureOutput = new ByteArrayOutputStream();
        CompressorOutputStream detectedSignatureOutput2 = factory.createCompressorOutputStream(InputStream.class, new ByteArrayInputStream(testData), null);
        detectedSignatureOutput2.write(testData);
        detectedSignatureOutput2.finish();
        assertArrayEquals(testData, IOUtils.toByteArray(new ByteArrayInputStream(detectedSignatureOutput.toByteArray())));
        assertEquals(testData.length + GzipCompressorOutputStream.DEFAULT_BUFFER_SIZE, detectedSignatureOutput.size());
        detectedSignatureOutput2.close();

        // No signature
        OutputStream noSignatureOutput = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream(InputStream.class, new ByteArrayInputStream(new byte[0]), noSignatureOutput);
            assertFalse("Expected CompressorException, but nothing was thrown.", false);
        } catch (CompressorException e) {
            // Expected
        }
        noSignatureOutput.close();
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamWithName() throws IOException {
        byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };

        // Gzip
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream gzipOutput = factory.createCompressorOutputStream("gzip", output);
        gzipOutput.write(testData);
        gzipOutput.finish();
        assertEquals(testData.length + GzipCompressorOutputStream.DEFAULT_BUFFER_SIZE, output.size());
        assertArrayEquals(testData, IOUtils.toByteArray(new ByteArrayInputStream(output.toByteArray())));
        gzipOutput.close();

        // Bzip2
        OutputStream bzip2Output = new ByteArrayOutputStream();
        CompressorOutputStream bzip2Output2 = factory.createCompressorOutputStream("bzip2", bzip2Output);
        bzip2Output2.write(testData);
        bzip2Output2.finish();
        assertEquals(testData.length + BZip2CompressorOutputStream.DEFAULT_BUFFER_SIZE, output.size());
        assertArrayEquals(testData, IOUtils.toByteArray(new ByteArrayInputStream(bzip2Output.toByteArray())));
        bzip2Output2.close();

        // XZ
        OutputStream xzOutput = new ByteArrayOutputStream();
        CompressorOutputStream xzOutput2 = factory.createCompressorOutputStream("xz", xzOutput);
        xzOutput2.write(testData);
        xzOutput2.finish();
        assertArrayEquals(testData, IOUtils.toByteArray(new ByteArrayInputStream(xzOutput.toByteArray())));
        xzOutput2.close();

        // Invalid name
        OutputStream invalidNameOutput = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream("invalidName", invalidNameOutput);
            assertFalse("Expected CompressorException, but nothing was thrown.", false);
        } catch (CompressorException e) {
            // Expected
        }
        invalidNameOutput.close();

        // No signature
        OutputStream noSignatureOutput = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream(InputStream.class, noSignatureOutput);
            assertFalse("Expected CompressorException, but nothing was thrown.", false);
        } catch (CompressorException e) {
            // Expected
        }
        noSignatureOutput.close();
    }

    @org.junit.Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        CompressorStreamFactory factory2 = new CompressorStreamFactory(true);
        assertTrue(factory2.getDecompressConcatenated());
    }
}