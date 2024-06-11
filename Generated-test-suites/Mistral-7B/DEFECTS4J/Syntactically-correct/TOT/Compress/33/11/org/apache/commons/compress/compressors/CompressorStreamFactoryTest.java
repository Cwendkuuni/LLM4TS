package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test for {@link CompressorStreamFactory}.
 */
public class CompressorStreamFactoryTest {

    /**
     * Test factory creation with no options.
     */
    @Test
    public void testFactoryCreation() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        assertNotNull(factory);
    }

    /**
     * Test factory creation with decompress until EOF option.
     */
    @Test
    public void testFactoryCreationWithDecompressUntilEOF() {
        final CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }

    /**
     * Test factory creation with decompress concatenated option.
     * @deprecated since 1.10
     */
    @Test
    @Deprecated
    public void testFactoryCreationWithDecompressConcatenated() {
        final CompressorStreamFactory factory = new CompressorStreamFactory(true);
        factory.setDecompressConcatenated(false);
        assertFalse(factory.getDecompressConcatenated());
    }

    /**
     * Test creating a GZip compressor input stream from a byte array.
     */
    @Test
    public void testCreateGZipCompressorInputStream() throws IOException {
        final byte[] data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.".getBytes();

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final CompressorInputStream in = factory.createCompressorInputStream(GZipCompressorInputStream.class.getName(),
                new ByteArrayInputStream(data));
        assertNotNull(in);

        // Test reading the original data
        final byte[] readData = IOUtils.toByteArray(in);
        assertNotNull(readData);
        assertTrue(IOUtils.contentEquals(data, readData));

        // Test close
        in.close();
    }

    /**
     * Test creating a BZip2 compressor input stream from a byte array.
     */
    @Test
    public void testCreateBZip2CompressorInputStream() throws IOException {
        final byte[] data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.".getBytes();

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final CompressorInputStream in = factory.createCompressorInputStream(BZip2CompressorInputStream.class.getName(),
                new ByteArrayInputStream(data));
        assertNotNull(in);

        // Test reading the original data
        final byte[] readData = IOUtils.toByteArray(in);
        assertNotNull(readData);
        assertTrue(IOUtils.contentEquals(data, readData));

        // Test close
        in.close();
    }

    /**
     * Test creating a Deflate compressor input stream from a byte array.
     */
    @Test
    public void testCreateDeflateCompressorInputStream() throws IOException {
        final byte[] data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.".getBytes();

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final CompressorInputStream in = factory.createCompressorInputStream(DeflateCompressorInputStream.class.getName(),
                new ByteArrayInputStream(data));
        assertNotNull(in);

        // Test reading the original data
        final byte[] readData = IOUtils.toByteArray(in);
        assertNotNull(readData);
        assertTrue(IOUtils.contentEquals(data, readData));

        // Test close
        in.close();
    }

    /**
     * Test creating a GZip compressor output stream to a byte array.
     */
    @Test
    public void testCreateGZipCompressorOutputStream() throws IOException {
        final byte[] data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.".getBytes();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final CompressorOutputStream outStream = factory.createCompressorOutputStream(GZipCompressorOutputStream.class.getName(), out);
        assertNotNull(outStream);

        final OutputStream os = outStream.getDelegateStream();
        os.write(data, 0, data.length);
        os.close();

        // Test reading the compressed data
        final byte[] readData = out.toByteArray();
        assertNotNull(readData);

        final CompressorInputStream in = factory.createCompressorInputStream(GZipCompressorInputStream.class.getName(),
                new ByteArrayInputStream(readData));
        final byte[] readData2 = IOUtils.toByteArray(in);
        assertNotNull(readData2);
        assertTrue(IOUtils.contentEquals(data, readData2));

        // Test close
        in.close();
    }

    /**
     * Test creating a BZip2 compressor output stream to a byte array.
     */
    @Test
    public void testCreateBZip2CompressorOutputStream() throws IOException {
        final byte[] data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.".getBytes();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final CompressorOutputStream outStream = factory.createCompressorOutputStream(BZip2CompressorOutputStream.class.getName(), out);
        assertNotNull(outStream);

        final OutputStream os = outStream.getDelegateStream();
        os.write(data, 0, data.length);
        os.close();

        // Test reading the compressed data
        final byte[] readData = out.toByteArray();
        assertNotNull(readData);

        final CompressorInputStream in = factory.createCompressorInputStream(BZip2CompressorInputStream.class.getName(),
                new ByteArrayInputStream(readData));
        final byte[] readData2 = IOUtils.toByteArray(in);
        assertNotNull(readData2);
        assertTrue(IOUtils.contentEquals(data, readData2));

        // Test close
        in.close();
    }

    /**
     * Test creating a Deflate compressor output stream to a byte array.
     */
    @Test
    public void testCreateDeflateCompressorOutputStream() throws IOException {
        final byte[] data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.".getBytes();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final CompressorOutputStream outStream = factory.createCompressorOutputStream(DeflateCompressorOutputStream.class.getName(), out);
        assertNotNull(outStream);

        final OutputStream os = outStream.getDelegateStream();
        os.write(data, 0, data.length);
        os.close();

        // Test reading the compressed data
        final byte[] readData = out.toByteArray();
        assertNotNull(readData);

        final CompressorInputStream in = factory.createCompressorInputStream(DeflateCompressorInputStream.class.getName(),
                new ByteArrayInputStream(readData));
        final byte[] readData2 = IOUtils.toByteArray(in);
        assertNotNull(readData2);
        assertTrue(IOUtils.contentEquals(data, readData2));

        // Test close
        in.close();
    }

    /**
     * Test creating a compressor input stream from a non-existent compressor name.
     */
    @Test
    public void testCreateUnknownCompressorInputStream() throws IOException {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final InputStream in = factory.createCompressorInputStream("UnknownCompressor", new ByteArrayInputStream(new byte[0]));
        assertThrows(CompressorException.class, () -> in.read());
    }

    /**
     * Test creating a compressor output stream from a non-existent compressor name.
     */
    @Test
    public void testCreateUnknownCompressorOutputStream() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        final OutputStream out = new ByteArrayOutputStream();
        assertThrows(CompressorException.class, () -> factory.createCompressorOutputStream("UnknownCompressor", out));
    }
}