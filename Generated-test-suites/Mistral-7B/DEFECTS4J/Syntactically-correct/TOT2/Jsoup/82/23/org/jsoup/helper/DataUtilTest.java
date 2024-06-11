import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Parser;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        File file = new File("validFile.html"); // Assuming a valid file exists with the name "validFile.html"
        Document document = DataUtil.load(file, charsetName, "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        File file = new File("validFile.html"); // Assuming a valid file exists with the name "validFile.html"
        try {
            Document document = DataUtil.load(file, charsetName, "");
            Assert.fail("Expected IOException but no exception was thrown");
        } catch (IOException e) {
            // Expected exception, do nothing
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = getClass().getResourceAsStream("/validFile.html"); // Assuming a valid file exists in the classpath with the name "/validFile.html"
        Document document = DataUtil.load(inputStream, charsetName, "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        InputStream inputStream = getClass().getResourceAsStream("/validFile.html"); // Assuming a valid file exists in the classpath with the name "/validFile.html"
        try {
            Document document = DataUtil.load(inputStream, charsetName, "");
            Assert.fail("Expected IOException but no exception was thrown");
        } catch (IOException e) {
            // Expected exception, do nothing
        }
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = getClass().getResourceAsStream("/validFile.html"); // Assuming a valid file exists in the classpath with the name "/validFile.html"
        Document document = DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        InputStream inputStream = getClass().getResourceAsStream("/validFile.html"); // Assuming a valid file exists in the classpath with the name "/validFile.html"
        try {
            Document document = DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());
            Assert.fail("Expected IOException but no exception was thrown");
        } catch (IOException e) {
            // Expected exception, do nothing
        }
    }
}