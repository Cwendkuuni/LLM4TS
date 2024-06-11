import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlDeclaration;
import org.jsoup.parser.Tag;
import org.jsoup.parser.Attributes;
import org.jsoup.parser.Attribute;
import org.jsoup.parser.TextNode;
import org.jsoup.parser.Comment;
import org.jsoup.parser.DataNode;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.Token;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://test.com");
 doc = parser.parse();
 }

 @Test
 public void testParseHtml() {
 // Test parse HTML into a Document
 assertEquals(htmlTag, doc.rootElement().tag());
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseBodyFragment() {
 // Test parse a fragment of HTML into the body of a Document
 String bodyHtml = "<body><p>This is a body fragment.</p></body>";
 doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
 assertEquals("<html><body></body></html>", doc.html().outerHtml());
 assertEquals("This is a body fragment.", doc.body().text());
 }

 @Test
 public void testParseComment() {
 // Test parse a comment
 parser = new Parser("<html><!-- This is a comment --></html>", "http://test.com");
 doc = parser.parse();
 Element comment = doc.select("html > comment()").first();
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 // Test parse an XML declaration
 parser = new Parser("<html xmlns:foo=\"http://example.com\" xml:version=\"1.0\">", "http://test.com");
 doc = parser.parse();
 XmlDeclaration decl = (XmlDeclaration) doc.head().child(0);
 assertEquals("http://example.com", decl.getNamespace());
 assertEquals("1.0", decl.getVersion());
 }

 @Test
 public void testParseEndTag() {
 // Test parse an end tag
 parser = new Parser("<html><body><p>Hello<p></body></html>", "http://test.com");
 doc = parser.parse();
 assertEquals(2, doc.body().childNodeSize());
 assertEquals("Hello", doc.body().child(0).text());
 assertEquals(null, doc.body().child(1).text());
 }

 @Test
 public void testParseStartTag() {
 // Test parse a start tag
 parser = new Parser("<html><body><h1>Test</h1></body></html>", "http://test.com");
 Element h1 = doc.select("h1").first();
 assertEquals("h1", h1.tag().name());
 assertEquals("Test", h1.text());
 }

 @Test
 public void testParseAttribute() {
 // Test parse an attribute
 parser = new Parser("<html><body><img src=\"image.png\" alt=\"Image\"/></body></html>", "http://test.com");
 Element img = doc.select("img").first();
 Attributes attributes = img.attributes();
 assertEquals("image.png", attributes.get("src"));
 assertEquals("Image", attributes.get("alt"));
 assertNull(attributes.get("title"));
 }

 @Test
 public void testParseTextNode() {
 // Test parse a text node
 assertEquals("", doc.body().child(0).text());
 parser = new Parser("<html><body><p>This is a text node.</p></body></html>", "http://test.com");
 TextNode textNode = (TextNode) doc.body().child(0).child(0);
 assertEquals("This is a text node.", textNode.text());
 }

 @Test
 public void testParseCdata() {
 // Test parse a CDATA section
 parser = new Parser("<html><body><![CDATA[This is a CDATA section]]></body></html>", "http://test.com");
 TextNode textNode = (TextNode) doc.body().child(0).child(0);
 assertEquals("This is a CDATA section", textNode.text());
 }
}