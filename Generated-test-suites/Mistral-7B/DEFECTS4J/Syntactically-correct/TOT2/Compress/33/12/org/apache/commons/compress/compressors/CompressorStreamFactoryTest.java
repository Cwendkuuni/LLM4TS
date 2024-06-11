import org.apache.commons.compress.CompressorException;
import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Testing for CompressorStreamFactory.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws Exception {
        // Test GZIP
        InputStream gzipInputStream = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream gzipCompressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, gzipInputStream);
        Assert.assertNotNull(gzipCompressorInputStream);

        // Test BZIP2
        InputStream bzip2InputStream = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream bzip2CompressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, bzip2InputStream);
        Assert.assertNotNull(bzip2CompressorInputStream);

        // Test XZ
        InputStream xzInputStream = new ByteArrayInputStream(XZCompressorInputStream.XZ_SIGNATURE);
        CompressorInputStream xzCompressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.XZ, xzInputStream);
        Assert.assertNotNull(xzCompressorInputStream);

        // Test DEFLATE
        InputStream deflateInputStream = new ByteArrayInputStream(DeflateCompressorInputStream.DEFLATE_SIGNATURE);
        CompressorInputStream deflateCompressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.DEFLATE, deflateInputStream);
        Assert.assertNotNull(deflateCompressorInputStream);

        // Test Unknown compression type
        InputStream unknownInputStream = new ByteArrayInputStream(new byte[10]);
        try {
            new CompressorStreamFactory().createCompressorInputStream("unknown", unknownInputStream);
            Assert.fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream() throws Exception {
        // Test GZIP
        OutputStream gzipOutputStream = new ByteArrayOutputStream();
        CompressorOutputStream gzipCompressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, gzipOutputStream);
        Assert.assertNotNull(gzipCompressorOutputStream);

        // Test BZIP2
        OutputStream bzip2OutputStream = new ByteArrayOutputStream();
        CompressorOutputStream bzip2CompressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, bzip2OutputStream);
        Assert.assertNotNull(bzip2CompressorOutputStream);

        // Test XZ
        OutputStream xzOutputStream = new ByteArrayOutputStream();
        CompressorOutputStream xzCompressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.XZ, xzOutputStream);
        Assert.assertNotNull(xzCompressorOutputStream);

        // Test DEFLATE
        OutputStream deflateOutputStream = new ByteArrayOutputStream();
        CompressorOutputStream deflateCompressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.DEFLATE, deflateOutputStream);
        Assert.assertNotNull(deflateCompressorOutputStream);

        // Test Unknown compression type
        OutputStream unknownOutputStream = new ByteArrayOutputStream();
        try {
            new CompressorStreamFactory().createCompressorOutputStream("unknown", unknownOutputStream);
            Assert.fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testDecompressConcatenated() throws Exception {
        // Test GZIP with decompressConcatenated = true
        InputStream gzipInputStream = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream gzipCompressorInputStream = new CompressorStreamFactory(true)
                .createCompressorInputStream(CompressorStreamFactory.GZIP, gzipInputStream);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        IOUtils.copy(gzipCompressorInputStream, result);
        Assert.assertEquals(gzipInputStream.available(), result.size());

        // Test GZIP with decompressConcatenated = false
        InputStream gzipInputStreamFalse = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream gzipCompressorInputStreamFalse = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, gzipInputStreamFalse);
        ByteArrayOutputStream resultFalse = new ByteArrayOutputStream();
        IOUtils.copy(gzipCompressorInputStreamFalse, resultFalse);
        Assert.assertEquals(gzipInputStream.available(), resultFalse.size());
        Assert.assertEquals(gzipInputStream.available(), result.size());
        Assert.assertNotSame(resultFalse.toByteArray(), result.toByteArray());

        // Test other compressors with decompressConcatenated = true/false
        InputStream bzip2InputStream = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        InputStream xzInputStream = new ByteArrayInputStream(XZCompressorInputStream.XZ_SIGNATURE);
        InputStream deflateInputStream = new ByteArrayInputStream(DeflateCompressorInputStream.DEFLATE_SIGNATURE);

        for (InputStream inputStream : new InputStream[] {bzip2InputStream, xzInputStream, deflateInputStream}) {
            CompressorInputStream compressorInputStream = new CompressorStreamFactory(true)
                    .createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
            ByteArrayOutputStream resultTrue = new ByteArrayOutputStream();
            IOUtils.copy(compressorInputStream, resultTrue);
            Assert.assertEquals(inputStream.available(), resultTrue.size());

            compressorInputStream = new CompressorStreamFactory()
                    .createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
            ByteArrayOutputStream resultFalse = new ByteArrayOutputStream();
            IOUtils.copy(compressorInputStream, resultFalse);
            Assert.assertEquals(inputStream.available(), resultFalse.size());
            Assert.assertNotSame(resultFalse.toByteArray(), resultTrue.toByteArray());
        }
    }
}