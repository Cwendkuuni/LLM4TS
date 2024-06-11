import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseError;
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
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");
 assertNotNull(doc);
 Element h1 = doc.selectFirst("h1");
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseInputWithTrackErrors() {
 String html = "<html><body><script>alert('This is a script!');</script></body></html>";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertTrue(errors.size() > 0);
 assertEquals("Encountered a script tag", errors.get(0).getMessage());
 assertNull(doc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput("<html></html>", null);
 }

 @Test
 public void testParseInputEmptyString() {
 Document doc = parser.parseInput("", "http://example.com");
 assertNotNull(doc);
 assertEquals(0, doc.childNodeSize());
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<img src=\"image.jpg\" alt=\"Image\">";
 Document doc = parser.parseInput(html, "http://example.com/index.html");
 String imageUrl = doc.selectFirst("img").attr("src");
 assertEquals("image.jpg", imageUrl);
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parser1 = new Parser(new HtmlTreeBuilder());
 Parser parser2 = new Parser(new XmlTreeBuilder());
 assertEquals(HtmlTreeBuilder.class, parser1.getTreeBuilder().getClass());
 assertEquals(XmlTreeBuilder.class, parser2.getTreeBuilder().getClass());
 parser1.setTreeBuilder(new XmlTreeBuilder());
 assertEquals(XmlTreeBuilder.class, parser1.getTreeBuilder().getClass());
 }

 @Test
 public void testIsTrackErrors() {
 assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsZero() {
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsNegative() {
 parser.setTrackErrors(-1);
 assertFalse(parser.isTrackErrors());
 }
}