package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test factory for {@link CompressorStreamFactory}.
 *
 * @since 1.0
 */
public class CompressorStreamFactoryTest {

    /**
     * Test creating a compressor input stream from a valid compressed input stream.
     */
    @Test
    public void testCreateCompressorInputStreamFromValidStream() throws Exception {
        // Gzip
        InputStream gzipIs = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_MAGIC);
        CompressorInputStream gzipCis = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, gzipIs);
        assertEquals(GzipCompressorInputStream.class, gzipCis.getClass());

        // Bzip2
        InputStream bzip2Is = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream bzip2Cis = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, bzip2Is);
        assertEquals(BZip2CompressorInputStream.class, bzip2Cis.getClass());

        // XZ
        InputStream xzIs = new ByteArrayInputStream(XZCompressorInputStream.XZ_SIG);
        CompressorInputStream xzCis = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.XZ, xzIs);
        assertEquals(XZCompressorInputStream.class, xzCis.getClass());

        // LZMA
        InputStream lzmaIs = new ByteArrayInputStream(LZMACompressorInputStream.LZMA_MAGIC);
        CompressorInputStream lzmaCis = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.LZMA, lzmaIs);
        assertEquals(LZMACompressorInputStream.class, lzmaCis.getClass());

        // Deflate
        InputStream deflateIs = new ByteArrayInputStream(DeflateCompressorInputStream.DEFLATE_SIG);
        CompressorInputStream deflateCis = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.DEFLATE, deflateIs);
        assertEquals(DeflateCompressorInputStream.class, deflateCis.getClass());
    }

    /**
     * Test creating a compressor input stream from an unknown compressed input stream.
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamFromUnknownStream() throws Exception {
        InputStream unknownIs = new ByteArrayInputStream(new byte[12]);
        new CompressorStreamFactory().createCompressorInputStream("unknown", unknownIs);
    }

    /**
     * Test creating a compressor output stream and writing a file with the correct signature.
     */
    @Test
    public void testCreateCompressorOutputStreamWriteFileSignature() throws Exception {
        OutputStream gzipOs = new ByteArrayOutputStream();
        CompressorOutputStream gzipCos = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, gzipOs);

        String testString = "Test string.";
        gzipCos.write(testString.getBytes("UTF-8"));
        gzipCos.finish();

        InputStream gzipIs = new ByteArrayInputStream(gzipOs.toByteArray());

        CompressorInputStream gzipCis = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, gzipIs);
        byte[] gzipSignature = new byte[GzipCompressorInputStream.GZIP_MAGIC.length];
        gzipCis.read(gzipSignature, 0, GzipCompressorInputStream.GZIP_MAGIC.length);
        assertArrayEquals(GzipCompressorInputStream.GZIP_MAGIC, gzipSignature);

        // Repeat for other compressor types
        OutputStream bzip2Os = new ByteArrayOutputStream();
        CompressorOutputStream bzip2Cos = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, bzip2Os);
        bzip2Cos.writeTo(new ByteArrayOutputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE.length));
        bzip2Cos.finish();

        InputStream bzip2Is = new ByteArrayInputStream(bzip2Os.toByteArray());
        byte[] bzip2Signature = new byte[BZip2CompressorInputStream.BZIP2_SIGNATURE.length];
        bzip2Is.read(bzip2Signature, 0, BZip2CompressorInputStream.BZIP2_SIGNATURE.length);
        assertArrayEquals(BZip2CompressorInputStream.BZIP2_SIGNATURE, bzip2Signature);

        OutputStream xzOs = new ByteArrayOutputStream();
        CompressorOutputStream xzCos = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.XZ, xzOs);
        xzCos.writeTo(XZCompressorInputStream.XZ_SIG);
        xzCos.finish();

        InputStream xzIs = new ByteArrayInputStream(xzOs.toByteArray());
        byte[] xzSignature = new byte[XZCompressorInputStream.XZ_SIG.length];
        xzIs.read(xzSignature, 0, XZCompressorInputStream.XZ_SIG.length);
        assertArrayEquals(XZCompressorInputStream.XZ_SIG, xzSignature);

        OutputStream lzmaOs = new ByteArrayOutputStream();
        CompressorOutputStream lzmaCos = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.LZMA, lzmaOs);
        lzmaCos.writeTo(LZMACompressorInputStream.LZMA_MAGIC);
        lzmaCos.finish();

        InputStream lzmaIs = new ByteArrayInputStream(lzmaOs.toByteArray());
        byte[] lzmaSignature = new byte[LZMACompressorInputStream.LZMA_MAGIC.length];
        lzmaIs.read(lzmaSignature, 0, LZMACompressorInputStream.LZMA_MAGIC.length);
        assertArrayEquals(LZMACompressorInputStream.LZMA_MAGIC, lzmaSignature);

        OutputStream deflateOs = new ByteArrayOutputStream();
        CompressorOutputStream deflateCos = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.DEFLATE, deflateOs);
        deflateCos.writeTo(DeflateCompressorInputStream.DEFLATE_SIG);
        deflateCos.finish();

        InputStream deflateIs = new ByteArrayInputStream(deflateOs.toByteArray());
        byte[] deflateSignature = new byte[DeflateCompressorInputStream.DEFLATE_SIG.length];
        deflateIs.read(deflateSignature, 0, DeflateCompressorInputStream.DEFLATE_SIG.length);
        assertArrayEquals(DeflateCompressorInputStream.DEFLATE_SIG, deflateSignature);
    }

    /**
     * Test creating a compressor output stream and writing to it, then reading it back.
     */
    @Test
    public void testCreateCompressorOutputStreamWriteReadFile() throws Exception {
        String testString = "Test string.";

        OutputStream gzipOs = new ByteArrayOutputStream();
        CompressorOutputStream gzipCos = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, gzipOs);
        gzipCos.write(testString.getBytes("UTF-8"));
        gzipCos.finish();

        InputStream gzipIs = new ByteArrayInputStream(gzipOs.toByteArray());
        InputStream expectedIs = new ByteArrayInputStream(testString.getBytes("UTF-8"));
        assertStreamEquals(expectedIs, gzipIs);

        // Repeat for other compressor types

        OutputStream bzip2Os = new ByteArrayOutputStream();
        CompressorOutputStream bzip2Cos = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, bzip2Os);
        bzip2Cos.write(testString.getBytes("UTF-8"));
        bzip2Cos.finish();
        InputStream bzip2Is = new ByteArrayInputStream(bzip2Os.toByteArray());
        assertStreamEquals(expectedIs, bzip2Is);

        OutputStream xzOs = new ByteArrayOutputStream();
        CompressorOutputStream xzCos = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.XZ, xzOs);
        xzCos.write(testString.getBytes("UTF-8"));
        xzCos.finish();
        InputStream xzIs = new ByteArrayInputStream(xzOs.toByteArray());
        assertStreamEquals(expectedIs, xzIs);

        OutputStream lzmaOs = new ByteArrayOutputStream();
        CompressorOutputStream lzmaCos = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.LZMA, lzmaOs);
        lzmaCos.write(testString.getBytes("UTF-8"));
        lzmaCos.finish();
        InputStream lzmaIs = new ByteArrayInputStream(lzmaOs.toByteArray());
        assertStreamEquals(expectedIs, lzmaIs);

        OutputStream deflateOs = new ByteArrayOutputStream();
        CompressorOutputStream deflateCos = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.DEFLATE, deflateOs);
        deflateCos.write(testString.getBytes("UTF-8"));
        deflateCos.finish();
        InputStream deflateIs = new ByteArrayInputStream(deflateOs.toByteArray());
        assertStreamEquals(expectedIs, deflateIs);
    }

    // ... Add more tests for edge cases and error scenarios

    /**
     * Helper method for comparing InputStreams.
     *
     * @param expected The expected InputStream.
     * @param actual The actual InputStream.
     */
    private void assertStreamEquals(InputStream expected, InputStream actual) throws IOException {
        byte[] expectedByteArray = IOUtils.toByteArray(expected);
        byte[] actualByteArray = new byte[expectedByteArray.length];
        int n = actual.read(actualByteArray, 0, expectedByteArray.length);
        assertEquals(expectedByteArray.length, n);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }
}