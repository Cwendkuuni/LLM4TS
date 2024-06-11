import org.junit.Test;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class ParserTest {
    // Test Parser constructor
    @Test
    public void testParserConstructor() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNotNull(parser);
    }

    // Test parseInput method with normal HTML input
    @Test
    public void testParseInputNormalHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = parser.parseInput(html, "");
        assertEquals("Hello World", doc.body().text());
    }

    // Test parseInput method with empty HTML input
    @Test
    public void testParseInputEmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "";
        Document doc = parser.parseInput(html, "");
        assertNull(doc);
    }

    // Test parseInput method with baseUri
    @Test
    public void testParseInputWithBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<a href='test.html'>Test</a>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        assertEquals("Test", doc.select("a[href='test.html']").text());
    }

    // Test getTreeBuilder method
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(treeBuilder, parser.treeBuilder);
    }

    // Test setTreeBuilder method
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertSame(newTreeBuilder, parser.treeBuilder);
    }

    // Test isTrackErrors method
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    // Test setTrackErrors method
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        assertEquals(1, parser.maxErrors);
    }

    // Test getErrors method
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseError error = new ParseError("Test Error", 1, 1, "Test Message");
        parser.errors.add(error);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Test Error", errors.get(0).getMessage());
    }

    // Test settings method
    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        assertNotNull(settings);
    }

    // Test settings method with custom settings
    @Test
    public void testSettingsWithCustomSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings customSettings = new ParseSettings();
        parser.settings(customSettings);
        assertSame(customSettings, parser.settings);
    }

    // Test parse method
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "");
        assertEquals("Hello World", doc.body().text());
    }

    // Test parseFragment method with normal HTML input
    @Test
    public void testParseFragmentNormalHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String fragmentHtml = "<p>Hello World</p>";
        Element context = new Element("div");
        List<Node> nodes = parser.parseFragment(fragmentHtml, context, "");
        assertEquals(1, nodes.size());
        assertEquals("Hello World", nodes.get(0).text());
    }

    // Test parseFragment method with empty HTML input
    @Test
    public void testParseFragmentEmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String fragmentHtml = "";
        Element context = new Element("div");
        List<Node> nodes = parser.parseFragment(fragmentHtml, context, "");
        assertEquals(0, nodes.size());
    }

    // Test parseFragment method with baseUri
    @Test
    public void testParseFragmentWithBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String fragmentHtml = "<a href='test.html'>Test</a>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodes.size());
        assertEquals("Test", nodes.get(0).select("a[href='test.html']").text());
    }

    // Test parseXmlFragment method with normal XML input
    @Test
    public void testParseXmlFragmentNormalXml() {
        Parser parser = new Parser(new XmlTreeBuilder());
        String fragmentXml = "<tag><subtag>Hello World</subtag></tag>";
        List<Node> nodes = parser.parseXmlFragment(fragmentXml, "");
        assertEquals(1, nodes.size());
        assertEquals("Hello World", nodes.get(0).text());
    }

    // Test parseBodyFragment method with normal HTML input
    @Test
    public void testParseBodyFragmentNormalHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String bodyHtml = "<p>Hello World</p>";
        Document doc = parser.parseBodyFragment(bodyHtml, "");
        assertEquals("Hello World", doc.body().text());
    }

    // Test unescapeEntities method with normal HTML input
    @Test
    public void testUnescapeEntitiesNormalHtml() {
        String string = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
        String unescapedString = Parser.unescapeEntities(string, false);
        assertEquals("<p>Hello & World</p>", unescapedString);
    }

    // Test unescapeEntities method with strict HTML input (attributes)
    @Test
    public void testUnescapeEntitiesStrictHtml() {
        String string = "<p class='test &other'>Hello & World</p>";
        String unescapedString = Parser.unescapeEntities(string, true);
        assertEquals("<p class='test other'>Hello & World</p>", unescapedString);
    }
}