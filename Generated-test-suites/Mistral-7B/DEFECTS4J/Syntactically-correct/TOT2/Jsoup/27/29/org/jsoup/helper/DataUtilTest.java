import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with the actual path of a valid HTML file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFile() throws IOException {
        File file = new File("does_not_exist.html"); // Replace with the actual path of a non-existent file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        try {
            DataUtil.load(file, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected exception, nothing to do here
        }
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";

        try {
            DataUtil.load(file, charsetName, baseUri);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception, nothing to do here
        }
    }

    @Test
    public void testLoadFileWithNullBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = null;

        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }
}