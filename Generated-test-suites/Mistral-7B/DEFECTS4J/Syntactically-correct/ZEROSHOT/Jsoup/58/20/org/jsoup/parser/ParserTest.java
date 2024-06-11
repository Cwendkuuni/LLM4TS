package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.tree.ParseErrorList;
import org.jsoup.parser.tree.TreeBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @BeforeEach
 void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 Document document = parser.parseInput(html, baseUri);
 assertNotNull(document);
 assertEquals("Hello World", document.select("h1").text());
 }

 @Test
 void testParseInputWithTrackErrors() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(1);
 List<ParseError> errors = parser.parseInput(html, baseUri).errors();
 assertNotNull(errors);
 assertEquals(1, errors.size());
 Assertions.assertEquals("No element name", errors.get(0).getMessage());
 }

 @Test
 void testParseInputWithMaxErrorsReached() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(1);
 List<Node> nodes = parser.parseFragment("<script>throw new Error('Test error')</script>", null, baseUri).toList();
 List<ParseError> errors = parser.parseInput(html, baseUri).errors();
 assertNotNull(errors);
 assertEquals(2, errors.size());
 }

 @Test
 void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = treeBuilder;
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 Parser parserWithNewTreeBuilder = parser.setTreeBuilder(newTreeBuilder);
 TreeBuilder actualTreeBuilder = parserWithNewTreeBuilder.getTreeBuilder();
 assertEquals(newTreeBuilder, actualTreeBuilder);
 }

 @Test
 void testIsTrackErrors() {
 boolean expectedTrackErrors = true;
 Parser parserWithTrackErrors = parser.setTrackErrors(5);
 boolean actualTrackErrors = parserWithTrackErrors.isTrackErrors();
 assertEquals(expectedTrackErrors, actualTrackErrors);
 }

 @Test
 void testSetTrackErrorsWithZero() {
 Parser parserWithoutTrackErrors = parser.setTrackErrors(0);
 boolean actualTrackErrors = parserWithoutTrackErrors.isTrackErrors();
 assertFalse(actualTrackErrors);
 }

 @Test
 void testGetErrors() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(1);
 List<ParseError> expectedErrors = List.of(new ParseError("No element name", 1, 11, null));
 List<ParseError> actualErrors = parser.parseInput(html, baseUri).errors();
 assertEquals(expectedErrors, actualErrors);
 }

 @Test
 void testSettings() {
 ParseSettings expectedSettings = treeBuilder.defaultSettings();
 ParseSettings actualSettings = parser.settings();
 assertEquals(expectedSettings, actualSettings);
 }

 @Test
 void testParse() {
 Document document = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 assertNotNull(document);
 assertEquals("Hello World", document.select("h1").text());
 }

 @Test
 void testParseFragment() {
 Element context = new Element("div");
 List<Node> nodes = Parser.parseFragment("<h1>Hello World</h1>", context, "http://example.com");
 assertNotNull(nodes);
 assertEquals(1, nodes.size());
 assertEquals("Hello World", nodes.get(0).text());
 }

 @Test
 void testParseXmlFragment() {
 String xml = "<xml><name>John Doe</name></xml>";
 List<Node> nodes = Parser.parseXmlFragment(xml, "http://example.com");
 assertNotNull(nodes);
 assertEquals(1, nodes.size());
 assertEquals("John Doe", nodes.get(0).text());
 }

 @Test
 void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Document document = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertNotNull(document);
 assertEquals("Hello World", document.select("h1").text());
 }

 @Test
 void testParseBodyFragmentWithNullBaseUri() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Document document = Parser.parseBodyFragment(bodyHtml, null);
 assertNotNull(document);
 assertNull(document.head());
 }

 @Test
 void testUnescapeEntities() {
 String html = "&lt;p&gt;<b>&amp;lt;a href=&quot;http://example.com&quot;&gt;Link&amp;gt;</b>&amp;gt;Text&amp;lt;/p&gt;";
 String unescapedHtml = Parser.unescapeEntities(html, false);
 assertEquals("<p><b><a href=\"http://example.com\">Link</a></b></p>", unescapedHtml);
 }

 @Test
 void testParseBodyFragmentRelaxedDeprecated() {
 Document document = Parser.parseBodyFragmentRelaxed("<body><h1>Hello World</h1></body>", "http://example.com");
 assertNotNull(document);
 assertEquals("Hello World", document.select("h1").text());
 }

 @Test
 void testHtmlParser() {
 Parser parser = Parser.htmlParser();
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
 }

 @Test
 void testXmlParser() {
 Parser parser = Parser.xmlParser();
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 assertEquals(XmlTreeBuilder.class, treeBuilder.getClass());
 }
}