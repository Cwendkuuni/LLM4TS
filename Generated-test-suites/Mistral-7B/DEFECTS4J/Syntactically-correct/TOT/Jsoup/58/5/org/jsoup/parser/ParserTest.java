import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.xml.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    @Test
    public void testParseHtmlWithBaseUri() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertEquals("Hello World", doc.body().text());
        Assert.assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testParseHtmlWithTrackErrors() {
        String html = "<html><head></head><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, null);
        Assert.assertEquals("Hello World", doc.body().text());
        Assert.assertEquals(1, parser.getErrors().size());
        Assert.assertEquals("Malformed HTML: non-processing instruction at line 3 column 2: 'script' is not allowed in prolog.", parser.getErrors().get(0).getMessage());
    }

    @Test
    public void testParseXmlFragment() {
        String xml = "<root><child><subChild/></child></root>";
        Parser parser = new Parser(new XmlTreeBuilder());
        List<Node> nodes = parser.parseFragment(xml, null, null);
        Element child = (Element) nodes.get(0);
        Element subChild = (Element) child.children().get(0);
        Assert.assertEquals("subChild", subChild.nodeName());
        Assert.assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body><script>alert('XSS')</script><h1>Hello World</h1>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        Document doc = parser.parseInput(html, null);
        Assert.assertEquals(5, parser.getErrors().size());
        Assert.assertEquals("Malformed HTML: scripting not allowed at line 2 column 1: 'script' is not allowed in prolog.", parser.getErrors().get(0).getMessage());
    }

    @Test
    public void testParseHtmlWithCustomTreeBuilder() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        TreeBuilder customTreeBuilder = new HtmlTreeBuilder() {
            @Override
            public Element newElement(String name, AttributeSet attrs) {
                Element element = super.newElement(name, attrs);
                element.addClass("custom-class");
                return element;
            }
        };
        Parser parser = new Parser(customTreeBuilder);
        Document doc = parser.parseInput(html, null);
        Assert.assertEquals("Hello World", doc.body().text());
        Assert.assertEquals(0, parser.getErrors().size());
        Assert.assertTrue(doc.body().hasClass("custom-class"));
    }

    @Test
    public void testParseXmlFragmentWithErrorTracking() {
        String xml = "<root><child><subChild/><script>alert('XSS')</script></child></root>";
        Parser parser = new Parser(new XmlTreeBuilder());
        parser.setTrackErrors(1);
        List<Node> nodes = parser.parseFragment(xml, null, null);
        Assert.assertEquals(1, parser.getErrors().size());
        Assert.assertEquals("Malformed XML: scripting not allowed at line 2 column 1: 'script' is not allowed in element content.", parser.getErrors().get(0).getMessage());
    }

    @Test
    public void testParseInvalidXml() {
        String xml = "<root><child><subChild/><script>alert('XSS')</script></child></root>";
        Parser parser = new Parser(new XmlTreeBuilder());
        parser.setTrackErrors(5);
        List<Node> nodes = parser.parseFragment(xml, null, null);
        Assert.assertEquals(5, parser.getErrors().size());
        Assert.assertEquals("Malformed XML: scripting not allowed at line 2 column 1: 'script' is not allowed in element content.", parser.getErrors().get(0).getMessage());
    }

    @Test
    public void testParseHtmlWithEmptyInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", null);
        Assert.assertNull(doc);
        Assert.assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testParseXmlWithEmptyInput() {
        Parser parser = new Parser(new XmlTreeBuilder());
        Document doc = parser.parseInput("", null);
        Assert.assertNull(doc);
        Assert.assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testParseHtmlWithCustomSettings() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        ParseSettings settings = new ParseSettings();
        settings.setTrimWhitespace(false);
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.settings(settings);
        Document doc = parser.parseInput(html, null);
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("Hello World\n", doc.body().text());
        Assert.assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testParseXmlWithCustomSettings() {
        String xml = "<root><child><subChild/></child></root>";
        ParseSettings settings = new ParseSettings();
        settings.setTrimWhitespace(false);
        Parser parser = new Parser(new XmlTreeBuilder());
        parser.settings(settings);
        List<Node> nodes = parser.parseFragment(xml, null, null);
        Element child = (Element) nodes.get(0);
        Assert.assertEquals("\nchild\n", child.outerHtml());
        Assert.assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testParseHtmlWithExternalResources() {
        String html = "<html><head><link rel=\"stylesheet\" href=\"styles.css\"/><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertEquals("Hello World", doc.body().text());
        Assert.assertEquals(0, parser.getErrors().size());
        Assert.assertNotNull(doc.select("head > link").first());
        Assert.assertEquals("styles.css", doc.select("head > link").first().attr("href"));
    }

    @Test
    public void testParseInvalidHtmlWithExternalResources() {
        String html = "<html><head><link rel=\"stylesheet\" href=\"styles.css\"><body><h1>Hello World<script>alert('XSS')</script></h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertEquals(5, parser.getErrors().size());
        Assert.assertEquals("Malformed HTML: scripting not allowed at line 4 column 1: 'script' is not allowed in script or style element.", parser.getErrors().get(0).getMessage());
    }
}