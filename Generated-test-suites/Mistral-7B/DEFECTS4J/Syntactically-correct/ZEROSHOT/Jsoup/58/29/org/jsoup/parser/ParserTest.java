package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TreeBuilder;
import org.jsoup.parse.ParseErrorList;
import org.jsoup.parse.ParseSettings;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test class for the Parser class.
 */
public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, "http://test.com");
        Assert.assertEquals("Test Title", document.title());
        Assert.assertEquals("Test Heading", document.select("h1").text());
        Assert.assertNull(parser.getErrors());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><script>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document document = parser.parseInput(html, "http://test.com");
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected char: 0x003C [<] at line 4 column 1 path = http://test.com", errors.get(0).getMessage());
    }

    @Test
    public void testParseInputWithMaxErrors() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><script><script>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(2);
        Document document = parser.parseInput(html, "http://test.com");
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(2, errors.size());
        Assert.assertEquals("Unexpected char: 0x003C [<] at line 4 column 1 path = http://test.com", errors.get(0).getMessage());
        Assert.assertEquals("Unexpected char: 0x003C [<] at line 5 column 1 path = http://test.com", errors.get(1).getMessage());
    }

    @Test
    public void testParseInputWithoutBaseUri() {
        String html = "<img src=\"image.png\">";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, null);
        Element img = document.select("img").first();
        Assert.assertNull(img.attr("src"));
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src=\"image.png\">";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, "http://test.com/index.html");
        Element img = document.select("img").first();
        Assert.assertEquals("image.png", img.attr("src"));
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertEquals(XmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Assert.assertEquals(1, parser.getMaxErrors());
    }

    @Test
    public void testGetErrors() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><script>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document document = parser.parseInput(html, "http://test.com");
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected char: 0x003C [<] at line 4 column 1 path = http://test.com", errors.get(0).getMessage());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        Assert.assertNotNull(settings);
    }

    @Test
    public void testParseStaticFunction() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Document document = Parser.parse(html, "http://test.com");
        Assert.assertEquals("Test Title", document.title());
        Assert.assertEquals("Test Heading", document.select("h1").text());
        Assert.assertNull(Parser.getErrors());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Test Heading</h1>";
        Element context = new Element("div");
        Document document = Parser.parseFragment(fragmentHtml, context, "http://test.com");
        Assert.assertEquals("Test Heading", context.text());
        Assert.assertNotNull(context.child(0));
        Assert.assertEquals("h1", context.child(0).tagName());
        Assert.assertNull(Parser.getErrors());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><tag>Test</tag></xml>";
        Document document = Parser.parseXmlFragment(fragmentXml, "http://test.com");
        Element tag = document.select("tag").first();
        Assert.assertEquals("Test", tag.text());
        Assert.assertNull(Parser.getErrors());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Test Heading</h1></body>";
        Document document = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        Assert.assertEquals("Test Heading", document.select("h1").text());
        Assert.assertNull(Parser.getErrors());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&amp; &lt; &gt; &quot; &apos; &nbsp; &eacute;";
        String unescapedString = Parser.unescapeEntities(string, false);
        Assert.assertEquals("& amp; &lt; &gt; &quot; &apos; &nbsp; é", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body><h1>Test Heading</h1></body>";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");
        Assert.assertEquals("Test Heading", document.select("h1").text());
        Assert.assertNull(Parser.getErrors());
    }
}