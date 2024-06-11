import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
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
 String html = "<html><head></head><body><h1>Test</h1></body></html>";
 String baseUri = "https://www.example.com/";
 Document doc = parser.parseInput(html, baseUri);
 assertNotNull(doc);
 assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.html());
 }

 @Test
 public void testParseInputWithTrackingErrors() {
 String html = "<html><head><script>alert('foo')</script></head><body></body></html>";
 String baseUri = "https://www.example.com/";
 parser.setTrackErrors(1);
 List<ParseError> errors = parser.getErrors();
 Document doc = parser.parseInput(html, baseUri);
 assertNotNull(doc);
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Unexpected '<', expecting '<!', 'DOCTYPE'", error.getMessage());
 assertNull(doc.body()); // since there was a parse error, the document should not be complete
 }

 @Test
 public void testParseInputWithNoTrackingErrors() {
 String html = "<html><head><script>alert('foo')</script></head><body></body></html>";
 String baseUri = "https://www.example.com/";
 List<ParseError> errors = parser.getErrors(); // should be empty
 Document doc = parser.parseInput(html, baseUri);
 assertNotNull(doc);
 assertEquals("<html><head><script>alert('foo')</script></head><body></body></html>", doc.html());
 }

 @Test
 public void testParseHtml() {
 String html = "<html><head></head><body><h1>Test</h1></body></html>";
 Document doc = Parser.parse(html);
 assertNotNull(doc);
 assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullInput() {
 Parser.parse(null);
 }

 @Test
 public void testParseXmlFragment() {
 String xml = "<xml><root><child/></root></xml>";
 Document doc = Parser.parseXmlFragment(xml, null);
 assertNotNull(doc);
 assertEquals("<xml><root><child/></root></xml>", doc.html());
 }

 @Test
 public void testParseFragment() {
 String html = "<div><p>Test</p></div>";
 Element context = Jsoup.parseBodyFragment("<body></body>").body();
 Document doc = Parser.parseFragment(html, context, null);
 assertNotNull(doc);
 assertEquals("<div><p>Test</p></div>", doc.html());
 }

 @Test
 public void testParseBodyFragment() {
 String html = "<body><p>Test</p></body>";
 Document doc = Parser.parseBodyFragment(html, null);
 assertNotNull(doc);
 assertEquals("<html><head></head><body><p>Test</p></body></html>", doc.html());
 }

 @Test
 public void testParseBodyFragmentWithEmptyBody() {
 String html = "<body></body>";
 Document doc = Parser.parseBodyFragment(html, null);
 assertNotNull(doc);
 assertEquals("<html><head></head><body></body></html>", doc.html());
 }

 @Test
 public void testUnescapeEntities() {
 String string = "&amp; &lt; &gt; &quot; &apos; < &gt;";
 String unescaped = Parser.unescapeEntities(string, false);
 assertEquals("& &< >\" ' <>", unescaped);
 string = "&amp; &lt; &gt; &quot; &apos; &lt; &gt;";
 unescaped = Parser.unescapeEntities(string, true);
 assertEquals("&amp; &lt; &lt; &gt; &quot; &quot; &gt; &gt;", unescaped);
 }
}