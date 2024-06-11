import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setup() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 @Test
 public void testParseInput() {
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 doc = parser.parseInput(html, "http://example.com");
 assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseInputWithTrackErrors() {
 String html = "<html><body><script>alert('Error')</script></body></html>";
 parser = new Parser(new HtmlTreeBuilder()).setTrackErrors(1);
 doc = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 assertTrue(errors.get(0) instanceof ScriptError);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullBaseUri() {
 parser.parseInput("<html></html>", null);
 }

 @Test
 public void testParseInputWithEmptyHtml() {
 parser.parseInput("", "http://example.com");
 assertEquals(Document.EMPTY_DOCUMENT, parser.getTreeBuilder().parse(parser.getErrors().toString(), "http://example.com", parser.getErrors()));
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(5);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<ul><li>Item 1</li><li>Item 2</li></ul>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, "http://example.com");
 Element ul = (Element) nodes.get(0);
 assertEquals(2, ul.childNodes().size());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><p>Item 1</p><p>Item 2</p></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals("Item 1", doc.body().getElementsByTag("p").get(0).text());
 assertEquals("Item 2", doc.body().getElementsByTag("p").get(1).text());
 }

 @Test
 public void testUnescapeHtmlEntities() {
 String escapedString = "&lt;p&gt;Hello &amp; World!&lt;/p&gt;";
 String unescapedString = Parser.unescapeEntities(escapedString);
 assertEquals("<p>Hello & World!</p>", unescapedString);
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><p>Item 1</p><p>Item 2</p></body>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 assertEquals("Item 1", doc.body().getElementsByTag("p").get(0).text());
 assertEquals("Item 2", doc.body().getElementsByTag("p").get(1).text());
 }
}