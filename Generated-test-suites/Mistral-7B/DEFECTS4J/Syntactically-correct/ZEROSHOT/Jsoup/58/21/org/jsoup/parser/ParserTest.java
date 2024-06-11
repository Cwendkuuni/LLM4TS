import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import java.util.List;
import java.util.ArrayList;

public class ParserTest {

 private Parser parser;
 private Document document;
 private List<ParseError> errors;

 @Before
 public void setup() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 @Test
 public void testParseInput() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 document = parser.parseInput(html, baseUri);
 assertNotNull(document);
 assertNotNull(document.head());
 assertNotNull(document.body());
 assertNotNull(document.title());
 assertNull(document.title().text());
 assertEquals("Hello World", document.getElementsByTag("h1").text());
 }

 @Test(expected = IllegalStateException.class)
 public void testParseInputWithInvalidHtml() {
 String html = "<html<tag";
 String baseUri = "http://example.com";
 parser.parseInput(html, baseUri);
 fail("Expected IllegalStateException");
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 String invalidHtml = "<html<tag";
 parser.setTrackErrors(1);
 errors = parser.parseInput(invalidHtml, "http://example.com");
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Error parsing start tag : tag", error.getMessage());
 }

 @Test
 public void testSettings() {
 ParseSettings settings = parser.settings();
 assertEquals(ParseSettings.defaultSettings(), settings);
 }

 @Test
 public void testParse() {
 document = Parser.parse("<html><head></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 assertNotNull(document);
 assertNotNull(document.head());
 assertNotNull(document.body());
 assertNotNull(document.title());
 assertNull(document.title().text());
 assertEquals("Hello World", document.getElementsByTag("h1").text());
 }

 @Test
 public void testParseFragment() {
 Element context = Jsoup.parse("<p>Context</p>").body();
 String fragmentHtml = "<h1>Fragment</h1>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 assertEquals(2, nodes.size());
 Node h1 = nodes.get(0);
 assertEquals("Fragment", h1.text());
 Element p = context;
 p.appendChild(h1);
 }

 @Test
 public void testParseXmlFragment() {
 String xml = "<root><tag>Text</tag></root>";
 List<Node> nodes = Parser.parseXmlFragment(xml, "http://example.com");
 assertEquals(2, nodes.size());
 Node tag = nodes.get(0);
 assertEquals("Text", tag.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Body Fragment</h1>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertNotNull(doc);
 assertEquals(1, doc.numChildren());
 Element h1 = doc.getChild(0);
 assertEquals("Body Fragment", h1.text());
 }
}