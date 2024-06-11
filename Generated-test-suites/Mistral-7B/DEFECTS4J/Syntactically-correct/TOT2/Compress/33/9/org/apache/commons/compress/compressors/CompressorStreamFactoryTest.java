import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressorStreamFactoryTest {

    private static final String TEST_FILE = "TestData.txt";
    private static final String TEST_CONTENT = "This is a test file for CompressorStreamFactory.";

    @BeforeClass
    public static void setup() {
        try {
            IOUtils.write(TEST_CONTENT.getBytes(), new FileOutputStream(TEST_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateBZip2CompressorInputStream() throws CompressorException, IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);

        byte[] expected = IOUtils.toByteArray(inputStream);
        byte[] actual = IOUtils.toByteArray(compressorInputStream);
        Assert.assertArrayEquals(expected, actual);
        compressorInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateBZip2CompressorOutputStream() throws IOException {
        String outputFileName = "TestData_bzip2.bz2";
        OutputStream outputStream = new FileOutputStream(outputFileName);
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);

        String inputContent = "This is a test file for CompressorStreamFactory.";
        compressorOutputStream.write(inputContent.getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();

        InputStream inputStream = new FileInputStream(outputFileName);
        byte[] expected = inputContent.getBytes();
        byte[] actual = IOUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(expected, actual);
        inputStream.close();
        new File(outputFileName).delete();
    }

    @Test(expected = CompressorException.class)
    public void testCreateUnsupportedCompressorInputStream() throws CompressorException, IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new CompressorStreamFactory().createCompressorInputStream("unsupported", inputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateUnsupportedCompressorOutputStream() throws CompressorException, IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream("unsupported", outputStream);
    }

    @Test
    public void testCreateDecompressorInputStream() throws CompressorException, IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, inputStream);

        byte[] expected = TEST_CONTENT.getBytes();
        byte[] actual = IOUtils.toByteArray(compressorInputStream);
        Assert.assertArrayEquals(expected, actual);
        compressorInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateDecompressorOutputStream() throws IOException {
        String outputFileName = "TestData_deflate.gz";
        OutputStream outputStream = new FileOutputStream(outputFileName);
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.DEFLATE, outputStream);

        String inputContent = "This is a test file for CompressorStreamFactory.";
        compressorOutputStream.write(inputContent.getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();

        InputStream inputStream = new FileInputStream(outputFileName);
        byte[] expected = inputContent.getBytes();
        byte[] actual = IOUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(expected, actual);
        inputStream.close();
        new File(outputFileName).delete();
    }

    @Test
    public void testCreatePack200CompressorInputStream() throws CompressorException, IOException {
        InputStream inputStream = new FileInputStream("TestData.pack");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.PACK200, inputStream);

        byte[] expected = TEST_CONTENT.getBytes();
        byte[] actual = IOUtils.toByteArray(compressorInputStream);
        Assert.assertArrayEquals(expected, actual);
        compressorInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreatePack200CompressorOutputStream() throws IOException {
        String outputFileName = "TestData_pack.pack";
        OutputStream outputStream = new FileOutputStream(outputFileName);
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.PACK200, outputStream);

        String inputContent = "This is a test file for CompressorStreamFactory.";
        compressorOutputStream.write(inputContent.getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();

        InputStream inputStream = new FileInputStream(outputFileName);
        byte[] expected = inputContent.getBytes();
        byte[] actual = IOUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(expected, actual);
        inputStream.close();
        new File(outputFileName).delete();
    }

    @Test
    public void testCreateXZCompressorInputStream() throws CompressorException, IOException {
        InputStream inputStream = new FileInputStream("TestData.xz");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.XZ, inputStream);

        byte[] expected = TEST_CONTENT.getBytes();
        byte[] actual = IOUtils.toByteArray(compressorInputStream);
        Assert.assertArrayEquals(expected, actual);
        compressorInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateXZCompressorOutputStream() throws IOException {
        String outputFileName = "TestData_xz.xz";
        OutputStream outputStream = new FileOutputStream(outputFileName);
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.XZ, outputStream);

        String inputContent = "This is a test file for CompressorStreamFactory.";
        compressorOutputStream.write(inputContent.getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();

        InputStream inputStream = new FileInputStream(outputFileName);
        byte[] expected = inputContent.getBytes();
        byte[] actual = IOUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(expected, actual);
        inputStream.close();
        new File(outputFileName).delete();
    }

    @Test
    public void testCreateLzmaCompressorInputStream() throws CompressorException, IOException {
        InputStream inputStream = new FileInputStream("TestData.lzma");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.LZMA, inputStream);

        byte[] expected = TEST_CONTENT.getBytes();
        byte[] actual = IOUtils.toByteArray(compressorInputStream);
        Assert.assertArrayEquals(expected, actual);
        compressorInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateLzmaCompressorOutputStream() throws IOException {
        String outputFileName = "TestData_lzma.lzma";
        OutputStream outputStream = new FileOutputStream(outputFileName);
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.LZMA, outputStream);

        String inputContent = "This is a test file for CompressorStreamFactory.";
        compressorOutputStream.write(inputContent.getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();

        InputStream inputStream = new FileInputStream(outputFileName);
        byte[] expected = inputContent.getBytes();
        byte[] actual = IOUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(expected, actual);
        inputStream.close();
        new File(outputFileName).delete();
    }

    @Test
    public void testCreateSnappyRawCompressorInputStream() throws CompressorException, IOException {
        InputStream inputStream = new FileInputStream("TestData.raw.snappy");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.SNAPPY_RAW, inputStream);

        byte[] expected = TEST_CONTENT.getBytes();
        byte[] actual = IOUtils.toByteArray(compressorInputStream);
        Assert.assertArrayEquals(expected, actual);
        compressorInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateSnappyFramedCompressorInputStream() throws CompressorException, IOException {
        InputStream inputStream = new FileInputStream("TestData.framed.snappy");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.SNAPPY_FRAMED, inputStream);

        byte[] expected = TEST_CONTENT.getBytes();
        byte[] actual = IOUtils.toByteArray(compressorInputStream);
        Assert.assertArrayEquals(expected, actual);
        compressorInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateZCompressorInputStream() throws CompressorException, IOException {
        InputStream inputStream = new FileInputStream("TestData.Z");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.Z, inputStream);

        byte[] expected = TEST_CONTENT.getBytes();
        byte[] actual = IOUtils.toByteArray(compressorInputStream);
        Assert.assertArrayEquals(expected, actual);
        compressorInputStream.close();
        inputStream.close();
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        boolean result = factory.getDecompressConcatenated();
        Assert.assertTrue(result);

        factory = new CompressorStreamFactory();
        result = factory.getDecompressConcatenated();
        Assert.assertFalse(result);
    }
}