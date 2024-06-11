import org.junit.Test;
import org.jsoup.parser.Parser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        assertEquals("Test", doc.title().text());
        assertNotNull(doc.body());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    // Add test cases for parseBodyFragment(), parse(), parseComment(), parseCdata(), parseXmlDecl(), parseEndTag(), parseStartTag(), parseAttribute(), parseTextNode()

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        String html = null;
        Parser parser = new Parser(html, "http://example.com");
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtml() {
        String html = "";
        Parser parser = new Parser(html, "http://example.com");
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, null);
        parser.parse();
    }

    // Add test cases for other exceptions and edge cases
}