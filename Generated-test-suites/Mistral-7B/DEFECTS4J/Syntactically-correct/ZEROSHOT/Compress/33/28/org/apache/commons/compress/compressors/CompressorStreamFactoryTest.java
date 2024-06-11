package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class CompressorStreamFactoryTest {

    @org.junit.Test
    public void testCompressorStreamFactory() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test default case
        CompressorInputStream inputStream = factory.createCompressorInputStream("bzip2", new ByteArrayInputStream(new byte[10]));
        assertTrue(inputStream instanceof BZip2CompressorInputStream);
        IOUtils.closeQuietly(inputStream);

        CompressorOutputStream outputStream = factory.createCompressorOutputStream("bzip2", new ByteArrayOutputStream());
        assertTrue(outputStream instanceof BZip2CompressorOutputStream);
        IOUtils.closeQuietly(outputStream);

        // Test with decompressConcatenated set to true
        CompressorStreamFactory factoryConcat = new CompressorStreamFactory(true);
        CompressorInputStream inputStreamConcat = factoryConcat.createCompressorInputStream("bzip2", new ByteArrayInputStream(new byte[10]));
        assertTrue(inputStreamConcat instanceof BZip2CompressorInputStream);
        assertTrue(factoryConcat.getDecompressConcatenated());
        IOUtils.closeQuietly(inputStreamConcat);

        CompressorOutputStream outputStreamConcat = factoryConcat.createCompressorOutputStream("bzip2", new ByteArrayOutputStream());
        assertTrue(outputStreamConcat instanceof BZip2CompressorOutputStream);
        assertTrue(factoryConcat.getDecompressConcatenated());
        IOUtils.closeQuietly(outputStreamConcat);

        // Test with wrong compressor name
        try {
            factory.createCompressorInputStream("wrongName", new ByteArrayInputStream(new byte[10]));
            assert False;
        } catch (CompressorException e) {
            // expected
        }

        try {
            factory.createCompressorOutputStream("wrongName", new ByteArrayOutputStream());
            assert False;
        } catch (CompressorException e) {
            // expected
        }

        // Test with null compressor name or input stream
        try {
            factory.createCompressorInputStream(null, new ByteArrayInputStream(new byte[10]));
            assert False;
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            factory.createCompressorInputStream("bzip2", null);
            assert False;
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test with null compressor name and input stream
        try {
            factory.createCompressorInputStream(null, null);
            assert False;
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test with null compressor name or output stream
        try {
            factory.createCompressorOutputStream("bzip2", null);
            assert False;
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            factory.createCompressorOutputStream(null, new ByteArrayOutputStream());
            assert False;
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test with null compressor name and output stream
        try {
            factory.createCompressorOutputStream(null, null);
            assert False;
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test with different compressor name
        CompressorOutputStream outputStreamGzip = factory.createCompressorOutputStream("gzip", new ByteArrayOutputStream());
        assertTrue(outputStreamGzip instanceof GzipCompressorOutputStream);
        IOUtils.closeQuietly(outputStreamGzip);

        CompressorInputStream inputStreamDeflate = factory.createCompressorInputStream("deflate", new ByteArrayInputStream(new byte[10]));
        assertTrue(inputStreamDeflate instanceof DeflateCompressorInputStream);
        IOUtils.closeQuietly(inputStreamDeflate);
    }
}