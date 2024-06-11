import org.junit.Test;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
        // Add assertions to check the loaded document
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
        // Add assertions to check the loaded document
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("ISO-8859-1"));
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "");
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        // Add assertions to check the loaded document
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("ISO-8859-1"));
        try {
            Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }
}