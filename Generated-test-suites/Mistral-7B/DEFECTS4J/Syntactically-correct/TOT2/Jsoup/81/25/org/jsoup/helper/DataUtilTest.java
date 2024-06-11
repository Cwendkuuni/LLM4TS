import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with actual test file path
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        InputStream input = getClass().getResourceAsStream("/test.html"); // replace with actual test file path
        Document doc = DataUtil.load(input, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInvalidInputStream() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        String charsetName = null;
        String baseUri = null;
        try {
            DataUtil.load(input, charsetName, baseUri);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        InputStream input = getClass().getResourceAsStream("/test.html"); // replace with actual test file path
        Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInvalidInputStream() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        String charsetName = null;
        String baseUri = null;
        try {
            DataUtil.parseInputStream(input, charsetName, baseUri, Parser.htmlParser());
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }
}