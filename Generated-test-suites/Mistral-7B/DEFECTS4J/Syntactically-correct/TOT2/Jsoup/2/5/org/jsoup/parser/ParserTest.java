import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head>" +
                "<body><h1>Welcome to Test Page</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Assert.assertEquals("Test Page", doc.title());
        Assert.assertEquals("<html><head><title>Test Page</title></head>" +
                "<body><h1>Welcome to Test Page</h1></body></html>", doc.html());
    }
}