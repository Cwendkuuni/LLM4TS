import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

    @Test
    public void testParseValidHtml() {
        String html = "<html><head><title>Test</title></head><body><p>Hi</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("<html><head><title>Test</title></head><body><p>Hi</p></body></html>", doc.html());
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("", doc.html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        String html = "<html><body></html>";
        Parser parser = new Parser(html, null, false);
        parser.parse(); // This line will throw an IllegalArgumentException
    }
}