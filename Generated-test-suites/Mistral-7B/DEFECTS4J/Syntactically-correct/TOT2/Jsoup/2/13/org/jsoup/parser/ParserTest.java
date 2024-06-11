import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseHtmlDocument() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Test Heading</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        assertEquals("<body><h1>Test Heading</h1></body>", doc.html());
        assertNull(doc.head());
    }

    @Test
    public void testParseEmptyDocument() {
        String html = "";
        Document doc = Parser.parse(html, "http://test.com");
        assertTrue(doc.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        Parser.parse(null, "http://test.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        Parser.parse("", null);
    }

    @Test
    public void testParseHtmlWithComments() {
        String html = "<html><!-- This is a comment --><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.body().text());
        assertTrue(doc.body().child(0) instanceof Comment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidStartTag() {
        String html = "<abc";
        Parser parser = new Parser(html, "http://test.com", false);
        parser.parse();
    }
}