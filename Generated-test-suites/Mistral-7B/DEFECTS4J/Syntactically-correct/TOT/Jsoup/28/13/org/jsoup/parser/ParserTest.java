package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    /**
     * Test parseInput method with valid HTML.
     */
    @Test
    public void testParseValidHtml() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Document doc = parser.parseInput(html, "http://example.com");

        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("Hello World", doc.body().text());
    }

    /**
     * Test parseInput method with empty HTML.
     */
    @Test
    public void testParseEmptyHtml() {
        String html = "";
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Document doc = parser.parseInput(html, "http://example.com");

        Assert.assertNotNull(doc);
        Assert.assertFalse(doc.body().hasText());
    }

    /**
     * Test parseInput method with invalid HTML.
     */
    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body><script>alert('Hello')</script>";
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        List<ParseError> errors = parser.parseInput(html, "http://example.com").errors();

        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Expecting: tag name", errors.get(0).message());
    }

    /**
     * Test parseInput method with maximum errors reached.
     */
    @Test(expected = RuntimeException.class)
    public void testParseMaxErrors() {
        String html = "<html><body><script>>alert('Hello')</script><script>>alert('Hello')</script>";
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder).setTrackErrors(2);
        List<ParseError> errors = parser.parseInput(html, "http://example.com").errors();

        Assert.assertEquals(2, errors.size());
        parser.parseInput(html, "http://example.com"); // this should throw
    }

    /**
     * Test parseInput method with null HTML.
     */
    @Test(expected = NullPointerException.class)
    public void testParseNullHtml() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        parser.parseInput(null, "http://example.com");
    }

    /**
     * Test getTreeBuilder method.
     */
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    /**
     * Test setTreeBuilder method.
     */
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder oldTreeBuilder = new HtmlTreeBuilder();
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(oldTreeBuilder);
        parser.setTreeBuilder(newTreeBuilder);
        Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    /**
     * Test isTrackErrors method.
     */
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());

        parser = new Parser(new HtmlTreeBuilder()).setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    /**
     * Test setTrackErrors method.
     */
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    /**
     * Test getErrors method.
     */
    @Test
    public void testGetErrors() {
        String html = "<html><body><script>>alert('Hello')</script><script>>alert('Hello')</script>";
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder).setTrackErrors(2);
        List<ParseError> errors = parser.parseInput(html, "http://example.com").errors();

        Assert.assertEquals(2, errors.size());
        List<ParseError> getErrors = parser.getErrors();
        Assert.assertEquals(2, getErrors.size());
        Assert.assertEquals(errors.get(0), getErrors.get(0));
        Assert.assertEquals(errors.get(1), getErrors.get(1));
    }

    /**
     * Test parse static method.
     */
    @Test
    public void testParseStatic() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("Hello World", doc.body().text());
    }

    /**
     * Test parseFragment static method.
     */
    @Test
    public void testParseFragmentStatic() {
        String fragmentHtml = "<p>Hello World</p>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        Document doc = Parser.parseFragment(fragmentHtml, context, "http://example.com");

        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.body().text());
    }

    /**
     * Test parseBodyFragment static method.
     */
    @Test
    public void testParseBodyFragmentStatic() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Assert.assertNotNull(doc);
        Assert.assertFalse(doc.head().text().isEmpty());
        Assert.assertEquals("Hello World", doc.body().text());
    }

    /**
     * Test parseBodyFragmentRelaxed static method.
     */
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        Assert.assertNotNull(doc);
        Assert.assertFalse(doc.head().text().isEmpty());
        Assert.assertEquals("Hello World", doc.body().text());
    }
}