package org.apache.commons.compress.compressors.test;

import org.apache.commons.compress.CompressorException;
import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test cases for {@link CompressorStreamFactory} class.
 */
public class CompressorStreamFactoryTest {

    /**
     * Test creating compressor input stream from a known compressor name and an input stream.
     */
    @Test
    public void testCreateCompressorInputStreamGzip() throws IOException, CompressorException {
        String compressorName = "gzip";
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream compressorInputStream = CompressorFactory.create(compressorName)
                .createCompressorInputStream(input);
        Assert.assertTrue("Unexpected compressor type", compressorInputStream instanceof GzipCompressorInputStream);
        compressorInputStream.close();
    }

    /**
     * Test creating compressor input stream from an unknown compressor name and an input stream.
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamUnknownCompressor() throws IOException {
        String compressorName = "unknown";
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorFactory.create(compressorName).createCompressorInputStream(input);
    }

    /**
     * Test creating compressor input stream from a known compressor name and a null input stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamWithNullInputStream() throws CompressorException {
        String compressorName = "gzip";
        CompressorFactory.create(compressorName).createCompressorInputStream(null);
    }

    /**
     * Test creating compressor output stream from a known compressor name and an output stream.
     */
    @Test
    public void testCreateCompressorOutputStreamGzip() throws IOException, CompressorException {
        String compressorName = "gzip";
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = CompressorFactory.create(compressorName)
                .createCompressorOutputStream(compressorName, output);
        compressorOutputStream.finish();
        compressorOutputStream.close();
        Assert.assertTrue("Unexpected compressor type", output instanceof GzipCompressorOutputStream);
    }

    /**
     * Test creating compressor output stream from an unknown compressor name and an output stream.
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamUnknownCompressor() throws IOException {
        String compressorName = "unknown";
        OutputStream output = new ByteArrayOutputStream();
        CompressorFactory.create(compressorName).createCompressorOutputStream(compressorName, output);
    }

    /**
     * Test creating compressor output stream from a known compressor name and a null output stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamWithNullOutputStream() throws CompressorException {
        String compressorName = "gzip";
        CompressorFactory.create(compressorName).createCompressorOutputStream(compressorName, null);
    }

    /**
     * Test creating compressor input stream from a known compressor name and a null input stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamWithNullInput() throws CompressorException {
        String compressorName = "gzip";
        InputStream input = null;
        CompressorFactory.create(compressorName).createCompressorInputStream(input);
    }

    /**
     * Test creating compressor output stream from a known compressor name and a null compressor name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamWithNullCompressorName() throws CompressorException {
        String compressorName = null;
        OutputStream output = new ByteArrayOutputStream();
        CompressorFactory.create(compressorName).createCompressorOutputStream(compressorName, output);
    }

    /**
     * Test creating compressor input stream from a known compressor name and a compressed input stream.
     */
    @Test
    public void testCreateCompressorInputStreamBzip2() throws IOException, CompressorException {
        String compressorName = "bzip2";
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = CompressorFactory.create(compressorName)
                .createCompressorInputStream(input);
        Assert.assertTrue("Unexpected compressor type", compressorInputStream instanceof BZip2CompressorInputStream);
        compressorInputStream.close();
    }

    /**
     * Test creating compressor input stream from a known compressor name and a null compressor name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamWithNullCompressorName2() throws CompressorException {
        String compressorName = null;
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorFactory.create(compressorName).createCompressorInputStream(input);
    }

    /**
     * Test creating compressor input stream from a known compressor name and an empty input stream.
     */
    @Test
    public void testCreateCompressorInputStreamEmptyInput() throws IOException, CompressorException {
        String compressorName = "gzip";
        InputStream input = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = CompressorFactory.create(compressorName)
                .createCompressorInputStream(input);
        compressorInputStream.close();
    }

    /**
     * Test creating compressor input stream from a known compressor name and a compressor input stream.
     */
    @Test
    public void testCreateCompressorInputStreamFromCompressorInputStream() throws IOException, CompressorException {
        String compressorName = "bzip2";
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = new BZip2CompressorInputStream(input);
        CompressorInputStream newCompressorInputStream = CompressorFactory.create(compressorName)
                .createCompressorInputStream(compressorInputStream);
        Assert.assertTrue("Unexpected compressor type", newCompressorInputStream instanceof BZip2CompressorInputStream);
        newCompressorInputStream.close();
        compressorInputStream.close();
    }

    /**
     * Test creating compressor output stream from a known compressor name and a compressor output stream.
     */
    @Test
    public void testCreateCompressorOutputStreamFromCompressorOutputStream() throws IOException, CompressorException {
        String compressorName = "deflate";
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new DeflateCompressorOutputStream(output);
        CompressorOutputStream newCompressorOutputStream = CompressorFactory.create(compressorName)
                .createCompressorOutputStream(compressorName, compressorOutputStream);
        Assert.assertTrue("Unexpected compressor type", newCompressorOutputStream instanceof DeflateCompressorOutputStream);
        newCompressorOutputStream.finish();
        newCompressorOutputStream.close();
        compressorOutputStream.close();
    }

    /**
     * Test creating compressor input stream from a known compressor name and a compressed input stream with errors.
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamWithErrors() throws IOException, CompressorException {
        String compressorName = "bzip2";
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        // Intentionally adding errors
        input.mark(input.available());
        input.reset();
        CompressorInputStream compressorInputStream = CompressorFactory.create(compressorName)
                .createCompressorInputStream(input);
        compressorInputStream.close();
    }

    /**
     * Test creating compressor output stream from a known compressor name and a compressor output stream with errors.
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamWithErrors() throws IOException, CompressorException {
        String compressorName = "deflate";
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new DeflateCompressorOutputStream(output);
        compressorOutputStream.write(new byte[]{-1, -1});
        compressorOutputStream.close();
        // Intentionally adding errors
        compressorOutputStream.mark(compressorOutputStream.size());
        compressorOutputStream.reset();
        CompressorFactory.create(compressorName).createCompressorOutputStream(compressorName, compressorOutputStream);
    }

    /**
     * Test creating compressor input stream from a known compressor name and a compressor input stream with errors.
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamWithErrors2() throws IOException, CompressorException {
        String compressorName = "bzip2";
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = new BZip2CompressorInputStream(input);
        compressorInputStream.close();
        // Intentionally adding errors
        compressorInputStream.mark(compressorInputStream.available());
        compressorInputStream.reset();
        CompressorFactory.create(compressorName).createCompressorInputStream(compressorInputStream);
    }

    /**
     * Test creating compressor output stream from a known compressor name and a compressor output stream with errors.
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamWithErrors2() throws IOException, CompressorException {
        String compressorName = "deflate";
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new DeflateCompressorOutputStream(output);
        compressorOutputStream.finish();
        compressorOutputStream.close();
        // Intentionally adding errors
        compressorOutputStream.mark(compressorOutputStream.size());
        compressorOutputStream.reset();
        CompressorFactory.create(compressorName).createCompressorOutputStream(compressorName, compressorOutputStream);
    }
}