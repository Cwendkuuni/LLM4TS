package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.Xml;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput_SimpleHtml() {
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 Document doc = parser.parseInput(html, null);
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseInput_NoBaseUri() {
 String html = "<img src='/image.png'>";
 try {
 parser.parseInput(html, null);
 fail("Expected ParseException to be thrown due to missing baseUri.");
 } catch (Exception e) {
 assertTrue(e instanceof org.jsoup.ParseException);
 }
 }

 @Test
 public void testParseInput_Xml() {
 String xml = "<xml><element>Test</element></xml>";
 Document doc = parser.parseInput(xml, "xml");;
 Element element = (Element) doc.body();
 assertEquals("Test", element.text());
 }

 @Test
 public void testParseInput_ParserErrorTracking() {
 String html = "<script><script"; // Malformed HTML
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, null);
 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Error parsing start tag : script", error.getMessage());
 }

 @Test
 public void testParseInput_ParseErrorsLimit() {
 String html = "<script><script"; // Malformed HTML, repeated multiple times for error tracking limit test
 parser.setTrackErrors(3);
 Document doc = parser.parseInput(html, null);
 List<ParseError> errors = parser.getErrors();
 assertEquals(3, errors.size());
 }

 @Test
 public void testParseInput_ParseSettings() {
 ParseSettings settings = ParseSettings.defaultSettings().tagStartStrictly("script");
 Parser parserWithSettings = new Parser(new HtmlTreeBuilder());
 parserWithSettings.settings(settings);
 String html = "<script><script"; // Malformed HTML
 parserWithSettings.parseInput(html, null);
 List<ParseError> errors = parserWithSettings.getErrors();
 assertTrue(errors.isEmpty());
 }

 @Test
 public void testParseFragment_SimpleHtml() {
 String html = "<img src='image.png' alt='test'>";
 Element context = Element.parse("<div></div>");
 List<Node> nodes = Parser.parseFragment(html, context, null);
 assertEquals(1, nodes.size());
 Node node = nodes.get(0);
 assertEquals("img", node.tagName());
 }

 @Test
 public void testParseFragment_Xml() {
 String xml = "<element><subElement><subElement2>Test</subElement2></subElement></element>";
 List<Node> nodes = Parser.parseXmlFragment(xml, null);
 assertEquals(3, nodes.size());
 Node root = nodes.get(0);
 assertEquals("element", root.tagName());
 }

 @Test
 public void testParseBodyFragment() {
 String html = "<body><p>Hello World</p></body>";
 Document doc = Parser.parseBodyFragment(html, null);
 assertEquals("Hello World", doc.body().text());
 }
}