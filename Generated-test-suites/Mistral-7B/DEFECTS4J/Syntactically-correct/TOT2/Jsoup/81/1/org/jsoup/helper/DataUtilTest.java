import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // assuming you have a test.html file in your project
        Document doc = DataUtil.load(file, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "Test Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "Test Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source Data";
        String destination = "Destination Data";
        InputStream inputStream = new ByteArrayInputStream(source.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] destinationData = outputStream.toByteArray();
        String actual = new String(destinationData, "UTF-8");
        Assert.assertEquals(destination, actual);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "InvalidCharsetName"); // replace with an invalid charset name
        Assert.fail(); // Test should throw IOException
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testData = "Test Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        String charsetName = "InvalidCharsetName"; // replace with an invalid charset name

        try {
            Document doc = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
            Assert.fail();
        } catch (IOException e) {
            // Expected exception
        }
    }
}