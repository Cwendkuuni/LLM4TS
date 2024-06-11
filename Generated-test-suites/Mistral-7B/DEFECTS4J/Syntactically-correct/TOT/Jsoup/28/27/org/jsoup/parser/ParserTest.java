package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TreeBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInputWithValidHtml() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");
 assertNotNull(doc);
 assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<img src=\"image.jpg\" alt=\"Image\">";
 Document doc = parser.parseInput(html, "http://example.com/index.html");
 Element img = doc.select("img").first();
 assertEquals("http://example.com/image.jpg", img.absUrl("src"));
 }

 @Test
 public void testParseInputWithTrackErrors() {
 String html = "<script><!-- Evil Script --></script>"; // deliberate XSS attack
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertTrue(errors.size() > 0);
 assertEquals("Unexpected token <script>", errors.get(0).message());
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test
 public void testParseInputWithEmptyHtml() {
 String html = "";
 Document doc = parser.parseInput(html, "http://example.com");
 assertNotNull(doc);
 assertEquals(0, doc.childNodes().size());
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
 public void testGetErrors() {
 String html = "<script><!-- Evil Script --></script>"; // deliberate XSS attack
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertNotNull(errors);
 assertEquals(1, errors.size());
 }

 @Test
 public void testParseStaticFunctions() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html);
 assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<ul><li>Apple</li><li>Banana</li></ul>";
 Element context = new Element("div");
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 Element ul = (Element) nodes.get(0);
 assertEquals(2, ul.childNodes().size());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<ul><li>Apple</li><li>Banana</li></ul>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals(2, doc.childNodes().size());
 Element body = doc.body();
 assertEquals(2, body.childNodes().size());
 List<Node> nodes = body.childNodes();
 assertEquals("Apple", nodes.get(0).text());
 assertEquals("Banana", nodes.get(1).text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<ul><li>Apple</li><li>Banana</li></ul>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 assertEquals("Apple", doc.select("li").get(0).text());
 assertEquals("Banana", doc.select("li").get(1).text());
 }
}