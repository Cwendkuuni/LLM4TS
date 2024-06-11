package org.apache.commons.compress.compressors;

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

import org.apache.commons.compress.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

/**
 * Unit tests for {@link CompressorStreamFactory}.
 */
public class CompressorStreamFactoryTest {

    /**
     * Test creating an instance of {@link CompressorStreamFactory} with no arguments.
     */
    @org.junit.Test
    public void testCreateInstance() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        assertNotNull(factory);
    }

    /**
     * Test creating an instance of {@link CompressorStreamFactory} with the {@code decompressConcatenated}
     * parameter set to {@code true}.
     */
    @org.junit.Test
    public void testCreateInstanceWithDecompressConcatenatedTrue() {
        final CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertNotNull(factory);
        assertTrue(factory.getDecompressConcatenated());
    }

    /**
     * Test creating an instance of {@link CompressorStreamFactory} with the {@code decompressConcatenated}
     * parameter set to {@code false}.
     */
    @org.junit.Test
    public void testCreateInstanceWithDecompressConcatenatedFalse() {
        final CompressorStreamFactory factory = new CompressorStreamFactory(false);
        assertNotNull(factory);
        assertFalse(factory.getDecompressConcatenated());
    }

    /**
     * Test creating a {@link CompressorInputStream} for a {@code null} input stream.
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamWithNullInputStream() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorInputStream(null);
        });
    }

    /**
     * Test creating a {@link CompressorInputStream} for a {@code null} compressor name.
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamWithNullCompressorName() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorInputStream(null, new ByteArrayInputStream(new byte[0]));
        });
    }

    /**
     * Test creating a {@link CompressorInputStream} for an invalid compressor name.
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamWithInvalidCompressorName() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(CompressorException.class, () -> {
            factory.createCompressorInputStream("Invalid", inputStream);
        });
    }

    /**
     * Test creating a {@link CompressorInputStream} for a valid compressor name.
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamWithValidCompressorName() throws IOException {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] data = "data".getBytes();
        final OutputStream os = outputStream;

        // Compress data using GZIP compressor.
        final CompressorOutputStream gzipCompressorOutputStream = factory.createCompressorOutputStream(
                CompressorStreamFactory.GZIP, os);
        gzipCompressorOutputStream.write(data);
        gzipCompressorOutputStream.finish();
        gzipCompressorOutputStream.close();

        final InputStream is = factory.createCompressorInputStream(CompressorStreamFactory.GZIP,
                new ByteArrayInputStream(outputStream.toByteArray()));

        final ByteArrayOutputStream dest = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            dest.write(buffer, 0, len);
        }
        dest.flush();

        final byte[] expected = "data".getBytes();
        assertArrayEquals(expected, dest.toByteArray());

        is.close();
    }

    /**
     * Test creating a {@link CompressorOutputStream} for a {@code null} output stream.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithNullOutputStream() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, null);
        });
    }

    /**
     * Test creating a {@link CompressorOutputStream} for a {@code null} compressor name.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithNullCompressorName() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorOutputStream(null, new ByteArrayOutputStream());
        });
    }

    /**
     * Test creating a {@link CompressorOutputStream} for an invalid compressor name.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithInvalidCompressorName() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(CompressorException.class, () -> {
            factory.createCompressorOutputStream("Invalid", outputStream);
        });
    }

    /**
     * Test creating a {@link CompressorOutputStream} for a valid compressor name.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithValidCompressorName() throws IOException {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String data = "data";

        // Create a compressor output stream and write data to it.
        final CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(
                CompressorStreamFactory.GZIP, outputStream);
        compressorOutputStream.write(data.getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();

        // Create a compressor input stream and read data from it.
        final InputStream is = factory.createCompressorInputStream(CompressorStreamFactory.GZIP,
                new ByteArrayInputStream(outputStream.toByteArray()));
        final byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            System.out.write(buffer, 0, len);
        }
        is.close();

        assertArrayEquals(data.getBytes(), buffer);
    }
}