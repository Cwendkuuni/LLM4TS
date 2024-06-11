import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 parser = new Parser("", "http://example.com", false);
 doc = parser.parse("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
 }

 @Test
 public void testParseHtml() {
 // Positive test: parse valid HTML and check structure and contents
 assertEquals(Tag.HTML, parser.tag());
 assertEquals(Tag.HTML, doc.tagName());
 assertEquals("http://example.com", doc.baseUri());
 assertNotNull(doc.head());
 assertNotNull(doc.title());
 assertEquals("Test", doc.title().text());
 assertNotNull(doc.body());
 assertNotNull(doc.title());
 assertEquals(1, doc.numChildren());
 assertEquals(Tag.H1, doc.body().tagName());
 assertEquals("Test", doc.body().text());
 }

 @Test
 public void testParseBodyFragment() {
 // Positive test: parse body fragment and check structure and contents
 parser = new Parser("<html><body><h1>Test</h1></body></html>", "http://example.com", true);
 doc = parser.parse();
 assertEquals(Tag.HTML, doc.tagName());
 assertEquals("http://example.com", doc.baseUri());
 assertNotNull(doc.body());
 assertNotNull(doc.body().tagName());
 assertEquals(Tag.H1, doc.body().tagName());
 assertEquals("Test", doc.body().text());
 }

 @Test
 public void testParseComment() {
 // Test comment parsing
 parser = new Parser("<html><!-- This is a comment --></html>");
 doc = parser.parse();
 assertNotNull(doc.body());
 Element comment = doc.body().child(0);
 assertEquals(Comment.class, comment.getClass());
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 // Test XML declaration parsing
 parser = new Parser("<html xmlns:foo=\"http://foo.com\"><head></head><body></body></html>");
 doc = parser.parse();
 assertEquals("http://foo.com", doc.xmlNamespaceURI());
 }

 @Test
 public void testParseEndTag() {
 // Test end tag parsing
 parser = new Parser("<html><body><p></p></body></html>");
 doc = parser.parse();
 assertEquals(1, doc.numChildren());
 assertEquals(Tag.P, doc.body().tagName());
 assertNull(doc.body().child(0));
 }

 @Test
 public void testParseStartTag() {
 // Test start tag parsing with attributes
 parser = new Parser("<html><head><title attr=\"value\"><meta charset=\"UTF-8\"/></title></head></html>");
 doc = parser.parse();
 assertEquals(1, doc.numChildren());
 assertEquals(Tag.HTML, doc.tagName());
 assertEquals("http://example.com", doc.baseUri());
 assertNotNull(doc.head());
 Element title = doc.head().child(0);
 assertEquals(Tag.TITLE, title.tagName());
 assertEquals("value", title.attr("attr"));
 assertEquals("UTF-8", doc.head().getElementsByTag("meta").get(0).attr("charset"));
 }

 @Test
 public void testParseTextNode() {
 // Test text node parsing
 parser = new Parser("<html><body>Test</body></html>");
 doc = parser.parse();
 assertEquals(1, doc.numChildren());
 assertEquals(Tag.BODY, doc.tagName());
 assertEquals("Test", doc.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml() {
 // Test parsing invalid HTML and check for IllegalArgumentException
 parser = new Parser("<html>");
 doc = parser.parse();
 assertNull(doc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingEndTag() {
 // Test parsing with missing end tag and check for IllegalArgumentException
 parser = new Parser("<html><body><p>Test</p><div>");
 doc = parser.parse();
 assertNotNull(doc.body());
 assertEquals(1, doc.numChildren());
 assertEquals(Tag.P, doc.body().tagName());
 assertEquals("Test", doc.body().text());
 assertNull(doc.body().child(1));
 assertTrue(doc.body().hasAttr("unclosedTag")); // Test unclosed tag attribute
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTagName() {
 // Test parsing with invalid tag name and check for IllegalArgumentException
 parser = new Parser("<html><body><invalid></body></html>");
 doc = parser.parse();
 assertNull(doc);
 }
}