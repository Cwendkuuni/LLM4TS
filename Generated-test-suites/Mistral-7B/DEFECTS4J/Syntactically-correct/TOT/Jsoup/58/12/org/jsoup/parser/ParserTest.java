package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setup() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseHtmlDocument() {
 String html = "<html><head></head><body><h1>Test</h1></body></html>";
 Document doc = parser.parseInput(html, null);
 assertNotNull(doc);
 Element body = doc.body();
 Element h1 = body.getChild(0);
 assertEquals("Test", h1.text());
 }

 @Test
 public void testParseXmlDocument() {
 String xml = "<xml><root><child>Test</child></root></xml>";
 Parser xmlParser = new Parser(new XmlTreeBuilder());
 Document doc = xmlParser.parseInput(xml, null);
 assertNotNull(doc);
 Element root = doc.getRootElement();
 Element child = root.getChild(0);
 assertEquals("Test", child.text());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div id='test'><p>Test</p></div>";
 Element context = new Element("html");
 Document doc = parser.parseInput(fragmentHtml, context);
 assertNotNull(doc);
 List<Node> nodes = doc.body().child(0).child(0).children();
 assertEquals(2, nodes.size());
 Element div = (Element) nodes.get(0);
 assertEquals("div", div.tagName());
 Element p = (Element) nodes.get(1);
 assertEquals("p", p.tagName());
 assertEquals("Test", p.text());
 }

 @Test
 public void testParseErrorTracking() {
 String html = "<html><body><script>alert('Test')</script></body></html>";
 Parser parserWithErrors = new Parser(new HtmlTreeBuilder());
 parserWithErrors.setTrackErrors(1);
 List<ParseError> errors = parserWithErrors.parseInput(html, null).errors();
 assertNotNull(errors);
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Encountered an error at line 3, column 1. Message: Syntax error on input", error.getMessage());
 }

 @Test
 public void testParseSettings() {
 ParseSettings settings = parser.settings();
 settings.setOmitScriptAndStyle();
 settings.setOmitXmlDecl();
 Document doc = parser.parseInput("<html><head><script></script></head><body></body></html>", null);
 assertNotNull(doc);
 List<Element> scripts = doc.getElementsByTag("script");
 assertTrue(scripts.isEmpty());
 }

 @Test
 public void testParseInputNull() {
 assertNull(parser.parseInput(null, null));
 }

 @Test
 public void testParseInputEmpty() {
 Document doc = parser.parseInput("", null);
 assertNotNull(doc);
 assertTrue(doc.isEmpty());
 }
}