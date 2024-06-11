import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test Parser</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.body();
        assertEquals("Hello World", root.getElementsByTag("h1").text());
        assertNotNull(doc.head().getElementsByTag("title"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Fragment Test</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        Element root = doc.body();
        assertEquals("Fragment Test", root.getElementsByTag("h1").text());
        assertNull(doc.head());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        String html = null;
        try {
            Parser.parse(html, "http://test.com");
        } catch (IllegalArgumentException e) {
            assertEquals("HTML is null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        String html = "<html><body></body></html>";
        try {
            Parser.parse(html, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Base URI is null", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals(0, doc.body().childNodes().size());
        assertEquals(0, doc.head().childNodes().size());
    }

    @Test
    public void testParseComments() {
        String html = "<html><!-- Test comment --><head><title>Test Parser</title></head><body><!-- Test comment --><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.body();
        assertEquals("Hello World", root.getElementsByTag("h1").text());
        assertEquals(2, doc.body().childNodes().size());
        assertEquals("Test comment", doc.body().getElementsByClass("comment").text());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidTagName() {
        String html = "<html><invalidtag></invalidtag></html>";
        try {
            Parser.parse(html, "http://test.com");
        } catch (RuntimeException e) {
            assertEquals("Invalid tag name: invalidtag", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParseXmlDeclaration() {
        String html = "<html xmlns:myxml=\"http://test.com/myxml\">";
        Document doc = Parser.parse(html, "http://test.com");
        XmlDeclaration decl = doc.head().getElementsByTag("xml").get(0);
        assertEquals("http://test.com/myxml", decl.xmlns("myxml"));
    }

    @Test
    public void testParseTextNodes() {
        String html = "<html><body><p>Test Text Nodes</p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.body();
        TextNode textNode = root.getElementsByTag("p").get(0).childNodes().get(0);
        assertEquals("Test Text Nodes", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[Test Cdata]]></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.body();
        TextNode textNode = root.childNodes().get(0);
        assertEquals("Test Cdata", textNode.text());
    }
}