package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.ParseErrorList;
import org.jsoup.nodes.TreeBuilder;
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
 public void testParseInput() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
 String baseUri = "http://example.com";
 Document doc = parser.parseInput(html, baseUri);
 assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 assertEquals("Hello", doc.body().child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, baseUri);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput(html, null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetTreeBuilder() {
 assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(5);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> errors = parser.getErrors();
 assertNotNull(errors);
 assertEquals(0, errors.size());
 }

 @Test
 public void testParse() {
 Document doc = Parser.parse(html, baseUri);
 assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 assertEquals("Hello", doc.body().child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 Parser.parse(null, baseUri);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
 Parser.parse(html, null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<h1>Hello</h1>";
 Element context = new Element("div");
 Document doc = Document.createShell(baseUri);
 doc.body().appendChild(context);
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
 assertEquals(1, nodes.size());
 assertEquals(Element.class, nodes.get(0).getClass());
 assertEquals("Hello", nodes.get(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullFragmentHtml() {
 Parser.parseFragment(null, context, baseUri);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullContext() {
 Parser.parseFragment(fragmentHtml, null, baseUri);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullBaseUri() {
 Parser.parseFragment(fragmentHtml, context, null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = Parser.parseBodyFragment(fragmentHtml, baseUri);
 assertEquals("div", doc.head().tagName());
 assertEquals(1, doc.body().child(0).childCount());
 assertEquals("Hello", doc.body().child(0).child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullFragmentHtml() {
 Parser.parseBodyFragment(null, baseUri);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullBaseUri() {
 Parser.parseBodyFragment(fragmentHtml, null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Document doc = Parser.parseBodyFragmentRelaxed(fragmentHtml, baseUri);
 assertEquals("div", doc.head().tagName());
 assertEquals(1, doc.body().child(0).childCount());
 assertEquals("Hello", doc.body().child(0).child(0).text());
 }

 @Test(expected = DeprecationException.class)
 public void testParseBodyFragmentRelaxedDeprecated() {
 Parser.parseBodyFragmentRelaxed(fragmentHtml, baseUri);
 fail("Expected DeprecationException");
 }
}