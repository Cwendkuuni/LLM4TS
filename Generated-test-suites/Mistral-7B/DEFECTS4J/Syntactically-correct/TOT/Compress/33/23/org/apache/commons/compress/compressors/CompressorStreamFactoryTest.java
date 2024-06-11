package org.apache.commons.compress.compressors.test;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

/**
 * Test for {@link CompressorStreamFactory} class.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws Exception {
        // Test Gzip compressor
        InputStream inputStream = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream gzipInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream("gzip", inputStream);
        Assert.assertEquals("gzip", gzipInputStream.getFormat());
        gzipInputStream.close();

        // Test Deflate compressor
        InputStream deflateInputStream = new ByteArrayInputStream(DeflateCompressorInputStream.DEFLATE_SIGNATURE);
        CompressorInputStream deflateInputStream1 = CompressorStreamFactory.newInstance().createCompressorInputStream("deflate", deflateInputStream);
        Assert.assertEquals("deflate", deflateInputStream1.getFormat());
        deflateInputStream1.close();

        InputStream deflateInputStream2 = new ByteArrayInputStream(DeflateCompressorInputStream.DEFLATE_SIGNATURE);
        CompressorInputStream deflateInputStream3 = CompressorStreamFactory.newInstance().createCompressorInputStream("gzip", deflateInputStream2);
        Assert.assertNull(deflateInputStream3.getFormat());
        deflateInputStream3.close();

        // Test unknown compressor
        InputStream unknownInputStream = new ByteArrayInputStream(new byte[10]);
        try {
            CompressorInputStream unknownInputStream1 = CompressorStreamFactory.newInstance().createCompressorInputStream("unknown", unknownInputStream);
            Assert.fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }
        unknownInputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream() throws Exception {
        // Test deflate compressor
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream deflateOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream("deflate", outputStream);
        deflateOutputStream.write("Hello world!".getBytes());
        deflateOutputStream.finish();
        deflateOutputStream.close();

        byte[] expectedOutput = DeflateCompressorOutputStream.DEFLATE_SIGNATURE;
        byte[] output = outputStream.toByteArray();

        for (int i = 0; i < expectedOutput.length; i++) {
            Assert.assertEquals("Unexpected byte in output", expectedOutput[i], output[i]);
        }

        // Test gzip compressor
        ByteArrayOutputStream gzipOutputStream = new ByteArrayOutputStream();
        CompressorOutputStream gzipOutputStream1 = CompressorStreamFactory.newInstance().createCompressorOutputStream("gzip", gzipOutputStream);
        gzipOutputStream1.write("Hello world!".getBytes());
        gzipOutputStream1.finish();
        gzipOutputStream1.close();

        byte[] gzipExpectedOutput = GzipCompressorOutputStream.GZIP_SIGNATURE;
        byte[] gzipOutput = gzipOutputStream.toByteArray();

        for (int i = 0; i < gzipExpectedOutput.length; i++) {
            Assert.assertEquals("Unexpected byte in output", gzipExpectedOutput[i], gzipOutput[i]);
        }

        // Test unknown compressor
        ByteArrayOutputStream unknownOutputStream = new ByteArrayOutputStream();
        try {
            CompressorOutputStream unknownOutputStream1 = CompressorStreamFactory.newInstance().createCompressorOutputStream("unknown", unknownOutputStream);
            Assert.fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }
        unknownOutputStream.close();
    }
}