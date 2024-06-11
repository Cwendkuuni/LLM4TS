import org.junit.Test;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParse() {
        // Given a valid HTML string and base URI
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        // When parsing the HTML string using the Parser class
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        // Then the parsed document should have the expected structure
        assertEquals("html", doc.tagName());
        Element htmlNode = doc.body();
        assertEquals("body", htmlNode.tagName());
        Element titleNode = doc.head().title();
        assertEquals("h1", htmlNode.child(0).tagName());
        assertEquals("Hello World", htmlNode.child(0).text());
        assertEquals("Test", titleNode.text());

        // Test parsing of attributes in start tag
        String htmlWithAttribute = "<div id='test' class='test'><p>Attribute Test</p></div>";
        Parser parserWithAttribute = new Parser(htmlWithAttribute, baseUri, false);
        Document docWithAttribute = parserWithAttribute.parse();
        Element div = docWithAttribute.body().child(0);
        assertEquals("div", div.tagName());
        assertEquals("test", div.attr("id"));
        assertEquals("test", div.attr("class"));
    }

    @Test
    public void testParseBodyFragment() {
        // Given a valid HTML fragment and base URI
        String bodyHtml = "<body><p>Fragment Test</p></body>";
        String baseUri = "http://example.com";

        // When parsing the HTML fragment using the Parser class
        Parser parser = new Parser(bodyHtml, baseUri, true);
        Document doc = parser.parse();

        // Then the parsed document should have the expected structure
        assertEquals("html", doc.tagName());
        Element htmlNode = doc.body();
        assertEquals("body", htmlNode.tagName());
        Element p = htmlNode.child(0);
        assertEquals("p", p.tagName());
        assertEquals("Fragment Test", p.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        // Given an invalid HTML string and base URI
        String invalidHtml = "<html><body<p>Invalid HTML</p>";
        String baseUri = "http://example.com";

        // When trying to parse the invalid HTML string using the Parser class
        Parser parser = new Parser(invalidHtml, baseUri, false);
        // Then an IllegalArgumentException should be thrown
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtml() {
        // Given an empty HTML string and base URI
        String emptyHtml = "";
        String baseUri = "http://example.com";

        // When trying to parse the empty HTML string using the Parser class
        Parser parser = new Parser(emptyHtml, baseUri, false);
        // Then an IllegalArgumentException should be thrown
        parser.parse();
    }
}