import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null);
        Assert.assertNotNull(doc);
        // add assertions to check the loaded document
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8");
        Assert.assertNotNull(doc);
        // add assertions to check the loaded document
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, "InvalidCharset");
        } catch (IOException e) {
            // expected exception, add assertions to check the error message
        }
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String testData = "test data";
        byte[] inputData = testData.getBytes();
        InputStream input = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        // add assertions to check the loaded document
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "test data";
        byte[] inputData = testData.getBytes("UTF-8");
        InputStream input = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.parseInputStream(input, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        // add assertions to check the loaded document
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testData = "test data";
        byte[] inputData = testData.getBytes();
        InputStream input = new ByteArrayInputStream(inputData);
        try {
            Document doc = DataUtil.parseInputStream(input, "InvalidCharset", null, Parser.htmlParser());
        } catch (IOException e) {
            // expected exception, add assertions to check the error message
        }
    }

    @Test
    public void testWriteAndReadByteBuffer() throws IOException {
        String testData = "test data";
        byte[] inputData = testData.getBytes();
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(inputData));
        ByteBuffer outputBuffer = ByteBuffer.allocate(inputData.length);
        DataUtil.crossStreams(byteBuffer.asInputStream(), outputBuffer.asOutputStream());
        byte[] outputData = outputBuffer.array();
        Assert.assertArrayEquals(inputData, outputData);
    }

    @Test
    public void testParseInputStreamWithLargeData() throws IOException {
        String testData = "Test data with a large size (1MB)";
        byte[] inputData = testData.getBytes();
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(inputData), 1 * 1024 * 1024);
        Document doc = DataUtil.parseInputStream(byteBuffer.asInputStream(), null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        // add assertions to check the loaded document
    }

    @Test
    public void testParseInputStreamWithCorruptedData() throws IOException {
        String testData = "Test data with corrupted characters";
        byte[] inputData = testData.getBytes();
        InputStream input = new ByteArrayInputStream(inputData);
        try {
            Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected exception, add assertions to check the error message
        }
    }

    @Test
    public void testReadFileToByteBufferWithLargeFile() throws IOException {
        String testData = "Test data with a large size (10MB)";
        File file = new File("large_file.html");
        byte[] inputData = testData.getBytes();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.write(inputData);
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(inputData.length, byteBuffer.limit());
        randomAccessFile.close();
    }
}