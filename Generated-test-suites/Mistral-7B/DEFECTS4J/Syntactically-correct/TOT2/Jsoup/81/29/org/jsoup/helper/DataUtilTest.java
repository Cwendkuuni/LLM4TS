import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testLoadInvalidInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            DataUtil.load(inputStream, "UTF-8", null);
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseInvalidInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }
}