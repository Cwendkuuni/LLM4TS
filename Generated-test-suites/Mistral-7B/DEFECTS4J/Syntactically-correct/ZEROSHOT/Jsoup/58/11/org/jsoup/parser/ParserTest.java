package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setup() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><p>Hello World</p></body></html>";
 String baseUri = "http://example.com/";
 List<ParseError> errors = parser.getErrors();
 Document document = parser.parseInput(html, baseUri);
 Assert.assertNotNull(document);
 Assert.assertEquals(0, errors.size());
 Assert.assertEquals("Hello World", document.body().text());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><script>alert('XSS');</script></body></html>";
 String baseUri = "http://example.com/";
 List<ParseError> errors = parser.getErrors();
 Document document = parser.parseInput(html, baseUri);
 Assert.assertNotNull(document);
 Assert.assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 Assert.assertEquals(ParseError.MalformedTag, error.getErrorCode());
 Assert.assertEquals("malformed tag <script>", error.getMessage());
 }

 @Test
 public void testParseInputWithMaxErrors() {
 String html = "<html><body><script>alert('XSS');</script><script>alert('XSS');</script></body></html>";
 String baseUri = "http://example.com/";
 parser.setTrackErrors(2);
 List<ParseError> errors = parser.getErrors();
 Document document = parser.parseInput(html, baseUri);
 Assert.assertNotNull(document);
 Assert.assertEquals(2, errors.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 String baseUri = "http://example.com/";
 parser.parseInput(null, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullBaseUri() {
 String html = "<html><body><p>Hello World</p></body></html>";
 parser.parseInput(html, null);
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
 parser.setTrackErrors(2);
 Assert.assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithMaxErrorsGreaterThanZero() {
 parser.setTrackErrors(3);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithMaxErrorsZero() {
 parser.setTrackErrors(0);
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 String html = "<html><body><script>alert('XSS');</script></body></html>";
 String baseUri = "http://example.com/";
 parser.setTrackErrors(1);
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testGetErrorsWhenNoErrors() {
 String html = "<html><body><p>Hello World</p></body></html>";
 String baseUri = "http://example.com/";
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 Assert.assertEquals(0, errors.size());
 }

 @Test
 public void testSettings() {
 ParseSettings settings = new ParseSettings();
 parser.settings(settings);
 Assert.assertEquals(settings, parser.settings());
 }

 @Test
 public void testParse() {
 String html = "<html><body><p>Hello World</p></body></html>";
 String baseUri = "http://example.com/";
 Document document = Parser.parse(html, baseUri);
 Assert.assertNotNull(document);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<p>Hello World</p>";
 Element context = new Element("html");
 String baseUri = "http://example.com/";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<xml><tag>Hello World</tag></xml>";
 String baseUri = "http://example.com/";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><p>Hello World</p></body>";
 String baseUri = "http://example.com/";
 Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
 Assert.assertNotNull(document);
 }

 @Test
 public void testUnescapeEntities() {
 String string = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
 String unescapedString = Parser.unescapeEntities(string, false);
 Assert.assertEquals("<p>Hello & World</p>", unescapedString);
 }

 @Test
 public void testUnescapeEntitiesInAttribute() {
 String string = "<input type=\"text\" value=\"&lt;script&gt;alert('XSS');&lt;/script&gt;\" />";
 String unescapedString = Parser.unescapeEntities(string, true);
 Assert.assertEquals("<input type=\"text\" value=\"<script>alert('XSS');</script>\">", unescapedString);
 }

 @Test
 public void testParseBodyFragmentRelaxedDeprecated() {
 String bodyHtml = "<body><p>Hello World</p></body>";
 String baseUri = "http://example.com/";
 Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
 Assert.assertNotNull(document);
 }
}