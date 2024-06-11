package org.apache.commons.compress.compressors.test;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test cases for {@link CompressorStreamFactory}.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamGzip() throws IOException, CompressorException {
        CompressorInputStream inputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, new ByteArrayInputStream(GzipTestData.GZIP_DATA));
        Assert.assertEquals("Incorrect Compressor type", GzipCompressorInputStream.class, inputStream.getClass());
        byte[] out = IOUtils.toByteArray(inputStream);
        Assert.assertArrayEquals("Incorrect decompressed data", GzipTestData.ORIGINAL_DATA, out);
        inputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamUnknown() throws IOException, CompressorException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            new CompressorStreamFactory().createCompressorInputStream("unknown", inputStream);
            Assert.fail("Expected CompressorException to be thrown");
        } catch (CompressorException e) {
            // expected
        }
    }

    @Test
    public void testCreateCompressorOutputStreamGzip() throws IOException, CompressorException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
        byte[] input = GzipTestData.ORIGINAL_DATA;
        compressorOutputStream.write(input, 0, input.length);
        compressorOutputStream.finish();
        Assert.assertEquals("Incorrect Compressor type", GzipCompressorOutputStream.class, compressorOutputStream.getClass());
        byte[] output = ((ByteArrayOutputStream) outputStream).toByteArray();
        Assert.assertArrayEquals("Incorrect compressed data", GzipTestData.GZIP_DATA, output);
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStreamBzip2() throws IOException, CompressorException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
        byte[] input = Bzip2TestData.ORIGINAL_DATA;
        compressorOutputStream.write(input, 0, input.length);
        compressorOutputStream.finish();
        Assert.assertEquals("Incorrect Compressor type", BZip2CompressorOutputStream.class, compressorOutputStream.getClass());
        byte[] output = ((ByteArrayOutputStream) outputStream).toByteArray();
        Assert.assertArrayEquals("Incorrect compressed data", Bzip2TestData.BZIP2_DATA, output);
        compressorOutputStream.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testSetDecompressConcatenatedTwice() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        factory.setDecompressConcatenated(false);
    }
}