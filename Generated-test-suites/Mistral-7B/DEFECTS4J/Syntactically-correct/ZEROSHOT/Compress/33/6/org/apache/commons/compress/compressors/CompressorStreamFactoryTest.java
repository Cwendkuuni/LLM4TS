package org.apache.commons.compress.compressors;

import static org.junit.Assert.*;

import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * CompressorStreamFactoryTest class to test CompressorStreamFactory methods
 *
 * @author John Doe
 */
public class CompressorStreamFactoryTest {

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorInputStream(java.lang.String, java.io.InputStream)}
     * and {@link CompressorStreamFactory#createCompressorInputStream(java.io.InputStream)}
     */
    @Test
    public void testCreateCompressorInputStream() throws IOException {
        // Test with valid input
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream gzipInputStream = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        assertTrue(gzipInputStream instanceof GzipCompressorInputStream);
        gzipInputStream.close();

        input = new ByteArrayInputStream(XZCompressorInputStream.XZ_SIGNATURE);
        CompressorInputStream xzInputStream = factory.createCompressorInputStream(input);
        assertTrue(xzInputStream instanceof XZCompressorInputStream);
        xzInputStream.close();

        input = new ByteArrayInputStream(XZCompressorInputStream.XZ_SIGNATURE);
        CompressorInputStream xzInputStream2 = factory.createCompressorInputStream(CompressorStreamFactory.XZ, input);
        assertTrue(xzInputStream2 instanceof XZCompressorInputStream);
        xzInputStream2.close();

        // Test with null input
        assertNull(factory.createCompressorInputStream(null));
        // Test with non-supported compressor
        assertNull(factory.createCompressorInputStream("unknown", input));

        // Test with valid input and set decompressConcatenated to true
        factory = new CompressorStreamFactory(true);
        ByteArrayOutputStream gzipOutputStream = new ByteArrayOutputStream();
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream gzipOutputStream2 = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
        IOUtils.copy(new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE), gzipOutputStream2);
        gzipOutputStream2.finish();
        gzipOutputStream2.close();

        InputStream expectedInputStream = new ByteArrayInputStream(gzipOutputStream.toByteArray());
        CompressorInputStream gzipInputStream3 = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, expectedInputStream);
        assertTrue(gzipInputStream3.available() > 0);
        gzipInputStream3.close();
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(java.lang.String, java.io.OutputStream)}
     * and {@link CompressorStreamFactory#createCompressorOutputStream(java.io.OutputStream)}
     */
    @Test
    public void testCreateCompressorOutputStream() throws IOException {
        // Test with valid input and output
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream gzipOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        assertNotNull(gzipOutputStream);
        gzipOutputStream.close();

        output = new ByteArrayOutputStream();
        CompressorOutputStream xzOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.XZ, output);
        assertNotNull(xzOutputStream);
        xzOutputStream.close();

        // Test with null output
        assertNull(factory.createCompressorOutputStream(null));
        // Test with non-supported compressor
        assertNull(factory.createCompressorOutputStream("unknown", output));
    }

    /**
     * Test method for {@link CompressorStreamFactory#getDecompressConcatenated()}
     */
    @Test
    public void testGetDecompressConcatenated() {
        // Test with default constructor
        CompressorStreamFactory factoryDefault = new CompressorStreamFactory();
        assertFalse(factoryDefault.getDecompressConcatenated());

        // Test with constructor with decompressConcatenated set to true
        CompressorStreamFactory factoryConcatenated = new CompressorStreamFactory(true);
        assertTrue(factoryConcatenated.getDecompressConcatenated());
    }
}