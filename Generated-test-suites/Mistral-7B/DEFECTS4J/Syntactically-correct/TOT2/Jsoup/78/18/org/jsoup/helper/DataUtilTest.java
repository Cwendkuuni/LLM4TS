import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with the actual path to a valid HTML file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        byte[] htmlBytes = html.getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(htmlBytes);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String invalidHtml = "<html><body><h1>Hello World</h1></body></html>";
        byte[] htmlBytes = invalidHtml.getBytes("ISO-8859-1"); // Invalid charset
        InputStream inputStream = new ByteArrayInputStream(htmlBytes);
        String charsetName = "ISO-8859-1";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}