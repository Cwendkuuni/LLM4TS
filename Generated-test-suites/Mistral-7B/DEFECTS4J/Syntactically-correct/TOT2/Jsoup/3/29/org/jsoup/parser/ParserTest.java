import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParseValidHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertNotNull(doc);
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.getElementsByTag("h1").first().text());
    }

    @Test
    public void testParseEmptyHtml() {
        Document doc = Parser.parse("", "http://example.com");
        assertNotNull(doc);
        assertTrue(doc.body().isEmpty());
        assertTrue(doc.head().isEmpty());
    }

    @Test
    public void testParseMalformedHtml() {
        String html = "<html><head><title>Test</title></body></html>"; // body tag before head tag
        try {
            Document doc = Parser.parse(html, "http://example.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}