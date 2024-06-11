import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.head();
        assertEquals(htmlTag, htmlNode.tag());

        Element titleNode = htmlNode.child(0);
        assertEquals(titleTag, titleNode.tag());
        assertEquals("Test Page", titleNode.text());

        Element bodyNode = doc.body();
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyNode = doc;
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());

        // Check empty Document
        Document emptyDoc = Parser.parse("", "http://example.com");
        assertTrue(emptyDoc.isEmpty());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div class='test'>Hello World</div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        Element divNode = doc.body();
        assertEquals(bodyTag, divNode.tag());

        Element testNode = divNode.child(0);
        assertEquals("test", testNode.classes().get(0));
        assertEquals("Hello World", testNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIllegalArgumentException() {
        Parser.parse("", null); // Base URI should not be null
    }

    @Test
    public void testParseInvalidHTML() {
        String invalidHtml = "<html><h1><script>alert('Hello World')</script></h1>";

        try {
            Parser.parse(invalidHtml, "http://example.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<div><p><img src='image.png'></p></div>";
        Document doc = Parser.parse(html, "http://example.com");

        Element divNode = doc.body();
        Element pNode = divNode.child(0);
        Element imgNode = pNode.child(0);

        assertEquals(divTag, divNode.tag());
        assertEquals(pTag, pNode.tag());
        assertEquals(imgTag, imgNode.tag());
    }

    @Test
    public void testParseDataNodes() {
        String html = "<html><body><h1>Hello &lt; World &gt;</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element h1Node = doc.body().child(0);
        TextNode textNode = (TextNode) h1Node.child(0);
        assertEquals("Hello < World >", textNode.text());
    }
}