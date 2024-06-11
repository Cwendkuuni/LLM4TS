package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.ParseErrorList;
import org.jsoup.nodes.TreeBuilder;
import org.jsoup.settings.ParseSettings;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    @Test
    public void testParserInstance() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Assert.assertNotNull(parser);
    }

    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://localhost/";
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("Hello World", doc.body().select("h1").text());

        ParseErrorList errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseInputWithErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        String html = "<html><head><title>Test</title></head><body><h1>Hello<script>World</script></h1></body></html>";
        String baseUri = "http://localhost/";
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("Hello", doc.body().select("h1").text());

        ParseErrorList errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals(ParseError.BAD_CHAR.toString(), error.toString());
        Assert.assertEquals(13, error.line());
        Assert.assertEquals(13, error.column());
    }

    @Test
    public void testParseInputWithMaxErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        String html = "<html><head><title>Test</title></head><body><h1>Hello<script>World</script></h1><script>JavaScript</script></body></html>";
        String baseUri = "http://localhost/";
        parser.setTrackErrors(2);
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertNotNull(doc);

        ParseErrorList errors = parser.getErrors();
        Assert.assertEquals(2, errors.size());
        ParseError error1 = errors.get(0);
        Assert.assertEquals(ParseError.BAD_CHAR.toString(), error1.toString());
        Assert.assertEquals(13, error1.line());
        Assert.assertEquals(13, error1.column());
        ParseError error2 = errors.get(1);
        Assert.assertEquals(ParseError.BAD_CHAR.toString(), error2.toString());
        Assert.assertEquals(28, error2.line());
        Assert.assertEquals(7, error2.column());
    }

    @Test
    public void testParseInputWithNoTrackingError() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        String html = "<html><head><title>Test</title></head><body><h1>Hello<script>World</script></h1></body></html>";
        String baseUri = "http://localhost/";
        parser.setTrackErrors(0);
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("Hello", doc.body().select("h1").text());

        ParseErrorList errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        TreeBuilder expectedTreeBuilder = treeBuilder;
        TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
        Assert.assertEquals(expectedTreeBuilder, actualTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder1 = new HtmlTreeBuilder();
        TreeBuilder treeBuilder2 = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder1);
        parser.setTreeBuilder(treeBuilder2);
        TreeBuilder expectedTreeBuilder = treeBuilder2;
        TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
        Assert.assertEquals(expectedTreeBuilder, actualTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Assert.assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        parser.setTrackErrors(0);
        int expectedMaxErrors = 0;
        int actualMaxErrors = parser.getMaxErrors();
        Assert.assertEquals(expectedMaxErrors, actualMaxErrors);

        parser.setTrackErrors(10);
        expectedMaxErrors = 10;
        actualMaxErrors = parser.getMaxErrors();
        Assert.assertEquals(expectedMaxErrors, actualMaxErrors);
    }

    @Test
    public void testGetErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        String html = "<html><head><title>Test</title></head><body><h1>Hello<script>World</script></h1></body></html>";
        String baseUri = "http://localhost/";

        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertNotNull(doc);

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals(ParseError.BAD_CHAR.toString(), error.toString());
        Assert.assertEquals(13, error.line());
        Assert.assertEquals(13, error.column());
    }

    @Test
    public void testSettings() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        ParseSettings expectedSettings = treeBuilder.defaultSettings();
        ParseSettings actualSettings = parser.settings();
        Assert.assertEquals(expectedSettings, actualSettings);

        Parser parserWithCustomSettings = new Parser(treeBuilder).settings(new ParseSettings.Builder().build());
        ParseSettings actualCustomSettings = parserWithCustomSettings.settings();
        Assert.assertNotEquals(expectedSettings, actualCustomSettings);
    }

    @Test
    public void testStaticParseFunction() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://localhost/";

        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("Hello World", doc.body().select("h1").text());

        ParseErrorList errors = Parser.htmlParser().getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseFragment() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        String fragmentHtml = "<h1>Hello World</h1>";
        Element context = new Element("div");
        String baseUri = "http://localhost/";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        Assert.assertEquals("h1", node.tagName());
        Assert.assertEquals("Hello World", node.text());

        ParseErrorList errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<book><title>The Catcher in the Rye</title></book>";
        String baseUri = "http://localhost/";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        Assert.assertEquals("book", node.tagName());
        Assert.assertEquals("The Catcher in the Rye", node.getElementsByTag("title").get(0).text());

        ParseErrorList errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://localhost/";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.body());
        Assert.assertEquals("Hello World", doc.body().select("h1").text());

        ParseErrorList errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
        String unescapedHtml = Parser.unescapeEntities(html, false);
        Assert.assertEquals("<p>Hello & World</p>", unescapedHtml);

        String attributeHtml = "<p id='&amp;id'></p>";
        String unescapedAttributeHtml = Parser.unescapeEntities(attributeHtml, true);
        Assert.assertEquals("<p id='&id'></p>", unescapedAttributeHtml);
    }

}