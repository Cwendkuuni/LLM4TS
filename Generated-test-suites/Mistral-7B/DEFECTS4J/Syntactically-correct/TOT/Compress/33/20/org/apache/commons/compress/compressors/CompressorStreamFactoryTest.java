package org.apache.commons.compress.compressors.test;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test the CompressorStreamFactory class.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws IOException {
        byte[] data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.".getBytes();

        // Create a ByteArrayOutputStream to write the compressed data to
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream cos = new CompressorStreamFactory().createCompressorOutputStream("bzip2", out);
        CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream("bzip2",
                new ByteArrayInputStream(data));

        IOUtils.copy(in, cos);
        cos.finish();
        cos.close();

        // Read the compressed data and decompress it back to its original form
        ByteArrayInputStream is = new ByteArrayInputStream(out.toByteArray());
        CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream("bzip2", is);
        byte[] expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.".getBytes();
        byte[] actual = new byte[expected.length];
        int lengthRead;

        while ((lengthRead = IOUtils.readFully(input, actual, 0, expected.length)) < expected.length) {
            // Buffer is not filled yet
            continue;
        }

        Assert.assertArrayEquals(expected, actual);
        input.close();
    }

    @Test
    public void testCreateCompressorOutputStream_NullName() {
        try {
            new CompressorStreamFactory().createCompressorOutputStream(null, new ByteArrayOutputStream());
            Assert.fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_NullOutputStream() {
        try {
            new CompressorStreamFactory().createCompressorOutputStream("bzip2", null);
            Assert.fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_UnknownFormat() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            new CompressorStreamFactory().createCompressorOutputStream("unknown", out);
            Assert.fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        } finally {
            out.close();
        }
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        Assert.assertFalse(factory.getDecompressConcatenated());

        CompressorStreamFactory factory2 = new CompressorStreamFactory(true);
        Assert.assertTrue(factory2.getDecompressConcatenated());
    }
}