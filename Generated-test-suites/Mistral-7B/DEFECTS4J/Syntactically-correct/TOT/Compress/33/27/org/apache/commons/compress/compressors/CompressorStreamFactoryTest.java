package org.apache.commons.compress.compressors.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreamFactory.CompressorStreamType;

import org.junit.Test;

/**
 * Test for {@link CompressorStreamFactory} class
 */
public class CompressorStreamFactoryTest {

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}
     * and {@link CompressorStreamFactory#createCompressorInputStream(InputStream)}
     */
    @Test
    public void testCreateCompressorStreams() throws IOException {
        String outputFileName = "output.gz";
        OutputStream outputStream = new FileOutputStream(outputFileName);
        CompressorOutputStream compressorOut = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);

        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        byte[] data = "Data to compress".getBytes();
        inputStream.write(data);
        inputStream.close();

        CompressorInputStream compressorIn = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP,
                        new ByteArrayInputStream(inputStream.toByteArray()));

        byte[] outputData = new byte[inputStream.size() + 5];
        int len = IOUtils.copy(compressorIn, outputData);
        compressorIn.close();
        outputStream.close();

        File file = new File(outputFileName);
        byte[] expected = new byte[len];
        FileInputStream expectedInputStream = new FileInputStream(file);
        IOUtils.readFully(expectedInputStream, expected, 0, len);
        expectedInputStream.close();
        file.delete();

        assertEquals(len, len);
        assertEquals(data, expected);

        assertNotNull(compressorOut);
        assertNotNull(compressorIn);
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}
     * and {@link CompressorStreamFactory#createCompressorInputStream(InputStream)} with unknown compressor type
     */
    @Test(expected = CompressorException.class)
    public void testUnknownCompressorType() throws IOException {
        String outputFileName = "output.xxx";
        OutputStream outputStream = new FileOutputStream(outputFileName);
        new CompressorStreamFactory().createCompressorOutputStream("xxx", outputStream);
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}
     * and {@link CompressorStreamFactory#createCompressorInputStream(InputStream)} with null stream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullStream() throws IOException {
        new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, null);
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}
     * and {@link CompressorStreamFactory#createCompressorInputStream(InputStream)} with mark unsupported stream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMarkUnsupportedStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, inputStream);
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}
     * with different compressor types and {@link CompressorInputStream#close()} method call
     */
    @Test
    public void testCompressorOutputStreamClose() throws IOException {
        String outputFileNameGzip = "output.gz";
        OutputStream outputStreamGzip = new FileOutputStream(outputFileNameGzip);

        String outputFileNameBzip2 = "output.bz2";
        OutputStream outputStreamBzip2 = new FileOutputStream(outputFileNameBzip2);

        String outputFileNameDeflate = "output.deflate";
        OutputStream outputStreamDeflate = new FileOutputStream(outputFileNameDeflate);

        // Test Gzip compressor
        CompressorOutputStream gzipCompressorOut = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStreamGzip);

        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        byte[] data = "Data to compress".getBytes();
        inputStream.write(data);
        inputStream.close();

        CompressorInputStream gzipCompressorIn = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP,
                        new ByteArrayInputStream(inputStream.toByteArray()));

        gzipCompressorOut.write(data);
        gzipCompressorOut.close();

        byte[] outputDataGzip = new byte[inputStream.size() + 5];
        int len = IOUtils.copy(gzipCompressorIn, outputDataGzip);
        gzipCompressorIn.close();

        // Test Bzip2 compressor
        CompressorOutputStream bzip2CompressorOut = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStreamBzip2);

        CompressorInputStream bzip2CompressorIn = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2,
                        new ByteArrayInputStream(inputStream.toByteArray()));

        bzip2CompressorOut.write(data);
        bzip2CompressorOut.close();

        byte[] outputDataBzip2 = new byte[inputStream.size() + 5];
        len = IOUtils.copy(bzip2CompressorIn, outputDataBzip2);
        bzip2CompressorIn.close();

        // Test Deflate compressor
        CompressorOutputStream deflateCompressorOut = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.DEFLATE, outputStreamDeflate);

        CompressorInputStream deflateCompressorIn = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.DEFLATE,
                        new ByteArrayInputStream(inputStream.toByteArray()));

        deflateCompressorOut.write(data);
        deflateCompressorOut.close();

        byte[] outputDataDeflate = new byte[inputStream.size() + 5];
        len = IOUtils.copy(deflateCompressorIn, outputDataDeflate);
        deflateCompressorIn.close();

        assertEquals(len, len);
        assertEquals(data, new String(outputDataGzip, 0, len).getBytes());
        assertEquals(data, new String(outputDataBzip2, 0, len).getBytes());
        assertEquals(data, new String(outputDataDeflate, 0, len).getBytes());

        assertNotNull(gzipCompressorOut);
        assertNotNull(gzipCompressorIn);
        assertNotNull(bzip2CompressorOut);
        assertNotNull(bzip2CompressorIn);
        assertNotNull(deflateCompressorOut);
        assertNotNull(deflateCompressorIn);
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}
     * and {@link CompressorStreamFactory#createCompressorInputStream(InputStream)} with unsupported compressor type
     * as an output stream
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedCompressorOutputStream() throws IOException {
        String outputFileName = "output.bzip2";
        OutputStream outputStream = new FileOutputStream(outputFileName);

        new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
    }
}