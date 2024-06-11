import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html"); // replace with a valid file path
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            Assert.fail("Failed to load file: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        String html = "<html><body></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.assertNotNull(doc);
            Assert.assertEquals("<html><body></body></html>", doc.html());
        } catch (IOException e) {
            Assert.fail("Failed to load input stream: " + e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // ignore close exception
            }
        }
    }

    @Test
    public void testParseInputStream() {
        String html = "<html><body></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes(Charset.forName("UTF-8")));

        try {
            Document doc = DataUtil.parseInputStream(byteBuffer.asInputStream(), charsetName, baseUri, Parser.htmlParser());
            Assert.assertNotNull(doc);
            Assert.assertEquals("<html><body></body></html>", doc.html());
        } catch (IOException e) {
            Assert.fail("Failed to parse input stream: " + e.getMessage());
        }
    }
}