package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class CompressorStreamFactoryTest {

    /**
     * Test creating input streams
     *
     * @throws IOException
     * @throws CompressorException
     */
    @org.junit.Test
    public void testCreateCompressorInputStream() throws IOException, CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test with valid input
        InputStream input = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        assertSame(GzipCompressorInputStream.class, compressorInputStream.getClass());

        // Test with null input
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorInputStream(CompressorStreamFactory.GZIP, null));

        // Test with invalid compressor name
        assertThrows(CompressorException.class, () -> factory.createCompressorInputStream(" InvalidCompressor", input));

        // Test with input that does not support mark
        InputStream unsupportedInput = new ByteArrayInputStream(new byte[0]) {
            @Override
            public synchronized void mark(int readlimit) {
                throw new UnsupportedOperationException();
            }

            @Override
            public synchronized void reset() throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorInputStream(CompressorStreamFactory.GZIP, unsupportedInput));

        // Test with empty input
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStreamEmpty = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, emptyInput);
        assertEquals(0, compressorInputStreamEmpty.read());
        compressorInputStreamEmpty.close();
    }

    /**
     * Test creating input streams with autodetection
     *
     * @throws IOException
     * @throws CompressorException
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamAutoDetection() throws IOException, CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test with valid gzip input
        ByteArrayOutputStream gzipOutput = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(gzipOutput);
        gzipOutputStream.write("Test".getBytes());
        gzipOutputStream.finish();
        gzipOutputStream.close();

        InputStream gzipInput = new ByteArrayInputStream(gzipOutput.toByteArray());
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(gzipInput);
        assertEquals(GzipCompressorInputStream.class, compressorInputStream.getClass());

        // Test with valid bzip2 input
        ByteArrayOutputStream bzip2Output = new ByteArrayOutputStream();
        BZip2CompressorOutputStream bzip2OutputStream = new BZip2CompressorOutputStream(bzip2Output);
        bzip2OutputStream.write("Test".getBytes());
        bzip2OutputStream.finish();
        bzip2OutputStream.close();

        InputStream bzip2Input = new ByteArrayInputStream(bzip2Output.toByteArray());
        CompressorInputStream compressorInputStreamBzip2 = factory.createCompressorInputStream(bzip2Input);
        assertEquals(BZip2CompressorInputStream.class, compressorInputStreamBzip2.getClass());

        // Test with invalid input
        InputStream invalidInput = new ByteArrayInputStream(new byte[0]);
        assertThrows(CompressorException.class, () -> factory.createCompressorInputStream(invalidInput));
    }

    /**
     * Test creating output streams
     *
     * @throws IOException
     * @throws DataFormatException
     */
    @org.junit.Test
    public void testCreateCompressorOutputStream() throws IOException, CompressorException, DataFormatException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test with valid output
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        assertEquals(GzipCompressorOutputStream.class, compressorOutputStream.getClass());

        // Test with null output
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, null));

        // Test with invalid compressor name
        assertThrows(CompressorException.class, () -> factory.createCompressorOutputStream(" InvalidCompressor", output));

        // Test with output that does not support write
        ByteArrayOutputStream unsupportedOutput = new ByteArrayOutputStream() {
            @Override
            public synchronized void write(int b) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, unsupportedOutput));
    }

    /**
     * Test setting decompressConcatenated
     */
    @org.junit.Test
    public void testSetDecompressConcatenated() throws IOException, CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test setting to false and then creating an input stream
        CompressorStreamFactory factoryFalse = new CompressorStreamFactory(false);
        InputStream input = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStreamFalse = factoryFalse.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        assertFalse(compressorInputStreamFalse.isDecompressConcatenated());

        // Test setting to true and then creating an input stream
        CompressorStreamFactory factoryTrue = new CompressorStreamFactory(true);
        InputStream inputTrue = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStreamTrue = factoryTrue.createCompressorInputStream(CompressorStreamFactory.GZIP, inputTrue);
        assertTrue(compressorInputStreamTrue.isDecompressConcatenated());

        // Test setting and then getting
        factory.setDecompressConcatenated(false);
        assertFalse(factory.getDecompressConcatenated());

        factory.setDecompressConcatenated(true);
        assertTrue(factory.getDecompressConcatenated());
    }

    /**
     * Test create compressor input stream with no compressor
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamNoCompressor() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        InputStream input = new ByteArrayInputStream(new byte[0]);
        assertThrows(CompressorException.class, () -> factory.createCompressorInputStream("NoCompressor", input));
    }
}