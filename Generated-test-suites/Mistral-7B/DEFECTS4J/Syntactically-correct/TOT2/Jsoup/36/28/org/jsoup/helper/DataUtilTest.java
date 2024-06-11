import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with actual test file
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "Test data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = null;
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "Test data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-16"));
        String charsetName = "UTF-16";
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "Test data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = DataUtil.Parser.xmlParser(); // or any other Parser
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = null; // should throw IOException
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.fail(); // Test should not reach this point
    }
}