import org.apache.commons.compress.compressors.CompressorException;
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
 * Test class for CompressorStreamFactory.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws Exception {
        // Test creating BZIP2 compressor input stream from known BZIP2 signature
        final InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
        Assert.assertNotNull(compressorInputStream);

        // Test creating invalid compressor input stream
        final InputStream invalidInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            new CompressorStreamFactory().createCompressorInputStream("INVALID", invalidInputStream);
            Assert.fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }

        // Test creating unknown compressor input stream
        final InputStream unknownInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            new CompressorStreamFactory().createCompressorInputStream("UNKNOWN", unknownInputStream);
            Assert.fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }

        // Test creating GZIP compressor input stream from known GZIP signature
        final InputStream gzipInputStream = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream gzipCompressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, gzipInputStream);
        Assert.assertNotNull(gzipCompressorInputStream);

        // Test creating compressor input stream with custom decompressConcatenated flag
        final boolean decompressConcatenated = false;
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory(decompressConcatenated);
        CompressorInputStream compressorInputStreamWithFlag = compressorStreamFactory.createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
        Assert.assertNotEquals(new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream), compressorInputStreamWithFlag);
    }

    @Test
    public void testCreateCompressorOutputStream() throws Exception {
        // Test creating BZIP2 compressor output stream
        final OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
        Assert.assertNotNull(compressorOutputStream);

        // Test creating invalid compressor output stream
        try {
            new CompressorStreamFactory().createCompressorOutputStream("INVALID", outputStream);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test creating unknown compressor output stream
        try {
            new CompressorStreamFactory().createCompressorOutputStream("UNKNOWN", outputStream);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test creating GZIP compressor output stream
        final OutputStream gzipOutputStream = new ByteArrayOutputStream();
        CompressorOutputStream gzipCompressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, gzipOutputStream);
        Assert.assertNotNull(gzipCompressorOutputStream);

        // Test creating compressor output stream with custom decompressConcatenated flag
        final boolean decompressConcatenated = false;
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory(decompressConcatenated);
        CompressorOutputStream compressorOutputStreamWithFlag = compressorStreamFactory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
        Assert.assertNotEquals(new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream), compressorOutputStreamWithFlag);
    }

    @Test
    public void testInputOutputStreamsWithSameSize() throws Exception {
        // Test creating BZIP2 compressor input and output stream and copying input to output
        final InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, new ByteArrayOutputStream());

        IOUtils.copy(compressorInputStream, compressorOutputStream);
        compressorInputStream.close();
        compressorOutputStream.close();

        final byte[] expected = IOUtils.toByteArray(new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE));
        final byte[] actual = IOUtils.toByteArray(new ByteArrayInputStream(compressorOutputStream.getOutputStream().toByteArray()));
        Assert.assertArrayEquals(expected, actual);

        // Test creating other compressor input and output streams and copying input to output
        final InputStream gzipInputStream = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream gzipCompressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, gzipInputStream);
        CompressorOutputStream gzipCompressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, new ByteArrayOutputStream());

        IOUtils.copy(gzipCompressorInputStream, gzipCompressorOutputStream);
        gzipCompressorInputStream.close();
        gzipCompressorOutputStream.close();

        final byte[] expectedGzip = IOUtils.toByteArray(new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE));
        final byte[] actualGzip = IOUtils.toByteArray(new ByteArrayInputStream(gzipCompressorOutputStream.getOutputStream().toByteArray()));
        Assert.assertArrayEquals(expectedGzip, actualGzip);
    }

    @Test
    public void testInputOutputStreamsWithDifferentSizes() throws Exception {
        // Test creating BZIP2 compressor input and output stream and copying more data to output than input
        final InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, new ByteArrayOutputStream());

        final byte[] dataToWrite = new byte[1024];
        IOUtils.write(dataToWrite, 0, compressorOutputStream.getOutputStream(), 1024);
        IOUtils.copy(compressorInputStream, compressorOutputStream);
        compressorInputStream.close();
        compressorOutputStream.close();

        final byte[] expected = IOUtils.toByteArray(new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE));
        final byte[] actual = new byte[1024 + BZip2CompressorInputStream.BZIP2_SIGNATURE.length];
        IOUtils.readFully(new ByteArrayInputStream(compressorOutputStream.getOutputStream().toByteArray()), actual, 0, actual.length);
        Assert.assertArrayEquals(expected, IOUtils.subarray(actual, 0, expected.length));

        // Test creating other compressor input and output streams and copying more data to output than input
        final InputStream gzipInputStream = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream gzipCompressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, gzipInputStream);
        CompressorOutputStream gzipCompressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, new ByteArrayOutputStream());

        final byte[] dataToWriteGzip = new byte[1024];
        IOUtils.write(dataToWriteGzip, 0, gzipCompressorOutputStream.getOutputStream(), 1024);
        IOUtils.copy(gzipCompressorInputStream, gzipCompressorOutputStream);
        gzipCompressorInputStream.close();
        gzipCompressorOutputStream.close();

        final byte[] expectedGzip = IOUtils.toByteArray(new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE));
        final byte[] actualGzip = new byte[1024 + GzipCompressorInputStream.GZIP_SIGNATURE.length];
        IOUtils.readFully(new ByteArrayInputStream(gzipCompressorOutputStream.getOutputStream().toByteArray()), actualGzip, 0, actualGzip.length);
        Assert.assertArrayEquals(expectedGzip, IOUtils.subarray(actualGzip, 0, expectedGzip.length));
    }
}