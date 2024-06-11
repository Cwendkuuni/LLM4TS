import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_Gzip() throws IOException, CompressorException {
        String name = "gzip";
        InputStream input = new ByteArrayInputStream("Gzip signature".getBytes());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, input);
        Assert.assertEquals(name, compressorInputStream.getFormatName());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_Bzip2() throws IOException, CompressorException {
        String name = "bzip2";
        InputStream input = new ByteArrayInputStream("Bzip2 signature".getBytes());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, input);
        Assert.assertEquals(name, compressorInputStream.getFormatName());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_Xz() throws IOException, CompressorException {
        String name = "xz";
        InputStream input = new ByteArrayInputStream("Xz signature".getBytes());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, input);
        Assert.assertEquals(name, compressorInputStream.getFormatName());
        compressorInputStream.close();
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownFormat() throws IOException, CompressorException {
        String name = "unknown";
        InputStream input = new ByteArrayInputStream("Any signature".getBytes());
        new CompressorStreamFactory().createCompressorInputStream(name, input);
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() throws IOException, CompressorException {
        String name = "gzip";
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, output);
        compressorOutputStream.write("Test data".getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(output.toByteArray());
        CompressorInputStream testCompressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, testInputStream);
        byte[] expectedData = "Test data".getBytes();
        Assert.assertArrayEquals(expectedData, IOUtils.toByteArray(testCompressorInputStream));
        testCompressorInputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_Bzip2() throws IOException, CompressorException {
        String name = "bzip2";
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, output);
        compressorOutputStream.write("Test data".getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(output.toByteArray());
        CompressorInputStream testCompressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, testInputStream);
        byte[] expectedData = "Test data".getBytes();
        Assert.assertArrayEquals(expectedData, IOUtils.toByteArray(testCompressorInputStream));
        testCompressorInputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_Xz() throws IOException, CompressorException {
        String name = "xz";
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, output);
        compressorOutputStream.write("Test data".getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(output.toByteArray());
        CompressorInputStream testCompressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, testInputStream);
        byte[] expectedData = "Test data".getBytes();
        Assert.assertArrayEquals(expectedData, IOUtils.toByteArray(testCompressorInputStream));
        testCompressorInputStream.close();
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_UnknownFormat() throws IOException, CompressorException {
        String name = "unknown";
        OutputStream output = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream(name, output);
    }

    @Test
    public void testDecompressConcatenated() throws IOException, CompressorException {
        String name = "gzip";
        OutputStream output = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(name, output);
        compressorOutputStream.write("Test data 1".getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.write("Test data 2".getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(output.toByteArray());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, testInputStream);
        byte[] expectedData1 = "Test data 1".getBytes();
        byte[] expectedData2 = "Test data 2".getBytes();
        Assert.assertArrayEquals(expectedData1, IOUtils.toByteArray(compressorInputStream));
        Assert.assertArrayEquals(expectedData2, IOUtils.toByteArray(compressorInputStream));
        compressorInputStream.close();
    }
}