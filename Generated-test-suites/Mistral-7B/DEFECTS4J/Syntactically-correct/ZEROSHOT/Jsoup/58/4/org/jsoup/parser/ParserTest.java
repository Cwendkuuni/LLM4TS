package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParserInstance() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertNotNull(parser);
        Assert.assertNotNull(parser.getTreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("Hello World", doc.body().child(0).text());
        Assert.assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testParseInvalidInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><script>alert('Hello World')</script>";
        String baseUri = "http://example.com";

        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();

        Assert.assertNotNull(doc);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error at line 3 column 11: Unexpected <script> tag.", errors.get(0).getMessage());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        Assert.assertSame(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetMaxErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = new ArrayList<>();
        parser.setTrackErrors(1);
        parser.setMaxErrors(2);

        String html = "<html><head><title>Test</title></head><body><script>alert('Hello World')</script>";
        String baseUri = "http://example.com";

        Document doc = parser.parseInput(html, baseUri);
        errors = parser.getErrors();

        Assert.assertNotNull(doc);
        Assert.assertEquals(2, errors.size());
        Assert.assertTrue(errors.get(1).getLine() > errors.get(0).getLine());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><head><title>Test</title></head><body><script>alert('Hello World')</script>";
        String baseUri = "http://example.com";

        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();

        Assert.assertEquals("Error at line 3 column 11: Unexpected <script> tag.", errors.get(0).getMessage());
        Assert.assertEquals(1, errors.size());
        Assert.assertNull(doc.title());
    }

    @Test
    public void testParseSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        settings.setTrimWhitespace(true);
        parser.settings(settings);

        String html = "<html><body><p>   Hello World   </p></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        Assert.assertEquals("Hello World", doc.body().text().trim());
    }

    @Test
    public void testStaticParseInput() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello World</h1>";
        Element context = new Element("body");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("Hello World", nodes.get(0).text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><tag>Hello World</tag></xml>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertInstanceOf(nodes.get(0), Element.class);
        Assert.assertEquals("Hello World", nodes.get(0).child(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.toString());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;/head&gt;&lt;body&gt;&lt;h1&gt;Hello &amp; World&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
        String unescaped = Parser.unescapeEntities(html, false);
        String expected = "<html><head><title>Test</title></head><body><h1>Hello & World</h1></body></html>";

        Assert.assertEquals(expected, unescaped);
    }
}