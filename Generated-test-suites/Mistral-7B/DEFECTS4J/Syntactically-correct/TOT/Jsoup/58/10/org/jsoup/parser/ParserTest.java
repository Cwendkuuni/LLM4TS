import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;

import java.util.List;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;
 private ParseSettings settings;

 @Before
 public void setup() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 settings = treeBuilder.defaultSettings();
 }

 @Test
 public void testParseInput() {
 String html = "<html><head></head><body><h1>Test</h1></body></html>";
 Document document = parser.parseInput(html, "http://example.com");
 assertEquals(html, document.html());
 assertTrue(document.body().hasText());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body onerror='throw new Error(\"Test\")'><script>1+1;</script></body></html>";
 List<ParseError> errors = parser.parseInput(html, "http://example.com").errors();
 assertEquals(1, errors.size());
 assertEquals("Error: onerror", errors.get(0).message());
 }

 @Test
 public void testParseInputWithTreeBuilder() {
 TreeBuilder treeBuilder = new HtmlTreeBuilder();
 Parser parser = new Parser(treeBuilder);
 String html = "<html><head></head><body><h1>Test</h1></body></html>";
 Document document = parser.parseInput(html, "http://example.com");
 assertEquals(html, document.html());
 assertTrue(document.body().hasText());
 assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testParseInputWithDefaultSettings() {
 Parser parser = new Parser(null);
 Document document = parser.parseInput("<html></html>", "http://example.com");
 assertTrue(document.tags().isEmpty());
 }

 @Test
 public void testParseInputWithCustomSettings() {
 ParseSettings settings = new ParseSettings.Builder().build();
 settings.setTrimWhitespace(false);
 Parser parser = new Parser(null).settings(settings);
 String html = "<html><head><title>Test</title></head><body></body></html>";
 Document document = parser.parseInput(html, "http://example.com");
 assertEquals("", document.title());
 }

 @Test
 public void testParseFragment() {
 String html = "<html><body><h1>Test</h1></body></html>";
 List<Node> nodes = Parser.parseFragment("<h1>Test</h1>", null, "http://example.com");
 Node node = nodes.get(0);
 assertEquals("h1", node.tagName());
 assertEquals("Test", node.text());
 }

 @Test
 public void testParseFragmentWithErrors() {
 String html = "<html><body onerror='throw new Error(\"Test\")'><script>1+1;</script></body></html>";
 List<Node> nodes = Parser.parseFragment("<h1>Test</h1>", null, "http://example.com");
 List<ParseError> errors = nodes.get(0).errors();
 assertEquals(1, errors.size());
 assertEquals("Error: onerror", errors.get(0).message());
 }

 @Test
 public void testParseXmlFragment() {
 String xml = "<xml><tag>Test</tag></xml>";
 List<Node> nodes = Parser.parseXmlFragment(xml, "http://example.com");
 Node node = nodes.get(0);
 assertEquals("tag", node.tagName());
 assertEquals("Test", node.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Test</h1>";
 Document document = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals(bodyHtml, document.body().html());
 }

 @Test
 public void testUnescapeEntities() {
 String string = "&lt;test&gt;&amp;amp;a&apos;s &quot;special&quot; characters &gt;";
 String unescaped = Parser.unescapeEntities(string, false);
 assertEquals("<test>&amp;a's &quot;special&quot; characters >", unescaped);
 }
}