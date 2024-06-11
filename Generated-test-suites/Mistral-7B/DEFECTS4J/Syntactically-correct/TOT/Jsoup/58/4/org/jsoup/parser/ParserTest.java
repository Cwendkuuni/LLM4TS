package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.TreeBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;
    private ParseSettings settings;

    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        settings = treeBuilder.defaultSettings();
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInput_ValidHtml_ReturnsDocument() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document document = parser.parseInput(html, null);
        assertNotNull(document);
        assertEquals("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", document.html());
    }

    @Test
    public void testParseInput_EmptyHtml_ReturnsDocument() {
        String html = "<html></html>";
        Document document = parser.parseInput(html, null);
        assertNotNull(document);
        assertEquals("<html></html>", document.html());
    }

    @Test
    public void testParseInput_InvalidHtml_ReturnsNullWithError() {
        String html = "<html><body<p>Hello World";
        assertNull(parser.parseInput(html, null));
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.line());
        assertEquals(1, error.column());
        assertEquals("Expecting: <tag>, but found: <body>" + System.lineSeparator() +
                "  (Position: line 1, column 13)", error.getMessage());
    }

    @Test
    public void testParseInput_TrackErrors_ReturnsDocumentWithErrors() {
        int maxErrors = 2;
        parser = new Parser(treeBuilder);
        parser.setTrackErrors(maxErrors);
        String html = "<html><body<p>Hello World";
        Document document = parser.parseInput(html, null);
        assertNotNull(document);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.line());
        assertEquals(1, error.column());
        assertEquals("Expecting: <tag>, but found: <body>" + System.lineSeparator() +
                "  (Position: line 1, column 13)", error.getMessage());
        assertSame(parser, document.parser());
        assertEquals(maxErrors - 1, parser.getErrors().size());
    }

    @Test
    public void testParseInput_TrackErrorsMaxReached_ReturnsNullWithError() {
        int maxErrors = 1;
        parser = new Parser(treeBuilder);
        parser.setTrackErrors(maxErrors);
        String validHtml = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String invalidHtml = "<html><body<p>Hello World";
        Document validDocument = parser.parseInput(validHtml, null);
        assertNotNull(validDocument);
        Document invalidDocument = parser.parseInput(invalidHtml, null);
        assertNull(invalidDocument);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.line());
        assertEquals(1, error.column());
        assertEquals("Expecting: <tag>, but found: <body>" + System.lineSeparator() +
                "  (Position: line 1, column 13)", error.getMessage());
        assertNull(parser.parseInput(validHtml, null));
        assertEquals(maxErrors, parser.getErrors().size());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(newTreeBuilder, treeBuilder);
    }

    @Test
    public void testIsTrackErrors_DefaultDisabled() {
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testIsTrackErrors_Enabled() {
        parser = new Parser(treeBuilder);
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors_Disabled() {
        parser = new Parser(treeBuilder);
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors_NoErrors() {
        List<ParseError> errors = parser.getErrors();
        assertEmpty(errors);
    }

    @Test
    public void testGetErrors_WithErrors() {
        int maxErrors = 2;
        parser = new Parser(treeBuilder);
        parser.setTrackErrors(maxErrors);
        String html = "<html><body<p>Hello World";
        parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.line());
        assertEquals(1, error.column());
        assertEquals("Expecting: <tag>, but found: <body>" + System.lineSeparator() +
                "  (Position: line 1, column 13)", error.getMessage());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = parser.settings();
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testSettings_Set() {
        ParseSettings newSettings = new ParseSettings();
        parser.settings(newSettings);
        ParseSettings settings = parser.settings();
        assertEquals(newSettings, settings);
    }

    @Test
    public void testParseStaticParameters_ValidHtml() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document document = Parser.parse(html, null);
        assertNotNull(document);
        assertEquals("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", document.html());
    }

    @Test
    public void testParseStaticParameters_InvalidHtml() {
        String html = "<html><body<p>Hello World";
        assertNull(Parser.parse(html, null));
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.line());
        assertEquals(1, error.column());
        assertEquals("Expecting: <tag>, but found: <body>" + System.lineSeparator() +
                "  (Position: line 1, column 13)", error.getMessage());
    }

    @Test
    public void testParseFragment_ValidHtml() {
        String fragmentHtml = "<p>Hello World</p>";
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("p", element.tagName());
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testParseFragment_InvalidHtml() {
        String fragmentHtml = "<body<p>Hello World";
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
        assertEmpty(nodes);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.line());
        assertEquals(1, error.column());
        assertEquals("Expecting: <tag>, but found: <body>" + System.lineSeparator() +
                "  (Position: line 1, column 13)", error.getMessage());
    }

    @Test
    public void testParseXmlFragment_ValidXml() {
        String fragmentXml = "<book xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"><title xsi:type=\"xs:string\">Test Title</title></book>";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("title", element.tagName());
        assertEquals("Test Title", element.text());
    }

    @Test
    public void testParseBodyFragment_ValidHtml() {
        String bodyHtml = "<p>Hello World</p>";
        Document document = Parser.parseBodyFragment(bodyHtml, null);
        assertNotNull(document);
        Element body = document.body();
        List<Node> nodes = body.childNodes();
        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("p", element.tagName());
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed_Deprecated_ValidHtml() {
        String bodyHtml = "<p>Hello World</p>";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
        assertNotNull(document);
        Element body = document.body();
        List<Node> nodes = body.childNodes();
        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("p", element.tagName());
        assertEquals("Hello World", element.text());
    }
}