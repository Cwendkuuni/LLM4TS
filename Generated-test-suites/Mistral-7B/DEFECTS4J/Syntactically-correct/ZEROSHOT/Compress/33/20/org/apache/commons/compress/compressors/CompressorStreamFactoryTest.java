package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreams;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * Test class for CompressorStreamFactory
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamBzip2() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("bzip2", inputStream);
        assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());
        verify(compressorInputStream).reset();
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamGzip() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("gzip", inputStream);
        assertEquals(GzipCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamXz() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(XZCompressorInputStream.XZ_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("xz", inputStream);
        assertEquals(XZCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamLzma() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(LZMACompressorInputStream.LZMA_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("lzma", inputStream);
        assertEquals(LZMACompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamPack200() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(Pack200CompressorInputStream.PACK200_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("pack200", inputStream);
        assertEquals(Pack200CompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamSnappyFramed() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(SnappyCompressorInputStream.SNAPPY_FRAMED_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("snappy-framed", inputStream);
        assertEquals(FramedSnappyCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamSnappyRaw() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(SnappyCompressorInputStream.SNAPPY_RAW_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("snappy-raw", inputStream);
        assertEquals(SnappyCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamZ() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(ZCompressorInputStream.Z_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("z", inputStream);
        assertEquals(ZCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamDeflate() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(DeflateCompressorInputStream.DEFLATE_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("deflate", inputStream);
        assertEquals(DeflateCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStreamBzip2() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("bzip2", outputStream);
        assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        verify(compressorOutputStream).reset();
    }

    @Test
    public void testCreateCompressorOutputStreamGzip() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("gzip", outputStream);
        assertEquals(GzipCompressorOutputStream.class, compressorOutputStream.getClass());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        verify(compressorOutputStream).reset();
    }

    @Test
    public void testCreateCompressorOutputStreamXz() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("xz", outputStream);
        assertEquals(XZCompressorOutputStream.class, compressorOutputStream.getClass());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        verify(compressorOutputStream).reset();
    }

    @Test
    public void testCreateCompressorOutputStreamLzma() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("lzma", outputStream);
        assertEquals(LZMACompressorOutputStream.class, compressorOutputStream.getClass());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        verify(compressorOutputStream).reset();
    }

    @Test
    public void testCreateCompressorOutputStreamPack200() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("pack200", outputStream);
        assertEquals(Pack200CompressorOutputStream.class, compressorOutputStream.getClass());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        verify(compressorOutputStream).reset();
    }

    @Test
    public void testCreateCompressorOutputStreamDeflate() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("deflate", outputStream);
        assertEquals(DeflateCompressorOutputStream.class, compressorOutputStream.getClass());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        verify(compressorOutputStream).reset();
    }

    @Test
    public void testCreateCompressorOutputStreamBzip2DecompressConcatenatedTrue() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory(true).createCompressorInputStream("bzip2", inputStream);
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory(true).createCompressorOutputStream("bzip2", outputStream);
        IOUtils.copy(compressorInputStream, compressorOutputStream);
        compressorInputStream.close();
        compressorOutputStream.finish();
        compressorOutputStream.close();
        byte[] output = outputStream.toByteArray();
        byte[] expectedOutput = BZip2CompressorInputStream.BZIP2_SIGNATURE;
        assertEquals(expectedOutput.length, output.length);
        for (int i = 0; i < expectedOutput.length; i++) {
            assertEquals(expectedOutput[i], output[i]);
        }
    }

    @Test
    public void testCreateCompressorOutputStreamBzip2DecompressConcatenatedFalse() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("bzip2", inputStream);
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("bzip2", outputStream);
        IOUtils.copy(compressorInputStream, compressorOutputStream, 1024);
        compressorInputStream.close();
        compressorOutputStream.finish();
        compressorOutputStream.close();
        byte[] output = outputStream.toByteArray();
        byte[] expectedOutput = new byte[1024];
        System.arraycopy(BZip2CompressorInputStream.BZIP2_SIGNATURE, 0, expectedOutput, 0, BZip2CompressorInputStream.BZIP2_SIGNATURE.length);
        assertEquals(expectedOutput.length, output.length);
        for (int i = 0; i < expectedOutput.length; i++) {
            assertEquals(expectedOutput[i], output[i]);
        }
    }

    @Test
    public void testGetDecompressConcatenated() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        boolean actual = factory.getDecompressConcatenated();
        assertTrue(actual);
    }

    @Test
    public void testGetDecompressConcatenatedDefaultValue() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        boolean actual = factory.getDecompressConcatenated();
        assertFalse(actual);
    }
}