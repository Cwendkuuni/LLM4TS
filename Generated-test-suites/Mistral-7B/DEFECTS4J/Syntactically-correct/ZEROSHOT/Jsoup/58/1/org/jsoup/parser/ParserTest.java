package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Document document;

 @Before
 public void setUp() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 @Test
 public void testParseInput() {
 String html = "<html><head><title>Test Parser</title></head><body><h1>Hello World</h1></body></html>";
 document = parser.parseInput(html, null);
 assertEquals("Test Parser", document.title().text());
 assertEquals("Hello World", document.body().select("h1").text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, null);
 }

 @Test
 public void testParseInputEmptyHtml() {
 parser.setTrackErrors(1);
 document = parser.parseInput("", null);
 assertEquals(1, parser.getErrors().size());
 assertNotNull(parser.getErrors().get(0).getLine());
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 assertEquals(parser.treeBuilder, treeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithMaxErrors() {
 parser.setTrackErrors(5);
 List<ParseError> errors = parser.getErrors();
 assertEquals(5, errors.size());
 }

 @Test
 public void testSetTrackErrorsWithZero() {
 parser.setTrackErrors(0);
 List<ParseError> errors = parser.getErrors();
 assertFalse(errors.isEmpty());
 }

 @Test
 public void testGetErrors() {
 parser.parseInput("<script></script>", null);
 List<ParseError> errors = parser.getErrors();
 assertFalse(errors.isEmpty());
 }

 @Test
 public void testSettings() {
 parser.settings(ParseSettings.defaultSettings().parserErrorHandler(new DefaultErrorHandler()));
 ParseSettings settings = parser.settings();
 assertNotNull(settings.getParserErrorHandler());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div><p>Body Test</p></div>";
 document = Parser.parseBodyFragment(bodyHtml, null);
 assertEquals("<html><head></head><body><div><p>Body Test</p></div></body></html>", document.html());
 }

 @Test
 public void testParseBodyFragmentWithBaseUri() {
 String bodyHtml = "<img src=\"image.jpg\" />";
 String baseUri = "http://example.com";
 document = Parser.parseBodyFragment(bodyHtml, baseUri);
 assertEquals("<html><head></head><body><img src=\"image.jpg\"></body></html>", document.html());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<ul><li>Fragment Test</li></ul>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, null);
 assertEquals(1, nodes.size());
 assertTrue(nodes.get(0) instanceof Element);
 assertEquals("Fragment Test", nodes.get(0).text());
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<xml><tag>XML Test</tag></xml>";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, null);
 assertEquals(1, nodes.size());
 assertTrue(nodes.get(0) instanceof Element);
 assertEquals("XML Test", nodes.get(0).text());
 }

 @Test
 public void testParseInputWithMaxErrors() {
 parser.setTrackErrors(5);
 String html = "<html><script></script>";
 document = parser.parseInput(html, null);
 List<ParseError> errors = parser.getErrors();
 assertEquals(5, errors.size());
 }
}