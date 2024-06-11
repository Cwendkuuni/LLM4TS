package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private Document doc;
 private TreeBuilder treeBuilder;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = new ArrayList<>();

 doc = parser.parseInput(html, baseUri);
 assertNotNull(doc);
 assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
 assertNull(parser.getErrors());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputEmptyHtml() {
 parser.parseInput("", "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput("<html></html>", null);
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><script>alert('Hello')</script></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = new ArrayList<>();
 parseWithErrors(parser, html, baseUri, errors);
 assertEquals(1, errors.size());
 assertEquals("Unexpected char 63 found, expected '>'", errors.get(0).getMessage());
 }

 @Test
 public void testParseInputWithMaxErrors() {
 String html = "<html><body><h1>Hello</h1><script>alert('World')</script></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = new ArrayList<>();
 Parser parserWithMaxErrors = parser.setTrackErrors(2);
 doc = parserWithMaxErrors.parseInput(html, baseUri);
 assertEquals("<html><body><h1>Hello</h1></body></html>", doc.html());
 assertEquals(2, errors.size());
 }

 @Test
 public void testParseInputWithNoTrackErrors() {
 String html = "<html><body><h1>Hello</h1><script>alert('World')</script></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = new ArrayList<>();
 Parser parserWithNoTrackErrors = parser.setTrackErrors(0);
 doc = parserWithNoTrackErrors.parseInput(html, baseUri);
 assertEquals("<html><body><h1>Hello</h1></body></html>", doc.html());
 assertNull(parserWithNoTrackErrors.getErrors());
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 assertTrue(parser.isTrackErrors());
 Parser parserWithNoTrackErrors = parser.setTrackErrors(0);
 assertFalse(parserWithNoTrackErrors.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(5);
 assertEquals(5, parser.getMaxErrors());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> errors = new ArrayList<>();
 Parser parserWithErrors = parser.setTrackErrors(2);
 errors = parserWithErrors.getErrors();
 assertNotNull(errors);
 assertNull(parserWithErrors.getErrors());
 assertEquals(0, errors.size());
 }

 @Test
 public void testSettings() {
 ParseSettings settings = parser.settings();
 assertNotNull(settings);
 assertEquals(settings, parser.settings());
 }

 @Test
 public void testParse() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 Document doc = Parser.parse(html, baseUri);
 assertNotNull(doc);
 assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
 assertNull(Parser.getErrors());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<ul><li>Item 1</li><li>Item 2</li></ul>";
 Element context = new Element("div");
 String baseUri = "http://example.com";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
 assertNotNull(nodes);
 assertEquals(2, nodes.size());
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<root><child>1</child><child>2</child></root>";
 String baseUri = "http://example.com";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
 assertNotNull(nodes);
 assertEquals(2, nodes.size());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<ul><li>Item 1</li><li>Item 2</li></ul>";
 String baseUri = "http://example.com";
 Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
 assertNotNull(doc);
 assertEquals("<html><body><ul><li>Item 1</li><li>Item 2</li></ul></body></html>", doc.html());
 }

 @Test
 public void testUnescapeEntities() {
 String string = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
 String unescapedString = Parser.unescapeEntities(string, false);
 assertEquals("<p>Hello & World</p>", unescapedString);
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<ul><li>Item 1</li><li>Item 2</li></ul>";
 String baseUri = "http://example.com";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
 assertNotNull(doc);
 assertEquals("<html><body><ul><li>Item 1</li><li>Item 2</li></ul></body></html>", doc.html());
 }

 private void parseWithErrors(Parser parser, String html, String baseUri, List<ParseError> errors) {
 doc = parser.parseInput(html, baseUri);
 errors = parser.getErrors();
 }
}