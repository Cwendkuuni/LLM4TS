import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
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
 String baseUri = "http://example.com";
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 Assert.assertEquals(errors.size(), 0);
 Document doc = parser.parseInput(html, baseUri);
 Assert.assertEquals(doc.title(), "");
 Assert.assertEquals(doc.body().text(), "Hello World");
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><h1>Hello <script>World</script></h1></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 Assert.assertEquals(errors.size(), 1);
 Assert.assertEquals(errors.get(0).line(), 3);
 Assert.assertEquals(errors.get(0).column(), 7);
 Assert.assertEquals(errors.get(0).message(), "Tag Script is not allowed in prolog or document element.");
 Document doc = parser.parseInput(html, baseUri);
 Assert.assertNull(doc);
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
 boolean isTrackErrors = parser.isTrackErrors();
 Assert.assertTrue(isTrackErrors);
 parser.setTrackErrors(0);
 isTrackErrors = parser.isTrackErrors();
 Assert.assertFalse(isTrackErrors);
 }

 @Test
 public void testGetErrors() {
 String html = "<html><body><h1>Hello <script>World</script></h1></body></html>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(1);
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 Assert.assertEquals(errors.size(), 1);
 Assert.assertEquals(errors.get(0).line(), 3);
 Assert.assertEquals(errors.get(0).column(), 7);
 Assert.assertEquals(errors.get(0).message(), "Tag Script is not allowed in prolog or document element.");
 Assert.assertEquals(parser.getErrors().size(), 1);
 Assert.assertEquals(parser.getErrors().get(0), errors.get(0));
 }

 @Test
 public void testSettings() {
 ParseSettings settings = parser.settings();
 Assert.assertNotNull(settings);
 }

 @Test
 public void testParse() {
 Document doc = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 Assert.assertEquals(doc.title(), "");
 Assert.assertEquals(doc.body().text(), "Hello World");
 }

 @Test
 public void testParseFragment() {
 String html = "<div id='container'>Hello <span>World</span></div>";
 Element context = Jsoup.parse("<div id='parent'></div>").body();
 List<Node> nodes = Parser.parseFragment(html, context, "http://example.com");
 Element container = (Element) nodes.get(0);
 Assert.assertEquals(container.id(), "container");
 Element span = (Element) nodes.get(1);
 Assert.assertEquals(span.text(), "World");
 }

 @Test
 public void testParseXmlFragment() {
 String xml = "<xml><element><subElement>Hello World</subElement></element></xml>";
 List<Node> nodes = Parser.parseXmlFragment(xml, "http://example.com");
 Element element = (Element) nodes.get(0);
 Element subElement = (Element) nodes.get(1);
 Assert.assertEquals(subElement.text(), "Hello World");
 }

 @Test
 public void testParseBodyFragment() {
 String html = "<body><h1>Hello World</h1></body>";
 Document doc = Parser.parseBodyFragment(html, "http://example.com");Assert.assertEquals(doc.title(), "");
 Assert.assertEquals(doc.body().text(), "Hello World");
 }

 @Test
 public void testUnescapeEntities() {
 String html = "&lt;p&gt;<a href=&quot;http://example.com&quot;>&amp;amp;amp;lt;a&gt;&amp;lt;/p&gt;";
 String unescaped = Parser.unescapeEntities(html, false);
 Assert.assertEquals(unescaped, "<p><a href=\"http://example.com\">&amp;amp;<a></p>");
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Document doc = Parser.parseBodyFragmentRelaxed("<body><h1>Hello World</h1></body>", "http://example.com");
 Assert.assertEquals(doc.title(), "");
 Assert.assertEquals(doc.body().text(), "Hello World");
 }

}