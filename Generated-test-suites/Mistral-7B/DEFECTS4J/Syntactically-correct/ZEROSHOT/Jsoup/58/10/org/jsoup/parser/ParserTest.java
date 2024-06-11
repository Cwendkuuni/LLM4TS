import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Test;
import org.junit.Assert;

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
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 Assert.assertEquals(0, errors.size());
 Document document = parser.parseInput(html, baseUri);
 Element h1 = document.select("h1").first();
 Assert.assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseInputWithTrackErrors() {
 String html = "<html><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(1);
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Encountered an invalid token: script", errors.get(0).getMessage());
 Document document = parser.parseInput(html, baseUri);
 Assert.assertNull(document.body().child(1).nodeName()); // the script tag should not have been parsed
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
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testParseWithSettings() {
 ParseSettings settings = new ParseSettings.Builder().build();
 Document document = Parser.parse( "<html><body><h1>Hello World</h1></body></html>", "http://example.com", settings );
 Element h1 = document.select("h1").first();
 Assert.assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<ul><li>item 1</li><li>item 2</li></ul>";
 Element context = new Element("div");
 Document document = Jsoup.parse("<div></div>");
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 Element ul = (Element) nodes.get(0);
 List<Element> listItems = ul.children();
 Assert.assertEquals(2, listItems.size());
 Assert.assertEquals("item 1", listItems.get(0).text());
 Assert.assertEquals("item 2", listItems.get(1).text());
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<person><name>John Doe</name></person>";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, "http://example.com");
 Element person = (Element) nodes.get(0);
 Element name = (Element) person.child(0);
 Assert.assertEquals("John Doe", name.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<html><body><h1>Hello World</h1></body></html>";
 Document document = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Element h1 = document.select("h1").first();
 Assert.assertEquals("Hello World", h1.text());
 }

 @Test
 public void testUnescapeEntities() {
 String string = "&lt;p&gt;<i>Hello &amp; World!</i>&gt;";
 String unescaped = Parser.unescapeEntities(string, false);
 Assert.assertEquals("<p><i>Hello & World!</i></p>", unescaped);
 }
}