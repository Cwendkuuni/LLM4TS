package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.ParseErrorList;
import org.jsoup.nodes.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

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
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "");

 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullBaseUri() {
 parser.parseInput("", null);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsAndParseInput() {
 parser.setTrackErrors(1);
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals(ParseError.MalformedMarkup, errors.get(0).getErrorCode());
 }

 @Test
 public void testSetTrackErrorsAndParseInputWithNoErrors() {
 parser.setTrackErrors(1);
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(0, errors.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetTrackErrorsAndParseInputWithMaxErrorsReached() {
 parser.setTrackErrors(1);
 String html = "<html><body>"; // incomplete html
 Document doc = parser.parseInput(html, "");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 parser.setTrackErrors(1); // try to set track errors again
 }

 @Test
 public void testParse() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "");

 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullHtml() {
 Parser.parse(null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 Parser.parse("", null);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<ul><li>Item 1</li><li>Item 2</li></ul>";
 Element context = new Element("div");
 Document doc = Document.create();
 doc.appendChild(context);
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "");

 Assert.assertEquals(2, nodes.size());
 Assert.assertTrue(nodes.get(0) instanceof Element);
 Assert.assertTrue(nodes.get(1) instanceof Element);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullFragmentHtml() {
 Parser.parseFragment(null, null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullContext() {
 Parser.parseFragment("", null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullBaseUri() {
 Parser.parseFragment("", null, null);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<ul><li>Item 1</li><li>Item 2</li></ul>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "");

 Assert.assertNotNull(doc);
 List<Element> list = doc.select("ul");
 Assert.assertEquals(1, list.size());
 List<Element> liList = list.get(0).children();
 Assert.assertEquals(2, liList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentWithNullBodyHtml() {
 Parser.parseBodyFragment(null, "");
 }

 @Test
 public void testParseBodyFragmentWithNullBaseUri() {
 Document doc = Document.createShell("");
 doc.body().text("Hello World");
 Parser.parseBodyFragment("Hello World", "");
 Assert.assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testUnescapeHtmlEntities() {
 String input = "&lt;p&gt;<b&gt;Hello &amp; World&lt;/b&gt;&lt;/p&gt;";
 String output = Parser.unescapeHtmlEntities(input);
 Assert.assertEquals("<p><b>Hello & World</b></p>", output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeHtmlEntitiesWithNullString() {
 Parser.unescapeHtmlEntities(null);
 }

 @Test
 public void testHtmlParser() {
 Document doc = Parser.htmlParser().parseInput("<html><body><h1>Hello World</h1></body></html>", "");
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testXmlParser() {
 String xml = "<root><child/></root>";
 Document doc = Parser.xmlParser().parseInput(xml, "");
 List<Element> elements = doc.select("root");
 Assert.assertEquals(1, elements.size());
 }
}