package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser htmlParser;
 private Parser xmlParser;
 private TreeBuilder htmlTreeBuilder;
 private TreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 htmlParser = Parser.htmlParser();
 xmlParser = Parser.xmlParser();
 htmlTreeBuilder = new HtmlTreeBuilder();
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseInput_NormalCase() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document docHtml = htmlParser.parseInput(html, "http://example.com");
 Document docXml = xmlParser.parseInput(html, "http://example.com");

 assertEquals("Test Page", docHtml.title().text());
 assertEquals("Hello World", docHtml.body().children().get(0).text());
 assertEquals("<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>", docHtml.html());
 assertEquals("<html><body><h1>Hello World</h1></body>", docXml.html());
 }

 @Test
 public void testParseInput_ErrorTrackingDisabled() {
 String html = "<html><script><script>alert('Error')</script></html>";
 List<ParseError> errors = new ArrayList<>();
 Parser parser = new Parser(htmlTreeBuilder) {
 @Override
 ParseErrorList getErrors() {
 return errors;
 }
 };
 Document doc = parser.parseInput(html, "http://example.com");
 assertFalse("Expected no errors, but got " + errors.size() + " errors", !errors.isEmpty());
 for (ParseError error : errors) {
 assertEquals("Unexpected character '<' - was expecting 'E' (char 61)", error.getMessage());
 }
 }

 @Test
 public void testParseInput_ErrorTrackingEnabled() {
 String html = "<html><script><script>alert('Error')</script></html>";
 Parser parser = htmlParser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "http://example.com");
 assertFalse("Expected 1 error, but got " + parser.getErrors().size() + " errors", parser.getErrors().size() != 1);
 ParseError error = parser.getErrors().get(0);
 assertEquals("Unexpected character '<' - was expecting 'E' (char 61)", error.getMessage());
 }

 @Test
 public void testParseInput_BaseUri() {
 String html = "<img src=\"image.jpg\" alt=\"Image\">";
 Document doc = htmlParser.parseInput(html, "http://example.com");
 Element img = doc.select("img").first();
 assertEquals("image.jpg", img.attr("src"));
 img = doc.select("img[src='image.jpg']").first();
 assertEquals("Image", img.attr("alt"));
 }

 @Test
 public void testParseFragment() {
 String fragment = "<ul><li>Item 1</li><li>Item 2</li></ul>";
 List<Node> nodes = Parser.parseFragment(fragment, null, "http://example.com");
 assertEquals(2, nodes.size());
 Element ul = (Element) nodes.get(0);
 List<Element> listItems = ul.children();
 assertEquals("Item 1", listItems.get(0).text());
 assertEquals("Item 2", listItems.get(1).text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 List<Node> nodes = doc.body().children();
 assertEquals(2, nodes.size());
 Element p1 = (Element) nodes.get(0);
 assertEquals("Paragraph 1", p1.text());
 Element p2 = (Element) nodes.get(1);
 assertEquals("Paragraph 2", p2.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 assertEquals("<html><body><p>Paragraph 1</p><p>Paragraph 2</p></body></html>", doc.html());
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = htmlTreeBuilder;
 TreeBuilder actualTreeBuilder = htmlParser.getTreeBuilder();
 assertEquals(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 htmlParser.setTreeBuilder(newTreeBuilder);
 TreeBuilder actualTreeBuilder = htmlParser.getTreeBuilder();
 assertEquals(newTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 assertFalse("Default parse error tracking is disabled", htmlParser.isTrackErrors());
 assertTrue("Parse error tracking can be enabled", htmlParser.setTrackErrors(1).isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> errors = new ArrayList<>();
 Parser parser = new Parser(htmlTreeBuilder) {
 @Override
 ParseErrorList getErrors() {
 return errors;
 }
 };
 parser.parseInput("<html><script><script>alert('Error')</script></html>", "http://example.com");
 List<ParseError> actualErrors = parser.getErrors();
 assertEquals("Unexpected character '<' - was expecting 'E' (char 61)", actualErrors.get(0).getMessage());
 assertEquals(1, actualErrors.size());
 }

 @Test
 public void testParse_NormalCase() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals("Test Page", doc.title().text());
 assertEquals("Hello World", doc.body().children().get(0).text());
 assertEquals("<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>", doc.html());
 }

 @Test
 public void testParse_ErrorTrackingDisabled() {
 String html = "<html><script><script>alert('Error')</script></html>";
 List<ParseError> errors = new ArrayList<>();
 Document doc = Parser.parse(html, "http://example.com", errors);
 assertFalse("Expected no errors, but got " + errors.size() + " errors", !errors.isEmpty());
 for (ParseError error : errors) {
 assertEquals("Unexpected character '<' - was expecting 'E' (char 61)", error.getMessage());
 }
 }

 @Test
 public void testParse_ErrorTrackingEnabled() {
 String html = "<html><script><script>alert('Error')</script></html>";
 Document doc = htmlParser.parse(html, "http://example.com");
 List<ParseError> errors = doc.errors();
 assertEquals(1, errors.size());
 assertEquals("Unexpected character '<' - was expecting 'E' (char 61)", errors.get(0).getMessage());
 }

 @Test
 public void testParse_BaseUri() {
 String html = "<img src=\"image.jpg\" alt=\"Image\">";
 Document doc = Parser.parse(html, "http://example.com");
 Element img = doc.select("img").first();
 assertEquals("image.jpg", img.attr("src"));
 img = doc.select("img[src='image.jpg']").first();
 assertEquals("Image", img.attr("alt"));
 }

 @Test
 public void testParseFragment() {
 String fragment = "<ul><li>Item 1</li><li>Item 2</li></ul>";
 List<Node> nodes = Parser.parseFragment(fragment, null, "http://example.com");
 assertEquals(2, nodes.size());
 Element ul = (Element) nodes.get(0);
 List<Element> listItems = ul.children();
 assertEquals("Item 1", listItems.get(0).text());
 assertEquals("Item 2", listItems.get(1).text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 List<Node> nodes = doc.body().children();
 assertEquals(2, nodes.size());
 Element p1 = (Element) nodes.get(0);
 assertEquals("Paragraph 1", p1.text());
 Element p2 = (Element) nodes.get(1);
 assertEquals("Paragraph 2", p2.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 assertEquals("<html><body><p>Paragraph 1</p><p>Paragraph 2</p></body></html>", doc.html());
 }
}