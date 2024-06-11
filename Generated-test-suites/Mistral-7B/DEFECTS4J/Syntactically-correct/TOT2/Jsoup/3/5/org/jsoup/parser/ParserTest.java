import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;

public class ParserTest {

    @Test
    public void testParseValidHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("<html></html>", doc.html());
    }

    @Test
    public void testParseMalformedHtml() {
        String html = "<html<title>Test</title>"; // missing '>' in title tag
        Document doc = Parser.parse(html, "http://example.com");
        // Check for expected failure due to malformed HTML
        // Can use assertThrows or try-catch with expected Exception
    }
}