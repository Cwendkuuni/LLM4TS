package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

public class ParserTest {

 private static final String VALID_HTML = "<html><head></head><body><h1>Hello World</h1></body></html>";
 private static final String INVALID_HTML = "<html<body>"; // missing tag end
 private static final String XML_FRAGMENT = "<xml><name>John Doe</name></xml>";

 @Test
 public void testParseValidHtml() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 Document doc = parser.parseInput(VALID_HTML, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals(1, doc.childNodes().size());
 Assert.assertEquals("html", doc.nodeName());
 }

 @Test(expected = ParseError.class)
 public void testParseInvalidHtml() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 Document doc = parser.parseInput(INVALID_HTML, "http://example.com");
 Assert.assertNull(doc);
 Assert.assertNotNull(parser.getErrors());
 Assert.assertEquals(1, parser.getErrors().size());
 }

 @Test
 public void testParseXmlFragment() {
 Parser parser = new Parser(new XmlTreeBuilder());
 List<Node> nodes = parser.parseFragment(XML_FRAGMENT, null, "http://example.com");
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 Assert.assertEquals("xml", nodes.get(0).nodeName());
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 Parser newParser = parser.setTreeBuilder(new XmlTreeBuilder());
 Assert.assertNotSame(parser, newParser);
 }

 @Test
 public void testIsTrackErrors() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(5);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 List<ParseError> errors = parser.parseInput(INVALID_HTML, "http://example.com").getErrors();
 Assert.assertNotNull(errors);
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testSettings() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 ParseSettings settings = parser.settings();
 Assert.assertNotNull(settings);
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 Document doc = parser.parseBodyFragment("<body><h1>Hello World</h1></body>", "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals(1, doc.childNodes().size());
 Assert.assertEquals("body", doc.nodeName());
 }

 @Test
 public void testParseInputWithBaseUri() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 Document doc = parser.parseInput("<a href='test.html'><h1>Link</h1></a>", "http://example.com");
 Assert.assertNotNull(doc);
 Element link = doc.selectFirst("a[href='test.html']");
 Assert.assertNotNull(link);
 Assert.assertEquals("test.html", link.attr("abs:href"));
 }

 @Test
 public void testParseFragment() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 List<Node> nodes = parser.parseFragment("<div><p>Hello World</p></div>", null, "http://example.com");
 Assert.assertNotNull(nodes);
 Assert.assertEquals(2, nodes.size());
 Assert.assertEquals("div", nodes.get(0).nodeName());
 }

 @Test
 public void testParseXmlFragmentWithBaseUri() {
 Parser parser = new Parser(new XmlTreeBuilder());
 List<Node> nodes = parser.parseFragment(XML_FRAGMENT, null, "http://example.com");
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 Assert.assertEquals("xml", nodes.get(0).nodeName());
 }
}