import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // assuming test.html exists in classpath
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(file, null, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html"); // assuming test.html exists in classpath
        String charsetName = "UTF-16";
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        // Add assertions based on expected document content when using UTF-16 charset
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputData = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(inputStream, null, baseUri, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputData = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        String charsetName = "UTF-16";
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        // Add assertions based on expected document content when using UTF-16 charset
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String inputData = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        String baseUri = "http://test.com";
        Document doc = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String inputData = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        String charsetName = "UTF-16";
        String baseUri = "http://test.com";
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.xmlParser());
        // Add assertions based on expected document content when using UTF-16 charset
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Test input data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String expectedOutput = "Test input data";
        Assert.assertEquals(expectedOutput, outputStream.toString());
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithIOException() throws IOException {
        String inputData = "Test input data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        OutputStream outputStream = new NullOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputData = "Test input data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedByteData = inputData.getBytes();
        byte[] actualByteData = byteBuffer.array();
        Assert.assertArrayEquals(expectedByteData, actualByteData);
    }

    @Test
    public void testReadToByteBufferWithEmptyInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertEquals(ByteBuffer.allocate(0).limit(), byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String filePath = "test.html"; // assuming test.html exists in classpath
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File(filePath));
        // Add assertions based on expected file content
        Assert.assertNotNull(byteBuffer);
    }

    @Test(expected = FileNotFoundException.class)
    public void testReadFileToByteBufferWithMissingFile() throws IOException {
        String filePath = "non-existent-file.html";
        DataUtil.readFileToByteBuffer(new File(filePath));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(ByteBuffer.allocate(0).limit(), byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/plain";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-16";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals("UTF-16", validatedCharset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "invalid-charset";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        int boundaryLength = DataUtil.boundaryLength;
        Assert.assertEquals(boundaryLength, mimeBoundary.length());
    }
}

class NullOutputStream extends OutputStream {
    @Override
    public void write(int b) {
        // Empty implementation
    }

    @Override
    public void close() throws IOException {
        // Empty implementation
    }
}