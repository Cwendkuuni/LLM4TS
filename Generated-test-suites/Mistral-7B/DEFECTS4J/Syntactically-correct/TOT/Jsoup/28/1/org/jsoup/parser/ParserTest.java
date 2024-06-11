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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
 public void testParseInput() {
 String html = "<html><body><p>Hello World</p></body></html>";
 Document doc = parser.parseInput(html, "");
 assertNotNull(doc);
 assertEquals("<html><body><p>Hello World</p></body></html>", doc.html());
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<img src=\"image.jpg\" />";
 String baseUri = "http://example.com";
 Document doc = Parser.parse(html, baseUri);
 assertNotNull(doc);
 assertNull(doc.body()); // no body in this case
 Element img = doc.selectFirst("img");
 assertEquals("image.jpg", img.attr("src"));
 }

 @Test
 public void testParseInputWithErrorTracking() {
 String html = "<script><script"; // script tag cannot be nested
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "");
 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 assertEquals("Encountered two <SCRIPT> tags at line 1 column 18. Was expecting: <!?>*", errors.get(0).getMessage());
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parser = new Parser(new XmlTreeBuilder());
 Element root = parser.parseInput("<root><element /></root>", "").body();
 assertEquals("element", root.nodeName());
 }

 @Test
 public void testIsTrackErrors() {
 Parser parser = new Parser(new TreeBuilder());
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithMaxErrors() {
 Parser parser = new Parser(new TreeBuilder());
 parser.setTrackErrors(3);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithZero() {
 Parser parser = new Parser(new TreeBuilder());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 Parser parser = new Parser(new TreeBuilder());
 parser.setTrackErrors(1);
 parser.parseInput("<script><script", "");
 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 assertEquals("Encountered two <SCRIPT> tags at line 1 column 18. Was expecting: <!?>*", errors.get(0).getMessage());
 }
}