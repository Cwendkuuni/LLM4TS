import org.apache.commons.compress.CompressorStreams;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.test.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressorStreamFactoryTest {

    private CompressorInputStream inputStream;
    private CompressorOutputStream outputStream;

    @Before
    public void setUp() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
    }

    @Test
    public void testCreateCompressorInputStream_Gzip() throws IOException {
        InputStream input = new ByteArrayInputStream(TestUtils.GZIP_SIGNATURE.getBytes());
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        Assert.assertEquals(CompressorStreams.GZIP, compressorInputStream.getCompressor());
    }

    @Test
    public void testCreateCompressorInputStream_Bzip2() throws IOException {
        InputStream input = new ByteArrayInputStream(TestUtils.BZIP2_SIGNATURE.getBytes());
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        Assert.assertEquals(CompressorStreams.BZIP2, compressorInputStream.getCompressor());
    }

    @Test
    public void testCreateCompressorInputStream_Xz() throws IOException {
        InputStream input = new ByteArrayInputStream(TestUtils.XZ_SIGNATURE.getBytes());
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.XZ, input);
        Assert.assertEquals(CompressorStreams.XZ, compressorInputStream.getCompressor());
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownCompressor() throws IOException {
        InputStream input = new ByteArrayInputStream(TestUtils.GZIP_SIGNATURE.getBytes());
        factory.createCompressorInputStream("unknown", input);
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        compressorOutputStream.write("Test".getBytes());
        compressorOutputStream.finish();
        Assert.assertEquals(TestUtils.GZIP_SIGNATURE, compressorOutputStream.getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().toByteArray());
    }

    @Test
    public void testCreateCompressorOutputStream_Bzip2() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        compressorOutputStream.write("Test".getBytes());
        compressorOutputStream.finish();
        Assert.assertEquals(TestUtils.BZIP2_SIGNATURE, compressorOutputStream.getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().getOutputStream().toByteArray());
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_UnknownCompressor() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        factory.createCompressorOutputStream("unknown", output);
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        Assert.assertTrue(factory.getDecompressConcatenated());
    }

    @Test
    public void testGetDecompressConcatenated_Default() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        Assert.assertFalse(factory.getDecompressConcatenated());
    }
}