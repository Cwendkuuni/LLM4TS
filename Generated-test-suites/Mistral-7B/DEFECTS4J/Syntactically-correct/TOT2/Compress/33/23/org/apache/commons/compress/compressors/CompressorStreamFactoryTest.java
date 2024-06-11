import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test cases for the CompressorStreamFactory class.
 */
public class CompressorStreamFactoryTest {

    /**
     * Test creating compressor input streams for different compressors.
     */
    @Test
    public void testCreateCompressorInputStream() throws IOException {
        // Test Gzip compressor
        CompressorInputStream gzipIn = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, new ByteArrayInputStream(new byte[0]));
        Assert.assertEquals(GzipCompressorInputStream.class, gzipIn.getClass());
        gzipIn.close();

        // Test Bzip2 compressor
        CompressorInputStream bzip2In = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, new ByteArrayInputStream(new byte[0]));
        Assert.assertEquals(BZip2CompressorInputStream.class, bzip2In.getClass());
        bzip2In.close();

        // Test Deflate compressor
        CompressorInputStream deflateIn = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.DEFLATE, new ByteArrayInputStream(new byte[0]));
        Assert.assertEquals(DeflateCompressorInputStream.class, deflateIn.getClass());
        deflateIn.close();

        // Test XZ compressor
        CompressorInputStream xzIn = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.XZ, new ByteArrayInputStream(new byte[0]));
        Assert.assertEquals(XZCompressorInputStream.class, xzIn.getClass());
        xzIn.close();

        // Test Pack200 compressor
        CompressorInputStream pack200In = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.PACK200, new ByteArrayInputStream(new byte[0]));
        Assert.assertEquals(Pack200CompressorInputStream.class, pack200In.getClass());
        pack200In.close();
    }

    /**
     * Test creating compressor output streams for different compressors.
     */
    @Test
    public void testCreateCompressorOutputStream() throws IOException {
        // Test Gzip compressor
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream gzipOut = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        gzipOut.write("Test data".getBytes());
        gzipOut.finish();
        Assert.assertEquals("Test data", new String(output.toByteArray()));
        gzipOut.close();

        // Test Bzip2 compressor
        OutputStream outputBzip2 = new ByteArrayOutputStream();
        CompressorOutputStream bzip2Out = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputBzip2);
        bzip2Out.write("Test data".getBytes());
        bzip2Out.finish();
        byte[] bzip2Output = outputBzip2.toByteArray();
        // Perform BZip2 verification here
        bzip2Out.close();

        // Test Deflate compressor
        OutputStream outputDeflate = new ByteArrayOutputStream();
        CompressorOutputStream deflateOut = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.DEFLATE, outputDeflate);
        deflateOut.write("Test data".getBytes());
        deflateOut.finish();
        byte[] deflateOutput = outputDeflate.toByteArray();
        // Perform Deflate verification here
        deflateOut.close();

        // Test XZ compressor
        OutputStream outputXZ = new ByteArrayOutputStream();
        CompressorOutputStream xzOut = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.XZ, outputXZ);
        xzOut.write("Test data".getBytes());
        xzOut.finish();
        byte[] xzOutput = outputXZ.toByteArray();
        // Perform XZ verification here
        xzOut.close();
    }

    /**
     * Test creating compressor input streams with invalid compressors.
     */
    @Test(expected = CompressorException.class)
    public void testCreateInvalidCompressorInputStream() throws IOException {
        new CompressorStreamFactory().createCompressorInputStream("InvalidCompressor", new ByteArrayInputStream(new byte[0]));
    }

    /**
     * Test creating compressor output streams with invalid compressors.
     */
    @Test(expected = CompressorException.class)
    public void testCreateInvalidCompressorOutputStream() throws IOException {
        new CompressorStreamFactory().createCompressorOutputStream("InvalidCompressor", new ByteArrayOutputStream());
    }
}