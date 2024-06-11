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

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setup() {
 parser = new Parser("", "", false);
 doc = parser.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
 }

 @Test
 public void testParseBasicHtml() {
 // Test parsing a basic HTML document
 Element root = doc.head();
 assertEquals("Test", root.child(0).text());
 Element h1 = doc.body().child(0);
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseEmptyHtml() {
 // Test parsing an empty HTML document
 doc = parser.parse("");
 assertNull(doc.head());
 assertNull(doc.body());
 }

 @Test
 public void testParseInvalidHtml() {
 // Test parsing invalid HTML and checking for errors
 doc = parser.parse("<html><body><script>alert('Hello');</script>");
 Element script = doc.body().child(0);
 assertEquals("script", script.tagName());
 assertNotNull(script.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 // Test parsing null HTML and checking for error
 parser.parse(null, "", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyBaseUri() {
 // Test parsing an empty base URI and checking for error
 parser.parse("<html></html>", "", false);
 }

 @Test
 public void testParseBodyFragment() {
 // Test parsing a body fragment
 Document fragmentDoc = Parser.parseBodyFragment("<p>Hello World</p>", "");
 Element p = fragmentDoc.body().child(0);
 assertEquals("Hello World", p.text());
 }

 @Test
 public void testParseRelaxedBodyFragment() {
 // Test parsing a body fragment with relaxed parsing
 Document fragmentDoc = Parser.parseBodyFragment("<p>Hello<img src=\"image.jpg\">World</p>", "");
 Element p = fragmentDoc.body().child(0);
 assertEquals("Hello<img src=\"image.jpg\">World", p.text());
 }

 @Test
 public void testParseComment() {
 // Test parsing comments
 doc = parser.parse("<html><!-- This is a comment --></html>");
 Element comment = doc.body().child(0);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDeclaration() {
 // Test parsing XML declarations
 doc = parser.parse("<html xmlns:foo=\"http://foo.com\" xml:lang=\"en\">Test</html>");
 XmlDeclaration decl = doc.head().child(0);
 assertEquals("http://foo.com", decl.xmlns("foo"));
 assertEquals("en", decl.xmlLang());
 }

 @Test
 public void testParseEndTag() {
 // Test parsing end tags
 doc = parser.parse("<html><body><div><p>Hello World</p></div></body></html>");
 Element p = doc.body().child(0).child(0);
 assertEquals("p", p.tagName());
 }

 @Test
 public void testParseStartTag() {
 // Test parsing start tags
 doc = parser.parse("<html><body><p id=\"test\" class=\"test\"><a href=\"http://example.com\">Link</a></p></body></html>");
 Element p = doc.body().child(0);
 assertEquals("p", p.tagName());
 assertEquals("test", p.attr("id"));
 assertEquals("test", p.attr("class"));
 Element a = p.child(0);
 assertEquals("a", a.tagName());
 assertEquals("http://example.com", a.attr("href"));
 }

 @Test
 public void testParseAttribute() {
 // Test parsing attributes
 doc = parser.parse("<html><body><p id=\"test\" class=\"test\" title=\"Test Title\"><a href=\"http://example.com\" rel=\"external\"></a></p></body></html>");
 Element p = doc.body().child(0);
 assertEquals("test", p.attr("id"));
 assertEquals("Test Title", p.attr("title"));
 assertEquals("external", p.child(0).attr("rel"));
 }

 @Test
 public void testParseTextNode() {
 // Test parsing text nodes
 doc = parser.parse("<html><body><p>Hello World</p></body></html>");
 TextNode textNode = (TextNode) doc.body().child(0).child(0);
 assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testParseCdata() {
 // Test parsing CDATA sections
 doc = parser.parse("<html><body><![CDATA[Hello World]]></body></html>");
 TextNode textNode = (TextNode) doc.body().child(0);
 assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testParseBaseTag() {
 // Test parsing base tags and updating the base URI
 doc = parser.parse("<html><body><base href=\"/foo\"/><p>Hello World</p></body></html>");
 assertEquals("/foo", doc.baseUri());
 }
}